package himalia.controller;

import java.awt.event.MouseAdapter;

import himalia.model.Model;
import himalia.view.RegionPanel;

/**
 * This controller is used for undo
 * @author Yan Wang
 */

public class UndoController extends MouseAdapter {
	/**
	 * The model that contains all entity classes
	 */
	Model model;
	
	/**
	 * The panel that view should be drawn on
	 */
	RegionPanel panel;

	/**
	 * Constructor
	 * @param model
	 * @param panel
	 */
	public UndoController(Model model, RegionPanel panel) {
		this.model = model;
		this.panel = panel;
		panel.setActiveListener(this);
		panel.setActiveMotionListener(this);
	}
	
	/**
	 * Undo a move
	 */
	public void process() {
		AbstractMove m = model.getBoard().getMovesUndo();
		if (m != null) {
			m.undo(model.getBoard());
			model.getBoard().addRedoMove(m);
			panel.redraw();
			panel.repaint();
		}
	}
}