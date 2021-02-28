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
	private static final long serialVersionUID = 6106677430760056507L;

	
	public WriteMessage(String ownerName) {
		this.ownerName = ownerName;
	}

	public String ownerName;
	public String path; 
	public String changes;
	public int offset; 
	public int lenght;
	public boolean adding;

}
