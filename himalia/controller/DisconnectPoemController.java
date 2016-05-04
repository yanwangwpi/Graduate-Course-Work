package himalia.controller;

import himalia.model.*;
import himalia.view.RegionPanel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 
 * @author saadjei
 *
 */
public class DisconnectPoemController extends MouseAdapter {

	Model model;
	RegionPanel panel;
	Board board;
	Poem selectedPoem;
	Row selectedRow;
	
	int rowHeight = 15;
	ProtectedRegion proReg;
	
	
	int deltaX, deltaY, originalX,originalY;
	int sourceRegion; //0 => Protected 1=>unprotected
	
	/**
	 * This is the main constructor of the controller.
	 */
	public DisconnectPoemController(Model m, RegionPanel panel){
		this.panel = panel;
		this.model = m;
		this.board = m.getBoard();
		this.proReg = board.getProtectedRegion();
	}
	
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
		}
		
		if (selectedPoem == null){return false;}
		selectedRow = selectedPoem.getRowFromPoint(new Position(x,y,0));

		// no longer in the board since we are moving it around...
		originalX = selectedRow.getPosition().x;
		originalY = selectedRow.getPosition().y;
			
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
		
		if (selectedRow == null) { return false; }

		selectedRow.setPosition(x - deltaX,y - deltaY);
		
		boolean ok = true;
    

		Position topPos = new Position(selectedRow.getPosition().x,selectedRow.getPosition().y,0);
		Position downPos = new Position(selectedRow.getPosition().x,selectedRow.getPosition().y+ rowHeight,0);

		ok = (board.isProtected(topPos)
				&& board.isProtected(downPos)); 
		
		if (!ok) {
			selectedRow.setPosition(originalX, originalY);
		} else {
			//panel.paintShape(selected);
			panel.repaint();
		}
		
		return ok;
	}
	
	/** Separate out this function for testing purposes. */
	protected boolean release (int x, int y) {
		boolean movePossible=false;
		boolean moveExecuted = false;
		
		DisconnectPoemMove move = null;
		Position destPoemPos = new Position(x - deltaX,y - deltaY,0);
		Position destPos = new Position(x ,y ,0);
		Position originalPos = new Position(originalX,originalY,0);
				
		if (selectedPoem == null) { return false; }
		if (selectedPoem.getRows().size() ==1){return false;
		
		}
		if (board.isProtected(destPos) && 
				!board.getProtectedRegion().detectOverlap(selectedRow)){
				movePossible =true;
		}
		
		if (movePossible){
			move = new DisconnectPoemMove(selectedPoem, destPos, originalPos, destPoemPos);
			if (move.execute(board)) {
				board.addUndoMove(move);
				board.clearRedoStacks();
				moveExecuted = true;
			}
		}
		
		if (!moveExecuted){
			selectedRow.setPosition(originalX, originalY);
		}/*else{
			//check for resulting single row poems and degrade them.
			for(Poem p: move.resultPoems){
				if (p.getRows().size() == 1 && p.getRows().get(0).getWords().size() == 1){
					proReg.degradeOneWordPoem(p);
				}
			}
		}*/
		selectedPoem = null;
		panel.redraw();
		panel.repaint();
		return movePossible;
	}	

}
