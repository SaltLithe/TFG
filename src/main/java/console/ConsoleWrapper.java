package console;

import java.io.PrintStream;

import userInterface.ObserverActions;
import userInterface.PropertyChangeMessenger;
import userInterface.UIController;

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

	PropertyChangeMessenger support;
	private InStreamWrapper inStream;
	private OutStreamWrapper outStream;
	private OutStreamWrapper errStream;
	private PrintStream outPrint;
	private PrintStream errPrint;

	PrintStream stdout;
	PrintStream stderr;
	
	

	public ConsoleWrapper() {

		stdout = System.out;
		stderr = System.err;

		support = PropertyChangeMessenger.getInstance();
		setInStream(new InStreamWrapper(this));

		setOutPrint(new OutStreamWrapper(this));
		setErrPrint(new OutStreamWrapper(this));
		
		
	}

	/**
	 * Resets streams
	 */
	public void reset() {

		setInStream(new InStreamWrapper(this));
		setOutPrint(new OutStreamWrapper(this));
		setErrPrint(new OutStreamWrapper(this));

	}

	/**
	 * Unlocks the semaphore that helps the running process to wait for input from
	 * the user
	 */
	public void releaseSemaphore() {

		getInStream().releaseSemaphore();

	}

	/**
	 * 
	 * Sets the last input read from the console
	 * 
	 * @param lastread
	 * 
	 */
	public void setLastRead(String lastread) {

		getInStream().setLastRead(lastread);

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

		
		Object[] message = {out};
		support.notify(ObserverActions.CONSOLE_PANEL_CONTENTS, message);
		

			try {
				UIController.developerComponent.consoleBuffer.put(out);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				e.printStackTrace();
			}
	
		

	}

	public OutStreamWrapper getErrStream() {
		return errStream;
	}

	public void setErrStream(OutStreamWrapper errStream) {
		this.errStream = errStream;
	}

	public OutStreamWrapper getOutStream() {
		return outStream;
	}

	public void setOutStream(OutStreamWrapper outStream) {
		this.outStream = outStream;
	}

	public PrintStream getOutPrint() {
		return outPrint;
	}

	public void setOutPrint(PrintStream outPrint) {
		this.outPrint = outPrint;
	}

	public PrintStream getErrPrint() {
		return errPrint;
	}

	public void setErrPrint(PrintStream errPrint) {
		this.errPrint = errPrint;
	}

	public InStreamWrapper getInStream() {
		return inStream;
	}

	public void setInStream(InStreamWrapper inStream) {
		this.inStream = inStream;
	}



}
