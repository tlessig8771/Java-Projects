import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class LoginPanel extends JPanel
{
	private String filename;
	private JPanel panel;
	private JTextField field1;
	private LoginInterface voter;
	
	LoginPanel(String voterFile, LoginInterface b) 
	{
		//Jpanels, textfields, buttons
		
		filename = voterFile;
		voter = b;
		
		panel = new JPanel(new BorderLayout()); //adding items to panel
		JLabel label = new JLabel("Please log into the site");
		label.setFont(new Font("Serif",Font.BOLD,20));
		
		JPanel nestedPanel = new JPanel(new GridLayout(1,2));

		JLabel label2 = new JLabel("Voter ID: ");
		label2.setFont(new Font("Serif",Font.BOLD,18));
		field1 = new JTextField();
		field1.setFont(new Font("Serif",Font.BOLD,18));
		JButton submitButton = new JButton("Submit");
		submitButton.setFont(new Font("Serif",Font.BOLD,20));
		
		panel.add(label, BorderLayout.PAGE_START);
		nestedPanel.add(label2);
		nestedPanel.add(field1);
		panel.add(nestedPanel, BorderLayout.CENTER);
		panel.add(submitButton, BorderLayout.PAGE_END);
		add(panel);
		
		submitButton.addActionListener(new LoginListener());
	}
	
	private class LoginListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) //determine if someone voted or not registered
		{
			
			String text = field1.getText();
			
			Voter newVoter = Voter.getVoter(filename, text);
			
			if(newVoter == null)
				JOptionPane.showMessageDialog(null, "You are not registered, sorry!");
			else if(newVoter.hasVoted() == true)
				JOptionPane.showMessageDialog(null,newVoter.getName()+", you have already voted!");
			else if(newVoter.hasVoted() == false)
				voter.setVoter(newVoter);
			
		}
		
	}


}
