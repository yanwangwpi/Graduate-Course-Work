package himalia.model;

import himalia.controller.AbstractMove;
import himalia.controller.Listener;

import java.util.ArrayList;
import java.util.Stack;


/**
<<<<<<< .mine
 * The class is used to creating board objects. It includes functions that board is responsible for
=======
 * The class is used to creating board objects. It helper functions. 
>>>>>>> .r269
 * @author Yan Wang
 *
 */

public class Board {
	
	/** Protected Region*/
	ProtectedRegion protectedRegion;
	
	/** Unrotected Region*/
	UnprotectedRegion unprotectedRegion;
	
	/** Undo moves*/
	Stack<AbstractMove> movesUndo;
	
	/**Redo moves*/
	Stack<AbstractMove> movesRedo;

	/**Listeners*/
	ArrayList<Listener> listeners = new ArrayList<Listener>();
	
	/**The word to be highlighted*/
	Word highlightedWord;
	
	
	/** Constructor of board
	 *@param ArrayList<Word> wrds             	Words exist in the board
	 *@param Position posProtect              	Position of protected region
	 *@param Position posUnprotect            	Position of unprotected region
	 *@param int heightProtect					The height of protected region
	 *@param int heightUnprotect				The height of protected region
	 *@param int widthProtect 					The width of protected region
	 *@param int widthUnprotect					The width of unprotected region
	 */
	public Board(ArrayList<Word> wrds, Position posProtect, Position posUnprotect, 
			int heightProtect, int heightUnprotect, int widthProtect, int widthUnprotect ){
		this.protectedRegion = new ProtectedRegion(widthProtect,heightProtect,posProtect);
		this.unprotectedRegion = new UnprotectedRegion(wrds, posUnprotect, heightUnprotect, widthUnprotect);
		this.movesUndo = new Stack<AbstractMove>();	
		this.movesRedo = new Stack<AbstractMove>();
		highlightedWord = null;
	}
	
	/**
	 * Constructor of board 
	 */
	Board(){}
	
	/**
	 * @return Protected region
	 */
	public ProtectedRegion getProtectedRegion(){
		return this.protectedRegion;
	}
	
	/**
	 * 
	 * @return Unprotected region
	 */
	public UnprotectedRegion getUnProtectedRegion(){
		return this.unprotectedRegion;
	}
	
	/**
	 * Set highlightedWord
	 * @param word
	 */
	public void setHighlightedWord(Word word){
		highlightedWord = word;
	}
	
	/**
	 * 
	 * @return highlightedWord
	 */
	public Word getHighlightedWord(){
		return highlightedWord;
	}

	/**
	 * Set the state of board according to m
	 * @param board memento m
	 */
	public void restore(BoardMemento m) {

		// set words in unprotected region
		for (Word w : m.storedUnprotected) {
			this.getUnProtectedRegion().getWords().add(new Word(w.getPosition().x, w.getPosition().y, w.getPosition().z, w.getValue(), w.getType()));
		}	
		
		//set words in the pending swap request back to unprotected region
		for (Word w : m.storedSwap) {
			this.getUnProtectedRegion().getWords().add(new Word(w.getPosition().x, w.getPosition().y, w.getPosition().z, w.getValue(), w.getType()));
		}
				
		//set words in protected region
		for (Word w : m.storedProtectedW) {
			this.getProtectedRegion().getWords().add(new Word(w.getPosition().x, w.getPosition().y, w.getPosition().z, w.getValue(), w.getType()));
		}
		
		//set poems in protected region
		for (Poem p : m.storedProtectedP) {
			Poem newp = new Poem();
			boolean flagPoem = true;
			for(Row r : new ArrayList<Row>(p.getRows().values())){
				boolean flagRow = true;
				Poem tmpp = new Poem();
				Row newr=null;
				int sizeHT = r.getWordsHT().size();
				for(int i = 0; i<sizeHT;i++){
					Word w = r.getWordsHT().get(i);
					Word neww = new Word(w.getPosition().x, w.getPosition().y, w.getPosition().z, w.getValue(), w.getType());
					if (flagRow){
						if(flagPoem){
							newr = new Row(newp,neww);
						}
						else{
						newr = new Row(tmpp,neww);
						}
						flagRow = false;
					}
					else{
						newr.addEdgeWord(neww);
					}
				}
				if (flagPoem){
					flagPoem = false;
				}
				else{
					newp.connectBottomRow(newr);
				}
			}
			
			this.getProtectedRegion().getPoems().add(newp);
		}
	}
	
	/**
	 * 
	 * @return state of board
	 */
	public BoardMemento getState() {
		return new BoardMemento(this.unprotectedRegion.getWords(),this.protectedRegion.getWords(),this.protectedRegion.getPoems(),this.unprotectedRegion.getSwapWords());
	}


	/**
	 * add move to undoMove
	 * @param undoMove
	 */
	public void addUndoMove(AbstractMove undoMove){
		this.movesUndo.push(undoMove);
	}
	
	/**
	 * add move to redoMove
	 * @param redoMove
	 */
	public void addRedoMove(AbstractMove redoMove){
		this.movesRedo.push(redoMove);
	}
	
	/**
	 * get move from undoMove
	 * @return
	 */
	public AbstractMove getMovesUndo(){
		if (this.movesUndo.isEmpty()){
			return null;
		}
		else{
		return this.movesUndo.pop();
		}
	}
	
	/**
	 * get move from undoMove
	 * @return
	 */
	public AbstractMove getMovesRedo(){
		if(this.movesRedo.isEmpty()){
			return null;
		}
		else{
		return this.movesRedo.pop();
		}
	}
	

	/**
	 * add word to protected region
	 * @param word
	 * @return
	 */
	public boolean addProtectedWord(Word word){
		boolean rlt = this.protectedRegion.addWord(word);
		notifyListeners();
		return rlt;
	}
	
	/**
	 * delete word from protected region
	 * @param word
	 * @return
	 */
	public Word deleteProtectedWord(Word word){
		Word wrd = this.protectedRegion.deleteWord(word);
		notifyListeners();
		return wrd;
	}
	
	/**
	 * add word to unprotected region
	 * @param word
	 * @throws CloneNotSupportedException
	 */
	public void addUnprotectedWord(Word word) throws CloneNotSupportedException{
		this.unprotectedRegion.addWord(word);	
		notifyListeners();
	}
	
	/**
	 * delete word from unprotected region
	 * @param word
	 * @return
	 */
	public Word deleteUnprotectedWord(Word word){
		Word wrd = this.unprotectedRegion.deleteWord(word);
		notifyListeners();
		return wrd;
	}

	/**
	 * release word from protected region to unprotected region
	 * @param word
	 * @param endi
	 * @throws CloneNotSupportedException
	 */
	public void releaseProtectedWord(Word word, Position endi) throws CloneNotSupportedException{
		this.getProtectedRegion().setPositionPWord(word, endi);
		addUnprotectedWord(deleteProtectedWord(word));
	}
	
	/**
	 * protect word from unprotected region to protected region
	 * @param word
	 * @param endi
	 */
	public void protectWord(Word word, Position endi){
		this.getUnProtectedRegion().setPositionUnWord(word, endi);
		addProtectedWord(deleteUnprotectedWord(word));	
	}


	/**
	 * tell if a position locates in protected region
	 * @param pos
	 * @return
	 */
	public boolean isProtected(Position pos){
		Position posProtect = this.getProtectedRegion().getPosition();
		int width = this.getProtectedRegion().getWidth();
		int height = this.getProtectedRegion().getHeight();
		return intersect(posProtect,width, height, pos);
	}
	
	/**
	 * tell if a position located in unprotected region
	 * @param pos
	 * @return
	 */
	public boolean isUnProtected(Position pos){
		Position posUnProtect = this.getUnProtectedRegion().getPosition();
		int width = this.getUnProtectedRegion().getWidth();
		int height = this.getUnProtectedRegion().getHeight();
		return intersect(posUnProtect,width, height, pos);
	}
	
	/**
	 * change word position within protected region
	 * @param wrd
	 * @param newPos
	 * @return
	 */
	public boolean moveInProtect(Word wrd, Position newPos){
		this.getProtectedRegion().setPositionPWord(wrd, newPos);
		notifyListeners();
		return true;
	}
	
	/**
	 * change word position within unprotected region
	 * @param wrd
	 * @param newPos
	 * @return
	 */
	public boolean moveInUnProtect(Word wrd, Position newPos){
		this.getUnProtectedRegion().setPositionUnWord(wrd, newPos);
		notifyListeners();

		this.getUnProtectedRegion().foreGroundWord(wrd);
		return true;
	}

	

	/**
	 * tell if pos2 intersect with a rectangle whose left top corner is pos1 
	 * @param pos1
	 * @param width
	 * @param height
	 * @param pos2
	 * @return
	 */
	public boolean intersect(Position pos1, int width,int height,Position pos2){
		if (pos2.x < pos1.x) {return false;}
		if (pos2.x >pos1.x+width) {return false;}
		if (pos2.y < pos1.y) {return false;}
		if (pos2.y >pos1.y+height) {return false;} 
				
		return true;
		
	}


	/**
	 * find the word located in (x,y) in protected region
	 * @param x
	 * @param y
	 * @return
	 */
	public Word findProtectedWord(int x, int y) {
		Position pos  = new Position(x,y,0);
		if (isProtected(pos))
		{
			return this.protectedRegion.findWord(pos);
		}
		else{
			return null;
		}

	}


	/**
	 * find the word located in (x,y) in unprotected region
	 * @param x
	 * @param y
	 * @return
	 */
	public Word findUnprotectedWord(int x, int y) {
		Position pos  = new Position(x,y,0);
		if (isUnProtected(pos))
		{
			return this.unprotectedRegion.findWord(pos);
		}
		else{
			return null;
		}
	}


	/**
	 * According to position (x,y), find the word locates in which poem and which row.
	 * @param x
	 * @param y
	 * @return ArrayList of objects: Poem, Row, Word.
	 */
	public ArrayList<Object> findPoemWord(int x, int y){
	    ArrayList<Object> res;
		for (Poem p : protectedRegion.getPoems()) {
	        res = p.getIntesectedWord(x, y);
	        if (res != null){
				if (res.get(1)!= null) {
		            res.add(3,p);
					return res;
		        }
	        }
	    }
		
		return null;
	}

	/**
	 * Clear the redo and undo stacks.
	 */
	public void clearRedoUndoStacks() {
		movesRedo.clear();
		movesUndo.clear();
	}
	
	/**
	 * clear the redo stack
	 */
	public void clearRedoStacks() {
		movesRedo.clear();
	}

	/**
	 * add listeners
	 * @param list
	 */
	public void addListener (Listener list) {
		listeners.add(list);
	}
	
	/** Remove a listener. */
	public void removeListener (Listener list) {
		listeners.remove(list);
	}

	/**
	 * notify listeners
	 */
	public void notifyListeners() {
		synchronized (listeners) {
			for (Listener list : listeners) {
				list.update();
			}
		}
	}

	public Word getOverlappedWords(Poem selectedPoem) {
		return protectedRegion.getOverlappedWord(selectedPoem);
	}
	
	
}
