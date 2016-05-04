package himalia.view;

import himalia.BrokerManager;
import himalia.controller.*;
import himalia.model.Model;
import himalia.model.Word;
import himalia.model.WordType;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/**
 * This is the GUI for Request Swap
 * @author susanqin,YanWang
 *
 */
public class RequestGUI extends JFrame{
	/**The number of swapped words */
    int wordCount;
    /**the types of the words I offer */
    ArrayList<String> offerTypes=new ArrayList<String>();
    /**the types of the words I request*/
    ArrayList<String> reqTypes=new ArrayList<String>();
    /**the words I offer*/
    ArrayList<String> offerWords=new ArrayList<String>();
    /**the words I request*/
    ArrayList<String> reqWords=new ArrayList<String>();
    /**swap request controller*/
	RequestSwapController src;
    /**controller will send message to broker manager to handle my swap*/
    BrokerManager broker;
    /**model I used in the CyberPoetrySlam*/
    private Model model;
    
    /**GUI elements*/
	private JPanel panelRequest;
	private JTable table;
	private JTable tableRec;
    private JTable tableSend;
    private JLabel StatusDisplay;
    JComboBox comboBoxTypeRec;
    JComboBox comboBoxTypeSend;
    JComboBox comboBoxWordSend = new JComboBox();
    JComboBox comboBoxWordRec = new JComboBox();
    JComboBox comboBoxNumWord ;
    JButton btnAddSend;
    JButton btnAddRec;
    JButton btnSubmit;

 /**Constructor*/ 
	public RequestGUI(Model m)  {
		
		model=m;
		setResizable(false);
		setTitle("Swap Request");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 369, 500);
		panelRequest = new JPanel();
		
		panelRequest.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(panelRequest);
		JLabel lblNumberOfWords = new JLabel("Number of words to swap");	
		lblNumberOfWords.setFont(new Font("Tahoma", Font.PLAIN, 13));
		JLabel lblPleaseChooseThe = new JLabel("Please choose the word type to send");
		lblPleaseChooseThe.setFont(new Font("Tahoma", Font.PLAIN, 13));	
		int size=model.getBoard().getUnProtectedRegion().getWords().size();
		String num[]= new String[size];
		for(int i=0;i<size;i++){
			num[i]=String.valueOf(i);
		}	
		comboBoxNumWord = new JComboBox(num);		
		comboBoxNumWord.setSelectedIndex(0);	
		comboBoxNumWord.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeComboBoxNumWord();			
			}

			
		});

		String types[]={"*","adj", "adv","verb","pron","prep","conj","noun","prefix","suffix","unknown","determiner","number"};	
		comboBoxTypeSend = new JComboBox(types);
		comboBoxTypeSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
				changeComboBoxTypeSend();
			}		
		});
	
		comboBoxTypeSend.setMinimumSize(new Dimension(60, 27));
		comboBoxTypeSend.setFont(new Font("Lucida Grande", Font.PLAIN, 9));

		String owords[]=new String[model.getBoard().getUnProtectedRegion().getWords().size()+1];
		owords[0]="*";
		for(int m1=1;m1<=size;m1++){
			owords[m1]=model.getBoard().getUnProtectedRegion().getWords().get(m1-1).getValue();
		}
		comboBoxWordSend = new JComboBox(owords);
		comboBoxWordSend.setFont(new Font("Lucida Grande", Font.PLAIN, 12));

		comboBoxTypeRec = new JComboBox(types);
		comboBoxTypeRec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeComboBoxTypeRec();
			}

			
		});
		comboBoxTypeRec.setFont(new Font("Lucida Grande", Font.PLAIN, 9));
		
		String currentWorkingDirectory = System.getProperty("user.dir");	
     	DownloadWordsController dwc = new DownloadWordsController(currentWorkingDirectory + "//GameWordsAndTypes.csv");
     	ArrayList<Word> allgamewords=dwc.getAllGameWords();
     	ArrayList gamewords=new ArrayList();
        int gamecount=0;
     	for(Word w:allgamewords){
     		gamewords.add(allgamewords.get(gamecount).getValue());
     		gamecount++;
     	}
		String rwords[]=new String[gamewords.size()+1];
		rwords[0]="*";
		for(int i=1;i<=gamewords.size();i++){
			rwords[i]=(String) gamewords.get(i-1);
		}
		comboBoxWordRec = new JComboBox(rwords);
		comboBoxWordRec.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		
		
		btnAddSend = new JButton("Add");
		btnAddSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				changebtnAddSend();			
				
			}
		});
		
	    btnAddRec = new JButton("Add");
		btnAddRec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changebtnAddRec();
			}			
		});
		
		JLabel lblSend = new JLabel("Send");		
		JLabel lblReceive = new JLabel("Receive");		
		JScrollPane scrollTableSend = new JScrollPane(table);		
		panelRequest.add(scrollTableSend);
		table = new JTable();		
	    btnSubmit = new JButton("Submit");
		btnSubmit.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				changebtnSubmit();
			}	
		});	
		JButton btnRevoke = new JButton("Clear");
		btnRevoke.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				    clearRequestInfo();					
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();	
		JScrollPane scrollPane_1 = new JScrollPane();
	    StatusDisplay = new JLabel("no swap request");
		GroupLayout gl_panelRequest = new GroupLayout(panelRequest);
		gl_panelRequest.setHorizontalGroup(
			gl_panelRequest.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelRequest.createSequentialGroup()
					.addGap(18)
					.addGroup(gl_panelRequest.createParallelGroup(Alignment.LEADING)
						.addComponent(StatusDisplay)
						.addGroup(gl_panelRequest.createSequentialGroup()
							.addComponent(lblNumberOfWords, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
							.addGap(68)
							.addComponent(comboBoxNumWord, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE))
						.addComponent(lblPleaseChooseThe)
						.addGroup(gl_panelRequest.createSequentialGroup()
							.addGroup(gl_panelRequest.createParallelGroup(Alignment.LEADING)
								.addComponent(lblSend)
								.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 122, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panelRequest.createSequentialGroup()
									.addGap(29)
									.addComponent(btnSubmit)))
							.addGroup(gl_panelRequest.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelRequest.createSequentialGroup()
									.addGap(59)
									.addGroup(gl_panelRequest.createParallelGroup(Alignment.LEADING)
										.addComponent(lblReceive, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
										.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE))
									.addGap(283)
									.addComponent(table, GroupLayout.PREFERRED_SIZE, 1, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panelRequest.createSequentialGroup()
									.addGap(94)
									.addComponent(btnRevoke))))
						.addGroup(gl_panelRequest.createSequentialGroup()
							.addGroup(gl_panelRequest.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(comboBoxTypeRec, 0, 0, Short.MAX_VALUE)
								.addComponent(comboBoxTypeSend, 0, 66, Short.MAX_VALUE))
							.addGap(32)
							.addGroup(gl_panelRequest.createParallelGroup(Alignment.TRAILING)
								.addComponent(comboBoxWordSend, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxWordRec, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE))
							.addGap(69)
							.addGroup(gl_panelRequest.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(btnAddRec, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
								.addComponent(btnAddSend, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 63, Short.MAX_VALUE))))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_panelRequest.setVerticalGroup(
			gl_panelRequest.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelRequest.createSequentialGroup()
					.addGroup(gl_panelRequest.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelRequest.createSequentialGroup()
							.addGap(12)
							.addComponent(lblNumberOfWords)
							.addGap(28)
							.addComponent(lblPleaseChooseThe))
						.addGroup(gl_panelRequest.createSequentialGroup()
							.addGap(11)
							.addComponent(comboBoxNumWord, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGroup(gl_panelRequest.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelRequest.createSequentialGroup()
							.addGap(19)
							.addComponent(comboBoxWordSend, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelRequest.createSequentialGroup()
							.addGap(19)
							.addComponent(comboBoxTypeSend, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelRequest.createSequentialGroup()
							.addGap(18)
							.addComponent(btnAddSend)))
					.addGroup(gl_panelRequest.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelRequest.createSequentialGroup()
							.addGap(19)
							.addGroup(gl_panelRequest.createParallelGroup(Alignment.LEADING)
								.addComponent(comboBoxWordRec, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxTypeRec, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(19)
							.addGroup(gl_panelRequest.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblSend)
								.addComponent(lblReceive)))
						.addGroup(gl_panelRequest.createSequentialGroup()
							.addGap(18)
							.addComponent(btnAddRec)))
					.addGap(18)
					.addGroup(gl_panelRequest.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_panelRequest.createSequentialGroup()
							.addGap(37)
							.addComponent(table, GroupLayout.PREFERRED_SIZE, 1, GroupLayout.PREFERRED_SIZE))
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
						.addComponent(scrollPane_1, 0, 0, Short.MAX_VALUE))
					.addGap(18)
					.addGroup(gl_panelRequest.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSubmit)
						.addComponent(btnRevoke))
					.addGap(54)
					.addComponent(StatusDisplay)
					.addGap(39))
		);
		
		tableRec = new JTable();
		scrollPane_1.setViewportView(tableRec);
		tableRec.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Word", "Type"
			}
		));
		
		tableSend = new JTable();
		scrollPane.setViewportView(tableSend);
		tableSend.setFillsViewportHeight(true);
		tableSend.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Word", "Type"
			}
		));
		 panelRequest.setLayout(gl_panelRequest);
	}
	/**Set the Status for review*/
    public void setStatus(String msg){
        this.StatusDisplay.setText(msg);;
    }
    /**set the Status to be visible*/
    public void setStatusVisible(boolean f){
        this.StatusDisplay.setVisible(f);
    }
    /**Initialize the Sending part so that I can issue a new swap*/
    public void initialSendWords(){
        //initial send words     
        int size=model.getBoard().getUnProtectedRegion().getWords().size();
        offerTypes.removeAll(offerTypes);
        offerWords.removeAll(offerWords);

        String owords[]=new String[model.getBoard().getUnProtectedRegion().getWords().size()+1];
        
        owords[0]="*";
        for(int i=1;i<=size;i++){
            owords[i]=model.getBoard().getUnProtectedRegion().getWords().get(i-1).getValue();
        }
       // ComboBoxModel mo=new ComboBoxModel(owords);
        comboBoxWordSend.setModel(new DefaultComboBoxModel(owords)); 
        comboBoxWordSend.setSelectedIndex(0);
        System.out.print("selected index:"+comboBoxWordSend.getSelectedIndex()+"string"+comboBoxWordSend.getSelectedItem().toString());
        this.comboBoxTypeSend.setSelectedIndex(0);
        comboBoxWordSend.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
  
        tableSend.setModel(new DefaultTableModel(
            new Object[][] {
            },
            new String[] {
                "Word", "Type"
            }
        ));
    }
    /**Initialize the Receiving part so that I can issue a new swap*/
    public void initialReceiveWords(){
        reqTypes.removeAll(reqTypes);
        reqWords.removeAll(reqWords);     
        String rwords[]=filterWordsForCombo("Receive","NoFilter");
        comboBoxWordRec.setModel(new DefaultComboBoxModel(rwords)); 
        comboBoxWordRec.setSelectedIndex(0);
        this.comboBoxTypeRec.setSelectedIndex(0);
        comboBoxWordRec.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
        tableRec.setModel(new DefaultTableModel(
            new Object[][] {
            },
            new String[] {
                "Word", "Type"
            }
        ));
 
    }
    /**get the broker I use*/
	public BrokerManager getBroker() {
		return broker;
	}
	/**set the broker*/
	public void setBroker(BrokerManager b){
		this.broker=b;
	}
	/**get the container I use to pop up error message*/
	public JFrame getFrame(){
		return this;
	}
	/**get the Receiver Type JcomboBox */
	 public JComboBox getcomboBoxTypeRec(){
		   return   comboBoxTypeRec;
	   }
	/**get the Send Type JcomboBox */
	 public JComboBox getcomboBoxTypeSend(){
		   return   comboBoxTypeSend;
	   }
	/**
	 * filter words for narrowing words down to some type, can be
	 * adj/adv/'*'...
	 * @param sendOrRec: if send, the words are based on the unprotected words.
	 * if receive, the words are based on GameWords file
	 * @param typeChoice:adj/adv/.....
	 * @return filtered String array 
	 */
	public String[] filterWords(String sendOrRec,String typeChoice) {
		   ArrayList<Word> words;
		   String narrowDownWords[];
			if(sendOrRec.equals("Send")){
				words=(ArrayList<Word>) model.getBoard().getUnProtectedRegion().getWords().clone();
				if(typeChoice.equals("NoFilter")){
					narrowDownWords=new String[words.size()];
					for(int i=0;i<narrowDownWords.length;i++){
					narrowDownWords[i]=words.get(i).getValue();	
					}
					return narrowDownWords;
				}
			}
			else{
				String currentWorkingDirectory = System.getProperty("user.dir");
			    DownloadWordsController dwc = new DownloadWordsController(currentWorkingDirectory + "//GameWordsAndTypes.csv");
				words=(ArrayList<Word>) dwc.getAllGameWords().clone();
				if(typeChoice.equals("NoFilter")){
					narrowDownWords=new String[words.size()];
					for(int i=0;i<narrowDownWords.length;i++){
					narrowDownWords[i]=words.get(i).getValue();	
					}
					return narrowDownWords;
				}
			}
	
			ArrayList<Word> thisTypeWords=new ArrayList<Word>();
			int size=words.size();
			for(int i=0;i<size;i++){
				if((words.get(i).getType().toString()).equals(typeChoice))
					thisTypeWords.add(words.get(i));
			}
			int thisTypeWordsSize=thisTypeWords.size();
			narrowDownWords=new String[thisTypeWordsSize];
			for(int i=0;i<thisTypeWordsSize;i++){
				 narrowDownWords[i]=thisTypeWords.get(i).getValue();
			}
			
			return narrowDownWords;
		}
	   public String[] filterWordsForCombo(String sendOrRec,String choice) {
			String[] WordsData = null;
			if(sendOrRec.equals("Receive")&&choice.equals("NoFilter"))
			{
				String[] wordsFiltered=filterWords("Receive",choice);
				WordsData=new String[wordsFiltered.length+1];
				WordsData[0]="*";
				for(int i=1;i<WordsData.length;i++){
					WordsData[i]=wordsFiltered[i-1];
				}
				return WordsData;
			}
			if(sendOrRec.equals("Send")&&choice.equals("NoFilter"))
			{
				String[] wordsFiltered=filterWords("Send",choice);
				WordsData=new String[wordsFiltered.length+1];
				WordsData[0]="*";
				for(int i=1;i<WordsData.length;i++){
					WordsData[i]=wordsFiltered[i-1];
				}
				return WordsData;
			}
			switch(choice)
			{
			case "adj":{
				String[] wordsFiltered=filterWords(sendOrRec,choice);	
				WordsData=new String[wordsFiltered.length+1];
				WordsData[0]="*";
				for(int i=1;i<WordsData.length;i++){
					WordsData[i]=wordsFiltered[i-1];
				}
				break;
			}
			case "adv":{
				String[] wordsFiltered=filterWords(sendOrRec,choice);	
				WordsData=new String[wordsFiltered.length+1];
				WordsData[0]="*";
				for(int i=1;i<WordsData.length;i++){
					WordsData[i]=wordsFiltered[i-1];
				}
				break;
			}
			case "verb":{
				String[] wordsFiltered=filterWords(sendOrRec,choice);	
				WordsData=new String[wordsFiltered.length+1];
				WordsData[0]="*";
				for(int i=1;i<WordsData.length;i++){
					WordsData[i]=wordsFiltered[i-1];
				}
				break;
			}
			case "pron":{
				String[] wordsFiltered=filterWords(sendOrRec,choice);	
				WordsData=new String[wordsFiltered.length+1];
				WordsData[0]="*";
				for(int i=1;i<WordsData.length;i++){
					WordsData[i]=wordsFiltered[i-1];
				}
				break;
			}
			case "conj":{
				String[] wordsFiltered=filterWords(sendOrRec,choice);	
				WordsData=new String[wordsFiltered.length+1];
				WordsData[0]="*";
				for(int i=1;i<WordsData.length;i++){
					WordsData[i]=wordsFiltered[i-1];
				}
				break;
			}
			case "noun":{
				String[] wordsFiltered=filterWords(sendOrRec,choice);	
				WordsData=new String[wordsFiltered.length+1];
				WordsData[0]="*";
				for(int i=1;i<WordsData.length;i++){
					WordsData[i]=wordsFiltered[i-1];
				}
				break;
			}
			case "prefix":{
				String[] wordsFiltered=filterWords(sendOrRec,choice);	
				WordsData=new String[wordsFiltered.length+1];
				WordsData[0]="*";
				for(int i=1;i<WordsData.length;i++){
					WordsData[i]=wordsFiltered[i-1];
				}
				break;
			}
			case "suffix":{
				String[] wordsFiltered=filterWords(sendOrRec,choice);	
				WordsData=new String[wordsFiltered.length+1];
				WordsData[0]="*";
				for(int i=1;i<WordsData.length;i++){
					WordsData[i]=wordsFiltered[i-1];
				}
				break;
			}
			case "unknown":{
				String[] wordsFiltered=filterWords(sendOrRec,choice);	
				WordsData=new String[wordsFiltered.length+1];
				WordsData[0]="*";
				for(int i=1;i<WordsData.length;i++){
					WordsData[i]=wordsFiltered[i-1];
				}
				break;
			}
			case "prep":{
				String[] wordsFiltered=filterWords(sendOrRec,choice);	
				WordsData=new String[wordsFiltered.length+1];
				WordsData[0]="*";
				for(int i=1;i<WordsData.length;i++){
					WordsData[i]=wordsFiltered[i-1];
				}
				break;
			}
			case "determiner":{
				String[] wordsFiltered=filterWords(sendOrRec,choice);	
				WordsData=new String[wordsFiltered.length+1];
				WordsData[0]="*";
				for(int i=1;i<WordsData.length;i++){
					WordsData[i]=wordsFiltered[i-1];
				}
				break;
			}
			case "number":{
				String[] wordsFiltered=filterWords(sendOrRec,choice);	
				WordsData=new String[wordsFiltered.length+1];
				WordsData[0]="*";
				for(int i=1;i<WordsData.length;i++){
					WordsData[i]=wordsFiltered[i-1];
				}
				break;
			}
		}
			return WordsData;
		}
/**Clear the last swap and get ready for the next*/
	public void clearRequestInfo() {
		comboBoxNumWord.setSelectedIndex(0);
		initialSendWords();
		initialReceiveWords();
		setVisible(true);
		setStatusVisible(false);
		getBroker().setStatus("no swap request");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	/**modify the value in the ComboBoxNumWor*/
	public void changeComboBoxNumWord() {
		wordCount= Integer.parseInt((String) comboBoxNumWord.getSelectedItem());
		comboBoxNumWord.setSelectedItem(wordCount);
	}
	/**modify the value in the ComboBoxTypeSend*/
	public  void changeComboBoxTypeSend() {
		String choice=comboBoxTypeSend.getSelectedItem().toString();
		if(choice.equals("*"))
			{
			String[] WordsData = filterWordsForCombo("Send","NoFilter");
			ComboBoxModel model = new DefaultComboBoxModel(WordsData);
			comboBoxWordSend.setModel(model);
			comboBoxTypeSend.setSelectedIndex(0);
			}
		else{
		String[] WordsData = filterWordsForCombo("Send",choice);
		ComboBoxModel model = new DefaultComboBoxModel(WordsData);
		comboBoxWordSend.setModel(model);
		}
	}
	/**modify the value in the ComboBoxTypeRec*/
	public void changeComboBoxTypeRec() {
		String choice=comboBoxTypeRec.getSelectedItem().toString();
		if(choice.equals("*"))
		{
			String[] WordsData = filterWordsForCombo("Receive","NoFilter");
			ComboBoxModel model = new DefaultComboBoxModel(WordsData);
			comboBoxWordRec.setModel(model);
			comboBoxTypeRec.setSelectedIndex(0);
		}
		else
		{String[] WordsData = filterWordsForCombo("Receive",choice);
		ComboBoxModel model = new DefaultComboBoxModel(WordsData);
		comboBoxWordRec.setModel(model);
		}
	}
	/**
	 * if the swap is valid, the controller will send it to broker to process
	 * else, pop up the error message and reset the swap.
	 */
	public void changebtnSubmit() {
		
		if(wordCount==offerTypes.size()&&wordCount==reqTypes.size()){
			src=new RequestSwapController(model, broker);
			src.setMsg(wordCount, offerTypes, offerWords, reqTypes, reqWords);
			src.process();
			getFrame().setVisible(false);;	
		}
		else{
			JOptionPane.showMessageDialog(getFrame(), "Request Error: Number of words is not matched! Requests reset.");	
			clearRequestInfo();
		}
	}
	/**set the words and type of words I offer
	 * format:word not "*"/type "*" is not allowed*/
	public  void changebtnAddSend() {
		if(((String) comboBoxTypeSend.getSelectedItem()).equals("*")&&((String) comboBoxWordSend.getSelectedItem()).equals("*")==false)
		{
			JOptionPane.showMessageDialog(getFrame(), "Word cannot be specified without type *");	
			comboBoxWordSend.setSelectedIndex(0);
		}
		else
		{
			offerTypes.add((String) comboBoxTypeSend.getSelectedItem());
			offerWords.add((String) comboBoxWordSend.getSelectedItem());
			DefaultTableModel model = (DefaultTableModel) tableSend.getModel();
			model.addRow(new Object[]{(String)  (String) comboBoxWordSend.getSelectedItem(),comboBoxTypeSend.getSelectedItem()});
				
		}
	}
	/**set the words and type of words I request
	 * format:word not "*"/type "*" is not allowed*/
	public void changebtnAddRec() {
		if(((String) comboBoxTypeRec.getSelectedItem()).equals("*")&&((String) comboBoxWordRec.getSelectedItem()).equals("*")==false)
		{
			JOptionPane.showMessageDialog(getFrame(), "Word cannot be specified without type *");	
			comboBoxWordRec.setSelectedIndex(0);
		}
		else{
		reqTypes.add((String) comboBoxTypeRec.getSelectedItem());
		reqWords.add((String) comboBoxWordRec.getSelectedItem());
		DefaultTableModel model = (DefaultTableModel) tableRec.getModel();
		model.addRow(new Object[]{(String) comboBoxWordRec.getSelectedItem(),(String) comboBoxTypeRec.getSelectedItem()});
		}
	}
}
