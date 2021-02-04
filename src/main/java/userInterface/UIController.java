package userInterface;



import core.DeveloperComponent;

/**
 * Class part of a Command pattern that allows any user interface component to call methods in the object
 * DeveloperComponent without needing a direct reference to it and passing any parameters it may need
 * It runs commands as lambda expressions either in the same thread from the method call or in a separate
 * thread
 * This class itselfs implements the singleton pattern 
 * @author Carmen Gómez Moreno
 *
 */
public class UIController {

	private static UIController instance;
	public static DeveloperComponent developerComponent;
	public static DeveloperMainFrame developerMainFrame;
	
	

	/**
	 * 
	 * @return this objects instance 
	 */
	public static UIController getInstance() {

		if (instance == null) {
			instance = new UIController();
		}
		return instance;
	}

	/**
	 * Set the instance of the developer component
	 * @param developerComponent
	 */
	public void setDeveloperComponent(DeveloperComponent developerComponent) {
		UIController.developerComponent = developerComponent;
	}

	/**
	 * 
	 * @return
	 */
	public DeveloperComponent getDeveloperComponent() {
		return developerComponent;
	}

	
	/**
	 * Method that runs a lambda expression on a new thread
	 * @param command The lambda expression to run 
	 */
	public static void runOnThread (Runnable command) {
		new Thread(command).start();

	}

	/**
	 * Method to set the main user interface
	 * @param developerMainFrame :  The main user interface
	 */
	public void setDeveloperMainFrame(DeveloperMainFrame developerMainFrame) {
		UIController.developerMainFrame = developerMainFrame;

	}

}
