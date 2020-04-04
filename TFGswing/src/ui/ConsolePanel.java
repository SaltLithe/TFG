package ui;
import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

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
		// TODO Auto-generated method stub

	}

}
