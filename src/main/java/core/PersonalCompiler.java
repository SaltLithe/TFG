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
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.apache.commons.io.FileUtils;

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

	
	public PersonalCompiler() {

		support = PropertyChangeMessenger.getInstance();
		console = new ConsoleWrapper();
	}

	public String run(String className, URLData[] added ) {

		
		
		//Copiar ficheros
		
		File[] copied = new File[added.length];
		
		for (int i = 0 ;  i < added.length ; i ++) {
			
			File destFile = new File(added[i].project+ "\\"+ "bin\\" + added[i].name + ".java" );
			File sourceFile = new File(added[i].path);		
			    try {
			    	Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					e.printStackTrace();
				}
			    
			    copied[i] = destFile;
			
			
		}
		
		//Compilar
		//Comprobar que esta el jdk
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		if (compiler == null) {
			DEBUG.debugmessage("SE REQUIERE EL USO DE JAVA JDK ");
		} else {
			DEBUG.debugmessage("SE PUEDE COMPILAR EL FICHERO");
		}
		
		
		String[] compiling = new String[copied.length];
		DEBUG.setExecuting();
		System.setOut(console.outPrint);
		System.setErr(console.errPrint);
		System.setIn(console.inStream);
		boolean cantcompile = false;

		for (int i = 0 ; i < copied.length ; i ++) {
		String actualname = copied[i].getName().substring(copied[i].getName().lastIndexOf("\\")+1 , copied[i].getName().lastIndexOf("."));
		String extension = copied[i].getName().substring(copied[i].getName().lastIndexOf("."),copied[i].getName().length());	
		
		if(extension.equals(".java")) {
		
	
			compiling[i] = added[i].project + "\\bin\\" + actualname + ".java";
		
		
		}
		}
		
		
		int compilationResult = compiler.run(null, null, null, compiling);
		if (compilationResult != 0) {
			cantcompile = true; 
		} 
		
		if(!cantcompile) {
		
		
		
		URL[] binfolder = new URL[1]; 
		try {
			binfolder[0] = new File(added[0].project+ "\\bin").toURI().toURL();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		URLClassLoader classLoader = new URLClassLoader(binfolder);
		
	
			try {
				String rawname = className.substring(0 , className.lastIndexOf("."));
				Class<?> cls = Class.forName(rawname  , true, classLoader);
				Object instance = cls.newInstance();
				Class[] argTypes = new Class[] { String[].class };
				Method method = cls.getDeclaredMethod("main", argTypes);

				String[] args = new String[0];

				String[] mainArgs = Arrays.copyOfRange(args, 0, args.length);

				method.invoke(instance, (Object) mainArgs);

			} catch (Exception e) {
				System.setOut(stdout);
				System.setErr(stderr);
				System.setIn(stdin);
				e.printStackTrace();

				

				DEBUG.debugmessage("HA OCURRIDO UN ERROR A LA HORA DE COMPILAR EXTERNO AL CODIGO");
			}
			
		for(String s : compiling) {
			File f = new File(s);
			f.delete();
		}

		DEBUG.unsetExecuting();
		System.setOut(stdout);
		System.setErr(stderr);
		System.setIn(stdin);

		DEBUG.debugmessage("SE HA ACABADO LA EJECUCION");

		return null;
	
	}else {
		DEBUG.unsetExecuting();
		System.setOut(stdout);
		System.setErr(stderr);
		System.setIn(stdin);
		return null;
	}

	}

	public void reactivateRunningProccess(String retrieved) {
		console.setLastRead(retrieved);
		console.releaseSemaphore();

	}
	
	public void addPath(String s) throws Exception {
	    File f = new File(s);
	    URL u = f.toURL();
	    URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
	    Class urlClass = URLClassLoader.class;
	    Method method = urlClass.getDeclaredMethod("addURL", new Class[]{URL.class});
	    method.setAccessible(true);
	    method.invoke(urlClassLoader, new Object[]{u});
	}
}