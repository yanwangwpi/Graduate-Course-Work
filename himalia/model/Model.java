package himalia.model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import himalia.BrokerManager;

/** This class is the main object that holds all the entity objects in the game. */
public class Model {
	
	protected Board b;
	protected BrokerManager bm;
	Word selectedWord;
	static String defaultStorage;
	
	/** This is the main constructor of the main model class. 
	 * @param b The current board.
	 * */
	public Model(Board b){
		this.b = b;
	}
	
	/** 
	 * Set the main broker manager for the model. 
	 * @param bmr This is the broker manager.
	 * */
	public void setBrokerManager(BrokerManager bmr){
		bm=bmr;
	}
	
	/** Return the board object. */
	public Board getBoard() {
		return b;
	}
	
	/** 
	 * Set the current selected word in the protected region. 
	 * @param s The selected word
	 * */
	public void setSelected(Word s) {
		selectedWord = s;
	}
	
	/** Return the selected word from the protected region. */
	public Word getSelected() {
		return selectedWord;
	}
	
	/** Set the momento object for the game. 
	 * @param storage The current momento object.
	 * */
	public void setStorage(String storage){
		defaultStorage = storage;
	}
	
	/** Return the current memento object for the game. */
	public String getStorage(){
		return defaultStorage;
	}
	
	/**
	 * Save the momento object, which contains the current state of the game.
	 * @param b  - the current board
	 * @param location - the path to the storage object.
	 */
	public static void storeState(Board b, String location) {
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(location));
			oos.writeObject(b.getState());
		} catch (Exception e) {
			
		}
		
		if (oos != null) {
			try { oos.close(); } catch (IOException ioe) { } 
		}
		
	}

}
