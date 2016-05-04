package himalia.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
/**
 * 
 * @author O
 *
 */

public class ProtectedRegion {
    /*
     * the list of poems in this region
     */
    ArrayList<Poem> poems;
    /*
     * list of words in this region
     */
    public ArrayList<Word> words;
    /*
     * the position of ProtectedRegion
     */
    Position position;
    /*
     * the width of this region
     */
    int width;
    /*
     * the height of this region
     */
    int height;

    
    /**
     * initialize protected region
     * @param width
     * @param height
     * @param position
     */
       
    public ProtectedRegion(int width, int height, Position position) {
        // this function initial the empty ProtectedRegion
        this.height = height;// final
        this.width = width;
        this.position = position;
        this.poems = new ArrayList<Poem>();
        this.words = new ArrayList<Word>();
    }
    /**
     * add poem to protected region
     * @param p
     */
    public void addPoem(Poem p){
    	this.poems.add(p);
    }
    /**
     * add an given word to protected region
     * @param word
     * @return
     */
    public boolean addWord(Word word) {// Support Board to add word
        words.add(word);

        return true;
    }
    /**
     * delete a given word to protected region, return the word
     * @param word
     * @return
     */
    public Word deleteWord(Word word) {// Support Board to delete Word

        int ii=0;
        for (Word w : words) {
            if (word.getID() == w.getID()) {
                return words.remove(ii);
            }
            ii++;
        }
        
        return null;
    }
    
    /**
     * delete the certain poem in protected region and return the poem.
     * @param poem
     * @return
     */
    public Poem deletePoem(Poem poem) {// Support Board to delete Poem
        int ii=0;
        for (Poem p : poems) {
            if (poem.getID() == p.getID()) {
                return poems.remove(ii);     
            }
            ii++;
        }
        return null;
    }

    /**
     * connect two words 
     * @param word1
     * @param word2
     * @return
     */
    public boolean connectWord(Word word1, Word word2) {
        
    	if (word1 != null && word2 != null) {
            Poem p = convertWordToPoem(word1);
            
            if (p.getRow(0).connectEdgeWord(deleteWord(word2), false)){
            	return true;
            }else{
            	//since the connection did not succeed, we destroy the newly created poem.
            	degradeOneWordPoem(p);
            	addWord(word2);
            }
        }
        return false;
    }
    
    /**
     * connect word to bottom or top of a poem
     * @param W
     * @param P
     * @return
     */
    public boolean connectWordToPoem(Word W, Poem P) {
        Poem temp = convertWordToPoem(W);
        if (temp != null) {
            connectPoemToPoem(temp, P);
            return true;
        }
        return false;
    }
    
    
    /**
     * disconnect an edge word from the poem.
     * @param poemID
     * @param pos
     * @return
     */
    public boolean disconnectEdgeWord(int poemID, Position pos) {

        Word w;
        for (Poem p : poems) {
            if(p.pointIntersects(pos.x, pos.y)){
               w = p.disconnectEdgeWordAtPoint(pos.x, pos.y);
               if (w !=null){
                   words.add(w);
                   return true;
               }
            }
            
        }
        return false;
    }

    /**
     * convert a single word to a poem
     * @param word
     * @return
     */
    public Poem convertWordToPoem(Word word) {
        Poem p = new Poem();
        Row r;

        r = new Row(p, deleteWord(word));

        poems.add(p);
        return p;

    }
    /**
     * connect two poems
     * @param poem1
     * @param poem2
     * @return
     */
    public boolean connectPoemToPoem(Poem poem1, Poem poem2) {
       
        if(poem1!=null&&poem2!=null){
        	if (poem1.connectPoem(deletePoem(poem2))){
        		return true;
        	}else{
        		addPoem(poem2);
        	}
        }
        return false;
    }
    
   
    /**
     * get the word ID by word position.
     * @param position
     * @return
     */
    public int getWordID(Position position) {// if the position has a word, it returns the wordID
        int ID = -1;
        // current words in the board
        for (Word w : words) {
            if (w.intersect(position.x, position.y)) {
                return w.getID();
            }
        }
        return ID;// if this happens, there's no word in this position, it will return a error ID
    }
    /**
     * get poem ID by Poem position
     * @param position
     * @return
     */
    public int getPoemID(Position position) {// if the position has a poem, it returns the poemID
        int ID = -1;
        // current poems in the board
        for (Poem p : poems) {
            for (Row r : p.getRows().values()) {
                for (Word w : r.getWords()) {
                    if (w.intersect(position.x, position.y)==true){
                    return p.getID();
                    }
                }
            }
        }
        return ID;
    }
    /**
     * get poem by ID
     * @param id
     * @return
     */
    public Poem getPoem(int id) {
        for (Poem p : poems) {
            if (p.getID() == id) {
                return p;
            }
        }
        return null;

    }
    /**
     * get word by ID
     * @param id
     * @return
     */
    public Word getWord(int id) {
        for (Word w : words) {
            if (w.getID() == id) {
                return w;
            }
        }
        return null;
    }
    /**
     * return ArrayList of poems in protected region
     * @return
     */
    public ArrayList<Poem> getPoems() {
        return this.poems;
    }
    /**
     * return ArrayList of words in protected region
     * @return
     */
    public ArrayList<Word> getWords() {
        return this.words;

    }
    /**
     * get the position of protected region
     * @return
     */

	public Position getPosition() {

		return this.position;
	}
	 /**
     * get the width of protected region
     * @return
     */
	public int getWidth() {

		return this.width;
	}
	 /**
     * get the height of protected region
     * @return
     */
	public int getHeight() {

		return this.height;
	}
	 /**
     * given a certain word, remove that from the protected region and return it
     * @param w
     * @return
     */

	public Word removeWord(Word w) {

	    int id=w.getID();
	    int ii=0;
        for (Word ww : words) {
            if (ww.getID()==id){
            return words.remove(ii);
            }
            ii++;
        }
        return null;
        
	}
	
	  /**
     * check whether a point is in the protected region
     * @param x
     * @param y
     * @return
     */

	public boolean withinProtectedArea(int x, int y) {

		int widthBoundary=position.x+width;
		int heightBoundary=position.y+height;
		
		return (x>=0&&x<widthBoundary&&y>=0&&y<heightBoundary);
	}
	
	 /**
     * 
     * Susan add
     * @param pos
     * @return
     */
	public boolean isOccupiedbyObject(Position pos) {
		return true;
	}
	 /**
     * detect whether a position has an object
     * @param pos
     * @return
     */
	public boolean detectOverlap(Position pos){
		//when move word w, w may overlap other word/poem
		//using brute force to detect whether it happens
		boolean result=false;
		ArrayList<Poem> pms=this.getPoems();
	
		//interact with poem
		for(Poem testPoem: pms)
			if(testPoem.intersects(pos)) {
				result=true;
			}
		//interact with word
		if(result==false){
			for(Word w: words){
				if(w.withinWordRect(pos.x, pos.y)){
					result=true;
				}
			}
		}
		return result;
	}
	 /**
     * detect whether a word is overlapped with other objects
     * @param word
     * @return
     */
	public boolean detectOverlap(Word word){
		//when move word w, w may overlap other word/poem
		//using brute force to detect whether it happens
	
		//check for poem intersections
	    for (Poem p : poems) {
	        if (p.intersects(word)) {
	            return true;
	        }
	    }
	    
		//check for word intersections
	    for (Word s : words) {
	        if(word.getID()!=s.getID()){
	        	if (word.intersectWord(s) || s.intersectWord(word)){
	        		return true;
	        	}
	        }
	    }
	    
		return false;
	}
	
	/**
	 * This function determines whether a row in a poem overlaps with any other objects in the protected region.
	 * @param selected
	 * @return
	 */
	public boolean detectOverlap(Row selected){
		
	    for (Poem p : poems) {
	    	if(p.getID() !=selected.getPoem().getID()){
	    		for (Word w: selected.getWords())
			        if (p.intersects(w)) {
			            return true;
			        }
	        }
	    	
	    }
	    
		//check for word overlaps;
	    
		for (Word s : words) {
        	if (selected.intersectsWord(s)){
        		return true;
        	}
	    }
	    
		return false;
	}
	/**
	 * This function determines whether a poem overlaps with any other objects in the protected region.
	 * @author saadjei
	 * @param selected
	 * @return
	 */
	public boolean detectOverlap(Poem selected){
		//Check for poems
		for (Poem p : this.getPoems()) {
			if (selected.getID() != p.getID() &&
					p.intersects(selected)) {
				return true;
			}
		}
		
		//check for word overlaps;
	    for (Word s : words) {
        	if (selected.intersects(s)){
        		return true;
        	}
	    }
	    
		return false;
	}
	
	
	/**
	 * This function determines whether a poem will overlap with any other objects in the protected region when connected to another.
	 * @author saadjei
	 * @param selected
	 * @param overlappedPoem
	 * @return
	 */
	public boolean detectPossibleOverlap(Poem selected, Poem overlappedPoem){
		Position currentReleasedPosition = new Position(selected.getPosition().x,selected.getPosition().y, 0);
		Position pos = null;
		boolean result = false;
		//Check for poems
		
		//Change the position of the selected poem depending on where the poem is to be attached
		switch (overlappedPoem.getConnectionDirection(selected)){
		case 0://top
			pos = new Position(currentReleasedPosition.x,overlappedPoem.getRow(0).getPosition().y - (selected.getRows().size() * 15),0);
			break;
		case 1: //bottom
			pos = new Position(currentReleasedPosition.x,overlappedPoem.getRow(overlappedPoem.getRows().size()-1).getPosition().y + 15,0);
			break;
		}
		if(pos == null){ return false;}

		selected.setPosition(pos);
		
		for (Poem p : this.getPoems()) {
			if (selected.getID() != p.getID() && overlappedPoem.getID() != p.getID() &&
					p.intersects(selected)) {
				result = true;
			}
		}
		
		//check for word overlaps;
	    for (Word s : words) {
        	if (selected.intersects(s)){
        		result= true;
        	}
	    }
	    //reset the position of the selected poem
	    selected.setPosition(new Position(currentReleasedPosition.x,currentReleasedPosition.y,0));
		return result;
	}
	
	/**
	 * check if a position has a word and return it.
	 * @param pos
	 * @return
	 */
	public Word findWord(Position pos){
		//when move word w, w may overlap other word/poem
		//using brute force to detect whether it happens

			for(Word w: words){
				if(w.intersect(pos.x, pos.y)){
					return w;
				}			
			}
			return null;

	}
	 /**
     * return the words that intersect with the other word.
     * @param selected
     * @return
     */
	public ArrayList<Word> getIntersectedWords(Word selected){
	    ArrayList<Word> wordList=new ArrayList<Word> ();
	    Position pos = selected.getPosition();
	    for (Word s : words) {
	        if(selected.getID()!=s.getID()){
	        	if (s.intersect(pos.x, pos.y)) {
	                wordList.add(s);
	            }else if(s.intersect(pos.x+selected.getWidth(), pos.y)){
		                wordList.add(s);
		        }else if(s.intersect(pos.x, pos.y+selected.getHeight())){
		                wordList.add(s);
		        }else if(s.intersect(pos.x+selected.getWidth(), pos.y+selected.getHeight())){
		                wordList.add(s);
		        }
	        }
	    }
	    return wordList;
	}
	   /**
     * return the poems that intersect with a certain word
     * @param word
     * @return
     */
	public ArrayList<Poem> getIntersectedPoems(Word word){
	    ArrayList<Poem> po=new ArrayList<Poem>();
	    for (Poem p : poems) {
	        if (p.intersects(word)) {
	            po.add(p);
	        }
	    }
	    return po;
	}

	/**
	 * This function returns the poem that intersects with a given point.
	 * @param x
	 * @param y
	 * @return
	 */
	public Poem getIntersectedPoem(int x, int y){
	    Position pos = new Position (x, y, 0);
		for (Poem p : poems) {
	        if (p.intersects(pos)) {
	            return p;
	        }
	    }
		return null;
	}
	 /**
     * connect word to left or right edge of the poem
     * @param p
     * @param w
     * @return
     */
	
	public boolean connectEdgeWord(Poem p, Word w){
		Word word = deleteWord(w);
		if(word==null) return false;
		if(p.connectEdgeWord(word)){
			return true;
		}
		words.add(word);
		return false;
		
		//return p.connectEdgeWord(w);
	}
	 /**
     * if the poem has only one word, change it to word
     * @param p
     */
	public Word degradeOneWordPoem(Poem p) {
		ArrayList<Row> r =new ArrayList<Row>(p.getRows().values());
		Word word = null;
		if (r.size()==1)
			if (r.get(0).getWords().size()==1){
				word = r.get(0).getWords().remove(0);
				this.addWord(word);
				this.deletePoem(p);
			}	
		return word;
	}
	
	/**
	 * Returns an array list of poems that intersect with the selected poem.
	 * @param selected
	 * @return
	 */
	public ArrayList<Poem> findOverlapedPoems(Poem selected){
		//Check for poems
		ArrayList <Poem> res = new ArrayList<Poem>();
		
		for (Poem p : this.getPoems()) {
			if (selected.getID() != p.getID() &&
					(p.intersects(selected) || selected.intersects(p))) {
				res.add(p);
			}
		}
		return res;
	}
	
	/**
	 * This method writes out the words of the poem into a file
	 * @param p
	 * @return
	 */
	
	public boolean publishPoem(Poem p){
		//open the file for appending
		try{
			Date currentDate = new Date();
			String poem = "\n" + currentDate.toString() 
					+ "\n*******************************************\n" 
					+ p.toString()
					+ "*******************************************\n";

			File file =new File("published_poems.txt");
 
    		//if file doesn't exists, then create it
    		if(!file.exists()){
    			file.createNewFile();
    		}
 
    		//true = append file
    		FileWriter fileWritter = new FileWriter(file.getName(),true);
	        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
	        bufferWritter.write(poem);
	        bufferWritter.close();
 
    	    return true;
    	}catch(IOException e){
    		e.printStackTrace();
    	}
		
		return false;
	}

	public void setPositionPWord(Word wrd, Position newPos) {
		wrd.setPosition(new Position(newPos.x,newPos.y,newPos.z));	
	}

	
	
	/**
	 * This function forms a one word poem
	 * @param overlappedWord
	 * @return
	 */
	public Poem formOneWordPoem(Word overlappedWord) {
		Poem overlappedPoem;
		overlappedPoem = new Poem();
		//Remove the word from the protected region.
		Row r = new Row(overlappedPoem, deleteWord(overlappedWord));
		addPoem(overlappedPoem);
		return overlappedPoem;
	}
	
	/**
	 * Get a single word that overlaps the selected poem.
	 * @param selectedPoem A poem that overlaps the word we are after.
	 * @return Word if there is an overlap not one word, Null if either there is no overlap
	 * 		   or there is more than one word that overlaps the poem.
	 */
	public Word getOverlappedWord(Poem selectedPoem) {
		int wordCount=0;
		Word foundWord=null;
		for(Word w: words){
			if (selectedPoem.intersects(w)){
				wordCount++;
				foundWord = w;
			}
		}
		
		if (wordCount == 1){ return foundWord;}
		return null;
	}
}
