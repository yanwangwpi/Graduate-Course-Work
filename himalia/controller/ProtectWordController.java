package himalia.controller;

import himalia.model.Model;
import himalia.model.Poem;
import himalia.model.Position;
import himalia.model.Word;
import himalia.view.RegionPanel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This controller is used for protect a word from unprotected region to protected region
 * @author Yan Wang
 *
 */

public class ProtectWordController extends MouseAdapter{
	/**model that covers the entity classes*/
	Model model;
	/**panel that the view should be drawn on*/
	RegionPanel panel;
	/**The word that is selected to be protected*/
	Word selected;
	/**difference between mouse position and the position of the word(which is left top corner)*/
	int deltaX, deltaY;
	/**the original position of the word*/
	int originalX,originalY;
	
	/**
	 * the constructor
	 * @param model
	 * @param panel
	 */
	public ProtectWordController(Model model, RegionPanel panel) {
		this.model = model;
		this.panel = panel;
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
	
	/** 
	 * Record the information while a word is selected 
	 */
	protected boolean select(int x, int y) {
	
		selected = model.getBoard().findUnprotectedWord(x, y);
		
		if (selected == null) { return false; }
		
		originalX = selected.getPosition().x;
		originalY = selected.getPosition().y;
			
		// set anchor for smooth moving
		deltaX = x - originalX;
		deltaY = y - originalY;
		
		// paint will happen once moves. This redraws state to prepare for paint
		panel.redraw();
		return true;
	}	
	
	/** 
	 * set the position of the selected word, while dragging
	 * @param x
	 * @param y
	 * @return
	 */
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
		
		if (!ok) {
			selected.setPosition(oldx, oldy);
		} else {
			panel.repaint();
		}
	
		return true;
	}
	
	/** 
	 * operations after the mouse is released
	 * @param x
	 * @param y
	 * @return
	 */
	protected boolean release (int x, int y) {

		if (selected == null) { return false; }
		
		//check if the new region is in protected region
		boolean region =
		model.getBoard().isProtected(new Position(x-deltaX,y-deltaY,0)) &
		model.getBoard().isProtected(new Position(x-deltaX,y-deltaY+selected.getHeight(),0)) & 
		model.getBoard().isProtected(new Position(x-deltaX+selected.getWidth(),y-deltaY,0));
				
		if (!region){
			selected.setPosition( originalX, originalY);	
			panel.redraw();
			panel.repaint();
			return false;
		}
		
		//check if there is overlap
		boolean ok = true;
		for (Poem p : model.getBoard().getProtectedRegion().getPoems()) {
			if (p.intersects(selected)) {
				ok = false;
				break;
			}
		}
		
		for (Word w : model.getBoard().getProtectedRegion().getWords()) {
			if (w.intersectWord(selected)) {
				ok = false;
				break;
			}
		}

		if (!ok) {
			selected.setPosition(originalX, originalY);			
			selected = null;
			panel.redraw();
			panel.repaint();
			return false;
		} 

		//construct a move and execute it.
		MoveWordMove move = new MoveWordMove(selected, new Position(originalX,originalY,0), new Position(x-deltaX,y-deltaY,0));
		
		if (move.execute(model.getBoard())) {
			model.getBoard().addUndoMove(move);
			model.getBoard().clearRedoStacks();
		}
		panel.redraw();
		panel.repaint();
		return true;
	}	
}