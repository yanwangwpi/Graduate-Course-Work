package himalia.controller;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import himalia.model.*;
import himalia.view.*;

public class ReleaseWordController extends MouseAdapter {
	Model model;
	RegionPanel panel;
	Word selected;
	int deltaX, deltaY, originalX,originalY;
	
	public ReleaseWordController(Model model, RegionPanel panel) {
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
	
	/** Separate out this function for testing purposes. */
	protected boolean select(int x, int y) {
	
		// pieces are returned in order of Z coordinate
		selected = model.getBoard().findProtectedWord(x, y);
		if (selected == null) { return false; }
		
		// no longer in the board since we are moving it around...
		//model.getBoard().remove(w);
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
		for (Poem p : model.getBoard().getProtectedRegion().getPoems()) {
			if (p.intersects(selected)) {
				ok = false;
				break;
			}
		}
		
		if (!ok) {
			selected.setPosition(oldx, oldy);
		} else {
			//panel.paintShape(selected);
			panel.repaint();
		}
	
		return true;
	}
	
	/** Separate out this function for testing purposes. */
	protected boolean release (int x, int y) {

		if (selected == null) { return false; }

		boolean region =
		model.getBoard().isUnProtected(new Position(x-deltaX,y-deltaY,0)) &
		model.getBoard().isUnProtected(new Position(x-deltaX,y-deltaY+selected.getHeight(),0)) & 
		model.getBoard().isUnProtected(new Position(x-deltaX+selected.getWidth(),y-deltaY,0));
		
		// now released we can create Move
		if (!region){
			selected.setPosition( originalX, originalY);
			panel.redraw();
			panel.repaint();
			return false;
		}
		

		MoveWordMove move = new MoveWordMove(selected, new Position(originalX,originalY,0), new Position(x-deltaX,y-deltaY,0));

		if (move.execute(model.getBoard())) {
			model.getBoard().addUndoMove(move);
			model.getBoard().clearRedoStacks();
		}
		
		// no longer selected
		model.setSelected(null);
		
		panel.redraw();
		panel.repaint();
		return true;
	}	
}

