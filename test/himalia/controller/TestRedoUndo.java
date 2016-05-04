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

public class TestRedoUndo extends TestCase {
	
	Model model;
	RegionPanel panel;

	protected void setUp(){
		Board board = null;
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
	
		board.getProtectedRegion().getWords().add(word2);
		
		Poem p = board.getProtectedRegion().convertWordToPoem(word2);
		
		int selectedID = board.getProtectedRegion().getPoems().get(0).getID();
		Poem selected = board.getProtectedRegion().getPoem(selectedID);
		ReleasePoemMove move = new ReleasePoemMove(selected);

		if (move.execute(board)) {
			board.addUndoMove(move);
			board.clearRedoStacks();
		}
		
		assertEquals(board.getProtectedRegion().getPoems().size(),0);
	}
	
	public void testUndo(){
		UndoController uc = new UndoController(model, panel);
		uc.process();
		assertEquals(model.getBoard().getProtectedRegion().getPoems().size(),1);
	}
	
	public void testRedo(){
		RedoController uc = new RedoController(model, panel);
		uc.process();
		assertEquals(model.getBoard().getProtectedRegion().getPoems().size(),0);
	}

}
