package console;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class that serves as wrapper for the console's output and error streams. In
 * order to control the console's output as to create a custom gui for the user
 * to enter input into we need to divert the standard input and output into our
 * own custom streams
 * 
 * @author Carmen Gómez Moreno
 *
 */
public class OutStreamWrapper extends PrintStream {

	private PrintStream realPrint;
	private static OutputStream outputStream = new ByteArrayOutputStream();
	private ConsoleWrapper console;
	private ReentrantLock writeLock;
	PrintStream stdout;
	PrintStream stderr;
	private OutputStream realPrintOS;

	/**
	 * 
	 * @param console : The console that created this OutStreamWrapper
	 */
	public OutStreamWrapper(ConsoleWrapper console) {

		super(outputStream);
		writeLock = new ReentrantLock();
		realPrintOS = new ByteArrayOutputStream();
		realPrint = new PrintStream(realPrintOS);
		this.console = console;

		stdout = System.out;
		stderr = System.err;

	}

	@Override
	public void write(byte[] b) throws IOException {

		realPrint.write(b);

	}

	/**
	 * Overrides OutputStream's write method in order to send output to the console gui
	 * This method allows the running instance of a program to read from a stream
	 * as i it read from System.in
	 * 
	 */
	@Override
	public void write(byte[] buf, int off, int len) {

		OutputStream output = new ByteArrayOutputStream();
		PrintStream outPrint = new PrintStream(output);
		outPrint.write(buf, off, len);
		outPrint.close();

		sendToConsole(output);
	}

	/*
	 * Overrides OutputStream's write method.
	 * We don't need to override this method but we need to so 
	 * this class can inherit from OutputStream
	 */
	
	@Override
	public void write(int b) {

		realPrint.write(b);

	}
	
	

	/**
	 * Transforms the running program's output into a String and sends it to the console gui
	 * @param realPrint
	 * @param outputStream
	 */
	
	private void sendToConsole( OutputStream outputStream) {
		//Prevent multiple writing at the same time
		writeLock.lock();
		//We put the output of the console into a a byte array
		byte[] inbaos = ((ByteArrayOutputStream) outputStream).toByteArray();
		//We create a new string from the bytes
		String s = new String(inbaos, StandardCharsets.UTF_8);
		try {
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		//We send the string we created to the gui 
		console.writeOutput(s);
		writeLock.unlock();
	
	}
}