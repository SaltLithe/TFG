package console;

import java.io.PrintStream;
import java.util.ArrayList;

import userInterface.ObserverActions;
import userInterface.PropertyChangeMessenger;

public class ConsoleWrapper {

	public PropertyChangeMessenger support;
	public InStreamWrapper inStream;
	public OutStreamWrapper outStream;
	public OutStreamWrapper errStream;

	public PrintStream outPrint;
	public PrintStream errPrint;
	PrintStream stdout;
	PrintStream stderr;

	public ConsoleWrapper() {

		stdout = System.out;
		stderr = System.err;

		support = PropertyChangeMessenger.getInstance();
		inStream = new InStreamWrapper(this);

		outPrint = new OutStreamWrapper(this);
		errPrint = new OutStreamWrapper(this);

	}

	public void releaseSemaphore() {
		inStream.releaseSemaphore();
	}

	public void setLastRead(String lastread) {

		inStream.setLastRead(lastread);
	}

	public void writeOutput(String out) {

		ArrayList<Object> list = new ArrayList<Object>();
		list.add(out);

		support.notify(ObserverActions.CONSOLE_PANEL_CONTENTS, null, list);
	}

}
