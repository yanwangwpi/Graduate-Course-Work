package himalia.controller;

import himalia.model.WordModel;
import himalia.view.WordTable;
import himalia.model.*;

import java.awt.Point;

import javax.swing.table.JTableHeader;

/**
 * This controller is used for sorting words in the table
 * @author Yan Wang
 *
 */

public class SortController {
	/** Table being controlled. */
	WordTable table;
	
	/**
	 * Constructor
	 * @param table  Controller needs to know of the JTable view.
	 */
	public SortController(WordTable table) {
		this.table = table; 
	}


	/**
	 * Implements the handler for clicking on a table column in the display.
	 * @param h
	 * @param point
	 */
	public void process (JTableHeader h, Point point) {
		int columnIndex = h.columnAtPoint(point);
		
		if (columnIndex != -1) {
			WordModel wordModel = (WordModel) h.getTable().getModel();
			wordModel.sort(Sortby.values()[columnIndex]);

		}
	}
}
