package userInterface.fileNavigation;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * Class used to implement our own custom renderer for the nodes of the trees so that we can use our own 
 * icons
 * @author Carmen Gómez Moreno
 *
 */
@SuppressWarnings("serial")
public class CustomTreeCellRenderer extends DefaultTreeCellRenderer{
	
	
	public CustomTreeCellRenderer() {
		super();
		
	}
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree , Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus){
		
		CustomTreeNode thisnode = (CustomTreeNode)value; 
		return new NodeMiniPanel(thisnode.name , sel , hasFocus , leaf , thisnode.path);
		
	}
	

}
