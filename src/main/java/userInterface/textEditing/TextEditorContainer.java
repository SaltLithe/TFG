package userInterface.textEditing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import core.DEBUG;
import core.DeveloperComponent;
import network.WriteMessage;
import userInterface.ConsolePanel;
import userInterface.DeveloperMainFrame;
import userInterface.ObserverActions;
import userInterface.UIController;

/*Clase que contiene los botones del menu asociado al editor de texto y que implementa sus
 * funcionalidades 
 * Qedan por implementar las funciones de ejecucion global y detener la ejecucion
 */
@SuppressWarnings("serial")
public class TextEditorContainer extends JPanel implements PropertyChangeListener {

	public TextEditorPanel textEditorPanel;
	
	private JSplitPane consoleDivision;
	public ConsolePanel consolePanel;
	String currentTabName = null;
	String currentProject = null; 

	private UIController uiController;
	private DeveloperComponent developerComponent;
	private DeveloperMainFrame developerMainFrame;

	public TextEditorContainer(DeveloperMainFrame developerMainFrame) {

		DEBUG.debugmessage("SE HA INVOCADO EL CONSTRUCTOR DE TEXTEDITORTOOLBAR");
		textEditorPanel = new TextEditorPanel(this);
		this.developerMainFrame = developerMainFrame;
		uiController = UIController.getInstance();
		developerComponent = uiController.getDeveloperComponent();
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

	// Metodo que llama al metodo run de developer component para ejecutar el codigo
	// y recupera los resultados
	// devueltos tras la ejecucion que son mostrados en el panel de consola
	// ARQUITECTURA A CONTROLLER CAMBIADA
	private void runLocal() {
		DEBUG.debugmessage("SE HA LLAMADO A RUNLOCAL EN TEXTEDITORTOOLBAR");
		String contents = developerMainFrame.getEditorPanelContents();
		//uiController.run(() -> developerComponent.runLocal(contents));

	}


	// Metodo que sirve para recuperar los contenidos del editor
	public String getContents() {
		DEBUG.debugmessage("SE HA LLAMADO A GETCONTENTS EN TEXTEDITORTOOLBAR");
		return textEditorPanel.getContents(currentTabName);

	}

	protected void sendTextUpdate(WriteMessage message) {

		uiController.run(() -> developerComponent.sendMessageToEveryone(message));

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		ObserverActions action = ObserverActions.valueOf(evt.getPropertyName());
		switch (action) {
		case ENABLE_TEXTEDITORTOOLBAR_BUTTONS:
			break;
			
		case CHANGE_TAB_FOCUS:
			DEBUG.debugmessage("CHANGING FOCUS");
			ArrayList<Object> list = (ArrayList<Object>) evt.getNewValue();
			currentTabName = (String)list.get(0);
			currentProject = (String)list.get(1);
			break; 
		default:
			break;
		}

	}

	public String getCurrentTabName() {

		return currentTabName;
	}

}
