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

import javax.swing.text.*;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.KeyListener;

import javax.swing.text.JTextComponent;

import chatsystem.gui.UserBox;

import chatsystem.Client;
import chatsystem.User;
import chatsystem.Message;
import chatsystem.MessageHistory;

@SuppressWarnings("serial")



/*************************************************Main Window Constructor*********************************************************/
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

   	private JPanel panUsers 	= new JPanel();
   	private JPanel panMsg 		= new JPanel();
	private JPanel conversationPanel = new JPanel();
	
	private Box textBox = Box.createHorizontalBox();
	private Box nameBox = Box.createHorizontalBox();
	
	private Box search;
	private boolean isSearchBarEmpty = true;
	private boolean isMsgBarEmpty = true;
	private boolean isNewMsgEmpty = true;

	private JLabel textName;
	private JLabel userNameC;

	private JTextArea writeMsg 	= new JTextArea("Write your message...", 0, 43); //padding to be done
	private JButton linkButton 	= new JButton(MainWindow.mouseExitedL);
	private JButton sendButton 	= new JButton(MainWindow.mouseExitedS);
	private JButton linkButton2 	= new JButton(MainWindow.mouseExitedL);
	private JButton sendButton2 	= new JButton(MainWindow.mouseExitedS);
	private JLabel lastMsgS		= new JLabel(" Last message 2 minutes ago");

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
	public static Icon mouseEnteredValidate = new ImageIcon("../resources/images/Validate_Button_Clicked.png");
	public static Icon mouseExitedValidate 	= new ImageIcon("../resources/images/Validate_Button_IDLE.png");
	// User Image
	public static ImageIcon user 		= new ImageIcon("../resources/images/ProfilePictureUserConnected.png");
	public static ImageIcon inactiveUser 	= new ImageIcon("../resources/images/ProfilePictureUserDisconnected.png");
	//Back
	public static Icon backA		= new ImageIcon("../resources/images/Back_Button_IDLE.png");
	public static Icon backB 		= new ImageIcon("../resources/images/Back_Button_Clicked.png");
	public static Icon back1		= new ImageIcon("../resources/images/Back_Button_IDLE.png");
	public static Icon back2 		= new ImageIcon("../resources/images/Back_Button_Clicked.png");
	//About Us
	Icon mouseEnteredAU 	= new ImageIcon("../resources/images/AboutUs_Button_Clicked.png");
	Icon mouseExitedAU 	= new ImageIcon("../resources/images/AboutUs_Button_IDLE.png");
	
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
		
		/************************************************************************************************************/
		/*************************************About Us Panel, Left-North*********************************************/
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
		
		JButton buttonM 	= new JButton(MainWindow.mouseExitedM);
		JButton buttonO 	= new JButton(MainWindow.mouseExitedO);
		
		buttonM.setBorder(BorderFactory.createLineBorder(MainWindow.backgroundColor));
		buttonO.setBorder(BorderFactory.createLineBorder(MainWindow.backgroundColor));
		BoxAboutUs2.add(buttonM);
		BoxAboutUs2.add(buttonO);
		
		/*******************************Option Button***********************************/
		buttonO.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				panMsg.removeAll();
				conversationPanel.removeAll();
				
				panMsg.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 10));
				Box mainBoxMsg = Box.createVerticalBox();
				panMsg.add(mainBoxMsg);
				mainBoxMsg.setAlignmentX(Component.CENTER_ALIGNMENT);
				
				//Back button
				JButton backButton	= new JButton(back1);
				backButton.setBorder(BorderFactory.createLineBorder(Color.white));
				mainBoxMsg.add(backButton); 
				backButton.addMouseListener(new MouseListener() {
					public void mouseClicked(MouseEvent e) {
					}
					@Override
		     			public void mousePressed(MouseEvent e) {
						displayConversation();
						setCursor(defaultCursor);
						backButton.setIcon(back1);
					}
					@Override
					public void mouseReleased(MouseEvent e) {
						
					}
					@Override
					public void mouseEntered(MouseEvent e){
						setCursor(handCursor);
						backButton.setIcon(back2);
		       			}
					@Override
					public void mouseExited(MouseEvent e) {
						setCursor(defaultCursor);
						backButton.setIcon(back1);
						
					}
				});
	
				
				Box title = Box.createVerticalBox();
				mainBoxMsg.add(title);
				title.setAlignmentX(Component.LEFT_ALIGNMENT);
				JLabel pad1 = new JLabel(" ");
				pad1.setFont(new java.awt.Font("CALIBRI",Font.BOLD,6));
				JLabel titleOption = new JLabel("Options");
				JLabel pad2 = new JLabel(" ");
				pad2.setFont(new java.awt.Font("CALIBRI",Font.BOLD,8));
				titleOption.setFont(new java.awt.Font("CALIBRI",Font.BOLD,23));
				titleOption.setForeground(Color.gray);
				title.add(pad1);
				title.add(titleOption);
				title.add(pad2);

				//Change UserName
				Box UserN = Box.createVerticalBox();
				UserN.setAlignmentX(Component.LEFT_ALIGNMENT);
				mainBoxMsg.add(UserN);
				JLabel textOption1 = new JLabel("Set new username");
				textOption1.setFont(new java.awt.Font("CALIBRI",Font.BOLD,15));
				textOption1.setForeground(myBlue);
				UserN.add(textOption1);
				JLabel textOption2 = new JLabel("You can change your username. Other users will see it.");
				textOption2.setFont(new java.awt.Font("CALIBRI",Font.PLAIN,13));
				textOption2.setForeground(Color.gray);
				UserN.add(textOption2);
				
				Box set = Box.createHorizontalBox();
				set.setAlignmentX(Component.LEFT_ALIGNMENT);
				set.setBackground(Color.white);
				UserN.add(set);
				Border down = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black);
				Border downBlue = BorderFactory.createMatteBorder(0, 0, 1, 0, myBlue);
				Border borderW = BorderFactory.createMatteBorder(1, 1, 0, 1, Color.white);
				Border borderTextField = BorderFactory.createCompoundBorder(down, borderW);
				Border borderTextField2 = BorderFactory.createCompoundBorder(downBlue, borderW);
				
				JTextField newUserName = new JTextField("Write your new username...");
				newUserName.setFont(new Font("CALIBRI", Font.PLAIN, 13));
				newUserName.getFont().deriveFont(Font.ITALIC);
				newUserName.setForeground(Color.gray);
				newUserName.setBorder(borderTextField);
				set.add(newUserName);
					newUserName.addMouseListener(new MouseListener() {
							public void mouseClicked(MouseEvent e) {}
							@Override
				     			public void mousePressed(MouseEvent e) {}
							@Override
							public void mouseReleased(MouseEvent e) {}
							@Override
							public void mouseEntered(MouseEvent e){
								setCursor(handCursor);
								newUserName.setBorder(borderTextField2);
				       			}
							@Override
							public void mouseExited(MouseEvent e) {
								setCursor(defaultCursor);
								newUserName.setBorder(borderTextField);
					
							}
					});

				JButton buttonGo = new JButton(mouseExitedValidate);
				set.add(buttonGo);
				Border emptyBorderButton = BorderFactory.createEmptyBorder();
				buttonGo.setBorder(emptyBorderButton);
				

					buttonGo.addMouseListener(new MouseListener() {
							@Override
							public void mouseClicked(MouseEvent e) {
							}
							@Override
				     			public void mousePressed(MouseEvent e) {
								
							}
							@Override
							public void mouseReleased(MouseEvent e) {
								
							}
							@Override
							public void mouseEntered(MouseEvent e){
								setCursor(handCursor);
								buttonGo.setIcon(mouseEnteredValidate);
				       			}
							@Override
							public void mouseExited(MouseEvent e) {
								setCursor(defaultCursor);
								buttonGo.setIcon(mouseExitedValidate);
							}
					});

				//Clear History
				Box clear = Box.createVerticalBox();
				clear.setAlignmentX(Component.LEFT_ALIGNMENT);
				mainBoxMsg.add(clear);
				JLabel pad3 = new JLabel(" ");
				pad3.setFont(new java.awt.Font("CALIBRI",Font.BOLD,15));
				JLabel textOption3 = new JLabel("Clear your history");
				textOption3.setFont(new java.awt.Font("CALIBRI",Font.BOLD,15));
				textOption3.setForeground(myBlue);
				clear.add(pad3);
				clear.add(textOption3);
				JLabel textHistory = new JLabel("You can delete your chat sessions at any time.");
				textHistory.setFont(new java.awt.Font("CALIBRI",Font.PLAIN,13));
				textHistory.setForeground(Color.gray);
				clear.add(textHistory);
				Box textAndButton = Box.createHorizontalBox();
				textAndButton.setAlignmentX(Component.LEFT_ALIGNMENT);
				mainBoxMsg.add(textAndButton);
				JLabel textHistory2 = new JLabel("Careful, it is an irreversible operation ! ");
				textHistory2.setFont(new java.awt.Font("CALIBRI",Font.PLAIN,13));
				textHistory2.setForeground(Color.gray);
				textAndButton.add(textHistory2);
				JButton buttonGo2 = new JButton(mouseExitedValidate);
				textAndButton.add(buttonGo2);
				buttonGo2.setBorder(emptyBorderButton);
				

					buttonGo2.addMouseListener(new MouseListener() {
							@Override
							public void mouseClicked(MouseEvent e) {
							}
							@Override
				     			public void mousePressed(MouseEvent e) {
								
							}
							@Override
							public void mouseReleased(MouseEvent e) {
								
							}
							@Override
							public void mouseEntered(MouseEvent e){
								setCursor(handCursor);
								buttonGo2.setIcon(mouseEnteredValidate);
				       			}
							@Override
							public void mouseExited(MouseEvent e) {
								setCursor(defaultCursor);
								buttonGo2.setIcon(mouseExitedValidate);
							}
					});

				//Appear offline
				Box offline = Box.createVerticalBox();
				offline.setAlignmentX(Component.LEFT_ALIGNMENT);
				mainBoxMsg.add(offline);
				JLabel pad5 = new JLabel(" ");
				pad5.setFont(new java.awt.Font("CALIBRI",Font.BOLD,15));
				JLabel textOption5 = new JLabel("Appear offline");
				textOption5.setFont(new java.awt.Font("CALIBRI",Font.BOLD,15));
				textOption5.setForeground(myBlue);
				offline.add(pad5);
				offline.add(textOption5);
				JLabel textOffline = new JLabel("You can choose to be invisible to other users.");
				textOffline.setFont(new java.awt.Font("CALIBRI",Font.PLAIN,13));
				textOffline.setForeground(Color.gray);
				offline.add(textOffline);
				Box offline2 = Box.createHorizontalBox();
				offline2.setAlignmentX(Component.LEFT_ALIGNMENT);
				mainBoxMsg.add(offline2);
				JLabel textOffline2 = new JLabel("You will still be able to send messages. ");
				textOffline2.setFont(new java.awt.Font("CALIBRI",Font.PLAIN,13));
				textOffline2.setForeground(Color.gray);
				JCheckBox check = new JCheckBox("Offline");
				check.setFont(new java.awt.Font("CALIBRI", Font.PLAIN, 13));
				check.setBackground(Color.white);
				check.setForeground(myBlue);
				if (check.isSelected()) {
				    //what to do if check box is selected -> be offline
				} else {
				    //do nothing -> be connected
				}
				offline2.add(textOffline2);
				offline2.add(check);

				//Footer
				ImageIcon footer		= new ImageIcon("../resources/images/backgroundPicture4.png");
				JLabel footerLabel		= new JLabel(footer); 
				panMsg.add(footerLabel);

				/* Draw the panel again */
				conversationPanel.revalidate();
				conversationPanel.repaint();
				panMsg.revalidate();
				panMsg.repaint();

				} //end of mouse clicked on Option
			@Override
     			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e){
				setCursor(handCursor);
				buttonO.setIcon(mouseEnteredO);
				buttonO.setBorder(BorderFactory.createLineBorder(myBlue));
       			}
			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(defaultCursor);
				buttonO.setIcon(mouseExitedO);
				buttonO.setBorder(BorderFactory.createLineBorder(Color.white));
			}	
			

		});
		/*********************************End of Option Button********************************/
		/*********************************New Button******************************************/
		buttonM.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				panMsg.removeAll();
				conversationPanel.removeAll();
				
				panMsg.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 10));
				Box mainBoxMsg = Box.createVerticalBox();
				panMsg.add(mainBoxMsg);
				mainBoxMsg.setAlignmentX(Component.CENTER_ALIGNMENT);
				
				//Back button
				JButton backButton	= new JButton(back1);
				backButton.setBorder(BorderFactory.createLineBorder(Color.white));
				mainBoxMsg.add(backButton); 
				backButton.addMouseListener(new MouseListener() {
					public void mouseClicked(MouseEvent e) {
					}
					@Override
		     			public void mousePressed(MouseEvent e) {
						displayConversation();
						setCursor(defaultCursor);
						backButton.setIcon(back1);
					}
					@Override
					public void mouseReleased(MouseEvent e) {
						
					}
					@Override
					public void mouseEntered(MouseEvent e){
						setCursor(handCursor);
						backButton.setIcon(back2);
		       			}
					@Override
					public void mouseExited(MouseEvent e) {
						setCursor(defaultCursor);
						backButton.setIcon(back1);
						
					}
				});
	
				
				Box title = Box.createVerticalBox();
				mainBoxMsg.add(title);
				title.setAlignmentX(Component.LEFT_ALIGNMENT);
				JLabel pad1 				= new JLabel(" ");
				pad1.setFont(new java.awt.Font("CALIBRI",Font.BOLD,6));
				JLabel titleOption 			= new JLabel("New Message");
				JLabel pad2 				= new JLabel(" ");
				pad2.setFont(new java.awt.Font("CALIBRI",Font.BOLD,10));
				titleOption.setFont(new java.awt.Font("CALIBRI",Font.BOLD,23));
				titleOption.setForeground(Color.gray);
				title.add(pad1);
				title.add(titleOption);
				title.add(pad2);

				//Combo Box
				Box newMsg = Box.createHorizontalBox();
				newMsg.setAlignmentX(Component.LEFT_ALIGNMENT);
				
				ArrayList<User> usersConnected 		= new ArrayList<User>();
				JComboBox<User> usersConnectedBox 	= new JComboBox<User>();
				for (User usr : master.getUserList()) {
					if (usr.isActive()){
						usersConnected.add(usr);
					}
				}
				usersConnectedBox.setModel(new DefaultComboBoxModel<User>(usersConnected.toArray(new User[usersConnected.size()])));
				usersConnectedBox.setEditable(false);
			

				JLabel fromLabel 			= new JLabel("From: " + master.getMainUserUsername() + " (you) ");
				fromLabel.setFont(new java.awt.Font("CALIBRI",Font.BOLD,15));
				fromLabel.setForeground(Color.gray);
				JLabel toLabel 				= new JLabel("To: ");
				toLabel.setFont(new java.awt.Font("CALIBRI",Font.BOLD,15));
				toLabel.setForeground(Color.gray);
				mainBoxMsg.add(fromLabel);
				mainBoxMsg.add(newMsg);
				newMsg.add(toLabel);
				newMsg.add(usersConnectedBox);
				JLabel pad6 				= new JLabel(" ");
				pad6.setFont(new java.awt.Font("CALIBRI",Font.BOLD,8));
				mainBoxMsg.add(pad6);

				Box newMsgBox = Box.createHorizontalBox();
				mainBoxMsg.add(newMsgBox);
				newMsgBox.setAlignmentX(Component.LEFT_ALIGNMENT);
				JTextArea writeNewMsg			= new JTextArea("Write your message here...",19,25);
				JScrollPane scrollNewMsg1 		= new JScrollPane(writeNewMsg, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,  JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				Border border = BorderFactory.createLineBorder(Color.gray);
    				scrollNewMsg1.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
				scrollNewMsg1.setBackground(Color.white);
				writeNewMsg.setWrapStyleWord(true);
				writeNewMsg.setLineWrap(true);
				newMsgBox.add(scrollNewMsg1);
				newMsgBox.add(linkButton2);
				newMsgBox.add(sendButton2);
				linkButton2.setBorder(BorderFactory.createLineBorder(MainWindow.backgroundColor));
				sendButton2.setBorder(BorderFactory.createLineBorder(MainWindow.backgroundColor));
				
				linkButton2.addMouseListener(new MouseListener() {
						public void mouseClicked(MouseEvent e) {}
						@Override
			     			public void mousePressed(MouseEvent e) {
						}
						@Override
						public void mouseReleased(MouseEvent e) {
						}
						@Override
						public void mouseEntered(MouseEvent e){
							linkButton2.setIcon(MainWindow.mouseEnteredL);
			       			}
						@Override
						public void mouseExited(MouseEvent e) {
							linkButton2.setIcon(MainWindow.mouseExitedL);
						}
					});
				sendButton2.addMouseListener(new MouseListener() {
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
							sendButton2.setIcon(MainWindow.mouseEnteredS);
			       			}
						@Override
						public void mouseExited(MouseEvent e) {
							sendButton2.setIcon(MainWindow.mouseExitedS);
						}
					});
				writeNewMsg.addFocusListener(new FocusListener() {
				@Override
				public void focusGained(FocusEvent e) {
					if (isNewMsgEmpty) {
						writeNewMsg.setText("");
						writeNewMsg.setForeground(MainWindow.foregroundColor);
					}
				}
				@Override
				public void focusLost(FocusEvent e) {
					if (writeNewMsg.getText().equals("")) {
						isNewMsgEmpty= true;
						writeNewMsg.setText("Write your message here...");
						writeNewMsg.setForeground(Color.gray);
					} else {
						isNewMsgEmpty = false;
					}
				}
			});
			
				//Footer
				ImageIcon footer2		= new ImageIcon("../resources/images/backgroundPicture5.png");
				JLabel footerLabel2		= new JLabel(footer2); 
				panMsg.add(footerLabel2);
				
				conversationPanel.revalidate();
				conversationPanel.repaint();
				panMsg.revalidate();
				panMsg.repaint();
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
		/*********************************End of New Button******************************************/
		/*****************************************About Us Panel code end here********************************************/
		/*****************************************************************************************************************/
		



		/*****************************************************************************************************************/
		/*************************************Users List, South Left******************************************************/
    		this.panUsers.setBackground(MainWindow.backgroundColor);
        	
		//Search Bar
		this.search = Box.createHorizontalBox();
		search.setBackground(MainWindow.backgroundColor);
		

		JTextField textField 		= new JTextField(" Search something ? ...     "); 
		textField.setFont(new Font("CALIBRI", Font.PLAIN, 15));
		textField.getFont().deriveFont(Font.ITALIC);
		textField.setForeground(Color.gray);
		textField.setBackground(MainWindow.backgroundColor);

		
		Border borderWhite = BorderFactory.createLineBorder(backgroundColor, 1);
		JTextField paddingSearchBar1 	= new JTextField("");
		//paddingSearchBar1.setFont(new Font("CALIBRI", Font.PLAIN, 6));
		paddingSearchBar1.setBorder(borderWhite);
		paddingSearchBar1.setEditable(false); 
		paddingSearchBar1.setBackground(MainWindow.backgroundColor);

		JTextField paddingSearchBar2 	= new JTextField("");
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
					textField.setText(" Search something ? ...     ");
					textField.setForeground(Color.gray);
				} else {
					isSearchBarEmpty = false;
				}
			}
		});

		this.panUsers.add(this.search);		
		this.displayUsersPanel();	
		/*************************************Code about Users List end***************************************************/
		/*****************************************************************************************************************/
		
		


		
		/****************************************************************************************************************/
		/************************************Conversation Panel, right panel********************************************/

		//User name and last message time at the top of the conversation part
			this.userNameC	= new JLabel("");
			userNameC.setFont(new java.awt.Font("CALIBRI",Font.BOLD,18));
			lastMsgS.setFont(new java.awt.Font("CALIBRI",Font.PLAIN,14));
			userNameC.setForeground(myBlue);
			lastMsgS.setForeground(Color.gray);
			nameBox.add(userNameC);
			nameBox.add(lastMsgS);
		
		//Display conversation with the selected user
		this.displayConversation();		

		/*************************************End of Conversation Panel code****************************************/
		/***********************************************************************************************************/
		



		//Split the three main Panel described above
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

/*************************************************End of Main Window Constructor *********************************************************/







/********************************************Display Users Panel (west/south) method*****************************************************/
	private void displayUsersPanel() {

		ArrayList<JButton> usersArray 		= new ArrayList<JButton>();
		/* Clean the whole panel */
		this.panUsers.removeAll();
		/* Do not forget to add the search bar back */
		panUsers.setLayout(new BorderLayout());
		this.panUsers.add(this.search, BorderLayout.NORTH);

		/* Retrieve all the current active users */
		JPanel testScroll 			= new JPanel();
		testScroll.setBackground(backgroundColor);

		JScrollPane scrollMsg 			= new JScrollPane(testScroll, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,  JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollMsg.setBackground(backgroundColor);
		scrollMsg.setBorder(BorderFactory.createLineBorder(backgroundColor));
		this.panUsers.add(scrollMsg, BorderLayout.CENTER);	
				

		for (User usr : this.master.getUserList()) {	
			
			System.out.println("DISPLAY USER : " + usr.getUsername());
			System.out.flush();
	
			Box mainBox = Box.createHorizontalBox();
			mainBox.setBackground(MainWindow.backgroundColor);

			Box rightBox = Box.createVerticalBox();
			Border emptyBorder = BorderFactory.createEmptyBorder();
			rightBox.setBorder(emptyBorder);

			/* Retrieve the user's username */
			JLabel userName 		= new JLabel(usr.getUsername());
			userName.setFont(new java.awt.Font("CALIBRI",Font.BOLD,15));
			rightBox.add(userName);

			JLabel lastMsg;
			if (usr.getMessageHistory() != null) {
				lastMsg 		= new JLabel(usr.getMessageHistory().getLastMessage()); //get  29 letters + [...]
			} else {
				lastMsg 		= new JLabel("You haven't started a chat yet"); //get  29 letters + [...]
			}
			lastMsg.setFont(new java.awt.Font("CALIBRI",Font.PLAIN,12));
			lastMsg.setForeground(Color.gray);	
			rightBox.add(lastMsg);

			JLabel paddingUB2 		= new JLabel("  ");

			JLabel userL;
			if (usr.isActive()) {
				userL 			= new JLabel(MainWindow.user); 
			} else {
				userL 			= new JLabel(MainWindow.inactiveUser); 
			}

			userL.setAlignmentX(Component.RIGHT_ALIGNMENT);
			mainBox.add(userL);
			mainBox.add(paddingUB2);
			mainBox.add(rightBox);
			
			JButton buttonUser1		 = new JButton();
			buttonUser1.setPreferredSize(new Dimension(305,70));
			usersArray.add(buttonUser1);
			
			//if nb_users < 5, display 5 rows 
			int nb_rows = Math.max(usersArray.size(),5); 
			testScroll.setLayout(new GridLayout(nb_rows,1));
			for(int i = 0 ; i < usersArray.size(); i++){
				System.out.println(usersArray.size());
				testScroll.add(usersArray.get(i));
			}

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

/********************************************End of display Users Panel (west/south) method***********************************************/
	



/********************************************Dislay Conversation Panel (east) method*****************************************************/
	private void displayConversation() {
		
		this.panMsg.removeAll();
		this.conversationPanel.removeAll();
		this.panMsg.setBackground(MainWindow.backgroundColor); 
		this.panMsg.setLayout(new BoxLayout(this.panMsg, BoxLayout.Y_AXIS));
		
		MessageHistory mh = this.currentRecipient.getMessageHistory();
	
		/************************ What to do if there is no conversation yet with the recipient user *************************/
		if (mh == null) {
			this.panMsg.removeAll();
			this.conversationPanel.removeAll();

			
			JPanel panelMsg 		= new JPanel();
	    		panelMsg.setBackground(backgroundColor);
			
			panMsg.add(panelMsg);
			//this.conversationPanel.add(panelMsg);
			
			Box boxMsg = Box.createVerticalBox();
			//boxMsg.setAlignmentX(Component.RIGHT_ALIGNMENT);
			panelMsg.add(boxMsg);


			// Logo
			ImageIcon logo 			= new ImageIcon("../resources/images/AuraLogo1.jpg");
			JLabel imageLogo		= new JLabel(logo); 
			imageLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
			boxMsg.add(imageLogo);
			ImageIcon logo2 		= new ImageIcon("../resources/images/AboutUsPicture.png");
			JLabel aboutUsFinal		= new JLabel(logo2); 
			JLabel paddingT1 		= new JLabel(" ");
			paddingT1.setFont(new java.awt.Font("CALIBRI",Font.BOLD,17));
			boxMsg.add(paddingT1);
			JButton backAButton		= new JButton(backA);	
			backAButton.setBorder(BorderFactory.createLineBorder(backgroundColor));

			
			JButton buttonAU 		= new JButton(mouseExitedAU);
			buttonAU.setBorder(BorderFactory.createLineBorder(backgroundColor));
			buttonAU.setAlignmentX(Component.CENTER_ALIGNMENT);
			boxMsg.add(buttonAU, BorderLayout.CENTER);
			buttonAU.addMouseListener(new MouseListener() {
				public void mouseClicked(MouseEvent e) {}
				@Override
	     			public void mousePressed(MouseEvent e) {
					panelMsg.removeAll();
					panelMsg.setLayout(new FlowLayout(FlowLayout.LEFT));
					panelMsg.add(backAButton);
					panelMsg.revalidate();
					panelMsg.add(aboutUsFinal);
					panelMsg.repaint();
					buttonAU.setIcon(mouseExitedAU);
					setCursor(defaultCursor);
				}
				@Override
				public void mouseReleased(MouseEvent e) {
					
				}
				@Override
				public void mouseEntered(MouseEvent e){
					setCursor(handCursor);
					buttonAU.setIcon(mouseEnteredAU);
	       			}
				@Override
				public void mouseExited(MouseEvent e) {
					setCursor(defaultCursor);
					buttonAU.setIcon(mouseExitedAU);
					
				}
			});
			
			//Text
			JLabel paddingT2 		= new JLabel(" ");
			paddingT2.setFont(new java.awt.Font("CALIBRI",Font.BOLD,17));
			JLabel sendTextMsg 		= new JLabel("Please, select a user to start a chat session.");
			sendTextMsg.setFont(new java.awt.Font("CALIBRI",Font.PLAIN,15));
			sendTextMsg.setForeground(Color.gray);
			sendTextMsg.setAlignmentX(Component.CENTER_ALIGNMENT);
			boxMsg.add(paddingT2, BorderLayout.CENTER);
			boxMsg.add(sendTextMsg, BorderLayout.CENTER);

			backAButton.addMouseListener(new MouseListener() {
				public void mouseClicked(MouseEvent e) {}
				@Override
	     			public void mousePressed(MouseEvent e) {
					panelMsg.removeAll();
					panelMsg.add(boxMsg);
					boxMsg.add(imageLogo);
					boxMsg.add(paddingT1);
					boxMsg.add(buttonAU, BorderLayout.CENTER);
					boxMsg.add(paddingT2, BorderLayout.CENTER);
					boxMsg.add(sendTextMsg, BorderLayout.CENTER);
					panelMsg.revalidate();
					panelMsg.repaint();
					backAButton.setIcon(backA);
					panelMsg.setLayout(new FlowLayout(FlowLayout.CENTER));
					setCursor(defaultCursor);
				}
				@Override
				public void mouseReleased(MouseEvent e) {
					
				}
				@Override
				public void mouseEntered(MouseEvent e){
					setCursor(handCursor);
					backAButton.setIcon(backB);
	       			}
				@Override
				public void mouseExited(MouseEvent e) {
					setCursor(defaultCursor);
					backAButton.setIcon(backA);
					
				}
			});
			/***************** End of what to do if there is no conversation yet with the recipient user ******************/
			




			/********************** What to do if there is a conversation with the recipient user ************************/
		} else {
			
			this.panMsg.removeAll();
			this.conversationPanel.removeAll();

			//Name at the top
			this.panMsg.add(this.nameBox);
			
			this.conversationPanel.setLayout(new BoxLayout(this.conversationPanel, BoxLayout.Y_AXIS));
			this.conversationPanel.setBackground(MainWindow.backgroundColor);
			
			JScrollPane scrollMsg 		= new JScrollPane(this.conversationPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,  JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scrollMsg.setBorder(BorderFactory.createLineBorder(MainWindow.backgroundColor, 10));
			scrollMsg.setPreferredSize(new Dimension(300,550));
			
			this.panMsg.add(scrollMsg);
			
			//TextBox at the bottom
			Border borderG = BorderFactory.createLineBorder(Color.gray, 2);
			Border borderB = BorderFactory.createLineBorder(myBlue, 2);
			this.panMsg.add(textBox);
			textBox.setBackground(MainWindow.backgroundColor);

			JTextField textField2 		= new JTextField(" Write your message...");
			textField2.setFont(new Font("CALIBRI", Font.PLAIN, 13));
			textField2.getFont().deriveFont(Font.ITALIC);
			textField2.setForeground(Color.gray);
			textField2.setBackground(MainWindow.backgroundColor);
			JScrollPane scrollMsg2 	= new JScrollPane(this.writeMsg, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,  JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
			
			Border down = BorderFactory.createMatteBorder(0, 0, 1, 0, MainWindow.foregroundColor);
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
					textField2.setBorder(borderG);
				}          
			    	@Override
			    	public void mouseEntered(MouseEvent e) {
					textField2.setBorder(borderB);
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

			
			/**************************************New Messages Management***********************************************/
			for (Message msg: this.currentRecipient.getMessageHistory().getMessageList()) {
				/* If the message comes from the recipient user */
				if (msg.getHasBeenSentByRecipient()) { 

					JPanel msgRow 		= new JPanel();
					msgRow.setBackground(MainWindow.backgroundColor);
					msgRow.setLayout(new BorderLayout());
					

					String content = msg.getContent();

					JTextArea receivedMsg;
					

				

					if(content.length() > 30) {
						receivedMsg 	= new JTextArea(content, 1, 30);
					} else {
						receivedMsg 	= new JTextArea(content, 1, content.length());
					}

					
					
					receivedMsg.setForeground(Color.black);
					receivedMsg.setBackground(myGray);
					receivedMsg.setWrapStyleWord(true);
					receivedMsg.setLineWrap(true);
					receivedMsg.setEditable(false);

					//How to have timestamped messages
					Timestamp ts = msg.getTimestamp();
					Date date=new Date(ts.getTime());
					DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy | HH:mm");  
                			String strDate = dateFormat.format(date);
					receivedMsg.setToolTipText(strDate);
			
					DateFormat dateFormat2 = new SimpleDateFormat("MM/dd");  
                			String strDate2 = dateFormat2.format(date);
					DateFormat dateFormat3 = new SimpleDateFormat("HH:mm");  
                			String strDate3 = dateFormat3.format(date);
					lastMsgS.setText("  Last message sent the " + strDate2 + " at " + strDate3 );
					
					receivedMsg.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(MainWindow.backgroundColor, 4), BorderFactory.createEmptyBorder(3, 3, 3, 3)));

					msgRow.add(receivedMsg, BorderLayout.LINE_START);
					this.conversationPanel.add(msgRow);
						
				}
				/* If we are the one who sent the message */
				else {

					JPanel msgRow 		= new JPanel();
					msgRow.setBackground(MainWindow.backgroundColor);
					msgRow.setLayout(new BorderLayout());
					
					
					String content = msg.getContent();

					JTextArea sentMsg;
					if(content.length() > 30) {
						sentMsg		= new JTextArea(content, 1, 30);
					} else {
						sentMsg 	= new JTextArea(content, 1, content.length());
					}
				
					sentMsg.setForeground(backgroundColor);
					sentMsg.setBackground(myBlue);
					sentMsg.setWrapStyleWord(true);
					sentMsg.setLineWrap(true);
					sentMsg.setEditable(false);
					
					//How to have timestamped messages
					Timestamp ts = msg.getTimestamp();
					Date date		=new Date(ts.getTime());
					DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy | HH:mm");  
                			String strDate = dateFormat.format(date);    
					sentMsg.setToolTipText(strDate);
				
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

		/* Draw the panel again */
		this.conversationPanel.revalidate();
		this.conversationPanel.repaint();
		this.panMsg.add(Box.createVerticalGlue());
		this.panMsg.revalidate();
		this.panMsg.repaint();
		
	}
/******************************************End of Dislay Conversation Panel (east) method**************************************************/



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
