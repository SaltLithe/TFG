package TFG;

import java.util.Deque;
import java.util.LinkedList;

public class UIController {

	private static Deque<Command> ActionList;
	private int actionsSaved = 100;
	private static UIController instance;
	private DeveloperComponent developerComponent;
	public DeveloperMainFrame developerMainFrame;

	public static UIController getInstance() {

		if (instance == null) {
			ActionList = new LinkedList<Command>();
			instance = new UIController();
		}
		return instance;
	}

	public void setDeveloperComponent(DeveloperComponent developerComponent) {
		this.developerComponent = developerComponent;
	}

	public DeveloperComponent getDeveloperComponent() {
		return developerComponent;
	}

	public void run(Runnable command) {
		if (!(ActionList.size() < actionsSaved)) {

			ActionList.removeFirst();
		}
		Command newCommand = new Command(command, developerComponent);
		ActionList.addLast(newCommand);
		newCommand.execute().run();

	}

	public void undo() {
		if (!ActionList.isEmpty()) {
			Command last = ActionList.removeLast();
			developerComponent = (DeveloperComponent) last.unExecute();

		}
	}

	public void setDeveloperMainFrame(DeveloperMainFrame developerMainFrame) {
		this.developerMainFrame = developerMainFrame;

	}

}
