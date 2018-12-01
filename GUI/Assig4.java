import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Assig4 implements VoteInterface, LoginInterface
{

	private JFrame theWindow; 
	private String vFileName;
	private Voter P;
	private LoginPanel logPan;
	private JButton login;
	private String bFileName;
	private JButton startButton;
	private BallotPanel ballots;
	
	public Assig4(String [] args) //basically doing what was done in the test files so everything shows
	{		
		vFileName = args[0];
		bFileName = args[1];
		theWindow = new JFrame("Assignment 4: Voting Program");
		theWindow.setLayout(new GridLayout(1,2));

		ballots = new BallotPanel(bFileName, this); //initialize ballots

		
		login = new JButton("Click to Login");
		login.setFont(new Font("Serif", Font.ITALIC + Font.BOLD, 30));
		login.addActionListener(new LoginListener());
		
		theWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theWindow.add(login);
		theWindow.pack();
		theWindow.setVisible(true);
		
	}

	public void setVoter(Voter newV) 
	{
		P = newV;
		theWindow.remove(logPan); //removes panel
				
		startButton = new JButton("See your ballots and vote");
		startButton.setFont(new Font("Serif", Font.BOLD, 30));
		startButton.setEnabled(true);
		startButton.addActionListener(new StartListenType());
		
		theWindow.add(startButton);
		theWindow.pack();
	}
	
	private class StartListenType implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == startButton)
			{
				theWindow.remove(startButton);
				theWindow.add(ballots);
				ballots.resetBallots(); //reset method
				theWindow.pack();
			}
		}
	}


	public void voted() //save what happens with voter
	{
		P.vote();
		Voter.saveVoter(vFileName, P);
		
		theWindow.remove(ballots);
		JOptionPane.showMessageDialog(null,P.getName()+" Thank you for voting");
		theWindow.add(login);
		theWindow.pack();
	}
	
	private class LoginListener implements ActionListener 
	{
		public void actionPerformed(ActionEvent e)
		{
			logPan = new LoginPanel(vFileName, Assig4.this);
			theWindow.remove(login);
			theWindow.add(logPan);
			theWindow.pack();
		}
	}
	
	public static void main(String [] args)
	{
		new Assig4(args);
	}
}
