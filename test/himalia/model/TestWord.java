package himalia.model;

import junit.framework.TestCase;

public class TestWord extends TestCase{

	public void testDimensions(){
		Word w = new Word(10, 1, 0, "My", WordType.adj);
		
		assertEquals(w.getPosition().x, 10);
		assertEquals(w.getPosition().y, 1);
		assertEquals(w.getValue(), "My");
		
		assertEquals(w.getWidth(), 16);
		//assertEquals(w.getID(), 30);

		w.setPosition(15, 12);
		assertEquals(w.getPosition().x, 15);
		assertEquals(w.getPosition().y, 12);
	}


	public void testIntersect(){
		Word w = new Word(10, 1, 0, "My", WordType.adv);
		
		assertEquals(w.intersect(11, 1), true);
		assertEquals(w.intersect(30, 1), false);
		
		assertEquals(w.intersect(11, 1), true);

	}
	
	/**
	 * Susan add it
	 */
	public void testwithinWordRect(){
		Word w = new Word(10, 1, 0, "My", WordType.adj);
		//valid case
		assertEquals(true,w.withinWordRect(10, 1));
		//invalid
		assertFalse(w.withinWordRect(100, 100));
		assertFalse(w.withinWordRect(10, 100));
		assertFalse(w.withinWordRect(10, 0));
		assertFalse(w.withinWordRect(0, 5));
		assertFalse(w.withinWordRect(0, 5));
	}
	/**
	 * Susan add
	 */
	public void testgetHeight(){
		Word w = new Word(10, 1, 0, "My", WordType.adj);
		assertEquals(15,w.getHeight());
	}
	/**
	 * Susan add
	 */
	public void testsetPosition(){
		Position p1=new Position(1,2,0);
		Word w = new Word(10, 1, 0, "My", WordType.adj);
		w.setPosition(p1);
		assertEquals(1,w.getPosition().x);
		assertEquals(2,w.getPosition().y);
	}
	/**
	 * Susan add
	 */
	public void testsetBottom(){
		Word w = new Word(10, 1, 0, "My", WordType.adj);
		w.setBottom();
		assertEquals(1,w.getPosition().z);
	}
	/**
	 * Susan add
	 */
	public void testsetTop(){
		Word w = new Word(10, 1, 1, "My", WordType.adj);
		w.setTop();
		assertEquals(0,w.getPosition().z);
	}
	/**
	 * Susan
	 * ####hard to test because ID is a static int
	 */
	public void testgetID(){
		Word w = new Word(10, 1, 1, "My", WordType.adj);
		w.getID();
	}
	/**
	 * Susan add it
	 */
	public void testgetType(){
		Word w = new Word(10, 1, 1, "My", WordType.adj);
		assertEquals(w.getType(),WordType.adj);
	}
	
	
	public void testIntersectWord(){
		Word w = new Word(10, 10, 0, "My", WordType.adv);
		Word w2 = new Word(12, 12, 0, "My", WordType.adv);
		Word w3 = new Word(9, 9, 0, "My", WordType.adv);
		Word w4 = new Word(12, 9, 0, "My", WordType.adv);
		Word w5 = new Word(9, 12, 0, "My", WordType.adv);
		Word w6 = new Word(50, 12, 0, "My", WordType.adv);
		assertEquals(w.intersectWord(w2), true);
		assertEquals(w.intersectWord(w3), true);
		assertEquals(w.intersectWord(w4), true);
		assertEquals(w.intersectWord(w5), true);
		assertEquals(w.intersectWord(w6), false);


	}

}
