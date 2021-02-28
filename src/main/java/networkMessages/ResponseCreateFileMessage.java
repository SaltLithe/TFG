package networkMessages;

import java.io.Serializable;
/**
 * Class acting as a message to indicate to a client that it must create a workspace element
 * @author Carmen Gómez Moreno
 *
 */
public class ResponseCreateFileMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5597576436024776409L;

	public ResponseCreateFileMessage() {
	}

	public String path;
	public String type;
	public String contents;
	public String newname;

}
