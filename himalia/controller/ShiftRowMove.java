
package himalia.controller;

import himalia.model.Board;
import himalia.model.Poem;
import himalia.model.Position;

/**
 * This is the move that support shift row
 * @author YingWang
 *
 */
public class ShiftRowMove extends AbstractMove {
    Position oldPos;
    Position newPos;
    Position currentPos;
    Poem poem;
    
/**
 * initialization of the move
 * @param poem
 * @param oldPos
 * @param newPos
 */
    public ShiftRowMove(Poem poem,Position oldPos,Position newPos) {
        
        this.oldPos = new Position(oldPos.x,oldPos.y,0);
        this.newPos = new Position(newPos.x,newPos.y,0);
        this.poem = poem;
    }

    @Override
    
    /**
     * do/Redo the excution
     */
    public boolean execute(Board board) {
       // Position Pos=new Position(currentPos.x+1,currentPos.y+1,0);
        this.poem.shiftRow(oldPos, newPos);
        return true;
    }

    @Override
    /**
     * undo the execution
     */
    public boolean undo(Board board) {
       // Position Pos=new Position(newPos.x+1,newPos.y,0);
        this.poem.shiftRow(newPos, oldPos);
        return true;
    }

}

