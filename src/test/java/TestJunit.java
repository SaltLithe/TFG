
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Test;

import core.PersonalCompiler;
import core.URLData;
import fileManagement.FILE_TYPE;
import fileManagement.FileManager;
import fileManagement.Project;
import fileManagement.WorkSpace;
import fileManagement.WorkSpaceManager;
import network.ResponseCreateFileMessage;

public class TestJunit {

	String testFolder = "C:\\Users\\Usuario\\Documents\\GitHub\\TFG\\TFG\\src\\test\\resources\\unitTestFolder";
	String failedExecution = "C:\\Users\\Usuario\\Documents\\GitHub\\TFG\\TFG\\src\\test\\resources\\compilationFails";
	String successExecution ="C:\\Users\\Usuario\\Documents\\GitHub\\TFG\\TFG\\src\\test\\resources\\successfulCompilation";
	
	String testWorkspace = "testWorkSpace";
	String fillerTestWorkspace = "fillerTestWorkspace";
	String fillerTestProject = "fillerTestProject";
	String testContent = "This is some test content";

	


	  @Test 
	  
	  public void testCompileFails() {
	  
	  PersonalCompiler compiler = new PersonalCompiler(); URLData[] testData_1 = new URLData[1]; 
	  testData_1[0] = new URLData(failedExecution + "\\" + "CompilationFailApp.java" , false, "CompilationFailApp", testFolder); 
	  int result = compiler.run("CompilationFailApp.java", testData_1);
	  assertTrue(result == 1);
	 }
	  


	  @Test public void testCompileSuccess() {
	  
	  PersonalCompiler compiler = new PersonalCompiler(); 
	  URLData[] testData_2 = new URLData[1]; 
	  testData_2[0] = new URLData("C:\\Users\\Usuario\\Documents\\GitHub\\TFG\\TFG\\src\\test\\resources\\unitTestFolder\\" + "SuccessfulCompilationApp.java" , true, "SuccessfulCompilationApp", "C:\\Users\\Usuario\\Documents\\GitHub\\TFG\\TFG\\src\\test\\resources\\unitTestFolder"); 

	  int result = compiler.run("SuccessfulCompilationApp.java", testData_2);
	  assertTrue(result == 0);
	 }
	  

	  
	 
	 

	private boolean workSpacecheck(String name) {
		boolean result = false;
		List<WorkSpace> readResult = WorkSpaceManager.getInstance().getAllWorkSpaces();

		for (WorkSpace ws : readResult) {
			if (ws.getName().equals(name)) {
				result = true;
			}
		}
		return result;
	}

	// Create a workspace
	@Test
	public void testAddRemoveWorkSpace() {
		WorkSpace filler = new WorkSpace();
		filler.setPath(testFolder + "\\" + fillerTestWorkspace);
		filler.setName(fillerTestWorkspace);
		WorkSpaceManager.getInstance().addWorkSpace(filler);
		WorkSpace ws = new WorkSpace();
		ws.setName(testWorkspace);
		ws.setPath(testFolder + "\\" + testWorkspace);

		assertFalse(workSpacecheck(testWorkspace));
		WorkSpaceManager.getInstance().addWorkSpace(ws);

		assertTrue(workSpacecheck(testWorkspace));
		WorkSpaceManager.getInstance().deleteWorkSpace(1, testWorkspace, null);
		WorkSpaceManager.getInstance().deleteWorkSpace(0, fillerTestWorkspace, null);

	}

	@Test
	public void testAddRemoveProject() {
		WorkSpace filler = new WorkSpace();
		filler.setPath(testFolder + "\\" + fillerTestWorkspace);
		filler.setName(testWorkspace);
		WorkSpaceManager.getInstance().addWorkSpace(filler);

		FileManager fm = new FileManager();
		fm.newProject(fillerTestProject, filler, true, false);
		assertTrue(new File(filler.getPath() + "\\" + fillerTestProject).exists());
		fm.deleteFile(fillerTestProject, filler.getPath() + "\\" + fillerTestProject, true, null, null);
		assertFalse(new File(filler.getPath() + "\\" + fillerTestProject).exists());
		WorkSpaceManager.getInstance().deleteWorkSpace(0, testWorkspace, null);

	}

	@Test
	public void testEditScanWorkSpace() {
		WorkSpace filler = new WorkSpace();
		filler.setPath(testFolder + "\\" + fillerTestWorkspace);
		filler.setName(testWorkspace);
		WorkSpaceManager.getInstance().addWorkSpace(filler);

		FileManager fm = new FileManager();
		fm.newProject(fillerTestProject, filler, true, false);
		assertTrue(new File(filler.getPath() + "\\" + fillerTestProject).exists());

		fm.writeFile(filler.getPath() + "\\" + fillerTestProject + "\\" + "newFile", testContent, FILE_TYPE.CLASS_FILE);
		fm.writeFolder(filler.getPath() + "\\" + fillerTestProject, FILE_TYPE.SRC_FOLDER, false, "newFolder",
				filler.getPath());
		List<Project> results = fm.scanWorkSpace(filler);
		assertTrue(results.get(0).getName().equals(fillerTestProject));

		List<ResponseCreateFileMessage> fileResults = fm.scanAndReturn(filler.getPath(), filler.getName());

		boolean found = false;
		for (ResponseCreateFileMessage m : fileResults) {
			if (m.contents != null && m.contents.equals(testContent)) {
				found = true;
			}
		}
		assertTrue(found);

		boolean projectFound = false;
		for (ResponseCreateFileMessage m : fileResults) {
			if (m.path.contains(fillerTestProject)) {
				projectFound = true;
			}
		}

		assert (projectFound);
		fm.deleteFile(fillerTestProject, results.get(0).getFullPath(), true, null, null);
		assertFalse(new File(results.get(0).getFullPath()).exists());
		WorkSpaceManager.getInstance().deleteWorkSpace(0, testWorkspace, null);

	}
	
	

}