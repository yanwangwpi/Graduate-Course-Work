
package himalia.controller;
import broker.*;
import broker.util.IProtocol;
import broker.util.Swap;
import himalia.model.Model;
//import himalia.model.SwapManager;
//import himalia.model.SwapRequest;
import himalia.model.UnprotectedRegion;
import himalia.model.Word;
import himalia.view.RegionPanel;

import java.awt.Toolkit;
import java.awt.event.*;
import java.util.ArrayList;

import himalia.BrokerManager;
import himalia.view.*;
import himalia.controller.*;
/**
 * This is the controller handling request swap
 * @author susanqin,ying_Wang
 *
 */
public class RequestSwapController extends MouseAdapter {

	/** Needed for controller behavior. */
    Model model;
    /**Broker manager I use*/
    BrokerManager broker;
	/**types of offer words*/
    public  ArrayList<String>  offerTypes;
    /**words I offer*/
	public  ArrayList<String>  offerWords;
	/**types of request words*/
	public  ArrayList<String>  requestTypes;
	/**words I request*/
	public  ArrayList<String>  requestWords;
	/**the number of words I swap*/
	public  int wordCount;
	/** Constructor holds onto key manager objects. */
	public RequestSwapController(Model model,BrokerManager bm) {
		this.model = model;
	    broker=bm;
	}
	 /**set the swap message*/
	 public void setMsg(int n, ArrayList<String> offerTypes2, ArrayList<String> offerWords2,ArrayList<String> reqTypes, ArrayList<String> reqWords){
			this.offerTypes=offerTypes2;
			this.requestTypes=reqTypes;
			this.offerWords=offerWords2;
			this.requestWords=reqWords;
			this.wordCount=n;
			
	 }
 
	/** Act immediately */
	public void process() {	
		// see if we have word we claim to be offering
		int index = 0,j;
		int uprWordSize=model.getBoard().getUnProtectedRegion().getWords().size();
	    ArrayList<Word> uprWords=model.getBoard().getUnProtectedRegion().getWords();
	    ArrayList<Word> uprSwapWords=model.getBoard().getUnProtectedRegion().getSwapWords();
	    String[] strofferWords = new String[wordCount];
		String[] strofferTypes = new String[wordCount];
		for(int i=0;i<wordCount;i++)
	    {strofferWords[i]=offerWords.get(i);
	     strofferTypes[i]=offerTypes.get(i);
	    }
	    ArrayList<Integer> order=reOrderReqWords(strofferWords,strofferTypes);
	    boolean findFlag;
		for(int i=0;i<wordCount;i++){
			findFlag=false;
			for ( j=0; j<uprWords.size()&&findFlag==false;j++) {
				if(offerWords.get(order.get(i)).equals("*")&&offerTypes.get(order.get(i)).equals("*")) {
				  //replace this "*" with real word
					System.out.println("Find Word * with type *");
				   offerWords.remove((int)order.get(i));
				   offerTypes.remove((int)order.get(i));
				   offerWords.add(order.get(i),uprWords.get(0).getValue());
				   offerTypes.add(order.get(i),uprWords.get(0).getType().toString());
				   Word tmp = model.getBoard().deleteUnprotectedWord(uprWords.get(0));
				   uprSwapWords.add(tmp);
					findFlag=true;
					continue;
				}
				if(offerWords.get(order.get(i)).equals("*")&&uprWords.get(j).getType().toString().equals(offerTypes.get(order.get(i)))) {
					   findFlag=true;
					   System.out.println("Find Word * with type not *");
					   offerWords.remove((int)order.get(i));
					   offerTypes.remove((int)order.get(i));
					   offerWords.add(order.get(i),uprWords.get(j).getValue());
					   offerTypes.add(order.get(i),uprWords.get(j).getType().toString());
					   Word tmp = model.getBoard().deleteUnprotectedWord(uprWords.get(0));
					   uprSwapWords.add(tmp);
					findFlag=true;
					continue;
					}
				 if (uprWords.get(j).getValue().equals(offerWords.get(order.get(i)))&&uprWords.get(j).getType().toString().equals(offerTypes.get(order.get(i)))) {
				    
				    System.out.println("Find word: "+ offerWords.get(order.get(i)));
					Word tmp = model.getBoard().deleteUnprotectedWord(uprWords.get(j));
					uprSwapWords.add(tmp);
				    findFlag=true;
				    continue;
				}
		 }
		index=i;
		}
		
		model.getBoard().notifyListeners();
		if (index!=offerWords.size()-1) {
		    System.out.print("words can't find");
			Toolkit.getDefaultToolkit().beep();
			return;
		}
		
		// let's make the request. We are requesting, and we want to have requested word for
		// the offer word.
		//BrokerManager broker = broker.getBroker();
		String strOfferTypes[] = new String[wordCount];
		String strOfferWords[] = new String[wordCount];
		String strRequestTypes[] = new String[wordCount];
		String strRequestWords[] = new String[wordCount];
		for( j=0;j<wordCount;j++){
			strOfferTypes[j]=offerTypes.get(j);
			strOfferWords[j]=offerWords.get(j);
			strRequestTypes[j]=requestTypes.get(j);
			strRequestWords[j]=requestWords.get(j);
		}
		Swap swapRequest = new Swap(broker.getID(), "*", wordCount, 
		    strOfferTypes, strOfferWords,strRequestTypes, strRequestWords);	
		String swapMsg = IProtocol.requestSwapMsg + IProtocol.separator + swapRequest.flatten();
		System.out.println(swapMsg);
		broker.sendMessage(swapMsg);
	}
	/**This reorder the swapping words according to their 
	 * Priority:[word,type]>[*,type]>[*,*]*/
	ArrayList<Integer> reOrderReqWords(String[] reqWs, String[] reqTs){
		ArrayList<Integer> finalArray = new ArrayList<Integer>();	
		 int[] A=new int[reqWs.length];
			for(int i=0;i<reqWs.length;i++)
			{
				if(reqWs[i]!="*"&&reqTs[i]!="*")
				A[i]=0;
				if(reqWs[i]=="*"&&reqTs[i]!="*")
				A[i]=1;
				if(reqWs[i]=="*"&&reqTs[i]=="*")
				A[i]=2;
			}	
			for(int j=0;j<reqWs.length;j++){
			if(A[j]==0)
				finalArray.add(j);
			}
			for(int j=0;j<reqWs.length;j++){
				if(A[j]==1)
					finalArray.add(j);
			}
			for(int j=0;j<reqWs.length;j++){
				if(A[j]==2)
					finalArray.add(j);
			}
	     return finalArray;
	 }
}
