package himalia.controller;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

import himalia.model.Board;
import himalia.model.Model;
import himalia.model.Poem;
import himalia.model.Position;
import himalia.model.Row;
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
public class TestDisconnectWordController extends TestCase {
    RegionPanel app;
    Model model;
    Board b;
    Word  w1;
    Word  w2;
    Word  w3;
    Word w4overlap;

    RegionPanel rp;
    DisconnectWordController mwc1;//test protected region move
    DisconnectWordController mwc2;
    DisconnectWordController mwc3;
    DisconnectWordController mwc4;
    Poem poem;
    Row r;

    protected void setUp() throws Exception {

        this.b = new Board(new ArrayList(),new Position(0,0,0), new Position(0,100,0), 100, 100, 100,100);
        this.w1 = new Word(102,203,0, "Seth" ,WordType.noun);
        this.w2 = new Word(103,205,0, "Susan" ,WordType.noun);
        this.w3=new Word(150,120,0, "sugar" ,WordType.noun);
        this.w4overlap=new Word(40,40,0,"salt",WordType.noun);
        poem=new Poem();
        r=new Row(poem, w1);
        r.connectEdgeWord(w2, false);
        

        this.b.getProtectedRegion().addWord(this.w3);
        this.b.getProtectedRegion().addWord(this.w4overlap);
        this.b.getProtectedRegion().addPoem(poem);

        this.model = new Model(this.b);    
         rp=new RegionPanel(this.model);
        // launch everything and go!
        this.mwc1=new DisconnectWordController(rp,this.model);
        this.mwc2=new DisconnectWordController(rp,this.model);
        this.mwc3=new DisconnectWordController(rp,this.model);
        this.mwc4=new DisconnectWordController(rp,this.model);

    }


    protected void tearDown() throws Exception {
        this.app=null;
        this.model=null;
        this.b=null;
    }
    
     
    public void testselect(){
      mwc1.select(103,205);
      mwc1.select(13,205);
    }
    

    public void testdrag(){
    	mwc1.select(103,205);
    	mwc1.drag(150,205);
    }

    public void testrelease(){
    	mwc1.select(103,205);
    	mwc1.release(153,205);
        

    }

    public void testmousePressed(){
      
        MouseEvent e=new MouseEvent(rp, 0, 0, 0, 103,205, 0, false);
        mwc1.mousePressed(e);
      
    }
    public void testmouseDragged(){
    	 MouseEvent e=new MouseEvent(rp, 0, 0, 0, 133,205, 0, false);
         mwc1.mouseDragged(e);
    }
    public void testmouseReleased(){
    	 MouseEvent e=new MouseEvent(rp, 0, 0, 0, 153,205, 0, false);
         mwc1.mouseReleased(e);;
    }
    
    public void testregister(){
        mwc4.register();
    }

}
