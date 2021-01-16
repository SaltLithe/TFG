package console;

import java.io.PrintStream;
import java.util.ArrayList;

import userInterface.ObserverActions;
import userInterface.PropertyChangeMessenger;

/**
 * 
 * Class created to access the console's data easily, includes useful functions
 * like resetting the console streams , access to the streams , keeping
 * references to the standard streams and managing input and output to send to
 * the user interface.
 * 
 * @author Carmen Gómez Moreno
 *
 *
 */
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

	/**
	 * Resets streams
	 */
	public void reset() {

		inStream = new InStreamWrapper(this);
		outPrint = new OutStreamWrapper(this);
		errPrint = new OutStreamWrapper(this);

	}

	/**
	 * Unlocks the semaphore that helps the running process to wait for input from
	 * the user
	 */
	public void releaseSemaphore() {

		inStream.releaseSemaphore();

	}

	/**
	 * 
	 * Sets the last input read from the console
	 * 
	 * @param lastread
	 * 
	 */
	public void setLastRead(String lastread) {

		inStream.setLastRead(lastread);

	}

	/**
	 * 
	 * Sends a notification to the user interface to write the next output from the
	 * console
	 * 
	 * @param out
	 * 
	 */

	public void writeOutput(String out) {

		ArrayList<Object> list = new ArrayList<Object>();
		list.add(out);
		support.notify(ObserverActions.CONSOLE_PANEL_CONTENTS, null, list);

	}

}
