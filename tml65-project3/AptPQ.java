import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.HashMap;

public class AptPQ 
{
	private int n = 0;		//num items in pq
	private int initSize = 11; //small enough that we dont waste space but large enough to prevent resizing right away
	private Apartment[] prioQ;
	private boolean thePriority; 	//are we doing min or max
	HashMap<Integer, Integer> PQHash = new HashMap<>();
	
	public AptPQ(boolean thePriority) 
	{
		n = 0;
		this.thePriority = thePriority; //make thisPriority variable = to what is passed in
		prioQ = new Apartment[initSize]; //create apartment array of size 11
	}
	
	public int getSize()
	{
		return n; //grab number of items in our pq
	}
	
	public boolean isEmpty()
	{
		return n == 0;
	}
	
	public Apartment getMinRent()
	{
		if(this.isEmpty())
			return null;
		return prioQ[1]; 	//just return first item in pq to get min
	}
	
	public Apartment getMaxFootage()
	{
		if(this.isEmpty())
			return null;
		return prioQ[1];	//just return first item in pq to get max
	}
	
	public boolean remove(String st, String aptNum, int zip)
	{
		//pq for each city with rent and footage
		String uniqueHash = st + aptNum + zip;
		//System.out.println(uniqueHash);
		int uniqueCode = uniqueHash.hashCode();
		//System.out.println("hashcode = "+uniqueCode);
		
		if(PQHash.containsKey(uniqueCode))
		{
			Integer toRemove = PQHash.get(uniqueCode);
			if(toRemove == null)
				return false;
			//System.out.println(PQHash.get(uniqueCode).toString());
			//System.out.println("the apt to remove is: "+toRemove);
			
			//System.out.println("apartment    "+prioQ[toRemove].toString());
			
			exch(toRemove, n);	//swap prio item with bottom most occurence 
			
			n--;	//decrease the size after exchange so that item is no longer in the heap
			
			sink(toRemove);	//puts the value at the bottom of the heap
			
			PQHash.remove(uniqueCode);	//remove from hashmap
			return true;
		}
		
		else
			return false; 		
	}
	
	public void insert(Apartment complex)
	{
		if(complex == null) //meaning we try to insert nothing
			return;
		
		String uniqueHash = complex.getAddress() + complex.getNum() + complex.getZip();
		int uniqueCode = uniqueHash.hashCode(); //create a unique code from our apartment string values
		
		n++; //increase items by 1
		//System.out.println("n is: "+n);
		
		if (n >= prioQ.length) //resize pq if we are going to create a scenario where we go out of bounds
			resizePQ();
		
		prioQ[n] = complex;		//priority queue start at 1 for heap based implementation according to book		
		
		PQHash.put(uniqueCode, n);		//place that code in the HashMap
		//System.out.println("apartment: \n" + complex +"\n at " + n);
		
		swim(n);
	}
	
	public boolean update(String address, String aptNum, int zip, double upRent)
	{
		String uniqueHash = address + aptNum + zip;
		int uniqueCode = uniqueHash.hashCode();
		//System.out.println("unique code: "+uniqueCode);
		
		if(PQHash.containsKey(uniqueCode))
		{
			Integer upSpot = PQHash.get(uniqueCode);	//spot we need to update
			if(upSpot == null)
				return false;
			
			prioQ[upSpot].Rent = upRent; //update rent value
			
			sink(upSpot);		//sink and swim it in the heap
			swim(upSpot);
			
			return true;						
		}
		
		return false;	
	}
	
	//will double size of our pq upon call
	public void resizePQ() 
	{
		Apartment[] temp = new Apartment[2*initSize];	//make new apartment array
		
		for(int i = 0; i < n; i++)
		{
			temp[i] = prioQ[i]; 	//copy over items from old array to new array
		}
		
		prioQ = temp; //make old array equal to new array
		initSize = 2*initSize; //update initSize since we changed it to be used for resize again
		
		return; 
	}
	
	/****
	 * General Helper Functions
	 ****/
	
	public Apartment get(String address, String aptNum, int zip)
	{
		String uniqueHash = address + aptNum + zip;
		int uniqueCode = uniqueHash.hashCode();
		if (PQHash.get(uniqueCode) == null) return null;
	
		return prioQ[PQHash.get(uniqueCode)];
	}
	
	private boolean greater(int i, int j)
	{
		//boolean x = prioQ[i].getRent() > prioQ[j].getRent();
		//System.out.println("pq[i] is "+prioQ[i].getRent()+ ((x) ? " > " : "<=") +prioQ[j].getRent()+" is pq[j]");
		return 	prioQ[i].getRent() > prioQ[j].getRent(); //return true if first has the greater rent to exchange
	}
	
	private boolean less(int i, int j)
	{
		//boolean x = prioQ[i].getFootage() < prioQ[j].getFootage();
		//System.out.println("pq[i] is "+prioQ[i].getFootage()+ ((x) ? " < " : " >=")+prioQ[j].getFootage()+" is pq[j]");
		return prioQ[i].getFootage() < prioQ[j].getFootage(); //return true if i footage < j footage to exchange
	}
	
	private void exch(int i, int j)
	{
		//System.out.println("Exchanging pq i "+prioQ[i].getFootage()+" with pq j "+prioQ[j].getFootage());
		int hash1 = prioQ[i].hashCode();
		int hash2 = prioQ[j].hashCode();
		
		PQHash.put(hash1, j);		//swap in the hashmap
		PQHash.put(hash2, i);
		
		Apartment temp = prioQ[i];
		prioQ[i] = prioQ[j];
		prioQ[j] = temp;
	}
	
	/***
	 * Heap helper functions taken from indexMinPQ.java
	 ***/
	private void swim(int k)
	{
		if(!thePriority) //if not true (false) we are doing this based on rent
		{
			while(k > 1 && greater(k/2,k))
			{
				exch(k, k/2);
				k = k/2;
			}
		}
		
		else		//thePriority is true so we go based off of footage
		{
			while(k > 1 && less(k/2,k))
			{
				exch(k, k/2);
				k = k/2;
			}
		}
	}
	
	private void sink(int k)
	{
		if(!thePriority)  //go based off rent if not true
		{
			while(2*k <= n)
			{
				int j = 2*k;
				if(j < n && greater(j, j + 1))
					j++;
				if(!greater(k,j))
					break;
				exch(k,j);
				k = j;
			}
		}
		
		else		//otherwise go based off square footage
		{
			while(2*k <= n)
			{
				int j = 2*k;
				if(j < n && less(j, j + 1))
					j++;
				if(!less(k,j))
					break;
				exch(k,j);
				k = j;
			}
		}
	}
	
	public void print()	//print method for debugging purposes
	{
		for(int i = 1; i <= n; i++)
		{
			if(prioQ[i] == null)
				break;
			System.out.println(prioQ[i].toString());
		}
	}
}
