package userInterface;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class CustomTreeCellRenderer extends DefaultTreeCellRenderer{
	
	
	public CustomTreeCellRenderer() {
		super();
		
	}
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree , Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus){
		
		
		CustomTreeNode thisnode = (CustomTreeNode)value; 
		
		NodeMiniPanel returning = new NodeMiniPanel(thisnode.name , "Icons/warning_icon.png", sel , hasFocus , leaf);
		return returning; 
	}
	

}
