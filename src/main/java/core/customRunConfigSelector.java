package core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import userInterface.runConfigDialog;

public class customRunConfigSelector  extends JPanel{
	
	public String name;
	public runConfigDialog parent; 
	
	JLabel nameLabel;
	
	
 public	customRunConfigSelector(String name, runConfigDialog parent) {
	 this.parent = parent; 
 	FlowLayout flowLayout = (FlowLayout) getLayout();
 	flowLayout.setAlignment(FlowLayout.LEFT);
 	
	 
	 
	 this.name = name; 
 	nameLabel = new JLabel(name);
 	nameLabel.setForeground(Color.white);
 	add(nameLabel);
	 
 	
 
 	setMinimumSize(new Dimension(parent.getWidth(), 25));
 	setPreferredSize(new Dimension(parent.getWidth(), 25));
 	setMaximumSize(new Dimension(parent.getWidth(),25));

 	setOpaque(false);
	 
	addMouseListener(new MouseListener() {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			DEBUG.debugmessage("CLICKED ON RUN CONFIG SELECTOR");
			customRunConfigSelector component = (customRunConfigSelector) arg0.getComponent();
			component.parent.clickedOption(name);
		
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

			

		});
		
	 
	 
 }


public void setUnselected() {
	DEBUG.debugmessage("SETTING DESELECTED");

	setOpaque(false);
	nameLabel.setForeground(Color.white);

	updateUI();
	
} 

public void setSelected() {
	DEBUG.debugmessage("SETTING SELECTED");
	setOpaque(true);
	setBackground(Color.lightGray);
	nameLabel.setForeground(Color.black);
	
	updateUI(); 

}



}
