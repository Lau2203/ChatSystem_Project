package chatsystem.gui;

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



public class MainWindow extends JFrame{

  	Color myBlue = new Color(24, 147, 248);
	private JSplitPane s1;
	private JSplitPane s2;
	private JSplitPane s3;

	public MainWindow()  {  
		super();         
	  	this.setTitle("MailWindow");
		this.setSize(900, 650); 				
		this.setLocationRelativeTo(null);
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		this.getContentPane().setBackground(Color.white);
		this.setResizable(true); 			
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		
		//About US
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
		JLabel padding2 = new JLabel("\n\n");
		BoxAboutUs.add(padding2);
		JLabel textName = new JLabel("Paul Hochon");
		textName.setFont(new java.awt.Font("CALIBRI",Font.BOLD,18));
		textName.setAlignmentX(Component.CENTER_ALIGNMENT);
		BoxAboutUs.add(textName);
		

		Box BoxAboutUs2 = Box.createHorizontalBox();
		BoxAboutUsMain.add(BoxAboutUs2, BorderLayout.SOUTH);
		ImageIcon icon2 = new ImageIcon("../resources/images/msg.png");
		JLabel msg = new JLabel(icon2); 
		BoxAboutUs2.add(msg);
		ImageIcon icon3 = new ImageIcon("../resources/images/option.png");
		JLabel option= new JLabel(icon3); 
		BoxAboutUs2.add(option);
		



   	 	JPanel panUsers = new JPanel();
    		panUsers.setBackground(Color.white);
        	JScrollPane editorScrollPaneUsers = new JScrollPane(panUsers);  
 		
		
   	 	JPanel panMsg = new JPanel();
    		panMsg.setBackground(Color.white);
		JScrollPane editorScrollPaneMsg = new JScrollPane(panMsg);
		

		//SPLITS
    		s1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panUs, panUsers);
		s1.setDividerSize(2);
		s1.setDividerLocation(180);
 		s1.setEnabled( false );

		s2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, s1, panMsg);
    		s2.setDividerLocation(280);
		s2.setDividerSize(7);
		s2.setEnabled( false );


    		this.getContentPane().add(s2, BorderLayout.CENTER);

		
		

	}

	public static void main(String[] args) {
		MainWindow cw = new MainWindow();
		cw.setVisible(true);
	}


}
