package userInterface;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import core.DeveloperComponent;
import core.URLData;
import core.customRunConfigSelector;
import java.awt.Dialog.ModalityType;
import java.awt.Dialog.ModalExclusionType;

public class runConfigDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private customRunConfigSelector lastSelected;
	private HashMap<String, customRunConfigSelector> selectorCollection;
	


	JButton ApplyButton;
	JButton ApplyRunButton;
	JButton CancelButton;
	JPanel panel;
	
	private void fillSelectors(URLData[] classes) {
		
		
		for(URLData data : classes) {
			customRunConfigSelector newselector = new customRunConfigSelector(data.path, this);
			selectorCollection.put(data.path, newselector);
			panel.add(newselector);
		}
		
		panel.setSize(panel.getMaximumSize());
		panel.updateUI();
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
		contentPanel.setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(21, 36, 353, 181);
		contentPanel.add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		
		
		fillSelectors(classes);
		
		JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.setBounds(390, 33, -361, 172);
		contentPanel.add(scrollPane);
		
	
		
		JLabel lblNewLabel = new JLabel("Select a class with a main method");
		lblNewLabel.setBounds(21, 11, 353, 14);
		contentPanel.add(lblNewLabel);
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
				
				
			

			}

		});

		
		ApplyButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				developerComponent.updateFocusedPair(lastSelected.name); 
				
			

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

		if(lastSelected!= null) {
			lastSelected.setUnselected();
		}
		lastSelected = selectorCollection.get(code);
		lastSelected = setSelected();
		
	}


	private customRunConfigSelector setSelected() {
		// TODO Auto-generated method stub
		return null;
	}
}
