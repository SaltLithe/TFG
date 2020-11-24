package core;

import java.util.HashSet;
import java.util.Iterator;

public class ClassPath {

	HashSet<String> classPaths;
	String project;
	
	
	public ClassPath(String project, String[] classes) {
		this.project = project;
		classPaths = new HashSet<String>();
		edit(classes , null);
		
		// TODO Auto-generated constructor stub
	}

	public URLData[] getClassPath() {
		URLData[] files = new URLData[classPaths.size()];
		Iterator<String> i = classPaths.iterator();
		int count = 0 ; 
		while(i.hasNext()) {
			
			String fullpath = i.next();
			String partialname = fullpath.substring(fullpath.lastIndexOf("\\")+1 , fullpath.length()); 
			String actualname  = partialname.substring(0 , partialname.lastIndexOf("."));
			files[count] = new URLData( fullpath, true , actualname , project) ;
			
			count++;
		
		}
		return files;  
	}
	
private String checkExtension(String path) {
		
	String returning = null; 
	try {
		String extension = path.substring(path.lastIndexOf("."),path.length());
		returning = extension;
	}catch(Exception e) {}
	return returning;
	}
	
	public void edit(String[] addedclasses, String[] removedclasses) {
		
		if(addedclasses != null) {
			for(String added : addedclasses) {
				String extension = checkExtension(added);

				if(extension != null && extension.equals(".java")) {
					
				
				if(!classPaths.contains(added)) {
					classPaths.add(added);
				}
				}
			
			}
		}
			if(removedclasses != null) {
	
			for(String removed : removedclasses) {
				if(classPaths.contains(removed)) {
					classPaths.remove(removed);
				}
			}
			}
		
		// TODO Auto-generated method stub
		
	}

}
