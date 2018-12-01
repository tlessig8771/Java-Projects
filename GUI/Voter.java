import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Voter 
{
	
	private String vid;
	private String name;
	private boolean voted;
	//private Scanner filein;
	//private String [] tempS;
	//private static ArrayList <Voter> allVoters;
	//private String f; 
	
	public Voter(String id, String n, boolean v)
	{
		vid = new String(id);
		name = new String(n);
		voted = v;
	}
	
	public Voter(String S) //third constructor - initialize new voter values
	{
		this(S,S,false);
	
	}
	
	public static Voter getVoter(String f, String n) 
	{
		
		File file = new File(f);
		Scanner filein;
		try {
			filein = new Scanner(file);
		
		boolean voted;
		//this.Voterfile = Voterfile;
		//ArrayList <Voter> allVoters = new ArrayList <Voter>();

		while(filein.hasNextLine())
		{
			String[] tempS = filein.nextLine().split(":"); //splitting items and assigning to variables
			String vid = tempS[0];
			String name = tempS[1];
			String votedd = tempS[2];
			voted = Boolean.parseBoolean(votedd);
			
			if(vid.equals(n))
			{
				filein.close();
				return new Voter(vid,name,voted);
			}

		}	

		
		filein.close();
		} catch (FileNotFoundException e) {
	
		}

		return null;	
	}
	
	public String getId() //method for voter id
	{
		return vid;
	}
	
	public String getName() //method for name
	{
		return name;
	}
	
	public boolean hasVoted() //method for voted
	{
		return voted;
	}
	
	public boolean vote() //mutator for voted
	{
		voted = true;
		return voted;
	}
	
	public static void saveVoter(String g, Voter U)
	{
		File newfile = new File("_"+g);
		PrintWriter printTofile;
		try {
			printTofile = new PrintWriter(newfile);
		 //writing to file
		boolean voted;
		
		File file = new File(g);
		Scanner filein = new Scanner(file);
		
		while(filein.hasNextLine())
		{
			String line = filein.nextLine();
			String[] tempS = line.split(":"); //splitting items and assigning to variables
			String vid = tempS[0];
			String name = tempS[1];
			String votedd = tempS[2];
			if(votedd == "false")
				voted = Boolean.parseBoolean(votedd);
			if(votedd == "true")
				voted = Boolean.parseBoolean(votedd);
			
			if(vid.equals(U.getId()))
			{
				printTofile.println(U.toFileString()); //prints updated info of specified voter
			}
			else
			{
				printTofile.println(line); //prints origial non-updated info
			}	
		}
		
		
		filein.close();
		
		printTofile.close(); //closing the file
		
		file.delete(); //delete old file
		newfile.renameTo(new File(g)); //renaming file	
		
		} catch (FileNotFoundException e) {
		}
		
	}
	
	public String toFileString() //format to print to file when saving
	{
		StringBuffer C = new StringBuffer();
		C.append(vid+":"+name+":"+voted);
		
		return C.toString();
	}
	
	public String toString() //format for output to user
	{
		StringBuffer A = new StringBuffer();
		A.append("\tVoter ID: "+vid);
		A.append("\n\tVoter Name: "+name);
		A.append("\n\tVoted: "+voted);
		
		return A.toString();
	}
	

}
