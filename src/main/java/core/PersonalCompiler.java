package core;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import console.ConsoleWrapper;
import userInterface.PropertyChangeMessenger;

public class PersonalCompiler {

	public InputStream in;
	public OutputStream out;
	public OutputStream err;
	ByteArrayOutputStream outbaos = new ByteArrayOutputStream();
	ByteArrayOutputStream errbaos = new ByteArrayOutputStream();
	PrintStream out3 = new PrintStream(errbaos);
	PrintStream out2 = new PrintStream(outbaos);
	private PropertyChangeMessenger support;
	ConsoleWrapper console;

	PrintStream stdout = System.out;
	PrintStream stderr = System.err;
	InputStream stdin = System.in;

	ConcurrentHashMap <String, URLClassLoader> loaders; 
	
	public PersonalCompiler() {

		support = PropertyChangeMessenger.getInstance();
		console = new ConsoleWrapper();
	}

	public String run(String className, String[] added ) {

			
		URL[] addedURLS = new URL[added.length];
		for(int i = 0 ; i < added.length ; i ++) {
			try {
				addedURLS[i] = new File(added[i]).toURI().toURL();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		URLClassLoader classLoader = new URLClassLoader(addedURLS);

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		if (compiler == null) {

			DEBUG.debugmessage("SE REQUIERE EL USO DE JAVA JDK ");

		} else {

			DEBUG.debugmessage("SE PUEDE COMPILAR EL FICHERO");

		}

		DEBUG.setExecuting();
		System.setOut(console.outPrint);
		System.setErr(console.errPrint);
		System.setIn(console.inStream);
		

			try {

				Class<?> cls = Class.forName(className, true, classLoader);
				Object instance = cls.newInstance();
				@SuppressWarnings("rawtypes")
				Class[] argTypes = new Class[] { String[].class };
				Method method = cls.getDeclaredMethod("main", argTypes);

				String[] args = new String[0];

				String[] mainArgs = Arrays.copyOfRange(args, 0, args.length);

				method.invoke(instance, (Object) mainArgs);

			} catch (Exception e) {
				e.printStackTrace();

				System.setOut(stdout);
				System.setErr(stderr);
				System.setIn(stdin);

				DEBUG.debugmessage("HA OCURRIDO UN ERROR A LA HORA DE COMPILAR EXTERNO AL CODIGO");
			}

		DEBUG.unsetExecuting();
		System.setOut(stdout);
		System.setErr(stderr);
		System.setIn(stdin);

		DEBUG.debugmessage("SE HA ACABADO LA EJECUCION");

		return null;
	}

	public void reactivateRunningProccess(String retrieved) {
		console.setLastRead(retrieved);
		console.releaseSemaphore();

	}
}