package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

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

	public JoinSessionPanel() {

		setLayout(new BorderLayout());

		setSize(new Dimension(600, 600));

		ip = new JLabel("Input your host IP");
		campoIp = new JTextField(25);

		contrase�a = new JLabel("Input your host Password ");
		campoContrase�a = new JTextField(25);

		JLabel nombre = new JLabel("Choose your name");
		campoNombre = new JTextField(25);

		JButton botonOk = new JButton("OK");

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
