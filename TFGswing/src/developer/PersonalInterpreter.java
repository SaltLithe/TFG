package developer;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import bsh.Interpreter;

/*Esta clase contiene un objeto interpeter de la libreria BeanShell , es capaz de ejecutar
 * un string como si fuera un script (es muy parecido a como cuando programas en python) , pero aun no se
 * que uso le quiero dar 
 */
public class PersonalInterpreter {
	private ByteArrayOutputStream baos = new ByteArrayOutputStream();
	private PrintStream recordingStream = new PrintStream(baos);
	private Interpreter interpreter = new Interpreter();

	public void run(String code) {

		System.setOut(recordingStream);
		try {
			interpreter.eval(code);
		} catch (Exception e) {
			System.err.println(e);
		}

		System.err.println("Results : " + (baos.toString()));
	}

}