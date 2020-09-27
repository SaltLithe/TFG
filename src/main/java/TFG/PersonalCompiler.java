package TFG;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

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

	public PersonalCompiler() {

		support = PropertyChangeMessenger.getInstance();
		console = new ConsoleWrapper();
	}

	public String run(String source, String className, String currentpath) {

		File folder = new File(currentpath + "/");
		// String path = folder.getPath() + "/" + className + ".java";
		String path = folder.getPath() + "/" + className;
		className = className.replace(".java", "");

		File sourceFile = new File(path);

		/*
		 * out2.flush(); out3.flush(); outbaos.reset(); errbaos.reset();
		 */

		try {
			Files.write(sourceFile.toPath(), source.getBytes(StandardCharsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
		}

		DEBUG.debugmessage("SE VA A INTENTAR COMPILAR EL FICHERO EN EL DIRECTORIO " + sourceFile.getPath());

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		if (compiler == null) {

			DEBUG.debugmessage("SE REQUIERE EL USO DE JAVA JDK ");

		} else {

			DEBUG.debugmessage("SE PUEDE COMPILAR EL FICHERO");

		}

		// despues de darle a run se crea un archivo .class que habria que eliminar

		// System.setOut(out2);
		// System.setErr(out3);
		DEBUG.setExecuting();
		System.setOut(console.outPrint);
		System.setErr(console.errPrint);
		System.setIn(console.inStream);
		int compilationResult = compiler.run(null, null, null, sourceFile.getPath());
		if (compilationResult == 0) {

			try {

				URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { folder.toURI().toURL() });
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

		} else {

			System.setOut(stdout);
			System.setErr(stderr);
			System.setIn(stdin);

			System.err.println("HA OCURRIDO UN ERROR A LA HORA DE COMPILAR DEBIDO AL CODIGO");
		}

		/*
		 * 
		 * String results = null;
		 * 
		 * String porque = errbaos.toString(); String señorporque = outbaos.toString();
		 * if (!errbaos.toString().equals("")) { results = errbaos.toString(); } else {
		 * 
		 * results = outbaos.toString(); }
		 * 
		 */

		DEBUG.unsetExecuting();
		System.setOut(stdout);
		System.setErr(stderr);
		System.setIn(stdin);

		DEBUG.debugmessage("SE HA ACABADO LA EJECUCION");
		String resultspath = folder.getPath() + "/" + className + ".class";
		File resultsFile = new File(resultspath);
		resultsFile.delete();

		/*
		 * ArrayList<Object> list = new ArrayList<Object>(); list.add(results);
		 * support.notify(ObserverActions.CONSOLE_PANEL_CONTENTS, null, list);
		 */
		return null;
	}

	public void reactivateRunningProccess(String retrieved) {
		console.setLastRead(retrieved);
		console.releaseSemaphore();

	}
}