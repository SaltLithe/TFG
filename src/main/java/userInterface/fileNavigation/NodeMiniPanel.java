package userInterface.fileNavigation;

import javax.swing.JPanel;

import core.DEBUG;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class NodeMiniPanel extends JPanel  {

	/**
	 * Create the panel.
	 */
	public NodeMiniPanel(String name , String iconpath , Boolean hasFocus , Boolean sel , Boolean leaf ) {

		ImageIcon icon = new ImageIcon (iconpath);
		JLabel iconLabel = new JLabel(icon);
		JLabel nameLabel = new JLabel(name);
		add(iconLabel);
		add(nameLabel);
		
		
		
		setOpaque(false);
		setPreferredSize(getPreferredSize());
		
		
		
		
		
		setVisible(true);
	}



}
