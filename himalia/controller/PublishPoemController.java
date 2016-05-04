
package himalia.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import himalia.model.*;
import himalia.view.*;

/**
 * 
 * @author saadjei
 *
 */
public class PublishPoemController extends MouseAdapter {
	Model model;
	RegionPanel panel;
	Poem selected;
	Board board;
	ProtectedRegion proReg;
	
	public PublishPoemController(Model model, RegionPanel panel) {
		this.model = model;
		this.panel = panel;
		this.board = model.getBoard();
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
	
	/** Separate out this function for testing purposes. */
	protected boolean select(int x, int y) {
		
		int selectedID = proReg.getPoemID(new Position(x,y,0));
		selected = proReg.getPoem(selectedID);

		if (selected == null) { return false; }
		
		if(proReg.publishPoem(selected)){
			//clear the stacks
			board.clearRedoUndoStacks();
			//release the poem
			ReleasePoemMove m = new ReleasePoemMove(selected);
			m.execute(board);
		}
		
		// paint will happen once moves. This redraws state to prepare for paint
		panel.redraw();
		panel.repaint();
		return true;
	}	
	

	
}

