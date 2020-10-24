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

import core.DEBUG;
import userInterface.ICON_MESSAGE;
import userInterface.TEXT_MESSAGE;
import userInterface.uiElementsManager;
import userInterface.warningDialog;

public class WorkSpaceManager {

	
	
	private static WorkSpaceManager instance = null;
	
	
	public static WorkSpaceManager getInstance() {
		if(instance == null) {
			
			instance = new WorkSpaceManager(); 
		}
		return instance;
	}
	
	private WorkSpaceManager() {
		
	}
	
	private boolean rewriteWorkSpaces(List<WorkSpace> ws) {
		File workspacefile = new File("src/main/resources/WorkSpaces.xml");
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
		
	}
	
	
	public boolean addWorkSpace(WorkSpace workspace , JFrame frame) {

		
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
			if (w.getPath().equals( workspace.getPath())) {
				repeated = true;

			} else {
				counter++;
			}

		}
		if (!repeated) {
			ws.add(workspace);
			
			return rewriteWorkSpaces(ws);

			
		} else {
			JOptionPane.showMessageDialog(frame,
				    "A workspace in the same path already exists in the list.",
				    "Can't add this Workspace",
				    JOptionPane.ERROR_MESSAGE);
			
			return false; 
		}
	}

	public  List<WorkSpace> getAllWorkSpaces() {

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

	public  String getFilePath() {
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

	
	
	
	public  void deleteWorkSpace(int id, String name ,JFrame frame) {
		// TODO Auto-generated method stub
		ArrayList<WorkSpace> wslist  = (ArrayList<WorkSpace>) getAllWorkSpaces(); 
		boolean hasName = false;
		int count = 0 ; 
		while ( count < wslist.size() && !hasName) {
			if(wslist.get(count).getName().equals(name)) {
				hasName = true;
			}
			count++;
		}
		if(hasName) {
		wslist.remove(id);
		rewriteWorkSpaces(wslist);
		
		JOptionPane.showMessageDialog(frame,
			    "Deleted successfully.");
		}
		else {
		
			JOptionPane.showMessageDialog(frame,
				    "Delete operation failed, could not find a WorkSpace with the name: "+ name+".", "Delete error",
				    JOptionPane.ERROR_MESSAGE);
		}
	}

	public boolean locateWorkSpace(String path, String name , JFrame frame) {
		
		ArrayList<WorkSpace> wslist  = (ArrayList<WorkSpace>) getAllWorkSpaces(); 

		boolean hasName = false;
		int count = 0 ; 
		while ( count < wslist.size() && !hasName) {
			if(wslist.get(count).getName().equals(name)) {
				hasName = true;
			}else {
			count++;
			}
		}
		
		if(hasName) {
			JOptionPane.showMessageDialog(frame,
				    "Located successfully.");
			
			wslist.get(count).setPath(path);
			this.rewriteWorkSpaces(wslist);
					
			return true; 
		}else {
			JOptionPane.showMessageDialog(frame,
				    "Locate operation failed, could not find a WorkSpace with the name: "+ name+" matching to the path you have selected. "
				    		+ "Try renaming the folder or adding the Workspace again.", "Locate error",
				    JOptionPane.ERROR_MESSAGE);
			return false; 
		}
		// TODO Auto-generated method stub
		
	}


	

}
