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

@SuppressWarnings("serial")

public class MainWindow extends JFrame{


  	Color myBlue = new Color(24, 147, 248);
	private JSplitPane s1;
	private JSplitPane s2;

	public MainWindow()  {  

		super();   
      
	  	this.setTitle("MailWindow");
		this.setSize(900, 650); 				
		this.setLocationRelativeTo(null); 
		this.getContentPane().setBackground(Color.white);
		this.setResizable(true); 			
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		
		// Cursors
		Cursor handCursor 	= new Cursor(Cursor.HAND_CURSOR);
   		Cursor defaultCursor 	= new Cursor(Cursor.DEFAULT_CURSOR);
	
		//About US - NORTH.LEFT
		JPanel panUs = new JPanel();
		panUs.setBackground(Color.BLUE); 
		Box BoxAboutUsMain = Box.createVerticalBox();
		panUs.add(BoxAboutUsMain, BorderLayout.NORTH);

		Box BoxAboutUs = Box.createVerticalBox();
		BoxAboutUsMain.add(BoxAboutUs, BorderLayout.NORTH);

		panUs.setBackground(Color.white);
		JLabel padding1 = new JLabel("\n");
		BoxAboutUs.add(padding1);
		ImageIcon icon = new ImageIcon("../resources/images/user.png");
		JLabel image = new JLabel(icon); 
		image.setAlignmentX(Component.CENTER_ALIGNMENT);
		BoxAboutUs.add(image);
		JLabel padding2 = new JLabel("\n");
		BoxAboutUs.add(padding2);
		JLabel textName = new JLabel("Paul Hochon");
		textName.setFont(new java.awt.Font("CALIBRI",Font.BOLD,18));
		textName.setAlignmentX(Component.CENTER_ALIGNMENT);
		BoxAboutUs.add(textName);
		JLabel padding3 = new JLabel("\n");
		BoxAboutUs.add(padding3);

		Box BoxAboutUs2 = Box.createHorizontalBox();
		BoxAboutUsMain.add(BoxAboutUs2, BorderLayout.SOUTH);
		Icon mouseEnteredM 	= new ImageIcon("../resources/images/MsgB2.png");
		Icon mouseExitedM 	= new ImageIcon("../resources/images/MsgA2.png");
		Icon mouseEnteredO 	= new ImageIcon("../resources/images/OptB.png");
		Icon mouseExitedO	= new ImageIcon("../resources/images/OptA.png");
		
		JButton buttonO = new JButton(mouseExitedO);
		JButton buttonM = new JButton(mouseExitedM);
		buttonM.setBorder(BorderFactory.createLineBorder(Color.white));
		buttonO.setBorder(BorderFactory.createLineBorder(Color.white));
		
		buttonM.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {}
			@Override
     			public void mousePressed(MouseEvent e) {
				//buttonM.setIcon(mouseEnteredM);
			}
			@Override
        		public void mouseReleased(MouseEvent e) {
				//buttonM.setIcon(mouseExitedM);
			}
			@Override
        		public void mouseEntered(MouseEvent e){
				setCursor(handCursor);
				buttonM.setIcon(mouseEnteredM);
				buttonM.setBorder(BorderFactory.createLineBorder(myBlue));
       			}
			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(defaultCursor);
				buttonM.setIcon(mouseExitedM);
				buttonM.setBorder(BorderFactory.createLineBorder(Color.white));
			}
		});

		buttonO.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {}
			@Override
     			public void mousePressed(MouseEvent e) {
				//buttonO.setIcon(mouseEnteredO);
			}
			@Override
        		public void mouseReleased(MouseEvent e) {
				//buttonO.setIcon(mouseExitedO);
			}
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

		BoxAboutUs2.add(buttonO);
		BoxAboutUs2.add(buttonM);
		//END of About US - NORTH.LEFT 
		

		
		//User Box - SOUTH.LEFT
   	 	JPanel panUsers = new JPanel();
    		panUsers.setBackground(Color.white);
        	JScrollPane scroll = new JScrollPane();
		panUsers.add(scroll);

 		
		//Search Bar
		Box search = Box.createVerticalBox();
		search.setBackground(Color.white);
		

		JTextField textField = new JTextField(" Search something ? ...               "); //padding to be done
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
		
			JLabel userName = new JLabel("Paul Hémique");
			userName.setFont(new java.awt.Font("CALIBRI",Font.BOLD,15));
			rightBox.add(userName);
			
			JLabel lastMsg = new JLabel("I spoke to our IT about our Ch..."); //get 29 letters + [...]
			lastMsg.setFont(new java.awt.Font("CALIBRI",Font.PLAIN,12));
			lastMsg.setForeground(Color.gray);	
			rightBox.add(lastMsg);
			
			JLabel paddingUB2 = new JLabel("  ");
			
			//User Image
			ImageIcon user = new ImageIcon("../resources/images/activet.png");
			JLabel userL = new JLabel(user); 
			

			mainBox.add(userL);
			mainBox.add(paddingUB2);
			mainBox.add(rightBox);
			
			JButton buttonUser1 = new JButton();
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
		//Messages
   	 	JPanel panMsg = new JPanel();
    		panMsg.setBackground(Color.white);
		

		//Main Box
		Box mainBoxM = Box.createVerticalBox();
		mainBoxM.setBackground(Color.white);
		panMsg.add(mainBoxM);

		//Messages Box
		Box msgBox = Box.createVerticalBox();
		msgBox.setBackground(Color.white);
		msgBox.add((Box.createRigidArea(new Dimension(600, 560))));
		mainBoxM.add(msgBox);

		//Write Text Box 
		Box textBox = Box.createHorizontalBox();
		textBox.setBackground(Color.white);
		JTextArea writeMsg = new JTextArea(2, 33);
		writeMsg.setFont(new java.awt.Font("CALIBRI",Font.PLAIN,15));
		JScrollPane scrollMsg = new JScrollPane(writeMsg, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,  JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		writeMsg.setBorder(borderG);
		//Do not split words
		writeMsg.setWrapStyleWord(true);
		writeMsg.setLineWrap(true);

		writeMsg.setBorder(borderG);
		textBox.add(scrollMsg);
		//Set Icons and Button
		Icon mouseEnteredL 	= new ImageIcon("../resources/images/15.png");
		Icon mouseExitedL	= new ImageIcon("../resources/images/14.png");
		Icon mouseEnteredS	= new ImageIcon("../resources/images/16.png");
		Icon mouseExitedS	= new ImageIcon("../resources/images/17.png");
		JButton linkButton = new JButton(new ImageIcon("../resources/images/14.png"));
		JButton sendButton = new JButton(new ImageIcon("../resources/images/17.png"));
		linkButton.setBorder(BorderFactory.createLineBorder(Color.white));
		sendButton.setBorder(BorderFactory.createLineBorder(Color.white));
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
					linkButton.setIcon(mouseEnteredL);
	       			}
				@Override
				public void mouseExited(MouseEvent e) {
					linkButton.setIcon(mouseExitedL);
				}
			});
		sendButton.addMouseListener(new MouseListener() {
				public void mouseClicked(MouseEvent e) {}
				@Override
	     			public void mousePressed(MouseEvent e) {
				}
				@Override
				public void mouseReleased(MouseEvent e) {
				}
				@Override
				public void mouseEntered(MouseEvent e){
					sendButton.setIcon(mouseEnteredS);
	       			}
				@Override
				public void mouseExited(MouseEvent e) {
					sendButton.setIcon(mouseExitedS);
				}
			});

		mainBoxM.add(textBox, BorderLayout.SOUTH);
		


		/************************************END OF MESSAGES PART*************************************************/
		/*********************************************************************************************************/
		//SPLITS
    		s1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panUs, panUsers);
		s1.setDividerSize(0);
		s1.setDividerLocation(185);
 		s1.setEnabled(false);

		s2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, s1, panMsg);
    		s2.setDividerLocation(280);
		s2.setDividerSize(7);
		s2.setEnabled(false);

		
    		this.getContentPane().add(s2, BorderLayout.CENTER);

		
		

	}

	public static void main(String[] args) {
		MainWindow mw = new MainWindow();
		mw.setVisible(true);
	}


}
