package userInterface;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import core.DEBUG;
import core.DeveloperComponent;
import core.URLData;
import core.customRunConfigSelector;

public class runConfigDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private customRunConfigSelector lastSelected;
	private HashMap<String, customRunConfigSelector> selectorCollection;
	private JScrollPane scrollPane; 
	


	JButton ApplyButton;
	JButton ApplyRunButton;
	JButton CancelButton;
	private JLabel lblNewLabel;
	JPanel panel;
	
	private void fillSelectors(URLData[] classes) {
		Dimension d = new Dimension(0,0);
		panel.add(Box.createRigidArea(new Dimension(0,5)));
		
		for(URLData data : classes) {
			customRunConfigSelector newselector = new customRunConfigSelector(data.path, this);
			selectorCollection.put(data.path, newselector);
			newselector.setAlignmentX(0.0f);
			newselector.setAlignmentY(Component.TOP_ALIGNMENT);
			
			panel.add(newselector);
			panel.add(new Box.Filler(d, d, d));

			
		}
		scrollPane.updateUI();
	}
	
	
	public runConfigDialog(DeveloperComponent developerComponent , URLData[] classes) {
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setModalityType(ModalityType.APPLICATION_MODAL);
		selectorCollection = new HashMap<String,customRunConfigSelector>();
		lastSelected = null;
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		panel = new JPanel(); 
		scrollPane = new JScrollPane(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		contentPanel.add(scrollPane, BorderLayout.CENTER);
		
		lblNewLabel = new JLabel("Select a class with a main method");
		scrollPane.setColumnHeaderView(lblNewLabel);
		
		
		
		fillSelectors(classes);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			
			ApplyRunButton = new JButton("Apply and Run");
			buttonPane.add(ApplyRunButton);
			{
				ApplyButton = new JButton("Apply");
				ApplyButton.setActionCommand("OK");
				buttonPane.add(ApplyButton);
				getRootPane().setDefaultButton(ApplyButton);
			}
			{
				CancelButton = new JButton("Cancel");
				CancelButton.setActionCommand("Cancel");
				buttonPane.add(CancelButton);
			}
		}
		ApplyRunButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(lastSelected != null) {
					developerComponent.updateFocusedPair(lastSelected.name); 
					//developerComponent.run();
					}
			

			}

		});

		
		ApplyButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(lastSelected != null) {
				developerComponent.updateFocusedPair(lastSelected.name); 
				}
				
			

			}

		});

		CancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				dispose(); 
			

			}

		});

		
		
		
		
		
		setVisible(true);
	}


	public void clickedOption(String code) {

		
		DEBUG.debugmessage("PARENT NOTIFIED");
		if(lastSelected!= null) {
			lastSelected.setUnselected();
		}
		lastSelected = selectorCollection.get(code);
		lastSelected.setSelected();
		
	}


	
}
