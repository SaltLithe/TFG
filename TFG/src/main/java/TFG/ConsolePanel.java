package TFG;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/*Clase sencilla que contiene un area de texto donde se escriben los resultados de la ejecución del código
 * del usuario , solo tiene un método que cambia este texto 
 */

public class ConsolePanel extends JPanel {

	private JTextArea consoleTextArea;

	public ConsolePanel() {
		consoleTextArea = new JTextArea();

		setLayout(new BorderLayout());
		add(new JScrollPane(consoleTextArea), BorderLayout.CENTER);

	}

	public void setContents(String results) {
		consoleTextArea.setText("");
		consoleTextArea.setText(results);

	}

}
