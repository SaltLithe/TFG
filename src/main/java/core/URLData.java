package core;

public class URLData {
	
	public String path;
	public boolean hasBeenModified;
	public String name;
	public String project; 
	
	
	
	public URLData(String path , Boolean hasBeenModified , String name , String project) {
		
		this.path = path ; 
		this.hasBeenModified = hasBeenModified; 
		this.project = project;
		this.name = name; 
		
		
		
	}
	
	
	
}
