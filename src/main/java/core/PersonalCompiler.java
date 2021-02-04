package core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.Arrays;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import console.ConsoleWrapper;
import fileManagement.FILE_PROPERTIES;
import userInterface.ObserverActions;
import userInterface.PropertyChangeMessenger;

/**
 * Class in charge of receiving file paths from the file system to compile and
 * run code from This class is essentially performing arbitrary code execution
 * using the Java Compiler
 * 
 * @author Usuario
 *
 */
public class PersonalCompiler implements PropertyChangeListener {

	// Public
	public InputStream in;
	public OutputStream out;
	public OutputStream err;

	// Private
	ByteArrayOutputStream outbaos = new ByteArrayOutputStream();
	ByteArrayOutputStream errbaos = new ByteArrayOutputStream();
	PrintStream out3 = new PrintStream(errbaos);
	PrintStream out2 = new PrintStream(outbaos);
	ConsoleWrapper console;
	PrintStream stdout = System.out;
	PrintStream stderr = System.err;
	InputStream stdin = System.in;
	private String[] compiling;
	private PropertyChangeMessenger support;

	public PersonalCompiler() {

		support = PropertyChangeMessenger.getInstance();
		console = new ConsoleWrapper();
	}

	public void run(String className, URLData[] added) {

	
		try {
			// Get reference to the bin directory
			File bindir = new File(added[0].project + "\\bin");
			// Create a bin dyrectory if the directory does not exist for any reason
			if (!bindir.exists()) {

				bindir.mkdir();
			
				final Path file = Paths.get(bindir.getAbsolutePath());
				final UserDefinedFileAttributeView view = Files.getFileAttributeView(file,
						UserDefinedFileAttributeView.class);

				// Write the necessary metadata to the newly created folder
				String property = FILE_PROPERTIES.binProperty;
				byte[] bytes = null;

				try {
					bytes = property.getBytes("UTF-8");
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				final ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
				writeBuffer.put(bytes);
				writeBuffer.flip();
				try {
					view.write(property, writeBuffer);
				} catch (IOException e) {
					e.printStackTrace();
				}

				
				// Update the file explorer
				Object[] message = {added[0].project , "bin" , added[0].project};
				support.notify(ObserverActions.UPDATE_PROJECT_TREE_ADD, message);
			}

			File[] copied = new File[added.length];
			// Copy the files to bin and keep a reference of all of the files we copied
			for (int i = 0; i < added.length; i++) {

				File destFile = new File(added[i].project + "\\" + "bin\\" + added[i].name + ".java");

				File sourceFile = new File(added[i].path);
				try {
					Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					e.printStackTrace();
				}

				copied[i] = destFile;

			}

			// Check that we can access the compiler
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			if (compiler == null) {
				DEBUG.debugmessage("SE REQUIERE EL USO DE JAVA JDK ");
			} else {
				DEBUG.debugmessage("SE PUEDE COMPILAR EL FICHERO");
			}

			// Prepare an array to pass onto the compiler
			// This compiler needs the names of all of the classes it has to compile
			// together
			// If a class depends on another class that is not inside this array it will not
			// be able to find it
			compiling = new String[copied.length];
			DEBUG.setExecuting();
			// Divert the standard streams
			System.setOut(console.getOutPrint());
			System.setErr(console.getErrPrint());
			System.setIn(console.getInStream());
			boolean cantcompile = false;

			// Fill this array
			for (int i = 0; i < copied.length; i++) {
				String actualname = copied[i].getName().substring(copied[i].getName().lastIndexOf("\\") + 1,
						copied[i].getName().lastIndexOf("."));
				String extension = copied[i].getName().substring(copied[i].getName().lastIndexOf("."),
						copied[i].getName().length());

				if (extension.equals(".java")) {

					compiling[i] = added[i].project + "\\bin\\" + actualname + ".java";

				}
			}

			// Run the compiler and save the result
			int compilationResult = compiler.run(null, null, null, compiling);
			// Check if the compiler has encountered any errors
			if (compilationResult != 0) {
				cantcompile = true;
			}

			// If there are no errors start running code
			if (!cantcompile) {

				// Create the array holding the url of the bin folder where the entrypoint class
				// is compiled
				URL[] binfolder = new URL[1];
				try {
					binfolder[0] = new File(added[0].project + "\\bin").toURI().toURL();
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				}
				// Create a class loader pointing to the binfolder
				URLClassLoader classLoader = new URLClassLoader(binfolder);

				try {

					// get the name of the entrypoint class
					String rawname = className.substring(0, className.lastIndexOf("."));
					// Create a class object using our classloader pointing to the bin folder and
					// passing
					// it a reference to the name of the entrypoint class
					Class<?> cls = Class.forName(rawname, true, classLoader);
					// Instantiate the class
					@SuppressWarnings("deprecation")
					Object instance = cls.newInstance();
					// Declare the usual main arguments
					@SuppressWarnings("rawtypes")
					Class[] argTypes = new Class[] { String[].class };

					// Using the instantiated class and the type of arguments main needs, retrieve
					// main method
					Method method = cls.getDeclaredMethod("main", argTypes);

					// Create the arguments for main
					String[] args = new String[0];
					String[] mainArgs = Arrays.copyOfRange(args, 0, args.length);
					try {
						// Tell the gui to activate the terminate button just before running
						support.notify(ObserverActions.ENABLE_TERMINATE, null);
						// run the main method
						method.invoke(instance, (Object) mainArgs);
					} catch (Exception e) {
						console.reset();

					}

					// If there are any errors , reset the standard streams
				} catch (Exception e) {
					System.setOut(stdout);
					System.setErr(stderr);
					System.setIn(stdin);
					e.printStackTrace();

				}

				// Delete the copies of the files we copied to bin
				safetyDelete();

				DEBUG.unsetExecuting();
				System.setOut(stdout);
				System.setErr(stderr);
				System.setIn(stdin);

			} else {
				DEBUG.unsetExecuting();
				System.setOut(stdout);
				System.setErr(stderr);
				System.setIn(stdin);
				console.reset();

			}

		} catch (Exception e) {
			DEBUG.unsetExecuting();
			System.setOut(stdout);
			System.setErr(stderr);
			System.setIn(stdin);

			console.reset();

		}
	}

	/**
	 * Method used to reactivate a running process that has stopped in order to wait
	 * for inputs from user
	 * 
	 * @param retrieved
	 */
	public void reactivateRunningProccess(String retrieved) {
		console.setLastRead(retrieved);
		console.releaseSemaphore();

	}

	/**
	 * Support method that will delete the copied java files from bin
	 */
	public void safetyDelete() {
		if (compiling != null) {

			for (String s : compiling) {
				File f = new File(s);
				f.delete();
			}

		}

	}

	/**
	 * Implementation of propertyChange from propertyChangeListener , used to listen
	 * to ui notification
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		ObserverActions action = ObserverActions.valueOf(evt.getPropertyName());
		switch (action) {
		case SAFETY_DELETE:

			safetyDelete();
			break;
		default:
			break;
		}

	}
}