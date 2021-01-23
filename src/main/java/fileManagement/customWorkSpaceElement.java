package fileManagement;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.io.File;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import userInterface.fileNavigation.workSpaceSelect;

/**
 * Class that represents a workspace in the workspace menu selection , it
 * contains the necessary information and method to open , locate or delete a
 * workspace
 * 
 * @author Carmen Gómez Moreno
 *
 */
public class customWorkSpaceElement extends JPanel {

	public int tempID;

	private static final long serialVersionUID = 1L;
	private String name;
	private String path;
	private JButton deleteLocateButton;
	private JLabel nameLabel;
	private Color notFoundColor;
	private Color foundColor;
	private int forcedHeight = 30;
	private int forcedWidth = 0;
	private WorkSpaceManager wsm;
	private boolean canOpen;
	@SuppressWarnings("unused")
	private workSpaceSelect parent;

	/**
	 * 
	 * @param name   : Name of this workspace
	 * @param path   : Path this workspace is located at
	 * @param parent : Parent object that creates this object in the workspace
	 *               selection menu
	 */
	@SuppressWarnings("unchecked")
	public customWorkSpaceElement(String name, String path, workSpaceSelect parent) {

		wsm = WorkSpaceManager.getInstance();
		this.parent = parent;
		this.name = name;
		this.path = path;
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		notFoundColor = new Color(222, 57, 49);

		nameLabel = new JLabel(name + ": " + path);
		foundColor = nameLabel.getForeground();

		deleteLocateButton = new JButton("");
		add(deleteLocateButton);

		nameLabel = new JLabel(name + ": " + path);

		checkExistance();

		nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
		nameLabel.setAlignmentX(0.5f);

		deleteLocateButton.setHorizontalAlignment(SwingConstants.LEFT);
		deleteLocateButton.setAlignmentX(0.5f);
		deleteLocateButton.setText("Delete");

		deleteLocateButton.addActionListener(new ActionListener() {

			// Add behaviour for the dialog that pops up if the user wants to delete the
			// workspace
			@Override
			public void actionPerformed(ActionEvent e) {

				Object[] options = { "Delete", "Cancel" };
				int n = JOptionPane.showOptionDialog(parent,
						"Delete this Workspace from the list? This won't delete this workspace from your system. You can delete this workspace from your system manually.",
						"Confirm delete", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
						options[1]);
				if (n == JOptionPane.OK_OPTION) {

					wsm.deleteWorkSpace(tempID, name, parent);
					parent.refresh();
				}

			}

		});

		this.setAlignmentX(Component.LEFT_ALIGNMENT);

		Font font = nameLabel.getFont();
		@SuppressWarnings("rawtypes")
		Map attributes = font.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		nameLabel.setFont(font.deriveFont(attributes));

		// Add behaviour for the dialog that pops up if the workspace is not available
		nameLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (!canOpen) {

					Object[] options = { "Locate", "Cancel" };
					int n = JOptionPane.showOptionDialog(parent,
							"This WorkSpace could not be located, would you like to locate it?.", "WorkSpace not found",
							JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

					if (n == JOptionPane.OK_OPTION) {

						String path = wsm.getFilePath();
						if (path != null) {

							// TODO do I need this?
							String search = "\\";
							int index = path.lastIndexOf(search);
							String subname = path.substring(index + 1, path.length());

							boolean results = wsm.locateWorkSpace(path, name, parent);

							if (results) {
								nameLabel.setForeground(foundColor);
								canOpen = true;
								parent.refresh();
							}

						}
					}
				} else {

					wsm.startMainApp(tempID, parent);
				}
			}
		});

		add(nameLabel);
		forcedWidth = this.getPreferredSize().width;
		this.setMinimumSize(new Dimension(forcedWidth, forcedHeight));
		this.setPreferredSize(new Dimension(forcedWidth, forcedHeight));
		this.setMaximumSize(new Dimension(forcedWidth, forcedHeight));

		this.setVisible(true);

	}

	/**
	 * @return workspace name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the components name
	 * 
	 * @name name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return workspace path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Set component path
	 * 
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Support method to see if a workspace still exists in order to mark it
	 * visually as such for the user
	 */
	private void checkExistance() {

		File workspacefile = new File(path);
		if (workspacefile.exists()) {
			canOpen = true;

		} else {

			nameLabel.setForeground(notFoundColor);
			canOpen = false;

		}

	}

}
