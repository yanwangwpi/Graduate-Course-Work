package himalia.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Map.Entry;

import himalia.model.*;
import himalia.view.*;
/**
 * This is the controller that mainly do shift a row
 * @author YingWang
 *
 */

public class ShiftRowController extends MouseAdapter {

    Model model;
    RegionPanel panel;
    Board board;
    Poem selected;
    Row r;
    int mouseleftx;
    int mouserightx;
    int originalrowx;
    int originalrowy;
    int minx;
    int maxx;
    int edge=3;
    
    int deltaX, deltaY, originalX,originalY;
    int sourceRegion; //0 => Protected 1=>unprotected
    
    /**
     * This is the main constructor of the controller.
     */
    public ShiftRowController(Model m, RegionPanel panel){
        this.panel = panel;
        this.model = m;
        this.board = m.getBoard();
    }
    
    public void register() {
        panel.setActiveListener(this);
        panel.setActiveMotionListener(this);
    }
    
    @Override
    public void mousePressed(MouseEvent me) {
        select(me.getX(), me.getY());
        
    }
    @Override
    public void mouseDragged(MouseEvent me) {
        drag(me.getX(), me.getY());
    }
    
    @Override
    public void mouseReleased(MouseEvent me) {
        release(me.getX(), me.getY());
        
    }
    
    /** Separate out this function for testing purposes. */
    protected boolean select(int x, int y) {
    

        // pieces are returned in order of Z coordinate
        if(board.isProtected(new Position(x,y,0))){
            selected = board.getProtectedRegion().getIntersectedPoem(x, y);
            if (selected == null){ return false;}
            r=selected.getRowFromPoint(new Position(x,y,0));
            originalrowx=r.getPosition().x;
            originalrowy=r.getPosition().y;
        }

        originalX = selected.getPosition().x;
        originalY = selected.getPosition().y;
        
        int rkey=0;
        Iterator<Entry<Integer, Row>> rows = selected.getRows().entrySet().iterator();
        while (rows.hasNext()) {
            Entry<Integer, Row> ent = rows.next();
            if(ent.getValue()== r){
                rkey=ent.getKey();
                break;
            }
        }
        Row upperRow;
        Row lowerRow;
        if(selected.getRows().containsKey(rkey-1)){
            if(selected.getRows().containsKey(rkey+1)){
                upperRow=selected.getRows().get(rkey-1);
                lowerRow=selected.getRows().get(rkey+1);
            }else{
                upperRow=selected.getRows().get(rkey-1);
                lowerRow=null;
            }
        }else{
            if(selected.getRows().containsKey(rkey+1)){
                upperRow=null;
                lowerRow=selected.getRows().get(rkey+1);
            }else{
                upperRow=null;
                lowerRow=null;
            }
        }

        if(upperRow==null){
            if(lowerRow==null){
                minx=0;
                maxx=board.getProtectedRegion().getWidth()-r.getWidth();
            } else{
                minx=lowerRow.getPosition().x-r.getWidth();
                maxx=lowerRow.getPosition().x+lowerRow.getWidth();
            }
        }else{
            Position UP=upperRow.getPosition();
            if(lowerRow==null){
                minx=UP.x-r.getWidth();
                maxx=UP.x+upperRow.getWidth();
            } else{
                minx=Math.max(UP.x-r.getWidth(), lowerRow.getPosition().x-r.getWidth());
                maxx=Math.min(UP.x+upperRow.getWidth(), lowerRow.getPosition().x+lowerRow.getWidth());
            }
        }

        if (selected == null) { return false; }
        
        // no longer in the board since we are moving it around...
        //board().remove(w);

            
        // set anchor for smooth moving
        deltaX = x - originalrowx;
        deltaY = y - originalrowy;
        
        // paint will happen once moves. This redraws state to prepare for paint
        panel.redraw();
        return true;
    }   
    
    /** Separate out this function for testing purposes. */
    protected boolean drag (int x, int y) {
        int buttonType = 0;
        // no board? no behavior! No dragging of right-mouse buttons...
        if (buttonType == MouseEvent.BUTTON3) { return false; }
        
        if (selected == null||r==null) { return false; }
        
        //panel.paintBackground(selected);
        int oldx = selected.getPosition().x;
        int oldy = selected.getPosition().y;   
        
        
        if((x - deltaX)>(minx+edge)&&(x - deltaX)<(maxx-edge)){
            if(selected.getRows().size()==1){
                selected.setPosition(new Position(x - deltaX, originalY,0));
            }else{
                r.setPosition(x - deltaX, r.getPosition().y);
            }
            
        }else{
            if((x - deltaX)<=(minx+edge)){
                r.setPosition(minx+edge+1, originalrowy); 
            }
            if((x - deltaX)>=(maxx+edge)){
                r.setPosition(maxx-edge-1, originalrowy); 
            }

        }
     
        boolean ok = true;
    /////////need to be modified
        if (board.getProtectedRegion().detectOverlap(selected)){
            ok = false;
        }else{
            Position topPos = new Position(selected.getPosition().x,selected.getPosition().y,0);
            Position downPos = new Position(selected.getPosition().x,selected.getPosition().y+ selected.getHeight(),0);

            ok = (board.isProtected(topPos)
                    && board.isProtected(downPos)); 
        }
        
        if (!ok) {
            selected.setPosition(new Position(oldx, oldy, 0));
            r.setPosition(originalrowx, originalrowy);   
        } else {
            //panel.paintShape(selected);
            panel.repaint();
        }
        
        return ok;
    }
    
    /** Separate out this function for testing purposes. */
    protected boolean release (int x, int y) {
        boolean movePossible=false;
        Position destPos = new Position(x,y,0);

        
        if (selected == null||r==null) { return false; }
        
        if (board.isProtected(destPos) && 
                !board.getProtectedRegion().detectOverlap(selected)){
                movePossible =true;
        }
        
        if (movePossible){
            if(selected.getRows().size()==1){
                MovePoemMove move = new MovePoemMove(selected, new Position(originalX,originalY,0), new Position(x-deltaX,originalY,0));
                
                if (move.execute(board)) {
                    board.clearRedoStacks();
                    board.addUndoMove(move);
                }
            }else{
                ShiftRowMove move;
                    move = new ShiftRowMove(selected, new Position(originalrowx,originalrowy,0), new Position(r.getPosition().x,originalrowy,0));

                //if (move.execute(board)) {
                    board.addUndoMove(move);
                //}
            }

        }else{
            
            selected.setPosition(new Position(originalX, originalY,0));
        }
        
        selected = null;
        
        panel.redraw();
        panel.repaint();
        return movePossible;
    }   
}

