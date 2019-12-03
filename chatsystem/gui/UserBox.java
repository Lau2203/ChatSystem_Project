package chatsystem.gui;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
@SuppressWarnings("serial")

public class UserBox extends Box {

	public UserBox() {
		super(BoxLayout.X_AXIS);
		Box mainBox = Box.createHorizontalBox();
		Box rightBox = Box.createVerticalBox();
	
		JLabel userName = new JLabel("Paul Hémique");
		userName.setFont(new java.awt.Font("CALIBRI",Font.BOLD,13));
		rightBox.add(userName);
		JLabel padding = new JLabel("\n");
		rightBox.add(padding);
		JLabel lastMsg = new JLabel("I spoke to our IT manager about the Chat System project and");	
		rightBox.add(lastMsg);

		//User Image
		ImageIcon user = new ImageIcon("../resources/images/active.png");
		JLabel userL = new JLabel(user); 
		

		mainBox.add(userL);
		mainBox.add(rightBox);
		this.setVisible(true);
	}

	
	/*public UserBox create() {
		Box box = Box.createHorizontalBox();
		box.
	}*/
	

}
