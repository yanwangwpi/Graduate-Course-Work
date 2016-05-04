package himalia.controller;

import himalia.model.Board;

import himalia.model.Position;
import himalia.model.Word;

/**
 * This class describes move of word, within or cross regions
 * @author Yan Wang
 *
 */
public class MoveWordMove extends  AbstractMove{
	
	/**old Position of last Move*/
	Position oldPos;
	/**Current Position*/
	Position newPos;
	/**word that is relevant in this move*/ 
	Word word;

	/**
	 *  Constructor, instantiate
	 * @param w
	 * @param op
	 * @param np
	 */
	public MoveWordMove(Word w,Position op, Position np){
		this.word=w;
		this.oldPos=new Position(op.x,op.y,op.z);
		this.newPos=new Position(np.x,np.y,np.z);
	}
	
	/**
	 * excute the move based on the location of oldPos and newPos
	 */
	@Override
	public boolean execute(Board board) {
		if (board.isProtected(this.oldPos)){
			if (board.isProtected(this.newPos)){
				board.moveInProtect(this.word, this.newPos);
				return true;
			}
		
			else if (board.isUnProtected(this.newPos)){
				try {
					board.releaseProtectedWord(this.word, this.newPos);
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				return true;
			}
			
			
		}
		else if (board.isUnProtected(this.oldPos)){
			if (board.isProtected(this.newPos)){
				board.protectWord(this.word, this.newPos);
				return true;
				}
			else if(board.isUnProtected(this.newPos)){
				board.moveInUnProtect(this.word, this.newPos);
				return true;
			}
			
		} 
		return false;
	}

	/**
	 * undo the move
	 */
	@Override
	public boolean undo(Board board) {
		if (board.isProtected(this.newPos)){
			if (board.isProtected(this.oldPos)){
				board.moveInProtect(this.word, this.oldPos);
				return true;
			}
		
			else if(board.isUnProtected(this.oldPos)){
				try {
					board.releaseProtectedWord(this.word, this.oldPos);
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				return true;
			}
			
		}
		else if (board.isUnProtected(this.newPos)){
			if (board.isProtected(this.oldPos)){
				board.protectWord(this.word, this.oldPos);
				return true;
				}
			else if (board.isUnProtected(this.oldPos)){
				board.moveInUnProtect(this.word, this.oldPos);
				return true;
			}
		} 
		return false;
	}

}
