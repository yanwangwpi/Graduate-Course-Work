package himalia.controller;

import himalia.model.Board;
import himalia.model.Poem;
import himalia.model.Position;
import himalia.model.Word;
import himalia.model.WordType;

import java.util.ArrayList;

import junit.framework.TestCase;

public class TestReleasePoemMove extends TestCase {
	
    Board board = null;
    public void testReleasePoemMove() {
        Word word1 = new Word(102,203,0,"He", WordType.adj);
        Word word2 = new Word(105,205,0,"llo", WordType.adj);

        ArrayList<Word> wrds = new ArrayList<Word>();
        wrds.add(word1);
        wrds.add(word2);
        Position posProtect = new Position(1,2,0);
        Position posUnprotect =new Position(100,200,0);
        int heightProtect = 50;
        int heightUnprotect=50; 
        int widthProtect=50; 
        int widthUnprotect=50;
        
        this.board = new Board(wrds, posProtect,posUnprotect, heightProtect,heightUnprotect,widthProtect, widthUnprotect);
        assert board!=null;
        this.board.protectWord(word1, new Position(10,10,0));
        //this.board.getProtectedRegion().addWord(word2);
        Poem p=board.getProtectedRegion().convertWordToPoem(word1);
        
        assertTrue(this.board.isProtected(word1.getPosition()));
        
        ReleasePoemMove move = new ReleasePoemMove(p);
        move.execute(this.board);
        
        assertTrue(this.board.isUnProtected(word1.getPosition()));
        //assertEquals(word1.getPosition().x,100);
    }
    
    protected void tearDown(){
        board=null;
    }
}
