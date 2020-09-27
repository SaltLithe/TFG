package TFG;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/*Clase sencilla que contiene un area de texto donde se escriben los resultados de la ejecución del código
 * del usuario , solo tiene un método que cambia este texto 
 */

public class ConsolePanel extends JPanel implements PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea consoleTextArea;
	private boolean readingListener;
	private UIController uiController;
	private DeveloperComponent developerComponent;
	int lastlenght;

	public ConsolePanel() {
		readingListener = false;
		consoleTextArea = new JTextArea();

		uiController = UIController.getInstance();
		developerComponent = uiController.getDeveloperComponent();

		setLayout(new BorderLayout());
		add(new JScrollPane(consoleTextArea), BorderLayout.CENTER);

		consoleTextArea.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (readingListener) {

						String retrieved = consoleTextArea.getText().substring(lastlenght,
								consoleTextArea.getText().length());

						uiController.run(() -> developerComponent.reactivateRunningProccess(retrieved));
						readingListener = false;
						consoleTextArea.setEnabled(false);
					}
				}

			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}

		});

	}

	public void setContents(String results) {

		consoleTextArea.setText(consoleTextArea.getText() + results);

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		ObserverActions action = ObserverActions.valueOf(evt.getPropertyName());
		DEBUG.debugmessage("salta salta");
		switch (action) {
		case CONSOLE_PANEL_CONTENTS:
			@SuppressWarnings("unchecked")
			ArrayList<Object> results = (ArrayList<Object>) evt.getNewValue();
			this.setContents((String) results.get(0));
			break;
		case ENABLE_READING_LISTENER:
			readingListener = true;
			lastlenght = consoleTextArea.getText().length();
			break;
		case ENABLE_CONSOLE_PANEL:
			consoleTextArea.setEnabled(true);
			break;
		default:
			break;
		}

	}

}
