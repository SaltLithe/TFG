package userInterface;

import javax.swing.tree.DefaultMutableTreeNode;

public class CustomTreeNode extends DefaultMutableTreeNode  {
	
	
	private String path;
	
	public CustomTreeNode(String path, String name) {
		super(name);
		this.path = path;
		

	}

}
