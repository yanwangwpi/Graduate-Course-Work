package himalia.controller;

import himalia.model.Board;
import himalia.model.Model;
import himalia.model.Poem;
import himalia.model.Position;
import himalia.model.Word;
import himalia.model.WordType;
import himalia.view.RegionPanel;

import java.util.ArrayList;

import junit.framework.TestCase;

public class TestHighlightController extends TestCase {

	Board board = null;
	Model model;
	RegionPanel panel;
	
	public void testGetHighlighted(){
		
		Word word1 = new Word(200,400,0,"Hello", WordType.adj);
		Word word2 = new Word(200,350,0,"World", WordType.adj);
		
		ArrayList<Word> wrds = new ArrayList<Word>();
		wrds.add(word1);
		wrds.add(word2);
		int height = 250;
		int width=500; 
		Position posProtect = new Position(0,0,0);
		Position posUnprotect =new Position(0,height+1,0);

		board = new Board(wrds, posProtect, posUnprotect,height, height, width,width);
		board.getUnProtectedRegion().setSortedWords((ArrayList<Word>) board.getUnProtectedRegion().getWords().clone());
		model = new Model(board);
		panel = new RegionPanel(model);
		
		HighlightController hlc = new HighlightController(panel);

		Word highlighted = hlc.getHighlighted(board,1 );
		
		assertEquals(highlighted.getValue(),"World");
		
	}

}
