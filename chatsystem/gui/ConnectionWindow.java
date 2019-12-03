package chatsystem.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

import chatsystem.LocalClient;

public class ConnectionWindow extends JFrame {

	public ConnectionWindow(LocalClient master)  {    

		super();         

		Color myBlue = new Color(24, 147, 248);

	  	this.setTitle("Aura - Login");
	    	this.setSize(600, 600);
	    	this.setLocationRelativeTo(null); 
		this.getContentPane().setBackground(Color.white);
		this.setResizable(false); 			
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);            
	 
		
		/* Basic resources */
		Icon mouseEntered 	= new ImageIcon("../resources/images/buttongo.jpg");
		Icon mouseExited 	= new ImageIcon("../resources/images/buttongoshadow.png");

		// Cursors
		Cursor handCursor 	= new Cursor(Cursor.HAND_CURSOR);
   		Cursor defaultCursor 	= new Cursor(Cursor.DEFAULT_CURSOR);
	
		// Logo
		ImageIcon icon 		= new ImageIcon("../resources/images/Aura2.jpg");
		JLabel image 		= new JLabel(icon); 
		/* End of basic resources */
		
		JPanel paneltext 	= new JPanel();
		JPanel paneltext1 	= new JPanel();
		JPanel paneltext2 	= new JPanel();

		JButton button = new JButton(mouseExited);
		

		//Text zone
		JPasswordField textField = new JPasswordField(15);
		paneltext.add(textField);
		paneltext.setBackground(Color.white);
		Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
        	textField.setBorder(border);

		textField.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e){}
     			public void mousePressed(MouseEvent e){}
        		public void mouseReleased(MouseEvent e){}
        		public void mouseEntered(MouseEvent e){
				Border b = BorderFactory.createLineBorder(myBlue, 2);
           			textField.setBorder(b);
       			}
			public void mouseExited(MouseEvent e) {
				Border b = BorderFactory.createLineBorder(Color.black, 2);
           			textField.setBorder(b);}
		});
		textField.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					e.consume();
					button.doClick();
					login(master, new String(textField.getPassword()));
				}
			}
			public void keyTyped(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}
		});
 

		LineBorder noBorder = new LineBorder(Color.WHITE, 3);

		/* Login button settings */
           	button.setBorder(BorderFactory.createLineBorder(Color.white));
		paneltext.add(button);

		//Changer bouton passage de la souris
		button.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				login(master, new String(textField.getPassword()));
			}
			@Override
     			public void mousePressed(MouseEvent e) {
				button.setIcon(mouseEntered);
			}
			@Override
        		public void mouseReleased(MouseEvent e) {
				button.setIcon(mouseExited);
			}
			@Override
        		public void mouseEntered(MouseEvent e){
				setCursor(handCursor);
       			}
			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(defaultCursor);
			}
			
 
    		});
		/* END of login button settings */

		//Forgot Password
		JTextArea labelArea = new JTextArea("Forgot your Password ?  \n \n \n \n \n \n \n \n \n");
		labelArea.setEditable(false);
		labelArea.setOpaque(false);
		paneltext2.add(labelArea);
		paneltext2.setBackground(Color.white);
		labelArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent me) {
				setCursor(handCursor);
				labelArea.setForeground(myBlue);
			}
			@Override
			public void mouseExited(MouseEvent me) {
				setCursor(defaultCursor);
				labelArea.setForeground(Color.black);
			}
			@Override
			public void mouseClicked(MouseEvent e){
				JOptionPane.showMessageDialog(null, "Please contact your administrator");
			}
      		});


		this.add(image, BorderLayout.NORTH);
		this.add(paneltext, BorderLayout.CENTER);
		this.add(paneltext2, BorderLayout.SOUTH);
		
  	}       	

	private void login(LocalClient master, String input) {
		if (master.login(input)) {

			this.setVisible(false);

			synchronized(master) {
				master.notify();
			}

			this.dispose();
		}
	}

	public static void main(String[] args) {
		ConnectionWindow cw = new ConnectionWindow(null);
		cw.setVisible(true);
	}
}


