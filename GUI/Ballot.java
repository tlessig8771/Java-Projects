import java.util.*;
import java.io.*;


public class Ballot 
{

	private String id; //variables
	private String title;
	private String[] options;
	private int [] votes;

	Ballot(String ID, String title, String[] options, int [] count) //constructor for ballots
	{
		id = ID;
		this.title = title;
		this.options = options;
		
		votes = count;
	}
	
	Ballot(String ID, String title, String[] options)
	{
		votes = new int [options.length]; //setting all ballots to 0 votes 
		for (int i = 0; i < options.length;i++)
		{
			votes[i] = 0;
		}
		
		id = ID;
		this.title = title;
		this.options = options;
		
	}
	
	public String getID() //method to get user id
	{
		return id;
	}
	
	public String getTitle() //method to get ballot title
	{
		return title;
	}
	
	public int getOpSize() //method to get length of how many choices each ballot has
	{
		return options.length;
	}
	
	public String getOption(int index)
	{
		return options[index];
	}
	
	public void incrementVotes(int optionIndex) //increment votes for what selected method
	{
		votes[optionIndex]++;
	}
	
	public String toFileString() //writing this to file
	{
		StringBuffer D = new StringBuffer();
		for (int i = 0; i < options.length;i++)
		{
			D.append(options[i]+":"+votes[i]+"\r\n");
		}
		
		return D.toString();
	}



}
