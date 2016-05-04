package himalia.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

/**
 * This class is for creating poem objects and contains all the functions performed on and by poems.
 * @author saadjei
 *
 */

public class Poem implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	// This variable contains all the rows in this poem.
	Hashtable<Integer, Row> pRows = new Hashtable<Integer, Row>();
	int nextRowIndex = 0;
	static int nextPoemID = 0;

	/** The indicator for the top row. */
	final int top = 0; 
	/** The indicator for the bottom row of this poem. */
	final int bottom = 1;
	
	/** Indicator for the index of the top row. */
	final int topRowIndex = 0;
	
	/** the height of each row in the poem. */
	final int rowHeight = 15;
	
	/** This is the current position of the poem. */
	Position pPos;
	
	int id;
	
	/**
	 * This is an empty constructor. Since a row cannot exist without a poem, this constructor creates the empty Poem object to which poems will later be added.
	 */
	public Poem(){
		pPos = new Position(0,0,0);
	}

	
	/**
	 * This function adds an initial row to the function once it is created. It fails if the poem contains at least on row.
	 * @param row - The Row object that is used to create initial row of the poem.
	 * @return - True if an initial row is created for the poem. False if the poem already has an initial row.
	 */
	public boolean addInitialRow(Row row){//put a row in to it, set the position
		if (nextRowIndex == 0){
			pRows.put(nextRowIndex, row);
			nextRowIndex++;
			
			pPos.x = row.getPosition().x;
			pPos.y = row.getPosition().y;

			id=nextPoemID;
			
			row.setPoem(this);
			nextPoemID++;
			return true;
		}
		
		return false;
	}

	/**
	 * Return a specific row in the poem, given the row index.
	 * @param index The index of the row.
	 * @return The row at the give index.
	 */
	public Row getRow(int index){
		if (index < 0){
			return null;
		}
		
		if(index >= nextRowIndex){
			return null;
		}
		
		return pRows.get(index);
	}
	
	/**
	 * Get the ID of the poem.
	 * @return the id of the current poem.
	 * @author - Ying
	 */
	public int getID(){
	    return this.id;
	}
	
	/**
	 * Sets the position of the poem, resetting the position of all it's constituent Rows and Words at the same time.
	 * @param newPos The new position of the poem.
	 * @return True if the position is set, false otherwise.
	 */
	public boolean setPosition(Position newPos){//set the position
		
		Position pos = new Position(newPos.x,  newPos.y, newPos.z);
		int deltaX = pos.x - pPos.x;
		int deltaY = pos.y - pPos.y;

		pPos.x = pos.x;
		pPos.y = pos.y;
				
		//reset the position of every word in the poem
		for(Row r :pRows.values()){
			 r.setPosition(r.getPosition().x + deltaX, r.getPosition().y + deltaY);
		}
		
		return true;
	}
	

	 /** 
	  * set the exact position of this poem without changing the row positions.
	 * @param x The x position to which the poem is set.
	 * @param y The y position to which the poem is set.
	 * @return True if the position is set.
	 */
	public boolean setPosition(int x, int y){
		pPos.x = x;
		pPos.y = y;
		return true;
	}

	/**
	 * Return the current position of the poem.
	 * @return A position object.
	 */
	public Position getPosition(){
		return pPos;
	}
	
	/**
	 * Return a hash table containing all the rows in a poem.
	 * @return The hash table of the rows in the poem.
	 */
	public Hashtable<Integer, Row> getRows(){
		return pRows;
	}

	/**
	 * Connects an edge word to the poem, given the word.
	 * @param w The word to be connected to the poem.
	 * @return True if the word is properly connected, and False otherwise.
	 */
	public boolean connectEdgeWord(Word w){
		
		ArrayList<Row> mRows = new ArrayList<Row>();
		
		for (Row r: pRows.values()){
			if (r.intersectsWord(w)){
				mRows.add(r);
			}
		}
		
		if (mRows.size()==1){
			return mRows.get(0).connectEdgeWord(w, false);
		}
		return false;
	}

	/**
	 * Disconnect an edge word from a poem given the index of the row in the poem and the index of the word in the row.
	 * @param rowIndex	The index of the row from which the word is to be disconnected.
	 * @param wordIndex	The the index of the word within the row which is meant to be disconnected.
	 * @return This function returns the disconnected word if the word index is valid. Otherwise it returns a null;
	 */
	public Word disconnectEdgeWord(int rowIndex, int wordIndex) {// disconnect a certain word  from the poem
		
		Row row = pRows.get(rowIndex);

		if(isValidDisconnection(rowIndex, wordIndex, row)){
			return row.disconnectEdgeWord(wordIndex);
		}
		return null;

	}


	/**
	 * Check whether a disconnection will cause a word to hang.
	 * @param rowIndex	The index of the row from which the word is to be disconnected.
	 * @param wordIndex	The the index of the word within the row which is meant to be disconnected.
	 * @param row	The row which is to be disconnected from the poem.
	 * @return True if disconnecting a word will not cause handing words, false otherwise.
	 */
	private boolean isValidDisconnection(int rowIndex, int wordIndex, Row row) {
		Word nextWord;

		Row adjacentRow = null;

		if (nextRowIndex == 1){//This is a single row poem
			return true;
		}
		
		//Get the adjacent row
		if(rowIndex == 0){
			//This is the first row
			adjacentRow = this.getRow(rowIndex+1);
		}else if(rowIndex == this.nextRowIndex-1){
			//This is the last row in the poem
			adjacentRow = this.getRow(rowIndex-1);
		}else{
			return true;
		}
		
		//get the side of the row from which your stuff was made
		if(wordIndex == 0){
			nextWord = row.getWord(1);
		}else if(wordIndex == row.getWords().size()-1){
			nextWord = row.getWord(wordIndex-1);
		}else{
			nextWord = null;
		}
		
		if(nextWord != null && adjacentRow != null){
			return adjacentRow.intersectsWord(nextWord);
		}
		
		return false;
	}

	/**
	 * Disconnect an edge word from the poem given a position on the poem..
	 * @param x The x position at which the word is to be disconnected.
	 * @param y The y position at which the word is to be disconnected.
	 * @return The disconnected word.
	 */
    public Word disconnectEdgeWordAtPoint(int x, int y) {// disconnect a certain word  from the poem
        for (Row r : pRows.values()){
          if (r.intersects(x, y)){
              //check if the point is on an edge word
              return r.disconnectEdgeWord(x, y);
          }
        }
        return null;                
    }

	
 	/**
 	 * Connects a row to the bottom of the poem and update the ownership of that row.
 	 * @param row The row object which id to be connected to the bottom of the poem.
 	 * @return True if the connection is successful, False otherwise.
 	 */
	public boolean connectBottomRow(Row row){
		pRows.put(nextRowIndex, row);

		row.setPoem(this);
		nextRowIndex++;
		
		return true;
	}
	
	/**
	 * This function connects two poems, one on top of the other.
	 * @param sourcePoem The poem to be connected to this target poem.
	 * @return True if the connection is successful, False otherwise.
	 */

	public boolean connectPoem(Poem sourcePoem){//connect two poem;
		//find if row intercepts with poem
		Boolean connected = false;		

		switch (getConnectionDirection(sourcePoem)){
		case top:
			//Check if connecting the poem p to this poem will cause a hanging row:
			if(connectionPreventsHanging(sourcePoem,top)){
				connected = connectTopPoem(sourcePoem, true);
			}
			break;
		case bottom:
			//Prevent hanging rows
			if(connectionPreventsHanging(sourcePoem,bottom)){
				connected = connectBottomPoem(sourcePoem);
			}
			break;
		default:
			connected = false;
		}
		
		return connected;
	}

	/**
	 * Connect a poem to the bottom of this poem
	 * @param sourcePoem The poem to be connected to this target poem. 
	 * @return True if the connection is successful, False otherwise.
	 */
	public boolean connectBottomPoem(Poem sourcePoem) {
		//attach the pRows to the bottom of this poem
		//there is no need to re-index
		Row r;
		Hashtable<Integer, Row> cPoemRows = sourcePoem.getRows();
		int initSize = cPoemRows.size();
		
		try{
			for(int k = 0; k <initSize; k++){
				r = cPoemRows.remove(k);
				r.setPosition(r.getPosition().x, pPos.y + getHeight());
				r.setPoem(this);
	
				pRows.put(nextRowIndex, r);
				nextRowIndex += 1;
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}


	/**
	 * Connect a poem to the top of this poem.
	 * @param sourcePoem The poem to be connected to this target poem
	 * @param reposition Indicated whether the rows in sourcePoem has to be repositioned.
	 * @return True if the connection is successful, False otherwise.
	 */
	public boolean connectTopPoem(Poem sourcePoem, boolean reposition) {
		//re-index this poem's rows
		Row r;
		
		try{
			Hashtable<Integer, Row> cPoemRows = sourcePoem.getRows();
			int currentRowSize = cPoemRows.size();
			
			Position pos = sourcePoem.getPosition();

			for(int j = nextRowIndex-1 ; j>=0 ; j-- ){
				r = pRows.remove(j);
				pRows.put(j + currentRowSize, r);
			}
			
			//move the rows of p into this poem
			for(int i = 0; i<currentRowSize; i++){
				r = cPoemRows.get(i); 
				r.setPoem(this);
				if (reposition){
					r.setPosition(r.getPosition().x, pPos.y - (currentRowSize-i) * rowHeight);
				}					
				pRows.put(i, r);
			}
			
			
			pos = pRows.get(0).getPosition();
			
			this.setPosition(pos.x, pos.y);
			
			nextRowIndex = pRows.size();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Detect the part of this poem that the source poem intersects.
	 * @param sourcePoem The source poem.
	 * @return -1= does not intersect; 0= top; 1= bottom
	 */
	public int getConnectionDirection(Poem sourcePoem){
		//get the first row and check if intersects		
		Position pPos = sourcePoem.getPosition();

		int thisHeight = this.getHeight();
		
		if(sourcePoem.getHeight()/2 + pPos.y >= thisHeight/2 + this.pPos.y){
			return bottom;
		}else if(sourcePoem.getHeight()/2 + pPos.y < thisHeight/2 + this.pPos.y){
			return top;
		}
		
		return -1;
	}
	
	/**
	 * This function returns true if the given point intersects with the poem.
	 * @param x The x position of the point.
	 * @param y The y position of the point.
	 * @return True if the point intersects the poem, False otherwise.
	 */
	public boolean pointIntersects(int x, int y){
		
		for(Row r :pRows.values()){
			if (r.intersects(x, y)){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * The function determines whether another poem intersects with this poem.
	 * 
	 * @param sourcePoem The source poem.
	 * @return True if any rows of the two poems intersect, and false otherwise.
	 */
	public boolean intersects(Poem sourcePoem){
		//This section allows for overlaps to be detected as long as any two rows of the poems overlap.
		for (Row r1 :sourcePoem.getRows().values()){	
			for(Row r2: this.getRows().values()){
				if (r1.intersectsRow(r2)){
					return true;
				}
			}
		}

		return false;
	}
	
	/**
	 * Return the row in the poem that is intersected by the point.
	 * @param pos The point at which the row is found.
	 * @return The row that intersects the position.
	 */
	public Row getRowFromPoint(Position pos){		
		for(Row r: pRows.values()){
			if (r.intersects(pos.x, pos.y))
				return r;
		}
		return null;
	}
	
	/**
	 * Return the row index of the row in the poem.
	 * @param pos Any position on the row whose index is being sort.
	 * @return The row index. -1 if the point does not intersect any of the rows in the poem.
	 */
	public int getRowIndexFromPoint(Position pos){		
		for(Integer rowIndex: pRows.keySet()){
			if (pRows.get(rowIndex).intersects(pos.x, pos.y))
				return rowIndex;
		}
		return -1;
	}
	
	
	/**
	 * Shifts a given row in the poem from one location to another within the poem.
	 * @param srcPos The source position.
	 * @param destPos The destination position.
	 * @return True if the row is shifted, and false otherwise.
	 */
	public boolean shiftRow(Position srcPos, Position destPos){//
		
		Row r = getRowFromPoint(srcPos);
		
		if(r != null){
			r.setPosition(destPos.x, destPos.y);
			
			if (pRows.get(0).equals(r))
				this.pPos=destPos;
			
			return true;
		}
		return false;
	}
	
	/**
	 * Disconnect a row from a given poem and return the resulting poems.
	 * @param pos The position of this poem from which a the disconnection takes place.
	 * @return An ArrayList of poems resulting from the disconnection.
	 */
	public ArrayList<Poem> disconnectPoem(Position pos){// 
		
		ArrayList<Poem> retValues = new ArrayList<Poem>();
		Poem firstPoem = new Poem();
		Row row = getRowFromPoint(pos);
		
		boolean rowFound= false;
		int kIndex = 0, foundIndex=-1;
		
		if(row!=null){
			//get the index of the row
			while (!rowFound && kIndex < nextRowIndex){
				if (pRows.get(kIndex) == row){
					rowFound = true;
					foundIndex = kIndex;
				}else
					kIndex++;
			}
			
			if (rowFound){	
				if(foundIndex == 0){
					//disconnect top row
					disconnectTopRow(firstPoem, foundIndex);
					retValues.add(firstPoem);
					//reset the position of this poem
					this.setPosition(this.getRow(0).getPosition().x, this.getRow(0).getPosition().y);
					nextRowIndex--;
					
				}else if(foundIndex == nextRowIndex-1){
					//disconnect bottom row
					row = pRows.remove(foundIndex);
					
					firstPoem.addInitialRow(row);
					retValues.add(firstPoem);				
					nextRowIndex--;
					
				}else if(foundIndex > 0 && foundIndex < nextRowIndex-1){
					//the middle row
					//create two additional poems.
					Poem secondPoem = disconnectMiddleRow(retValues, firstPoem,
							foundIndex);
					
					nextRowIndex -= foundIndex;
					retValues.add(secondPoem);
				}
				
			}
		}
		return retValues;
	}


	/**
	 * Disconnect a middle row from this poem. This is a helper function.
	 * 
	 * @param retValues An empty array list list of poems.
	 * @param firstPoem The new poem that will be created from the disconnection. 
	 * @param foundIndex The index at which the new poem will be disconnected form this poem.
	 * @return The second poem that is created from the disconnected poem.
	 */
	private Poem disconnectMiddleRow(ArrayList<Poem> retValues, Poem firstPoem,
			int foundIndex) {
		firstPoem.addInitialRow(pRows.remove(foundIndex));								
		
		retValues.add(firstPoem);
		
		//for the new poem
		Poem secondPoem = new Poem();
		
		secondPoem.addInitialRow(pRows.remove(foundIndex + 1));
		
		for (int i = foundIndex + 2; i <nextRowIndex; i++){
			secondPoem.connectBottomRow(pRows.remove(i));
		}

		return secondPoem;
	}


	/**
	 * Disconnect the top row of the poem.
	 * @param topRowPoem The top row poem to be disconnected.
	 * @param foundIndex The index of the row to be disconnected.
	 */
	private void disconnectTopRow(Poem topRowPoem, int foundIndex) {
		topRowPoem.addInitialRow(pRows.remove(foundIndex));
		//re-index the rows in the document
		for(int i =1; i<=nextRowIndex-1; i++){
			pRows.put(i-1, pRows.remove(i));
		}
	}

	/**
	 * Detects whether a position object intercepts a poem.
	 * @param p The position which is being tested.
	 * @return true if the position intersects a point.
	 */
	public boolean intersects(Position p) {
		return pointIntersects(p.x, p.y);
	}

	/**
	 * Detects whether the poem intersects a given word.
	 * @param word The word being tested.
	 * @return True implies that the poem intersects the word.
	 */
	public boolean intersects(Word word) {
		for (Row r: pRows.values()){
			if (r.intersectsWord(word)){
				return true;
			}			
		}
		return false;
	}
	
	/**
	 * Get the list of objects that intersect with this poem as a given position.
	 * @param x The x position of the point being tested.
	 * @param y The y position of the point being tested.
	 * @return ArrayList of objects: Poems, Rows, Words.
	 */
	public ArrayList<Object> getIntesectedWord(int x, int y){
		//return row index as well as word
		ArrayList<Object> res = new ArrayList<Object>();
		Word w; Row r;
		Set<Integer> keys = pRows.keySet(); 
		
		for (Integer iRow :keys){
			r = pRows.get(iRow);
			w = r.getIntersectedWord(x, y);
			if (w !=null && r.isEdgeWord(w)){
				res.add(0,iRow); //row Index
				
				//get the word index in the row
				Set<Integer> wKeys = r.getWordsHT().keySet();
				for (Integer iWord : wKeys){
					if (w == r.getWordsHT().get(iWord)){
						res.add(1,iWord); //word Index
						break;
					}
				}
				res.add(2,w); //word
				return res;
			}
		}
		
		return null;
	}

	/**
	 * This function returns the height of a given poem
	 * @return An integer representing the height of the poem.
	 */
	public int getHeight() {
		return this.pRows.size() * rowHeight;
	}

	/**
	 * Return the poem as a string.
	 * @return The text of the poem.
	 */
	public String toString(){
		String result="";
		
		for(int key = 0; key < nextRowIndex; key++){
			result += pRows.get(key).toString() + "\n";
		}
		
		return result;
	}
	
	/**
	 * Disconnect all the rows that begin at rowIndex of this poem and create a poem with the disconnected rows.
	 * @param rowIndex The row index.
	 * @param fromTop Boolean representing whether a disconnection if from the top or not.
	 * @return The poem that results from the disconnection.
	 */
	public Poem disconnectPoemFromRowIndex(int rowIndex, boolean fromTop){
		Poem poem =null;
		
		if (fromTop){
			if (rowIndex >=0){
				poem =  new Poem();
		    	int firstRow = 0, nextRow;
				
		    	poem.addInitialRow(this.getRows().remove(firstRow));
		    	
		    	//add the new rows to the poem
		    	for(int j = 1; j < rowIndex; j++){
		    		poem.connectBottomRow(pRows.remove(j));
		    	}
		    	
		    	//re-index the rows in this poem
		    	nextRow= firstRow;
		    	for(int k = rowIndex; k < nextRowIndex; k++){
		    		pRows.put(nextRow, pRows.remove(k));
		    		nextRow++;
		    	}
		    	
		    	this.setPosition(pRows.get(0).getPosition().x, pRows.get(0).getPosition().y);
		    	
		    	nextRowIndex -= rowIndex;
			}
		}else{
			if (rowIndex < nextRowIndex){
				poem =  new Poem();
				
		    	poem.addInitialRow(this.getRows().remove(rowIndex));
		    	
		    	for(int j = rowIndex+1; j <nextRowIndex; j++){
		    		poem.connectBottomRow(pRows.remove(j));
		    	}
		    	
		    	nextRowIndex = rowIndex;
			}
		}
		return poem;
	}
	
	/** 
	 * Checks whether this poem is a one word poem.
	 * @return True of this poem is a single word poem. and false otherwise.
	 */
	public boolean isSingleWordPoem(){
		return pRows.size()== 1 && pRows.get(0).getWords().size()==1;
	}
	
	/**
	 * 
	 * Return the number of rows in this poem.
	 * 
	 * @return The count of the rows.
	 */
	public int getRowCount(){
		return pRows.size();
	}
	
	 /** 
	  * Determine whether connecting a poem to another prevents hanging rows.
	  * @param connectingPoem The connecting poem.
	  * @param connectionPoint The side at which the connection is to occur. 0 => top; 1=>bottom.
	  * @return True if the connection prevents hanging poems/rows/words, false otherwise.
	  */
	private boolean connectionPreventsHanging(Poem connectingPoem, int connectionPoint) {
		boolean goodJoin = false;
		
		switch (connectionPoint){
		case top:
			//Prevent hanging poems at the top.
			goodJoin = goodTopJoin(connectingPoem);
			break;
		case bottom:
			//Prevent hanging poems at the bottom.
			goodJoin = goodBottomJoin(connectingPoem);		
			break;
		}

		return goodJoin;
	}


	/**
	 * Check whether connecting a poem to another will cause a hanging row at the bottom of the poem.
	 * @param connectingPoem
	 * @return
	 */
	private boolean goodBottomJoin(Poem connectingPoem) {
		boolean goodJoin;
		Row row1 = this.getRow(this.getRowCount()-1);
		Position pos1 = row1.getPosition();

		Row row2 = connectingPoem.getRow(0);
		Position pos2 = row2.getPosition();
		
		goodJoin =((pos1.x <= pos2.x && pos2.x <= pos1.x + row1.getWidth())
				|| (pos1.x <= pos2.x + row2.getWidth()
						&& pos2.x + row2.getWidth() <= pos1.x + row1.getWidth()));
		
		if (!goodJoin){
			row2.setPosition(pos2.x, pos1.y+15);
			goodJoin = row1.intersectsRow(row2);
			row2.setPosition(pos2.x, pos2.y);
		}
		return goodJoin;
	}


	/**
	 * Check whether connecting a poem to another will cause a hanging row at the top of the poem.
	 * @param connectingPoem
	 * @return
	 */
	private boolean goodTopJoin(Poem connectingPoem) {
		boolean goodJoin;
		Row row1 = this.getRow(0);
		Position pos1 = row1.getPosition();

		Row row2 = connectingPoem.getRow(connectingPoem.getRowCount()-1);
		if(row2 == null){return true;}
		
		Position pos2 = row2.getPosition();
		
		goodJoin = (pos1.x <= pos2.x && pos2.x <= pos1.x + row1.getWidth())
				   	|| (pos1.x <= pos2.x + row2.getWidth()
						&& pos2.x + row2.getWidth() <= pos1.x + row1.getWidth());
		if (!goodJoin){
			row2.setPosition(pos2.x, pos1.y-15);
			goodJoin = row1.intersectsRow(row2);
			row2.setPosition(pos2.x, pos2.y);
		}
		return goodJoin;
	}
		
		
}
