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

public class TestConnectPoemController extends TestCase{
	Board board = null;
    int heightProtect = 200;
    int heightUnprotect=200; 
    int widthProtect=300; 
    int widthUnprotect=300;
    Poem p1, p2;
    RegionPanel panel;
    Model model;
    
    ConnectPoemController cpc;
    
    protected void setUp(){
        Word word1 = new Word(50,100,0,"He", WordType.adj);
        Word word2 = new Word(70,105,0,"llo", WordType.adj);
        Word word3 = new Word(100,150,0,"Seth", WordType.adj);
        Word word4 = new Word(150,150,0,"Same", WordType.adj);
        Word word5 = new Word(0,0,0,"Guess", WordType.adj);

        ArrayList<Word> wrds = new ArrayList<Word>();
        wrds.add(word1);
        wrds.add(word2);
        
        Position posProtect = new Position(0,0,0);
        Position posUnprotect =new Position(0,200,0);

        this.board = new Board(wrds, posProtect,posUnprotect, heightProtect,heightUnprotect,widthProtect, widthUnprotect);
        assert board!=null;
        

        this.board.getProtectedRegion().addWord(word1);
        this.board.getProtectedRegion().addWord(word2);
        this.board.getProtectedRegion().addWord(word3);
        this.board.getProtectedRegion().addWord(word4);
        this.board.getProtectedRegion().addWord(word5);
        word1.setPosition(50, 100);
        word2.setPosition(70, 100);
        word3.setPosition(100, 150);
        word4.setPosition(150, 150);
        word5.setPosition(1, 1);
        
        p1=board.getProtectedRegion().convertWordToPoem(word1);

        p2 = board.getProtectedRegion().convertWordToPoem(word2);
        model = new Model(board);	
		panel=new RegionPanel(model);
		// launch everything and go!
		//app.setVisible (true);
		
        cpc = new ConnectPoemController(model, panel);
    }
    
//    public void testSelect(){
//    	assertTrue(cpc.select(52, 101));
//    }
//    
//    public void testDrag(){
//    	assertTrue(cpc.select(52, 101));
//    	assertTrue(cpc.drag(55, 101));
//    }

    public void testRelease(){
    	assertEquals(board.getProtectedRegion().getPoems().size(), 2);
    	assertTrue(cpc.select(52, 101));
    	
    	assertTrue(cpc.drag(55, 101));
    	
    	assertFalse(cpc.release(50, 105));

    	assertTrue(cpc.select(52, 101));

    	assertTrue(cpc.drag(72, 105));

    	assertTrue(cpc.release(72, 105));
    	
    	assertEquals(board.getProtectedRegion().getPoems().size(), 1);
    	
    	
    	assertTrue(cpc.select(100,150));
    	
    	assertTrue(cpc.drag(152, 150));
    	
    	assertTrue(cpc.release(170, 200));

    	assertFalse(cpc.select(100,150));
    	
    	assertTrue(cpc.select(1,2));
    	
    	//assertTrue(cpc.drag(152, 150));
    	
    	assertFalse(cpc.release(5, 5));
    }
    
    protected void tearDown(){
        board=null;
    }

}
