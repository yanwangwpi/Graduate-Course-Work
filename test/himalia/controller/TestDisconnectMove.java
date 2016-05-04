package himalia.controller;

import java.util.ArrayList;

import himalia.model.Board;
import himalia.model.Poem;
import himalia.model.Position;
import himalia.model.Row;
import himalia.model.Word;
import himalia.model.WordType;
import junit.framework.TestCase;

public class TestDisconnectMove extends TestCase {
	Poem poem;
	Word disconnectedWord;
	Position posNew;
	Position posOld;
	DisconnectWordMove dm;
	DisconnectWordMove invaliddm;
	Board board = null;
	
	protected void setUp() throws Exception {
		Word word1 = new Word(102,203,0,"He", WordType.adj);
        Word word2 = new Word(103,205,0,"llo", WordType.adj);
        posOld=word2.getPosition();
		poem=new Poem();
		Row r=new Row(poem, word1);
		r.connectEdgeWord(word2, false);
		posNew=new Position(100,100,0);
		  ArrayList<Word> wrds = new ArrayList<Word>();

	        Position posProtect = new Position(1,2,0);
	        Position posUnprotect =new Position(100,200,0);
	        int heightProtect = 50;
	        int heightUnprotect=50; 
	        int widthProtect=50; 
	        int widthUnprotect=50;
	        
	        this.board = new Board(wrds, posProtect,posUnprotect, heightProtect,heightUnprotect,widthProtect, widthUnprotect);
	        this.board.getProtectedRegion().addPoem(poem);

	        
            dm= new DisconnectWordMove(poem,0,0, posNew, posOld);
            Position invalidPN=new Position(50,50,0);
            Position invalidPO=new Position(40,50,0);
            invaliddm= new DisconnectWordMove(poem,0,0, invalidPN, invalidPO);
            
            
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testExecute(){
		dm.execute(board);
		assertEquals(dm.disconnectedWord.getPosition().x,posNew.x);
	    board.addUndoMove(dm);
		invaliddm.execute(board);
		board.addUndoMove(invaliddm);
	}

}
