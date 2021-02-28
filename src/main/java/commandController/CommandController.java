package commandController;



import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.MoreExecutors;

import core.DeveloperComponent;
import userInterface.uiGeneral.DeveloperMainFrame;

/**
 * Class part of a Command pattern that allows any user interface component to call methods in the object
 * DeveloperComponent without needing a direct reference to it and passing any parameters it may need
 * It runs commands as lambda expressions either in the same thread from the method call or in a separate
 * thread
 * This class itselfs implements the singleton pattern 
 * @author Carmen Gómez Moreno
 *
 */
public class CommandController {

	private static CommandController instance;
	public static DeveloperComponent developerComponent;
	public static DeveloperMainFrame developerMainFrame;
	private static ExecutorService threadRunnerPool; 
	private static ExecutorService threadRunner; 
	
	

	/**
	 * 
	 * @return this objects instance 
	 */
	public static CommandController getInstance() {

		if (instance == null) {
			threadRunnerPool = Executors.newCachedThreadPool();
			threadRunner = MoreExecutors.getExitingExecutorService((ThreadPoolExecutor) threadRunnerPool , 100 , TimeUnit.MILLISECONDS);
			instance = new CommandController();
		}
		return instance;
	}

	/**
	 * Set the instance of the developer component
	 * @param developerComponent
	 */
	public void setDeveloperComponent(DeveloperComponent developerComponent) {
		CommandController.developerComponent = developerComponent;
		
		
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
		threadRunner.execute(command);

	}

	/**
	 * Method to set the main user interface
	 * @param developerMainFrame :  The main user interface
	 */
	public void setDeveloperMainFrame(DeveloperMainFrame developerMainFrame) {
		CommandController.developerMainFrame = developerMainFrame;

	}

}
