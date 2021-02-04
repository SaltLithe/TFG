package userInterface.fileNavigation;

import java.util.Enumeration;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.DefaultTreeModel;

/**
 * UI class containing the elements neccessary to navigate through the workspace
 * 
 * @author Carmen Gómez Moreno
 *
 */
@SuppressWarnings("serial")
public class FileExplorerPanel extends JPanel {
	JScrollPane scrollPane;
	LinkedList<JTree> treeList = new LinkedList<>();
	JPanel newpane;

	public FileExplorerPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		newpane = new JPanel();
		newpane.setLayout(new BoxLayout(newpane, BoxLayout.Y_AXIS));
		newpane.setAlignmentX(LEFT_ALIGNMENT);
		newpane.setAlignmentY(TOP_ALIGNMENT);
		setVisible(true);

	}

	/**
	 * Method used to visit every node in the tree in order to update them
	 * 
	 * @param node  : The node this method starts visiting from
	 * @param model : The original model of the tree
	 * @return the updated model
	 */
	public DefaultTreeModel visitAllNodes(CustomTreeNode node, DefaultTreeModel model) {
		System.out.println(node);
		if (node.getChildCount() >= 0) {
			for (@SuppressWarnings("rawtypes")
			Enumeration e = node.children(); e.hasMoreElements();) {
				CustomTreeNode n = (CustomTreeNode) e.nextElement();
				model.nodeChanged(n);
				visitAllNodes(n, model);
			}
		}
		return model;
	}

	/**
	 * Method used to add a navigation tree to the panel
	 * 
	 * @param tree : The tree to add to the panel
	 */
	public void addProjectTree(ProjectTree tree) {

		newpane.add(tree);

		this.updateUI();
		this.removeAll();
		scrollPane = new JScrollPane(newpane);

		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setSize(getPreferredSize());
		add(scrollPane);

		scrollPane.updateUI();

		this.updateUI();

	}

	/**
	 * Method used to remove a tree from the panel 
	 * @param tree : The tree that will be removed 
	 */
	public void removeAndClear(ProjectTree tree) {

		newpane.remove(tree);
		this.updateUI();
		this.removeAll();
		scrollPane = new JScrollPane(newpane);

		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setSize(getPreferredSize());
		add(scrollPane);

		scrollPane.updateUI();

		this.updateUI();

	}

	/**
	 * Method used to clean the panel from any elements
	 */
	public void clean() {

		newpane.removeAll();
		newpane.updateUI();
	}
}
