package himalia.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is used for creating memento object that storing the board state
 * @author Yan Wang
 *
 */

public class BoardMemento implements Serializable {
	/**Store words in unprotected region*/
	ArrayList<Word> storedUnprotected = new ArrayList<Word>();
	/**Store words in protected region*/
	ArrayList<Word> storedProtectedW = new ArrayList<Word>();
	/**Store poems in protected reigon */
	ArrayList<Poem> storedProtectedP = new ArrayList<Poem>();
	/**Store words that are ready for swap(, in case losing these words if the program shuts done during swap)*/
	ArrayList<Word> storedSwap= new ArrayList<Word>();
	

	/**
	 * Has special permissions to be able to inspect all attributes of Word and Poem objects
	 * and make copy as needed.
	 * @param wordsUnp
	 * @param wordsP
	 * @param poems
	 * @param swapWords
	 */
	public BoardMemento(ArrayList<Word> wordsUnp,ArrayList<Word> wordsP,ArrayList<Poem> poems,ArrayList<Word> swapWords) {
		
		//Store words in unprotected region
		for (Word w : wordsUnp) {
			storedUnprotected.add(new Word(w.getPosition().x, w.getPosition().y, w.getPosition().z, w.getValue(), w.getType()));
		}		
		
		//Store words in protected region
		for (Word w : wordsP) {
			storedProtectedW.add(new Word(w.getPosition().x, w.getPosition().y, w.getPosition().z, w.getValue(), w.getType()));
		}
		
		//Store poems in protected region
		for (Poem p : poems) {
			Poem newp = new Poem();
			boolean flagPoem = true;
			for(Row r : new ArrayList<Row>(p.getRows().values())){
				boolean flagRow = true;
				Row newr=null;
				Poem tmpp = new Poem();
				int sizeHT = r.getWordsHT().size();
				for(int i= 0;i<sizeHT;i++){
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
			storedProtectedP.add(newp);
		}
		
		//Store swap words
		for (Word w : swapWords) {
			storedSwap.add(new Word(w.getPosition().x, w.getPosition().y, w.getPosition().z, w.getValue(), w.getType()));
		}
	}

	/**
	 * Unique tag for memento objects on disk
	 * 
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 7708517838167328419L;


}