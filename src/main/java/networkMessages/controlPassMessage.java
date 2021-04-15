package networkMessages;

import java.io.Serializable;

public class controlPassMessage implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 5504686703303418765L;

	public controlPassMessage(String name) {
		this.name = name; 
	}

	public String name;
}
