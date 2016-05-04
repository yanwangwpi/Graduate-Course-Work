package himalia.model;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

/**
 * This class is used to create model of wordtable
 * @author Yan Wang
 *
 */

public class WordModel extends AbstractTableModel{
	
	private static final long serialVersionUID = 1L;
	
	//Each time to sort, change it to the other, so as to order ascendingly or descendingly.
	boolean sortOrder = false;
	Sortby sortby = null;
	Board board;
	
	//The words list to be sorted
	ArrayList<Word> words;
	
	/** Key values. */
	public static final String wordLabel = "Word";
	public static final String typeLabel = "Type";
	
	/** The Table model needs to know the board which contains the words. */
	@SuppressWarnings("unchecked")
	public WordModel (Board b) {
		this.board = b;
		this.board.getUnProtectedRegion().setSortedWords((ArrayList<Word>)this.board.getUnProtectedRegion().getWords().clone()) ;
		this.words = this.board.getUnProtectedRegion().getSortedWords();
		sortOrder = this.board.getUnProtectedRegion().getSortOrder();
		sortby = this.board.getUnProtectedRegion().getSortby();
		
	}
	
	public int getColumnCount() {
		return 2;
	}

	public int getRowCount() {
		return words.size();
	}

	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Word w = words.get(rowIndex);
		
		if (columnIndex == 0) { 
			return w.getValue();
		} 
		else if(columnIndex == 1) { 
			return w.getType();
		}
		return "";
	}

	public void sort(Sortby fieldName) {
		sortOrder = !sortOrder;
		//Sort words
		board.getUnProtectedRegion().exploreWords(fieldName,sortOrder,this.words);
		//Change sort settings
		board.getUnProtectedRegion().setSortOrder(sortOrder);
		board.getUnProtectedRegion().setSortby(fieldName);
	}
	

}
