
package userInterface.fileNavigation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import userInterface.DeveloperMainFrame;
import userInterface.ObserverActions;
import userInterface.PropertyChangeMessenger;
import userInterface.UIController;

/**
 * Represents a file sistem with tree nodes for the user to navigate
 * 
 * @author Carmen Gómez Moreno
 *
 */
@SuppressWarnings("serial")
public class ProjectTree extends JPanel implements TreeSelectionListener {

	public JTree internalTree;
	private CustomTreeNode lastParent = null;
	private String project;
	private DeveloperMainFrame frame;
	private ArrayList<String> baseClassPath;
	private PropertyChangeMessenger support = PropertyChangeMessenger.getInstance();

	/**
	 * 
	 * @param dir     : The directory for this navigation tree
	 * @param project : The project this tree represents
	 */
	public ProjectTree(File dir, String project) {

		baseClassPath = new ArrayList<String>();
		this.project = project;
		frame = UIController.developerMainFrame;
		setLayout(new BorderLayout());

		internalTree = new JTree(scanAndAdd(null, dir, project, true));
		String[] baseClassPathArray = new String[baseClassPath.size()];
		for (int i = 0; i < baseClassPath.size(); i++) {
			baseClassPathArray[i] = baseClassPath.get(i);
		}
		UIController.developerComponent.loadClassPath(baseClassPathArray, project);

		// Adding functionality for the nodes right here

		internalTree.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				CustomTreeNode node = null;
				int selRow = internalTree.getRowForLocation(me.getX(), me.getY());
				TreePath selPath = internalTree.getPathForLocation(me.getX(), me.getY());
				internalTree.setSelectionPath(selPath);
				try {
					node = (CustomTreeNode) internalTree.getSelectionPath().getLastPathComponent();
				} catch (Exception e) {
				}

				if (SwingUtilities.isLeftMouseButton(me)) {

					if (node != null) {
						String name = node.name;
						String path = node.path;

						if (node.isFile) {
							UIController.developerComponent.openFile(name, path, null, project);
						}
					}
				} else if (SwingUtilities.isRightMouseButton(me)) {

					if (selRow > -1) {
						internalTree.setSelectionRow(selRow);
					}
					if (node != null && !node.hideContents) {
						NodePopupMenu menu = new NodePopupMenu(node.isFile, false, node);
						TreePath path = internalTree.getPathForLocation(me.getX(), me.getY());
						Rectangle bounds = internalTree.getPathBounds(path);
						menu.show(me.getComponent(), (int) bounds.getMaxX(), (int) bounds.getMinY());
					}
				}

			}
		});

		CustomTreeCellRenderer rend = new CustomTreeCellRenderer();

		internalTree.setCellRenderer(rend);

		JScrollPane scrollpane = new JScrollPane();
		scrollpane.getViewport().add(internalTree);
		add(BorderLayout.CENTER, scrollpane);
		this.setVisible(true);

	}

	/**
	 * Define the minimum size of the panel containing this tree
	 */
	public Dimension getMinimumSize() {
		return new Dimension(200, 400);
	}

	/**
	 * efine the maximum size of the panel containing this tree
	 */
	public Dimension getPreferredSize() {
		return new Dimension(200, 400);
	}

	/**
	 * Implementation of an inherited method
	 */
	public void valueChanged(TreeSelectionEvent e) {

	}

	/**
	 * MEthod used to scan the directory for this project tree and add nodes for the
	 * different folders and files
	 * 
	 * @param currentRootNode : The Node to scan
	 * @param dir             : The directory to scan
	 * @param project         : The project this method is generating a tree for
	 * @param isStart         : A flag indicating if this is the first node to be
	 *                        scanned
	 * @return nodes for the recursive method to work
	 */
	CustomTreeNode scanAndAdd(CustomTreeNode currentRootNode, File dir, String project, boolean isStart) {
		String path = dir.getPath();
		String name = path.substring(path.lastIndexOf("\\") + 1, path.length());
		boolean isDir = true;
		if (dir.isDirectory()) {
			isDir = false;
		}
		// Create a node for the current directory
		CustomTreeNode currentNode = new CustomTreeNode(path, name, project, isDir, true);
		if (!isStart) {
			// if it is the first node , it is the root
			currentRootNode.add(currentNode);
		}

		// Get all the files for the current directory
		String[] fileArray = dir.list();
		ArrayList<File> files = new ArrayList<File>();

		// Add them to the list if they are not hidden
		if (!currentNode.hideContents) {

			for (String filename : fileArray) {
				File newfile = new File(path + "//" + filename);
				files.add(newfile);
			}
		}

		// Check if what we scanned are files or directories
		ArrayList<File> addLater = new ArrayList<File>();
		for (File f : files) {

			// If they are directories , start the method again
			if (f.isDirectory()) {
				scanAndAdd(currentNode, f, project, false);

			} else {
				// If they are not we will just add them later
				addLater.add(f);
			}

		}
		// Once the directories have been scanned , add the files
		for (File g : addLater) {
			String filepath = g.getAbsolutePath();
			String filename = filepath.substring(filepath.lastIndexOf("\\") + 1, filepath.length());
			boolean isFile = true;
			if (g.isDirectory()) {
				isFile = false;
			}
			currentNode.add(new CustomTreeNode(g.getAbsolutePath(), filename, project, isFile, false));
			baseClassPath.add(g.getAbsolutePath());

		}
		return currentNode;
	}

	/**
	 * Method used to insert a new node into a tree
	 * 
	 * @param name   : The name of the node
	 * @param path   : The path of the node
	 * @param isFile : Flag idicating if this is a folder
	 */
	public void insertTreeNode(String name, String path, Boolean isFile) {
		CustomTreeNode newchild;
		if (isFile) {
			newchild = new CustomTreeNode(path + "\\" + name, name, project, isFile, false);
		} else {
			newchild = new CustomTreeNode(path, name, project, isFile, false);

		}

		// Get the path of this nodes parent
		String parentpath = newchild.getParentPath();
		// With this string , find the node in the tree
		findParent(parentpath, (CustomTreeNode) internalTree.getModel().getRoot());
		if (parentpath != null && lastParent != null) {

			// Insert the new node
			DefaultTreeModel model = (DefaultTreeModel) internalTree.getModel();
			model.insertNodeInto(newchild, lastParent, lastParent.getChildCount());
			model.reload();
			internalTree.setModel(model);
			internalTree.updateUI();

		}
		lastParent = null;

	}

	/**
	 * Method used to delete a node from this tree
	 * 
	 * @param node : The node to be deleted from this tree
	 */
	public void deleteTreeNode(CustomTreeNode node) {

		// Get the root node
		DefaultTreeModel model = (DefaultTreeModel) internalTree.getModel();
		CustomTreeNode root = (CustomTreeNode) model.getRoot();
		// If we are deleting the root , just delete the whole tree
		if (root.path == node.path) {
			Object[] message = { node.path };
			support.notify(ObserverActions.DELETE_PROJECT_TREE, message);

			// If it is not the root , tell the model to delete this node
		} else {
			model.removeNodeFromParent(node);
			model.reload();
			internalTree.setModel(model);
			internalTree.updateUI();
		}

	}

	/**
	 * 
	 * @return the tree object inside the panel
	 */
	public JTree getInternalTree() {
		return internalTree;
	}

	/**
	 * Support method used to get the parent node of another node given it's path
	 * @param parentpath : The path of the node that this method will find
	 * @param current : The node this method is currently scanning
	 */
	private void findParent(String parentpath, CustomTreeNode current) {

		//If the current node matches the node has been found
		if (current.path.equals(parentpath)) {

			lastParent = current;

		//If it is not, get all of the children nodes and scan each of them
		} else if (!current.isFile) {
			int count = current.getChildCount();
			for (int i = 0; i < count; i++) {
				CustomTreeNode nextnode = (CustomTreeNode) current.getChildAt(i);
				if (!nextnode.isFile) {
					findParent(parentpath, nextnode);
				}
			}

		}

	}

}
