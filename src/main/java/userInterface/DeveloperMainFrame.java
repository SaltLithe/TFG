package userInterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.ImageObserver;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarkLaf;

import userInterface.fileNavigation.FileExplorerToolbar;
import userInterface.networkManagement.UsersPanel;
import userInterface.textEditing.TextEditorContainer;
import userInterface.textEditing.TextEditorPanel;

/**
 * Class containing all of the necessary elements for the main interface of this
 * program
 * 
 * @author Carmen Gómez Moreno
 *
 */
@SuppressWarnings("serial")
public class DeveloperMainFrame extends JFrame implements PropertyChangeListener {

	TextEditorContainer textEditorContainer;
	FileExplorerToolbar fileExplorerToolbar;
	UsersPanel usersPanel;
	MenuToolbar menuToolbar;
	TextEditorPanel textEditorPanel;
	ConsolePanel consolePanel;
	JSplitPane explorerDivision;
	JSplitPane usersDivision;
	private Dimension screenSize;
	private PropertyChangeMessenger support;
	private JFrame instance;
	private boolean somethingHasChanged;
	private UIController controller;

	DeveloperMainFrame() {

		super("Pair Leap");
		support = PropertyChangeMessenger.getInstance();
		instance = this;
		somethingHasChanged = false;

		try {
			UIManager.setLookAndFeel(new FlatDarkLaf());
		} catch (Exception ex) {
		}

		this.controller = UIController.getInstance();
		controller.setDeveloperMainFrame(this);

		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLayout(new BorderLayout());
		textEditorContainer = new TextEditorContainer();
		fileExplorerToolbar = new FileExplorerToolbar();
		textEditorPanel = textEditorContainer.textEditorPanel;
		fileExplorerToolbar.setPreferredSize(new Dimension(ImageObserver.HEIGHT, 500));
		menuToolbar = new MenuToolbar(textEditorContainer);
		usersPanel = new UsersPanel();
		explorerDivision = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, fileExplorerToolbar, textEditorContainer);
		explorerDivision.setDividerLocation(200);
		usersDivision = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, explorerDivision, usersPanel);
		usersDivision.setDividerLocation(1200);

		add(usersDivision);
		add(menuToolbar, BorderLayout.NORTH);
		usersPanel.updateUI();

		setSize(screenSize.width, screenSize.height);

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		// Configure the program's behaviour when the user tries to close it
		// In this case it displays a dialog if there are changes left to save , stops
		// any running process that
		// may still be running and deletes copies of java files from bin directory
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {

				support.notify(ObserverActions.SAFETY_STOP, null);
				support.notify(ObserverActions.SAFETY_DELETE, null);

				if (somethingHasChanged) {
					int result = JOptionPane.showConfirmDialog(instance,
							"You left some unsaved changes. Would you like to do a full save of all of your progress before closing?",
							"Closing PairLeap", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (result == JOptionPane.YES_OPTION) {
						support.notify(ObserverActions.SAFETY_SAVE, null);
						support.notify(ObserverActions.SAVE_FULL, null);

					}
				}
				dispose();
				System.exit(0);
			}
		});
		setVisible(true);

	}

	/**
	 * Implementation of propertyChange from PropertyChangeListener that allows this
	 * class to listen to ui notifications
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		ObserverActions action = ObserverActions.valueOf(evt.getPropertyName());
		switch (action) {

		case SET_SAVE_FLAG_TRUE:
			somethingHasChanged = true;

			break;

		case SET_SAVE_FLAG_FALSE:
			somethingHasChanged = false;
			break;

		default:
			break;

		}

	}

}
