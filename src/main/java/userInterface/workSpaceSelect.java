package userInterface;

import java.awt.BorderLayout;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.Text;
import org.jdom2.input.SAXBuilder;

import com.formdev.flatlaf.FlatDarkLaf;

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

		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		try {
			doc = builder.build("/resources/WorkSpaces.xml");
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (Iterator it = doc.getRootElement().getContent().iterator(); it.hasNext();) {
			Content content = (Content) it.next();
			if (content instanceof Text) {
				System.out.println("[Text: " + ((Text) content).getTextNormalize());
			} else {
				System.out.println(content.toString());
			}
		}

	}

}
