package userInterface.fileNavigation;

import javax.swing.JPanel;

import core.DEBUG;
import fileManagement.FILE_PROPERTIES;
import fileManagement.FILE_TYPE;
import fileManagement.Project;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.Optional;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class NodeMiniPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public NodeMiniPanel(String name, Boolean hasFocus, Boolean sel, Boolean leaf, String path) {

		FILE_TYPE type = getFolderType(path);
		String iconpath = "Icons/";

		switch (type) {
		case PROJECT_FOLDER:
			iconpath = iconpath + "projectFolder_icon.png";
			break;
		case SRC_FOLDER:
			iconpath = iconpath + "srcFolder_icon.png";

			break;
		case BIN_FOLDER:
			iconpath = iconpath + "binFolder_icon.png";

			break;
		case PACKAGE_FOLDER:
			iconpath = iconpath + "package_icon.png";

			break;
		case JAVA_FILE:
			iconpath = iconpath + "javaFile_icon.png";

			break;
		case ANY_FILE:
			iconpath = iconpath + "anyFile_icon.png";

			break;
		case ANY_FOLDER:
			iconpath = iconpath + "anyFolder_icon.png";

			break;
		default:
			iconpath = iconpath + "anyFile_icon.png";

			break;

		}

		ImageIcon icon = new ImageIcon(iconpath);
		JLabel iconLabel = new JLabel(icon);
		JLabel nameLabel = new JLabel(name);
		add(iconLabel);
		add(nameLabel);

		setOpaque(false);

		setVisible(true);
	}

	private String getExtension(String path) {
		String returning = null;
		try {
			returning = path.substring(path.lastIndexOf(".") + 1);
		} catch (Exception e) {
		}
		return returning;
	}

	
	
	private FILE_TYPE getMetaData(String path) {
		
		FILE_TYPE returning = null; 
		
		final Path file = Paths.get(path);
		final UserDefinedFileAttributeView view = Files.getFileAttributeView(file,
				UserDefinedFileAttributeView.class);
		ByteBuffer readBuffer = null;
		boolean success = false;
		int count = 0;
		while(count < FILE_PROPERTIES.properties.length && !success) {
			

			try {
				readBuffer = ByteBuffer.allocate(view.size(FILE_PROPERTIES.properties[count]));
			} catch (IOException e) {
			}
			try {
				view.read(FILE_PROPERTIES.properties[count], readBuffer);
			} catch (IOException e) {
			}
		
			try {
				readBuffer.flip();
				final String valueFromAttributes = new String(readBuffer.array(), "UTF-8");
				if (valueFromAttributes.equals(FILE_PROPERTIES.properties[count])) {
					switch (FILE_PROPERTIES.properties[count]) {
						
					case FILE_PROPERTIES.projectProperty:
						returning = FILE_TYPE.PROJECT_FOLDER;
						break;
					case FILE_PROPERTIES.srcProperty:
						returning = FILE_TYPE.SRC_FOLDER;
						break;
					case FILE_PROPERTIES.binProperty:
						returning = FILE_TYPE.BIN_FOLDER;
						break;
					default:
						break;
					
					
					}
					success = true; 
				}else {
					count++;
				}
			} catch (Exception e) {
				count++;
			}
					
		}	
		return returning; 
	}
	
	
	private FILE_TYPE getFolderType(String path) {

		
		
		FILE_TYPE returning = null;
		File f = new File(path);
		if (!f.isFile()) {
			returning = getMetaData(path);
			if(returning == null) {
				returning = FILE_TYPE.ANY_FOLDER;
			}
		} else {
			String extension = getExtension(path);
			if (extension != null) {
				switch (extension) {
				case "java":
					returning = FILE_TYPE.JAVA_FILE;
					break;

				default:
					returning = FILE_TYPE.ANY_FILE;
					break;

				}

			} else {
				returning = FILE_TYPE.ANY_FILE;

			}
		}

		return returning;
	}

}
