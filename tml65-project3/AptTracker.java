import java.io.*;
import java.util.*;

public class AptTracker 
{
	public static void main(String [] args) throws IOException
	{
		Scanner readSelection = new Scanner(System.in);
		Scanner details = new Scanner(System.in);
		Scanner wordScan;
		String address;
		String city;				//initialze variables
		String number; 
		int zip;
		double rent;
		double footage;
		int option;
		char answer;
		boolean run = true;
		String [] apartments;
		String line = "";
		String uniqueCity;
		int cityCode;
		
		AptPQ rentPQ = new AptPQ(false);	//pq for rent meaning we do min
		AptPQ footagePQ = new AptPQ(true);	//pq for square footage meaning we do max
		AptPQ cityRent = new AptPQ(false);
		AptPQ cityFootage = new AptPQ(true);
		HashMap<Integer, AptPQ> checkCityRent = new HashMap<>();
		HashMap<Integer, AptPQ> checkCityFootage = new HashMap<>();		//create hashmaps
		
		System.out.println("****DISCLAIMER****"
				+ "\n**Please be sure to enter cities starting with upper case to ensure this program runs as intended**"
				+ "\n**This program is a work in progress. Please follow specifications for all other inputs when prompted**"
				+ "\n**Please do not intentionally disregard input parameters, as out may not be what is intended**"
				+ "\n****THANK YOU*****\n"); 
		
		File wordFile = new File("apartments.txt"); //read in the dictionary file and make a scanner ready to add words
		wordScan = new Scanner(wordFile);
		if(wordScan.hasNextLine())
			line = wordScan.nextLine(); //skip first line
		
		while(wordScan.hasNextLine())				//while there is another apartment in the file we will insert into pq
		{
			line = wordScan.nextLine();
			apartments = line.split(":");
			address = apartments[0];
			number = apartments[1];
			city = apartments[2];
			zip = Integer.parseInt(apartments[3]);
			rent = Double.parseDouble(apartments[4]);		//have rent and footage as doubles in case user enters 450.50 for rent or 1125.33 for sq ft
			footage = Double.parseDouble(apartments[5]);
			
			Apartment complex = new Apartment(address, number, city, zip, rent, footage); //add apartment
			rentPQ.insert(complex);	//insert into PQs
			footagePQ.insert(complex);
			
			uniqueCity = complex.getCity();
			cityCode = uniqueCity.hashCode();
			//System.out.println("citycode = "+cityCode);
			
			if(!checkCityRent.containsKey(cityCode) && !checkCityFootage.containsKey(cityCode)) //doesnt have the city so add it
			{
				cityRent = new AptPQ(false);
				cityFootage = new AptPQ(true);
				cityRent.insert(complex);
				cityFootage.insert(complex);	//insert apartment
				
				checkCityRent.put(cityCode, cityRent);		//put in hashmap
				checkCityFootage.put(cityCode, cityFootage);				
			}
			
			else
			{
				checkCityRent.get(cityCode).insert(complex); //insert into pq matching city
				checkCityFootage.get(cityCode).insert(complex);
			}
			//System.out.println(complex.toString());
		}	
		
		//System.out.println("Size of city thing: "+checkCityRent.size());
		wordScan.close(); //close the scanner which is reading the file
		//System.out.println("Did it work?");
		//rentPQ.print();
		//System.out.println("\n\n\nnow we go for footage");
		//footagePQ.print();
		
		while(run)
		{
			System.out.println("\nMAIN MENU\n\n\t1) Add an Apartment \n\t2) Update an Apartment \n\t3) Remove Specific Apartment \n\t4) Retrieve Lowest Price Apartment"
					+ "\n\t5) Retrieve Highest Square Footage Apartment \n\t6) Retrieve Lowest Priced Apartment by City \n\t"
					+ "7) Retrieve Highest Square Footage Apartment By City \n\t8) Quit Program ");
			
			System.out.print("\nWhat would you like to do? (Enter the number to the option you would like): ");
			option = readSelection.nextInt();
			
			while(option < 1 || option > 8)		//MENU CREATION above
			{
				System.out.println("Invalid input. Please try again.\n"
						+ "What would you like to do? (Enter the number of the option you would like): ");
				option = readSelection.nextInt();
			}
			
			details.reset(); 	//reset scanner in event something unexpected happens
			
			switch(option)
			{
				case 1:				//add an apartment
					System.out.print("\nEnter Street Address: "); //get inputs
					address = details.nextLine();
					
					System.out.print("\nEnter Apartment Number: ");
					number = details.nextLine();
					
					System.out.print("\nEnter City: ");
					city = details.nextLine();
					
					System.out.print("\nEnter Zip Code (Integers only): ");
					zip = Integer.parseInt(details.nextLine());
					
					System.out.print("\nEnter Price to Rent (numbers only): ");
					rent = Double.parseDouble(details.nextLine());
					
					System.out.print("\nEnter Square Footage (numbers only): ");
					footage = Double.parseDouble(details.nextLine());
					
					//System.out.println(address+" "+number+" "+city+" "+zip+" "+rent+" "+footage);
					
					Apartment complex = new Apartment(address, number, city, zip, rent, footage); //add the apartment
					
					rentPQ.insert(complex);
					footagePQ.insert(complex); //insert in pqs
										
					uniqueCity = complex.getCity();	//get the hashcode
					cityCode = uniqueCity.hashCode();
					//System.out.println("cityCode = "+cityCode);
										
					if(!checkCityRent.containsKey(cityCode) && !checkCityFootage.containsKey(cityCode)) //doesnt have the city so add it
					{
						cityRent = new AptPQ(false);		//add to hashmap for city and city pqs
						cityFootage = new AptPQ(true);
						cityRent.insert(complex);
						cityFootage.insert(complex);
						
						checkCityRent.put(cityCode, cityRent);
						checkCityFootage.put(cityCode, cityFootage);
					}
					
					else
					{
						checkCityRent.get(cityCode).insert(complex); //addd to city pq for the city we previously added
						checkCityFootage.get(cityCode).insert(complex);
					}
					
					//details.reset(); //clear the scanner in the event something unexpected happens
					
					break;
					
				case 2:				//update apartment
					System.out.print("\nEnter Street Address: "); //get inputs
					address = details.nextLine();
					
					System.out.print("\nEnter Apartment Number: ");
					number = details.nextLine();
					
					System.out.print("\nPlease Enter the Zip Code for this Apartment (Integers only): ");
					zip = Integer.parseInt(details.nextLine());
					
					System.out.print("\nWould you like to update the price of this apartment? (yes or no) ");
					answer = readSelection.next().charAt(0);
					
					while(answer != 'y' && answer != 'Y' && answer != 'n' && answer != 'N') //error checking
					{
						System.out.print("Invalid input. Please only use yes or no. \nTry again: ");
						answer = readSelection.nextLine().charAt(0);
					}
					
					if(answer == 'y' || answer == 'Y') //take both lower and upper case yes
					{
						System.out.print("Please Enter a New Rent Price (numbers only): ");
						rent = Double.parseDouble(details.nextLine());
						
						//make sure we could update both
						boolean wasUpdated = rentPQ.update(address, number, zip, rent) && footagePQ.update(address, number, zip, rent);
						
						Apartment rem = rentPQ.get(address, number, zip);
						if(rem == null) //if we get nothing
						{
							System.out.println("Could not find in system. Try again.");
							break;
						}
						
						cityCode = rem.getCity().hashCode(); //otherwise get associated apartment values from hashmap
						
						checkCityRent.get(cityCode).update(address, number, zip, rent);
						checkCityFootage.get(cityCode).update(address, number, zip, rent);
						
						if(wasUpdated)
						{
							System.out.println("The Apartment has been updated");
							//rentPQ.print();
							details.reset(); 
							break;
						}
						
						else
						{
							System.out.println("Sorry, the Apartment you are trying to update is not in our database.");
							details.reset();
							break;
						}	
					}
					
					else
					{
						System.out.println("Update cancelled. Returning to Main Menu\n");
						break;
					}
					
//					break;
					
				case 3:				//remove
					
					//rentPQ.print();
					System.out.print("Please Enter the Street Address: "); //get inputs to remove
					address = details.nextLine();
					
					System.out.print("\nEnter the Apartment Number: ");
					number = details.nextLine();
					
					System.out.print("\nPlease Enter the Zip Code for this Apartment (Integers only): ");
					zip = Integer.parseInt(details.nextLine());		
					
					Apartment rem = rentPQ.get(address, number, zip); //check to see if we get a null value and if so we dont have anything
					
					if(rem == null) 
					{
						System.out.println("Apartment does not exist. Try again.");
						break;
					}
					
					boolean wasRentRem = rentPQ.remove(address, number, zip);	//check to see if we removed it
					boolean wasFootageRem = footagePQ.remove(address, number, zip);

					cityCode = rem.getCity().hashCode();
					
					checkCityRent.get(cityCode).remove(address, number, zip);		//get values from hashmap
					checkCityFootage.get(cityCode).remove(address, number, zip);
					
					if(wasRentRem && wasFootageRem) //if removed
					{
						System.out.println("The apartment has been removed");
					}
					
					else //otherwise
					{
						System.out.println("It seems our database could not find that apartment to remove"
								+ ".\nPlease try again and make sure everything is correct");
					}
					
					break;
					
					
				case 4:				//lowest price
					Apartment minRent = rentPQ.getMinRent();
					if(minRent == null) //check to see if nothing first
					{
						System.out.println("No apartments are available");
						break;
					}
					System.out.println("\nLowest Priced Apartment to Rent: \n\n"+minRent.toString());
					
					break;
					
				case 5:				//highest square footage
					Apartment maxFootage = footagePQ.getMaxFootage();
					if(maxFootage == null)	//check to see if null first
					{
						System.out.println("No apartments are available");
						break;
					}
					System.out.println("\nHighest Square Footage Apartment: \n\n"+maxFootage.toString());
					break;
					
				case 6:				//lowest priced by city
					System.out.print("Please enter the city you would like to find the lowest rent for: ");
					city = details.nextLine();
					
					uniqueCity = city;
					cityCode = uniqueCity.hashCode();	//grab the unique code associted with what user enters
					
					AptPQ minCityRent;
					Apartment low;
					
					if(checkCityRent.containsKey(cityCode)) //check to see if we have it, if we return a null, or print out what is requested
					{
						minCityRent = checkCityRent.get(cityCode);
						if(minCityRent == null)
						{
							System.out.println("No apartments are available");
							break;
						}
						//minCityRent.print();
						
						low = minCityRent.getMinRent();
						if(low == null)
						{
							System.out.println("No apartments are available");
							break;
						}
						System.out.println("\nLowest Priced Rent for "+city+" is: \n\n"+low.toString());
					}
					
					else
						System.out.println("Sorry, the city you are searching for isn't in our system. Please check to make sure your entry was correct");
					
					break;
					
				case 7: 			//highest square footage by city
					System.out.print("Please enter the city you would like to find the highest square footage for: ");
					city = details.nextLine();
					
					uniqueCity = city;		//same function as case 6 but for footage
					cityCode = uniqueCity.hashCode();
					
					if(checkCityFootage.containsKey(cityCode))
					{
						if(checkCityFootage.get(cityCode).getMaxFootage() == null)
						{
							System.out.println("No apartments are available");
							break;
						}
						System.out.println("\nHighest square footage apartment in "+city+" is: \n\n"+checkCityFootage.get(cityCode).getMaxFootage().toString());
					
					}
					else
						System.out.println("Sorry, the city you are searching for isn't in our system. Please check to make sure your entry was correct");
					
					break;
					
				case 8:				//quit
					run = false;
			}		
		}
		
		System.out.println("Thanks for using! Program has ended");
	}	
}
