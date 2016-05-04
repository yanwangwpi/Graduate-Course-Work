package himalia.model;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * This class creates the row objects which form apart of the poem.
 * @author saadjei
 *
 */
public class Row implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Hashtable<Integer, Word> rWords;
	static int nextRowID = 0;
	int id;
	Position rPos;
	int rWidth;
	int nextWordIndex;
	Poem rPoem;
	static int mHeight=15;

	/**
	 * This constructor creates a single word row and attaches it to a poem.
	 * @param poem
	 * @param w
	 */
	public Row(Poem poem,Word w){
	  //set the id somewhere
        rWords = new Hashtable<Integer, Word>();
        //get the current position of the row
        Position initRPos = w.getPosition(); 
        int initX = initRPos.x;
        int initY = initRPos.y;
        rPos = new Position(initX,initY,0);
        
        poem.setPosition(initRPos);
        
        rWidth = 0;
        id=nextRowID;
        
        nextRowID++;
  
        
        //Add first word
        rWords.put(0, w);
        initX += w.getWidth();
        rWidth += w.getWidth();

        
        nextWordIndex = 1;
      
        poem.addInitialRow(this);

        rPoem = poem;
        //rPoem.addInitialRow(this);
	}
	

	/**
	 * This is the default constructor for rows. 
	 * @param poem
	 * @param w1
	 * @param w2
	 * @throws Exception
	 */
	public Row(Poem poem, Word w1, Word w2) throws Exception{
		//set the id somewhere
		rWords = new Hashtable<Integer, Word>();
		//get the current position of the row
		Position initRPos = w1.getPosition(); 
		int initX = initRPos.x;
		int initY = initRPos.y;
		
		rPos = new Position(initX,initY,0);

		//setPosition(initX, initY);
		rWidth = 0;
		id=w1.getID();
		//w1.setPosition(initX, initY, 0);
		
		//Add first word
		rWords.put(0, w1);
		initX += w1.getWidth();
		rWidth += w1.getWidth();

		//Add second word
		w2.setPosition(initX, initY);	
		rWords.put(1, w2);
		rWidth += w2.getWidth();	
		
		nextWordIndex = 2;

		if (poem.getRows().size()>0){
			throw new Exception("This is not an empty poem. You can add rows to a poem by calling the poem connectRow functions.");
		}
		rPoem = poem;

		rPoem.addInitialRow(this);
		
	}
	

	/**
	 * Returns the list of words in the row as an ArrayList of words;
	 * @return
	 */
	public ArrayList<Word> getWords(){
		ArrayList<Word> words = new ArrayList<Word>( rWords.values());
		return words;
	}
	
	/**
	 * Return the words of the row as a hash table.
	 * @return
	 */
	public Hashtable<Integer, Word> getWordsHT(){
		return rWords;
	}
	
	/**
	 * Return the word at the index provided.
	 * @param wordIndex
	 * @return
	 */
	public Word getWord(int wordIndex){
		if (wordIndex < 0){
			return null;
		}
		
		if(wordIndex >= nextWordIndex){
			return null;
		}
		
		return rWords.get(wordIndex);
	}
	
	/**
	 * Get the current position of the row
	 * @return
	 */
	public Position getPosition(){
		return rPos;
	}
	
	/**
	 * set the current position of the row
	 * @param x --the x-coordinate of the position
	 * @param y --the y coordinate of the position
	 */
	public void setPosition(int x, int y){
		
		//determine the change in the position
		// x change and y change

		int xChange = x - rPos.x;
		int yChange = y - rPos.y;
		
		rPos.x = x;
		rPos.y = y;
		
		Position curPos;
		//reset the position of each of the words in the row
		
		for(Word w: rWords.values()){
			curPos = w.getPosition();
			
			curPos.x += xChange;
			curPos.y += yChange;
			
			w.setPosition(curPos);
		}
	}
	
	/**
	 * Set position of row without moving the words.
	 * @param x
	 * @param y
	 */
	private void setRowPos(int x,int y){
		rPos.x = x;
		rPos.y = y;
	}
	/**
	 * Get the width of the row.
	 * @return
	 */
	public int getWidth(){
		return rWidth;
	}
	
	
	/**
	 * Return the current poem to which the row belongs
	 * @return
	 */
	public Poem getPoem(){
		return rPoem;
	}
	
	/**
	 * Determines whether the x and y positions intersects the row.
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean intersects(int x, int y){
		ArrayList<Word> words = new ArrayList<Word>(rWords.values());
		for(Word w: words){
			if (w.intersect(x, y)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Get the word in the row that intersects the x and y position.
	 * @param x
	 * @param y
	 * @return
	 */
	public Word getIntersectedWord(int x, int y){
		ArrayList<Word> words = new ArrayList<Word>(rWords.values());
		for(Word w: words){
			if (w.intersect(x, y)){
				return w;
			}
		}
		return null;
	}
	
	/**
	 * This function connects a word to the edge of a row.
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean connectEdgeWord(Word wordToConnect, boolean fromUndo){
		
		int wordWidth = wordToConnect.getWidth();
		Position mPosition = wordToConnect.getPosition();
		boolean connectionSuccessfull = false;
		//test for all the four corner coordinates of the word
		
		if (this.intersectsWord(wordToConnect)){
			//set the position of that 
			//determine the side to attach
			//get the position of the row, and the width
			
			if ((rPos.x + rWidth/2) < 
					(mPosition.x+wordWidth/2)){//use the middle point of the word
				
				//connect to right
				wordToConnect.setPosition(rPos.x + rWidth,rPos.y);
				
				rWords.put(nextWordIndex, wordToConnect);
				
				connectionSuccessfull = true;
			}else{
				//connect to the left
			
				if(rPos.x - wordToConnect.getWidth() < 0 ){
					//increase the position of every word in the row by the width of the wordToConnect
					for(Word w: rWords.values()){
						w.setPosition(w.getPosition().x + wordWidth, rPos.y);
					}
					wordToConnect.setPosition(rPos.x,rPos.y);	
					connectionSuccessfull= true;
				}else{
					//reset the position of the row.
					if (!fromUndo)
						wordToConnect.setPosition(rPos.x - wordWidth, rPos.y);
					rPos.x = rPos.x - wordWidth;
					connectionSuccessfull=true;
				}
				// Shift the keys one place to the right
				for(int i=nextWordIndex-1; i>=0; i--){
					rWords.put(i+1, rWords.remove(i));
				}
				rWords.put(0, wordToConnect);
				
			}
			
			if (connectionSuccessfull){
				rWidth += wordWidth;
								
				nextWordIndex++;
			}
		}
		return connectionSuccessfull;
	}
	
	/**
	 * Disconnects an edge word at a given x and y position of this row.
	 * @param x
	 * @param y
	 * @return
	 */
	public Word disconnectEdgeWord(int x, int y){
		//determine if point intercepts with row
		Word wrdReturn = null;
		Word wrd = this.getIntersectedWord(x, y);
		if (wrd != null){
			//check if word is an edge word
			int wWidth = wrd.getWidth();
			Position wPos = wrd.getPosition();
			
			if (wPos.x == rPos.x && wPos.y == rPos.y){
				//this is a left edge word				
				
				//Remove the word from the Row
				wrdReturn = rWords.remove(0);
				//RepositionKeys in map
				for(int i =1; i<=nextWordIndex-1; i++){
					rWords.put(i-1, rWords.remove(i));
				}
				
				this.setRowPos(wPos.x + wWidth, y);
			
			}else if(rPos.x + rWidth ==wPos.x + wWidth){
						//this is a right edge word
						//remove rightmost edge word
						wrdReturn = rWords.remove(nextWordIndex-1);
					}else{
					    return null;
					}
			
			rWidth -= wWidth;
			nextWordIndex--;
			return wrdReturn;
		}
		return null;
	}
	
	/**
	 * A candidate for re-factoring.
	 * Disconnect an edge word at a given wordIndex of this row.
	 * 
	 * @param wordIndex
	 * @return
	 */
	public Word disconnectEdgeWord(int wordIndex){
		//check if word is an edge word
		if(wordIndex == 0 || wordIndex == nextWordIndex-1){
			Word w = rWords.remove(wordIndex);
			
			//re-index the words in the poem
			if (wordIndex == 0){
				for(int i =1; i<=nextWordIndex-1; i++){
					rWords.put(i-1, rWords.remove(i));
				}
				//reset the position of this row
			}
			
			nextWordIndex--;
			rWidth -= w.getWidth();
			
			if(wordIndex == 0){
				//reset position of the row;
				rPos.x = rWords.get(0).getPosition().x;
			}
			return w;	
		}
		return null;
	}
	
	/**
	 * Sets the poem for a this row.
	 * @param poem
	 * @return
	 */
	public boolean setPoem(Poem poem) {
		// TODO Auto-generated method stub
		if(rPoem != null){
			rPoem = poem;
			return true;
		}else
			return false;
	}

	/**
	 * Determines whether a given word intersects this row.
	 * @param word
	 * @return
	 */
	public boolean intersectsWord(Word word){
		for (Word w1:this.getWords()){
			if( word.intersectWord(w1) || w1.intersectWord(word)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This function detects whether a row intersects another row.
	 * @param row
	 * @return
	 */
	
	public boolean intersectsRow(Row row){
		
		for (Word w1:this.getWords()){
			
			for(Word w2 : row.getWords()){
				if( w2.intersectWord(w1) || w1.intersectWord(w2)){
					return true;
				};
			}
		}
		
		return false;
	}
	
	/**
	 * Determines whether a word is an edge word of this row.
	 * @param wrd
	 * @return
	 */
	public boolean isEdgeWord(Word wrd){
		//check if word is an edge word
		int wWidth = wrd.getWidth();
		Position wPos = wrd.getPosition();

		if (wPos.x == rPos.x && wPos.y == rPos.y){
			//this is the left edge word
			return true;
		}else if(rPos.x + rWidth ==wPos.x + wWidth){
			//this is the right edge word
			return true;
		}else{
		    return false;
		}
	}
	
	/**
	 * Adds an edge word to this row.
	 * @param neww
	 */
	public void addEdgeWord(Word neww) {
		
		rWidth += neww.getWidth();
		
		rWords.put(nextWordIndex, neww);
		
		nextWordIndex++;
	}

	/**
	 * This function returns the words of the row as a sentence.
	 */
	public String toString(){
		String result="";
		
		for(int key = 0; key < nextWordIndex; key++){
			result += rWords.get(key).getValue() + " ";
		}
		return result;
	}

	

}
