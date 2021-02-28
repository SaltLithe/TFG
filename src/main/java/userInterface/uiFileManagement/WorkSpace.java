package userInterface.uiFileManagement;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * This class is used to represent a workspace in xml format
 * @author Usuario
 *
 */
@XmlRootElement(name = "WorkSpace")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class WorkSpace implements Serializable {

	
	private static final long serialVersionUID = 1L;

	private String name;
	private String path;

	public WorkSpace() {
		super();
	}

	@Override
	public String toString() {
		return "WorkSpace [name=" + name + ", path=" + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
