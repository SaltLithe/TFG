
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

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

	public CreateSessionPanel(JFrame f) {

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
				if (!campoNombre.getText().equals("") && !campoContraseña.getText().equals("")) {
					initMainFrame(campoNombre.getText());
					// frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				}

			}

		});

	}

	public void initMainFrame(String nombre) {

		SwingUtilities.invokeLater(new Runnable() {

			String n = nombre;

			@Override
			public void run() {
				ServerSocket server;
				try {
					server = new ServerSocket(8080);

				} catch (IOException e) {
					server = null;
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String remoteaddr;
				try {
					remoteaddr = InetAddress.getLocalHost().getHostAddress();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					remoteaddr = null;
					e.printStackTrace();
				}
//
				DeveloperMainFrame frame = new DeveloperMainFrame(server);

				frame.setSessionOwner(n);
				frame.setIpIndicator(remoteaddr);

				// DeveloperMainFrame mainFrame = new DeveloperMainFrame();

			}

		});

	}

}
