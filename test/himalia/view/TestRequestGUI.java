package himalia.view;

import himalia.BrokerManager;
import himalia.model.Board;
import himalia.model.Model;
import himalia.model.Position;
import himalia.model.Word;
import himalia.model.WordType;

import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import junit.framework.TestCase;
/**
 * 
 * @author susanqin
 *
 */
public class TestRequestGUI extends TestCase {
	RequestGUI rgui;
	BrokerManager bm;
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
		  rgui=new RequestGUI(m);
		  bm=new BrokerManager(rgui,m);
		  bm.connect();
		  rgui.setBroker(bm);
		  
	}
   
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	public void testsetStatus(){
	rgui.setStatus("Success");
	}
	public void testChangeComboBoxNumWord(){
		rgui.changeComboBoxNumWord();
		rgui.comboBoxNumWord.setSelectedIndex(1);
		rgui.changeComboBoxNumWord();
		
	}
	public void testchangeComboBoxTypeSend(){
		rgui.changeComboBoxTypeSend();
		rgui.comboBoxWordSend.setSelectedIndex(2);
		rgui.changeComboBoxTypeSend();
		
	}
	public void testClear(){
		
		rgui.clearRequestInfo();
	}
	public void testgetcomboBoxTypeRec(){
		rgui.getcomboBoxTypeRec();
	}
	public void testgetcomboBoxTypeSend(){
		rgui.getcomboBoxTypeSend();
	}
	public void testgetFrame(){
		rgui.getFrame();
	}
	public void testchangebtnSubmit(){
		ArrayList<String> offerTypes=new ArrayList<String>();
		offerTypes.add("adj");
	    ArrayList<String> reqTypes=new ArrayList<String>();
	    reqTypes.add("adj");
	    ArrayList<String> offerWords=new ArrayList<String>();
	    offerWords.add("sweet");
	    ArrayList<String> reqWords=new ArrayList<String>();
	    reqWords.add("bitter");
	    rgui.reqTypes=reqTypes;
	    rgui.reqWords=reqWords;
	    rgui.offerTypes=offerTypes;
	    rgui.offerWords=offerWords;
	    rgui.wordCount=1;
		rgui.changebtnSubmit();
	}
	public void testchangebtnAddSend(){
		rgui.changebtnAddSend();
		
	}
	public void testfilterWordsForCombo(){
		ArrayList<String> offerTypes=new ArrayList<String>();
		offerTypes.add("adj");
	    ArrayList<String> reqTypes=new ArrayList<String>();
	    reqTypes.add("adj");
	    ArrayList<String> offerWords=new ArrayList<String>();
	    offerWords.add("sweet");
	    ArrayList<String> reqWords=new ArrayList<String>();
	    reqWords.add("bitter");
	    rgui.reqTypes=reqTypes;
	    rgui.reqWords=reqWords;
	    rgui.offerTypes=offerTypes;
	    rgui.offerWords=offerWords;
	    rgui.wordCount=1;
	    rgui.filterWordsForCombo("Send", "adj");
	    rgui.filterWordsForCombo("Send", "adv");
	    rgui.filterWordsForCombo("Send", "verb");
	    rgui.filterWordsForCombo("Send", "pron");
	    rgui.filterWordsForCombo("Send", "conj");
	    rgui.filterWordsForCombo("Send", "noun");
	    rgui.filterWordsForCombo("Send", "prefix");
	    rgui.filterWordsForCombo("Send", "suffix");
	    rgui.filterWordsForCombo("Send", "unknown");
	    rgui.filterWordsForCombo("Send", "prep");
	    rgui.filterWordsForCombo("Send", "xxx");
	}
	public void testchangebtnAddRec(){
		rgui.changebtnAddRec();
	}
}
