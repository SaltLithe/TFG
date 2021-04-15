package networkMessages;


import java.io.Serializable;
/**
 * Class acting as a message to indicate to users that they have to update text from files
 * @author Carmen Gómez Moreno
 *
 */
public class WriteMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 141679437617562355L;

	
	public WriteMessage(String ownerName) {
		this.ownerName = ownerName;
	}

	public String ownerName = "";
	public String path = "";
	public String changes = "";
	public int offset = -1;
	public int lenght = -1;
	public boolean adding = true;
	public int filler = -1;

}
