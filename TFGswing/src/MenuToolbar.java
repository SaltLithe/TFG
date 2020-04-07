
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MenuToolbar extends JPanel {
//
	private JButton folder;
	private JButton save;
	private JButton saveAll;
	private FileExplorerToolbar fileExplorerToolbar;
	private DeveloperComponent developerComponent;
	private JButton startserver;

	public void runServer() {

		developerComponent.runServer();

	}

	public MenuToolbar(DeveloperComponent dc, FileExplorerToolbar fet, TextEditorToolbar textEditorToolbar) {

		developerComponent = dc;
		fileExplorerToolbar = fet;

		folder = new JButton("Open Folder");
		save = new JButton("Save File");
		saveAll = new JButton("Save All");
		startserver = new JButton("Start Server");

		save.setEnabled(false);
		saveAll.setEnabled(false);

		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(folder);
		add(save);
		add(saveAll);
		add(startserver);

		startserver.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				runServer();
				folder.setEnabled(true);
			}

		});

		folder.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				developerComponent.selectFocusedFolder();
				fileExplorerToolbar.enableToolbarButtons();
				fileExplorerToolbar.updateAllButtons(developerComponent.getAllFiles());
				fileExplorerToolbar.enableTextEditor();
				save.setEnabled(true);
				saveAll.setEnabled(true);

			}

		});

		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					developerComponent.saveCurrentFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

		});

		saveAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					developerComponent.saveAllFiles();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

		});

	}

}
