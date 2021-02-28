package console;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Semaphore;

import observerController.ObserverActions;

/**
 * Class that serves as wrapper for the console's input stream. In order to
 * control the console's input as to create a custom gui for the user to enter
 * input into we need to divert the standard input and output into our own
 * custom streams
 * 
 * @author Carmen Gómez Moreno
 *
 */

public class InStreamWrapper extends InputStream {

	private ConsoleWrapper console;
	private InputStream input;
	private Semaphore semaphore;
	private PrintStream stdout;
	private PrintStream stderr;

	/**
	 * 
	 * @param console : The consoleWrapper that creates this InStreamWrapper
	 */
	public InStreamWrapper(ConsoleWrapper console) {

		stdout = System.out;
		stderr = System.err;
		this.console = console;
		semaphore = new Semaphore(0);

	}

	/**
	 * Sets the last input that has been read from the console by copying it into the 
	 * InputStream this class wraps
	 * 
	 * @param lastRead : The last Read input from the console
	 */
	protected void setLastRead(String lastRead) {

		input = new ByteArrayInputStream(lastRead.getBytes(StandardCharsets.UTF_8));

	}

	/**
	 * Unlocks the semaphore that helps the running process to wait for input from
	 * the user
	 */
	protected void releaseSemaphore() {

		semaphore.release();

	}

	/**
	 * Overrides InputStream's read method in order to capture input by activating
	 * the console guy and blocking until the semaphore is released once the user
	 * has written something into the inputStream
	 * This method allows the running instance of a program to read from a stream
	 * as i it read from System.in
	 * 
	 */
	@Override
	public int read(byte[] b, int off, int len) throws IOException {

		//Tell the console guy to allow writing and start listening for input
		console.support.notify(ObserverActions.ENABLE_CONSOLE_PANEL,null);
		console.support.notify(ObserverActions.ENABLE_READING_LISTENER,null);

		try {
			//Block until something has been written
			semaphore.acquire();
		} catch (InterruptedException e) {

			System.setOut(stdout);
			System.setErr(stderr);
			Thread.currentThread().interrupt();
			e.printStackTrace();
		}

		//Read the input
		int nread = input.read(b, off, len);
		try {
			//Close the stream
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Return the inputs to the running process 
		return nread;

	}

	/*
	 * Overrides InputStream's read method.
	 * We don't need to override this method but we need to so 
	 * this class can inherit from InputStream
	 */
	@Override
	public int read() throws IOException {

		console.support.notify(ObserverActions.ENABLE_CONSOLE_PANEL, null);
		console.support.notify(ObserverActions.ENABLE_READING_LISTENER, null);
		return  input.read();

	}

}
