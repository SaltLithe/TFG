
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class CreateSessionPanel extends JPanel {

	JLabel contraseña;
	JTextField campoContraseña;
	JButton botonOk;
	JLabel nombre;
	JTextField campoNombre;

	JPanel areaContraseña;
	JPanel areaNombre;

	JFrame frame;
	DeveloperMainFrame mf;

	public CreateSessionPanel(JFrame f) {

		// Añade todos los elementos necesarios para crear la sesión
		frame = f;
		setLayout(new BorderLayout());

		setSize(new Dimension(600, 600));

		contraseña = new JLabel("Set your password");
		campoContraseña = new JTextField(25);

		JLabel nombre = new JLabel("Choose your name");
		JTextField campoNombre = new JTextField(25);

		JButton botonOk = new JButton("OK");

		areaContraseña = new JPanel();
		areaNombre = new JPanel();
		areaContraseña.setLayout(new FlowLayout(FlowLayout.CENTER));
		areaNombre.setLayout(new FlowLayout(FlowLayout.CENTER));

		areaContraseña.add(contraseña);
		areaContraseña.add(campoContraseña);

		areaNombre.add(nombre);
		areaNombre.add(campoNombre);
		add(areaNombre, BorderLayout.NORTH);
		add(areaContraseña, BorderLayout.CENTER);

		add(botonOk, BorderLayout.SOUTH);

		botonOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				/*
				 * Si los campos con la información necesaria para crear una sesión no se han
				 * dejado en blanco llama al método para invocar una nueva ventana
				 */
				if (!campoNombre.getText().equals("") && !campoContraseña.getText().equals("")) {
					initMainFrame(campoNombre.getText());
				}

			}

		});

	}

	// Método que invoca en un nuevo hilo la nueva ventana y crea un ServerComponent
	// para pasárselo
	public void initMainFrame(String nombre) {

		SwingUtilities.invokeLater(new Runnable() {

			String n = nombre;

			@Override
			public void run() {

				DEBUG.debugmessage("SE HA INVOCADO EL HILO PARA LA INTERFAZ PRINCIPAL");

				ServerComponent server = new ServerComponent();

				String remoteaddr = server.getaddress();

				DeveloperMainFrame mainframe = new DeveloperMainFrame(null, server);

				mainframe.setSessionOwner(n);
				mainframe.setIpIndicator(remoteaddr);

			}

		});
		this.setVisible(false);

	}

}
