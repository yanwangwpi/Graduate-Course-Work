package himalia.controller;

import java.util.ArrayList;

import himalia.model.Board;
import himalia.model.Poem;
import himalia.model.Position;
import himalia.model.Row;
import himalia.model.Word;
import himalia.model.WordType;
import junit.framework.TestCase;

public class TestMovePoemMove extends TestCase {
     MovePoemMove move;
     Poem poem;
     Position oldPos;
     Position newPos;
     Board b;
	protected void setUp() throws Exception {
		ArrayList<Word> words=new ArrayList<Word>();
		b=new Board(words,new Position(0,0,0),new Position(100,100,0),100,100,100,100);
		poem=new Poem();
		Word w=new Word(0,0,0,"Good",WordType.adj);
		Row r=new Row(poem, w );
		poem.addInitialRow(r);
		b.getProtectedRegion().addPoem(poem);
		oldPos=new Position(10,10,0);
		newPos=new Position(20,20,0);
		move=new MovePoemMove(poem, oldPos, newPos);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
    public void testexecute(){
    	 move.execute(b);
    	 assertEquals(newPos.x,poem.getPosition().x);
    }
    public void testUndo(){
    	move.undo(b);
    	assertEquals(oldPos.x,poem.getPosition().x);
    }
}
