package himalia.model;
import java.util.ArrayList;

import himalia.model.*;
import junit.framework.TestCase;
import junit.framework.TestResult;

public class TestUnprotectedRegion extends TestCase {
	UnprotectedRegion r;
	ArrayList<Word> ws;
	Position pos;
/*
 * (non-Javadoc)
 * @see junit.framework.TestCase#setUp()
 */
   protected void setUp(){
	   Word w1=new Word(10,10, 0,"Spring", WordType.noun);
//	System.out.println("no.1: "+w1.getID()); 
	    Word w2=new Word(11,20, 0,"aaa", WordType.adj);
//	System.out.println("no.2: "+w2.getID());
	    Word w3=new Word(12,30, 0,"aba", WordType.adv);
//	System.out.println("no.3: "+w3.getID());
	    Word w4=new Word(13,20, 0,"abb", WordType.adj);
	//    System.out.println("no.4: "+w4.getID());
	    Word w5=new Word(14,20, 0,"abc", WordType.adj);
	    Word w6=new Word(60,20, 0,"acc", WordType.adj);
	    pos=new Position(0,0,0);
	    ws=new ArrayList<Word>();
	    ws.add(w1);
	    ws.add(w2);
	    ws.add(w3);
	    ws.add(w4);
	    ws.add(w5);
	    ws.add(w6);
		//UnprotectedRegion x=new UnprotectedRegion();
		r=new UnprotectedRegion(ws, pos, 200, 200);
		
    }
	
/**
 * test:public ArrayList<Word> exploreWords(Sortby sortby)
 */
	public void testExploreWords(){
		/**
		Word w1=new Word(10,10, 0,"Spring", WordType.noun);
	    Word w2=new Word(11,20, 0,"aaa", WordType.adj);
	    Word w3=new Word(12,30, 0,"aba", WordType.adv);
	    Word w4=new Word(13,20, 0,"abb", WordType.adj);
	    Word w5=new Word(14,20, 0,"abc", WordType.adj);
	    Word w6=new Word(15,20, 0,"acc", WordType.adj);
		 */
	//default: no sort

//	ArrayList<Word> original=(ArrayList<Word>) r.words.clone();
//	ArrayList<Word> wns=new ArrayList<Word>();
//	wns=r.exploreWords(Sortby.noSort, false, wns);
//	int i;
//	for(i=0;i<ws.size()-1;i++)
//		assertEquals(wns.get(i).getValue(),original.get(i).getValue());
//	//Sort by initial
//	String initialOrder[]={"aaa","aba","abb","abc","acc","Spring","Summer"};
//	ArrayList<Word> ws=new ArrayList<Word>();
//	ws=r.exploreWords(Sortby.initial);
//	//System.out.println(ws.get(6).getValue());
//	for(i=0;i<ws.size();i++)
//		assertEquals(ws.get(i).getValue(),initialOrder[i]);
//	
//	
////	Sort by Type
////	 * type order:adj, adv, conj,noun,prefix,suffix
////	 * 
//	 
//	String typeOrder[]={"aaa","abb","abc","acc","aba",};
//	ArrayList<Word> wq=new ArrayList<Word>();
//	wq=r.exploreWords(Sortby.type);	
//    ArrayList<String> adjs=new ArrayList<String>();
//	
//	for(i=0;i<4;i++)
//		adjs.add(wq.get(i).getValue());
//	for(i=0;i<4;i++)
//		assertEquals(adjs.contains(typeOrder[i]),true);
//	assertEquals(wq.get(4).getValue(),typeOrder[4]);

	//assertEquals(wq.get(5).getValue(),typeOrder[5]);
	}
	/**
	 * test function showWord()
	 * given the wordID,find the word in unprotected area words,if not 
	 * found, return null
	 */
	public void testshowWord(){
		//valid 
		//System.out.println("this rounds:");
		//assertEquals(r.showWord(1).getValue(),"aaa");
		//System.out.println(r.showWord(1).getValue());

		//invalid
		//assertEquals(r.showWord(25),null);
		
	}
	/**
	 * test function:moveWord(int wordID, Position desPos)
	 */
	public void testmoveWord(){
//		Position p1=new Position(50,50,0);
//		//valid
//		assertEquals(r.moveWord(r.words.get(1).getID(), p1),true);
//		//invalid case 1: out of boundary
//		Position p2=new Position(250,250,0);
//		assertEquals(r.moveWord(r.words.get(1).getID(), p2),false);
//		//invalid case 2: no such ID
//		assertEquals(r.moveWord(31, p1),false);
		
	}
	
	/**
	 * test function:public void addWord(Word w)
	 */
	public void testAddWord(){
		Word w1=new Word(40,20, 0,"Summer", WordType.noun);
		try {
			r.addWord(w1);
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(true,r.words.contains(w1));
	}
	/**
	 * test: boolean deleteWord(Word w)
	 */
	public void testDeleteWord(){
		//valid 
		assertEquals(r.deleteWord(r.words.get(0)).getValue(),"Spring");
		
	}
	/**
	 * test:public Position getPosition()
	 */
	public void testGetPosition(){
		assertEquals(r.getPosition().x,0);
		assertEquals(r.getPosition().y,0);
		assertEquals(r.getPosition().z,0);
	}
	/**
	 * test:public int getWidth()
	 */
	public void testGetWidth(){
		assertEquals(r.getWidth(),200);
	
	}
	/**
	 * test:public int getHeight()
	 */
	public void testgetHeight(){
		assertEquals(r.getHeight(),200);
	
	}
	/**
	 * test:public int getWords()
	 */
	public void testgetWords(){
		assertEquals(r.getWords(),r.words);
	
	}
	/**
	 * test:public boolean withinBoard(int x, int y)
	 */
	public void testwithinBoard(){
		//invalid 1: x<0,y normal
		assertEquals(r.withinBoard(-1, 202),false);
		//invalid 2: x>boundary,y normal
		assertEquals(r.withinBoard(300, 202),false);
		//invalid 3: x normal,y <0
		assertEquals(r.withinBoard(101, -1),false);
		//invalid 4: x normal ,y>boundary
		assertEquals(r.withinBoard(101, 302),false);
		//valid
		assertEquals(r.withinBoard(101, 102),true);
	
	}
	/**
	 * test: public void foreGroundWord(Word w)
	 */
	public void testforGround(){
		
		int id=r.words.get(0).getID();
		r.foreGroundWord(r.words.get(0));
		assertEquals(id, r.words.get(r.words.size()-1).getID());
	}
	/**
	 * test:public void scrambleWords()
	 */
	public void testscrambleWords(){
		
		assertEquals(true,r.scrambleWords());
	}
		
	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
   protected void tearDown(){
	    Word w1=null;		  
	    Word w2=null;	
	    Word w3=null;	  
	    pos=null;
	    ws=null;
		r=null;
	}
   
   public void testfindWord(){
	   Position p1=r.words.get(0).getPosition();
	   assertEquals(r.findWord(p1).getValue(),"Spring");
   }
}
