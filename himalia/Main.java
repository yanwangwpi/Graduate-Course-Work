package himalia;

import himalia.controller.DownloadWordsController;
import himalia.model.*;
import himalia.view.CyberPoetrySlamGUI;
import himalia.view.RequestGUI;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;



public class Main {

	/**
	 * Launch the application.
	 */

	
	static final String defaultStorage = "Himalia.storage";

	
	static Board loadState(String location) {
		ObjectInputStream ois = null;
		BoardMemento m = null;
		Board b = null;
		 //replaced by download
		String currentWorkingDirectory = System.getProperty("user.dir");
						
		DownloadWordsController dwc = new DownloadWordsController(currentWorkingDirectory + "//WordsAndTypes.csv");
			

		int height = 327;
		int width=500; 
				
		 try {
			 ois = new ObjectInputStream (new FileInputStream(location));
			 m = (BoardMemento) ois.readObject();
			 ois.close();
			 ArrayList<Word> bwords =  new ArrayList<Word>();
			 b = new Board(bwords,new Position(0,0,0), new Position(0,height+1,0), height, height, width,width);
			 b.restore(m);
			 return b;
		 } catch (Exception e) {
			 System.out.println(e.getMessage());
			 // unable to perform restore
		 }

		 // close down safely
		 if (ois != null) {
			 try { ois.close(); } catch (IOException ioe) { }
		 }

		 ArrayList<Word> bwords = dwc.getGameWords();
		 
		 b = new Board(bwords,new Position(0,0,0), new Position(0,height+1,0), height, height, width,width);
		 return b;
	}
	
	/** Launch GUI by installing window controller on exit. */ 
	public static void main (String args[]) {
		System.out.println ("Welcome to Himalia CyberPoetrySlam!");
		
		// Create the entity objects that form the basis of our model
		final Board b = loadState(defaultStorage);
		final Model model = new Model(b);
		model.setStorage(defaultStorage);
		
		System.out.println ("Model constructed");	
		// In some cases we can only construct the view once model 
		// is available. In other cases, we can construct the View 
		// object and then configure it later with the model object. 
		// In this example, we show the former. 
			
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

		frame.addWindowListener (new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				Model.storeState(b, defaultStorage);
				System.exit(0);
			}
		});

     frame.setVisible(true);

	}


}
