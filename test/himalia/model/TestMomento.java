package himalia.model;

import java.util.ArrayList;


import junit.framework.TestCase;

public class TestMomento extends TestCase {
	Board board = null;
		public void testConstruction() {
			
			Word word = new Word(2,3,0,"Hello", WordType.adj);
			ArrayList<Word> wrds = new ArrayList<Word>();
			wrds.add(word);
			Position posProtect = new Position(1,2,0);
			Position posUnprotect =new Position(100,200,0);
			int heightProtect = 50;
			int heightUnprotect=50; 
			int widthProtect=50; 
			int widthUnprotect=50;
			board =new Board(wrds, posProtect,posUnprotect, heightProtect,heightUnprotect,widthProtect, widthUnprotect);
			
			Word word2 = new Word(102,202,0,"World!", WordType.adj);
			board.getProtectedRegion().addWord(word2);
			
			// create memento and restore b2 to that state
			BoardMemento m = board.getState();
			//Board board2 = new Board();
			//System.out.println(m.storedUnprotected.get(0).getValue());
			//System.out.println(m.storedProtectedW.get(0).getValue());
			//board2.restore(m);
			
			// both b and b2 should have just single shape 
			//System.out.println(board.getUnProtectedRegion().getWords().get(0).getValue());
			//System.out.println(m.storedUnprotected.get(0).getValue());
			//System.out.println(board.getProtectedRegion().getWords().get(0).getValue());
			//System.out.println(m.storedProtectedW.get(0).getValue());
			//assertEquals(board.getUnProtectedRegion().getWords().get(0), m.storedUnprotected.get(0).getValue());
			//assertEquals(board.getProtectedRegion().getWords().get(0), m.storedProtectedW.get(0).getValue());
			board.getProtectedRegion().deleteWord(word2);
			assertEquals(board.getProtectedRegion().getWords().size(),0);
			board.restore(m);
			assertEquals(board.getProtectedRegion().getWords().size(),1);
			
			Word word3 = new Word(3,3,0,"Blabla", WordType.adj);
			Poem poem = new Poem();
			Row row = new Row(poem, word3);
			Poem poem2 = new Poem();
			Row row2 = new Row(poem2, word2);
			poem.connectPoem(poem2);
			board.getProtectedRegion().addPoem(poem);
			m = board.getState();
			//System.out.println(board.getProtectedRegion().getPoems().size());
			assertEquals(board.getProtectedRegion().getPoems().size(),1);
			board.getProtectedRegion().deletePoem(poem);
			assertEquals(board.getProtectedRegion().getPoems().size(),0);
			board.restore(m);
			//System.out.println(board.getProtectedRegion().getPoems().size());
			assertEquals(board.getProtectedRegion().getPoems().size(),1);
			
		}
protected void tearDown(){
	board=null;
}
}
