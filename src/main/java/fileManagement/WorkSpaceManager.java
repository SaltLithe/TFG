package fileManagement;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import core.DEBUG;
import userInterface.ICON_MESSAGE;
import userInterface.TEXT_MESSAGE;
import userInterface.uiElementsManager;
import userInterface.warningDialog;

public class WorkSpaceManager {

	public static boolean addWorkSpace(WorkSpace workspace) {

		File workspacefile = new File("src/main/resources/WorkSpaces.xml");
		List<WorkSpace> ws = null;
		uiElementsManager uiEM = uiElementsManager.getInstance();
		try {
			ws = getAllWorkSpaces();
		} catch (Exception e) {
		}
		if (ws == null) {

			ws = new ArrayList<WorkSpace>();
		}
		boolean repeated = false;
		int counter = 0;
		while (counter < ws.size() && !repeated) {
			WorkSpace w = ws.get(counter);
			if (w.getPath() == workspace.getPath()) {
				repeated = true;

			} else {
				counter++;
			}

		}
		if (!repeated) {
			ws.add(workspace);
			WorkSpaces newws = new WorkSpaces();
			newws.setWorkSpaces(ws);

			JAXBContext jaxbContext = null;
			try {
				jaxbContext = JAXBContext.newInstance(WorkSpaces.class);
			} catch (JAXBException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Marshaller jaxbMarshaller = null;
			try {
				jaxbMarshaller = jaxbContext.createMarshaller();
			} catch (JAXBException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			} catch (PropertyException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// Marshal the employees list in file
			try {
				jaxbMarshaller.marshal(newws, workspacefile);
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true; 
		} else {

			DEBUG.debugmessage("Esta repetido el workspace");
			warningDialog diag = new warningDialog(uiEM.getStringNameFromKey(TEXT_MESSAGE.REPEATED_WORKSPACE),uiEM.getIconNameFromKey(ICON_MESSAGE.WARNING_ICON));
			diag.setVisible(true);
			return false; 
		}
	}

	public static List<WorkSpace> getAllWorkSpaces() {

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

		try {
			return ws.getWorkSpaces();
		} catch (Exception e) {
			return null;
		}

	}

	public static String getFilePath() {
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
				System.out.println("You selected the directory: " + fileChooser.getSelectedFile());
			}
		}

		return path;
	}

}
