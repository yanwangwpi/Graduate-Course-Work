package himalia.view;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import himalia.controller.HighlightController;
import himalia.controller.RefreshWordTableController;
import himalia.controller.SortController;
import himalia.model.Board;
import himalia.model.Model;
import himalia.model.WordModel;

/**
 * This class is used to create view for sorting and exploring table
 * @author Yan Wang
 *
 */

public class WordTable extends JPanel{

	private static final long serialVersionUID = -3383046923244327856L;
	/**
	 * View Jtable
	 */
	JTable jtable = null;
	/**
	 * The board that contains models to be drawn
	 */
	Board board;
	/**
	 * Model for Jtable
	 */
	WordModel wordModel;
	/**
	 * Panel that is related to this table
	 */
	RegionPanel panel;
	
	
	
	public WordTable(Model model, RegionPanel panel){
	this.board = model.getBoard();
	this.panel = panel;

	
	// create the model for the data which backs this table
	wordModel = new WordModel(board);
	
	// add to listener chain
	board.addListener(new RefreshWordTableController(this));
	
	// the proposed dimension of the UI
	Dimension sizeScroll = new Dimension(500, 80);
	
	// Scrollable panel will enclose the JTable and support scrolling vertically
	JScrollPane jsp = new JScrollPane();
	jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	jsp.setPreferredSize(sizeScroll);
	
	// Just add the JTable to the set. First create the list of Players,
	// then the SwingModel that supports the JTable which you now create.
	jtable = new JTable(wordModel);
	
	// let's tell the JTable about its columns.
	TableColumnModel columnModel = new DefaultTableColumnModel();

	// must build one by one...
	String[] headers = new String[] { WordModel.wordLabel, WordModel.typeLabel};
	int index = 0;
	for (String h : headers) {
		TableColumn col = new TableColumn(index++);
		col.setHeaderValue(h);
		columnModel.addColumn(col);
	}		
	jtable.setColumnModel(columnModel);
	
	// let's install a sorter and also make sure no one can rearrange columns
	JTableHeader header = jtable.getTableHeader();
	
	// purpose of this sorter is to sort by columns.
	header.addMouseListener(new MouseAdapter()	{
		public void mouseClicked (MouseEvent e)	{
			JTableHeader h = (JTableHeader) e.getSource() ;
			new SortController(WordTable.this).process(h, e.getPoint());
		}
	});
	
	// Here's the trick. Make the JScrollPane take its view from the JTable.
	jsp.setViewportView(jtable);

	// add the scrolling Pane which encapsulates the JTable
	// physical size limited
	this.setPreferredSize(sizeScroll);
	this.add(jsp);
	
	
	this.jtable.addMouseListener(new java.awt.event.MouseAdapter()
	{
	    public void mouseClicked(java.awt.event.MouseEvent e)
	    {
	        
	        Point mousepoint;                     
	        mousepoint =e.getPoint(); 
	        int wordIndex=jtable.rowAtPoint(mousepoint);
	        new HighlightController(WordTable.this.panel).process(board, wordIndex);   
	    }
	});
}

/**
 * Causes the display of the shapes to be updated to the latest data.
 */
public void refreshTable() {
	// THIS is the key method to ensure JTable view is synchronized
	jtable.revalidate();
	jtable.repaint();
	this.revalidate();
	this.repaint();
}
}

