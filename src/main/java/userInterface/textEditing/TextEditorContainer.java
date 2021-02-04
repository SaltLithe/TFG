package userInterface.textEditing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import userInterface.ConsolePanel;
import userInterface.ObserverActions;

/**
 * UI class containing bot the texteditor panel and the console
 * 
 * @author Carmen Gómez Moreno
 *
 */
@SuppressWarnings("serial")
public class TextEditorContainer extends JPanel implements PropertyChangeListener {

	public TextEditorPanel textEditorPanel;

	private JSplitPane consoleDivision;
	public ConsolePanel consolePanel;
	String currentTabName = null;
	String currentProject = null;

	public TextEditorContainer() {

		textEditorPanel = new TextEditorPanel();
		setLayout(new BorderLayout());

		JPanel toolbarArea = new JPanel();
		toolbarArea.setLayout(new FlowLayout(FlowLayout.LEFT));

		consolePanel = new ConsolePanel();

		add(toolbarArea, BorderLayout.NORTH);
		consoleDivision = new JSplitPane(JSplitPane.VERTICAL_SPLIT, textEditorPanel, consolePanel);
		consoleDivision.setDividerLocation(500);
		add(toolbarArea, BorderLayout.NORTH);
		add(consoleDivision, BorderLayout.CENTER);

	}

	/**
	 * Method that returns the contents of an opened tab
	 * 
	 * @return the contents of the requested tab
	 */
	public String getContents() {
		return textEditorPanel.getContents(currentTabName);

	}

	/**
	 * Returns the focused tab name
	 * 
	 * @return the focused tab name
	 */
	public String getCurrentTabName() {

		return currentTabName;
	}

	/**
	 * Returns all of the contents of opened tabs
	 * 
	 * @return a String array containing the contents of the opened tabs
	 */
	public String[] getAllContents() {
		return textEditorPanel.getAllContents();

	}

	/**
	 * Returns all of the names of opened tabs
	 * 
	 * @return a String array containing the contents of the opened tabs
	 */
	public String[] getAllNames() {
		return textEditorPanel.getAllNames();
	}

	/**
	 * Implementation of propertyChange from PropertyChangeListener so this class
	 * can listen to ui notifications
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		ObserverActions action = ObserverActions.valueOf(evt.getPropertyName());
		@SuppressWarnings("unchecked")
		ArrayList<Object> list = (ArrayList<Object>) evt.getNewValue();

		switch (action) {
		case ENABLE_TEXTEDITORTOOLBAR_BUTTONS:
			break;

		case CHANGE_TAB_FOCUS:
			currentTabName = (String) list.get(0);
			currentProject = (String) list.get(1);
			break;
	
		default:
			break;
		}

	}
}
