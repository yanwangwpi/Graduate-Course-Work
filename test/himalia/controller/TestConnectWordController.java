package himalia.controller;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

import himalia.model.Board;
import himalia.model.Model;
import himalia.model.Position;
import himalia.model.Word;
import himalia.model.WordType;
import himalia.view.CyberPoetrySlamGUI;
import himalia.view.RegionPanel;
import junit.framework.TestCase;

public class TestConnectWordController extends TestCase {
    RegionPanel app;
    Model model;
    Board b;
    Word  w1;
    Word  w2;
    Word  w3;
    Word w4overlap;
    ConnectWordController mwc1;//test protected region move
    ConnectWordController mwc2;
    ConnectWordController mwc3;
    ConnectWordController mwc4;


    protected void setUp() throws Exception {
        //ArrayList<Word> wrds, Position posProtect, Position posUnprotect, 
        //int heightProtect, int heightUnprotect, int widthProtect, int widthUnprotect)
        this.b = new Board(new ArrayList(),new Position(0,0,0), new Position(0,100,0), 100, 100, 100,100);
        this.w1 = new Word(20,15,0, "Seth" ,WordType.noun);
        this.w2 = new Word(40,130,0, "Susan" ,WordType.noun);
        this.w3=new Word(150,120,0, "sugar" ,WordType.noun);
        this.w4overlap=new Word(40,40,0,"salt",WordType.noun);
        
//        this.b.addWord(w1);  
//        this.b.addWord(w2);  
//        this.b.addWord(w3);  
//        this.b.addWord(w4overlap); 
        
        this.b.getProtectedRegion().addWord(w1);
        this.b.getProtectedRegion().addWord(w2);
        this.b.getProtectedRegion().addWord(w3);
        this.b.getProtectedRegion().addWord(w4overlap);
        //make it null first for no errors
        this.model = new Model(b);    
        RegionPanel rp=new RegionPanel(model);
        // launch everything and go!
        //app.setVisible (true);
        this.mwc1=new ConnectWordController(rp,model);
        this.mwc2=new ConnectWordController(rp,model);
        this.mwc3=new ConnectWordController(rp,model);
        this.mwc4=new ConnectWordController(rp,model);

    }


    protected void tearDown() throws Exception {
        this.app=null;
        this.model=null;
        this.b=null;
    }
    
//public boolean select(int x, int y) 
     
    public void testselect(){
        //test select word in protected region: w->(20,15);
        assertEquals(true,this.mwc1.select(20, 15));
        assertEquals(false,this.mwc1.select(200, 150));
    }
    

    public void testdrag(){
        this.mwc1.select(20, 15);
        assertEquals(true,this.mwc1.drag(20, 15));
        this.mwc2.select(200, 150);
        assertEquals(false,this.mwc2.drag(200, 150));
    }

    public void testrelease(){
        //unprotected Move
        this.mwc2.select(200, 150);
        assertEquals(false,this.mwc2.drag(200, 150));
        
        this.mwc1.select(20, 15);
        assertEquals(true,this.mwc1.drag(40, 130));
        assertEquals(true,this.mwc1.release(40, 130));
        
        this.mwc3.select(150, 120);
        assertEquals(true,this.mwc1.drag(40, 130));
        assertEquals(false,this.mwc1.release(40, 130));//this one should be considered
        
        

    }
/*
    public void testmousePressed(){
        mwc4.selected=new Word(40,40,0, "Seth" ,WordType.noun);
        MouseEvent e=new MouseEvent(app, 0, 0, 0, 20,15, 0, false); 
        mwc4.mousePressed(e);
    }
    public void testmouseDragged(){
        MouseEvent e=new MouseEvent(app, 0, 0, 0, 30,15, 0, false);
        mwc4.mouseDragged(e);
    }
    public void testmouseReleased(){
        MouseEvent e=new MouseEvent(app, 0, 0, 0, 60,60, 0, false);
        mwc4.mouseReleased(e);;
    }
    */
    public void testregister(){
        mwc4.register();
    }

}
