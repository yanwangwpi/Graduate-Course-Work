package himalia.model;

import java.util.ArrayList;

import junit.framework.TestCase;

public class TestRow extends TestCase{
	public void testRowCreation() throws Exception{
		//create an array list of words
		Poem p = new Poem();
		Word w = new Word(15, 20, 0, " new word",WordType.adj);
		
		Row myRow = new Row(p, new Word(10, 20, 0, "Seth",WordType.adj), new Word(10, 20, 0, "same ",WordType.adj));
		
		assertEquals(myRow.intersects(10, 10), false);

		assertEquals(myRow.connectEdgeWord(w, false), true);
		
		Word ww = myRow.disconnectEdgeWord(10, 20);


		assertEquals(myRow.getWords().size(), 2);
		
		assertEquals(myRow.getPoem(), p);
		
		int t = myRow.getWidth();
		
		assertEquals(myRow.getWidth(), t);
		
		
		Word w2 = new Word(5, 25, 0, "test new word",WordType.adj);

		assertEquals(myRow.connectEdgeWord(w2, false), true);
		
		
		Word w3 = myRow.disconnectEdgeWord(5, 25);
		
		assertEquals(w3, null);

		myRow.setPosition(2, 4);
		
		assertEquals(myRow.getPosition().x, 2);
		assertEquals(myRow.getPosition().y, 4);
		
		Word w4 = new Word(1000, 4, 0, "test new word",WordType.adj);

		assertEquals(myRow.intersectsWord(w4), false);
		
		w4.setPosition(new Position(2,4,0));

		assertEquals(myRow.intersectsWord(w4), true);

	}
}
