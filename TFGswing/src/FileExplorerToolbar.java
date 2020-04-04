
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class FileExplorerToolbar extends JPanel {
//
	private FileExplorerPanel fileExplorerPanel;

	private JButton addClass;
	private JButton addScript;
	private DeveloperComponent developerComponent;
	private TextEditorToolbar textEditorToolbar;

	public FileExplorerToolbar(DeveloperComponent dp, TextEditorToolbar tet) {

		fileExplorerPanel = new FileExplorerPanel();
		textEditorToolbar = tet;
		developerComponent = dp;

		addClass = new JButton("Add Class");
		addScript = new JButton("Add Script");

		JPanel toolbarspace = new JPanel();
		toolbarspace.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolbarspace.add(addClass);
		toolbarspace.add(addScript);
		setLayout(new BorderLayout());
		fileExplorerPanel.setPreferredSize(new Dimension(this.WIDTH, this.HEIGHT));
		add(toolbarspace, BorderLayout.NORTH);
		add(fileExplorerPanel, BorderLayout.CENTER);

		addClass.setEnabled(false);
		addScript.setEnabled(false);

		addClass.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JDialog d = new JDialog();

				JLabel l = new JLabel("this is a dialog box");

				d.add(l);

				d.setSize(300, 300);

				d.setLayout(new BorderLayout());
				JLabel mensaje = new JLabel("Choose a class name");
				JTextField textbox = new JTextField();
				JButton ok = new JButton("Ok");

				mensaje.setSize(new Dimension(150, 50));
				ok.setSize(new Dimension(100, 50));

				d.add(mensaje, BorderLayout.NORTH);
				d.add(textbox, BorderLayout.CENTER);
				d.add(ok, BorderLayout.SOUTH);
				// set visibility of dialog

				ok.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						String nombrerecibido = textbox.getText();
						if (nombrerecibido != null) {
							d.dispose();
							developerComponent.createNewClassFile(nombrerecibido, null);
							createFileButton(nombrerecibido);

						}

					}

				});

				d.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				d.setVisible(true);

			}

		});

		addScript.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

			}

		});

	}

	public void createFileButton(String name) {

		if (!fileExplorerPanel.containsButton(name)) {
			JButton newbutton = new JButton(name);

			newbutton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					String newcontents = developerComponent.openFile(name, textEditorToolbar.getContents());
					textEditorToolbar.setContents(newcontents);
				}

			});

			fileExplorerPanel.addButton(newbutton);
			this.updateUI();
		}
	}

	public void enableToolbarButtons() {

		addClass.setEnabled(true);
		addScript.setEnabled(true);

	}

	public void updateAllButtons(File[] ficheros) {
		// TODO Auto-generated method stub
		for (int i = 0; i < ficheros.length; i++) {

			String nombreboton = ficheros[i].getName().replace(".java", "");
			this.createFileButton(nombreboton);

		}

	}

	public void enableTextEditor() {
		textEditorToolbar.enableEditor();
		textEditorToolbar.enableButtons();

	}

}
