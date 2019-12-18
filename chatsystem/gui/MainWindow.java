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
import java.awt.Font;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BoxLayout;

import java.awt.event.MouseListener;
import java.awt.event.KeyListener;
import java.awt.event.FocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;

import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.FocusEvent;
import java.awt.event.WindowEvent;

import java.util.ArrayList;
import java.lang.Math;

import chatsystem.gui.UserBox;

import chatsystem.Client;
import chatsystem.User;
import chatsystem.Message;
import chatsystem.MessageHistory;

@SuppressWarnings("serial")

public class MainWindow extends JFrame{


	private Client master;
	private User currentRecipient;

	public static final int WIDTH 	= 900;
	public static final int HEIGHT 	= 650;

  	Color myBlue = new Color(24, 147, 248);
	Color myGray = new Color(220,220,220);

	public static Color backgroundColor = Color.white;
	public static Color foregroundColor = Color.black;

	private JSplitPane s1;
	private JSplitPane s2;

	// User Box - SOUTH.LEFT
   	private JPanel panUsers 	= new JPanel();
   	private JPanel panMsg 		= new JPanel();
	private JPanel conversationPanel = new JPanel();
	
	private Box textBox = Box.createHorizontalBox();
	private Box nameBox = Box.createHorizontalBox();
	
	private Box search;
	private boolean isSearchBarEmpty = true;
	private boolean isMsgBarEmpty = true;

	private JLabel textName;
	private JLabel userNameC;

	private JTextArea writeMsg 	= new JTextArea("Write your message...", 0, 43); //padding to be done
	private JButton linkButton 	= new JButton(MainWindow.mouseExitedL);
	private JButton sendButton 	= new JButton(MainWindow.mouseExitedS);

	/* Resources */
	public static ImageIcon icon 		= new ImageIcon("../resources/images/MyProfilePicture.png");

	public static Icon mouseEnteredM 	= new ImageIcon("../resources/images/SendNewMsg_Button_Clicked.png");
	public static Icon mouseExitedM 	= new ImageIcon("../resources/images/SendNewMsg_Button_IDLE.png");
	public static Icon mouseEnteredO 	= new ImageIcon("../resources/images/SetOptions_Button_Clicked.png");
	public static Icon mouseExitedO		= new ImageIcon("../resources/images/SetOptions_Button_IDLE.png");
	public static Icon mouseEnteredL 	= new ImageIcon("../resources/images/AddFiles_Button_Clicked.png");
	public static Icon mouseExitedL		= new ImageIcon("../resources/images/AddFiles_Button_IDLE.png");
	public static Icon mouseEnteredS	= new ImageIcon("../resources/images/SendMsg_Button_Clicked.png");
	public static Icon mouseExitedS		= new ImageIcon("../resources/images/SendMsg_Button_IDLE.png");
	// User Image
	public static ImageIcon user 		= new ImageIcon("../resources/images/ProfilePictureUserConnected.png");
	public static ImageIcon inactiveUser 	= new ImageIcon("../resources/images/ProfilePictureUserDisconnected.png");

	// Cursors
	public static Cursor handCursor 	= new Cursor(Cursor.HAND_CURSOR);
   	public static Cursor defaultCursor 	= new Cursor(Cursor.DEFAULT_CURSOR);

	public MainWindow(Client master)  {  

		super();   
	
		this.master = master;
		this.currentRecipient = this.master.getMainUser();
      
	  	this.setTitle("Aura - Chat System");
		this.setSize(MainWindow.WIDTH, MainWindow.HEIGHT); 				
		this.setLocationRelativeTo(null); 
		this.getContentPane().setBackground(MainWindow.backgroundColor);
		this.setResizable(false); 			
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				shutdown();
			}
		});
		
	
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
		this.search = Box.createHorizontalBox();
		search.setBackground(MainWindow.backgroundColor);
		

		JTextField textField 		= new JTextField(" Search something ? ...                  "); 
		textField.setFont(new Font("CALIBRI", Font.PLAIN, 15));
		textField.getFont().deriveFont(Font.ITALIC);
		textField.setForeground(Color.gray);
		textField.setBackground(MainWindow.backgroundColor);

		
		Border borderWhite = BorderFactory.createLineBorder(Color.white, 0);
		JTextField paddingSearchBar1 = new JTextField("");
		//paddingSearchBar1.setFont(new Font("CALIBRI", Font.PLAIN, 6));
		paddingSearchBar1.setBorder(borderWhite);
		paddingSearchBar1.setEditable(false); 
		paddingSearchBar1.setBackground(MainWindow.backgroundColor);

		JTextField paddingSearchBar2 = new JTextField("");
		//paddingSearchBar2.setFont(new Font("CALIBRI", Font.PLAIN, 3));
		paddingSearchBar2.setBorder(borderWhite);
		paddingSearchBar2.setEditable(false);
		paddingSearchBar2.setBackground(MainWindow.backgroundColor);

		Border borderG = BorderFactory.createLineBorder(Color.gray, 2);
		Border borderB = BorderFactory.createLineBorder(myBlue, 2);
		textField.setBorder(borderG);
		search.add(paddingSearchBar1);
		search.add(textField);
		search.add(paddingSearchBar2);
		

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
		this.panMsg.setLayout(new BoxLayout(this.panMsg, BoxLayout.Y_AXIS));

		//NameBox
		
//		this.panMsg.add(nameBox);
	
		this.userNameC	= new JLabel("");
		userNameC.setFont(new java.awt.Font("CALIBRI",Font.BOLD,18));
		JLabel lastMsgS		= new JLabel(" Last message 2 minutes ago");
		lastMsgS.setFont(new java.awt.Font("CALIBRI",Font.PLAIN,14));
		userNameC.setForeground(myBlue);
		lastMsgS.setForeground(Color.gray);
		nameBox.add(userNameC);
		nameBox.add(lastMsgS);
		
		
		/*********************************************Conversation*********************************************/

//		this.panMsg.add(conversationPanel);
		this.displayConversation();		

		
		/***********************************End of Code about Conversation**************************************/
		//Write Text Box 
		
		textBox.setBackground(MainWindow.backgroundColor);

		textField.setFont(new Font("CALIBRI", Font.PLAIN, 13));
		textField.getFont().deriveFont(Font.ITALIC);
		textField.setForeground(Color.gray);
		textField.setBackground(MainWindow.backgroundColor);
		JScrollPane scrollMsg2 	= new JScrollPane(this.writeMsg, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,  JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
		
		Border down = BorderFactory.createMatteBorder(0, 0, 1, 0, MainWindow.foregroundColor);
		//Border borderG2 = BorderFactory.createLineBorder(Color.gray, 1);
		Border borderW = BorderFactory.createMatteBorder(1, 1, 0, 1, MainWindow.backgroundColor);
		Border borderTextField = BorderFactory.createCompoundBorder(down, borderW);
		
		scrollMsg2.setBorder(borderTextField);
		this.writeMsg.setBorder(borderW);	

		this.writeMsg.addMouseListener(new MouseListener() {           
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
					sendMessage(writeMsg.getText());
					writeMsg.setText("");
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
		this.linkButton.setBorder(BorderFactory.createLineBorder(MainWindow.backgroundColor));
		this.sendButton.setBorder(BorderFactory.createLineBorder(MainWindow.backgroundColor));
		textBox.add(linkButton);
		textBox.add(sendButton);
		this.linkButton.addMouseListener(new MouseListener() {
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
		this.sendButton.addMouseListener(new MouseListener() {
				public void mouseClicked(MouseEvent e) {
					sendMessage(writeMsg.getText());
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

		this.panMsg.add(textBox);
		


		/************************************END OF MESSAGES PART*************************************************/
		/*********************************************************************************************************/
		//SPLITS
    		s1 	= new JSplitPane(JSplitPane.VERTICAL_SPLIT, panUs, this.panUsers);
		s1.setDividerSize(0);
		s1.setDividerLocation(193);
 		s1.setEnabled(false);

		s2 	= new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, s1, panMsg);
    		s2.setDividerLocation(280);
		s2.setDividerSize(7);
		s2.setEnabled(false);

		
    		this.getContentPane().add(s2, BorderLayout.CENTER);
	}

	private void displayUsersPanel() {

		ArrayList<JButton> usersArray = new ArrayList<JButton>();
		/* Clean the whole panel */
		this.panUsers.removeAll();
		/* Do not forget to add the search bar back */
		panUsers.setLayout(new BorderLayout());
		this.panUsers.add(this.search, BorderLayout.NORTH);

		/* Retrieve all the current active users */
		/*JScroll the Panel Users*/ //TO DO Maybe we could put the UserBox on the left side
		JPanel testScroll = new JPanel();
		testScroll.setBackground(Color.white);

		//testScroll.setLayout(new BorderLayout());
		JScrollPane scrollMsg 	= new JScrollPane(testScroll, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,  JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollMsg.setBackground(Color.white);
		scrollMsg.setBorder(BorderFactory.createLineBorder(Color.white));
		this.panUsers.add(scrollMsg, BorderLayout.CENTER);	
		/*End of JScrollPane Code*/		

		for (User usr : this.master.getUserList()) {	
			

			System.out.println("DISPLAY USER : " + usr.getUsername());
			System.out.flush();

	
			Box mainBox = Box.createHorizontalBox();
			mainBox.setBackground(MainWindow.backgroundColor);
			//usersArray.add(mainBox); -> add on the button

			Box rightBox = Box.createVerticalBox();
			Border emptyBorder = BorderFactory.createEmptyBorder();
			rightBox.setBorder(emptyBorder);

			/* Retrieve the user's username */
			JLabel userName 	= new JLabel(usr.getUsername());
			userName.setFont(new java.awt.Font("CALIBRI",Font.BOLD,15));
			rightBox.add(userName);

			JLabel lastMsg;
			if (usr.getMessageHistory() != null) {
				lastMsg = new JLabel(usr.getMessageHistory().getLastMessage()); //get  29 letters + [...]
			} else {
				lastMsg = new JLabel("You haven't started a chat yet"); //get  29 letters + [...]
			}
			lastMsg.setFont(new java.awt.Font("CALIBRI",Font.PLAIN,12));
			lastMsg.setForeground(Color.gray);	
			rightBox.add(lastMsg);

			JLabel paddingUB2 	= new JLabel("  ");

			JLabel userL;
			if (usr.isActive()) {
				userL 		= new JLabel(MainWindow.user); 
			} else {
				userL 		= new JLabel(MainWindow.inactiveUser); 
			}

			userL.setAlignmentX(Component.RIGHT_ALIGNMENT);
			mainBox.add(userL);
			mainBox.add(paddingUB2);
			mainBox.add(rightBox);
			
			

			JButton buttonUser1 = new JButton();
			buttonUser1.setPreferredSize(new Dimension(305,70));
			usersArray.add(buttonUser1); //we add the ArrayList on each button

			int nb_rows = Math.max(usersArray.size(),5); //nb_users < 5, display 5 rows 
			testScroll.setLayout(new GridLayout(nb_rows,1));
			for(int i = 0 ; i < usersArray.size(); i++){
				System.out.println(usersArray.size());
				testScroll.add(usersArray.get(i));
			}
			//testScroll.setLayout(new BorderLayout());
			//testScroll.add(buttonUser1, BorderLayout.NORTH);

			buttonUser1.setBackground(MainWindow.backgroundColor);
			buttonUser1.setBorderPainted(false);
			buttonUser1.setLayout(new BorderLayout());
			buttonUser1.add(mainBox, BorderLayout.WEST);

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

		}
		
	
		/* Draw the panel again */
		this.panUsers.revalidate();
		this.panUsers.repaint();
	}

	private void displayConversation() {

		this.panMsg.removeAll();
		this.conversationPanel.removeAll();

		this.panMsg.add(this.nameBox);

		this.conversationPanel.setLayout(new BoxLayout(this.conversationPanel, BoxLayout.Y_AXIS));
		this.conversationPanel.setPreferredSize(new Dimension(275, 540));
		this.conversationPanel.setBackground(MainWindow.backgroundColor);
		
		JScrollPane scrollMsg 	= new JScrollPane(this.conversationPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,  JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		//scrollMsg.setBorder(BorderFactory.createEmptyBorder());
		scrollMsg.setBorder(BorderFactory.createLineBorder(MainWindow.backgroundColor, 10));
		
		this.panMsg.add(scrollMsg);

		MessageHistory mh = this.currentRecipient.getMessageHistory();
		
		if (mh == null) {
			/* What to do if there is no conversation yet with the recipient user */
		} else {
			for (Message msg: this.currentRecipient.getMessageHistory().getMessageList()) {
				/* If the message comes from the recipient user */
				if (msg.getHasBeenSentByRecipient()) { 

					JPanel msgRow = new JPanel();
					msgRow.setLayout(new BorderLayout());
					msgRow.setPreferredSize(new Dimension(200, 100));

					String content = msg.getContent();

					JTextArea receivedMsg = new JTextArea(content);
				
					receivedMsg.setForeground(Color.black);
					receivedMsg.setBackground(myGray);
					receivedMsg.setWrapStyleWord(true);
					receivedMsg.setLineWrap(true);
					receivedMsg.setEditable(false);
					
					
					receivedMsg.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(MainWindow.backgroundColor, 4), BorderFactory.createEmptyBorder(3, 3, 3, 3)));

					msgRow.add(receivedMsg, BorderLayout.LINE_START);
					this.conversationPanel.add(msgRow);
						
				}
				/* If we are the one who sent the message */
				else {

					JPanel msgRow = new JPanel();
					msgRow.setLayout(new BorderLayout());
					msgRow.setPreferredSize(new Dimension(200, 100));

					String content = msg.getContent();

					JTextArea sentMsg = new JTextArea(content);
					

					sentMsg.setForeground(Color.white);
					sentMsg.setBackground(myBlue);
					sentMsg.setWrapStyleWord(true);
					sentMsg.setLineWrap(true);
					sentMsg.setEditable(false);
				
					sentMsg.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(MainWindow.backgroundColor, 4), BorderFactory.createEmptyBorder(3, 3, 3, 3)));

					msgRow.add(sentMsg, BorderLayout.LINE_END);

					this.conversationPanel.add(msgRow);

				}
			}
		}

		this.conversationPanel.add(Box.createVerticalGlue());

		if (!this.currentRecipient.isActive()) {
			this.writeMsg.setEnabled(false);
			this.sendButton.setEnabled(false);
			this.linkButton.setEnabled(false);
		} else {
			this.writeMsg.setEnabled(true);
			this.sendButton.setEnabled(true);
			this.linkButton.setEnabled(true);
		}


		this.conversationPanel.revalidate();
		this.conversationPanel.repaint();

		this.panMsg.add(textBox);		

		this.panMsg.add(Box.createVerticalGlue());

		this.panMsg.revalidate();
		this.panMsg.repaint();
	}

	private void updateConversationPanel(User user) {
		this.userNameC.setText(user.getUsername());
		this.currentRecipient = user;
		this.displayConversation();
	}

	/* Whether a user just got online or offline, we need to update the users panel */
	public void notifyUserActivityModification(User usr) {
		System.out.println("GUI RECEIVED : USER ACTIVITY MODIFICATION");
		System.out.flush();
		this.displayUsersPanel();
		if (this.currentRecipient.equals(usr)) {
			this.updateConversationPanel(usr);
		}
	}
	
	public void notifyNewMainUserUsername() {
		System.out.println("GUI RECEIVED : NEW MAIN USER USERNAME");
		System.out.flush();
		this.textName.setText(this.master.getMainUser().getUsername());
	}

	public void notifyNewUserUsername(User usr) {
		System.out.println("GUI RECEIVED : NEW USER USERNAME");
		System.out.flush();
		/* It will fetch the new username while redrawing the whole panel */
		this.displayUsersPanel();
		if (this.currentRecipient.equals(usr)) {
			this.updateConversationPanel(usr);
		}
	}

	public void notifyNewMessage(User recipient) {
		System.out.println("GUI RECEIVED : NEW MESSAGE");
		System.out.flush();
		if (this.currentRecipient.equals(recipient)) {
			displayConversation();
		}
	}

	private void sendMessage(String content) {
		if (!this.currentRecipient.isActive() || content == null || content.equals("")) {
			return;
		}
	
		this.master.notifyNewMessageToBeSent(content, this.currentRecipient);
	}

	private void shutdown() {
		this.master.shutdown();
		System.exit(1);
	}
}
