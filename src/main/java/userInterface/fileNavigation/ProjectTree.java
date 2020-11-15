
package userInterface.fileNavigation;

import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import core.DEBUG;
import core.DeveloperComponent;
import userInterface.DeveloperMainFrame;
import userInterface.UIController;

public class ProjectTree extends JPanel implements TreeSelectionListener {

	private JTree tree;
	private UIController uiController;
	private DeveloperComponent developerComponent;
	private CustomTreeNode lastParent = null;
	private String project; 
	private DeveloperMainFrame frame; 

	public void valueChanged(TreeSelectionEvent e) {
		TreePath path = e.getNewLeadSelectionPath();
		Object lspc = tree.getLastSelectedPathComponent();
		System.out.println("path = " + path + "\n" + "lspc = " + lspc);
	}

	public ProjectTree(File dir , String project) {

		
		this.project = project;
		uiController = UIController.getInstance();
		developerComponent = uiController.getDeveloperComponent();
		frame = uiController.getFrame(); 
		setLayout(new BorderLayout());

		tree = new JTree(scanAndAdd(null, dir,  project , true));

		tree.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				CustomTreeNode node = null;
				int selRow = tree.getRowForLocation(me.getX(), me.getY());
				TreePath selPath = tree.getPathForLocation(me.getX(), me.getY());
				tree.setSelectionPath(selPath);
				try {
				node = (CustomTreeNode) tree.getSelectionPath().getLastPathComponent();
				}
				catch (Exception e) {}


				if (SwingUtilities.isLeftMouseButton(me)) {

					if (node != null) {
						String name = node.name; 
						String path = node.path; 
						

						if (node.isFile) {
							uiController.run(() -> developerComponent.openFile(name, path, null));
						}
					}
				} else if (SwingUtilities.isRightMouseButton(me)) {

					if (selRow > -1) {
						tree.setSelectionRow(selRow);
					}
					if (node != null) {
						NodePopupMenu menu = new NodePopupMenu(node.isFile, false , node , frame , uiController , developerComponent);
						TreePath path = tree.getPathForLocation(me.getX(), me.getY());
						Rectangle bounds = tree.getPathBounds(path);
						menu.show(me.getComponent(), (int) bounds.getMaxX(), (int) bounds.getMinY());
					}
				}

			}
		});

		CustomTreeCellRenderer rend = new CustomTreeCellRenderer();

		tree.setCellRenderer(rend);

		JScrollPane scrollpane = new JScrollPane();
		scrollpane.getViewport().add(tree);
		add(BorderLayout.CENTER, scrollpane);
		this.setVisible(true);

	}

	CustomTreeNode scanAndAdd(CustomTreeNode currentRootNode, File dir, String project , boolean isStart) {
		String path = dir.getPath();
		String name = path.substring(path.lastIndexOf("\\") + 1, path.length());
		boolean isDir = true;
		if (dir.isDirectory()) {
			isDir = false;
		}
		CustomTreeNode currentNode = new CustomTreeNode(path, name, project , isDir , true);
		if (!isStart) {
			// Si no estamos empezando podemos a�adir la ruta al nodo anterior
			// Esto es para que el project no se a�ada a s� mismo
			currentRootNode.add(currentNode);
		}

		String[] fileArray = dir.list();
		ArrayList<File> files = new ArrayList<File>();

		for (String filename : fileArray) {
			File newfile = new File(path + "//" + filename);
			files.add(newfile);
		}

		ArrayList<File> addLater = new ArrayList<File>();
		for (File f : files) {

			if (f.isDirectory()) {
				scanAndAdd(currentNode, f, project ,  false);
			} else {

				addLater.add(f);
			}

		}
		// No puedes iterar y a�adir no se por que
		for (File g : addLater) {
			String filepath = g.getAbsolutePath();
			String filename = filepath.substring(filepath.lastIndexOf("\\") + 1, filepath.length());
			boolean isFile = true;
			if (g.isDirectory()) {
				isFile = false;
			}
			currentNode.add(new CustomTreeNode(g.getAbsolutePath(), filename, project,  isFile , false));

		}
		return currentNode;
	}

	private void findParent(String parentpath, CustomTreeNode current) {

		if (current.path.equals(parentpath)) {

			lastParent = current;

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

	public void insertTreeNode(String name, String path) {
		CustomTreeNode newchild = new CustomTreeNode( path+"\\"+name, name,  project,  true, false);
		String parentpath = newchild.getParentPath();
		findParent(parentpath, (CustomTreeNode) tree.getModel().getRoot());
		if (parentpath != null && lastParent != null) {

			DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
			model.insertNodeInto(newchild, lastParent, lastParent.getChildCount());
			model.reload();
			tree.setModel(model);
			tree.updateUI();

		}
		lastParent = null;

	}

	public Dimension getMinimumSize() {
		return new Dimension(200, 400);
	}

	public Dimension getPreferredSize() {
		return new Dimension(200, 400);
	}

	public void deleteTreeNode( CustomTreeNode node) {
		
	
					DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
					model.removeNodeFromParent(node);
					model.reload();
					tree.setModel(model);
					tree.updateUI();
			
			
		
		// TODO Auto-generated method stub
		
	}

	/** Main: make a Frame, add a FileTree */

}
