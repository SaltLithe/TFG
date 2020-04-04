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

	JLabel contraseņa;
	JTextField campoContraseņa;
	JButton botonOk;
	JLabel ip;
	JLabel nombre;
	JTextField campoNombre;
	JTextField campoIp;

	JPanel areaContraseņa;
	JPanel areaNombre;
	JPanel areaIP;

	public JoinSessionPanel() {

		setLayout(new BorderLayout());

		setSize(new Dimension(600, 600));

		ip = new JLabel("Input your host IP");
		campoIp = new JTextField(25);

		contraseņa = new JLabel("Input your host Password ");
		campoContraseņa = new JTextField(25);

		JLabel nombre = new JLabel("Choose your name");
		campoNombre = new JTextField(25);

		JButton botonOk = new JButton("OK");

		areaIP = new JPanel();
		areaContraseņa = new JPanel();
		areaNombre = new JPanel();
		areaContraseņa.setLayout(new FlowLayout(FlowLayout.CENTER));
		areaNombre.setLayout(new FlowLayout(FlowLayout.CENTER));
		areaIP.setLayout(new FlowLayout(FlowLayout.CENTER));

		areaIP.add(ip);
		areaIP.add(campoIp);

		areaContraseņa.add(contraseņa);
		areaContraseņa.add(campoContraseņa);
		areaContraseņa.add(botonOk);

		areaNombre.add(nombre);
		areaNombre.add(campoNombre);

		add(areaNombre, BorderLayout.NORTH);
		add(areaIP, BorderLayout.CENTER);
		add(areaContraseņa, BorderLayout.SOUTH);

	}
}
