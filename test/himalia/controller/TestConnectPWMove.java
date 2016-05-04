package himalia.controller;

import java.util.ArrayList;

import himalia.model.Board;
import himalia.model.Poem;
import himalia.model.Position;
import himalia.model.ProtectedRegion;
import himalia.model.Word;
import himalia.model.WordType;
import junit.framework.TestCase;

public class TestConnectPWMove extends TestCase {
    Board board = null;
    public void testConnectPWMove() {
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
        this.board.getProtectedRegion().addWord(word1);
        this.board.getProtectedRegion().addWord(word2);
        Poem p=board.getProtectedRegion().convertWordToPoem(word1);
        
        ConnectEdgeWordMove Move=new ConnectEdgeWordMove(word2.getPosition(),word2, p);
        
       Move.execute(board);//this one should be modified later
        
       // assertEquals(word1.getPosition().x,word2.getPosition().x-word1.getWidth());
       //assertEquals(word1.getPosition().y,word2.getPosition().y);
        
        Move.undo(board);//this one should be modified later
        

    }
    
    protected void tearDown(){
        board=null;
    }

}
