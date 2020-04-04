
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

	JLabel contraseņa;
	JTextField campoContraseņa;
	JButton botonOk;
	JLabel nombre;
	JTextField campoNombre;

	JPanel areaContraseņa;
	JPanel areaNombre;

	JFrame frame;

	public CreateSessionPanel(JFrame f) {

		frame = f;
		setLayout(new BorderLayout());

		setSize(new Dimension(600, 600));

		contraseņa = new JLabel("Set your password");
		campoContraseņa = new JTextField(25);

		JLabel nombre = new JLabel("Choose your name");
		JTextField campoNombre = new JTextField(25);

		JButton botonOk = new JButton("OK");

		areaContraseņa = new JPanel();
		areaNombre = new JPanel();
		areaContraseņa.setLayout(new FlowLayout(FlowLayout.CENTER));
		areaNombre.setLayout(new FlowLayout(FlowLayout.CENTER));

		areaContraseņa.add(contraseņa);
		areaContraseņa.add(campoContraseņa);

		areaNombre.add(nombre);
		areaNombre.add(campoNombre);
		add(areaNombre, BorderLayout.NORTH);
		add(areaContraseņa, BorderLayout.CENTER);

		add(botonOk, BorderLayout.SOUTH);

		botonOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!campoNombre.getText().equals("") && !campoContraseņa.getText().equals("")) {
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
				DeveloperMainFrame frame = new DeveloperMainFrame(null, server);

				frame.setSessionOwner(n);
				frame.setIpIndicator(remoteaddr);

				// DeveloperMainFrame mainFrame = new DeveloperMainFrame();

			}

		});

	}

}
