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

	PrintStream stdout = System.out;
	PrintStream stderr = System.err;

	public String run(String source, String className, String currentpath) {

		File folder = new File(currentpath + "/");
		String path = folder.getPath() + "/" + className + ".java";
		File sourceFile = new File(path);

		out2.flush();
		out3.flush();
		outbaos.reset();
		errbaos.reset();

		try {
			Files.write(sourceFile.toPath(), source.getBytes(StandardCharsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
		}

		DEBUG.debugmessage("SE VA A INTENTAR COMPILAR EL FICHERO EN EL DIRECTORIO " + sourceFile.getPath());

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		if (compiler == null) {
			System.setOut(stdout);
			System.setErr(stderr);

			DEBUG.debugmessage("SE REQUIERE EL USO DE JAVA JDK ");

		} else {
			System.setOut(stdout);
			System.setErr(stderr);
			DEBUG.debugmessage("SE PUEDE COMPILAR EL FICHERO");

		}

		// despues de darle a run se crea un archivo .class que habria que eliminar
		System.setOut(out2);
		System.setErr(out3);
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
				System.setOut(stdout);
				System.setErr(stderr);

			} catch (Exception e) {
				e.printStackTrace();
				System.setOut(stdout);
				System.setErr(stderr);
				DEBUG.debugmessage("HA OCURRIDO UN ERROR A LA HORA DE COMPILAR EXTERNO AL CODIGO");
			}

		} else {
			System.setOut(stdout);
			System.setErr(stderr);
			System.err.println("HA OCURRIDO UN ERROR A LA HORA DE COMPILAR DEBIDO AL CODIGO");
		}

		String results = null;
		if (!errbaos.toString().equals("")) {
			results = errbaos.toString();
		} else {

			results = outbaos.toString();
		}

		String resultspath = folder.getPath() + "/" + className + ".class";
		File resultsFile = new File(resultspath);
		resultsFile.delete();

		DEBUG.debugmessage("EL CODIGO SE HA PODIDO EJECUTAR CORRECTAMENTE CON RESULTADOS" + results);

		return results;
	}
}