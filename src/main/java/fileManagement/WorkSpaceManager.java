package fileManagement;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import core.DeveloperComponent;

/**
 * Class that manages workspaces by reading them from the xml list and loading
 * their information into object. This class can also read and write from this
 * list, allowing the user to add , create and remove workspaces
 * 
 * @author Carmen Gómez Moreno
 *
 */
public class WorkSpaceManager {

	private static WorkSpaceManager instance = null;

	/**
	 * Method serving as constructor
	 * 
	 * @return The instance of this object
	 */
	public static WorkSpaceManager getInstance() {
		if (instance == null) {

			instance = new WorkSpaceManager();
		}
		return instance;
	}

	/**
	 * Method used to add a workspace to the list
	 * 
	 * @param workspace : The workspace object to add
	 */
	public void addWorkSpace(WorkSpace workspace) {

		List<WorkSpace> ws = null;
		try {
			ws = getAllWorkSpaces();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (ws == null) {

			ws = new ArrayList<>();
		}
		boolean repeated = false;
		int counter = 0;
		while (counter < ws.size() && !repeated) {
			WorkSpace w = ws.get(counter);
			if (w.getPath().equals(workspace.getPath())) {
				repeated = true;

			} else {
				counter++;
			}

		}
		if (!repeated) {
			ws.add(workspace);
			rewriteWorkSpaces(ws);

		}
	}

	/**
	 * Add a new workspace to the workspace selection menu
	 * 
	 * @param workspace : The workspace object to add
	 * @param frame     : The frame where the menu is
	 * @return if the operation is successful
	 */
	public boolean addWorkSpace(WorkSpace workspace, JFrame frame) {

		List<WorkSpace> ws = null;
		try {
			ws = getAllWorkSpaces();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (ws == null) {

			ws = new ArrayList<>();
		}
		boolean repeated = false;
		int counter = 0;
		while (counter < ws.size() && !repeated) {
			WorkSpace w = ws.get(counter);
			if (w.getPath().equals(workspace.getPath())) {
				repeated = true;

			} else {
				counter++;
			}

		}
		if (!repeated) {
			ws.add(workspace);

			return rewriteWorkSpaces(ws);

		} else {
			JOptionPane.showMessageDialog(frame, "A workspace in the same path already exists in the list.",
					"Can't add this Workspace", JOptionPane.ERROR_MESSAGE);

			return false;
		}
	}

	/**
	 * Method that returns a list with all of the workspaces in the xml list
	 * 
	 * @return a list containing workspace objects
	 */
	public List<WorkSpace> getAllWorkSpaces() {

		Unmarshaller jaxbUnmarshaller = null;
		JAXBContext jaxbContext = null;
		WorkSpaces ws = null;
		try {
			jaxbContext = JAXBContext.newInstance(WorkSpaces.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		try {
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		try {
			File tempWS = new File("./WorkSpaces.xml");
			ws = (WorkSpaces) jaxbUnmarshaller.unmarshal(tempWS);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			return ws.getWorkSpaces();
		} catch (Exception e) {
			return new ArrayList<>();
		}

	}

	/**
	 * Method that returns a path choosen by the user via usage of a JFileChooser
	 * 
	 * @return the path the user chose
	 */
	public String getFilePath() {
		String path = null;
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new java.io.File("."));
		fileChooser.setDialogTitle("Select a Folder to Open");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
		int returnValue = fileChooser.showSaveDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			if (fileChooser.getSelectedFile().isDirectory()) {
				File filepath = fileChooser.getSelectedFile();
				path = filepath.getAbsolutePath();
			}
		}

		return path;
	}

	/**
	 * Method used to delete a workspace from the menu list and the xml list
	 * 
	 * @param id    : Id of this workspace representation in the list
	 * @param name  : Name of the workspace
	 * @param frame : Frame where the menu containing this workspace's
	 *              representation is
	 */
	public void deleteWorkSpace(int id, String name, JFrame frame) {
		ArrayList<WorkSpace> wslist = (ArrayList<WorkSpace>) getAllWorkSpaces();
		boolean hasName = false;
		int count = 0;
		while (count < wslist.size() && !hasName) {
			if (wslist.get(count).getName().equals(name)) {
				hasName = true;
			}
			count++;
		}
		if (hasName) {
			wslist.remove(id);
			rewriteWorkSpaces(wslist);
			if (frame != null) {
				JOptionPane.showMessageDialog(frame, "Deleted successfully.");
			}
		} else {
			if (frame != null) {
				JOptionPane.showMessageDialog(frame,
						"Delete operation failed, could not find a WorkSpace with the name: " + name + ".",
						"Delete error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Method used to locate a workspace , a workspace needs locating when its path
	 * listed in the xml list does not contain a workspace folder with the name in
	 * the list
	 * 
	 * @param path  : The path of the workspace
	 * @param name  : The name of this workspace
	 * @param frame : The frame of the menu where the workspace representation is
	 *              located
	 * @return
	 */
	public boolean locateWorkSpace(String path, String name, JFrame frame) {

		ArrayList<WorkSpace> wslist = (ArrayList<WorkSpace>) getAllWorkSpaces();

		boolean hasName = false;
		int count = 0;
		while (count < wslist.size() && !hasName) {
			if (wslist.get(count).getName().equals(name)) {
				hasName = true;
			} else {
				count++;
			}
		}

		if (hasName) {
			JOptionPane.showMessageDialog(frame, "Located successfully.");

			wslist.get(count).setPath(path);
			this.rewriteWorkSpaces(wslist);

			return true;
		} else {
			JOptionPane.showMessageDialog(frame,
					"Locate operation failed, could not find a WorkSpace with the name: " + name
							+ " matching to the path you have selected. "
							+ "Try renaming the folder or adding the Workspace again.",
					"Locate error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

	}

	/**
	 * Method used to start the main app without a workspace
	 */
	public void startMainApp() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				new DeveloperComponent(null);
			}

		});
		t.start();
	}

	/**
	 * Method used to start the main app with a workspace
	 * 
	 * @param tempID : The id of the workspace representation in the menu
	 * @param frame  : The frame where the menu is
	 */
	public void startMainApp(int tempID, JFrame frame) {

		List<WorkSpace> ws = this.getAllWorkSpaces();

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				new DeveloperComponent(ws.get(tempID));
			}

		});
		t.start();
		frame.dispose();

	}

	/**
	 * Protect the constructor
	 */
	private WorkSpaceManager() {

	}

	/**
	 * Method used to rewrite the xml file when a workspace has been added or
	 * removed
	 * 
	 * @param ws : List used to write the file
	 * @return if the operation has been completed successfully
	 */
	private boolean rewriteWorkSpaces(List<WorkSpace> ws) {

		File workspacefile = new File("./WorkSpaces.xml");
		WorkSpaces newws = new WorkSpaces();
		newws.setWorkSpaces(ws);

		JAXBContext jaxbContext = null;
		try {
			jaxbContext = JAXBContext.newInstance(WorkSpaces.class);
		} catch (JAXBException e1) {
			e1.printStackTrace();
		}
		Marshaller jaxbMarshaller = null;
		try {
			jaxbMarshaller = jaxbContext.createMarshaller();
		} catch (JAXBException e1) {
			e1.printStackTrace();
		}

		try {
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		} catch (PropertyException e1) {
			e1.printStackTrace();
		}

		// Marshal the employees list in file
		try {
			jaxbMarshaller.marshal(newws, workspacefile);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return true;

	}

}
