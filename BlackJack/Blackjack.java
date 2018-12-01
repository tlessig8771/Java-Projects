import java.util.*;
import java.io.*;

//Trent Lessig - Assignment 1
//DUE February 2nd 2018

public class Blackjack
{
	public static void main(String[] args)
	{		
		int rounds = Integer.parseInt(args[0]); //get number of rounds
		int decks = Integer.parseInt(args[1]); //get number of decks
		int userVal = 0;
		int dealerVal = 0;
		int dealerWins = 0;
		int userWins = 0;
		int push = 0;
		Card cards;

		RandIndexQueue<Card> userCards = new RandIndexQueue<Card>(5); //initialize RandIndexQueue<Card> Objects
		RandIndexQueue<Card> shoe = new RandIndexQueue<Card>(decks*52);
		RandIndexQueue<Card> dealerCards = new RandIndexQueue<Card>(5);
		RandIndexQueue<Card> discard = new RandIndexQueue<Card>(5);

		for(int x = 0; x < decks; x++) //go through a nested for loop to get the suit and value of each card in x decks
		{
			for(Card.Suits cardSuite: Card.Suits.values())
			{
				for(Card.Ranks cardRank: Card.Ranks.values())
				{
					cards = new Card(cardSuite,cardRank); //create card and add to shoe
					shoe.offer(cards);
				}
			}
		}
		
		int origSize = shoe.size();
		shoe.shuffle(); //shuffle the cards

		System.out.println("Playing "+rounds+" rounds with "+ decks+" decks");

		int count = 1;

		for(int i = 1; i <= rounds; i++)
		{
			userVal = 0; //reset variables
			dealerVal = 0;
			userCards.clear();
			dealerCards.clear();

			if(rounds <= 10)
			{
				System.out.println();System.out.println();System.out.println("Starting Round "+count);

				userCards.offer(shoe.poll()); //alternating cards dealt to player and dealer
				dealerCards.offer(shoe.poll());
				userCards.offer(shoe.poll());
				dealerCards.offer(shoe.poll());
				userVal = returnVal(userCards); //get associated values with those cards
				dealerVal = returnVal(dealerCards);

				System.out.println("Player : "+userCards+" : "+userVal); //print out cards and associated value
				System.out.println("Dealer : "+dealerCards+" : "+dealerVal);

				if(userVal == 21 && dealerVal == 21) //testing for blackjack cases
				{
					System.out.println("Result: PUSH! - Two BlackJacks");
					push++;
				}
				else if(userVal == 21 && dealerVal != 21)
				{
					System.out.println("Result: Player Wins - BlackJack");
					userWins++;
				}
				else if(dealerVal == 21 && userVal != 21)
				{
					System.out.println("Result: Dealer WINS - BlackJack");
					dealerWins++;
				}
				else
				{
					while(userVal != 21 && dealerVal != 21) //PLAYER loop
					{
						if(userVal < 17)//condition for if hand is less than a STAND value
						{
							Card givenCard = shoe.poll(); //simplify process for poll and offer
							userCards.offer(givenCard); //hit player
														
							userVal = returnVal(userCards); //get card(s)

							System.out.println("Player HITS: "+userCards.get(userCards.size()-1));

							if(userVal == 21) //break if true STAND special case
							{
								System.out.println("Player STANDS: "+userCards+" : "+userVal);
								break;
							}
						}
						else if(userVal >= 17 && userVal <= 21) //condition for STANDS
						{
							System.out.println("Player STANDS: "+userCards+" : "+userVal);
							break;
						}
						else if(userVal > 21) //condition for BUSTS
						{
							System.out.println("Player BUSTS: "+userCards+" : "+userVal+"\nResult: Dealer WINS");
							dealerWins++;
							break;
						}
					}

					while(dealerVal != 21) //DEALER loop
					{
						if(userVal > 21)
						{
							break;
						}
						else if(dealerVal < 17) //condition for if hand is less than a STAND value
						{
							Card givenCard = shoe.poll(); //simplify poll and offer process
							dealerCards.offer(givenCard); //hit dealer
							
							dealerVal = returnVal(dealerCards);

							System.out.println("Dealer HITS: "+dealerCards.get(dealerCards.size()-1)); //print out what they got

							if(dealerVal == 21) //break if true
							{
								System.out.println("Dealer STANDS: "+dealerCards+" : "+dealerVal);
								break;
							}
						}
						else if(dealerVal >= 17 && dealerVal <= 21) //condition for STAND values
						{
							System.out.println("Dealer STANDS: "+dealerCards+" : "+dealerVal);
							break;
						}
						else if(dealerVal > 21) //condition for BUST values
						{
							System.out.println("Dealer BUSTS : "+dealerCards+" : "+dealerVal);
							System.out.println("Result: Player WINS");
							userWins++;
							break;
						}
					}

					//Find the winner/result
					if(dealerVal >= 17 && dealerVal <= 21 && userVal >=17 && userVal <= 21)
					{
						if(userVal > dealerVal) //compare hands, show who won
						{
							System.out.println("Result: Player WINS");
							userWins++;
						}
						else if(dealerVal > userVal)
						{
							System.out.println("Result: Dealer WINS");
							dealerWins++;
						}
						else
						{
							System.out.println("Result: PUSH!");
							push++;
						}
					}
				}
				count++;	

				for(int z = 0; z < userCards.size(); z++) //add user and dealer cards to the discarded pile
				{
					Card discardedCard = userCards.poll();
					discard.offer(discardedCard);
				}
				for(int k = 0; k < dealerCards.size(); k++)
				{
					Card discardedCard = dealerCards.poll();
					discard.offer(discardedCard);
				}
			} //out rounds <= 10 if
			

			else //show reshuffle and results; not cards or values
			{				
				userCards.offer(shoe.poll()); //alternating cards dealt to player and dealer
				dealerCards.offer(shoe.poll());
				userCards.offer(shoe.poll());
				dealerCards.offer(shoe.poll());

				for(int j = 0; j < userCards.size();j++) //going through to add up value of the hand(s)
				{
					userVal = returnVal(userCards); //call method/function
				}
				for (int k = 0; k < dealerCards.size(); k++)
				{
					dealerVal = returnVal(dealerCards);
				}
				if(userVal == 21 && dealerVal == 21) //test for blackjack cases
				{
					push++;
				}
				else if(userVal == 21 && dealerVal != 21)
				{
					userWins++;
				}
				else if(dealerVal == 21 && userVal != 21)
				{
					dealerWins++;
				}
				else 
				{		//play blackjack like above but don't print anything just final results
					while(userVal != 21 && dealerVal != 21) //PLAYER loop
					{
						if(userVal < 17)
						{
							userCards.offer(shoe.poll()); //hit player
							userVal = returnVal(userCards);

							if(userVal == 21)
							{
								break;
							}
						}
						else if(userVal >= 17 && userVal <= 21)
						{
							break;
						}
						else if(userVal > 21)
						{
							dealerWins++;
							break;
						}
					}

					while(dealerVal != 21) //DEALER loop
					{
						if(userVal > 21)
						{
							break;
						}
						else if(dealerVal < 17)
						{
							dealerCards.offer(shoe.poll()); //hit dealer
							dealerVal = returnVal(dealerCards);

							if(dealerVal == 21)
							{
								break;
							}
						}
						else if(dealerVal >= 17 && dealerVal <= 21)
						{
							break;
						}
						else if(dealerVal > 21)
						{
							userWins++;
							break;
						}
					}

					//Find the winner/result
					if(dealerVal >= 17 && dealerVal <= 21 && userVal >=17 && userVal <= 21)
					{
						if(userVal > dealerVal)
							userWins++;
						else if(dealerVal > userVal)
							dealerWins++;
						else
							push++;
					}
				}
				count++;	

				int uCard = userCards.size();
				int dCard = dealerCards.size();

				for(int z = 0; z < uCard; z++) //add user and dealer cards to discard pile
				{
					Card discardedCard = userCards.poll();
					discard.offer(discardedCard);
				}
				for(int k = 0; k < dCard; k++)
				{
					Card discardedCard = dealerCards.poll();
					discard.offer(discardedCard);
				}
				
			} //outside else associated with if round <= 10 statement
			
			int pile = discard.size();			
			
			if(shoe.size() <= .25*origSize)
			{
				for(int b = 0; b < pile; b++)
				{
					shoe.offer(discard.poll());
				}
				
				shoe.shuffle();
				System.out.println();
				System.out.println("Adding back to shoe and reshuffling for round "+count);
			}

		} // out for loop

		System.out.println("\nAfter "+rounds+" rounds, the results are: \n\tPlayer Wins: "+userWins+"\n\tDealer Wins: "+dealerWins+"\n\tPushes: "+push);
	}
	
	//method for adding the cards together
	private static int returnVal(RandIndexQueue<Card> Cards)
	{
		int numAces = 0;
		int sum = 0;

		for(int i = 0; i < Cards.size();i++)
		{
			int value = Cards.get(i).value(); //if card is an ace keep track of it
			if(value == 11)
				numAces++;
			else
				sum = sum + value; //else add values of cards
		}

		for (int i = 0; i < numAces;i++) //loop to determine what value of the ace should be added
		{
			if(sum + 11 > 21)
				sum = sum + 1;
			else
				sum = sum + 11;
		}

		return sum;
	}
}

