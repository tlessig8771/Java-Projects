
public class Player 
{
	
	private String Name;
	private int Rounds;
	private int Wins;
	private int Losses;
	
	public Player(String n, int r, int w, int l) //constructor that takes 4 inputs
	{
		Name = new String(n);
		Rounds = r;
		Wins = w;
		Losses = l;
	}
	
	public Player(String n, String r, String w, String l) //second constructor to parse values
	{
		this(n, Integer.parseInt(r),Integer.parseInt(w),Integer.parseInt(l));
	}
	
	public Player(String S) //third constructor - initialize new player values
	{
		this(S,0,0,0);
	}
	
	public String getPlayerName() //method for name
	{
		return Name;
	}
	
	public int getR() //method for rounds
	{
		return Rounds;
	}
	
	public int getW() //method for wins
	{
		return Wins;
	}
	
	public int getL() //method for losses
	{
		return Losses;
	}
	
	public void won() //method for wins. increments the amount of rounds won
	{
		Wins = Wins + 1;
	}
	
	public void lost() // method for losses. increments the amount of rounds lost
	{
		Losses = Losses + 1;
	}
	
	public void rounds() //method for how many rounds player played
	{
		Rounds = Losses + Wins;
	}
	
	
	public String toFileString() //format to print to file when saving
	{
		StringBuffer C = new StringBuffer();
		C.append(Name+" "+Rounds+" "+Wins+" "+Losses);
		
		return C.toString();
	}
	
	public String toString() //format for output to user
	{
		StringBuffer A = new StringBuffer();
		A.append("\tName: "+Name);
		A.append("\n\tRounds Played: "+Rounds);
		A.append("\n\tWins: "+Wins);
		A.append("\n\tLosses: "+Losses+"\n");
		
		return A.toString();
	}
	
	//WHATS NEEDED FOR THIS CLASS:
		//encapsulation of players name, win/loss info and methods/mutators for accessing the players info
		//player object updated after each round for the specified player that is playing the game
		//must work with the PlayerListTest (given)
		//output should be consistent with that shown in the PlayersTest.txt file
	
}
