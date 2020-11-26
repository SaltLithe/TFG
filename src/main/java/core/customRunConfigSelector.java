package core;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import userInterface.runConfigDialog;
import userInterface.runConfigDialog;

public class customRunConfigSelector  extends JPanel{
	
	private customRunConfigSelector  instance = this; 
	public String name;
	
	
 public	customRunConfigSelector(String name, runConfigDialog parent) {
 	
	 
	 
	 this.name = name; 
 	JLabel nameLabel = new JLabel(name);
 	add(nameLabel);
	 
 	setPreferredSize(this.getPreferredSize());
	setOpaque(false);
	setBackground(Color.blue);
	 
	addMouseListener(new MouseListener() {

		@Override
		public void mouseClicked(java.awt.event.MouseEvent arg0) {
			
			parent.clickedOption(name);
		
			
		}

		@Override
		public void mouseEntered(java.awt.event.MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(java.awt.event.MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(java.awt.event.MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(java.awt.event.MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

			

		});
		
	 
	 
 }


public void setUnselected() {

	this.setOpaque(true);
	
} 

public void setSelected() {
	this.setOpaque(false);
}



}
