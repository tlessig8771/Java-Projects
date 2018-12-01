// CS 0445 Spring 2018
// Some hints / help for Assignment 1.
// Note:  Parts of this program (identified below) will not work until you
// have completed the RandIndexQueue<T> implementation.  To see the rest of it execute
// simply comment out that section.

import java.util.*;
public class A1Help
{
	public static void main(String [] args)
	{
		// Demo of Card class
		Card c1 = new Card(Card.Suits.Diamonds, Card.Ranks.Seven);
		Card c2 = new Card(Card.Suits.Hearts, Card.Ranks.Ace);
		Card c3 = new Card(Card.Suits.Spades, Card.Ranks.Ace);
		Card c4 = new Card(Card.Suits.Spades, Card.Ranks.Queen);
		
		System.out.println("Card 1 is " + c1.toString());
		System.out.println("Card 2 is " + c2.toString());
		System.out.println("Card 3 is " + c3.toString());
		System.out.println("Card 4 is " + c4.toString());
		
		showValues(c1);
		showValues(c2);
		showValues(c3);
		showValues(c4);
		
		System.out.println("Here are all of the suits:");
		for (Card.Suits s: Card.Suits.values())
			System.out.print(s + " ");
		System.out.println("\n");
		
		System.out.println("Here are all of the ranks:");
		for (Card.Ranks r: Card.Ranks.values())
			System.out.print(r + " ");
		System.out.println("\n");
		
		// Below this line requires the RandIndexQueue<T> class.  Comment this out if you
		// have not yet finished your RandIndexQueue<T> class
		
		// First let's make two RandIndexQueue objects of Cards
		RandIndexQueue<Card> myCards = new RandIndexQueue<Card>(10);
		RandIndexQueue<Card> otherCards = new RandIndexQueue<Card>(10);
		
		// Now put some cards into one
		myCards.offer(c1);
		myCards.offer(c2);
		myCards.offer(c3);
		
		// Print it out
		System.out.println("myCards " + myCards.toString());
		
		// Move two into the other one
		otherCards.offer(myCards.poll());
		otherCards.offer(myCards.poll());
		
		System.out.println("myCards " + myCards.toString());
		System.out.println("otherCards " + otherCards.toString());
		
	}
	
	public static void showValues(Card c)
	{
		System.out.print(c.toString() + " has Value1: " + c.value());
		System.out.println(" and Value2: " + c.value2());
	}
}
