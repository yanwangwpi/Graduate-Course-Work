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

public class TestProtectWordController extends TestCase {
	ProtectWordController protectWordController;
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
		protectWordController = new ProtectWordController(model, panel);
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
		
		//If release at unprotected region, Nope! Go back.
		assertEquals(protectWordController.release(110,300),false);
		assertTrue(protectWordController.select(pos.x+1, pos.y+2));
		assertEquals(protectWordController.selected.getPosition().x, pos.x);	
		assertEquals(protectWordController.selected.getPosition().y, pos.y);
		
		assertTrue(protectWordController.release(10,20));
		//System.out.println(protectWordController.selected.getPosition().x);
		assertTrue(board.getMovesUndo()!=null);
		//protectWordController.release(10,20);
		//System.out.println(protectWordController.selected.getPosition().x);
		//assertTrue(protectWordController.select(pos.x+1, pos.y+2));

		assertEquals(protectWordController.selected.getPosition().x, 10-protectWordController.deltaX);	
		assertEquals(protectWordController.selected.getPosition().y, 20-protectWordController.deltaY);
		assertEquals(board.getUnProtectedRegion().getWords().size(),0);
		assertEquals(board.getProtectedRegion().getWords().size(),1);
		
		assertTrue(protectWordController.drag(50,50));
		//System.out.println(protectWordController.selected.getPosition().y);
		assertEquals(protectWordController.selected.getPosition().x, 50-protectWordController.deltaX);	
		assertEquals(protectWordController.selected.getPosition().y, 50-protectWordController.deltaY);	

		
	}
	public void testregister(){
		wrds.add(word);
		board = new Board(wrds, posProtect,posUnprotect, heightProtect,heightUnprotect,widthProtect, widthUnprotect);
		Model model = new Model(board);
		RegionPanel panel = new RegionPanel(model);
		protectWordController = new ProtectWordController(model, panel);
		protectWordController.register();
	}
	public void testmousePressed(){
			
		wrds.add(word);
		board = new Board(wrds, posProtect,posUnprotect, heightProtect,heightUnprotect,widthProtect, widthUnprotect);
		Model model = new Model(board);
		RegionPanel panel = new RegionPanel(model);
		protectWordController = new ProtectWordController(model, panel);
		protectWordController.register();
		MouseEvent e=new MouseEvent(panel, 0, 0, 0, 20,15, 0, false);
		protectWordController.mousePressed(e);
	}
	public void testmouseDragged(){
		
		wrds.add(word);
		board = new Board(wrds, posProtect,posUnprotect, heightProtect,heightUnprotect,widthProtect, widthUnprotect);
		Model model = new Model(board);
		RegionPanel panel = new RegionPanel(model);
		protectWordController = new ProtectWordController(model, panel);
		protectWordController.register();
		MouseEvent e=new MouseEvent(panel, 0, 0, 0, 20,15, 0, false);
		protectWordController.mouseDragged(e);
	}
public void testmouseReleased(){
		
		wrds.add(word);
		board = new Board(wrds, posProtect,posUnprotect, heightProtect,heightUnprotect,widthProtect, widthUnprotect);
		Model model = new Model(board);
		RegionPanel panel = new RegionPanel(model);
		protectWordController = new ProtectWordController(model, panel);
		protectWordController.register();
		MouseEvent e=new MouseEvent(panel, 0, 0, 0, 20,15, 0, false);
		protectWordController.mouseReleased(e);
	}
	
	protected void tearDown(){
	    this.board=null;
	}

}
