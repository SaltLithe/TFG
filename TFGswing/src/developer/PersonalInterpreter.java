package developer;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import bsh.EvalError;
import bsh.Interpreter;


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