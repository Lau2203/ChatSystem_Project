package chatsystem.gui;

import chatsystem.gui.UserBox;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import java.awt.Font;
import java.awt.Dimension;
import javax.swing.border.Border;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

import chatsystem.MainUser;
import chatsystem.Client;

@SuppressWarnings("serial")

public class MainWindowBeginning extends JFrame{


  	Color myBlue = new Color(24, 147, 248);
	private JSplitPane s1;
	private JSplitPane s2;
	Image bg = Toolkit.getDefaultToolkit().getImage("../resources/images/backgroundPicture.png");
	
	public MainWindowBeginning()  {  

		super();   
      
	  	this.setTitle("MainWindowBeginning");
		this.setSize(900, 650); 				
		this.setLocationRelativeTo(null); 
		this.getContentPane().setBackground(Color.white);
		this.setResizable(false); 			
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		
		// Cursors
		Cursor handCursor 	= new Cursor(Cursor.HAND_CURSOR);
   		Cursor defaultCursor 	= new Cursor(Cursor.DEFAULT_CURSOR);
	
		

		Icon mouseEnteredValidate 	= new ImageIcon("../resources/images/Validate_Button_Clicked.png");
		Icon mouseExitedValidate 	= new ImageIcon("../resources/images/Validate_Button_IDLE.png");

		//About US - NORTH.LEFT
		JPanel panUs 		= new JPanel();
		panUs.setBackground(Color.BLUE); 
		Box BoxAboutUsMain = Box.createVerticalBox();
		panUs.add(BoxAboutUsMain, BorderLayout.NORTH);

		Box BoxAboutUs = Box.createVerticalBox();
		BoxAboutUsMain.add(BoxAboutUs, BorderLayout.NORTH);

		panUs.setBackground(Color.white);
		JLabel padding1 	= new JLabel("\n");
		BoxAboutUs.add(padding1);
		ImageIcon icon 		= new ImageIcon("../resources/images/MyProfilePicture.png");
		JLabel image 		= new JLabel(icon); 
		image.setAlignmentX(Component.CENTER_ALIGNMENT);
		BoxAboutUs.add(image);
		JLabel padding2		= new JLabel("\n");
		BoxAboutUs.add(padding2);
		JLabel textName 	= new JLabel("Paul Hochon");
		textName.setFont(new java.awt.Font("CALIBRI",Font.BOLD,18));
		textName.setAlignmentX(Component.CENTER_ALIGNMENT);
		BoxAboutUs.add(textName);
		JLabel padding3 	= new JLabel("\n");
		BoxAboutUs.add(padding3);

		Box BoxAboutUs2 = Box.createHorizontalBox();
		BoxAboutUsMain.add(BoxAboutUs2, BorderLayout.SOUTH);
		Icon mouseEnteredM 	= new ImageIcon("../resources/images/SendNewMsg_Button_Clicked.png");
		Icon mouseExitedM 	= new ImageIcon("../resources/images/SendNewMsg_Button_IDLE.png");
		Icon mouseEnteredO 	= new ImageIcon("../resources/images/SetOptions_Button_Clicked.png");
		Icon mouseExitedO		= new ImageIcon("../resources/images/SetOptions_Button_IDLE.png");
		
		JButton buttonO 	= new JButton(mouseExitedO);
		JButton buttonM 	= new JButton(mouseExitedM);
		buttonM.setBorder(BorderFactory.createLineBorder(Color.white));
		buttonO.setBorder(BorderFactory.createLineBorder(Color.white));
	
		//END of About US - NORTH.LEFT 
		

		
		//User Box - SOUTH.LEFT
   	 	JPanel panUsers 		= new JPanel();
    		panUsers.setBackground(Color.white);
		

		JScrollPane scrollMsg1 	= new JScrollPane();
		scrollMsg1.add(panUsers);
        

 		
		//Search Bar
		Box search = Box.createVerticalBox();
		search.setBackground(Color.white);
		

		JTextField textField 		= new JTextField(" Search something ? ...               "); //padding to be done
		textField.setFont(new Font("CALIBRI", Font.PLAIN, 15));
		textField.getFont().deriveFont(Font.ITALIC);
		textField.setForeground(Color.gray);
		
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
		    	public void mouseClicked(MouseEvent e) {
					JTextField texteField = ((JTextField)e.getSource());
					textField.setText("");
					textField.getFont().deriveFont(Font.PLAIN);
					textField.setForeground(Color.black);
		    }
		});

		panUsers.add(search);		

		/************************************TO BE DONE IN USER BOX CLASS***************************************/
		/*********************************************************************************************************/
		/******************* Create seven UB ****************************/
		for (int i = 0 ; i < 7 ; i++) {	
			Box mainBox = Box.createHorizontalBox();
			mainBox.setBackground(Color.white);

			Box rightBox = Box.createVerticalBox();
			Border emptyBorder = BorderFactory.createEmptyBorder();
			rightBox.setBorder(emptyBorder);
		
			JLabel userName 	= new JLabel("Paul Hémique");
			userName.setFont(new java.awt.Font("CALIBRI",Font.BOLD,15));
			rightBox.add(userName);
			
			JLabel lastMsg 		= new JLabel("I spoke to our IT about our Ch..."); //get 29 letters + [...]
			lastMsg.setFont(new java.awt.Font("CALIBRI",Font.PLAIN,12));
			lastMsg.setForeground(Color.gray);	
			rightBox.add(lastMsg);
			
			JLabel paddingUB2 	= new JLabel("  ");
			
			//User Image
			ImageIcon user 		= new ImageIcon("../resources/images/ProfilePictureUserConnected.png");
			JLabel userL 		= new JLabel(user); 
			

			mainBox.add(userL);
			mainBox.add(paddingUB2);
			mainBox.add(rightBox);
			
			JButton buttonUser1 	= new JButton();
			buttonUser1.setBackground(Color.white);
			buttonUser1.setBorderPainted(false);
			buttonUser1.add(mainBox);

			buttonUser1.addMouseListener(new MouseListener() {
				public void mouseClicked(MouseEvent e) {}
				@Override
	     			public void mousePressed(MouseEvent e) {
				}
				@Override
				public void mouseReleased(MouseEvent e) {
				}
				@Override
				public void mouseEntered(MouseEvent e){
					setCursor(handCursor);
					userName.setForeground(myBlue);
	       			}
				@Override
				public void mouseExited(MouseEvent e) {
					setCursor(defaultCursor);
					userName.setForeground(Color.black);
				}
			});
			
			panUsers.add(buttonUser1,BorderLayout.WEST);
		}
		
		
		/*********************************************************************************************************/
		/*********************************************************************************************************/

		/*********************************************************************************************************/
		/**************************************Messages***********************************************************/
		JPanel panelMsg 	= new JPanel();
    		panelMsg.setBackground(Color.white);
		
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
		//About Us
		Icon mouseEnteredAU 	= new ImageIcon("../resources/images/AboutUs_Button_Clicked.png");
		Icon mouseExitedAU 	= new ImageIcon("../resources/images/AboutUs_Button_IDLE.png");
		
		//Back
		Icon backA		= new ImageIcon("../resources/images/Back_Button_IDLE.png");
		Icon backB 		= new ImageIcon("../resources/images/Back_Button_Clicked.png");
		
		JButton backAButton	= new JButton(backA);
		backAButton.setBorder(BorderFactory.createLineBorder(Color.white));


		
		JButton buttonAU 	= new JButton(mouseExitedAU);
		buttonAU.setBorder(BorderFactory.createLineBorder(Color.white));
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
		
		//Texte
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


		

	

		/************************************END OF MESSAGES PART*************************************************/


		/*****************Buttons New and Option**********************************/
		BoxAboutUs2.add(buttonO);
		BoxAboutUs2.add(buttonM);
		
		

		//ImageIcon bg 		 = new ImageIcon("../resources/images/backgroundPicture.png");

		buttonO.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				panelMsg.removeAll();
				
				
				panelMsg.setLayout(new FlowLayout(FlowLayout.LEFT));
				Box mainBoxMsg = Box.createVerticalBox();
				panelMsg.add(mainBoxMsg);
				mainBoxMsg.setAlignmentX(Component.CENTER_ALIGNMENT);
				
				mainBoxMsg.add(backAButton);
	
				
				Box title = Box.createVerticalBox();
				mainBoxMsg.add(title);
				title.setAlignmentX(Component.LEFT_ALIGNMENT);
				JLabel pad1 = new JLabel(" ");
				pad1.setFont(new java.awt.Font("CALIBRI",Font.BOLD,6));
				JLabel titleOption = new JLabel("Option");
				JLabel pad2 = new JLabel(" ");
				pad2.setFont(new java.awt.Font("CALIBRI",Font.BOLD,6));
				titleOption.setFont(new java.awt.Font("CALIBRI",Font.BOLD,20));
				titleOption.setForeground(Color.gray);
				title.add(pad1);
				title.add(titleOption);
				title.add(pad2);

				//Change UserName
				Box UserN = Box.createVerticalBox();
				UserN.setAlignmentX(Component.LEFT_ALIGNMENT);
				mainBoxMsg.add(UserN);
				JLabel textOption1 = new JLabel("Set Username");
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
				JLabel textHistory2 = new JLabel("Careful, it is an irreversible operation !   ");
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
						

				panelMsg.revalidate();
				panelMsg.repaint();
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

		
	
		/*********************************************************************************************************/
		//SPLITS
    		s1 	= new JSplitPane(JSplitPane.VERTICAL_SPLIT, panUs, panUsers);
		s1.setDividerSize(0);
		s1.setDividerLocation(185);
 		s1.setEnabled(false);

		s2 	= new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, s1, panelMsg);
    		s2.setDividerLocation(280);
		s2.setDividerSize(7);
		s2.setEnabled(false);

		
    		this.getContentPane().add(s2, BorderLayout.CENTER);
	}


	public static void main(String[] args) {
		MainWindowBeginning mw = new MainWindowBeginning();
		mw.setVisible(true);
	}


}
