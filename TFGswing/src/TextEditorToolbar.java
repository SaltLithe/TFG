
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import ui.ConsolePanel;
import ui.TextEditorPanel;

public class TextEditorToolbar extends JPanel {

	private TextEditorPanel textEditorPanel;
	private JButton runLocal;
	private JButton runGlobal;
	private JButton terminate;
	private DeveloperComponent developerComponent;
	private JSplitPane consoleDivision;
	private ConsolePanel consolePanel;
	private Thread runner;
	private Thread observer;

//
	public TextEditorToolbar(DeveloperComponent dp) {

		textEditorPanel = new TextEditorPanel();
		setLayout(new BorderLayout());
		developerComponent = dp;
		runLocal = new JButton("Run Locally");
		runGlobal = new JButton("Run Globally");
		terminate = new JButton("Terminate Process");
		terminate.setEnabled(false);
		runLocal.setEnabled(false);
		runGlobal.setEnabled(false);
		JPanel toolbarArea = new JPanel();
		toolbarArea.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolbarArea.add(runLocal);
		toolbarArea.add(runGlobal);
		toolbarArea.add(terminate);

		consolePanel = new ConsolePanel();

		add(toolbarArea, BorderLayout.NORTH);
		consoleDivision = new JSplitPane(JSplitPane.VERTICAL_SPLIT, textEditorPanel, consolePanel);
		consoleDivision.setDividerLocation(500);
		add(toolbarArea, BorderLayout.NORTH);
		add(consoleDivision, BorderLayout.CENTER);

		runLocal.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				runLocal();

			}

		});

		runGlobal.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Mandar codigo al dueño para que ejecute codigo de compilacion global
				// Con todo lo que eso conlleva

			}

		});

		terminate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// Si se esta ejecutando codigo local se activa este boton
				// Matar al hilo de ejecucion de codigo local

			}

		});

	}

	private void runLocal() {
		String results = developerComponent.runLocal();
		consolePanel.setContents(results);
	}

	public void enableButtons() {
		// TODO Auto-generated method stub
		runLocal.setEnabled(true);
		runGlobal.setEnabled(true);
		terminate.setEnabled(true);
	}

	public String getContents() {
		// TODO Auto-generated method stub

		return textEditorPanel.getContents();

	}

	public void setContents(String newcontents) {
		// TODO Auto-generated method stub
		textEditorPanel.setContents(newcontents);

	}

	public void enableEditor() {
		textEditorPanel.enableEditor();
		// TODO Auto-generated method stub

	}

}
