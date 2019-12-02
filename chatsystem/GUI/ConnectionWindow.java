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

public class ConnectionWindow extends JFrame {
  	Color myBlue = new Color(24, 147, 248);
	public ConnectionWindow()  {    
		super();         
	  	this.setTitle("ConnectionWindow");
	    	this.setSize(600, 600);
	    	this.setLocationRelativeTo(null); 
		this.getContentPane().setBackground(Color.white);
		this.setResizable(false); 			
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);            
	 
		
	
		//Logo
		ImageIcon icon = new ImageIcon("../images/Aura2.jpg");
		JLabel image = new JLabel(icon); 
		
		JPanel paneltext = new JPanel();
		JPanel paneltext1 = new JPanel();
		JPanel paneltext2 = new JPanel();
		

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
 

		//Bouton
		LineBorder noBorder = new LineBorder(Color.WHITE, 3);
		JButton button = new JButton(new ImageIcon("../images/buttongo.jpg"));
		button.setBorder(noBorder);
		paneltext.add(button);
	
		//Changer bouton passage de la souris
		button.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e){}
     			public void mousePressed(MouseEvent e){}
        		public void mouseReleased(MouseEvent e){}
        		public void mouseEntered(MouseEvent e){
				Border b = BorderFactory.createLineBorder(Color.gray);
           			button.setBorder(b);
       			}
			public void mouseExited(MouseEvent e) {
				Border b = BorderFactory.createLineBorder(Color.white);
           			button.setBorder(b);}
			
 
    		});
		//Cursor
		Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
   		Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
		button.addMouseListener(new MouseAdapter() {
         	public void mouseEntered(MouseEvent me) {
            		setCursor(handCursor);
         	}
        	public void mouseExited(MouseEvent me) {
           		setCursor(defaultCursor);
         	}
      		});

		//Forgot Password
		JTextArea labelArea = new JTextArea("Forgot your ID ?  \n \n \n \n \n \n \n \n \n");
		labelArea.setEditable(false);
		labelArea.setOpaque(false);
		paneltext2.add(labelArea);
		paneltext2.setBackground(Color.white);
		labelArea.addMouseListener(new MouseAdapter() {
         	public void mouseEntered(MouseEvent me) {
            		setCursor(handCursor);
         	}
        	public void mouseExited(MouseEvent me) {
           		setCursor(defaultCursor);
         	}
      		});


		this.add(image, BorderLayout.NORTH);
		this.add(paneltext, BorderLayout.CENTER);
		this.add(paneltext2, BorderLayout.SOUTH);
		
  	}       	

	public static void main(String[] args) {
		ConnectionWindow cw = new ConnectionWindow();
		cw.setVisible(true);
	}
}


