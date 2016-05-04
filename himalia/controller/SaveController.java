package himalia.controller;

import himalia.model.Model;

/**
 * This controller is used to save the state of the board
 * @author Yan Wang
 *
 */
public class SaveController {
	/** View being controlled. */
	Model model;
	
	/** Constructor */
	public SaveController(Model model) {
		this.model = model; 
	}


	/**
	 * store state
	 */
	public void process() {
		Model.storeState(model.getBoard(), model.getStorage());
	}

}
