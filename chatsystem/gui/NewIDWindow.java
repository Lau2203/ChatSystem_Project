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
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;

import chatsystem.LocalClient;

@SuppressWarnings("serial")
public class NewIDWindow extends JFrame {

	public NewIDWindow()  {    

		super();         

		Color myBlue = new Color(24, 147, 248);

	  	this.setTitle("Aura - New ID");
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
		ImageIcon icon 		= new ImageIcon("../resources/images/Aura3.jpg");
		JLabel image 		= new JLabel(icon); 
		/* End of basic resources */ 

		
		//Boxes creation
		Box mainBox = Box.createVerticalBox();
		Box logo = Box.createHorizontalBox();
		Box texte = Box.createVerticalBox();
		Box newID = Box.createHorizontalBox();
		Box msgError = Box.createHorizontalBox(); //TO BE DONE

		logo.add(image);
		mainBox.add(logo);
		
		JLabel textLabelPadding	= new JLabel("padding");
		textLabelPadding.setForeground(Color.white);
		textLabelPadding.setFont(new java.awt.Font("CALIBRI",Font.PLAIN,15));
		JLabel textLabel	= new JLabel("Your last username is unavailable, please enter a new one.");
		textLabel.setFont(new java.awt.Font("CALIBRI",Font.PLAIN,17));
		textLabel.setForeground(myBlue);
		textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		texte.add(textLabelPadding);
		texte.add(textLabel);
		mainBox.add(texte);

		JTextField newIDF = new JTextField("Enter your new username...", 30);
		newIDF.setPreferredSize(new Dimension(7,25));
		newIDF.setMaximumSize(newIDF.getPreferredSize());
		newIDF.setFont(new Font("CALIBRI", Font.PLAIN, 15));
		newIDF.getFont().deriveFont(Font.ITALIC);
		newIDF.setBackground(Color.white);
		Border borderG = BorderFactory.createLineBorder(Color.gray, 2);
		Border borderB = BorderFactory.createLineBorder(myBlue, 2);
        	newIDF.setBorder(borderG);

	

		Border down = BorderFactory.createMatteBorder(0, 0, 1, 0, MainWindow.foregroundColor);
		Border downBlue = BorderFactory.createMatteBorder(0, 0, 1, 0, myBlue);
		Border borderW = BorderFactory.createMatteBorder(1, 1, 0, 1, MainWindow.backgroundColor);
		Border borderTextField = BorderFactory.createCompoundBorder(down, borderW);
		Border borderTextField2 = BorderFactory.createCompoundBorder(downBlue, borderW);
		newIDF.setBorder(borderTextField);


		newID.add(newIDF);
		newIDF.addMouseListener(new MouseListener() {           
			@Override
		   	public void mouseReleased(MouseEvent e) {}         
		   	@Override
		   	public void mousePressed(MouseEvent e) {}          
		    	@Override
		    	public void mouseExited(MouseEvent e) {
				newIDF.setBorder(borderTextField);
			}           
		    	@Override
		    	public void mouseEntered(MouseEvent e) {
				newIDF.setBorder(borderTextField2);
			}          
		    	@Override
		    	public void mouseClicked(MouseEvent e) {
					JTextField newIDF = ((JTextField)e.getSource());
					newIDF.setText("");
					newIDF.getFont().deriveFont(Font.PLAIN);
					newIDF.setForeground(MainWindow.foregroundColor);
		    }
		});
		
		

		/* Button settings */
		JButton button = new JButton(mouseExited);
           	button.setBorder(BorderFactory.createLineBorder(Color.white));
		newID.add(button);

		button.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
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

		newIDF.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					e.consume();
					button.doClick();
				}
			}
			public void keyTyped(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}
		});

		mainBox.add(newID);


		this.add(mainBox);
	}
			
		
	public static void main(String[] args) {
		NewIDWindow niw = new NewIDWindow();
		niw.setVisible(true);
	}
}

		

     


