package himalia.controller;

import himalia.model.Board;
import himalia.model.Poem;
import himalia.model.Position;
import himalia.model.Word;

/**
 * 
 * @author saadjei
 *
 */
public class DisconnectWordMove extends AbstractMove {

	/** the poem from which the word is being disconnected. */
	Poem poem;
	/** The disconnected word */
	Word disconnectedWord;
	
	/** 
	 * The word resulting from the disconnected poem. 
	 * This is set only if the poem from which a word is being disconnected is a two-word single-row poem. 
	 */
	Word degradedOnePoemWord;
	
	Position posNew;
	Position posOld;
	int rowIndex;
	int wrdIndex;
	
	/** This is the constructor for the disconnect word move. */
	public DisconnectWordMove(Poem poem, int rowIndex, int wrdIndex, Position posNew,Position posOld){
		this.poem = poem;
		this.posNew = new Position(posNew.x,posNew.y,posNew.z);
		this.posOld = posOld;
		this.rowIndex = rowIndex;
		this.wrdIndex = wrdIndex;
	}
	
	
	@Override
	/** Execute the disconnection of a word from a poem.*/
	public boolean execute(Board board) {
		
		poem = board.getProtectedRegion().getIntersectedPoem(poem.getPosition().x, poem.getPosition().y);
		disconnectedWord = poem.disconnectEdgeWord(this.rowIndex, this.wrdIndex);

		if (disconnectedWord !=null){
			disconnectedWord.setPosition(posNew);
			board.getProtectedRegion().addWord(disconnectedWord);
			//check if the resulting poem is a one word poem and degrade it
			if(poem.isSingleWordPoem()){
				degradedOnePoemWord=board.getProtectedRegion().degradeOneWordPoem(poem);
				poem =null;
			}else if(rowIndex == 0){
    			Position pos = poem.getRow(0).getPosition();
    			poem.setPosition(pos.x, pos.y);
    		}
			return true;
		}
		return false;
	}

	@Override
	/** Undo the disconnection of the words. */
	public boolean undo(Board board) {
		
		disconnectedWord.setPosition(posOld);
		if(degradedOnePoemWord != null){
			poem = board.getProtectedRegion().formOneWordPoem(degradedOnePoemWord);
			degradedOnePoemWord= null;
		}
		poem = board.getProtectedRegion().getIntersectedPoem(poem.getPosition().x, poem.getPosition().y);
		if(poem.getRow(rowIndex).connectEdgeWord(disconnectedWord, true)){
			board.getProtectedRegion().removeWord(disconnectedWord);
			disconnectedWord = null;
			return true;
		}
		
		return false;
	}
	

}
