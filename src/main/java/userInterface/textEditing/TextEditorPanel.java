package userInterface.textEditing;

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
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JLabel;
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
import userInterface.ObserverActions;
import userInterface.UIController;

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
	private HashMap <String,TextEditorTab> tabCollection;

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
		
		tabCollection = new HashMap<String,TextEditorTab>(); 
		

		setLayout(new BorderLayout());
		this.setSize(new Dimension(ImageObserver.WIDTH, ImageObserver.HEIGHT));

		this.toolbar = toolbar;
		tabPane = new JTabbedPane();
		tabPane.setTabLayoutPolicy(1);
		
		
		int numPruebas = 20 ; 

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
	
	public void addTab(String name , String path) {
		
		TabMiniPanel mp1 = new TabMiniPanel(name,path);
        TextEditorTab tab = new TextEditorTab(path,mp1);
        mp1.setParent(tab);
		tabPane.addTab("", tab.panel);
		int index = tabPane.indexOfComponent(tab.panel);
		tabPane.setTabComponentAt(index, mp1);
		this.tabCollection.put(path, tab);
		
		
		
		
		
	}
	
	public void setFullText(String path , String contents) {
		this.tabCollection.get(path).setTextEditorCode(contents);
	}

	

	@SuppressWarnings("unchecked")
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		ObserverActions action = Enum.valueOf(ObserverActions.class, evt.getPropertyName());
		ArrayList<Object> results;
		switch (action) {

		case ENABLE_TEXT_EDITOR:
		//	enableEditor();
			break;
		case UPDATE_PANEL_CONTENTS:
		//	enableEditor();
			results = (ArrayList<Object>) evt.getNewValue();
			String editingpath = (String) results.get(3);
			
			this.tabCollection.get(editingpath).updateContents(results);
			
		
			break;
		case SET_TEXT_CONTENT:
			results = (ArrayList<Object>) evt.getNewValue();
			String path = (String) results.get(2);

			if(!tabCollection.containsKey(path)) {
			String contents = (String) results.get(0);
			String filename = (String) results.get(1);
			
			this.addTab(filename, path);
			this.setFullText(path, contents);
		
			}
			break;
		case CLOSE_TAB:
			results = (ArrayList<Object>) evt.getNewValue();
			String closingpath = (String) results.get(0);
			if(this.tabCollection.containsKey(closingpath)) {
				TextEditorTab removing = tabCollection.get(closingpath);
				int closingindex = tabPane.indexOfComponent(removing.panel);
				tabPane.remove(closingindex);
				this.tabCollection.remove(closingpath);
				
			}
			break;
		case SAVED_SINGLE_FILE:
			
			results = (ArrayList<Object>) evt.getNewValue();
			String savingpath = (String) results.get(0);
			if(tabCollection.containsKey(savingpath)) {
			tabCollection.get(savingpath).setAsSaved(); 
			tabCollection.get(savingpath).miniPanel.setAsSaved();			
			}
			break; 
		default:
			break;
		}
	}

	public void CloseTab(String path) {
		this.tabPane.remove(tabPane.indexOfComponent(tabCollection.get(path).panel));
		
	}

}