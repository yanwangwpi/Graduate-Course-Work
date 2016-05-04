
package himalia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;
/**
 * This class is for creating unprotected objects and contains all the functions performed on and by unprotected Region.
 * @author susanqin
 *
 */
public class UnprotectedRegion {
	/**words on Unprotected region*/
	ArrayList<Word> words;
	/**sorted words list*/
	ArrayList<Word> sortedWords;
	/**whether to sort*/
	boolean sortOrder = false;
	/**sortby which strategy*/
	Sortby sortby = null;
	
	/**the location of Unprotected Region*/
	Position position;
	/**width of the region*/
	int width;
	/**height of the region*/
	int height;
	/** used for temporarily store the swapping words*/
	public ArrayList<Word> SwapWords;
	/**
	 * Constructor of the Unprotected Region
	 * @param w:Words in unprotected region
	 * @param p:position of the region
	 * @param wi:width
	 * @param h:height
	 */
	 @SuppressWarnings("unchecked")
	public UnprotectedRegion(ArrayList<Word> w, Position p, int h, int wi){
		//words=new ArrayList<Word>();
		words=(ArrayList<Word>) w.clone();
		//sortedWords=new ArrayList<Word>();
		sortedWords=(ArrayList<Word>) w.clone();
		//sortedWords=(ArrayList<Word>) words.clone();
		//calculate the area
		this.position=p;
		this.width=wi;
		this.height=h;
		scrambleWords();
		SwapWords=new ArrayList<Word>();
	}

	 /**
	 * This class is used for sort words by their Lexicographic order
	 * @author susanqin, Yan_Wang
	 *
	 */
	 class InitialComparatorAscending implements Comparator<Word>{
		public int compare(Word o1, Word o2){
			String s1=o1.getValue().toLowerCase();
			String s2=o2.getValue().toLowerCase();
			return s1.compareTo(s2);
		}
	 }
	 
	/**
	 * This class is used for sort words by their type ascending order, based on the ordinal of the type
	 * @author susanqin,Yan_Wang
	 *
	 */
	class TypeComparatorAscending implements Comparator<Word>{
		public int compare(Word o1, Word o2){		
			return o1.getType().ordinal()-o2.getType().ordinal();
		}
	}
	
	/**
	 * This class is used for sort words by their lexicographic descending order, based on the ordinal of the type
	 * @author susanqin,Yan_Wang
	 *
	 */
	 class InitialComparatorDescending implements Comparator<Word>{
		public int compare(Word o1, Word o2){
			String s1=o1.getValue().toLowerCase();
			String s2=o2.getValue().toLowerCase();
			return s2.compareTo(s1);
		}
	 }
	 
	 /**
	  * This class is used for sort words by their type descending order, based on the ordinal of the type
	  * @author susanqin,Yan_Wang
	  *
	  */
	 	class TypeComparatorDescending implements Comparator<Word>{
			public int compare(Word o1, Word o2){		
				return o2.getType().ordinal()-o1.getType().ordinal();
			}
		}
	
	/**
	 * explore words in some order
	 * @param sortby: sortby is a way to sort the unprotected words.
	 * @param sortOrder: sort or not
	 * @param sortedWords: words list to be sorted
	 */
	public void exploreWords(Sortby sortby, boolean sortOrder, ArrayList<Word> sortedWords){
		//sort by initial letter
		//don't change the order of the words in 'words'
		//return a list of sorted words.only need info about word and its type
		if(sortby!=null){
		if (sortOrder){
		if (sortby.equals(Sortby.initial)){		
			Collections.sort(sortedWords,new InitialComparatorAscending());
			return;
		}
		//sort by word type
		if(sortby.equals(Sortby.type)){
			Collections.sort(sortedWords,new TypeComparatorAscending());
			return;
		}
		return;
		}
		else
		{
			if (sortby.equals(Sortby.initial)){		
				Collections.sort(sortedWords,new InitialComparatorDescending());
				return;
			}
			//sort by word type
			if(sortby.equals(Sortby.type)){
				Collections.sort(sortedWords,new TypeComparatorDescending());
				return;
			}
			return;	
		}
		}
	}
	
	/**
	 * the word will be inserted in the rear of Arraylist
	 * @param w
	 * @return
	 * @throws CloneNotSupportedException
	 */
	public boolean addWord(Word w) throws CloneNotSupportedException{
	
		words.add(w);
		Word wordCopy = w.clone();
		sortedWords.add(wordCopy);
		exploreWords(sortby, sortOrder, sortedWords);
		return true;
	}
	/**
	 * the word w will be deleted 
	 * @param w: the word is removed 
	 * @return the deleted word
	 */
	public Word deleteWord(Word w){
		
		for(Word word: sortedWords)
		{
			if (w.getID()==word.getID() ){
				sortedWords.remove(word);
				break;
				}
		}	
		
		exploreWords(sortby, sortOrder, sortedWords);
		
		int i=0;
		for(Word word: words){
			if(word.getID()==w.getID()){
				return words.remove(i);
			}
		 i++;
		}
		return null;
	}
	
	
	/**
	 * select a sort order strategy	
	 * @param sortOrder
	 */
	public void setSortOrder(boolean sortOrder) {
		this.sortOrder = sortOrder;
		
		}
	/**
	 * 	select a sort strategy
	 * @param sortby
	 */
	public void setSortby(Sortby sortby) {
		this.sortby = sortby;	
	}
	/**get the sort order*/
	public boolean getSortOrder() {
		return this.sortOrder;
		
	}
	/**get the sorting strategy*/
	public Sortby getSortby() {
		return this.sortby;	
	}
	/**get position of Unprotected Region*/
	public Position getPosition(){
		return this.position; 
	}
	/**get width of the unprotected Region*/
	public int getWidth(){
		return this.width;
	}
	/**get height of the Unprotected Region*/
	public int getHeight(){
		return this.height;
	}
	/**get words of the Unprotected Region*/
	public ArrayList<Word> getWords(){
		return this.words;
	}
	/**get sorted words*/
	public ArrayList<Word> getSortedWords(){
		return this.sortedWords;
	}

	/**check if this point(x,y) in unprotected area*/
	public boolean withinBoard(int x, int y) {
		// TODO Auto-generated method stub
		int widthBoundary=position.x+width;
		int heightBoundary=position.y+height;
		
		return (x>=0&&x<widthBoundary&&y>=0&&y<heightBoundary);
	}
	/**put the word to the rail of the words list*/
	public boolean foreGroundWord(Word w) {
		//when w is inserted in an area which is occupied by other words
		//should set these old words in the background
		//hide after w;
		//if w is in the rear, return true;
		if(words.get(words.size()-1).getID()==w.getID())
			return true;
		//else do the delete and insert.
		int index=0;
		int i;
		for(i=0;i<words.size();i++)
		if(w.getID()==words.get(i).getID())
			index=i;
			Word temp=words.get(index);
			words.remove(index);
			//put the word in the rear
			words.add(temp);
		return true;
	}
	
	
	/** After getting the words, should scramble the words*/
	public boolean scrambleWords(){
		Random randomx = new Random();
		ArrayList<Integer> xCoordinate=new ArrayList<Integer>();
		ArrayList<Integer>  yCoordinate=new ArrayList<Integer>();
		for(int i=0;i<words.size();i++){
			xCoordinate.add((randomx.nextInt(width-40))%width+position.x);
			yCoordinate.add((randomx.nextInt(height-15))%height+position.y);
		}
		boolean fourEdgeSafe=false;
		
		
		
		for(int j=0;j<words.size();j++){
			if(withinBoard(xCoordinate.get(j),yCoordinate.get(j)))
				if(withinBoard(xCoordinate.get(j)+words.get(j).getWidth(),yCoordinate.get(j))) {
					words.get(j);
					if(withinBoard(xCoordinate.get(j),yCoordinate.get(j)+Word.mHeight)) {
						words.get(j);
						if(withinBoard(xCoordinate.get(j)+words.get(j).getWidth(),yCoordinate.get(j)+Word.mHeight))
							fourEdgeSafe=true;
					}
				}
			if(fourEdgeSafe==true)
			words.get(j).setPosition(xCoordinate.get(j), yCoordinate.get(j));
			else
			words.get(j).setPosition(position.x, position.y);
			
		}
		return true;
	}
	
	/**get the word in position pos*/
	public Word findWord(Position pos){
		//when move word w, w may overlap other word/poem
		//using brute force to detect whether it happens
         
		for(int i= words.size()-1;i>=0;i--){
			if(words.get(i).withinWordRect(pos.x, pos.y)){				
				return words.get(i);
			}			
		}
		return null;

	}

	/**Return the temporary array of the swapped words, used for store these words in broker manager. */
	public ArrayList<Word> getSwapWords(){
		return SwapWords;
	}
	/**set the words to the sorted words*/
	public void setSortedWords(ArrayList<Word> words){
		sortedWords = words;
	}
	/**set the wrd's position to position*/
	public void setPositionUnWord(Word wrd, Position newPos) {
		for(Word word: sortedWords)
		{
			if (wrd.getID()==word.getID()){
				word.setPosition(new Position(newPos.x,newPos.y,newPos.z));
				break;
				}
		}	
		wrd.setPosition(new Position(newPos.x,newPos.y,newPos.z));
			
	}
	/**when issued swap, the words I offer will
	 * be put to Swapwords words list*/
	public void sendBackSwap() {
		int size=SwapWords.size();
		for(int i=0;i<size;i++){
			try {
				this.addWord(SwapWords.remove(i));
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
	}
	/**clear the swap words stored in Swapwords*/
	public void clearSwapWords() {
		SwapWords.clear();
	}

}

