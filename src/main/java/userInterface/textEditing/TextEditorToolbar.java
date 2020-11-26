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
public class TextEditorToolbar extends JPanel implements PropertyChangeListener {

	public TextEditorPanel textEditorPanel;
	private JButton runLocal;
	private JButton runGlobal;
	private JButton terminate;
	private JSplitPane consoleDivision;
	public ConsolePanel consolePanel;
	String currentTabName = null;
	String currentProject = null; 

	private UIController uiController;
	private DeveloperComponent developerComponent;
	private DeveloperMainFrame developerMainFrame;

	public TextEditorToolbar(DeveloperMainFrame developerMainFrame) {

		DEBUG.debugmessage("SE HA INVOCADO EL CONSTRUCTOR DE TEXTEDITORTOOLBAR");
		textEditorPanel = new TextEditorPanel(this);
		this.developerMainFrame = developerMainFrame;
		uiController = UIController.getInstance();
		developerComponent = uiController.getDeveloperComponent();
		setLayout(new BorderLayout());
		runLocal = new JButton("Run Locally");
		runGlobal = new JButton("Run Globally");
		terminate = new JButton("Terminate Process");
		terminate.setEnabled(false);
		runLocal.setEnabled(false);
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
				DEBUG.debugmessage("SE HA PULSADO EL BOTON RUNLOCAL");
				runLocal();

			}

		});

		runGlobal.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				uiController.run(()->{
					try {
						developerComponent.run(currentTabName);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				});

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

	// Metodo que llama al metodo run de developer component para ejecutar el codigo
	// y recupera los resultados
	// devueltos tras la ejecucion que son mostrados en el panel de consola
	// ARQUITECTURA A CONTROLLER CAMBIADA
	private void runLocal() {
		DEBUG.debugmessage("SE HA LLAMADO A RUNLOCAL EN TEXTEDITORTOOLBAR");
		String contents = developerMainFrame.getEditorPanelContents();
		uiController.run(() -> developerComponent.runLocal(contents));

	}

	// Metodo para activar los botones de este toolbar
	public void enableButtons() {
		runLocal.setEnabled(true);
		runGlobal.setEnabled(true);
		terminate.setEnabled(true);
	}

	// Metodo que sirve para recuperar los contenidos del editor
	public String getContents() {
		DEBUG.debugmessage("SE HA LLAMADO A GETCONTENTS EN TEXTEDITORTOOLBAR");
		return textEditorPanel.getContents();

	}

	protected void sendTextUpdate(WriteMessage message) {

		uiController.run(() -> developerComponent.sendMessageToEveryone(message));

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		ObserverActions action = ObserverActions.valueOf(evt.getPropertyName());
		switch (action) {
		case ENABLE_TEXTEDITORTOOLBAR_BUTTONS:
			enableButtons();
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

}
