package userInterface;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "uiElements")
@XmlAccessorType(XmlAccessType.FIELD)
public class uiElements {
	@XmlElement(name = "uiElements")
	private List<uiElement> uielements = null;
	
	public uiElements() {
		
		uielements = new ArrayList<uiElement>(); 
	}

	public List<uiElement> getuiElements() {
		return uielements;
	}

	public void setuiElements(List<uiElement> uiElements) {
		this.uielements = uielements;
	}
}