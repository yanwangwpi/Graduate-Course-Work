/**
 * 
 */
package himalia.controller;

import himalia.model.*;
import himalia.view.RegionPanel;

import java.util.ArrayList;

import junit.framework.TestCase;

/**
 * @author saadjei
 *
 */
public class TestPublishPoem extends TestCase {
	Board board = null;
    int heightProtect = 200;
    int heightUnprotect=200; 
    int widthProtect=300; 
    int widthUnprotect=300;
    Poem p1, p2;
    RegionPanel panel;
    Model model;
    
    PublishPoemController ppc;
    ConnectPoemController cpc;
    
    protected void setUp(){
        Word word1 = new Word(50,100,0,"He", WordType.adj);
        Word word2 = new Word(70,105,0,"llo", WordType.adj);


        ArrayList<Word> wrds = new ArrayList<Word>();
        wrds.add(word1);
        wrds.add(word2);
        
        Position posProtect = new Position(0,0,0);
        Position posUnprotect =new Position(0,200,0);

        this.board = new Board(wrds, posProtect,posUnprotect, heightProtect,heightUnprotect,widthProtect, widthUnprotect);
        assert board!=null;
        

        this.board.getProtectedRegion().addWord(word1);
        this.board.getProtectedRegion().addWord(word2);

        word1.setPosition(50, 100);
        word2.setPosition(70, 100);

        
        p1=board.getProtectedRegion().convertWordToPoem(word1);

        p2 = board.getProtectedRegion().convertWordToPoem(word2);
        
        model = new Model(board);	
		panel=new RegionPanel(model);
		// launch everything and go!
		//app.setVisible (true);
		
        ppc = new PublishPoemController(model, panel);
        cpc = new ConnectPoemController(model, panel) ;

    }        
    
	public void testPublishPoem(){
    	assertEquals(board.getProtectedRegion().getPoems().size(), 2);
    	assertTrue(cpc.select(52, 101));
    	
    	assertTrue(cpc.drag(55, 101));
    	
    	assertFalse(cpc.release(50, 105));

    	assertTrue(cpc.select(52, 101));

    	assertTrue(cpc.drag(72, 105));

    	assertTrue(cpc.release(72, 105));
    	
    	assertEquals(board.getProtectedRegion().getPoems().size(), 1);
    	
    	assertTrue(ppc.select(72, 105));
    	
    	assertEquals(board.getProtectedRegion().getPoems().size(), 0);
	}
	
    protected void tearDown(){
        board=null;
        model = null;
        panel = null;
    }
}
