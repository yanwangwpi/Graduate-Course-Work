package himalia.controller;

import himalia.model.Board;
import himalia.model.Model;
import himalia.model.Poem;
import himalia.model.Position;
import himalia.model.Word;
import himalia.model.WordType;
import himalia.view.RegionPanel;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

import junit.framework.TestCase;

public class TestReleaseWordController extends TestCase {
	
	Board board = null;

	Word word = new Word(100,105,0,"Hello", WordType.adj);

	ArrayList<Word> wrds = new ArrayList<Word>();
	Position posProtect = new Position(0,0,0);
	Position posUnprotect =new Position(0,250,0);
	int heightProtect = 250;
	int heightUnprotect=250; 
	int widthProtect=500; 
	int widthUnprotect=500;
	

	public void testSelect(){
		wrds.add(word);
		board = new Board(wrds, posProtect,posUnprotect, heightProtect,heightUnprotect,widthProtect, widthUnprotect);
		Model model = new Model(board);
		RegionPanel panel = new RegionPanel(model);
		ProtectWordController protectWordController = new ProtectWordController(model, panel);

		
		//Initially, we don't have selected word
		assertEquals(protectWordController.selected, null);
		
		//We have 1 word in unprotected region
		assertEquals(board.getUnProtectedRegion().getWords().size(),1);
		
		//Test select
		Position pos=  board.getUnProtectedRegion().getWords().get(0).getPosition();
		protectWordController.select(pos.x+1, pos.y+2);
		//assertEquals(protectWordController.selected, null);
		assertEquals(protectWordController.deltaX, 1);
		//System.out.println(protectWordController.deltaY);
		assertEquals(protectWordController.deltaY, 2);		
		assertTrue(protectWordController.release(10,20));

	
		ReleaseWordController releaseWordController = new ReleaseWordController(model, panel);
		//protectWordController.select(99, 99);
		assertEquals(releaseWordController.selected, null);
				
		assertEquals(board.getUnProtectedRegion().getWords().size(),0);

		//board.addUnprotectedWord(word);
		assertTrue(releaseWordController.select(12, 20));

		assertFalse(releaseWordController.release(5,15));

		
		assertTrue(releaseWordController.release(110,300));
		
		//System.out.println(protectWordController.selected.getPosition().x);
		assertTrue(board.getMovesUndo()!=null);
		assertTrue(board.getMovesUndo()!=null);
		assertTrue(board.getMovesUndo()==null);
		//protectWordController.release(10,20);
		//System.out.println(protectWordController.selected.getPosition().x);
		//assertEquals(releaseWordController.selected.getPosition().x, 9);	
		//assertEquals(releaseWordController.selected.getPosition().y, 20);
		
		assertEquals(board.getProtectedRegion().getWords().size(),0);
		assertEquals(board.getUnProtectedRegion().getWords().size(),1);
		
		assertTrue(releaseWordController.drag(110,150));
		//System.out.println(protectWordController.selected.getPosition().y);
		assertEquals(releaseWordController.selected.getPosition().x, 107);	
		assertEquals(releaseWordController.selected.getPosition().y, 148);	
		
	}
	

	public void tearDown(){
		board = null;
	}
}
