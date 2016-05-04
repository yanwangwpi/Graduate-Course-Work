package himalia.model;

/**
 * For creating position objects.
 * @author saadjei
 *
 */
public class Position implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7810809014994106995L;
	public int x,y,z;
		
	public Position(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;		
	}
}
