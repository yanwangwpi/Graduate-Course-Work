package himalia.controller;

import java.util.ArrayList;

import himalia.BrokerManager;
import himalia.model.Board;
import himalia.model.Model;
import himalia.model.Position;
import himalia.model.Word;
import himalia.model.WordType;
import himalia.view.CyberPoetrySlamGUI;
import himalia.view.RequestGUI;
import junit.framework.TestCase;
/**
 * 
 * @author susanqin
 *
 */
public class TestRequestSwapController extends TestCase {
	RequestSwapController reqCon;
	
	protected void setUp() throws Exception {
		
		Model model ;
		Board board = null;
		Word word = new Word(1,1,0,"Hello", WordType.adj);
		ArrayList<Word> wrds = new ArrayList<Word>();
		wrds.add(word);
		Position posProtect = new Position(0,0,0);
		Position posUnprotect =new Position(0,250,0);
		int heightProtect = 250;
		int heightUnprotect=250; 
		int widthProtect=500; 
		int widthUnprotect=500;
		Word w1=new Word(10,10, 0,"Spring", WordType.noun);
//		System.out.println("no.1: "+w1.getID()); 
		    Word w2=new Word(11,20, 0,"aaa", WordType.adj);
//		System.out.println("no.2: "+w2.getID());
		    Word w3=new Word(12,30, 0,"aba", WordType.adv);
//		System.out.println("no.3: "+w3.getID());
		    Word w4=new Word(13,20, 0,"abb", WordType.adj);
		//    System.out.println("no.4: "+w4.getID());
		    Word w5=new Word(14,20, 0,"abc", WordType.adj);
		    Word w6=new Word(60,20, 0,"acc", WordType.adj);
		    board = new Board(wrds, posProtect,posUnprotect, heightProtect,heightUnprotect,widthProtect, widthUnprotect);
			board.getUnProtectedRegion().addWord(w1);
			board.getUnProtectedRegion().addWord(w2);
			board.getUnProtectedRegion().addWord(w3);
			board.getUnProtectedRegion().addWord(w4);
			board.getUnProtectedRegion().addWord(w5);
			board.getUnProtectedRegion().addWord(w6);
		
		model=new Model(board);
		
		CyberPoetrySlamGUI frame = new CyberPoetrySlamGUI(model);

		RequestGUI rgui=new RequestGUI(model);
		BrokerManager bm=new BrokerManager(rgui, model);
		model.setBrokerManager(bm);
		if (bm.connect("localhost")) {
			rgui.setBroker(bm);
			frame.setRequestGui(rgui);
		} else if (bm.connect("gheineman.cs.wpi.edu")) {
			rgui.setBroker(bm);
			frame.setRequestGui(rgui);
		}
		reqCon =new RequestSwapController(model, bm);
	}
   public void testprocess(){
	   ArrayList<String> a1=new ArrayList<String>();
		a1.add("*");
		ArrayList<String> a2=new ArrayList<String>();
		a2.add("*");
		ArrayList<String> a3=new ArrayList<String>();
		a3.add("*");
		ArrayList<String> a4=new ArrayList<String>();
       a4.add("*");
       reqCon.setMsg(1, a1, a2, a3, a4);
      
	   reqCon.process();
	   reqCon.offerTypes.clear();
       reqCon.offerWords.clear();
       reqCon.requestTypes.clear();
       reqCon.requestWords.clear();
       ArrayList<String> a5=new ArrayList<String>();
		a5.add("noun");
		ArrayList<String> a6=new ArrayList<String>();
		a6.add("*");
		ArrayList<String> a7=new ArrayList<String>();
		a7.add("*");
		ArrayList<String> a8=new ArrayList<String>();
      a8.add("*");
      reqCon.setMsg(1, a5, a6, a7, a8);
      reqCon.process();
      reqCon.offerTypes.clear();
      reqCon.offerWords.clear();
      reqCon.requestTypes.clear();
      reqCon.requestWords.clear();
      ArrayList<String> a9=new ArrayList<String>();
		a9.add("noun");
		ArrayList<String> a10=new ArrayList<String>();
		a10.add("Spring");
		ArrayList<String> a11=new ArrayList<String>();
		a11.add("verb");
		ArrayList<String> a12=new ArrayList<String>();
		a12.add("ask");
	     reqCon.setMsg(1, a9, a10, a11, a12);
	     reqCon.process();
	     reqCon.offerTypes.clear();
	     reqCon.offerWords.clear();
	     reqCon.requestTypes.clear();
	     reqCon.requestWords.clear();
     	ArrayList<String> a13=new ArrayList<String>();
		a13.add("verb");
		ArrayList<String> a14=new ArrayList<String>();
		a14.add("ask");
		ArrayList<String> a15=new ArrayList<String>();
		a15.add("verb");
		ArrayList<String> a16=new ArrayList<String>();
		a16.add("ask");
		reqCon.setMsg(1, a13, a14, a15, a16);
  reqCon.process();
   }
   public void testsetMsg(){
	   	ArrayList<String> a1=new ArrayList<String>();
		a1.add("*");
		ArrayList<String> a2=new ArrayList<String>();
		a2.add("*");
		ArrayList<String> a3=new ArrayList<String>();
		a3.add("*");
		ArrayList<String> a4=new ArrayList<String>();
        a4.add("*");
        reqCon.setMsg(1, a1, a2, a3, a4);
        assertEquals(reqCon.wordCount,1);
        assertEquals(reqCon.offerTypes.get(0),"*");
        assertEquals(reqCon.offerWords.get(0),"*");
        assertEquals(reqCon.requestTypes.get(0),"*");
        assertEquals(reqCon.requestWords.get(0),"*");
        reqCon.offerTypes.clear();
        reqCon.offerWords.clear();
        reqCon.requestTypes.clear();
        reqCon.requestWords.clear();
   }
   public void testReorder(){
	   	String ws[]={"*","ok","*","no","*"};
		String ts[]={"*","noun","verb","noun","*"};
       ArrayList<Integer> result=reqCon.reOrderReqWords(ws,ts); 
      
   }
	protected void tearDown() throws Exception {
		reqCon=null;
	}

}
