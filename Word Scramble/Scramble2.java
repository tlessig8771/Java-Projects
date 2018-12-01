import java.util.*;
import java.io.*;

public class Scramble2 
{
	private Scanner filein;
	private String currentWord = "";
	private String scrambledWord = "";
	private ArrayList <String> words;
	private int wordsCount = 0;
	
	Scramble2(String gameFile) throws IOException
	{
		File file = new File(gameFile); //getting the file
		filein = new Scanner(file);
		
		words = new ArrayList <String>();
		
		while(filein.hasNextLine())
		{
			words.add(filein.nextLine());
			
		}	
		
		Random random = new Random();
		String temp;
		
		for(int k = 0; k < words.size();k++) //goes to the size of the word list
		{
			
			int rand = random.nextInt(words.size());
			
			temp = words.get(k);
			words.set(k, words.get(rand)); //swapping words to randomize the order of the words

			words.set(rand,temp);
			
		}
		
		
		filein.close();
	}
	
	public String getRealWord() //calls to scramble, gets next word if called again, returns null if reaches end of file
	{
		if(currentWord.compareTo("") != 0 && scrambledWord.compareTo("") == 0) //comparing the words to empty string
		{
			return currentWord;
		}		
		
		if(wordsCount < words.size())
		{
			currentWord = words.get(wordsCount);
			scrambledWord = "";
			return currentWord;
		}
		
		return null; //no more words
		
	}
	
	public String getScrambledWord() //scrambled word of most recent word returned by getRealWord, null if no call to above or above reaches end
	{
		if(scrambledWord.compareTo("") == 0 && currentWord.compareTo("") != 0)
		{
			char temp;
			Random scramGen = new Random(); //creation of the scrambled words here and below
			StringBuilder b = new StringBuilder(currentWord);
			
			
			for(int i=0;i< b.length();i++)
			{
				
				int rand = scramGen.nextInt(b.length());
				int rand2 = scramGen.nextInt(b.length());
				
				while(rand == rand2) //making sure rand and rand2 do not equal each other so that correct word isn't shown
				{
					rand = scramGen.nextInt(b.length()); //randomizing the character in the words
					rand2 = scramGen.nextInt(b.length());
				}
				
				temp = b.charAt(rand);
				b.replace(rand, rand+1, b.charAt(rand2)+""); //swapping characters to randomize the order of the word
				b.replace(rand2, rand2+1, temp+"");			
			}
			
			scrambledWord = b.toString();
			
			wordsCount++;

			return scrambledWord;
		}
		
		else if(currentWord.compareTo("") != 0)
		{
			return scrambledWord;
		}
		
		return null; 
	}
	
	public void reset()
	{
		currentWord = "";
		scrambledWord = "";
		wordsCount = 0;
		
		Random random = new Random();
		String temp;
		
		for(int k = 0; k < words.size();k++)
		{
			
			int rand = random.nextInt(words.size());
			
			temp = words.get(k);
			words.set(k, words.get(rand)); //swapping words to randomize the order of the words

			words.set(rand,temp);
			
		}
		
		
	}
}

