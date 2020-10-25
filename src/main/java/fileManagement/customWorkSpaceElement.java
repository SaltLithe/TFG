package fileManagement;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.io.File;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import core.DEBUG;
import userInterface.workSpaceSelect;

import java.awt.Component;

public class customWorkSpaceElement extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String path;
	private JButton deleteLocateButton;
	private JLabel nameLabel;

	private Color notFoundColor;
	private Color foundColor; 
	public int tempID; 
	
	
	private int forcedHeight = 30;
	private int forcedWidth = 0; 
	private WorkSpaceManager wsm; 
	private boolean canOpen; 
	
	private workSpaceSelect parent ; 
	private JFrame frame; 

	public customWorkSpaceElement(String name, String path, workSpaceSelect parent,JFrame frame) {

		wsm = WorkSpaceManager.getInstance(); 
		this.parent = parent; 
		this.frame = frame; 
		this.name = name;
		this.path = path;
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		notFoundColor = new Color(222, 57, 49);
		
		
		
		nameLabel = new JLabel(name + ": " + path);
		foundColor = nameLabel.getForeground();

		deleteLocateButton = new JButton("");
		add(deleteLocateButton);

		nameLabel = new JLabel(name + ": " + path);
		//this.setSize(this.getPreferredSize());

		checkExistance();

		nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
		nameLabel.setAlignmentX(0.5f);

		deleteLocateButton.setHorizontalAlignment(SwingConstants.LEFT);
		deleteLocateButton.setAlignmentX(0.5f);
		deleteLocateButton.setText("Delete");

		
		deleteLocateButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				
			
					
					Object[] options = {"Delete",
		                    "Cancel"};
		int n = JOptionPane.showOptionDialog(frame,
		 "Delete this Workspace from the list? This won't delete this workspace from your system. You can delete this workspace from your system manually.",
		    "Confirm delete",
		    JOptionPane.OK_CANCEL_OPTION,
		    JOptionPane.QUESTION_MESSAGE,
		    null,
		    options,
		    options[1]);
		if(n == JOptionPane.OK_OPTION) {
			

			 wsm.deleteWorkSpace(tempID,name, frame); 
			parent.refresh(); 
		}
					
			

				
				}
				

			

			

		});

		this.setAlignmentX(Component.LEFT_ALIGNMENT);

		Font font = nameLabel.getFont();
		Map attributes = font.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		nameLabel.setFont(font.deriveFont(attributes));
		
		
		nameLabel.addMouseListener(new MouseAdapter()  
		{  
		    public void mouseClicked(MouseEvent e)  
		    {  
		    	if(!canOpen) {
		    		
		    		
					Object[] options = {"Locate",
		                    "Cancel"};
		int n = JOptionPane.showOptionDialog(frame,
		 "This WorkSpace could not be located, would you like to locate it?.",
		    "WorkSpace not found",
		    JOptionPane.OK_CANCEL_OPTION,
		    JOptionPane.QUESTION_MESSAGE,
		    null,
		    options,
		    options[1]);
		
		
		if(n == JOptionPane.OK_OPTION) {
		  
		    	
		    	String path = wsm.getFilePath();
				if (path != null) {
					
					String search = "\\";
					int index = path.lastIndexOf(search);
					String subname = path.substring(index + 1, path.length());
					
					
					boolean results = 	wsm.locateWorkSpace(path,name,frame);
					
					if(results) {
						nameLabel.setForeground(foundColor);
						canOpen = true;
						parent.refresh();					
					}
									
									
									
				}
		    	}
		    	}
		    	else {
		    		
		    		
		    		wsm.startMainApp(tempID,frame);
		    	}
		    }  
		}); 
		
		
		add(nameLabel);
		
		forcedWidth = this.getPreferredSize().width;
		this.setMinimumSize(new Dimension(forcedWidth,forcedHeight));
		this.setPreferredSize(new Dimension(forcedWidth,forcedHeight));
		this.setMaximumSize(new Dimension(forcedWidth,forcedHeight));
		

		this.setVisible(true);

	}

	private void checkExistance() {

		File workspacefile = new File(path);
		if (workspacefile.exists()) {
			canOpen = true; 

		} else {
			
			nameLabel.setForeground(notFoundColor);
			canOpen = false; 

		}

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
