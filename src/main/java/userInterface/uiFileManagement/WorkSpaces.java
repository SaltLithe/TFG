package userInterface.uiFileManagement;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * Class used in the writing and reading from the xml workspace file
 * @author Usuario
 *
 */
@XmlRootElement(name = "WorkSpaces")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkSpaces {
	@XmlElement(name = "WorkSpace")
	private List<WorkSpace> workspaces = null;

	public List<WorkSpace> getWorkSpaces() {
		return workspaces;
	}

	public void setWorkSpaces(List<WorkSpace> workspaces) {
		this.workspaces = workspaces;
	}
}