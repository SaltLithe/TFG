package userInterface.uiFileNavigation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import commandController.CommandController;
import userInterface.uiFileEditing.newClassDialog;
import userInterface.uiFileEditing.newSrcDialog;
import userInterface.uiGeneral.DeveloperMainFrameWrapper;

/**
 * UI Class that pops up a context menu when the user right clicks on nodes This
 * adds functionality to add classes and folders depending on which kind of node
 * the user right clicked on
 * 
 * @author Carmen Gómez Moreno
 *
 */
@SuppressWarnings("serial")
public class NodePopupMenu extends JPopupMenu {

	private JMenu addNew;
	private JMenuItem addClass;
	private JMenuItem addFolder;
	private JMenuItem delete;

	/**
	 * 
	 * @param isFile    : Flag that indicates if this node is a file
	 * @param isProject : Flag that indicates if this node is a a project
	 * @param parent    : The parent node of this node
	 */
	public NodePopupMenu(boolean isFile, boolean isProject, CustomTreeNode parent) {

		addNew = new JMenu("new");

		addClass = new JMenuItem("class");
		delete = new JMenuItem("delete");

		addFolder = new JMenuItem("src folder");

		if (CommandController.developerComponent.isConnected) {

			addClass.setEnabled(false);
			addFolder.setEnabled(false);
			delete.setEnabled(false);

		}

		addClass.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(!CommandController.developerComponent.isConnected) {

				new newClassDialog(parent.path, parent.project);
				}

			}

		});

		if (!isFile) {

			addFolder.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					new newSrcDialog(parent.path, parent.project);

				}

			});

			addNew.add(addClass);
			addNew.add(addFolder);

			add(addNew);

			delete.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					CommandController.developerComponent.setProjectFocus(parent.project);
					int result = JOptionPane.showConfirmDialog(DeveloperMainFrameWrapper.getInstance(),
							"Delete this Folder?", "Delete", JOptionPane.OK_CANCEL_OPTION);
					if (result == JOptionPane.OK_OPTION) {

						CommandController.developerComponent.deleteFile(parent.name, parent.path, !parent.isFile,
								parent.project, parent);
					}
				}

			});

			add(delete);

		} else if (isFile) {

			add(delete);
			delete.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					CommandController.developerComponent.setProjectFocus(parent.project);

					int result = JOptionPane.showConfirmDialog(DeveloperMainFrameWrapper.getInstance(),
							"Delete this class?", "Delete", JOptionPane.OK_CANCEL_OPTION);
					if (result == JOptionPane.OK_OPTION) {

						CommandController.developerComponent.deleteFile(parent.name, parent.path, !parent.isFile,
								parent.project, parent);
					}
				}

			});

		} else if (isProject) {

		}
	}

}
