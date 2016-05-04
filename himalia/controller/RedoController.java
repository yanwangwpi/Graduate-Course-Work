package himalia.controller;

import java.awt.event.MouseAdapter;

import himalia.model.Model;
import himalia.view.RegionPanel;
/**
 * This controller is used for manage redo moves
 * @author Yan Wang
 *
 */
public class RedoController extends MouseAdapter{
	Model model;
	RegionPanel panel;

	public RedoController(Model model, RegionPanel panel) {
		this.model = model;
		this.panel = panel;
		panel.setActiveListener(this);
		panel.setActiveMotionListener(this);
	}
	
	public void process() {
		AbstractMove m = model.getBoard().getMovesRedo();
		if (m != null) {
			m.execute(model.getBoard());
			model.getBoard().addUndoMove(m);
			panel.redraw();
			panel.repaint();
		}
	}
}
