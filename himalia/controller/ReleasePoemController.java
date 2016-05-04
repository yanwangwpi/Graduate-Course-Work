
package himalia.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import himalia.model.Model;
import himalia.model.Poem;
import himalia.model.Position;
import himalia.view.RegionPanel;

/**
 * This controller is used for release poem
 * @author Yan Wang
 *
 */
public class ReleasePoemController extends MouseAdapter {
	/**The model that contains all entity classes*/
	Model model;
	/**The panel where the view should be drawn on*/
	RegionPanel panel;
	/**The poem that is to be released*/
	Poem selected;

	/**
	 * constructor
	 * @param model
	 * @param panel
	 */
	public ReleasePoemController(Model model, RegionPanel panel) {
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

	/**
	 * Select the poem according to position (x,y), and execute the release poem move
	 * @param x
	 * @param y
	 * @return
	 */
	protected boolean select(int x, int y) {

		//select the poem according to position (x,y)
		int selectedID = model.getBoard().getProtectedRegion().getPoemID(new Position(x,y,0));
		selected = model.getBoard().getProtectedRegion().getPoem(selectedID);
		
		//create the release poem move
		if (selected == null) { return false; }
		ReleasePoemMove move = new ReleasePoemMove(selected);
		//execute the release poem move
		if (move.execute(model.getBoard())) {
			model.getBoard().addUndoMove(move);
			model.getBoard().clearRedoStacks();
		}
		
		selected= null;
		panel.redraw();
		panel.repaint();
		return true;
	}	
	

	
}

