package himalia.controller;

import java.util.ArrayList;

import himalia.model.Board;
import himalia.model.Word;
import himalia.view.RegionPanel;

/**
 * This controller is responsible for highlight the word that is choosen in the table
 * @author Yan Wang
 *
 */
public class HighlightController {
	
	/**panel that the view should be drawn on*/
	RegionPanel panel;
	
	/**
	 * Constructor of the controller
	 * @param panel
	 */
	public HighlightController(RegionPanel panel){
		this.panel = panel;
	}
	
	public void process(Board board, int wordIndex){
		Word highlighted = getHighlighted(board, wordIndex);
        board.setHighlightedWord(highlighted);
        panel.redraw();
        panel.repaint();
        board.setHighlightedWord(null);
	}
	
	/**
	 * according to the wordIndex in sortedWords in unprotected region, get the word in unprotected region
	 * @param board
	 * @param wordIndex
	 * @return
	 */
	Word getHighlighted(Board board, int wordIndex){
		Word wordCopy = board.getUnProtectedRegion().getSortedWords().get(wordIndex);
		ArrayList <Word> wordsUn=board.getUnProtectedRegion().getWords(); 
		Word highlighted = null;
		for(Word w: wordsUn)
		{
			if (w.getID()==wordCopy.getID()){
				highlighted = w;
				break;
				}
		}
		return highlighted;
	}
    
}
