package himalia.controller;

import himalia.model.Board;
/**
 * 
 * @author susanqin
 *
 */
//Command pattern:
abstract public class AbstractMove {
	/** Execute */
	public abstract boolean execute(Board board);
	
	/** Request undo. */
	public abstract boolean undo(Board board);


}
