import java.io.*;
import java.util.*;

public class ac_test
{
	
	public static void main(String [] args) throws IOException
	{
		Scanner wordScan; //initialize variables that will be used
		Scanner userScan;
		ArrayList<Long> timeArr = new ArrayList<>();
		ArrayList<String> suggestWords = new ArrayList<>();
		long time;
		long time2; 
		long totalTime;
		String buildWord = "";
		boolean flag = true;
		
		
		Scanner character = new Scanner(System.in);
		String piece;
		
		DLB myDLB = new DLB(); //initialize new DLBS one for dictionary and one for the user
		DLB userDLB = new DLB();
		
		File wordFile = new File("dictionary.txt"); //read in the dictionary file and make a scanner ready to add words
		wordScan = new Scanner(wordFile);
		
		File userFile = new File("user_history.txt"); //create the file
		if(userFile.exists())
		{
			userScan = new Scanner(userFile); //if we exist, go through the file and add to the userDLB
			while(userScan.hasNextLine()) 
			{
				String userWord = userScan.nextLine();
				userDLB.add(userWord);
			}
			userScan.close();
		}
		
		while(wordScan.hasNextLine())				//while there is another word in the file we will add it to the DLB
		{
			String newWord = wordScan.nextLine();
			myDLB.add(newWord);
		}	

		wordScan.close(); //close the scanner which is reading the file
		
		System.out.print("Enter your first character: ");
		
		
		while(flag) //while flag is true, user enters next character we search for it and submit predictions
		{
			piece = character.nextLine(); //grab the character the user types
			buildWord += piece;	
			
			if(piece.equals("!"))
			{
				flag = false;
				break;
			}
			
			else if(piece.equals("$") || piece.equals("1") || piece.equals("2") || piece.equals("3") || piece.equals("4") || piece.equals("5"))
			{
				//myDLB.add(word);;  MOVE TO BOTTOM
				//we need to add users word if not in dictionary to extra dlb
				//we need to add users selected to choice to extra dlb
					//can do so by accessing arraylist of predictions (i-1) which would be indices 0 - 4
					//so i in this case will be piece where we turn string piece to int and subtract 1 to get value in arraylist
				
				switch(piece) //switch case to add words to the user DLB
				{
				case "$":
					buildWord = buildWord.substring(0, buildWord.length()-1);
					System.out.println("\n\t"+buildWord+" ADDED TO DICTIONARY");
					userDLB.add(buildWord); //add to userDLB
					buildWord = "";
					break;
					
				case "1":
					System.out.println("\nYou have selected option 1. \n"+"WORD COMPLETED: "+suggestWords.get(0));
					userDLB.add(suggestWords.get(0)); //add to userDLB
					buildWord = "";
					break;
					
				case "2":
					System.out.println("\nYou have selected option 2. \n"+"WORD COMPLETED: "+suggestWords.get(1));
					userDLB.add(suggestWords.get(1)); //add to userDLB
					buildWord = "";
					break;
					
				case "3":
					System.out.println("\nYou have selected option 3. \n"+"WORD COMPLETED: "+suggestWords.get(2));
					userDLB.add(suggestWords.get(2)); //add to userDLB
					buildWord = "";
					break;
					
				case "4":
					System.out.println("\nYou have selected option 4. \n"+"WORD COMPLETED: "+suggestWords.get(3));
					userDLB.add(suggestWords.get(3)); //add to userDLB
					buildWord = "";
					break;
					
				case "5":
					System.out.println("\nYou have selected option 5. \n"+"WORD COMPLETED: "+suggestWords.get(4));
					userDLB.add(suggestWords.get(4)); //add to userDLB
					buildWord = "";
					break;				
				}
				
				System.out.print("\n\nEnter the first character to the next word: "); //start over
				
				if(piece.equals("!"))
				{
					flag = false;
					break;
				}
				
				flag = true;;
			}
			
			else
			{	
				suggestWords.clear(); //clear suggestions for next predict
				
				
				
				time = System.nanoTime(); //figure out total time
				if(userFile.exists() || userDLB != null)
				{
					userDLB.searchSug(suggestWords, buildWord, 5, true);
				}
				myDLB.searchSug(suggestWords,buildWord,5,false);
				
				time2 = System.nanoTime();
				totalTime = time2 - time;
				
				timeArr.add(totalTime);
				System.out.printf("\n(%.8f s)\n",totalTime*Math.pow(10, -9));
				if(suggestWords.size() == 0)
					System.out.println("NO PREDICTIONS CAN BE MADE");
				else
				{
				//if(suggestWords.size() != 0) //print predictions out if we have any
					System.out.println("Predicitions: ");
				
					for(int i = 0; i < suggestWords.size(); i++) //print out suggestions we found in dlb
					{
						System.out.print(i+1+") "+suggestWords.get(i)+"\t");
					}
				}
				
				System.out.print("\n\nEnter your next character: ");	
			}
			
		}
		 
		character.close(); //close files and scanners
		
		ArrayList<String> userWords = new ArrayList<>();
		PrintWriter userHist = new PrintWriter("user_history.txt"); 
		userDLB.searchSug(userWords, "", 0, true); //go and find all words we added to the user DLB
		
		for(int i = 0; i < userWords.size(); i++)
		{
			userHist.println(userWords.get(i).replace("$",""));
		}
		userHist.close();
		
		long sum = 0;
		int count = 0;
		
		for(int i = 0; i < timeArr.size(); i++)
		{
			sum += timeArr.get(i); //add every total time we had saved to determine average output of the run
			count++;	
		}
		System.out.printf("\n\n\nAverage time: %.8f",(sum/count)*Math.pow(10, -9));
	}
	
}
