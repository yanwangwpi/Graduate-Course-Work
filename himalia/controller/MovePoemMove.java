
package himalia.controller;

import himalia.model.Board;
import himalia.model.Poem;
import himalia.model.Position;

public class MovePoemMove extends AbstractMove {
    Position oldPos;
    Position newPos;
    Poem poem;
    

    public MovePoemMove(Poem poem,Position oldPos, Position newPos) {
        
        this.oldPos = oldPos;
        this.newPos = newPos;
        this.poem = poem;
    }

    @Override
    public boolean execute(Board board) {
        // TODO Auto-generated method stub
        this.poem.setPosition(newPos);
        return true;
    }

    @Override
    public boolean undo(Board board) {
        // TODO Auto-generated method stub
        this.poem.setPosition(oldPos);
        return true;
    }

}

