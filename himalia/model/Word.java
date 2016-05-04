/**
 * 
 */
package himalia.model;

/**
 * This is the Class for creating words.
 * @author saadjei
 *
 */

public class Word implements java.io.Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5330755364784259349L;
	private int id;
	private String value;
	static int nextWordID;
	private int mWidth;
	private Position mPosition;
	static int mHeight=15;
	static int mCharacterWidth = 8;
	
    WordFormat wordFormat = new WordFormat();


    /**
     * This is the default constructor for creating word objects.
     * @param x
     * @param y
     * @param z
     * @param value
     * @param t
     */
	public Word(int x, int y, int z, String value, WordType t){
		mPosition = new Position(x,y,z);
		mWidth = value.length()*mCharacterWidth; //We need to determine the width of a character
		
		this.value = value;

		this.id = nextWordID;
		
		nextWordID++;
		this.wordFormat.setType(t);
	}
	
	/** Return the id for this word*/
	public int getID(){
		return this.id;
	}
	
	/** Return the type of word this is*/
	public WordType getType(){
		return this.wordFormat.type;
	}
	
	/** Returns the position of the word.*/
	public Position getPosition(){
		return mPosition;
	}
	
	/** Returns the width of the word. */
	public int getWidth(){
		return mWidth;
	}
	
	/** Makes a copy of this word.*/
    public Word clone() throws CloneNotSupportedException  {
        return (Word) super.clone();
    }
	
	/**
	 * Determine whether a point intersects with the word.
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean intersect(int x, int y){
		if (mPosition.x > x) {return false;}
		if (mPosition.x + mWidth < x) {return false;}
		if (mPosition.y > y) {return false;}
		if (mPosition.y + mHeight < y) {return false;} //height is constant: say 5
				
		return true;
		
	}
	
	/** Determine whether another word intersects with this word.*/
	public boolean intersectWord(Word wrd){
		int x = wrd.getPosition().x;
		int y = wrd.getPosition().y;
		int wid = wrd.getWidth();
		int height = wrd.getHeight();
		if (intersect(x,y) || intersect(x+wid,y) || intersect(x+wid,y+height) || intersect(x,y+height))
		{
			return true;
		} 
				
		return false;
		
	}
	
	/**
	 * Set the x, y position of this word. 
	 * @param x
	 * @param y
	 */
	public void setPosition(int x, int y) {
		mPosition.x = x;
		mPosition.y = y;
		//mPosition.z = z;
	}
	

	/** Change the z position of the word in order to bring it to the top,
	 * particularly in the unprotected region.*/
	
	public void setTop(){
		mPosition.z=0;
	}
	
	/** Change the z position of the word so it appears below all other words.*/
	public void setBottom(){
		mPosition.z=1;
	}

	/**Return the value of this word.*/
	public String getValue() {
		return value;
	}

	/**Set the string value of this word.*/
	public void setValue(String value){
		this.value = value;
		mWidth = value.length() * mCharacterWidth;
	}
	
	/** Set the position of this word using a position object. */
	public void setPosition(Position position) {

		mPosition = new Position(position.x,position.y, position.z);

	}

	/** Return the height of the word.*/
	public int getHeight() {
		return mHeight;
	}
	
	/** Check whether a point is within the space occupied by this word.*/ 
	public boolean withinWordRect(int x, int y){
		if(mPosition.x<=x&&x<=mPosition.x+mWidth)
			if(mPosition.y<=y&&y<=mPosition.y+mHeight)
				return true;
		return false;
		
	}
}
