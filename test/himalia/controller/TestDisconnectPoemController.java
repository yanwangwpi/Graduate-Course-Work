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
public class TestDisconnectPoemController extends TestCase{

	Board board = null;
    int heightProtect = 200;
    int heightUnprotect=200; 
    int widthProtect=300; 
    int widthUnprotect=300;
    Poem p1, p2, p3;
    RegionPanel panel;
    Model model;
    Word word1, word2, word3;
    ProtectedRegion proReg;
    DisconnectPoemController dpc;
    ConnectPoemController cpc;
    
    //UndoController undC;
    
    protected void setUp(){
         word1 = new Word(50,100,0,"He", WordType.adj);
         word2 = new Word(70,105,0,"llo", WordType.adj);
         word3 = new Word(100,150,0,"Seth", WordType.adj);
        //Word word4 = new Word(150,150,0,"Same", WordType.adj);
        //Word word5 = new Word(0,0,0,"Guess", WordType.adj);

        ArrayList<Word> wrds = new ArrayList<Word>();
        wrds.add(word1);
        wrds.add(word2);
        
        Position posProtect = new Position(0,0,0);
        Position posUnprotect =new Position(0,200,0);

        this.board = new Board(wrds, posProtect,posUnprotect, heightProtect,heightUnprotect,widthProtect, widthUnprotect);
        assert board!=null;
        proReg = this.board.getProtectedRegion();

        proReg.addWord(word1);
        proReg.addWord(word2);
        proReg.addWord(word3);
        //proReg.addWord(word4);
        //proReg.addWord(word5);
        word1.setPosition(50, 100);
        word2.setPosition(70, 100);
        word3.setPosition(100, 150);
        //word4.setPosition(150, 150);
        //word5.setPosition(1, 1);
        
        p1=proReg.convertWordToPoem(word1);

        p2 = proReg.convertWordToPoem(word2);
        
        p3 = proReg.convertWordToPoem(word3);
        
        model = new Model(board);	
		panel=new RegionPanel(model);
		// launch everything and go!
		//app.setVisible (true);
		
        dpc = new DisconnectPoemController(model, panel);
        cpc = new ConnectPoemController(model, panel);
        
        //undC = new UndoController(model, panel);
    }        
    
    public void testRelease(){
    	//Test two connected single word rows
    	assertEquals(proReg.getPoems().size(), 3);
    	assertTrue(cpc.select(52, 101));
    	
    	assertTrue(cpc.drag(55, 101));
    	
    	assertFalse(cpc.release(50, 105));

    	assertTrue(cpc.select(52, 101));

    	assertTrue(cpc.drag(72, 105));

    	assertTrue(cpc.release(72, 105));
    	
    	assertEquals(proReg.getPoems().size(), 2);

    	assertTrue(dpc.select(72, 105));
    	
    	assertFalse(dpc.drag(150, 250));
    	
    	assertTrue(dpc.select(72, 105));
    	
    	assertTrue(dpc.drag(200, 150));

    	assertTrue(dpc.release(200, 150));
    	
    	assertEquals (proReg.getPoems().size(), 1);
    	
    	
        p1=proReg.convertWordToPoem(word1);

        p2 = proReg.convertWordToPoem(word2);
        
        //p3 = proReg.convertWordToPoem(word3);
        
        Position pos = new Position(p1.getPosition().x+1, p1.getPosition().y+1,0 );
        p2.setPosition(pos);
        
        assertTrue(p1.connectPoem(proReg.deletePoem(p2)));
        
        p3.setPosition(pos);
        
        assertTrue(p1.connectPoem(proReg.deletePoem(p3)));
        
        assertTrue(dpc.select(pos.x, pos.y));
        
        assertTrue(dpc.drag(70,80));
        
        assertTrue(dpc.release(120, 85));
        
        assertEquals(proReg.getPoems().size(), 1);
        
        //undC.process();
        
        //assertEquals(proReg.getPoems().size(), 2);
        
    }
    
    protected void tearDown(){
        board=null;
    }
}
