package userInterface;

import core.DEBUG;
import userInterface.fileNavigation.FileExplorerToolbar;
import userInterface.textEditing.TextEditorPanel;
import userInterface.textEditing.TextEditorToolbar;
import userInterface.fileNavigation.FileExplorerPanel;

public class DeveloperMainFrameWrapper implements Runnable {

	private static DeveloperMainFrame instance = null;
	private static DeveloperMainFrameWrapper singleInstance = null;

	public static FileExplorerToolbar getFileExplorerToolbar() {
		DEBUG.debugmessage(" por aqui si pasa aaaaa");

		return instance.fileExplorerToolbar;
	}

	public MenuToolbar getMenuToolbar() {
		return instance.menuToolbar;
	}

	public static TextEditorPanel getTextEditorPanel() {
		DEBUG.debugmessage(" por aqui si pasa bbbbb");
		return instance.textEditorToolbar.textEditorPanel;
	}

	public static TextEditorToolbar getTextEditorToolbar() {
		return instance.textEditorToolbar;
	}

	public static ConsolePanel getConsolePanel() {
		DEBUG.debugmessage("ME cago n la leche");
		return instance.textEditorToolbar.consolePanel;
	}

	public static FileExplorerPanel getFileExplorerPanel() {
		DEBUG.debugmessage(" por aqui si pasa ccc");

		return instance.fileExplorerToolbar.fileExplorerPanel;
	}

	public static DeveloperMainFrameWrapper getSingleInstance() {
		if (singleInstance == null) {
			singleInstance = new DeveloperMainFrameWrapper();
		}
		return singleInstance;
	}

	private DeveloperMainFrameWrapper() {

	}

	@Override
	public void run() {
		DEBUG.debugmessage("RUN BITCH RUN");
		if (instance == null) {
			instance = new DeveloperMainFrame();
		}
		// TODO Auto-generated method stub

	}

}
