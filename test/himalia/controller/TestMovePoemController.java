package himalia.controller;

import himalia.model.*;
import himalia.view.*;

import java.util.ArrayList;

import junit.framework.TestCase;

public class TestMovePoemController extends TestCase{

	Board b;
	Model model;
	//SwapManager sm;
	Word w1, w2, w4, w3, t1;
	RegionPanel app;
	MovePoemController mpc;
	
	Poem p, p2;
	Row myRow,myRow2;
	protected void setUp() throws Exception {
		this.b = new Board(new ArrayList<Word>(),new Position(0,0,0), new Position(0,100,0), 100, 100, 100,100);
		ProtectedRegion proReg = b.getProtectedRegion();
		
		w1 = new Word(20,15,0, "Seth" ,WordType.noun);
		w2 = new Word(40,130,0, "Susan" ,WordType.noun);
		w3=new Word(80,120,0, "sugar" ,WordType.noun);
		w4=new Word(40,40,0,"salt",WordType.noun);
		//t1 = new Word(30,150,0, "Seth" ,WordType.noun);

		proReg.addWord(w1);
		proReg.addWord(w2);
		proReg.addWord(w3);
		
		proReg.addWord(w4);
		
		// SwapRequest sw=null;
	    //    this.sm = new SwapManager(sw);
		model = new Model(b);	
		app=new RegionPanel(model);
		// launch everything and go!
		//app.setVisible (true);
		mpc=new MovePoemController(model,app);
		
		 p = new Poem();
		 p2 = new Poem();
		 myRow = new Row(p, proReg.removeWord(w1),proReg.removeWord(w2));
			
		 myRow2 = new Row(p2, proReg.removeWord(w3),proReg.removeWord(w4));
			
	}


	protected void tearDown() throws Exception {
		app=null;
    	model=null;
    	this.b=null;
	}
	
	public void testOverlaps() throws Exception{		
		
		assertEquals(p.getRows().size(), 1);
		
		assertEquals(p2.getRows().size(), 1);
		
		//p2.setPosition(p.getPosition());
		//assertFalse(p.connectPoem(p2));
		
		Position pos = new Position(5, 10,0);
		
		assertEquals(p.setPosition(pos), true);
		assertEquals(p.pointIntersects(50, 70), false);

		assertEquals(p.pointIntersects(5,10), true);
		
		p2.setPosition(pos);
		
		assertTrue(p.intersects(p2));
		
		pos.x = 100;
		pos.y= 100;
		p2.setPosition(pos);
		
		assertFalse(p.intersects(p2));
		
		w2.setPosition(pos);
		
		assertTrue(p2.intersects(w2));

	}
	
	
	 public void testMovePoem(){
	    //test move word in protected region: w->(20,15);

	 	b.getProtectedRegion().addPoem(p);
    	assertTrue(this.mpc.select(p.getPosition().x + 4, p.getPosition().y));

    	assertFalse(this.mpc.drag(400, 130));
    	//invalid case, no word is selected
    	assertFalse(this.mpc.release(400, 130));
    	
    	assertTrue(this.mpc.select(p.getPosition().x + 4, p.getPosition().y));

    	//assertTrue(mpc.release(30, 40));
    	
//    	assertTrue(this.mpc.select(p.getPosition().x + 4, p.getPosition().y));
//
//    	assertTrue(mpc.release(p.getPosition().x, p.getPosition().y));
    }
	    
}
