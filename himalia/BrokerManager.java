package himalia;
import broker.handler.*;
import broker.util.ConfirmSwapMessage;
import broker.util.IProtocol;
import broker.util.MatchSwapMessage;
import broker.util.Swap;
import broker.*;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

import himalia.controller.DownloadWordsController;
import himalia.model.Model;
//import himalia.model.SwapRequest;
import himalia.model.Word;
import himalia.model.WordType;
import himalia.view.CyberPoetrySlamGUI;
//import himalia.model.Shape;
import himalia.view.RegionPanel;
import himalia.view.RequestGUI;



/**
 * Provides a case study in how to use the BrokerClient in your code. You should break
 * the logic into multiple controllers to handle the different responses from the broker.
 */
/**
 * 
 * @author susanqin,Ying_Wang
 *
 */
public class BrokerManager implements IHandleBrokerMessage {
	
	/** Need Model to be able to make any resolution. */
	Model model;
	
	/** Need Application in case need to make changes to GUI. */
	RequestGUI gui;
	
	/** Your interface to the broker. */
	BrokerClient broker;
	
	/** Thread managing connections. When you are done, call shutdown for clean exit. */
	ReaderThread thread;
	
	/** Determines connectivity. */
	boolean connected;
	
	String status;
	/**The valid words we accept*/
	ArrayList<String> cleanWords;
	/**
	 * Will need to be changed once we decide where broker is being run.
	 * 
	 * In general, information such as this shouldn't be in the compiled Java file 
	 */
	public final static String brokerHost = "gheineman.cs.wpi.edu";
	public final static int    brokerPort = 9172;
	/**
	 * Constructor of Brokermanager,set the gui and the model for brokermanager
	 * @param frame2: GUI
	 * @param model: model
	 */
	public BrokerManager(RequestGUI frame2, Model model) {
		this.gui = frame2;
		this.model = model;
		this.status="no swap request";
		this.cleanWords=new ArrayList<String>();
		String currentWorkingDirectory = System.getProperty("user.dir");	
		DownloadWordsController dwc = new DownloadWordsController(currentWorkingDirectory + "//GameWordsAndTypes.csv");
     	ArrayList<Word> allgamewords=dwc.getAllGameWords();
		int size=allgamewords.size();
		for(int i=0;i<size;i++){
			this.cleanWords.add(i,allgamewords.get(i).getValue());
		}
	}
	
	/**
	 * Return the Status of current swap
	 * @return status
	 */
	public String getStatus(){
	    return this.status;
	}
	/**
	 * Set the Status of current swap
	 * @param msg: Status to be set
	 */
	public void setStatus(String msg){
	    this.status=msg;
	}
	
	/** Are we connected to the broker? */
	public boolean isConnected() { return connected; }
	
	/** Get our unique id. */
	public String getID() { 
		if (broker == null) { return null; }
		return broker.getID(); 
	}
	
	/** Cleanly shutdown interface to broker. */
	public void shutdown() { 
		if (thread != null) { thread.shutdown(); }
	}
	
	/** Try to connect to broker on default host. */
	public boolean connect() {
		return connect(brokerHost);
	}
	
	/**
	 * Try to connect to the broker.
	 * 
	 * @param host 
	 */
	public boolean connect(String host) {
		
		broker = new BrokerClient(host, brokerPort);

		if (!broker.connect()) {
			System.err.println("unable to connect to broker on " + host);
			broker.shutdown();
			return false;
		}
		
		// at this point we are connected, and we will block waiting for 
		// any messages from the broker. These will be sent to the process
		System.out.println("Connected as : " + broker.getID() + " : " + broker.getStatus());
		
		// start thread to process commands from broker.
		thread = new ReaderThread(broker, this);
		thread.start();
		
		// nothing more to do here. This thread is now independent and will respond
		// to broker messages, as well as our own concerns.
		return true;
	}

	/** Helper method for debugging. */
	/**
	 * Sends swap request to the broker
	 */
	public void sendMessage(String msg) {
		broker.getBrokerOutput().println(msg);
	}
	/**
	 * Convert a string to a WordType
	 * @param s String to be converted
	 * @return the WordType 
	 */
	public WordType convertStringtypeToWordType(String s){
		WordType wt;
		switch(s){
			case "adj": wt=WordType.adj;break;
			case "adv": wt=WordType.adv;break;
			case "verb":wt=WordType.verb;break;
			case "pron":wt=WordType.pron;break;
			case "prep":wt=WordType.prep;break;
			case "conj":wt=WordType.conj;break;
			case "noun":wt=WordType.noun;break;
			case "prefix":wt=WordType.prefix;break;
			case "suffix":wt=WordType.suffix;break;
			case "unknown": wt=WordType.unknown;break;
			case "determiner": wt=WordType.determiner;break;
	     default: wt=WordType.unknown;break;
		}
		return wt;
	}
	/**This reorder the swapping words according to their 
	 * Priority:[word,type]>[*,type]>[*,*]*/
	public static ArrayList<Integer> reOrderReqWords(String[] reqWs, String[] reqTs){
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
	/**This is the core process of handling a swap request */
	@Override
	public void process(BrokerClient broker, String msg) {
		String[] Add = null;
		String[] Remove=null;
		System.out.println("Process message:" + msg);
		if (msg.startsWith(IProtocol.denySwapMsg)) {
			this.status="Denied swap request";
			this.model.getBoard().getUnProtectedRegion().sendBackSwap();
			this.model.getBoard().notifyListeners();
			Model.storeState(model.getBoard(), model.getStorage());
			ShowResult(null,null);
			return;
		}
		// some third party has asked for a swap. Pull this out into its own controller
		if (msg.startsWith(IProtocol.matchSwapMsg)) {
			Swap s = MatchSwapMessage.getSwap(msg);
			System.out.println("Third party trying a swap:" + s.flatten());		
			// swap is requesting words. We have to check to see if we have these
			// requested words.
			
			boolean failed = true;
			ArrayList<Word> ups=(ArrayList<Word>) model.getBoard().getUnProtectedRegion().getWords().clone();
			ArrayList<Integer> order=reOrderReqWords(s.offerWords,s.offerTypes);
			for(int i=0;i<s.n;i++)	{
				failed=true;
				if(ups.size()<=0){
					System.out.println("There are no words in unprotected area");
					failed=true;
					break;
				}
				if(s.requestWords[order.get(i)].equals("*")&&s.requestTypes[order.get(i)].equals("*"))
				{	failed=false;
				    ups.remove(0);
					break;
				}
				for (Word w : ups) {
				
				if(s.requestWords[order.get(i)].equals("*")&&s.requestTypes[order.get(i)].equals("*")==false)
				{
					failed=false;
					if(w.getType().toString().equals(s.requestTypes[order.get(i)]))
						{
						ups.remove(w);
						break;
						}
				}						
			   if (w.getValue().equals(s.requestWords[order.get(i)])&&w.getType().toString().equals(s.requestTypes[order.get(i)])) 
					{
						failed=false;
						ups.remove(w);
						break;
					}
				}
			}
		if(failed==false){
			//check if the offer words of the one has the "bad words"
			for(int i=0;i<s.n;i++){
				if(s.offerTypes[i]=="*"||this.cleanWords.contains(s.offerWords[i])==false){
					failed=true;
					break;
				}
			}
			
		}
				if (failed) {
					System.out.println("Unable to satisfy swap request");
					broker.getBrokerOutput().println(IProtocol.denySwapMsg + 
							IProtocol.separator + s.requestor_id);
					return;
				}
				
			System.out.println("Accepting satisfy swap request");
			//We have to consider full * and half * situation:
			boolean hasStar=checkReqStar(s);
			
			// what should we do? Agree of course! Here is where your code would
			// normally "convert" wildcards into actual words in your board state.
			// for now this is already assumed (note sample/other swap)
			if(!hasStar)
			{broker.getBrokerOutput().println(IProtocol.confirmSwapMsg + IProtocol.separator + s.flatten());
			return;}
			else{
				
				ups=(ArrayList<Word>) model.getBoard().getUnProtectedRegion().getWords().clone();
				
				 String  temprequestor_id=s.requestor_id;
				 String  tempacceptor_id=s.acceptor_id; 
				 int     tempn=s.n;
				 String  tempofferTypes[] = new String[s.n];
				 String  tempofferWords[] = new String[s.n];
				 String  temprequestTypes[] = new String[s.n];
				 String  temprequestWords[] = new String[s.n];
				for(int i=0;i<s.n;i++){
					tempofferTypes[i]=s.offerTypes[i];
					tempofferWords[i]=s.offerWords[i];
				}
				for(int i=0;i<s.n;i++)	
				{		
						if(s.requestWords[order.get(i)].equals("*")&&s.requestTypes[order.get(i)].equals("*"))
						{	
							temprequestTypes[i]=ups.get(0).getType().toString();
							temprequestWords[i]=ups.get(0).getValue();
							ups.remove(0);
							
						}
						for (Word w : ups) {
							if(s.requestWords[order.get(i)].equals("*")&&s.requestTypes[order.get(i)].equals("*")==false)
							{						
								temprequestTypes[i]=s.requestTypes[order.get(i)];
								
								if(w.getType().toString().equals(s.requestTypes[order.get(i)]))
									{
									temprequestWords[i]=w.getValue();
									ups.remove(w);
									break;
									}
							}
								
									if (w.getValue().equals(s.requestWords[order.get(i)])&&w.getType().toString().equals(s.requestTypes[order.get(i)])) 
									{
										  temprequestTypes[i]=s.requestTypes[order.get(i)];
										  temprequestWords[i]=s.requestWords[order.get(i)];
									ups.remove(w);
									break;
									}
							 }
						   
						
				}	 
				Swap newReq=new Swap(temprequestor_id,tempacceptor_id,tempn,tempofferTypes,tempofferWords,temprequestTypes,temprequestWords);
				broker.getBrokerOutput().println(IProtocol.confirmSwapMsg + IProtocol.separator + newReq.flatten());
			    return;
			}
		}
		
		// Execute the swap
		ArrayList<Word> wordsToRemove=new ArrayList<Word>();
		ArrayList<Word> wordsToAdd=new ArrayList<Word>();
		if (msg.startsWith(IProtocol.confirmSwapMsg)) {
			Swap s = ConfirmSwapMessage.getSwap(msg);		
			System.out.println("Before Swap:");
			System.out.println(s.flatten());
			// carry out the swap. We were the one making the request...	
			if (broker.getID().equals(s.requestor_id)) {
				//just keep swapWords to see if it will be useful in the future
				//model.getBoard().getUnProtectedRegion().getSwapWords().clear();
					for(int i=0;i<s.n;i++){
						Word wordRemove=new Word(0,0,0, s.offerWords[i],this.convertStringtypeToWordType(s.offerTypes[i]));
						wordsToRemove.add(wordRemove);
						Word wordAdd=new Word(0,0,0, s.requestWords[i],this.convertStringtypeToWordType(s.requestTypes[i]));
						wordsToAdd.add(wordAdd);
					}
					Remove=s.offerWords.clone();
					Add=s.requestWords.clone();
			} 
			else {
					for(int i=0;i<s.n;i++){
						Word wordAdd=new Word(0,0,0, s.offerWords[i],this.convertStringtypeToWordType(s.offerTypes[i]));
						wordsToAdd.add(wordAdd);
						Word wordRemove=new Word(0,0,0, s.requestWords[i],this.convertStringtypeToWordType(s.requestTypes[i]));
						wordsToRemove.add(wordRemove);
						}
					Remove=s.requestWords.clone();
					Add=s.offerWords.clone();
			}
	// remove each word as found in Unprotected Region
	if (broker.getID().equals(s.acceptor_id)){
		ArrayList<Word> upwords=model.getBoard().getUnProtectedRegion().getWords();
		for(int i=0;i<s.n;i++)	{		
			for (int j=0;j<upwords.size();j++) {
				{
				if (upwords.get(j).getValue().equals(wordsToRemove.get(i).getValue())){
						if(upwords.get(j).getType().toString().equals((wordsToRemove.get(i).getType().toString()))) {

							Word tmp = model.getBoard().deleteUnprotectedWord(upwords.get(j));                         
                               break;
						}
					}
				}
			}
		}
		
	}
	else{
		this.model.getBoard().getUnProtectedRegion().clearSwapWords();
		Model.storeState(model.getBoard(), model.getStorage());		
		}
		//add new words
		int width=model.getBoard().getUnProtectedRegion().getWidth();
		int height=model.getBoard().getUnProtectedRegion().getHeight();
		int x=model.getBoard().getUnProtectedRegion().getPosition().x;
		int y=model.getBoard().getUnProtectedRegion().getPosition().y;
			Random r = new Random();
			try{
				for (int j = 0; j < s.n; j++) {
					int rx = (int) (r.nextInt(width)%width+x);
					int ry=(int) (r.nextInt(height)%height+y);		
					Word w = new Word (rx, ry, 0, wordsToAdd.get(j).getValue(),wordsToAdd.get(j).getType());
					model.getBoard().getUnProtectedRegion().addWord(w);
				}
			}catch(Exception e){
				e.printStackTrace();
			}

        this.status="confirm swap success";
		ShowResult(Add,Remove); 
		this.model.getBoard().notifyListeners();
		Model.storeState(model.getBoard(), model.getStorage());	
       	return;
		}	
	}
/**
 * Will pop up a message, telling player what words he get and what
 * words he lose
 * @param add: words to be added 
 * @param remove: words to be removed
 */
	public void ShowResult(String add[], String remove[]) {
		if(status.equals("Denied swap request"))
		 JOptionPane.showMessageDialog(gui, "Your request is denied");
		else if(status.equals("confirm swap success")){
		 StringBuffer wordsLost=new StringBuffer();
		 wordsLost.append("You have lost the following word(s):\n");
		 StringBuffer wordsReceived=new StringBuffer();
		 for(int i=0;i<remove.length-1;i++){
		 wordsLost.append(remove[i]+","); 
		 }
		 wordsLost.append(remove[remove.length-1]+".\n");
		 for(int i=0;i<add.length-1;i++){
			 wordsReceived.append(add[i]+","); 
			 }
		 wordsLost.append("You gained the following word(s):\n");
		 wordsReceived.append(add[add.length-1]+".\n");
		 JOptionPane.showMessageDialog(gui, wordsLost.toString()+wordsReceived.toString());
		}
		else{
			;
		}
		
	}
	/**
	 * Helper Method:check if a swap message contains any "*" 
	 * @param s
	 * @return
	 */
	public boolean checkReqStar(Swap s) {
		boolean hasstar=false;
		for(int i=0;i<s.n;i++){
			if((s.requestWords[i].equals("*")))
				{hasstar=true;
				 break;
				}
		}
		return hasstar;
	}

	/**
	 * If Broker vanishes, then we disable buttons. Note there is no logic to
	 * try to reconnect to broker.
	 */
	@Override
	public void brokerGone() {
		System.err.println("Lost broker connection.");
		this.model.getBoard().getUnProtectedRegion().sendBackSwap();
		this.model.getBoard().notifyListeners();
		Model.storeState(model.getBoard(), model.getStorage());
	}


}

