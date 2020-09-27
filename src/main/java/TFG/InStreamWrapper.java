package TFG;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;

public class InStreamWrapper extends InputStream {

	private ConsoleWrapper console;
	private InputStream input;
	ThreadPoolExecutor executor;
	Semaphore semaphore;

	PrintStream stdout;
	PrintStream stderr;

	public InStreamWrapper(ConsoleWrapper console) {
		stdout = System.out;
		stderr = System.err;
		this.console = console;
		executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
		semaphore = new Semaphore(0);

	}

	public void setLastRead(String lastread) {

		// use ByteArrayInputStream to get the bytes of the String and convert them to
		// InputStream.

		input = new ByteArrayInputStream(lastread.getBytes(Charset.forName("UTF-8")));

	}

	public void releaseSemaphore() {
		semaphore.release();

	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		console.support.notify(ObserverActions.ENABLE_CONSOLE_PANEL, null, null);
		console.support.notify(ObserverActions.ENABLE_READING_LISTENER, null, null);

		try {
			semaphore.acquire();
		} catch (InterruptedException e) {

			System.setOut(stdout);
			System.setErr(stderr);
			e.printStackTrace();
		}
		/*
		 * byte[] bytes = IOUtils.toByteArray(input); for (int i = 0; i < bytes.length;
		 * i++) { b[i] = bytes[i]; }
		 */

		int nread = input.read(b, off, len);
		try {
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return nread;
	}

	@Override
	public int read() throws IOException {

		console.support.notify(ObserverActions.ENABLE_CONSOLE_PANEL, null, null);
		console.support.notify(ObserverActions.ENABLE_READING_LISTENER, null, null);
		/*
		 * try { semaphore.acquire(); } catch (InterruptedException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
		int nread = input.read();
		return nread;
	}

}
