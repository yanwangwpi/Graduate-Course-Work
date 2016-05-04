package himalia.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import himalia.model.*;

import himalia.view.RegionPanel;

/**
 * 
 * @author saadjei
 */
public class DisconnectWordController extends MouseAdapter{
	
	Model model;
    RegionPanel panel;
    Word selectedWord;
    Poem selectedPoem;
    int originalx;
    int originaly;
    int deltaX;
    int deltaY;
    
    int rowIndex;
    int wordIndex;

    /**
     * Constructor
     * @param panel
     * @param model
     */
    public DisconnectWordController(RegionPanel panel,Model model) {
        this.model = model;
        this.panel = panel;
    }

    /** Set up press events but no motion events. */
    public void register() {
        panel.setActiveListener(this);
        panel.setActiveMotionListener(this);
    }

     
   @Override
   public void mousePressed(MouseEvent me) {
       select(me.getX(), me.getY());
   }
   
   //the selected word are set
   public boolean select(int x, int y) { 
       Board b = model.getBoard();
       ArrayList<Object> result= b.findPoemWord(x, y);
       if( result == null){
    	   return false;
       }
       
       rowIndex = (int) result.get(0);
       
       wordIndex = (int) result.get(1);
       
       selectedWord = (Word) result.get(2);

       if (selectedWord == null) { return false; }

       selectedPoem = (Poem) result.get(3);
       
       //check if the selected row has just one word in it. 
       //prevent the situation where a word row has just one word which is being disconnected.
       if (selectedPoem.getRow(rowIndex).getWords().size()==1){
    	   selectedWord = null;
    	   selectedPoem = null;
    	   rowIndex = -1;
    	   wordIndex = -1;
    	   return false;
       }
       originalx = selectedWord.getPosition().x;
       originaly = selectedWord.getPosition().y;
           
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
   public void mouseDragged(MouseEvent me) {
       drag(me.getX(), me.getY());
   }
   
   /** Drag the word to the position x, y. */
   public boolean drag(int x, int y) {
       // no board? no behavior! No dragging of right-mouse buttons...
       int buttonType = 0;
       if (buttonType == MouseEvent.BUTTON3) { return false; }

       
       if (selectedWord == null) { 
           return false;
       }
       
       
	       panel.paintBackground(selectedWord);
	       int oldx = selectedWord.getPosition().x;
	       int oldy = selectedWord.getPosition().y;
	       
	       selectedWord.setPosition(x - deltaX, y - deltaY);
	       
	       boolean ok = true;
	
	       if (!ok) {
	           selectedWord.setPosition(oldx, oldy);
	       } else {
	           panel.repaint();
	       }
       return true;
    
       
}

/**
    * Whenever the mouse is released, complete the move. 
    * This is a GUI controller.
    */
   @Override
   public void mouseReleased(MouseEvent me) {
       release(me.getX(), me.getY());
   }

   /** execute release operation. */
	public boolean release(int x, int y) {
		boolean executedSuccessfully = false;
	    if (selectedWord == null) { return false; }
	    
	    Board b = model.getBoard(); 
	    ProtectedRegion pR = b.getProtectedRegion();
	    
	    ArrayList<Poem> pp=pR.getIntersectedPoems(selectedWord);
	    
	    ArrayList<Word> ww=pR.getIntersectedWords(selectedWord);
	    
	    if(pp.size()==1 && ww.size()==0){
	        DisconnectWordMove move= new DisconnectWordMove(selectedPoem, rowIndex, wordIndex, selectedWord.getPosition(), new Position(originalx, originaly, 0));
	        executedSuccessfully = move.execute(b);
	        if(executedSuccessfully){
	            b.addUndoMove(move);
	            b.clearRedoStacks();
	        }
	    }
	    
	    if(!executedSuccessfully){	    	
	    	selectedWord.setPosition(originalx, originaly);
		}
	 
	    selectedWord = null;
	    panel.redraw();
	    panel.repaint();
	    
	    return true;
	    
	}

}
