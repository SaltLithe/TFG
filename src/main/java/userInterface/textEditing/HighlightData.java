package userInterface.textEditing;

/**
 * Class containing the information of a highlighted text , includes information such as the beggining and end
 * of the highlight , the name of the user this highlight belongs to and the highlight object itself
 * @author Usuario
 *
 */
public class HighlightData {

	public int linestart;
	public Object highlight;
	public int lineend;
	
	/**
	 * 
	 * @param linestart : Where the highlight begins
	 * @param lineend : Where the highliht ends 
	 * @param name : The name of the client that created the highlight
	 * @param highlight : The highlight object
	 */
	public HighlightData (int linestart , int lineend, String name,  Object highlight) {
		
		this.linestart = linestart;
		this.highlight = highlight; 
		this.lineend = lineend;
		
		
	}
	
	
	
}
