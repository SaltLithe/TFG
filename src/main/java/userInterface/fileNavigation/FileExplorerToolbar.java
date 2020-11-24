package userInterface.fileNavigation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import core.DEBUG;
import core.DeveloperComponent;
import fileManagement.Project;
import userInterface.DeveloperMainFrame;
import userInterface.ObserverActions;
import userInterface.UIController;

@SuppressWarnings("serial")
public class FileExplorerToolbar extends JPanel implements PropertyChangeListener {
//
	public FileExplorerPanel fileExplorerPanel;

	private JButton addClass;
	private JButton addScript;
	private DeveloperComponent developerComponent;
	private UIController uiController;
	private DeveloperMainFrame developerMainFrame;

	private HashMap<String, ProjectTree> trees;

	public FileExplorerToolbar(DeveloperMainFrame developerMainFrame) {

		DEBUG.debugmessage("SE HA INVOCADO AL CONSTRUCTOR DE FILEEXPLORERTOOLBAR");
		trees = new HashMap<String, ProjectTree>();
		fileExplorerPanel = new FileExplorerPanel();
		this.developerMainFrame = developerMainFrame;
		uiController = UIController.getInstance();
		developerComponent = uiController.getDeveloperComponent();

		addClass = new JButton("Add Class");
		addScript = new JButton("Add Script");

		JPanel toolbarspace = new JPanel();
		toolbarspace.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolbarspace.add(addClass);
		toolbarspace.add(addScript);
		setLayout(new BorderLayout());
		fileExplorerPanel.setPreferredSize(new Dimension(ImageObserver.WIDTH, ImageObserver.HEIGHT));
		add(toolbarspace, BorderLayout.NORTH);
		add(fileExplorerPanel, BorderLayout.CENTER);

		addClass.setEnabled(false);
		addScript.setEnabled(false);

		// Al botón de añadir clase se le asocia con la creación de un diálogo con los
		// elementos necesarios
		// para crear una nueva clase , de momento se queda en esta misma clase

	}

	// Método que añade un botón al FileExplorerPanel si no existe de antemano ,
	// este método además se encarga
	// de darle su comportamiento al bóton antes de añadirlo

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		ObserverActions action = ObserverActions.valueOf(evt.getPropertyName());
		switch (action) {

		case ADD_PROJECT_TREE:
			DEBUG.debugmessage("ADDING TREE");
			ArrayList<Object> eventList2 = (ArrayList<Object>) evt.getNewValue();
			File newProject = (File) eventList2.get(0);
			ProjectTree tree = new ProjectTree(newProject, newProject.getPath());
			trees.put(newProject.getPath(), tree);
			fileExplorerPanel.addProjectTree(tree);
			fileExplorerPanel.updateUI();

			break;
		case UPDATE_PROJECT_TREE_ADD:
			ArrayList<Object> eventList3 = (ArrayList<Object>) evt.getNewValue();
			String path = (String) eventList3.get(0);
			String name = (String) eventList3.get(1);
			String projectPath = (String) eventList3.get(2);

			

				trees.get(projectPath).insertTreeNode(name, path);
				

			
			break;
		case UPDATE_PROJECT_TREE_REMOVE:

			ArrayList<Object> eventList4 = (ArrayList<Object>) evt.getNewValue();
			String projectPathDeleting = (String) eventList4.get(0);
			CustomTreeNode node = (CustomTreeNode) eventList4.get(1);

			trees.get(projectPathDeleting).deleteTreeNode(node);

			break;
		default:
			break;
		}
	}

	// Método para habilitar el área de edición de texto

}
