import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class BallotPanel extends JPanel
{
	//creating variables
	private String bfilename;
	private VoteInterface votes;
	private JPanel bpanel;
	private Ballot Ballots;
	private ArrayList <Ballot> ballotHolder;
	private JPanel [] nests;
	private ArrayList <JButton[]> buttons;
	
	
	BallotPanel(String bFile, VoteInterface v) //reading in file and voterInterface
	{
		bfilename = bFile;
		votes = v;
		String tempS[];
		ballotHolder = new ArrayList<>(); //holding ballots
		
		File file = new File(bFile);
		Scanner filein; //try catch to prevent exceptions
		try { 
			filein = new Scanner(file);
			PrintWriter printToFile;

			if(filein.hasNextLine())
			{
				Integer.parseInt(filein.nextLine());


				while(filein.hasNextLine()) //splitting items into parts to add to arraylist
				{
					tempS = filein.nextLine().split(":"); //splitting items and assigning to variables
					String idB = tempS[0];
					String cat = tempS[1];
					String [] ops = tempS[2].split(","); //array list will contain
					
					ballotHolder.add(new Ballot(idB,cat,ops)); //new ballot
					File ballotFile = new File(idB+".txt"); //ballot file name
					printToFile = new PrintWriter(ballotFile);
					printToFile.print(ballotHolder.get(ballotHolder.size()-1).toFileString()); //printing info to file
					printToFile.close();
				}	
			}

			filein.close();
		} catch (FileNotFoundException e) {

		}
		
		Ballot myBallot; //bunch of nested ifs, for loops, Jpanels, JButtons
		nests = new JPanel[ballotHolder.size()];
		bpanel = new JPanel(new GridLayout(1,nests.length+1,15,0));
		JButton [] tempButton;
		buttons = new ArrayList <>();
		
		for(int i = 0; i < ballotHolder.size(); i++) //iterating through
		{
			myBallot = ballotHolder.get(i);
			
			nests[i] = new JPanel(new GridLayout(myBallot.getOpSize()+1,1,0,15)); //nested JPanels 
			
			JLabel label = new JLabel(myBallot.getTitle()); //gets title of office/category
			label.setFont(new Font("Serif",Font.BOLD,30));
			label.setForeground(Color.BLUE);
			nests[i].add(label); //adding to jlabel
			
			tempButton = new JButton[myBallot.getOpSize()];
			
			for(int j = 0; j < myBallot.getOpSize() ; j++) //getting the info for each button that will  be created
			{
				tempButton[j] = new JButton(myBallot.getOption(j));
				tempButton[j].setFont(new Font("Serif",Font.BOLD,20));
				nests[i].add(tempButton[j]);
				tempButton[j].addActionListener(new ButtonListener());
			}
			buttons.add(tempButton);
			bpanel.add(nests[i]); //adds to label and then Jpanel
			
		}
		
		JButton submit = new JButton("Submit"); //submit button
		submit.setFont(new Font("Serif",Font.BOLD,20));
		bpanel.add(submit);
		
		
		add(bpanel); //adds everything so user can see
		
		
		submit.addActionListener(new VoteListener());
		
		
	}
	
	
	public void resetBallots()
	{
		for(int i = 0; i < buttons.size(); i++) //when called each button will be placed back to orginal black color
		{
			for(int j = 0; j < buttons.get(i).length ; j++) 
			{
				buttons.get(i)[j].setForeground(Color.BLACK); 
			}				
		}
	}
	
	
	private class VoteListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			int input = JOptionPane.showConfirmDialog(null, "Confirm Vote?");
			if(input == 0) //user clicks submit
			{
				//save votes
				for(int i = 0; i < ballotHolder.size();i++)
				{
					File infile = new File(ballotHolder.get(i).getID()+".txt"); //making file and temp file to rename later
					File outfile = new File("_"+ballotHolder.get(i).getID()+".txt");
					
					try {
						PrintWriter P = new PrintWriter(outfile);
						
						for(int j = 0; j < buttons.get(i).length;j++) // if button is red increment count if not ignore
						{
							if(buttons.get(i)[j].getForeground() == Color.RED)
							{
								ballotHolder.get(i).incrementVotes(j);	
							}
						}
						
						P.print(ballotHolder.get(i).toFileString()); //prints info to file
						P.close(); //closing printwriter
						infile.delete(); // deleting file
						outfile.renameTo(new File(ballotHolder.get(i).getID()+".txt")); //renaming file
						
						
					} catch (FileNotFoundException e1) {
					
					}
					
				}
				
				votes.voted(); //updating vote status
			}
			
		}
		
	}
	
	private class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			JButton buttonCaller = (JButton)e.getSource(); //Jbuttons
			int col = getCol(buttonCaller); //columns
			
			for(int i = 0; i < buttons.get(col).length;i++) //iterate through, if button clicked other buttons are set to black
			{
				if(buttons.get(col)[i] != buttonCaller)
				{
					buttons.get(col)[i].setForeground(Color.BLACK); 
				}
			}
			
			if(buttonCaller.getForeground() != Color.RED) // if one button clicked and not red, set red
			{
				buttonCaller.setForeground(Color.RED);
			}
			else
				buttonCaller.setForeground(Color.BLACK); // else put back to black
			
		}
		
		private int getCol(JButton b) //iterates through to see what button in which column was clicked to determine what is selected
		{
			for(int i = 0; i < buttons.size(); i++)
			{
				
				for(int j = 0; j < buttons.get(i).length ; j++) 
				{
						if(b == buttons.get(i)[j])
						{
							return i;
						}
				}				
			}
			return -1; //should never reach here unless something goes wrong
		}
		
	}
	
}
