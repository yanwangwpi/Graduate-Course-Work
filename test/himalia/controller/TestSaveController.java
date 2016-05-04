package himalia.controller;

import himalia.model.Board;
import himalia.model.Model;
import himalia.model.Position;
import himalia.model.Word;
import himalia.model.WordType;

import java.util.ArrayList;

import junit.framework.TestCase;

public class TestSaveController extends TestCase {
	SaveController savCon;
	protected void setUp() throws Exception {
		ArrayList<Word> wrds=new ArrayList<Word>();
		  Word w1=new Word(10,10, 0,"Spring", WordType.noun);
		  Word w2=new Word(11,20, 0,"aaa", WordType.adj);
		  Word w3=new Word(12,30, 0,"aba", WordType.adj);
		  Word w4=new Word(13,20, 0,"abb", WordType.adj);
		  Word w5=new Word(14,20, 0,"abc", WordType.adj);
		  Word w6=new Word(60,20, 0,"acc", WordType.adj);
		  wrds.add(w1);
		  wrds.add(w2);
		  wrds.add(w3);
		  wrds.add(w4);
		  wrds.add(w5);
		  wrds.add(w6);
		  Position posProtect=new Position(0,0,0);
		  Position posUnprotect=new Position(0,100,0);
		  Board b=new Board(wrds,posProtect,posUnprotect,100,100,100,100);
		  Model m=new Model(b);
		savCon=new SaveController(m);
	}

	public void testprocess(){
		savCon.process();
	}
	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
