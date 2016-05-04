package himalia.view;

import himalia.model.Board;
import himalia.model.Model;
import himalia.model.Position;
import himalia.model.Word;
import himalia.model.WordType;

import java.util.ArrayList;

import junit.framework.TestCase;

public class TestGUI extends TestCase {

	
	CyberPoetrySlamGUI gui;
	RequestGUI rgui;

	public void testApp(){
		Board board = null;
		Word word = new Word(100,105,0,"Hello", WordType.adj);
		ArrayList<Word> wrds = new ArrayList<Word>();
		Position posProtect = new Position(1,2,0);
		Position posUnprotect =new Position(100,100,0);
		int heightProtect = 50;
		int heightUnprotect=50; 
		int widthProtect=50; 
		int widthUnprotect=50;
		wrds.add(word);
		board = new Board(wrds, posProtect,posUnprotect, heightProtect,heightUnprotect,widthProtect, widthUnprotect);
		Model model = new Model(board);
		
		gui = new CyberPoetrySlamGUI(model);
		rgui=new RequestGUI(model);
		gui.setVisible(true);
		
		if(gui !=null){
			gui.dispose();
		}
		
		//System.out.println("Hello World!");
	}
	

}
