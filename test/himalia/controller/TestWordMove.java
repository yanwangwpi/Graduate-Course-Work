package himalia.controller;

import java.util.ArrayList;

import junit.framework.TestCase;
import himalia.model.Board;
import himalia.model.Position;
import himalia.model.Word;
import himalia.model.WordType;

public class TestWordMove extends TestCase{
	Board board = null;
	
	public void testAddProtectedWord(){

	Word word = new Word(102,203,0,"Hello", WordType.adj);
	ArrayList<Word> wrds = new ArrayList<Word>();
	wrds.add(word);
	Position posProtect = new Position(1,2,0);
	Position posUnprotect =new Position(100,200,0);
	int heightProtect = 50;
	int heightUnprotect=50; 
	int widthProtect=50; 
	int widthUnprotect=50;
	this.board = new Board(wrds, posProtect,posUnprotect, heightProtect,heightUnprotect,widthProtect, widthUnprotect);
	assert this.board!=null;
	
	Position pos1 = word.getPosition();
	Position pos2 = new Position(101,201,0);
	Position pos3 = new Position(3,4,0);
	Position pos4 = new Position(102,202,0);	
		
	MoveWordMove wordMove = new MoveWordMove(word,pos1, pos2);
	//System.out.println(word.getPosition().x);
	int oldX = word.getPosition().x;

	wordMove.execute(this.board);
	assertEquals(word.getPosition().x,101);
	wordMove.undo(this.board);
	assertEquals(word.getPosition().x,oldX);
	
	wordMove = new MoveWordMove(word,pos1, pos3);
	oldX =  word.getPosition().x;
	wordMove.execute(this.board);
	assertEquals(word.getPosition().x,3);
	wordMove.undo(this.board);
	assertEquals(word.getPosition().x,oldX);
	
//	wordMove = new WordMove(word,pos1, pos2);
//	wordMove.execute(this.board);
//	wordMove = new WordMove(word,word.getPosition(), pos3);
//	assertEquals(word.getPosition().x,101);
//	wordMove.execute(this.board);
//	assertEquals(word.getPosition().x,3);
//	wordMove.undo(this.board);
//	assertEquals(word.getPosition().x,101);
	
//	wordMove = new WordMove(word,pos2, pos4);
//	assertEquals(word.getPosition().x,101);
//	wordMove.execute(this.board);
//	assertEquals(word.getPosition().x,102);
//	wordMove.undo(this.board);
//	assertEquals(word.getPosition().x,101);
	}

/*
 * (non-Javadoc)
 * @see junit.framework.TestCase#tearDown()
 */
protected void tearDown(){
    this.board=null;
}
}
