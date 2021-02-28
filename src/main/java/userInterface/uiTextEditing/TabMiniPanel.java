package userInterface.uiTextEditing;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import commandController.CommandController;
import core.DEBUG;
import core.DeveloperComponent;
import observerController.ObserverActions;
import observerController.PropertyChangeMessenger;

/**
 * UI class that contains the panel of the text tabs inside of the editor Used
 * to display both the name and the close file button
 * 
 * @author Carmen Gómez Moreno
 *
 */
@SuppressWarnings({ "serial", "unused" })
public class TabMiniPanel extends JPanel {

	private String path;
	private String name;
	private CommandController uiController;
	private DeveloperComponent developerComponent;
	private PropertyChangeMessenger support;
	private JLabel nameLabel;
	private TextEditorTab parent;
	private String project;

	/**
	 * 
	 * @param name    : The name of the file this tab is opening
	 * @param path    : The path of the file this tab is opening
	 * @param project : The project that the file this tabs opens belongs to
	 */
	public TabMiniPanel(String name, String path, String project) {

		support = PropertyChangeMessenger.getInstance();
		this.project = project;
		this.name = name;
		this.path = path;
		this.uiController = CommandController.getInstance();
		this.developerComponent = uiController.getDeveloperComponent();

		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);

		nameLabel = new JLabel(name);
		add(nameLabel);

		JButton closeButton = new JButton("X");
		closeButton.setOpaque(false);

		closeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				CommandController.developerComponent.closeTab(path, parent.unsavedChanges, parent.getContents());

			}

		});

		add(closeButton);
		setOpaque(false);
		setSize(getPreferredSize());
		setVisible(true);
	}

	/**
	 * Sets the tab that this tabpanel belongs to
	 * 
	 * @param parent
	 */
	public void setParent(TextEditorTab parent) {
		this.parent = parent;
	}

	/**
	 * Sets the name of this tab as normal , indicating that there are no unsaved
	 * changes
	 */
	public void setAsSaved() {
		nameLabel.setText(name);
		updateUI();
	}

	/**
	 * Adds an asterisk to this tab's name , indicating that there are unsaved
	 * changes
	 */
	public void setAsUnsaved() {

		nameLabel.setText(name + "*");
		updateUI();
	}

}
