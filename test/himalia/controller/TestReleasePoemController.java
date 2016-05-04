package himalia.controller;

import java.util.ArrayList;

import himalia.model.Board;
import himalia.model.Model;
import himalia.model.Poem;
import himalia.model.Position;
import himalia.model.Word;
import himalia.model.WordType;
import himalia.view.RegionPanel;
import junit.framework.TestCase;

public class TestReleasePoemController extends TestCase {
	
	Board board = null;
	Model model;
	RegionPanel panel;
	
	public void testSelect(){
		Word word1 = new Word(200,300,0,"Hello", WordType.adj);
		Word word2 = new Word(100,100,0,"World", WordType.adj);
		ArrayList<Word> wrds = new ArrayList<Word>();
		wrds.add(word1);
		int height = 250;
		int width=500; 
		Position posProtect = new Position(0,0,0);
		Position posUnprotect =new Position(0,height+1,0);
	
		board = new Board(wrds, posProtect, posUnprotect,height, height, width,width);
		model = new Model(board);
		panel = new RegionPanel(model);
		
		Position pos = new Position(1,1,0);
		board.protectWord(word1, pos);
		
		Poem p = board.getProtectedRegion().convertWordToPoem(word1);
		
		ReleasePoemController prc = new ReleasePoemController(model,panel);
		
		assertEquals(board.getProtectedRegion().getPoems().size(),1);
		prc.select(2,2);
		assertEquals(board.getProtectedRegion().getPoems().size(),0);
		
	}
	
}
