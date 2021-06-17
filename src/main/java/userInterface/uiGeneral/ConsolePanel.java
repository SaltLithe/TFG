package userInterface.uiGeneral;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import commandController.CommandController;
import core.DEBUG;
import observerController.ObserverActions;
import javax.swing.JCheckBox;

/**
 * UI class serving as the console for the user , uses a JPanel to write text
 * into it and read text from user as input
 * 
 * @author Carmen Gómez Moreno
 *
 */
@SuppressWarnings("serial")
public class ConsolePanel extends JPanel implements PropertyChangeListener {

	private JTextArea consoleTextArea;
	private boolean readingListener;
	int lastlenght;
	private JCheckBox clearCheck;

	public ConsolePanel() {
		readingListener = false;
		consoleTextArea = new JTextArea();
		setLayout(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(consoleTextArea);
		add(scrollPane, BorderLayout.CENTER);
		
		clearCheck = new JCheckBox("Clear console on run");
		clearCheck.setSelected(true);
		scrollPane.setColumnHeaderView(clearCheck);

		// Detect when the user has pressed enter, determine the lenght of the input and
		// send it to the console
		// input stream , we only need to override this part of the key listener
		consoleTextArea.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {

				if ((e.getKeyCode() == KeyEvent.VK_ENTER) && readingListener) {
					

						String retrieved = consoleTextArea.getText().substring(lastlenght,
								consoleTextArea.getText().length());

						CommandController.developerComponent.reactivateRunningProccess(retrieved);
						readingListener = false;
						consoleTextArea.setEnabled(false);
					
				}

			}

			@Override
			public void keyTyped(KeyEvent e) {
				//We dont need to implement this method
			}

			@Override
			public void keyPressed(KeyEvent e) {
				//We dont need to implement this method

			}

		});

	}

	/**
	 * Add Contents to the console when the running process writes output
	 * 
	 * @param results : The output from the running process
	 */
	public void setContents(String results) {
		
		consoleTextArea.setText(consoleTextArea.getText() + results);
	}

	/**
	 * Implementation of propertyChange from PropertyChangeListener to listen to ui
	 * notifications
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		ObserverActions action = ObserverActions.valueOf(evt.getPropertyName());
		@SuppressWarnings("unchecked")
		ArrayList<Object> results = (ArrayList<Object>) evt.getNewValue();

		switch (action) {
		case CONSOLE_PANEL_CONTENTS:
		
			this.setContents((String) results.get(0));
			break;
		case ENABLE_READING_LISTENER:
			readingListener = true;
			lastlenght = consoleTextArea.getText().length();
			break;
		case ENABLE_CONSOLE_PANEL:
			consoleTextArea.setEnabled(true);
			break;
		case CLEAR_CONSOLE:
			if(clearCheck.isSelected()){
			consoleTextArea.setText("");
			}
			break; 
		default:
			break;
		}

	}

}
