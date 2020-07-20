package TFG;
import java.net.UnknownHostException;

import javax.swing.SwingUtilities;

/*La clase app es la primera clase de toda aplicacion , en nuestro caso inicializa el primer componente
 * de interfaz que contendr� los elemntos necesarios para lanzar la interfaz principal de la aplicaci�n
 */

public class App {

	public static void main(String[] args) throws UnknownHostException {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				@SuppressWarnings("unused")
				InitFrame frame = new InitFrame();
				DEBUG.debugmessage("INTERFAZ INICIALIZADA");

			}

		});

	}

}
