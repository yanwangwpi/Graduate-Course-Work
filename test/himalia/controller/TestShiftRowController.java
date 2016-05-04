package himalia.controller;

import java.util.ArrayList;

import himalia.model.Board;
import himalia.model.Model;
import himalia.model.Poem;
import himalia.model.Position;
import himalia.model.Row;
import himalia.model.Word;
import himalia.model.WordType;
import himalia.view.RegionPanel;
import junit.framework.TestCase;

public class TestShiftRowController extends TestCase {
    Board b;
    Model model;
    //SwapManager sm;
    Word w1, w2, w4, w3, t1,w5,w6,w7,w8;
    RegionPanel app;
    ShiftRowController mpc,mpc1,mpc2,mpc3;
    
    Poem p, p2,p3,p4;
    Row myRow,myRow2,myRow3,myRow4;

    protected void setUp() throws Exception {

       this.b = new Board(new ArrayList(),new Position(0,0,0), new Position(0,100,0), 100, 100, 100,100);
        
        w1 = new Word(20,15,0, "Seth" ,WordType.noun);
        w2 = new Word(40,130,0, "Susan" ,WordType.noun);
        w3=new Word(20,120,0, "sugar" ,WordType.noun);
        w4=new Word(40,40,0,"salt",WordType.noun);
        w5 = new Word(20,150,0,"Same", WordType.adj);
        w6 = new Word(0,0,0,"Guess", WordType.adj);
        t1 = new Word(30,150,0, "Seth" ,WordType.noun);
        w7 = new Word(80,150,0,"Same", WordType.adj);
        w8 = new Word(0,0,0,"Guess", WordType.adj);
        /*
        b.getProtectedRegion().addWord(w1);
        b.getProtectedRegion().addWord(w2);
        b.getProtectedRegion().addWord(w3);
        
        b.getProtectedRegion().addWord(w4);
        b.getProtectedRegion().addWord(w5);
        b.getProtectedRegion().addWord(w6);
*/
        model = new Model(b);   
        app=new RegionPanel(model);

        mpc=new ShiftRowController(model,app);
        mpc1=new ShiftRowController(model,app);
        mpc2=new ShiftRowController(model,app);
        mpc3=new ShiftRowController(model,app);
        
         p = new Poem();
         p2 = new Poem();
         p3 = new Poem();
         p4 = new Poem();
         
         myRow = new Row(p, w1, w2);
         myRow2 = new Row(p2, w3, w4);
         myRow3 = new Row(p3, w5, w6);
         myRow4 = new Row(p4, w7, w8);
         assertTrue(p.connectBottomPoem(p2));
         p.connectBottomPoem(p3);
         b.getProtectedRegion().addPoem(p);
         b.getProtectedRegion().addPoem(p4);

    }

    protected void tearDown() throws Exception {
        app=null;
        model=null;
        this.b=null;
    }
    
    public void testShiftRow(){
        //test move word in protected region: w->(20,15);

        int x,x1,x2,x3;
        int y,y1,y2,y3;
        x=p.getRow(0).getPosition().x;
        y=p.getRow(0).getPosition().y;
        
        assertTrue(this.mpc.select(x, y));

        assertTrue(this.mpc.drag(x-100, y));
        //invalid case, no Row is selected
        assertFalse(this.mpc.release(x-100, y));
      
        x1=p.getRow(1).getPosition().x;
        y1=p.getRow(1).getPosition().y;
        assertTrue(this.mpc1.select(x1, y1));
        
        assertTrue(this.mpc1.drag(x1-1, y1));    
        assertTrue(this.mpc1.release(x-1, y));
        x2=p.getRow(2).getPosition().x;
        y2=p.getRow(2).getPosition().y;
        assertTrue(this.mpc2.select(x2, y2));
        
        x3=p4.getRow(0).getPosition().x;
        y3=p4.getRow(0).getPosition().y;
        /*
        assertTrue(this.mpc3.select(x3, y3));

        assertTrue(this.mpc3.drag(x3-100, y3));

        assertFalse(this.mpc3.release(x3-100, y3));
       */

    }
}
