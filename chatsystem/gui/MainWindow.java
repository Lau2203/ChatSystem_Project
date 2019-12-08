package chatsystem.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.Box;
import javax.swing.BorderFactory;
import javax.swing.border.LineBorder;
import javax.swing.border.Border;

import java.awt.Component;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.BorderLayout;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.awt.event.KeyListener;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.FocusEvent;

import chatsystem.gui.UserBox;

import chatsystem.Client;
import chatsystem.User;
import chatsystem.Message;
import chatsystem.MessageHistoryManager;

@SuppressWarnings("serial")

public class MainWindow extends JFrame{

	private Client master;
	private MessageHistoryManager messageHistoryManager;

	public static final int WIDTH 	= 900;
	public static final int HEIGHT 	= 650;

  	Color myBlue = new Color(24, 147, 248);

	public static Color backgroundColor = Color.white;
	public static Color foregroundColor = Color.black;

	private JSplitPane s1;
	private JSplitPane s2;

	// User Box - SOUTH.LEFT
   	private JPanel panUsers 	= new JPanel();
   	private JPanel panMsg 		= new JPanel();

	private Box search;
	private boolean isSearchBarEmpty = true;
	private boolean isMsgBarEmpty = true;

	private JLabel textName;

	/* Resources */
	public static ImageIcon icon 		= new ImageIcon("../resources/images/user.png");

	public static Icon mouseEnteredM 	= new ImageIcon("../resources/images/MsgB2.png");
	public static Icon mouseExitedM 	= new ImageIcon("../resources/images/MsgA2.png");
	public static Icon mouseEnteredO 	= new ImageIcon("../resources/images/OptB.png");
	public static Icon mouseExitedO		= new ImageIcon("../resources/images/OptA.png");
	public static Icon mouseEnteredL 	= new ImageIcon("../resources/images/15.png");
	public static Icon mouseExitedL		= new ImageIcon("../resources/images/14.png");
	public static Icon mouseEnteredS	= new ImageIcon("../resources/images/16.png");
	public static Icon mouseExitedS		= new ImageIcon("../resources/images/17.png");
	// User Image
	public static ImageIcon user 		= new ImageIcon("../resources/images/activet.png");

	// Cursors
	public static Cursor handCursor 	= new Cursor(Cursor.HAND_CURSOR);
   	public static Cursor defaultCursor 	= new Cursor(Cursor.DEFAULT_CURSOR);

	public MainWindow(Client master, MessageHistoryManager mhm)  {  

		super();   
	
		this.master = master;
		this.messageHistoryManager = mhm;
      
	  	this.setTitle("Aura - Chat System");
		this.setSize(MainWindow.WIDTH, MainWindow.HEIGHT); 				
		this.setLocationRelativeTo(null); 
		this.getContentPane().setBackground(MainWindow.backgroundColor);
		this.setResizable(false); 			
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		
	
		//About US - NORTH.LEFT
		JPanel panUs 		= new JPanel();
		panUs.setBackground(Color.BLUE); 
		Box BoxAboutUsMain = Box.createVerticalBox();
		panUs.add(BoxAboutUsMain, BorderLayout.NORTH);

		Box BoxAboutUs = Box.createVerticalBox();
		BoxAboutUsMain.add(BoxAboutUs, BorderLayout.NORTH);

		panUs.setBackground(MainWindow.backgroundColor);
		JLabel padding1 	= new JLabel("\n");
		BoxAboutUs.add(padding1);
		JLabel image 		= new JLabel(MainWindow.icon); 
		image.setAlignmentX(Component.CENTER_ALIGNMENT);
		BoxAboutUs.add(image);
		JLabel padding2		= new JLabel("\n");
		BoxAboutUs.add(padding2);
		this.textName 	= new JLabel(this.master.getMainUserUsername());
		this.textName.setFont(new java.awt.Font("CALIBRI",Font.BOLD,18));
		this.textName.setAlignmentX(Component.CENTER_ALIGNMENT);
		BoxAboutUs.add(this.textName);
		JLabel padding3 	= new JLabel("\n");
		BoxAboutUs.add(padding3);

		Box BoxAboutUs2 = Box.createHorizontalBox();
		BoxAboutUsMain.add(BoxAboutUs2, BorderLayout.SOUTH);
		
		JButton buttonO 	= new JButton(MainWindow.mouseExitedO);
		JButton buttonM 	= new JButton(MainWindow.mouseExitedM);
		buttonM.setBorder(BorderFactory.createLineBorder(MainWindow.backgroundColor));
		buttonO.setBorder(BorderFactory.createLineBorder(MainWindow.backgroundColor));
		
		buttonM.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {}
			@Override
     			public void mousePressed(MouseEvent e) {
				//buttonM.setIcon(MainWindow.mouseEnteredM);
			}
			@Override
        		public void mouseReleased(MouseEvent e) {
				//buttonM.setIcon(MainWindow.mouseExitedM);
			}
			@Override
        		public void mouseEntered(MouseEvent e){
				setCursor(MainWindow.handCursor);
				buttonM.setIcon(MainWindow.mouseEnteredM);
				buttonM.setBorder(BorderFactory.createLineBorder(myBlue));
       			}
			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(MainWindow.defaultCursor);
				buttonM.setIcon(MainWindow.mouseExitedM);
				buttonM.setBorder(BorderFactory.createLineBorder(MainWindow.backgroundColor));
			}
		});

		buttonO.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {}
			@Override
     			public void mousePressed(MouseEvent e) {
				//buttonO.setIcon(MainWindow.mouseEnteredO);
			}
			@Override
        		public void mouseReleased(MouseEvent e) {
				//buttonO.setIcon(MainWindow.mouseExitedO);
			}
			@Override
        		public void mouseEntered(MouseEvent e){
				setCursor(MainWindow.handCursor);
				buttonO.setIcon(MainWindow.mouseEnteredO);
				buttonO.setBorder(BorderFactory.createLineBorder(myBlue));
       			}
			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(MainWindow.defaultCursor);
				buttonO.setIcon(MainWindow.mouseExitedO);
				buttonO.setBorder(BorderFactory.createLineBorder(MainWindow.backgroundColor));
			}
		});

		BoxAboutUs2.add(buttonO);
		BoxAboutUs2.add(buttonM);
		//END of About US - NORTH.LEFT 
		

		
    		this.panUsers.setBackground(MainWindow.backgroundColor);
        	

 		
		//Search Bar
		this.search = Box.createVerticalBox();
		search.setBackground(MainWindow.backgroundColor);
		

		JTextField textField 		= new JTextField(" Search something ? ...               "); //padding to be done
		textField.setFont(new Font("CALIBRI", Font.PLAIN, 15));
		textField.getFont().deriveFont(Font.ITALIC);
		textField.setForeground(Color.gray);
		textField.setBackground(MainWindow.backgroundColor);
		
		Border borderG = BorderFactory.createLineBorder(Color.gray, 2);
		Border borderB = BorderFactory.createLineBorder(myBlue, 2);
		textField.setBorder(borderG);
		search.add(textField);
		

		textField.addMouseListener(new MouseListener() {           
			@Override
		   	public void mouseReleased(MouseEvent e) {}         
		   	@Override
		   	public void mousePressed(MouseEvent e) {}          
		    	@Override
		    	public void mouseExited(MouseEvent e) {
				textField.setBorder(borderG);
			}           
		    	@Override
		    	public void mouseEntered(MouseEvent e) {
				textField.setBorder(borderB);
			}          
		    	@Override
		    	public void mouseClicked(MouseEvent e) {}
		});

		textField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (isSearchBarEmpty) {
					textField.setText("");
					textField.setForeground(MainWindow.foregroundColor);
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if (textField.getText().equals("")) {
					isSearchBarEmpty = true;
					textField.setText(" Search something ? ...               ");
					textField.setForeground(Color.gray);
				} else {
					isSearchBarEmpty = false;
				}
			}
		});

		this.panUsers.add(this.search);		

		/************************************TO BE DONE IN USER BOX CLASS***************************************/
		/*********************************************************************************************************/
		/******************* Create seven UB ****************************/
		this.displayUsersPanel();		
		
		/*********************************************************************************************************/
		/*********************************************************************************************************/

		/*********************************************************************************************************/
		/**************************************Messages***********************************************************/

    		this.panMsg.setBackground(MainWindow.backgroundColor); 

		//Main Box
		Box mainBoxM = Box.createVerticalBox();
		mainBoxM.setBackground(MainWindow.backgroundColor);
		this.panMsg.add(mainBoxM);
		
		
		//NameBox
		Box nameBox = Box.createHorizontalBox();
		mainBoxM.add(nameBox);
	
		JLabel userNameC	= new JLabel("Paula Roïd ");
		userNameC.setFont(new java.awt.Font("CALIBRI",Font.BOLD,18));
		JLabel lastMsgS		= new JLabel(" Last message 2 minutes ago");
		lastMsgS.setFont(new java.awt.Font("CALIBRI",Font.PLAIN,14));
		userNameC.setForeground(myBlue);
		lastMsgS.setForeground(Color.gray);
		nameBox.add(userNameC);
		nameBox.add(lastMsgS);
		
		
		
		/*********************************************Conversation*********************************************/
		JPanel msgBoxPanel = new JPanel();
		//mainBoxM.add(msgBoxPanel);
		msgBoxPanel.setPreferredSize(new Dimension(275, 547)); //OK
		mainBoxM.add(msgBoxPanel);
		msgBoxPanel.setBackground(MainWindow.backgroundColor);

		//Messages Box
		Box msgBox = Box.createHorizontalBox();
		msgBox.setOpaque(true);
		msgBox.setBackground(MainWindow.backgroundColor);
		msgBox.add((Box.createRigidArea(new Dimension(60, 0))));
		msgBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		JScrollPane scrollMsg 	= new JScrollPane(msgBox, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,  JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollMsg.setPreferredSize(new Dimension(585,532)); //NE MARCHE PAS
		scrollMsg.setBorder(BorderFactory.createEmptyBorder());
		
		
		msgBoxPanel.add(scrollMsg);
		
		Box leftBox = Box.createVerticalBox();
		leftBox.setBackground(MainWindow.backgroundColor);
		leftBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		msgBox.add(leftBox, BorderLayout.WEST);

		//RIGID AREA BETWEEN LEFT AND RIHGT
		Box paddingBox = Box.createHorizontalBox();
		paddingBox.add((Box.createRigidArea(new Dimension(5, 0))));
		paddingBox.setForeground(MainWindow.backgroundColor);
		paddingBox.setOpaque(true);
		msgBox.add(paddingBox);
		
		Box rightBox = Box.createVerticalBox();
		rightBox.setBackground(MainWindow.backgroundColor);
		rightBox.setAlignmentX(Component.RIGHT_ALIGNMENT);
		msgBox.add(rightBox, BorderLayout.EAST);
		
		for (int i=0 ; i<40; i++){
			//For each new message sent -- rightBoxUs
			Box boxS 	= Box.createVerticalBox();
			boxS.setBackground(MainWindow.backgroundColor);
			rightBox.add(boxS);
			JTextArea myMsg 	= new JTextArea("Good Morning, is Sarah Crôche in your office ?", 2, 20);
			myMsg.setForeground(MainWindow.foregroundColor);
			myMsg.setBackground(MainWindow.backgroundColor);
			myMsg.setWrapStyleWord(true);
			myMsg.setLineWrap(true);
			myMsg.setEditable(false);
			//boxS.setAlignmentX(Component.CENTER_ALIGNMENT);
			//myMsg.setOpaque(false)
			boxS.add(myMsg);

			//For each new message received -- leftBox
			//White Message
			Box boxLabelMsgReceived 	= Box.createVerticalBox();
			boxLabelMsgReceived.setBackground(MainWindow.backgroundColor);
			leftBox.add(boxLabelMsgReceived);
			boxLabelMsgReceived.setBorder(BorderFactory.createLineBorder(MainWindow.backgroundColor));
			JTextArea myMsgWhite = new JTextArea("MY MESSAGE WHITE", 2, 20);
			myMsgWhite.setForeground(MainWindow.backgroundColor);
			myMsgWhite.setBackground(MainWindow.backgroundColor);
			myMsgWhite.setWrapStyleWord(true);
			myMsgWhite.setLineWrap(true);
			myMsgWhite.setEditable(false);
			//boxLabelMsgReceived.setAlignmentX(Component.CENTER_ALIGNMENT);
			//myMsg.setOpaque(false)
			boxLabelMsgReceived.add(myMsgWhite);

			Box boxR 	= Box.createVerticalBox();
			boxR.setBackground(MainWindow.backgroundColor);
			leftBox.add(boxR);
			JTextArea theirMsg 	= new JTextArea("I'm sorry but she is in a meeting now, if you give me your number, I'll ask her to call you back", 2, 20);
			theirMsg.setWrapStyleWord(true);
			theirMsg.setLineWrap(true);
			theirMsg.setForeground(myBlue);
			theirMsg.setBackground(MainWindow.backgroundColor);
			theirMsg.setEditable(false);
			//boxR.setAlignmentX(Component.LEFT_ALIGNMENT);
			//theirMsg.setOpaque(false);
			boxR.add(theirMsg);
		}

		
		/***********************************End of Code about Conversation**************************************/
		//Write Text Box 
		Box textBox = Box.createHorizontalBox();
		textBox.setBackground(MainWindow.backgroundColor);

		JTextArea writeMsg		= new JTextArea("Write your message...", 0, 43); //padding to be done
		textField.setFont(new Font("CALIBRI", Font.PLAIN, 13));
		textField.getFont().deriveFont(Font.ITALIC);
		textField.setForeground(Color.gray);
		textField.setBackground(MainWindow.backgroundColor);
		JScrollPane scrollMsg2 	= new JScrollPane(writeMsg, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,  JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
		
		Border down = BorderFactory.createMatteBorder(0, 0, 1, 0, MainWindow.foregroundColor);
		//Border borderG2 = BorderFactory.createLineBorder(Color.gray, 1);
		Border borderW = BorderFactory.createMatteBorder(1, 1, 0, 1, MainWindow.backgroundColor);
		Border borderTextField = BorderFactory.createCompoundBorder(down, borderW);
		
		scrollMsg2.setBorder(borderTextField);
		writeMsg.setBorder(borderW);	

		writeMsg.addMouseListener(new MouseListener() {           
			@Override
		   	public void mouseReleased(MouseEvent e) {}         
		   	@Override
		   	public void mousePressed(MouseEvent e) {}          
		    	@Override
		    	public void mouseExited(MouseEvent e) {
				textField.setBorder(borderG);
			}           
		    	@Override
		    	public void mouseEntered(MouseEvent e) {
				textField.setBorder(borderB);
			}          
		    	@Override
		    	public void mouseClicked(MouseEvent e) {
					//JTextArea writeMsg = ((JTextArea)e.getSource());
					//writeMsg.setText("");
					//writeMsg.getFont().deriveFont(Font.PLAIN);
					//writeMsg.setFont(new Font("CALIBRI", Font.PLAIN, 13));
					//writeMsg.setForeground(MainWindow.foregroundColor);
		    }
		});	

		writeMsg.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (isMsgBarEmpty) {
					writeMsg.setText("");
					writeMsg.setForeground(MainWindow.foregroundColor);
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if (writeMsg.getText().equals("")) {
					isMsgBarEmpty = true;
					writeMsg.setText("Write your message...");
					writeMsg.setForeground(Color.gray);
				} else {
					isMsgBarEmpty = false;
				}
			}
		});

		writeMsg.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					e.consume();
					sendMessage();
				}
			}
			public void keyTyped(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}
		});

		//Do not split words
		writeMsg.setWrapStyleWord(true);
		writeMsg.setLineWrap(true);
		textBox.add(scrollMsg2);
		//Set Icons and Button
		JButton linkButton 	= new JButton(MainWindow.mouseExitedL);
		JButton sendButton 	= new JButton(MainWindow.mouseExitedS);
		linkButton.setBorder(BorderFactory.createLineBorder(MainWindow.backgroundColor));
		sendButton.setBorder(BorderFactory.createLineBorder(MainWindow.backgroundColor));
		textBox.add(linkButton);
		textBox.add(sendButton);
		linkButton.addMouseListener(new MouseListener() {
				public void mouseClicked(MouseEvent e) {}
				@Override
	     			public void mousePressed(MouseEvent e) {
				}
				@Override
				public void mouseReleased(MouseEvent e) {
				}
				@Override
				public void mouseEntered(MouseEvent e){
					linkButton.setIcon(MainWindow.mouseEnteredL);
	       			}
				@Override
				public void mouseExited(MouseEvent e) {
					linkButton.setIcon(MainWindow.mouseExitedL);
				}
			});
		sendButton.addMouseListener(new MouseListener() {
				public void mouseClicked(MouseEvent e) {
					sendMessage();
				}
				@Override
	     			public void mousePressed(MouseEvent e) {
				}
				@Override
				public void mouseReleased(MouseEvent e) {
				}
				@Override
				public void mouseEntered(MouseEvent e){
					sendButton.setIcon(MainWindow.mouseEnteredS);
	       			}
				@Override
				public void mouseExited(MouseEvent e) {
					sendButton.setIcon(MainWindow.mouseExitedS);
				}
			});

		mainBoxM.add(textBox, BorderLayout.SOUTH);
		


		/************************************END OF MESSAGES PART*************************************************/
		/*********************************************************************************************************/
		//SPLITS
    		s1 	= new JSplitPane(JSplitPane.VERTICAL_SPLIT, panUs, this.panUsers);
		s1.setDividerSize(0);
		s1.setDividerLocation(185);
 		s1.setEnabled(false);

		s2 	= new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, s1, panMsg);
    		s2.setDividerLocation(280);
		s2.setDividerSize(7);
		s2.setEnabled(false);

		
    		this.getContentPane().add(s2, BorderLayout.CENTER);
	}

	private void displayUsersPanel() {
		/* Clean the whole panel */
		this.panUsers.removeAll();
		/* Do not forget to add the search bar back */
		this.panUsers.add(this.search);
		/* Retrieve all the current active users */
		for (User usr : this.master.getUserList()) {	
			
			Box mainBox = Box.createHorizontalBox();
			mainBox.setBackground(MainWindow.backgroundColor);

			Box rightBox = Box.createVerticalBox();
			Border emptyBorder = BorderFactory.createEmptyBorder();
			rightBox.setBorder(emptyBorder);

			/* Retrieve the user's username */
			JLabel userName 	= new JLabel(usr.getUsername());
			userName.setFont(new java.awt.Font("CALIBRI",Font.BOLD,15));
			rightBox.add(userName);

			JLabel lastMsg 		= new JLabel("I spoke to our IT manager abo..."); //get 29 letters + [...]
			lastMsg.setFont(new java.awt.Font("CALIBRI",Font.PLAIN,12));
			lastMsg.setForeground(Color.gray);	
			rightBox.add(lastMsg);

			JLabel paddingUB2 	= new JLabel("  ");

			JLabel userL 		= new JLabel(MainWindow.user); 


			mainBox.add(userL);
			mainBox.add(paddingUB2);
			mainBox.add(rightBox);

			JButton buttonUser1 	= new JButton();
			buttonUser1.setBackground(MainWindow.backgroundColor);
			buttonUser1.setBorderPainted(false);
			buttonUser1.add(mainBox);

			buttonUser1.addMouseListener(new MouseListener() {
					public void mouseClicked(MouseEvent e) {
						updateConversationPanel(usr);
					}
					@Override
					public void mousePressed(MouseEvent e) {
					}
					@Override
					public void mouseReleased(MouseEvent e) {
					}
					@Override
					public void mouseEntered(MouseEvent e){
					setCursor(MainWindow.handCursor);
					userName.setForeground(myBlue);
					}
					@Override
					public void mouseExited(MouseEvent e) {
					setCursor(MainWindow.defaultCursor);
					userName.setForeground(MainWindow.foregroundColor);
					}
					});

			this.panUsers.add(buttonUser1,BorderLayout.WEST);
		}

		/* Draw the panel again */
		this.panUsers.revalidate();
		this.panUsers.repaint();
	}

	private void updateConversationPanel(User user) {
		System.out.println("NEED TO UPDATE CONVERSATION PANEL !");
	}

	/* Whether a user just got online or offline, we need to update the users panel */
	public synchronized void notifyUserActivityModification() {
		this.displayUsersPanel();
	}
	
	public synchronized void notifyNewMainUserUsername(String username) {
		this.textName.setText(username);
	}

	public synchronized void notifyNewUserUsername() {
		/* It will fetch the new username while redrawing the whole panel */
		this.displayUsersPanel();
		/* TODO */
		/* If we are focused on the conversation of the user which has changed its 
		 * username we need to update the title of the conversation with its new 
		 * username */
	}

	public synchronized void notifyNewMessage(User recipient, Message msg) {
		
	}

	private void sendMessage() {
		System.out.println("NEED TO SEND MESSAGE RIGHT NOW !");		
	}

	public static void main(String[] args) {
		//MainWindow mw = new MainWindow();
		//mw.setVisible(true);
	}


}
