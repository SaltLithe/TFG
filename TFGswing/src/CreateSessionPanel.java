
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

	JLabel contrase�a;
	JTextField campoContrase�a;
	JButton botonOk;
	JLabel nombre;
	JTextField campoNombre;

	JPanel areaContrase�a;
	JPanel areaNombre;

	JFrame frame;
	DeveloperMainFrame mf;

	public CreateSessionPanel(JFrame f) {

		// A�ade todos los elementos necesarios para crear la sesi�n
		frame = f;
		setLayout(new BorderLayout());

		setSize(new Dimension(600, 600));

		contrase�a = new JLabel("Set your password");
		campoContrase�a = new JTextField(25);

		JLabel nombre = new JLabel("Choose your name");
		JTextField campoNombre = new JTextField(25);

		JButton botonOk = new JButton("OK");

		areaContrase�a = new JPanel();
		areaNombre = new JPanel();
		areaContrase�a.setLayout(new FlowLayout(FlowLayout.CENTER));
		areaNombre.setLayout(new FlowLayout(FlowLayout.CENTER));

		areaContrase�a.add(contrase�a);
		areaContrase�a.add(campoContrase�a);

		areaNombre.add(nombre);
		areaNombre.add(campoNombre);
		add(areaNombre, BorderLayout.NORTH);
		add(areaContrase�a, BorderLayout.CENTER);

		add(botonOk, BorderLayout.SOUTH);

		botonOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				/*
				 * Si los campos con la informaci�n necesaria para crear una sesi�n no se han
				 * dejado en blanco llama al m�todo para invocar una nueva ventana
				 */
				if (!campoNombre.getText().equals("") && !campoContrase�a.getText().equals("")) {
					initMainFrame(campoNombre.getText());
				}

			}

		});

	}

	// M�todo que invoca en un nuevo hilo la nueva ventana y crea un ServerComponent
	// para pas�rselo
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
