package userInterface.textEditing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.image.ImageObserver;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.google.common.util.concurrent.MoreExecutors;

import com.google.common.util.concurrent.MoreExecutors;

import core.DEBUG;
import network.HighLightMessage;
import network.WriteMessage;
import userInterface.ObserverActions;
import userInterface.PropertyChangeMessenger;
import userInterface.UIController;

/*Clase que contiene el editor de texto usando un elemento del tipo RSyntaxTextArea 
 * Tiene tambien una referencia al focus actual que indica el archivo sobre el que se está
 * trabajando actualmente en la aplicación
 * 
 */
/**
 * UI Class containing the text of the edited files inside of a special text
 * area RSyntaxTextArea implements line numbers , syntax highlight and more
 * 
 * @author Carmen Gómez Moreno
 *
 */
@SuppressWarnings("serial")
public class TextEditorPanel extends JPanel implements PropertyChangeListener {

	public ArrayBlockingQueue<WriteMessage> sendBuffer = new ArrayBlockingQueue<WriteMessage>(200);
	public ArrayBlockingQueue<HighLightMessage> highlightBuffer = new ArrayBlockingQueue<HighLightMessage>(200);

	private JTabbedPane tabPane;
	private HashMap<String, TextEditorTab> tabCollection;
	private PropertyChangeMessenger propertyChangeMessenger;
	private String chosenName = null;

	ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
	ExecutorService executorService = MoreExecutors.getExitingExecutorService(executor, 100, TimeUnit.MILLISECONDS);
	private long sendDelay = 50;

	public TextEditorPanel() {
		propertyChangeMessenger = PropertyChangeMessenger.getInstance();

		tabCollection = new HashMap<String, TextEditorTab>();

	
		
		
		executorService.submit(()-> sendMessages());
		executorService.submit(()->sendHighlights());
		
	

		setLayout(new BorderLayout());
		this.setSize(new Dimension(ImageObserver.WIDTH, ImageObserver.HEIGHT));

		tabPane = new JTabbedPane();

		tabPane.setTabLayoutPolicy(1);

		this.add(tabPane, BorderLayout.CENTER);

		tabPane.setVisible(true);

		this.setVisible(true);
	}

	/**
	 * Method that , given a tab name , will return it's contents
	 * 
	 * @param name : The name of the tab where the contents will be pulled from
	 * @return string containing the contents of a tab
	 */
	public String getContents(String name) {

		return tabCollection.get(name).getContents();
	}

	/**
	 * Method used to add a tab to the editor
	 * 
	 * @param name     : The name of the tab
	 * @param path     : The path of the file this tab is opening
	 * @param project  : The project the file belongs to
	 * @param contents : The contents of the file
	 */
	public void addTab(String name, String path, String project, String contents) {

		UIController.developerComponent.setProjectFocus(project);
		TabMiniPanel mp1 = new TabMiniPanel(name, path, project);
		TextEditorTab tab = new TextEditorTab(path, mp1, project, chosenName, this);
		tab.setTextEditorCode(contents);
		mp1.setParent(tab);

		tabPane.addTab("", tab);
		tabPane.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {

				try {

					JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
					TextEditorTab selected = (TextEditorTab) tabbedPane.getSelectedComponent();
					UIController.developerComponent.setProjectFocus(selected.getProject());
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

	/**
	 * Method used to close a tab
	 * 
	 * @param name : The name of this tab
	 */
	public void CloseTab(String name) {
		this.tabPane.remove(tabPane.indexOfComponent(tabCollection.get(name)));

	}

	/**
	 * Method used to retrieve the contents of all opened tabs
	 * 
	 * @return an array containing the contents of all tabs
	 */
	public String[] getAllContents() {
		String[] returning = new String[tabCollection.size()];
		int count = 0;
		for (String key : tabCollection.keySet()) {
			returning[count] = tabCollection.get(key).getContents();

			count++;
		}
		return returning;
	}

	/**
	 * Method used to retrieve the name of all opened tabs
	 * 
	 * @return an array containing the nams of all tabs
	 */
	public String[] getAllNames() {
		String[] returning = new String[tabCollection.size()];
		int count = 0;
		for (String key : tabCollection.keySet()) {
			returning[count] = tabCollection.get(key).getPath();

			count++;
		}
		return returning;
	}

	/**
	 * Method used by a special thread that sends highlights to other users TODO
	 * VERY IMPORTANT HOLY FUCK THIS SHOULD NOT BE A TREAD PER TAB YOU ONLY
	 * REALISTICALLY EDIT ONE TAB AT A TIME ARE YOU CRAZY
	 */
	public void sendHighlights() {

		while (true) {
			HighLightMessage message = null;

			try {

				message = highlightBuffer.take();

			} catch (InterruptedException e) {

				e.printStackTrace();
			}

			final HighLightMessage finalMessage = message;
			UIController.developerComponent.sendMessageToEveryone(finalMessage);
			try {
				Thread.sleep(sendDelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * Method used by a special thread that sends write updates to other users TODO
	 * HOLY SHIT SAME AS ABOVE
	 */
	public void sendMessages() {

		while (true) {
			WriteMessage message = null;
			try {
				message = sendBuffer.take();

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			final WriteMessage finalMessage = message;
			UIController.developerComponent.sendMessageToEveryone(finalMessage);
			try {
				Thread.sleep(sendDelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Implementation of propertyChange from PropertyChangeListener so that this
	 * class can listen to ui notifications
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		ObserverActions action = Enum.valueOf(ObserverActions.class, evt.getPropertyName());
		ArrayList<Object> results;
		switch (action) {

		case UPDATE_PANEL_CONTENTS:
			synchronized (this) {
				results = (ArrayList<Object>) evt.getNewValue();
				String editingpath = (String) results.get(0);

				String key = findKeyFromPath(editingpath);
				if (key != null) {
					this.tabCollection.get(key).updateContents(results);
				} else {
				}
			}
			break;

		case UPDATE_HIGHLIGHT:
			results = (ArrayList<Object>) evt.getNewValue();
			String editingpath = (String) results.get(4);
			String key = findKeyFromPath(editingpath);
			if (key != null) {

				DEBUG.debugmessage("Got a highlight for name " + (String) results.get(3));
				int color = -1;
				try {
					color = (int) results.get(2);
				} catch (Exception uwu) {
				}
				this.tabCollection.get(key).paintHighLight((int) results.get(0), (int) results.get(1), color,
						(String) results.get(3));
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
			results = (ArrayList<Object>) evt.getNewValue();
			String newname = (String) results.get(0);
			setChosenName(newname);
			break;
		case DISABLE_TEXT_EDITOR:

			enableComponents(this, false);
			break;
		case ENABLE_TEXT_EDITOR:
			enableComponents(this, true);
			break;
		case SAFETY_STOP:
			
			executorService.shutdown();
			
			break;

		default:

			break;
		}
	}

	/**
	 * Support method that , given the path of a file , check if there is any tab
	 * that could match it
	 * 
	 * @param editingpath : The path to find a key from
	 * @return the key for this path if it exists
	 */
	private String findKeyFromPath(String editingpath) {

		// TODO put a lock here
		String similar = null;
		for (String key : tabCollection.keySet()) {

			if (key.contains(editingpath))
				;
			similar = key;

		}

		return similar;
	}

	private void enableComponents(Container container, boolean enable) {
		Component[] components = container.getComponents();
		for (Component component : components) {
			component.setEnabled(enable);
			if (component instanceof Container) {
				enableComponents((Container) component, enable);
			}
		}
	}

	/**
	 * Used to update the users name in the sesion
	 * 
	 * @param chosenName : The new name
	 */
	private void setChosenName(String chosenName) {
		this.chosenName = chosenName;
	}

	/**
	 * Support method used to update the focus as to indicate what tab is in focus
	 * 
	 * @param currentProject : The project this tab belongs to
	 * @param currentTab     : The tab that is in focus
	 */
	private void updateContainer(String currentProject, String currentTab) {

		Object[] message = { currentTab, currentProject };
		propertyChangeMessenger.notify(ObserverActions.CHANGE_TAB_FOCUS, message);

	}

}