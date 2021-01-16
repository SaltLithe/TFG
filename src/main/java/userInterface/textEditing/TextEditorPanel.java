package userInterface.textEditing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.ImageObserver;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.fife.ui.rtextarea.RTextScrollPane;

import core.DEBUG;
import core.DeveloperComponent;
import userInterface.ObserverActions;
import userInterface.PropertyChangeMessenger;
import userInterface.UIController;

/*Clase que contiene el editor de texto usando un elemento del tipo RSyntaxTextArea 
 * Tiene tambien una referencia al focus actual que indica el archivo sobre el que se está
 * trabajando actualmente en la aplicación
 * 
 */
@SuppressWarnings("serial")
public class TextEditorPanel extends JPanel implements PropertyChangeListener {

	private RTextScrollPane textEditorScrollPane;
	private JTabbedPane tabPane;

	private String focus = null;

	private int newCaretPos;
	private TextEditorContainer toolbar;
	private UIController uicontroller;
	private DeveloperComponent developerComponent;
	private HashMap<String, TextEditorTab> tabCollection;
	private PropertyChangeMessenger propertyChangeMessenger;
	private String chosenName = null;

	// Activa el editor de texto
	public void enableTextEditorArea() {

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

	private void setChosenName(String chosenName) {
		this.chosenName = chosenName;
	}
	public TextEditorPanel(TextEditorContainer toolbar) {
		propertyChangeMessenger = PropertyChangeMessenger.getInstance();

		uicontroller = UIController.getInstance();
		developerComponent = uicontroller.getDeveloperComponent();

		tabCollection = new HashMap<String, TextEditorTab>();

		setLayout(new BorderLayout());
		this.setSize(new Dimension(ImageObserver.WIDTH, ImageObserver.HEIGHT));

		this.toolbar = toolbar;
		tabPane = new JTabbedPane();

		tabPane.setTabLayoutPolicy(1);

		this.add(tabPane, BorderLayout.CENTER);

		tabPane.setVisible(true);

		// Trackear el caret

		this.setVisible(true);
	}

	// Metodo que recupera los contenidos del editor de texto

	private void updateContainer(String currentProject, String currentTab) {

		ArrayList<Object> list = new ArrayList<Object>();
		list.add(currentTab);
		list.add(currentProject);

		propertyChangeMessenger.notify(ObserverActions.CHANGE_TAB_FOCUS, null, list);

	}

	public String getContents(String name) {

		return tabCollection.get(name).getContents();
	}

	// Metodo para activar el editor de texto

	public void addTab(String name, String path, String project, String contents) {

		this.developerComponent.setProjectFocus(project);
		TabMiniPanel mp1 = new TabMiniPanel(name, path, project);
		TextEditorTab tab = new TextEditorTab(path, mp1, project , chosenName);
		tab.setTextEditorCode(contents);
		mp1.setParent(tab);

		tabPane.addTab("", tab);
		tabPane.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {

				DEBUG.debugmessage("CAMBIA CAMBIA");
				try {

					JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
					TextEditorTab selected = (TextEditorTab) tabbedPane.getSelectedComponent();
					developerComponent.setProjectFocus(selected.getProject());
					updateContainer(tab.getProject(), tab.getPath());

				} catch (Exception excp) {
				}
			}

		});
		int index = tabPane.indexOfComponent(tab);

		tabPane.setTabComponentAt(index, mp1);

		this.tabCollection.put(path, tab);
		updateContainer(tab.getProject(), tab.getPath());

	}

	public void setFullText(String path, String contents) {
		this.tabCollection.get(path).setTextEditorCode(contents);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		ObserverActions action = Enum.valueOf(ObserverActions.class, evt.getPropertyName());
		ArrayList<Object> results;
		switch (action) {

		case ENABLE_TEXT_EDITOR:
			// enableEditor();
			break;
		case UPDATE_PANEL_CONTENTS:
			// enableEditor();
			synchronized(this) {
			results = (ArrayList<Object>) evt.getNewValue();
			String editingpath = (String) results.get(0);

			String key = findKeyFromPath(editingpath);
			if(key!= null) {
			this.tabCollection.get(key).updateContents(results);
			}
			else {
				//TO-DO warning de que se esta haciendo un update erroneo 
			}
			}
			break;
			
		case UPDATE_HIGHLIGHT:
			DEBUG.clientmessage("Updating highlight");
			results = (ArrayList<Object>)evt.getNewValue();
			String editingpath = (String) results.get(4);
			String key = findKeyFromPath(editingpath);
			if(key != null) {
				int lines = (int)results.get(0);
				int linee = (int)results.get(1);
				int color = -1;
				try {
				color = (int)results.get(2);
				}catch(Exception uwu) {}
				this.tabCollection.get(key).paintHighLight((int)results.get(0),(int)results.get(1),color,(String)results.get(3));
			}
			break;
		case SET_TEXT_CONTENT:
			results = (ArrayList<Object>) evt.getNewValue();
			String path = (String) results.get(2);

			if (!tabCollection.containsKey(path)) {
				String contents = (String) results.get(0);
				String filename = (String) results.get(1);
				String project = (String) results.get(3);

				addTab(filename, path, project, contents);

			}
			break;
		case CLOSE_TAB:
			results = (ArrayList<Object>) evt.getNewValue();
			String closingpath = (String) results.get(0);
			if (this.tabCollection.containsKey(closingpath)) {
				TextEditorTab removing = tabCollection.get(closingpath);
				int closingindex = tabPane.indexOfComponent(removing);
				tabPane.remove(closingindex);
				this.tabCollection.remove(closingpath);

			}
			break;
		case SAVED_SINGLE_FILE:

			results = (ArrayList<Object>) evt.getNewValue();
			String savingpath = (String) results.get(0);
			if (tabCollection.containsKey(savingpath)) {
				tabCollection.get(savingpath).setAsSaved();
				tabCollection.get(savingpath).miniPanel.setAsSaved();
			}
			break;
		case CLOSE_ALL_TABS:
			tabPane.removeAll();
			tabCollection.clear();
			
			break;
			
		case SET_CHOSEN_NAME:
			results = (ArrayList<Object>)evt.getNewValue();
			String newname = (String) results.get(0);
			setChosenName(newname);
			break;
		default:
			break;
		}
	}

	private String findKeyFromPath(String editingpath) {
		
		//TO-DO put a lock here
		String similar = null; 
		for ( String key : tabCollection.keySet()) {
			
			if(key.contains(editingpath));
			similar = key;
			
		}
		
		
		
		return similar;
	}

	public void CloseTab(String path) {
		this.tabPane.remove(tabPane.indexOfComponent(tabCollection.get(path)));

	}

	public String[] getAllContents() {
		String[] returning = new String[tabCollection.size()];
		int count = 0;
		for (String key : tabCollection.keySet()) {
			returning[count] = tabCollection.get(key).getContents();

			count++;
		}
		return returning;
	}

	public String[] getAllNames() {
		String[] returning = new String[tabCollection.size()];
		int count = 0;
		for (String key : tabCollection.keySet()) {
			returning[count] = tabCollection.get(key).getPath();

			count++;
		}
		return returning;
	}

}