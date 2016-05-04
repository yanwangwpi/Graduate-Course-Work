package himalia.controller;

import himalia.model.Word;

import java.util.ArrayList;

import junit.framework.TestCase;

public class TestDownloadWordsController extends TestCase{
	public void testDownlaodWordsController(){
		String currentWorkingDirectory = System.getProperty("user.dir");
		
		//"M://Backup//desktop//Courses//Design of Software Systems//Project//GameWordsAndTypes.csv"
		
		DownloadWordsController dwc = new DownloadWordsController(currentWorkingDirectory + "//GameWordsAndTypes.csv");
		
		
		ArrayList<Word> bwords = dwc.getGameWords();
		
		assertEquals(bwords.size(),100);
	}
}
