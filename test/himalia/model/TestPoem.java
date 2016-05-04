/**
 * 
 */
package himalia.model;

import java.util.ArrayList;

import junit.framework.TestCase;

/**
 * @author saadjei
 *
 */
public class TestPoem extends TestCase {

	public void testPoem() throws Exception{
		Poem p = new Poem();
		Poem p2 = new Poem();
		//Word w = new Word(15, 20, 0, " new word",WordType.adj);
		
		Row myRow = new Row(p, new Word(10, 20, 0, "Seth",WordType.adj), new Word(10, 20, 0, "same ",WordType.adj));
		
		Row myRow2 = new Row(p2, new Word(10, 20, 0, "Seth",WordType.adj), new Word(10, 20, 0, "same ",WordType.adj));

		//p.addInitialRow(myRow);
		
		//p2.addInitialRow(myRow2);
		
		assertEquals(p.getRows().size(), 1);
		
		assertEquals(p2.getRows().size(), 1);
		
		//Row myRow3 = new Row(p, new Word(10, 20, 0, "Seth",WordType.adj), new Word(10, 20, 0, "same ",WordType.adj));

		//assertEquals(p.addInitialRow(myRow3), false);
		
		//assertEquals(p.connectBottomRow(myRow3), true);

		assertEquals(p.connectPoem(p2), true);
		
		Position pos = new Position(5, 10,0);
		
		assertEquals(p.setPosition(pos), true);
		assertEquals(p.pointIntersects(50, 70), false);

		assertEquals(p.pointIntersects(5,10), true);

		//assertEquals(p.shiftRow(pos, new Position(5,10,0)), true);
		
		ArrayList<Poem> pRes = p.disconnectPoem(new Position(5,15,0)); 
		
		assertEquals(pRes.size(), 1);
		
		assertEquals(p.intersects(p.getRow(0).getWords().get(0)), true);
		
		Word w4 = new Word(500, 500, 0, "Seth",WordType.adj);
		
		assertEquals(p.intersects(w4),false);
		
	}
	
	
	public void testShiftRow() throws Exception{
		Poem p = new Poem();
		Poem p2 = new Poem();
		Poem p3 = new Poem();
		Row myRow = new Row(p, new Word(10, 20, 0, "Sample",WordType.adj), new Word(17, 20, 0, " Spaced",WordType.adj));
		
		Row myRow1 = new Row(p2, new Word(15, 20, 0, "Samy",WordType.adj), new Word(21, 20, 0, "queue1 ",WordType.adj));
		Row myRow2 = new Row(p3, new Word(16, 20, 0, "Sam45y",WordType.adj), new Word(26, 20, 0, "queue2 ",WordType.adj));
		
		p.connectBottomRow(myRow1);
		p.connectBottomRow(myRow2);
		
		
		assertEquals(p.shiftRow(new Position(10,20,0), new Position(20,20,0)), true);
		
		Word ww= p.disconnectEdgeWord(2, 0);
		//assertEquals(ww,null);
		
	}
	
}
