package userInterface;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.formdev.flatlaf.FlatDarkLaf;

import fileManagement.WorkSpace;
import fileManagement.WorkSpaces;

public class workSpaceSelect extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */
	public workSpaceSelect() {

		try {
			UIManager.setLookAndFeel(new FlatDarkLaf());
		} catch (Exception ex) {
			System.err.println("Failed to initialize LaF");
		}
//aaaa
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel = new JLabel("LAUNCH");
		contentPane.add(lblNewLabel, BorderLayout.NORTH);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.WEST);

		JButton btnNewButton = new JButton("New WorkSpace");
		panel.add(btnNewButton);
		this.setVisible(true);

		Unmarshaller jaxbUnmarshaller = null;
		JAXBContext jaxbContext = null;
		WorkSpaces ws = null;
		try {
			jaxbContext = JAXBContext.newInstance(WorkSpaces.class);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			ws = (WorkSpaces) jaxbUnmarshaller.unmarshal(new File("src/main/resources/WorkSpaces.xml"));
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(ws.getWorkSpaces().size());

		for (WorkSpace workspace : ws.getWorkSpaces()) {
			System.out.println(workspace.getName());
			System.out.println(workspace.getPath());
		}
	}
}
