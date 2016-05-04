package himalia.controller;

import himalia.model.*;

/**
 * 
 * @author YingWang
 *
 */
public class ConnectWordsMove extends AbstractMove {
    Word word1;
    Word word2;
    Poem poem;
    Position pos1;
    Position pos2;
    Position pos3;
    /**
     * initialize connect two words
     * @param w1
     * @param w2
     */
    public ConnectWordsMove(Word w1, Word w2,Position p2) {
 
        this.word1 = w1;
        this.word2 = w2;
        this.pos1=new Position(w1.getPosition().x,w1.getPosition().y,0);
        this.pos2=new Position(p2.x,p2.y,0);
        //get the r position.
        this.pos3=new Position(w2.getPosition().x,w2.getPosition().y,0);
    }

    @Override
    /**
     * connect two words
     */
    public boolean execute(Board board) {
    	ProtectedRegion proReg = board.getProtectedRegion();
    	//this part is for redo
    	//reset the position of the second word to its previous position
    	word2.setPosition(pos3);
    	
    	if (board.getProtectedRegion().connectWord(word1, word2)){
    		poem = proReg.getIntersectedPoem(pos1.x, pos1.y);
	        return true;
    	}
        return false;
    }

    @Override
    /**
     * redo the "connect two words"
     * @author saadjei
     */
    public boolean undo(Board board) {
    	ProtectedRegion proReg = board.getProtectedRegion();

        Position pp=new Position(word2.getPosition().x + 1,word2.getPosition().y,0);
        poem = proReg.getIntersectedPoem(pos1.x, pos1.y);
        
        if(poem == null){return false;}
        
        word2 = poem.disconnectEdgeWordAtPoint(pp.x, pp.y);
        if (word2 != null){proReg.addWord(word2);}
        
        if(word2 != null || poem.isSingleWordPoem()){
        	
        	word1 = proReg.degradeOneWordPoem(poem);
        	
            word2.setPosition(pos2);
            poem = null;
            return true;
        }
        
        return false;
    }

}
