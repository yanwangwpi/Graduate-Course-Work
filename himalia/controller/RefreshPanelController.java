package himalia.controller;

import himalia.view.RegionPanel;

/**
 * This controller implements Listener, and is used for refresh the panel
 * @author Yan Wang
 *
 */
public class RefreshPanelController implements Listener {
	/** Widget to be refreshed. */
	RegionPanel panel;
	
	public RefreshPanelController(RegionPanel panel) {
		this.panel = panel;
	}
	
	public void update() {
		panel.redraw();
		panel.repaint();
	}

}
