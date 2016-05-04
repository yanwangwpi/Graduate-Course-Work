package himalia.controller;

import himalia.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * This controller mainly downloads all the words into the protected area if the momento object does not exist.
 * @author saadjei
 *
 */
public class DownloadWordsController {

	String filePath;
	final int WholeNumber=17;
	final int reservoirSize = 100;
    ArrayList <String> reservoirList= new ArrayList<String>(reservoirSize); 
    ArrayList <String> reservoirWordTypes = new ArrayList<String>(reservoirSize); 
    
    /** This is the main constructor for the class */
	public DownloadWordsController(String filePath){
		this.filePath = filePath;
	}

	/** Return only the required number of words for the game. */
	public ArrayList<Word> getGameWords(){
		//get the list of words
		//open a file,
		ArrayList<Word> boardWords = new ArrayList<Word>();
		WordType t;
		try {
			sampler();
			for (int i=0; i<reservoirList.size();i++){
				//get the word type for this word
				String temp = reservoirWordTypes.get(i).toString();
				switch (temp.substring(0, temp.length()-1)){
				case "adjective":
					t = WordType.adj;
					break;
				case "adverb":
					t = WordType.adv;
					break;
				case "verb":
					t = WordType.verb;
					break;
				case "pronoun":
					t = WordType.pron;
					break;
				case "conjunction":
					t = WordType.conj;
					break;
				case "preposition":
					t = WordType.prep;
					break;
				case "prefix":
					t = WordType.prefix;
					break;
				case "suffix":
					t = WordType.suffix;
					break;
				case "noun":
					t = WordType.noun;
					break;
				case "determiner":
					t = WordType.determiner;
					break;
				case "pronouns":
					t = WordType.pron;
					break;	
				case "number":
					t = WordType.number;
					break;	
				default:
					t = WordType.unknown;
					break;
				}

				boardWords.add(new Word(-1,-1,-1,reservoirList.get(i), t));
			}
			
			return boardWords;
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}
		return null;
	}

	/** Sample a number of words from the file */
	@SuppressWarnings("resource")
	private void sampler () throws FileNotFoundException, IOException
    {
        String currentLine=null;
        //reservoirList is where our selected lines stored

        // we will use this counter to count the current line number while iterating
        int count=0; 

        Random ra = new Random();
        int randomNumber = 0;
        Scanner sc = new Scanner(new File(filePath)).useDelimiter("\n");
        while (sc.hasNext())
        {
            currentLine = sc.next();
            count ++;
        	String [] lineAttributes = currentLine.split(",");
            if (count<=reservoirSize)
            {
            	//split the current line into word and word type
                reservoirList.add(lineAttributes[0]);
                reservoirWordTypes.add(lineAttributes[1]);//change this to the correct word type of the word
            }
            else if ((randomNumber = (int) ra.nextInt(count))<reservoirSize)
            {
                reservoirList.set(randomNumber, lineAttributes[0]);
                reservoirWordTypes.set(randomNumber, lineAttributes[1]);

            }
        }
        sc.close();
        
    }
	
	/** Return all the words for the game. */
	public ArrayList<Word> getAllGameWords() {
		String currentLine=null;
        //reservoirList is where our selected words are stored
        ArrayList<Word> allwords=new ArrayList<Word>();
        Scanner sc = null;
		try {
			sc = new Scanner(new File(filePath)).useDelimiter("\n");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        while (sc.hasNext())
        {
	        currentLine = sc.next();
	        String [] lineAttributes = currentLine.split(",");
          
    		//split the current line into word and word type
		    Word w=new Word(0,0,0,lineAttributes[0],this.convertStringtypeToWordType(lineAttributes[1]));
		   
		    allwords.add(w);
        }
        sc.close();
		
		return allwords;
	}

	/**Return the type for a given word. */
	public WordType convertStringtypeToWordType(String s){
		WordType wt;
		switch(s.substring(0, s.length()-1)){
		case "adjective":
			wt = WordType.adj;
			break;
		case "adverb":
			wt = WordType.adv;
			break;
		case "verb":
			wt = WordType.verb;
			break;
		case "pronoun":
			wt = WordType.pron;
			break;
		case "conjunction":
			wt = WordType.conj;
			break;
		case "preposition":
			wt = WordType.prep;
			break;
		case "prefix":
			wt = WordType.prefix;
			break;
		case "suffix":
			wt = WordType.suffix;
			break;
		case "noun":
			wt = WordType.noun;
			break;
		case "determiner":
			wt = WordType.determiner;
			break;
		case "pronouns":
			wt = WordType.pron;
			break;					
		default:
			wt = WordType.unknown;
			break;
		}
		return wt;
	}
}