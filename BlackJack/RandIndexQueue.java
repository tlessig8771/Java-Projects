import java.util.*;
import java.io.*;

//Trent Lessig - Assignment 1
//Due February 2nd 2018

@SuppressWarnings("unchecked")

public class RandIndexQueue<T> implements MyQ<T>, Indexable<T>, Shufflable
{
	
	private int qSize; //Circular Queue Size
    private T[] array;
    private int rear;//rear position of Circular queue
    private int front; //front position of Circular queue     
    private int qMoves;

	public RandIndexQueue(int maxSize) 
	{
        array = (T[]) new Object[maxSize]; //creating generic array object
        qSize = 0;
        front = 0;
        rear = 0; //initialize variables to zero
        qMoves = 0;
    }
    
    //MyQ methods

    public boolean offer(T item)
    {
    	
    	if(qSize+1 == array.length) //resize array if condition is met
		{
			T [] newArray = (T[]) new Object[array.length*2]; //double array if resize is needed
			
			for(int i = 0; i < array.length;i++) //go through each item in the old array and copy to new array
			{
				newArray[i] = array[i];
			}
			array = newArray; //assign old array to the new one
		}
    	else
    	{
    		array[rear] = item; //adding the item to the array
            rear = (rear + 1) % array.length; 
            qSize++; //increment size and number of moves
    	}
        
    	qMoves++;
    	
    	return true;
    }

    public T poll()
    {
    	T pollElement; //generic variable
    	
        if (qSize == 0) //condition for if array is empty
        {
            return null;
        }
        else  //otherwise
        {
            pollElement = array[front]; 
           	front = ((front+1)%array.length);
        	qSize--; //descrease the size of the queue
           
        }
        qMoves++; //increment moves
        
        return pollElement;
    }
    
    public T peek()
    {
    	T element; //generic variable
    	
    	if(qSize == 0) //if queue is empty
			return null;
    	else
    	{
    		element = array[rear]; //get front element in queue and return it
    	}
    	
    	return element;
    }

    public boolean isFull() //method to see if queue is full
    {
        if(qSize == array.length+1)
        {
        	return true;
        }
        else
        	return false;
    }

    public boolean isEmpty() //method to see if queue is empty
    {
        if(qSize == 0)
        {
        	return true;
        }
        else
        	return false;
    }
    
    public int size() //method to get queue size variable
    {
    	return qSize;
    }
    
    public void clear() //method to clear queue/array by reinitializing the variables
    {
    	//Arrays.fill(array, 0);
    	front = 0;
    	rear = 0;
    	qSize = 0;
    }
    
    public int getMoves()
    {
    	return qMoves; //return value of the moves variable
    }
    
	public void setMoves(int moves)
	{
		qMoves = moves; //the queue moves set equal to the moves passed in as argument
	}
	
	//Shufflable method
	
	public void shuffle() //method for randomness
	{		
		int n = qSize;
		int change;
        Random shuffle = new Random();

        for (int i = front; i < n; i++) //go through queue
        {
            change = i + shuffle.nextInt(n - i); //randomizing a position to swap below
            
            T temp = array[i]; //creating temp variables and swapping around numbers
            array[i] = array[change];
            array[change] = temp;
        }	
	}
	
	//Indexable methods
	
	public T get(int i)
	{
		if(qSize < (i+1))
		{
			throw new IndexOutOfBoundsException();
		}
		return array[(i%qSize)+front];
	}
	
	public void set(int i, T item)
	{
		if(qSize < (i+1))
		{
			throw new IndexOutOfBoundsException();
		}
		array[(i%qSize)+front] = item;
	}
	
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		
		b.append("Contents: ");

		for(int i = front; i != rear; i=(i+1)%array.length)
		{
			b.append(array[i]+" ");
		}
		
		return b.toString(); //printing a nicely formatted string back to main
	}	
}
