package himalia.view;

import himalia.BrokerManager;
import himalia.controller.*;
import himalia.model.*;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.border.LineBorder;

import java.awt.Color;
import java.awt.Rectangle;

/**
 * GUI
 * @author Yan Wang
 *
 */
public class CyberPoetrySlamGUI extends JFrame {

	/**
	 * Make eclipse happy.
	 */
	private static final long serialVersionUID = -5712039466043587885L;
	private JPanel contentPanel;
	/**Table to sort and explore words*/
	WordTable wordTable;
	/**Panel to draw*/
	RegionPanel panelRegion;
	/**Form to send request*/
	RequestGUI requestGui;
	
	BrokerManager broker;
	
	JButton btnIssueRequest;

	Model model;
	private JButton btnProtectAWord;

	/**
	 * Create the frame.
	 */
	public CyberPoetrySlamGUI(Model m) {
		setResizable(false);
		model = m;
		setTitle("CyberPoetrySlam");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 746, 800);
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPanel);
		

		this.panelRegion = new RegionPanel(model);
		panelRegion.setSize(new Dimension(500, 600));
		panelRegion.setBounds(new Rectangle(0, 0, 500, 500));
		panelRegion.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelRegion.setToolTipText("");

		btnIssueRequest = new JButton("Issue Swap Requset");
		//btnIssueRequest.hide();
		
		btnIssueRequest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//RequestGUI request = new RequestGUI(model);
				requestGui.clearRequestInfo();
				requestGui.comboBoxNumWord.enable(true);
				requestGui.getcomboBoxTypeRec().enable(true);
				requestGui.getcomboBoxTypeSend().enable(true);
				requestGui.comboBoxWordSend.enable(true);
				requestGui.comboBoxWordRec.enable(true);
				requestGui.btnAddSend.setEnabled(true);
				requestGui.btnAddRec.setEnabled(true);
				requestGui.btnSubmit.setEnabled(true);
				requestGui.setVisible(true);
				requestGui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                requestGui.setStatusVisible(false);
				//new RequestSwapController(model, request).process();
			}
		});
		
		JButton btnRelease = new JButton("Release Poem");
		
		btnRelease.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ReleasePoemController(model, panelRegion).register();
			}
		});
		
		JButton btnPublish = new JButton("Publish Poem");
		
		btnPublish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new PublishPoemController(model, panelRegion).register();

			}
		});

		
		JButton btnUndo = new JButton("Undo");
		btnUndo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new UndoController(model, panelRegion).process();
			}
			
		});
		


		
		JButton btnRedo = new JButton("Redo");
		btnRedo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new RedoController(model, panelRegion).process();
			}
			
		});
		

		
		btnProtectAWord = new JButton("Protect Word");
		btnProtectAWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ProtectWordController(model, panelRegion).register();
			}
		});
		
		JButton btnUnprotectAWord = new JButton("Release Word");
		btnUnprotectAWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new ReleaseWordController(model, panelRegion).register();;
			}
		});
		
		JButton btnMovePWord = new JButton("Move Word");
		btnMovePWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new MoveWordController( model,panelRegion).register();
				
			}
		});
		
		JButton btnConnectEdgeWord = new JButton("Connect Word");
		btnConnectEdgeWord.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent arg0) {
	                new ConnectWordController(panelRegion, model).register();
	            }
	        });
		
		JButton btnDisconnect = new JButton("Disconnect Word");
		btnDisconnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new DisconnectWordController(panelRegion, model).register();
            }
        });

		
		JButton btnConnectPoem = new JButton("Connect Poem");
		btnConnectPoem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new ConnectPoemController(model,panelRegion).register();
			}
		});
		
		JButton btnDisconnectPoem = new JButton("Disconnect Poem");
		btnDisconnectPoem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new DisconnectPoemController(model,panelRegion).register();
			}
		});
		
		JButton btnNewButton = new JButton("Move Poem");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new MovePoemController(model,panelRegion).register();
				//updateUprotectedTable();
				
			}
		});
		
		JButton btnShiftRow = new JButton("Shift Row");
		btnShiftRow.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent arg0) {
	                new ShiftRowController(model,panelRegion).register();
	                //updateUprotectedTable();
	                
	            }
	        });
		
		wordTable = new WordTable(model,panelRegion);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new SaveController(model).process();
			}
		});
		
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(btnIssueRequest, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
								.addComponent(btnRedo, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
								.addComponent(btnUndo, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
								.addComponent(btnSave, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
								.addComponent(btnShiftRow, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
								.addComponent(btnDisconnectPoem, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
								.addComponent(btnConnectPoem, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
								.addComponent(btnProtectAWord, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
								.addComponent(btnUnprotectAWord, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
								.addComponent(btnMovePWord, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
								.addComponent(btnConnectEdgeWord, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
								.addComponent(btnDisconnect, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
								.addComponent(btnPublish, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
								.addComponent(btnRelease, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
								.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panelRegion, GroupLayout.PREFERRED_SIZE, 500, GroupLayout.PREFERRED_SIZE))
						.addComponent(wordTable, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(btnIssueRequest)
							.addGap(17)
							.addComponent(btnSave)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnUndo)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnRedo)
							.addGap(18)
							.addComponent(btnProtectAWord)
							.addGap(18)
							.addComponent(btnUnprotectAWord)
							.addGap(18)
							.addComponent(btnMovePWord)
							.addGap(18)
							.addComponent(btnConnectEdgeWord)
							.addGap(18)
							.addComponent(btnDisconnect)
							.addGap(18)
							.addComponent(btnPublish)
							.addGap(18)
							.addComponent(btnRelease)
							.addGap(18)
							.addComponent(btnNewButton)
							.addPreferredGap(ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
							.addComponent(btnShiftRow)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnConnectPoem)
							.addGap(13)
							.addComponent(btnDisconnectPoem)
							.addGap(49))
						.addComponent(panelRegion, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(wordTable, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
					.addGap(23))
		);
		

		contentPanel.setLayout(gl_contentPanel);

	}

	
	public RegionPanel getPanel(){
		return panelRegion;
	}
	public void setBroker(BrokerManager bm) { 
        this.broker = bm;
        this.setTitle("ID:" + bm.getID());
        btnIssueRequest.setEnabled(true);
    }
    public BrokerManager getBroker() { return broker; }
    public void setRequestGui(RequestGUI rgui){
    	requestGui=rgui;
    }
    public WordTable getWordTable(){
    	return wordTable;
    }
}
