package himalia.controller;


import java.awt.event.*;

import himalia.model.Board;
import himalia.model.Model;
import himalia.model.Position;
import himalia.model.Word;
import himalia.view.RegionPanel;

/**
 * this controller will only let the word moves within one certain area;Protected
 * (overlapping not allowed)/Unprotected(allowed overlapping),I refer the code in Georgy's example:
 * @author susanqin
 *
 */

public class MoveWordController extends MouseAdapter{
	
	/**the protected region and unprotected region*/
	RegionPanel panel;
	/**the word to be moved*/
	Word selected;
	/**the board I use*/
	Board board;
	/**the deltaX/y is used for draging
	 * originalX/Y save the original position*/
	int deltaX, deltaY, originalX,originalY;
	/**0 => Protected 1=>unprotected*/
	int sourceRegion; 
	
	/**Constructor*/
	public MoveWordController(Model model, RegionPanel panel) {
		//this.model = model;
		this.panel = panel;
		
		board = model.getBoard();
	}
	/**register the listeners*/
	public void register() {
		panel.setActiveListener(this);
		panel.setActiveMotionListener(this);
	}
	
	@Override
	/**action performed when mouse pressed*/
	public void mousePressed(MouseEvent me) {
		select(me.getX(), me.getY());
		
	}
	@Override
	/**action performed when mouse dragged*/
	public void mouseDragged(MouseEvent me) {
		drag(me.getX(), me.getY());
	}
	
	@Override
	/**action performed when mouse released*/
	public void mouseReleased(MouseEvent me) {
		release(me.getX(), me.getY());
		
	}
	
	/** Separate out this function for testing purposes. */
	protected boolean select(int x, int y) {	
		// pieces are returned in order of Z coordinate
		if(board.isProtected(new Position(x,y,0))){
			selected = board.findProtectedWord(x, y);
			sourceRegion = 0;
		}else{
			selected = board.findUnprotectedWord(x, y);
			sourceRegion = 1;
		}
		//board.getUnProtectedRegion().foreGroundWord(selected);
		if (selected == null) { return false; }
		
		// no longer in the board since we are moving it around...
		//board().remove(w);
		originalX = selected.getPosition().x;
		originalY = selected.getPosition().y;
			
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
		
		if (selected == null) { return false; }
		
		panel.paintBackground(selected);
		int oldx = selected.getPosition().x;
		int oldy = selected.getPosition().y;
		
		selected.setPosition(x - deltaX, y - deltaY);		
		boolean ok = true;
		ok = isMovePossible();	
		if (!ok) {
			selected.setPosition(oldx, oldy);
		} else {
			panel.repaint();
		}	
		return true;
	}
	/**helper method for dragging/release word.*/
	private boolean isMovePossible() {
		boolean ok;
		if (board.getProtectedRegion().detectOverlap(selected)){
			ok = false;
		}else{
			Position topPos = new Position(selected.getPosition().x,selected.getPosition().y,0);
			Position downPos = new Position(selected.getPosition().x,selected.getPosition().y+ selected.getHeight(),0);

			ok = (sourceRegion == 1 && board.isUnProtected(topPos)) ||
					(sourceRegion == 0 && board.isProtected(downPos)); 
		}
		return ok;
	}
	
	/** Separate out this function for testing purposes. */
	protected boolean release (int x, int y) {
		boolean movePossible=false;
		Position destPos = new Position(x,y,0);
		
		if (selected == null) { return false; }
		int wordWidth=selected.getWidth();
		int wordHeight=selected.getHeight();
		Position destPosrightTop=new Position(x+wordWidth,y,0);
		Position destPosleftBot=new Position(x,y+wordHeight,0);
		Position destPosrightBot=new Position(x+wordWidth,y+wordHeight,0);
		if (sourceRegion ==1 && board.isUnProtected(destPos)&&board.isUnProtected(destPosrightTop)&&board.isUnProtected(destPosleftBot)&&board.isUnProtected(destPosrightBot)){ //Unprotected Region move
			movePossible =true;

		}else if(sourceRegion ==0 && board.isProtected(destPos)&&board.isProtected(destPos)&&board.isProtected(destPosrightTop)&&board.isProtected(destPosleftBot)&&board.isProtected(destPosrightBot)){ 
			 //protected region move
			selected.setPosition(destPos);
			if (!board.getProtectedRegion().detectOverlap(selected)){
				movePossible =true;
			}else{
				movePossible = false;
			}
		}		
		if (movePossible){
			
			MoveWordMove move = new MoveWordMove(selected, new Position(originalX,originalY,0), new Position(x-deltaX,y-deltaY,0));
	
			if (move.execute(board)) {
				board.addUndoMove(move);
				board.clearRedoStacks();
			}
		}else{
			
			selected.setPosition(originalX, originalY);
		}
		selected = null;
		panel.redraw();
		panel.repaint();
		return true;
	}	
}
