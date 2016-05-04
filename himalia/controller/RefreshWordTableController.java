package himalia.controller;

import himalia.view.WordTable;

/**
 * This controller implements Listener, and is used for refresh the table
 * @author Yan Wang
 *
 */

public class RefreshWordTableController implements Listener {

	/** Widget to be refreshed. */
	WordTable table;
	
	public RefreshWordTableController(WordTable table) {
		this.table = table;
	}
	
	@Override
	public void update() {
		table.refreshTable();
	}
}
