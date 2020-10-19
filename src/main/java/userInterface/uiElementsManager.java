package userInterface;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import core.Command;
import fileManagement.WorkSpaces;

public class uiElementsManager {
	
	private static  uiElementsManager instance ; 
	private static HashMap<TEXT_MESSAGE, String>textCollection;
	private static HashMap <ICON_MESSAGE, String>iconCollection; 
	
	private uiElements texts;
	private uiElements icons;
	
	public static uiElementsManager getInstance() {

		if (instance == null) {
			instance = new uiElementsManager();
		}
		return instance;
	}

	private uiElementsManager() {
		textCollection = new HashMap<TEXT_MESSAGE, String>(); 
		iconCollection = new HashMap<ICON_MESSAGE, String>(); 
		
		Unmarshaller jaxbUnmarshaller = null;
		JAXBContext jaxbContext = null;
		texts = null;
		try {
			jaxbContext = JAXBContext.newInstance(uiElements.class);
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
			texts = (uiElements) jaxbUnmarshaller.unmarshal(new File("src/main/resources/TextMessages.xml"));
			icons = (uiElements) jaxbUnmarshaller.unmarshal(new File("src/main/resources/IconMessages.xml"));
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		for (uiElement e : texts.getuiElements()) {
			textCollection.put(TEXT_MESSAGE.valueOf(e.getKey()), e.getName());
			
		}
		for(uiElement e : icons.getuiElements()) {
			iconCollection.put(ICON_MESSAGE.valueOf(e.getKey()), e.getName());
		}

	}
	
	
	public static String getStringNameFromKey(TEXT_MESSAGE key) {

	return textCollection.get(key);
}
	
	public static String getIconNameFromKey(ICON_MESSAGE key) {

	return iconCollection.get(key);
}
}