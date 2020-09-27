package TFG;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.ReentrantLock;

public class OutStreamWrapper extends PrintStream {

	private ConsoleWrapper console;
	static String nada = "";
	private static OutputStream outputStream = new ByteArrayOutputStream();
	public PrintStream realPrint;

	private ReentrantLock writeLock;
	PrintStream stdout;
	PrintStream stderr;
	private OutputStream outputStream2 = new ByteArrayOutputStream();

	public OutStreamWrapper(ConsoleWrapper console) {

		super(outputStream);
		writeLock = new ReentrantLock();
		realPrint = new PrintStream(outputStream2);
		this.console = console;

		stdout = System.out;
		stderr = System.err;

	}

	private void sendToConsole(PrintStream realPrint, OutputStream outputStream2) {
		writeLock.lock();
		byte[] inbaos = ((ByteArrayOutputStream) outputStream2).toByteArray();

		String s = new String(inbaos, StandardCharsets.UTF_8);
		try {
			outputStream2.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		realPrint.flush();
		try {
			outputStream2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		realPrint.close();
		console.writeOutput(s);
		writeLock.unlock();

	}

	@Override
	public void write(byte[] b) throws IOException {

		realPrint.write(b);

		sendToConsole(null, null);

		// TODO Auto-generated method stub

	}

	@Override
	public void write(byte[] buf, int off, int len) {

		OutputStream output = new ByteArrayOutputStream();
		PrintStream realPrint = new PrintStream(output);
		realPrint.write(buf, off, len);
		realPrint.close();

		sendToConsole(realPrint, output);
	}

	@Override
	public void write(int b) {

		realPrint.write(b);
		sendToConsole(null, null);

	}

}