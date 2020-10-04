package fileManagement;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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