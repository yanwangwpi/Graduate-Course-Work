package himalia.model;

import himalia.controller.AbstractMove;
import himalia.controller.MoveWordMove;

import java.util.ArrayList;
import java.util.Hashtable;

import junit.framework.TestCase;

public class TestBoard extends TestCase{
	
	Board board = null;

	public void testBoard() {
		Board board = null;
		Word word = new Word(1,1,0,"Hello", WordType.adj);
		ArrayList<Word> wrds = new ArrayList<Word>();
		wrds.add(word);
		Position posProtect = new Position(0,0,0);
		Position posUnprotect =new Position(0,250,0);
		int heightProtect = 250;
		int heightUnprotect=250; 
		int widthProtect=500; 
		int widthUnprotect=500;
		board = new Board(wrds, posProtect,posUnprotect, heightProtect,heightUnprotect,widthProtect, widthUnprotect);
		assert board!=null;
	}

	
	public void testAddProtectedWord(){
		Board board = null;
		Word word = new Word(1,1,0,"Hello", WordType.adj);
		ArrayList<Word> wrds = new ArrayList<Word>();
		wrds.add(word);
		Position posProtect = new Position(0,0,0);
		Position posUnprotect =new Position(0,250,0);
		int heightProtect = 250;
		int heightUnprotect=250; 
		int widthProtect=500; 
		int widthUnprotect=500;
		board = new Board(wrds, posProtect,posUnprotect, heightProtect,heightUnprotect,widthProtect, widthUnprotect);
	
		Word word2 = new Word(3,3,0,"Hello", WordType.adj);
		board.protectedRegion.addWord(word2);
		//board.addProtectedWord(word2);
		//board.deleteProtectedWord(word);
		
	}
	
	public void testDeleteProtectedWord(){
		Board board = null;
		Word word = new Word(1,1,0,"Hello", WordType.adj);
		ArrayList<Word> wrds = new ArrayList<Word>();
		wrds.add(word);
		Position posProtect = new Position(0,0,0);
		Position posUnprotect =new Position(0,250,0);
		int heightProtect = 250;
		int heightUnprotect=250; 
		int widthProtect=500; 
		int widthUnprotect=500;
		board = new Board(wrds, posProtect,posUnprotect, heightProtect,heightUnprotect,widthProtect, widthUnprotect);
	
		//Word word2 = new Word(1,1,0,"Hello", WordType.adj);
		board.deleteProtectedWord(word);
		
	}
	
	public void testAddUnProtectedWord(){
		Board board = null;
		Word word = new Word(1,1,0,"Hello", WordType.adj);
		ArrayList<Word> wrds = new ArrayList<Word>();
		wrds.add(word);
		Position posProtect = new Position(0,0,0);
		Position posUnprotect =new Position(0,250,0);
		int heightProtect = 250;
		int heightUnprotect=250; 
		int widthProtect=500; 
		int widthUnprotect=500;
		board = new Board(wrds, posProtect,posUnprotect, heightProtect,heightUnprotect,widthProtect, widthUnprotect);
	
		Word word2 = new Word(3,3,0,"Hello", WordType.adj);

		try{
		board.addUnprotectedWord(word2);}
		catch(CloneNotSupportedException e) {
			e.printStackTrace();
		}

		board.deleteUnprotectedWord(word);
		
	}

	
	public void testProtectedWord(){
		Board board = null;
		Word word = new Word(1,1,0,"Hello", WordType.adj);
		ArrayList<Word> wrds = new ArrayList<Word>();
		wrds.add(word);
		Position posProtect = new Position(0,0,0);
		Position posUnprotect =new Position(0,250,0);
		int heightProtect = 250;
		int heightUnprotect=250; 
		int widthProtect=500; 
		int widthUnprotect=500;
		board = new Board(wrds, posProtect,posUnprotect, heightProtect,heightUnprotect,widthProtect, widthUnprotect);
	
		
		Position position2= new Position(4,4,1);
		board.protectWord(word, position2);
		assert word.getPosition()==position2;
		position2= new Position(4,4,1);
		board.protectWord(word, position2);
		assert word.getPosition()==position2;
	}

	public void testFindUnprotectedWord(){
		Board board = null;
		Word word1 = new Word(200,300,0,"Hello", WordType.adj);
		
		ArrayList<Word> wrds = new ArrayList<Word>();
		wrds.add(word1);
		int height = 250;
		int width=500; 
		Position posProtect = new Position(0,0,0);
		Position posUnprotect =new Position(0,height+1,0);

		board = new Board(wrds, posProtect, posUnprotect,height, height, width,width);

		word1.setPosition(300, 300);

		Word foundWord = board.findUnprotectedWord(301, 301);
		assertEquals(foundWord.getValue(),"Hello");
		
		foundWord = board.findUnprotectedWord(100, 300);
		assertEquals(foundWord,null);
		
	}
	
	public void testFindProtectedWord(){
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

		board.getProtectedRegion().getWords().add(word2);

		Word foundWord = board.findProtectedWord(100, 100);
		assertEquals(foundWord.getValue(),"World");
		
		foundWord = board.findUnprotectedWord(100, 300);
		assertEquals(foundWord,null);
		
	}
	
	public void testMoveInRegion(){
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

		board.getProtectedRegion().getWords().add(word2);
		
		board.moveInProtect(word2, new Position(100,105,0));
		assertEquals(word2.getPosition().y,105);
		
		board.moveInUnProtect(word1, new Position(200,270,0));
		assertEquals(word1.getPosition().y,270);
		
	}
	
	public void testReleaseProtectedWord(){
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

		board.getProtectedRegion().getWords().add(word2);
		
		try {
			board.releaseProtectedWord(word2, new Position(200,310,0));
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(board.getProtectedRegion().getWords().size(),0);
		assertEquals(word2.getPosition().y,310);
		
		board.protectWord(word1, new Position(100,200,0));
		assertEquals(board.getUnProtectedRegion().getWords().size(),1);
		assertEquals(word1.getPosition().y,200);
		
	}
	
	public void testRestore(){
		Board board = null;
		Word word1 = new Word(200,300,0,"Hello", WordType.adj);
		Word word2 = new Word(200,302,0,"World", WordType.adj);
		Word word3 = new Word(200,304,0,"Sun", WordType.adj);
		Word word4 = new Word(200,306,0,"Light", WordType.adj);
		ArrayList<Word> wrds = new ArrayList<Word>();
		wrds.add(word1);
		wrds.add(word2);
		wrds.add(word3);
		wrds.add(word4);
		int height = 250;
		int width=500; 
		Position posProtect = new Position(0,0,0);
		Position posUnprotect =new Position(0,height+1,0);

		board = new Board(wrds, posProtect, posUnprotect,height, height, width,width);
		
		board.protectWord(word1, new Position(100,200,0));
		board.protectWord(word2, new Position(150,200,0));
		board.protectWord(word3, new Position(200,200,0));
		
        Poem p1=board.getProtectedRegion().convertWordToPoem(word1);
        Poem p2 = board.getProtectedRegion().convertWordToPoem(word2);
        
        BoardMemento bm = board.getState();
        
        assertEquals(board.getUnProtectedRegion().getWords().size(),1);
        assertEquals(board.getProtectedRegion().getPoems().size(),2);
        assertEquals(board.getProtectedRegion().getWords().size(),1);
        
        board.restore(bm);
        
        //Since new objects in momento are added to the initial, no every object is doubled
        assertEquals(board.getUnProtectedRegion().getWords().size(),2);
        assertEquals(board.getProtectedRegion().getPoems().size(),4);
        assertEquals(board.getProtectedRegion().getWords().size(),2);
		
	}

/*	public void testReleasePoem() throws Exception{
		
		Word word = new Word(5,5,0,"Hello", WordType.adj);
		Word word2 = new Word(5,5,0,"Hello", WordType.adj);
		Board board = null;
		ArrayList<Word> wrds = new ArrayList<Word>();
		wrds.add(word);
		Position posProtect = new Position(1,2,0);
		Position posUnprotect =new Position(100,200,0);
		int heightProtect = 10;
		int heightUnprotect=10; 
		int widthProtect=5; 
		int widthUnprotect=5;
		board = board.getInstance(wrds, posProtect,posUnprotect, heightProtect,heightUnprotect,widthProtect, widthUnprotect);
	

		Poem poem = new Poem();

		Row row = new Row(poem, word,word2);
		Hashtable<Integer, Row> rows = new Hashtable<Integer, Row>();
		rows.put(0,row);
		poem.pRows = rows;
		board.releasePoem(poem);
		Position position2 = new Position(5,5,0);
		assert word.getPosition().equals(position2)==false;
		
	}*/
	
	
	public void testRequestIPsFromBroker(){
		Board board = null;
		Word word = new Word(1,1,0,"Hello", WordType.adj);
		ArrayList<Word> wrds = new ArrayList<Word>();
		wrds.add(word);
		Position posProtect = new Position(0,0,0);
		Position posUnprotect =new Position(0,250,0);
		int heightProtect = 250;
		int heightUnprotect=250; 
		int widthProtect=500; 
		int widthUnprotect=500;
		board = new Board(wrds, posProtect,posUnprotect, heightProtect,heightUnprotect,widthProtect, widthUnprotect);
	
//		ArrayList<String> ips = new ArrayList<String>();
//		ips.add("123");
//		ips.add("234");
//		board.requestIPsFromBroker(ips);
//		assert board.providedIPAddress.get(1).equals("234")==true;
	}
	
	
	
	public void testIsProtected(){
		Board board = null;
		Word word = new Word(1,1,0,"Hello", WordType.adj);
		ArrayList<Word> wrds = new ArrayList<Word>();
		wrds.add(word);
		Position posProtect = new Position(0,0,0);
		Position posUnprotect =new Position(0,250,0);
		int heightProtect = 250;
		int heightUnprotect=250; 
		int widthProtect=500; 
		int widthUnprotect=500;
		board = new Board(wrds, posProtect,posUnprotect, heightProtect,heightUnprotect,widthProtect, widthUnprotect);
		
		Position tmp1 = new Position(2,3,0);
		Position tmp2 = new Position(100,260,0);
		boolean tmp = board.isProtected(tmp1);
		assertTrue(tmp);
		tmp = board.isProtected(tmp2);
		assertTrue(!tmp);
		tmp = board.isProtected(tmp1);
		assertTrue(tmp);	
		tmp = board.isUnProtected(tmp1);
		assertTrue(!tmp);
		tmp = board.isUnProtected(tmp2);
		assertTrue(tmp);	
		
	}
/*
 * (non-Javadoc)
 * @see junit.framework.TestCase#tearDown()
 */
protected void tearDown(){
	board=null;
}
}


