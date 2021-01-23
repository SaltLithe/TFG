package userInterface.fileNavigation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.ImageObserver;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JPanel;
import userInterface.ObserverActions;

/**
 * UI class that implements ui listener methods and contains the FileExplorerPanel calling its
 * methods when necessary
 * Keeps references to all of the navigation trees
 * @author Carmen Gómez Moreno
 *
 */
@SuppressWarnings("serial")
public class FileExplorerToolbar extends JPanel implements PropertyChangeListener {

	public FileExplorerPanel fileExplorerPanel;
	private HashMap<String, ProjectTree> trees;

	public FileExplorerToolbar() {

		trees = new HashMap<String, ProjectTree>();
		fileExplorerPanel = new FileExplorerPanel();

		JPanel toolbarspace = new JPanel();
		toolbarspace.setLayout(new FlowLayout(FlowLayout.LEFT));

		setLayout(new BorderLayout());
		fileExplorerPanel.setPreferredSize(new Dimension(ImageObserver.WIDTH, ImageObserver.HEIGHT));
		add(toolbarspace, BorderLayout.NORTH);
		add(fileExplorerPanel, BorderLayout.CENTER);

	}

	
	@SuppressWarnings("unchecked")
	/**
	 * Mehtod implementing propertyChange from propertyChangeEvent in order to listen to ui notifications
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		ObserverActions action = ObserverActions.valueOf(evt.getPropertyName());
		ArrayList<Object> results = (ArrayList<Object>) evt.getNewValue();
		switch (action) {

		//Clean the panel
		case CLEAR_PANEL:

			fileExplorerPanel.clean();

			break;
		//Add a tree
		case ADD_PROJECT_TREE:
			File newProject = (File) results.get(0);
			ProjectTree tree = new ProjectTree(newProject, newProject.getPath());
			trees.put(newProject.getPath(), tree);
			fileExplorerPanel.addProjectTree(tree);
			fileExplorerPanel.updateUI();

			break;
		//Add a node to a project tree
		case UPDATE_PROJECT_TREE_ADD:

			String path = (String) results.get(0);
			String name = (String) results.get(1);
			String projectPath = (String) results.get(2);
			Boolean isFile = (Boolean) results.get(3);

			trees.get(projectPath).insertTreeNode(name, path, isFile);

			break;

		//Delete a project tree
		case DELETE_PROJECT_TREE:
			String projectPath2 = (String) results.get(0);
			fileExplorerPanel.removeAndClear(trees.get(projectPath2));
			fileExplorerPanel.updateUI();

			break;
		//Remove a node from a project tree
		case UPDATE_PROJECT_TREE_REMOVE:

			String projectPathDeleting = (String) results.get(0);
			CustomTreeNode node = (CustomTreeNode) results.get(1);

			trees.get(projectPathDeleting).deleteTreeNode(node);

			break;
		default:
			break;
		}
	}

}
