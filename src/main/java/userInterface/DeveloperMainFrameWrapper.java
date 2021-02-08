package userInterface;

import java.awt.Image;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.imageio.ImageIO;

import userInterface.fileNavigation.FileExplorerPanel;
import userInterface.fileNavigation.FileExplorerToolbar;
import userInterface.textEditing.TextEditorContainer;
import userInterface.textEditing.TextEditorPanel;

/**
 * This class wraps the developerMainFrame , the reason for this is because user
 * interface in swing is usually run on a separate ui thread , this prevents us
 * from getting references to various ui components In order to get references
 * to these components so we can add them to the list of classes that listen to
 * ui notifications, we need a class that saves a reference to the interface
 * while allowing us to run it in its thread This class will implement the
 * Runnable interface and we will override the run method with the creation of
 * the interface class we use for this program This class also implements the
 * singleton pattern , meaning we will create only one instance of the user
 * interface per instance of this program
 * 
 * @author Carmen Gómez Moreno
 *
 */
public class DeveloperMainFrameWrapper implements Runnable {

	private static DeveloperMainFrame instance = null;
	private static DeveloperMainFrameWrapper singleInstance = null;
	public static Image windowIcon ;

	/**
	 * 
	 * @return the File explorer toolbar
	 */
	public static FileExplorerToolbar getFileExplorerToolbar() {

		return instance.fileExplorerToolbar;
	}

	/**
	 * 
	 * @return The menu toolbar
	 */
	public static MenuToolbar getMenuToolbar() {
		return instance.menuToolbar;
	}

	/**
	 * 
	 * @return The text editor panel
	 */
	public static TextEditorPanel getTextEditorPanel() {
		return instance.textEditorContainer.textEditorPanel;
	}

	/**
	 * 
	 * @return The text editor container
	 */
	public static TextEditorContainer getTextEditorToolbar() {
		return instance.textEditorContainer;
	}

	/**
	 * 
	 * @return The console panel
	 */
	public static ConsolePanel getConsolePanel() {
		return instance.textEditorContainer.consolePanel;
	}

	/**
	 * 
	 * @return The file explorer panel
	 */
	public static FileExplorerPanel getFileExplorerPanel() {

		return instance.fileExplorerToolbar.fileExplorerPanel;
	}

	/**
	 * 
	 * @return The users Panel
	 */
	public static PropertyChangeListener getUsersPanel() {

		return instance.usersPanel;
	}

	/**
	 * 
	 * @return The user interface itself
	 */
	public static DeveloperMainFrame getInstance() {
		return instance;
	}

	/**
	 * 
	 * @return Instance of this object
	 */
	public static DeveloperMainFrameWrapper getSingleInstance() {
		if (singleInstance == null) {
			try {
				windowIcon =  ImageIO.read((DeveloperMainFrameWrapper.class.getResource("/resources/images/window_Icon.png")));
			} catch (IOException e) {
				e.printStackTrace();
				e.printStackTrace();
			}
			singleInstance = new DeveloperMainFrameWrapper();
		}
		return singleInstance;
	}

	/**
	 * Implement the run method from runnable
	 */
	@Override
	public void run() {
		if (instance == null) {
			instance = new DeveloperMainFrame();
		}

	}

	/**
	 * Protects the regular constructor
	 */
	private DeveloperMainFrameWrapper() {

	}
}
