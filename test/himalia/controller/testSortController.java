package himalia.controller;

import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JTable;

import himalia.model.Board;
import himalia.model.Model;
import himalia.model.Position;
import himalia.model.Word;
import himalia.model.WordModel;
import himalia.model.WordType;
import himalia.view.RegionPanel;
import himalia.view.WordTable;
import junit.framework.TestCase;

public class testSortController extends TestCase {
	JTable jtable; 
	Board board;
	WordModel wordModel;
	RegionPanel panel;
	SortController sortCon;
	WordTable table;
	Model model;
	protected void setUp() throws Exception {
		ArrayList<Word> words=new ArrayList<Word>();
		words.add(new Word(120,120,0,"eee",WordType.adj));
		words.add(new Word(121,120,0,"bbb",WordType.adj));
		words.add(new Word(122,120,0,"ccc",WordType.adj));
		words.add(new Word(123,120,0,"ddd",WordType.adj));
		words.add(new Word(124,120,0,"aaa",WordType.adj));
		board=new Board(words,new Position(0,0,0),new Position(100,100,0),100,100,100,100);
		model=new Model(board);
		panel=new RegionPanel(model);
		table=new WordTable(model, panel);
		
		//jt=new JTable();
		sortCon=new SortController(table);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	public void testSort (){
		JTable jt=new JTable();
		int jtablex=jt.getX()+20;
		int jtabley=jt.getY()+20;
		
		sortCon.process((jt.getTableHeader()), new Point(jtablex,jtabley) );
	}

}
