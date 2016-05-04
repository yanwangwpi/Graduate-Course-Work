package himalia.controller;

import himalia.model.Board;
import himalia.model.Poem;
import himalia.model.Position;
import himalia.model.Word;

/**
 * 
 * @author yingwang
 *
 */
public class ConnectEdgeWordMove extends AbstractMove {

    Word word;
    Poem poem;

    Position sourcePos;
    Position releasedPos;
    
    int rowIndex;
    boolean fromRedo = false;
/**
 * initial the connect move
 * @param word
 * @param poem
 */
    public ConnectEdgeWordMove(Position srcPosition, Word word, Poem poem) {
    	this.word = word;
        this.poem = poem;
        this.sourcePos = new Position(srcPosition.x,srcPosition.y,0);
        this.releasedPos = new Position(word.getPosition().x,word.getPosition().y,0);
        
        rowIndex = poem.getRowIndexFromPoint(this.sourcePos);
    }

    @Override
    /**
     * connect word to a poem
     */
    public boolean execute(Board board) {
    	//for redo purposes

    	if(fromRedo){
    		poem = board.getProtectedRegion().getIntersectedPoem(poem.getPosition().x, poem.getPosition().y);   		
    		fromRedo = false;
    	}
    	word.setPosition(releasedPos);

        return board.getProtectedRegion().connectEdgeWord(poem, word);
    }

    @Override
    /**
     * redo the "connect word to poem"
     */
    public boolean undo(Board board) {
    	fromRedo = true;
        if( board.getProtectedRegion().disconnectEdgeWord(poem.getID(), word.getPosition())){
            word.setPosition(new Position(sourcePos.x,sourcePos.y,sourcePos.z));
    		if(rowIndex == 0){
    			Position pos = poem.getRow(0).getPosition();
    			poem.setPosition(pos.x, pos.y);
    		}
            return true;
        }
        //word.setPosition(releasedPos);
        return false;
    }

}
