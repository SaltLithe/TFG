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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import core.CustomRunConfigSelector;
import core.DEBUG;
import core.URLData;

/**
 * Class that opens a dialog for the user to select the entrypoint class , uses
 * CustomRunConfigSelector items for the user to select an entrypoint
 * 
 * @author Carmen Gómez Moreno
 *
 */
@SuppressWarnings("serial")
public class RunConfigDialog extends JDialog {

	JButton ApplyButton;
	JButton ApplyRunButton;
	JButton CancelButton;
	JPanel panel;

	private final JPanel contentPanel = new JPanel();
	private CustomRunConfigSelector lastSelected;
	private HashMap<String, CustomRunConfigSelector> selectorCollection;
	private JScrollPane scrollPane;
	private JLabel lblNewLabel;

	public RunConfigDialog(URLData[] classes, boolean global) {
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setModalityType(ModalityType.APPLICATION_MODAL);
		selectorCollection = new HashMap<String, CustomRunConfigSelector>();
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

				if (lastSelected != null) {
					if (!global) {
						UIController.developerComponent.updateFocusedPair(lastSelected.name);
						UIController.developerComponent.startLocalRunningThread();
					} else {
						UIController.developerComponent.updateFocusedPair(lastSelected.name);
						UIController.developerComponent.startRunGlobal(false);
					}
					dispose();

				}

			}

		});

		ApplyButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (lastSelected != null) {
					UIController.developerComponent.updateFocusedPair(lastSelected.name);
				}

			}

		});

		CancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				dispose();

			}

		});

		if (global) {
			DEBUG.debugmessage("THIS IS A GLOBAL VERSION");
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

			ApplyButton.setEnabled(false);
			CancelButton.setEnabled(false);
			this.getRootPane().updateUI();

		}
		setVisible(true);
	}

	/**
	 * Method used by the components representing the entrypoint class to indicate
	 * that they have been selected
	 * 
	 * @param code : The code of the component that has been selected
	 */
	public void clickedOption(String code) {

		if (lastSelected != null) {
			lastSelected.setUnselected();
		}
		lastSelected = selectorCollection.get(code);
		lastSelected.setSelected();

	}

	/**
	 * Method to create the classes representing the class files that comprise the
	 * project
	 * 
	 * @param classes : The class file paths from the project
	 */
	private void fillSelectors(URLData[] classes) {

		Dimension d = new Dimension(0, 0);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));

		// For each class , create a selector and add it
		for (URLData data : classes) {
			CustomRunConfigSelector newselector = new CustomRunConfigSelector(data.path, this);
			selectorCollection.put(data.path, newselector);
			newselector.setAlignmentX(0.0f);
			newselector.setAlignmentY(Component.TOP_ALIGNMENT);

			panel.add(newselector);
			panel.add(new Box.Filler(d, d, d));

		}
		scrollPane.updateUI();
	}

}
