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
import java.util.Calendar;
import java.applet.Applet;


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
		
		JPanel panUs = new JPanel();	
		panUs.setBackground(myBlue);
		JLabel textName = new JLabel("Paul Hochon", SwingConstants.CENTER);
		textName.setFont(new java.awt.Font(Font.SERIF,Font.BOLD,15));
		panUs.add(textName);
		

   	 	JPanel panUsers = new JPanel();
    		panUsers.setBackground(Color.white);
        	JScrollPane editorScrollPaneUsers = new JScrollPane(panUsers);  
 		
		
   	 	JPanel panMsg = new JPanel();
    		panMsg.setBackground(Color.white);
		JScrollPane editorScrollPaneMsg = new JScrollPane(panMsg);
		

		//SPLITS
    		s1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panUs, panUsers);
		s1.setDividerSize(2);
		s1.setDividerLocation(200);
 		s1.setEnabled( false );

		s2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, s1, panMsg);
    		s2.setDividerLocation(300);
		s2.setDividerSize(7);
		s2.setEnabled( false );


    		this.getContentPane().add(s2, BorderLayout.CENTER);

		
		

	}

	public static void main(String[] args) {
		MainWindow cw = new MainWindow();
		cw.setVisible(true);
	}


}
