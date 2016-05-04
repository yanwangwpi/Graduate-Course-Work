package himalia.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import himalia.model.*;
import himalia.view.RegionPanel;
/**
 * 
 * @author YingWang, Seth
 *
 */
public class ConnectWordController extends MouseAdapter {
    Model model;
    RegionPanel panel;
    Word selected;

    int originalx;
    int originaly;
    int deltaX;
    int deltaY;



/**
 * initialize connect word controller
 * @param panel
 * @param model
 */
    public ConnectWordController(RegionPanel panel,Model model) {
        this.model = model;
        this.panel = panel;
    }

    /** 
     * Set up press events but no motion events. 
     */
    public void register() {
        panel.setActiveListener(this);
        panel.setActiveMotionListener(this);
    }

     
   @Override
   /**
    * mouse press event
    */
   public void mousePressed(MouseEvent me) {
       select(me.getX(), me.getY());
   }
   
   //the selected word are set
   /**
    * select 
    * @param x
    * @param y
    * @return
    */
   public boolean select(int x, int y) {
    // TODO Auto-generated method stub

       Board b = model.getBoard();
       selected = b.findProtectedWord(x, y);
       if (selected == null) { return false; }
       //b.getProtectedRegion().removeWord(selected);
       
       //model.setSelected(selected);
       originalx = selected.getPosition().x;
       originaly = selected.getPosition().y;
           
       // set anchor for smooth moving
       deltaX = x - originalx;
       deltaY = y - originaly;
       
       // paint will happen once moves. This redraws state to prepare for paint
       panel.redraw();
       return true;
    
}

/**
    * Whenever mouse is dragged, attempt to start object.
    * This is a GUI controller.
    */
   @Override
   /**
    * mouse drag event
    */
   public void mouseDragged(MouseEvent me) {
       drag(me.getX(), me.getY());
   }
   
   /**
    * drag
    * @param x
    * @param y
    * @return
    */
   public boolean drag(int x, int y) {
    // TODO Auto-generated method stub
       // no board? no behavior! No dragging of right-mouse buttons...
       int buttonType = 0;
       if (buttonType == MouseEvent.BUTTON3) { return false; }

       
       if (selected == null) { 
           return false;
       }
       
       panel.paintBackground(selected);
       int oldx = selected.getPosition().x;
       int oldy = selected.getPosition().y;
       
       selected.setPosition(x - deltaX, y - deltaY);
       
       boolean ok = true;
       /*
      //rework this part include all the words on the boards
       for (Word s : model.getBoard().getProtectedRegion().getWords()) {
           if (s.intersect(x - deltaX, y - deltaY)) {
               ok = false;
               break;
           }
       }
       
       //rework this part include all the poems on the boards
       for (Poem p : model.getBoard().getProtectedRegion().getPoems()) {
           if (p.intersects(selected.getPosition())) {
               ok = false;
               break;
           }
       }
       */
       if (!ok) {
           selected.setPosition(oldx, oldy);
       } else {
           panel.repaint();
       }
       
       return true;
    
}

/**
    * Whenever mouse is released, complete the move. 
    * This is a GUI controller.
    */
   @Override
   /**
    * mouse release event
    */
   public void mouseReleased(MouseEvent me) {
       release(me.getX(), me.getY());
   }

   /**
    * release
    * @param x
    * @param y
    * @return
    */
public boolean release(int x, int y) {
	boolean releaseState=false;
    if (selected == null) { return false; }
    
    Board b = model.getBoard(); 
    ProtectedRegion pR = b.getProtectedRegion();
    //Position releasePos = new Position(x,y,0);
    
    ArrayList<Poem> pp=pR.getIntersectedPoems(selected);
    
    ArrayList<Word> ww=pR.getIntersectedWords(selected);
    Position originalPos=new Position(originalx,originaly,0);
    
    if(pp.size()==1 && ww.size()==0){
        Poem poem=pp.get(0);
        //model.getBoard().getProtectedRegion().connectWordToPoem(selected, p);
        ConnectEdgeWordMove move= new ConnectEdgeWordMove(originalPos,selected, poem);
        
        releaseState = move.execute(b);
        if(releaseState){
            b.addUndoMove(move);
            b.clearRedoStacks();
        }
    }else if(pp.size()==0 && ww.size()==1){
	        Word word=ww.get(0);

	        ConnectWordsMove move= new ConnectWordsMove(word,selected,originalPos);
	        
	        releaseState = move.execute(b);
	        if(releaseState){
	            b.addUndoMove(move);
	        }
	    }
    
    if (!releaseState){
    	
    	selected.setPosition(originalx, originaly);
    }
    
    // no longer selected
    model.setSelected(null);
    
    panel.redraw();
    panel.repaint();
    
    return releaseState;
    
}

}
