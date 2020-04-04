
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
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

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!campoNombre.getText().equals("") && !campoContrase�a.getText().equals("")
						&& !campoIp.getText().equals("")) {
					initMainFrame(campoNombre.getText(), campoIp.getText());
					// frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				}

			}

			private void initMainFrame(String nombre, String ad) {

				SwingUtilities.invokeLater(new Runnable() {

					String n = nombre;
					String address = ad;

					@Override
					public void run() {
						Socket socket;
						try {
							socket = new Socket(address, 8080);

						} catch (IOException e) {
							socket = null;
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						String remoteaddr;

						//
						DeveloperMainFrame frame = new DeveloperMainFrame(socket, null);

						frame.setSessionOwner(n);
						frame.setIpIndicator(address);

						// DeveloperMainFrame mainFrame = new DeveloperMainFrame();

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
