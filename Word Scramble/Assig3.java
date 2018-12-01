import java.util.*;
import java.io.*;

public class Assig3 {

	public static void main(String[] args) throws IOException
	{

		String name = ""; //creating my variables for the program
		String cont = "";
		String guess = "";
		int count = 3; //amount of tries they have
		String scram = null;
		String word = null;
		//int numWords=0;
		//String Words []= null;
		String answer = "";
		int rounds;
		int wins;
		int losses;

		//ITEMS NEEDED:
			//NEED EXTRA OUTER LOOP SO MULTIPLE PLAYERS CAN PLAY BEFORE QUITTING
			//RETRIEVE PLAYER INFO IF PLAYER EXISTS. IF NOT ASK TO ADD THEM TO THE LIST
			//AT THE END SHOW RESULTS OF THAT GAME ALONG WITH OVERALL RESULTS FOR THAT PLAYER
			//SHOW CUMULATIVE STATS AFTER GAME IS OVER
			//RESET BEFORE NEW PLAYER PLAYS THE GAME. DO NOT USE A NEW SCRAMBLE 

		Scanner file = new Scanner(new File("words.txt"));
		Scanner input = new Scanner(System.in);
		Scanner playerfile =  new Scanner(new File("players.txt"));

		Scramble2 myScramble = new Scramble2("words.txt"); 
		PlayerList allPlayers = new PlayerList("players.txt");


		System.out.println("Welcome to the Scramble Game!");
		
		while("".equals(name)) // if they give a non empty name
		{
			System.out.println("Enter your name to continue (<enter> to quit): ");
			name = input.nextLine();
			if(name.equals(""))
			{
				break;
			}
			
			Player onePlayer = allPlayers.getPlayer(name); // find them
			if(onePlayer != null) // if found
			{
				System.out.println("Welcome back, "+name);
				cont = "y";
			}	
			else // if not found
			{
				
				System.out.println("You are not in our player list.");
				System.out.println("Would you like to be added to the list? (y/n) ");
				answer = input.nextLine();
				answer = answer.toLowerCase();
				if(answer.equals("n")) // if they didn't enter a name
				{
					name="";
					continue;
				}
				
				// if they want to be added, add them
				
				onePlayer = new Player(name);
				allPlayers.addPlayer(onePlayer); 
				System.out.println("Welcome!");
				cont = "y";
				
			}				
			
			rounds = 0; //reset the values for each run
			losses = 0;
			wins = 0;

			while(cont.equals("y")) //continue to do this loop while user selects y
			{

				word = myScramble.getRealWord(); //call for next word
				if(word == null) //if no more words are left in file
				{
					System.out.println("End of game. No more words \n");
					break;
				}

				System.out.println("\n"+name+" you have "+count+" guesses left to get the scramble.\n");
				System.out.println("THE SCRAMBLE: "+ myScramble.getScrambledWord()); //scrambling the real word that was called
				System.out.print("YOUR GUESS: ");
				guess = input.nextLine();
				guess = guess.toLowerCase(); //making it so it doesn't matter HoW THey EnTeR thE WorD so IT StiL WorkS

				scram = myScramble.getScrambledWord(); //making a variable for the scramble

				while(count >= 1)
				{
					if(guess.equals(word)) //checking to see if they match
					{
						System.out.println("Congratulations! You got the scramble.\n");
						allPlayers.getPlayer(name).won();
						wins++;
						break;
					}

					else if(guess != word) //if they don't do this
					{
						System.out.println("Sorry that is not correct. Here are the letters you got right: ");
						int i = 0;

						while(i < word.length() && i < guess.length()) //both need to be true for this section to run
						{	
							if(guess.charAt(i) == word.charAt(i)) //showing the letters user got correct

							{
								System.out.print(guess.charAt(i));
							}
							else
							{
								System.out.print("_"); //blank spaces for letters they did not get correct
							}

							i++;
						}

						while(i < word.length()) //goes to the length of the real word because i may not be the correct length
						{
							System.out.print("_");
							i++;
						}					
					}

					count = count - 1;
					if(count == 0) //showing the user the correct answer if they run out of guesses
					{
						System.out.println("\n\nYou have "+count+" guesses left\nBetter luck next time. Round Over!");
						System.out.println("\nThe correct word is: "+word);
						
						allPlayers.getPlayer(name).lost();
						losses++;
						break; //exits
					}

					System.out.println("\n\nYou have "+count+" guesses left to get the scramble.\n");

					System.out.println("THE SCRAMBLE: "+ scram);
					System.out.print("YOUR GUESS: ");
					guess = input.nextLine();
					guess = guess.toLowerCase(); //making it so it doesn't matter HoW THey EnTeR thE WorD so IT StiL WorkS		
				}

				count = 3; //re-initializing
				System.out.println("\nWould you like to play another round? (y/n)");
				cont = input.nextLine();
				cont = cont.toLowerCase();				
			}
			
			allPlayers.getPlayer(name).rounds(); //printing out info and updating classes
			rounds = wins + losses;
			System.out.println("Here are today's stats: ");
			System.out.println("\n\tRounds Played: "+rounds+"\n\tRounds Won: "+wins+"\n\tRounds Lost: "+losses);
			System.out.println("\n\nHere are your cumulative stats: ");
			System.out.println("\n"+allPlayers.getPlayer(name).toString());
			allPlayers.saveList(); //saving to classes
			myScramble.reset();
			
			name = ""; //repeat

		}

		System.out.println("Thank you for playing!\n"); //outside yes no while loop
		
		System.out.println("Overall Stats:\n\n"+allPlayers.toString()); //showing combined stats of all players

	}

}