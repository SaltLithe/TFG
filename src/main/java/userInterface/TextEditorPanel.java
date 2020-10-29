package userInterface;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.TextArea;
import java.awt.image.ImageObserver;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import core.DEBUG;
import core.DeveloperComponent;
import network.WriteMessage;

/*Clase que contiene el editor de texto usando un elemento del tipo RSyntaxTextArea 
 * Tiene tambien una referencia al focus actual que indica el archivo sobre el que se está
 * trabajando actualmente en la aplicación
 * 
 */
@SuppressWarnings("serial")
public class TextEditorPanel extends JPanel implements PropertyChangeListener {

	
	
	
	private RSyntaxTextArea textEditorArea;
	private RTextScrollPane textEditorScrollPane;
	private JTabbedPane tabPane;

	private String focus = null;

	private int newCaretPos;
	private TextEditorToolbar toolbar;
	private UIController uicontroller;
	private DeveloperComponent developerComponent;

	private Color dark = new Color(70, 70, 70);

	// Activa el editor de texto
	public void enableTextEditorArea() {

		textEditorArea.setEnabled(true);
	}

	// Metodo para poner el focus del editor
	public void setFocus(String name) {

		this.focus = name;
	}

	// Metodo para recuperar el focus actual
	public String getFocus() {

		return focus;
	}

	// Metodo para establecer los contenidos del editor de forma correcta cambiando
	// el focus actual


	public TextEditorPanel(TextEditorToolbar toolbar) {

		uicontroller = UIController.getInstance();
		developerComponent = uicontroller.getDeveloperComponent();

		setLayout(new BorderLayout());
		this.setSize(new Dimension(ImageObserver.WIDTH, ImageObserver.HEIGHT));

		this.toolbar = toolbar;
		tabPane = new JTabbedPane();
		
		
	
		
		TextEditorTab tab = new TextEditorTab();
		tabPane.addTab("Tab de prueba", tab.panel);
		
		TextEditorTab tab2 = new TextEditorTab();
		tabPane.addTab("Tab de prueba", tab2.panel);

		this.add(tabPane,BorderLayout.CENTER);
		
		tabPane.setVisible(true);
		
		
		// Trackear el caret
	
		
		

		
		this.setVisible(true);
	}

	// Metodo que recupera los contenidos del editor de texto



	public String getContents() {

		return textEditorArea.getText();
	}

	// Metodo para activar el editor de texto
	public void enableEditor() {
		textEditorArea.setEnabled(true);

	}

	public void updateContents(int caret, String added) {

		textEditorArea.insert(added, caret);

	}

	public void updateContents(int caret, int lenght) {

		DEBUG.debugmessage("DELETED THE FOLLOWING : " + textEditorArea.getText().substring(caret, caret + lenght));
		textEditorArea.replaceRange(null, caret, caret + lenght);

	}

	@SuppressWarnings("unchecked")
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		ObserverActions action = Enum.valueOf(ObserverActions.class, evt.getPropertyName());
		ArrayList<Object> results;
		switch (action) {

		case ENABLE_TEXT_EDITOR:
			enableEditor();
			break;
		case UPDATE_PANEL_CONTENTS:
			enableEditor();
			results = (ArrayList<Object>) evt.getNewValue();
			boolean adding = (boolean) results.get(2);
			
			//TO-DO PREPARAR ESTRUCTURA DE TABS
			//messageWrite = true;
			if (adding) {
				int caret = (int) results.get(0);
				String added = (String) results.get(1);
				updateContents(caret, added);
			} else if (!adding) {
				int caret = (int) results.get(0);
				int lenght = (int) results.get(1);
				updateContents(caret, lenght);
			}
			break;
		case SET_TEXT_CONTENT:
			results = (ArrayList<Object>) evt.getNewValue();
			String newcontents = (String) results.get(0);
			String filename = (String) results.get(1);

			//TO-DO PREPARAR ESTRUCTURA MISMO PROBLEMA
			//setTextEditorCode(newcontents, filename);
			DEBUG.debugmessage("Jamas cambieis vuestra arquitectura lo ultimo");
			break;
		default:
			break;
		}
	}

}