
package himalia.controller;

import java.util.ArrayList;
import java.util.Random;

import himalia.model.Board;
import himalia.model.Poem;
import himalia.model.Position;
import himalia.model.Row;
import himalia.model.Word;

/**
 * This class is used to create the object of release poem move
 * @author Yan Wang
 *
 */
public class ReleasePoemMove extends  AbstractMove{

	/**The poem that is to be released*/
	Poem poem;
	/**The positions of words while they are in the poem*/
	ArrayList<ArrayList<Position>> pos;
	/**The words that belong to the poem before release*/
	ArrayList<ArrayList<Word>> words;
	
	/**
	 * Constructor
	 * @param poem
	 */
	public ReleasePoemMove(Poem poem){
		this.poem = poem;
		 pos = new ArrayList<ArrayList<Position>>();
		 words = new ArrayList<ArrayList<Word>>();
	}
	
	/**
	 * Execute the move
	 */
	@Override
	public boolean execute(Board board) {
	
		Position posUp = board.getUnProtectedRegion().getPosition();
		int width = board.getUnProtectedRegion().getWidth();
		int height = board.getUnProtectedRegion().getHeight();
		Random randomx = new Random();
		int x,y;
		Row row;
		Word w;
		ArrayList<ArrayList<Position>> pos = new ArrayList<ArrayList<Position>>();
		ArrayList<ArrayList<Word>> words = new ArrayList<ArrayList<Word>>();
	
		poem = board.getProtectedRegion().getIntersectedPoem(poem.getPosition().x, poem.getPosition().y);
		int numRow = poem.getRows().size();
		//delete each row
		for (int i = 0; i < numRow; i++){
			 row = poem.getRow(i);
				ArrayList<Word> wordtmp=new ArrayList<Word>();
				ArrayList<Position> postmp=new ArrayList<Position>();
			//delete each word in a row
				int sizeHT = row.getWordsHT().size();
			for (int j =0; j< sizeHT; j++){
				w = row.getWordsHT().remove(j);
				wordtmp.add(w);
				postmp.add(new Position(w.getPosition().x,w.getPosition().y,w.getPosition().z));
				
				x = (randomx.nextInt(width))%width+posUp.x;
				y = (randomx.nextInt(height))%height+posUp.y;
				w.setPosition(x,y);
				try {
					board.addUnprotectedWord(w);
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			}
			words.add(wordtmp);
			pos.add(postmp);
			poem.getRows().remove(row);
		}
		board.getProtectedRegion().deletePoem(poem);
		poem=null;
		this.pos = pos;
		this.words = words;
		return true;
	}

	/**
	 * Undo the move
	 */
	@Override
	public boolean undo(Board board) {
		Poem newp = new Poem();
		boolean flagPoem = true;
		int i = 0;
		//Form a poem
		for(ArrayList<Word> r : words){
			boolean flagRow = true;
			Row newr=null;
			Poem tmpp = new Poem();
			int j=0;
			
			//Form a row
			for (j=0;j<r.size();j++){
				Word w = r.get(j);
				w = board.deleteUnprotectedWord(w);
				w.setPosition(pos.get(i).get(j));
				if (flagRow){
					if(flagPoem){	
						newr = new Row(newp,w);
					}
					else{
					newr = new Row(tmpp,w);
					}
					flagRow = false;
				}
				else{
					newr.addEdgeWord(w);
				}
			}
			
			if (flagPoem){
				flagPoem = false;
			}
			else{
				newp.connectBottomRow(newr);
			}
			i++;
		}
		board.getProtectedRegion().addPoem(newp);
		this.poem = newp;
		return true;
	}
}



