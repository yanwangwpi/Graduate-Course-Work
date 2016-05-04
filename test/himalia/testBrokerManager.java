package himalia;

import java.util.ArrayList;

import himalia.model.Board;
import himalia.model.Model;
import himalia.model.Position;
import himalia.model.Word;
import himalia.model.WordType;
import himalia.view.RequestGUI;
import junit.framework.TestCase;
import broker.util.*;
import broker.handler.*;
import broker.ipc.*;
import broker.server.*;
import broker.BrokerClient;

public class testBrokerManager extends TestCase {
	BrokerManager bmr;
	BrokerManager bmrbackup;
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
		  RequestGUI rgui=new RequestGUI(m);
		  bmr=new BrokerManager(rgui,m);
		  bmr.status="no swap request";
	}

	protected void tearDown() throws Exception {
		bmr=null;
	}
	
	public void testcheckStar(){
		String Id1="Sender001";
		String Id2="Receiver002";
		String  offerTypes[]={"*"};
		String  requestTypes[]={"*"};
		String  offerWords[]={"*"};
		String  requestWords[]={"*"};
		String Id3="Sender003";
		String Id4="Receiver004";
		String  offerTypes1[]={"adv"};
		String  requestTypes1[]={"a"};
		String  offerWords1[]={"adv"};
		String  requestWords1[]={"b"};
		Swap s=new Swap(Id1, Id2, 1, offerTypes, requestTypes, offerWords, requestWords);
		Swap s1=new Swap(Id3, Id4, 1, offerTypes1, requestTypes1, offerWords1, requestWords1);
		assertEquals(bmr.checkReqStar(s),true);
		assertEquals(bmr.checkReqStar(s1),false);
	}
	
	public void testbrokerGone(){
	bmr.brokerGone();
	}
	public void testsendMessage(){
	    bmr.connect();
		bmr.sendMessage("hello");
	}
	public void testreOrderReqWords(){
		
	}
	public void testProcess(){
		bmr.connect();
		//deny
		bmr.process(bmr.broker, "DENY_SWAP:d05c157a-8f06-4678-b0c1-50ca4ce0cbe5");
		//matchswap->success/no star
		bmr.process(bmr.broker, "MATCH_SWAP:"+"b2e73f0f-daf2-4fce-912d-9617f3d05ed9:"+bmr.getID()+":1:noun:Spring:adj:acc:");
		//matchswap->failed
		bmr.process(bmr.broker, "MATCH_SWAP:"+"b2e73f0f-daf2-4fce-912d-9617f3d05ed9:"+bmr.getID()+":1:noun:Spring:adj:great:");
		//matchswap->success/star
		bmr.process(bmr.broker, "MATCH_SWAP:"+"b2e73f0f-daf2-4fce-912d-9617f3d05ed9:"+bmr.getID()+":1:noun:Beauty:adj:*:");
		bmr.process(bmr.broker, "MATCH_SWAP:"+"b2e73f0f-daf2-4fce-912d-9617f3d05ed9:"+bmr.getID()+":1:noun:Beauty:*:*:");	
		bmr.process(bmr.broker, "MATCH_SWAP:"+"b2e73f0f-daf2-4fce-912d-9617f3d05ed9:"+bmr.getID()+":2:noun:verb:Beauty:ask:*:adj:*:aba:");
		//confirm->requestor
		bmr.process(bmr.broker, "CONFIRM_SWAP:"+bmr.getID()+":b2e73f0f-daf2-4fce-912d-9617f3d05ed9:1:noun:Spring:adv:never:");
		//confirm->acceptor
		bmr.process(bmr.broker, "CONFIRM_SWAP:"+"b2e73f0f-daf2-4fce-912d-9617f3d05ed9:"+bmr.getID()+":1:noun:Spring:adj:aaa:");

	}
	public void testconvertStringtypeToWordType(){
		assertEquals(WordType.unknown,bmr.convertStringtypeToWordType("unknown"));
		assertEquals(WordType.verb,bmr.convertStringtypeToWordType("verb"));
		assertEquals(WordType.adv,bmr.convertStringtypeToWordType("adv"));
		assertEquals(WordType.conj,bmr.convertStringtypeToWordType("conj"));
		assertEquals(WordType.pron,bmr.convertStringtypeToWordType("pron"));
		assertEquals(WordType.prefix,bmr.convertStringtypeToWordType("prefix"));
		assertEquals(WordType.suffix,bmr.convertStringtypeToWordType("suffix"));
		assertEquals(WordType.prep,bmr.convertStringtypeToWordType("prep"));
		assertEquals(WordType.unknown,bmr.convertStringtypeToWordType("hello"));
	}
	public void testconnect(){
		bmr.connect();
	}
	public void testshutdown(){
		bmr.shutdown();
	}
	public void testgetID(){
		bmr.getID();
	}
	public void testgetStatus(){
	assertEquals("no swap request",bmr.getStatus());
	}
	public void testisConnect(){
	bmr.isConnected();
	}
	public void testsetStatus(){
		bmr.setStatus("success");
		assertEquals("success",bmr.getStatus());
	}
	public void testinValidRequest(){
		bmr.connect();
		bmr.process(bmr.broker, "MATCH_SWAP:"+"b2e73f0f-daf2-4fce-912d-9617f3d05ed9:"+bmr.getID()+":1:Sucks!:*:*:*:");	
	}
}
