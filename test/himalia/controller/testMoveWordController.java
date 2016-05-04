package himalia.controller;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

import himalia.model.Board;
import himalia.model.Model;
import himalia.model.Position;
import himalia.model.ProtectedRegion;
import himalia.model.UnprotectedRegion;
import himalia.model.Word;
import himalia.model.WordType;
import himalia.view.CyberPoetrySlamGUI;
import himalia.view.RegionPanel;
import junit.framework.TestCase;
/**
 * 
 * @author susanqin
 *
 */

public class testMoveWordController extends TestCase {
	RegionPanel app;
	Model model;
	Board b;
	Word  w1;
	Word  w2;
	Word  w3;
	Word w4overlap;
	Word t1;
	//SwapManager sm;
	MoveWordController mwc1;//test protected region move
	MoveWordController mwc2;//unprotected region move
	MoveWordController mwc3;//invalid
	MoveWordController mwc4;
	MoveWordController mwc5;//invalid from unpro->pro

	protected void setUp() throws Exception {
		//ArrayList<Word> wrds, Position posProtect, Position posUnprotect, 
		//int heightProtect, int heightUnprotect, int widthProtect, int widthUnprotect)
		this.b = new Board(new ArrayList(),new Position(0,0,0), new Position(0,100,0), 100, 100, 100,100);
//		w1 = new Word(20,15,0, "Seth" ,WordType.noun);
//		w2 = new Word(40,130,0, "Susan" ,WordType.noun);
//		w3=new Word(800,120,0, "sugar" ,WordType.noun);
//		w4overlap=new Word(40,40,0,"salt",WordType.noun);
//		t1 = new Word(30,150,0, "Seth" ,WordType.noun);
//		
//		b.addWord(w1);	
//		b.addWord(w2);	
//		b.getProtectedRegion().addWord(w1);
//		b.getUnProtectedRegion().addWord(w2);
//		b.getProtectedRegion().addWord(w4overlap);
		model = new Model(b);	
		app=new RegionPanel(model);
		// launch everything and go!
		//app.setVisible (true);
		mwc1=new MoveWordController(model,app);
		mwc2=new MoveWordController(model,app);
		mwc3=new MoveWordController(model,app);//invalid 
		/*mwc1=new MoveWordController(app,model);
		mwc2=new MoveWordController(app,model);
		mwc3=new MoveWordController(app,model);//invalid 
		mwc4=new MoveWordController(app,model);
		mwc5=new MoveWordController(app,model);*/
	}


	protected void tearDown() throws Exception {
		app=null;
    	model=null;
    	this.b=null;
	}
	
//public boolean select(int x, int y) 
	 
    public void testselect(){
    	//test move word in protected region: w->(20,15);
        this.w1 = new Word(20,15,0, "Seth" ,WordType.noun);
//    	this.b.addWord(w1);
    	this.b.getProtectedRegion().addWord(w1);
    	assertEquals(true,this.mwc1.select(20, 15));
    	//test move word in unprotected region: w->(20,15);
    	this.w2 = new Word(40,130,0, "Susan" ,WordType.noun);

    	try{
    	this.b.addUnprotectedWord(w2);
    	}
    	catch(CloneNotSupportedException e) {
			e.printStackTrace();
		}

    	assertEquals(true,this.mwc2.select(40, 130));
    	//invalid case, no word is selected
    	assertEquals(false,this.mwc3.select(150,120));
    }
    
    // * test:public boolean drag(int x, int y)
     
    public void testdrag(){
        this.w1 = new Word(20,15,0, "Seth" ,WordType.noun);
//    	this.b.addWord(this.w1);
    	this.b.getProtectedRegion().addWord(this.w1);
    	mwc1.select(20, 15);
    	assertEquals(true,mwc1.drag(30, 15));
    }
    public void testrelease(){
    	//unprotected Move
        this.w2 = new Word(40,130,0, "Susan" ,WordType.noun);

	
    	try{
    		this.b.addUnprotectedWord(w2);
    	}
    	catch(CloneNotSupportedException e) {
    		e.printStackTrace();
    	}	

    	this.mwc2.select(40, 130);
    	assertEquals(true,this.mwc2.release(50, 150));
    	//Protected Move
    	//move to overlapping place
    	//System.out.println(model.getBoard().getProtectedRegion().getWords().get(0).getPosition().y);
    	this.w1 = new Word(20,15,0, "Seth" ,WordType.noun);
//    	this.b.addWord(this.w1);
    	this.b.getProtectedRegion().addWord(this.w1);
    	w3 = new Word(40,40,0, "Seth1" ,WordType.noun);
//    	this.b.addWord(this.w3);
    	this.b.getProtectedRegion().addWord(this.w3);
    	this.mwc1.select(20, 15);
    	assertEquals(true,this.mwc1.release(40, 40));
 
    	
    	
    }

    	/*
    	//mwc1.word=new Word(20,15,0, "Seth" ,WordType.noun);
    	MouseEvent e=new MouseEvent(app, 0, 0, 0, 20,15, 0, false);	
    	mwc1.mousePressed(e);
    }
    
    public void testmouseDragged(){
    	MouseEvent e1=new MouseEvent(app, 0, 0, 0, 20,15, 0, false);	
    	mwc1.mousePressed(e1);
    	MouseEvent e2=new MouseEvent(app, 0, 0, 0, 30,15, 0, false);
    	mwc1.mouseDragged(e2);
    }
    
    public void testmouseReleased(){
    	MouseEvent e1=new MouseEvent(app, 0, 0, 0, 20,15, 0, false);	
    	mwc1.mousePressed(e1);
    	MouseEvent e2=new MouseEvent(app, 0, 0, 0, 60,60, 0, false);
    	mwc1.mouseReleased(e2);;
    }*/
    public void testregister(){
    	mwc1.register();
    }
}
