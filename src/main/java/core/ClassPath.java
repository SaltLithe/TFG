package core;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Class used to keep paths to all of the classes of a single project in order
 * to retrieve them later so the user can run code.
 * 
 * @author Carmen Gómez Moreno
 *
 */
public class ClassPath {

	HashSet<String> classPaths;
	String project;

	/**
	 * 
	 * @param project : The relative path of the project the classes belong to
	 * @param classes : Structure containing the paths of the classes to be compiled
	 *                in order to run code for the specified project
	 */
	public ClassPath(String project, String[] classes) {
		this.project = project;
		classPaths = new HashSet<String>();
		edit(classes, null);

	}

/**
 * Returns the url data needed for running code.
 * @return URLData[] containing the effective path of the files of the project this classpath represents
 */
	public URLData[] getClassPath() {
		URLData[] files = new URLData[classPaths.size()];
		Iterator<String> i = classPaths.iterator();
		int count = 0;
		while (i.hasNext()) {

			String fullpath = i.next();
			String partialname = fullpath.substring(fullpath.lastIndexOf("\\") + 1, fullpath.length());
			String actualname = partialname.substring(0, partialname.lastIndexOf("."));
			files[count] = new URLData(fullpath, true, actualname, project);

			count++;
		}
		return files;
	}
/**
 * Method that simultaneously adds and removes classes , used in constructor to fill the classpath
 * This method will avoid any file that is not a .java file
 * 
 * @param addedclasses : Array of classes to add. Can be null
 * @param removedclasses : Array of classes to remove. Can be null
 */
	public void edit(String[] addedclasses, String[] removedclasses) {

		//Add classes if there are any available
		if (addedclasses != null) {
			for (String added : addedclasses) {
				String extension = checkExtension(added);

				if (extension != null && extension.equals(".java")) {

					if (!classPaths.contains(added)) {
						classPaths.add(added);
					}
				}

			}
		}
		//Remove classes if there are any to remove
		if (removedclasses != null) {

			for (String removed : removedclasses) {
				if (classPaths.contains(removed)) {
					classPaths.remove(removed);
				}
			}
		}

	}

	/**
	 * Support method that, given a path , returns the extension of the file it points to 
	 * @param path
	 * @return
	 */
	private String checkExtension(String path) {

		String returning = null;
		try {
			String extension = path.substring(path.lastIndexOf("."), path.length());
			returning = extension;
		} catch (Exception e) {
		}
		return returning;
	}

}
