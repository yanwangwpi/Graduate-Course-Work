package himalia.model;

/**
 * store the wordType 
 * @author susanqin
 *
 */
public class WordFormat implements java.io.Serializable {
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1014674150513981445L;
	WordType type;
	  String value;
	  
	  /**set the type, and save the value as the string*/
	  public void setType(WordType t){
		  this.type=t;
		  this.value=t.toString();
	  }
	  /**get the word type*/
	  public WordType getType(){
		  return type;
	  }
}
