
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class JoinSessionPanel extends JPanel {

	JLabel contrase�a;
	JTextField campoContrase�a;
	JButton botonOk;
	JLabel ip;
	JLabel nombre;
	JTextField campoNombre;
	JTextField campoIp;

	JPanel areaContrase�a;
	JPanel areaNombre;
	JPanel areaIP;
	JFrame frame;

	public JoinSessionPanel(JFrame f) {

		// A�ade todos los elementos necesarios para unirse a una sesi�n
		frame = f;
		setLayout(new BorderLayout());

		setSize(new Dimension(600, 600));

		ip = new JLabel("Input your host IP");
		campoIp = new JTextField(25);

		contrase�a = new JLabel("Input your host Password ");
		campoContrase�a = new JTextField(25);

		JLabel nombre = new JLabel("Choose your name");
		campoNombre = new JTextField(25);

		JButton botonOk = new JButton("OK");

		botonOk.addActionListener(new ActionListener() {

			/*
			 * Si los campos con la informaci�n necesaria para unirse a una sesi�n no se han
			 * // dejado en // blanco llama al m�todo para invocar una nueva ventana
			 */

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!campoNombre.getText().equals("") && !campoContrase�a.getText().equals("")
						&& !campoIp.getText().equals("")) {
					initMainFrame(campoNombre.getText(), campoIp.getText());
				}
				setVisible(false);
			}

			/*
			 * M�todo que invoca en un nuevo hilo la nueva ventana, crea un Socket // y lo
			 * conecta a la ip dada
			 */
			private void initMainFrame(String nombre, String ad) {

				// Esto podria ser algo distinto a un socket pero como funciona con el
				// Asynchronous server socket channel pues lo dejamos asi

				SwingUtilities.invokeLater(new Runnable() {

					String n = nombre;
					String address = ad;

					@Override
					public void run() {
						DEBUG.debugmessage("SE HA INVOCADO EL HILO PARA LANZAR LA INTERFAZ PRINCIPAL DEL CLIENTE");
						Socket socket;
						DEBUG.debugmessage("SE VA A INTENTAR CONECTAR EL CLIENTE AL SERVIDOR");
						try {

							socket = new Socket(address, 8080);
							DEBUG.debugmessage("EL SOCKET DEL CLIENTE SE HA CONECTADO CON EXITO");

						} catch (IOException e) {
							DEBUG.debugmessage("NO SE HA LOGRADO CONECTAR EL CLIENTE AL SERVIDOR");

							socket = null;
							e.printStackTrace();
						}

						/*
						 * Esto es un mensaje de prueba del cliente , en el futuro no deberia usarse
						 * pues el server al conectarse un cliente debe asignarle un id , y es mejor que
						 * el cliente no mande mensajes sin tener un id con el que identificarse
						 */

						OutputStream output = null;
						try {
							output = socket.getOutputStream();
						} catch (IOException e) {
							e.printStackTrace();
						}
						PrintWriter writer = new PrintWriter(output, true);
						writer.println("This is a message sent to the server");
						DeveloperMainFrame frame = new DeveloperMainFrame(socket, null);

						frame.setSessionOwner(n);

					}

				});
			}
		});

		areaIP = new JPanel();
		areaContrase�a = new JPanel();
		areaNombre = new JPanel();
		areaContrase�a.setLayout(new FlowLayout(FlowLayout.CENTER));
		areaNombre.setLayout(new FlowLayout(FlowLayout.CENTER));
		areaIP.setLayout(new FlowLayout(FlowLayout.CENTER));

		areaIP.add(ip);
		areaIP.add(campoIp);

		areaContrase�a.add(contrase�a);
		areaContrase�a.add(campoContrase�a);
		areaContrase�a.add(botonOk);

		areaNombre.add(nombre);
		areaNombre.add(campoNombre);

		add(areaNombre, BorderLayout.NORTH);
		add(areaIP, BorderLayout.CENTER);
		add(areaContrase�a, BorderLayout.SOUTH);

	}
}
