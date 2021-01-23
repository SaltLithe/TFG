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

/**
 * UI class that contains a panel for the graphical part of the tree nodes This
 * class helps the cell tree renderer to render the correct icon for each node
 * 
 * @author Carmen Gómez Moreno
 *
 */
@SuppressWarnings("serial")
public class NodeMiniPanel extends JPanel {

	/**
	 * 
	 * @param name     : The name of the node
	 * @param hasFocus : Flag indicating if this node has focus
	 * @param sel      : Flag indicating if this node is selected
	 * @param leaf     : Flag indcating if this node is a leaf
	 * @param path     : Path of this node
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

	/**
	 * Support method used to get the extension from the nodes path
	 * 
	 * @param path : The path for this node
	 * @return a String containing the extension TODO this method could go in a
	 *         static class
	 */
	private String getExtension(String path) {
		String returning = null;
		try {
			returning = path.substring(path.lastIndexOf(".") + 1);
		} catch (Exception e) {
		}
		return returning;
	}

	/**
	 * Support method that , given a folder path , returns the type of metadata
	 * 
	 * @param path : The path of the folder
	 * @return enum FILE_TYPE indicating the type of folder
	 */
	private FILE_TYPE getMetaData(String path) {

		FILE_TYPE returning = null;

		final Path file = Paths.get(path);
		final UserDefinedFileAttributeView view = Files.getFileAttributeView(file, UserDefinedFileAttributeView.class);
		ByteBuffer readBuffer = null;
		boolean success = false;
		int count = 0;
		while (count < FILE_PROPERTIES.properties.length && !success) {

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
				} else {
					count++;
				}
			} catch (Exception e) {
				count++;
			}

		}
		return returning;
	}

	/**
	 * Support method that gets either the folder or file type of whatever path it
	 * receives , calls the other two private methods inside this class
	 * 
	 * @param path : The path to check
	 * @return a FILE_TYPE enum with either a folder or file type
	 */
	private FILE_TYPE getFolderType(String path) {

		FILE_TYPE returning = null;
		File f = new File(path);
		if (!f.isFile()) {
			returning = getMetaData(path);
			if (returning == null) {
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
