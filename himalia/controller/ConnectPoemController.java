package himalia.controller;

import himalia.model.*;
import himalia.view.*;

import java.awt.event.*;
import java.util.ArrayList;

/**
 * This is the controller the handles the connecting of poems. 
 * Poems are connected only on top of or at the bottom of another poem.
 * @author saadjei
 *
 */
public class ConnectPoemController extends MouseAdapter{
	Model model;
	RegionPanel panel;
	Board board;
	Poem selectedPoem;
	Word selectedWord;
	
	ProtectedRegion proReg;
	Poem singleWordPoem;
	
	Boolean fromSingleWordPoem=false;
	
	int deltaX, deltaY, originalX,originalY;
	int sourceRegion; //0 => Protected 1=>unprotected
	
	/**
	 * This is the main constructor of the ConnectPoemController.
	 */
	public ConnectPoemController(Model m, RegionPanel panel){
		this.panel = panel;
		this.model = m;
		this.board = m.getBoard();
		this.proReg = board.getProtectedRegion();
	}
	
	/** Register this controller as the current controller **/ 
	public void register() {
		panel.setActiveListener(this);
		panel.setActiveMotionListener(this);
	}
	
	@Override
	public void mousePressed(MouseEvent me) {
		select(me.getX(), me.getY());
		
	}
	@Override
	public void mouseDragged(MouseEvent me) {
		drag(me.getX(), me.getY());
	}
	
	@Override
	public void mouseReleased(MouseEvent me) {
		release(me.getX(), me.getY());
		
	}
	
	/** Separate out this function for testing purposes. */
	protected boolean select(int x, int y) {
	
		Position pos =new Position(x,y,0); 
		// pieces are returned in order of Z coordinate
		if(board.isProtected(pos)){
			selectedPoem = proReg.getIntersectedPoem(x, y);		
			if (selectedPoem == null) { 
				//Check if the intersected point yields a word.
				selectedWord = proReg.findWord(pos);
			}
		}
		
		if(selectedWord != null){
			selectedPoem = proReg.formOneWordPoem(selectedWord);
			fromSingleWordPoem = true;
			selectedWord = null;
		}else{
			fromSingleWordPoem = false;
		}
		
		if (selectedPoem == null){return false;}
		
		// no longer in the board since we are moving it around...
		//board().remove(w);
		originalX = selectedPoem.getPosition().x;
		originalY = selectedPoem.getPosition().y;
			
		// set anchor for smooth moving
		deltaX = x - originalX;
		deltaY = y - originalY;
		
		// paint will happen once moves. This redraws state to prepare for paint
		panel.redraw();
		return true;
	}	
	
	/** Separate out this function for testing purposes. */
	protected boolean drag (int x, int y) {
		int buttonType = 0;
		// no board? no behavior! No dragging of right-mouse buttons...
		if (buttonType == MouseEvent.BUTTON3) { return false; }
		
		if (selectedPoem == null) { return false; }
		
		panel.paintBackground(selectedPoem);
		int oldx = selectedPoem.getPosition().x;
		int oldy = selectedPoem.getPosition().y;
		
		selectedPoem.setPosition(new Position(x - deltaX, y - deltaY,0));
		
		boolean ok = true;
    
		if (board.getProtectedRegion().detectOverlap(selectedPoem)){
			ok = true;
		}else{
			Position topPos = new Position(selectedPoem.getPosition().x,selectedPoem.getPosition().y,0);
			Position downPos = new Position(selectedPoem.getPosition().x,selectedPoem.getPosition().y+ selectedPoem.getHeight(),0);

			ok = (board.isProtected(topPos)
					&& board.isProtected(downPos)); 
		}
		
		if (!ok) {
			selectedPoem.setPosition(new Position(oldx, oldy, 0));
		} else {
			//panel.paintShape(selected);
			panel.repaint();
		}
		
		return ok;
	}
	
	/** Separate out this function for testing purposes. */
	protected boolean release (int x, int y) {
		boolean movePossible=false;
		ArrayList<Poem> res= new ArrayList<Poem>(0);
		Position releasedPos = new Position(x,y,0);
		Poem overlappedPoem = null;
		boolean moveExecuted = false;
				
		if (selectedPoem == null) { return false; }
		
		if (board.isProtected(releasedPos)){ 
			res = proReg.findOverlapedPoems(selectedPoem);
			if (res.size()==1 ){
				overlappedPoem = res.get(0);
				movePossible =true;
			}else{
				overlappedPoem = getOverlappedSingleWordPoem(x,y);
				movePossible = overlappedPoem != null;
			}
			
			if(movePossible){
				//check if an overlap will occur when the connection takes place 
				movePossible = !proReg.detectPossibleOverlap(selectedPoem, overlappedPoem);
			}
		}
		
		if (movePossible){			
			ConnectPoemMove move = new ConnectPoemMove(overlappedPoem, selectedPoem, new Position(originalX,originalY,0), new Position(x-deltaX,y-deltaY,0));
			if (move.execute(board)) {
				board.addUndoMove(move);
				board.clearRedoStacks();
				moveExecuted = true;
			}
		}
		
		if (!moveExecuted){
			cleanupUnexecutedMove(overlappedPoem);
		}
		
		selectedPoem= null;
		fromSingleWordPoem=false;
		
		panel.redraw();
		panel.repaint();
		return movePossible;
	}

	/** Clean up the board if the move is not possible */
	private void cleanupUnexecutedMove(Poem overlappedPoem) {
		if(fromSingleWordPoem){
			selectedWord=proReg.degradeOneWordPoem(selectedPoem);
			selectedWord.setPosition(new Position(originalX, originalY,0));
			model.setSelected(null);
			
			if (overlappedPoem != null){
				if(overlappedPoem.getRows().size() == 1 
						&& overlappedPoem.getRow(0).getWords().size() ==1){
					proReg.degradeOneWordPoem(overlappedPoem);
				}
			}
		}else{
			selectedPoem.setPosition(new Position(originalX, originalY,0));
		}
	}
	
	/** Return any single word poems that overlap the selected poem */
	private Poem getOverlappedSingleWordPoem(int x, int y){
		Word overlappedWord = board.findProtectedWord(x, y);
		if (overlappedWord == null){
			Position pos = selectedPoem.getPosition();
			//top-left corner;
			overlappedWord = board.findProtectedWord(pos.x, pos.y);
			if (overlappedWord == null){
				//top-right corner
				overlappedWord = board.findProtectedWord(pos.x, pos.y + 15); //use row height
				if (overlappedWord == null){
					//bottom-left corner
					int rowWidth = selectedPoem.getRow(0).getWidth();
					overlappedWord = board.findProtectedWord(pos.x + rowWidth, pos.y);
					if(overlappedWord == null){
						//bottom-right corner
						overlappedWord = board.findProtectedWord(pos.x + rowWidth, pos.y + 15);
						if(overlappedWord == null){
							//go thru words in protected region and figure out if there overlap the select poem
							overlappedWord = board.getOverlappedWords(selectedPoem);
							if(overlappedWord ==null){ return null;}
						}
					}
				}
			}
		}
		
		return proReg.formOneWordPoem(overlappedWord);
	}

}
