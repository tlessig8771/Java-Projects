import java.util.*;
import java.io.*;

public class PlayerList 
{
	private Scanner filein; //creating variables
	//private String Name;
	private ArrayList <Player> allPlayers;
	private String [] tempS;
	private String Playerfile;
	private String name = "";
	private String rounds = "";
	private String wins = "";
	private String lost = "";
	private int sumRounds = 0;
	private int sumWins = 0;
	private int sumLosses = 0;
	private String winPct;


	PlayerList(String Playerfile) throws IOException //constructor that takes file to create array list
	{
		File file = new File(Playerfile);
		filein = new Scanner(file);
		this.Playerfile = Playerfile;
		allPlayers = new ArrayList <Player>();

		while(filein.hasNextLine())
		{
			tempS = filein.nextLine().split(" "); //splitting items and assigning to variables
			name = tempS[0];
			rounds = tempS[1];
			wins = tempS[2];
			lost = tempS[3];

			allPlayers.add(new Player(name, rounds, wins, lost)); //new player object
		}	

		filein.close();
	}

	public Player getPlayer(String S)
	{
		for (int i = 0; i < allPlayers.size(); i++)
		{
			if (allPlayers.get(i).getPlayerName().equals(S))
				return allPlayers.get(i);
		}
		return null;
	}

	public void addPlayer(Player add)
	{
		allPlayers.add(add); //adds player to list
	}

	public String toString()
	{
		StringBuffer D = new StringBuffer();

		sumRounds = 0;
		sumWins = 0;
		sumLosses = 0;

		D.append("Number of Players: "+Integer.toString(allPlayers.size())); //reads how many players there are

		for(int i=0; i<allPlayers.size();i++) //adding up the rounds played
		{
			sumRounds = sumRounds + (allPlayers.get(i).getR());
		}

		D.append("\n\tTotal Rounds Played: "+sumRounds);

		for(int i = 0; i<allPlayers.size();i++) //adding up the wins
		{
			sumWins = sumWins + (allPlayers.get(i).getW());
		}

		D.append("\n\tTotal Wins: "+sumWins);

		for(int i = 0; i<allPlayers.size();i++) //adding up the losses
		{
			sumLosses = sumLosses + (allPlayers.get(i).getL());
		}

		D.append("\n\tTotal Losses: "+sumLosses);

		winPct = String.format("%.2f", ((double)sumWins/(double)sumRounds)*100); //finding the win percentage

		D.append("\n\tPercent of Rounds won: "+winPct);

		return D.toString();	
	}

	public String toStringAdmin() //to String method
	{
		StringBuffer P = new StringBuffer();
		P.append("Individual Player Stats: \n\n");

		for (int i = 0; i < allPlayers.size(); i++) //returns the individual player stats
		{
			P.append(allPlayers.get(i).toString() + "\n");
		}
		return P.toString();
	}

	public Player removePlayer(String rName)
	{
		for (int i = 0; i < allPlayers.size(); i++)
		{
			if (allPlayers.get(i).getPlayerName().equals(rName)) //looks for player if found removes that player
			{
				Player returnPlayer = allPlayers.get(i); 
				allPlayers.remove(i); 
				return returnPlayer;
			}

		}
		return null;
	}

	public void saveList() throws IOException //throws exception because of writing to file
	{
		PrintWriter printTofile = new PrintWriter(Playerfile); //writing to file
		for(int i = 0; i < allPlayers.size();i++)
		{
			printTofile.println(allPlayers.get(i).toFileString()); //prints the info of the player in the format that was given for the file
		}

		printTofile.close(); //closing the file

	}

	//WHATS NEEDED FOR THIS CLASS:
	//player objects read in from file
	//name, rounds, won, lost (blank space between items. Infinitely many)
	//all player objects should be saved back to file
	//should run with the PlayerListTest class (given)	

}
