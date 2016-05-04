package himalia.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import himalia.model.*;
import himalia.view.*;

/**
 * This controller handles all operations relating to moving a poem around the board.
 * @author saadjei
 *
 */
public class MovePoemController extends MouseAdapter {

	Model model;
	RegionPanel panel;
	Board board;
	Poem selected;
	
	int deltaX, deltaY, originalX,originalY;
	int sourceRegion; //0 => Protected 1=>unprotected
	
	/**
	 * This is the main constructor of the controller.
	 */
	public MovePoemController(Model m, RegionPanel panel){
		this.panel = panel;
		this.model = m;
		this.board = m.getBoard();
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
	
		if(board.isProtected(new Position(x,y,0))){
			selected = board.getProtectedRegion().getIntersectedPoem(x, y);
		}
		
		if (selected == null) { return false; }
		
		originalX = selected.getPosition().x;
		originalY = selected.getPosition().y;
			
		// set anchor for smooth moving
		deltaX = x - originalX;
		deltaY = y - originalY;
		
		// This redraws state to prepare for paint
		panel.redraw();
		return true;
	}	
	
	/** Separate out this function for testing purposes. */
	protected boolean drag (int x, int y) {
		int buttonType = 0;
		// no board? no behavior! No dragging of right-mouse buttons...
		if (buttonType == MouseEvent.BUTTON3) { return false; }
		
		if (selected == null) { return false; }
		
		int oldx = selected.getPosition().x;
		int oldy = selected.getPosition().y;
		
		selected.setPosition(new Position(x - deltaX, y - deltaY,0));
		
		boolean ok = true;
    
		ok = isMovePossible();
		
		if (!ok) {
			selected.setPosition(new Position(oldx, oldy, 0));
		} else {
			//panel.paintShape(selected);
			panel.repaint();
		}
		
		return ok;
	}

	/**
	 * Check if the move is possible.
	 * Separated out since the same functionality is called else where.*/
	private boolean isMovePossible() {
		boolean ok;
		if (board.getProtectedRegion().detectOverlap(selected)){
			ok = false;
		}else{
			Position myPos = selected.getPosition();
			Position topLeft = new Position(myPos.x,myPos.y,0);
			
			Position topRight = new Position(myPos.x  + selected.getRow(0).getWidth(),myPos.y,0);
			
			Position bottomLeft = new Position(selected.getRow(selected.getRowCount()-1).getPosition().x,selected.getRow(selected.getRowCount()-1).getPosition().y + 15,0);
			
			Position bottomRight = new Position(myPos.x + selected.getRow(selected.getRowCount()-1).getWidth(),myPos.y + selected.getHeight(),0);

			ok = (board.isProtected(topLeft)
					&& board.isProtected(topRight)
					&& board.isProtected(bottomLeft)
					&& board.isProtected(bottomRight)); 
		}

		return ok;
	}
	
	/** Separate out this function for testing purposes. */
	protected boolean release (int x, int y) {
		boolean movePossible=false;
		Position destPos = new Position(x,y,0);
		
		if (selected == null) { return false; }
		selected.setPosition(new Position(x - deltaX, y - deltaY,0));
		
		if (board.isProtected(destPos)){	
			movePossible =isMovePossible();
		}
		
		if (movePossible){			
			MovePoemMove move = new MovePoemMove(selected, new Position(originalX,originalY,0), new Position(x-deltaX,y-deltaY,0));
	
			if (move.execute(board)) {
				board.addUndoMove(move);
				board.clearRedoStacks();
			}
		}else{
			selected.setPosition(new Position(originalX, originalY,0));
		}
		
		selected = null;
		panel.redraw();
		panel.repaint();
		return movePossible;
	}	
}
