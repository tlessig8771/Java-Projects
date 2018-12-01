// CS 0445 Spring 2018 - Trent Lessig - Assignment #3
// Read this class and its comments very carefully to make sure you implement
// the class properly.  The data and public methods in this class are identical
// to those MyStringBuilder, with the exception of the two additional methods
// shown at the end.  You cannot change the data or add any instance
// variables.  However, you may (and will need to) add some private methods.
// No iteration is allowed in this implementation. 

// For more details on the general functionality of most of these methods, 
// see the specifications of the similar method in the StringBuilder class.  
public class MyStringBuilder2
{
	// These are the only three instance variables you are allowed to have.
	// See details of CNode class below.  In other words, you MAY NOT add
	// any additional instance variables to this class.  However, you may
	// use any method variables that you need within individual methods.
	// But remember that you may NOT use any variables of any other
	// linked list class or of the predefined StringBuilder or 
	// or StringBuffer class in any place in your code.  You may only use the
	// String class where it is an argument or return type in a method.
	private CNode firstC;	// reference to front of list.  This reference is necessary
							// to keep track of the list
	private CNode lastC; 	// reference to last node of list.  This reference is
							// necessary to improve the efficiency of the append() method
	private int length;  	// number of characters in the list

	// You may also add any additional private methods that you need to
	// help with your implementation of the public methods.

	// Create a new MyStringBuilder2 initialized with the chars in String s
	
	// Constructor to make a new MyStringBuilder2 from a String.  The constructor
	// itself is NOT recursive – however, it calls a recursive method to do the
	// actual set up work.  This should be your general approach for all of the
	// methods, since the recursive methods typically need extra parameters that
	// are not given in the specification.
	public MyStringBuilder2(String s)
	{
	      if (s != null && s.length() > 0)
	            makeBuilder(s, 0);

	      else  // no String so initialize empty MyStringBuilder2
	      {
	            length = 0;
	            firstC = null;
	            lastC = null;
	      }
	}

	// Recursive method to set up a new MyStringBuilder2 from a String
	private void makeBuilder(String s, int pos)
	{
	      // Recursive case – we have not finished going through the String
	      if (pos < s.length()-1)
	      {
	            // Note how this is done – we make the recursive call FIRST, then
	            // add the node before it.  In this way the LAST node we add is
	            // the front node, and it enables us to avoid having to make a
	            // special test for the front node.  However, many of your
	            // methods will proceed in the normal front to back way.
	            makeBuilder(s, pos+1);
	            firstC = new CNode(s.charAt(pos), firstC);
	            length++;
	      }

	      else if (pos == s.length()-1) // Special case for last char in String
	      {                             // This is needed since lastC must be set to point to this node
	            firstC = new CNode(s.charAt(pos));
	            lastC = firstC;
	            length = 1;
	      }

	      else  // This case should never be reached, due to the way the constructor is set up.  However, I included it as a
	      {     // safeguard (in case some other method calls this one)
	            length = 0;
	            firstC = null;
	            lastC = null;
	      }
	}

	// Create a new MyStringBuilder2 initialized with the chars in array s
	public MyStringBuilder2(char [] s)
	{
		if (s != null && s.length > 0)
            makeBuilder(s, 0);

		else  // no char array so initialize empty MyStringBuilder2
		{
            length = 0;
            firstC = null;
            lastC = null;
		}
	}
	
	//recursive method to set up a MyStringBuilder2 from a char array
	private void makeBuilder(char[] s, int pos)
	{
	      if (pos < s.length-1) 	      // Recursive case – we have not finished going through the String
	      {
	            makeBuilder(s, pos+1);
	            firstC = new CNode(s[pos], firstC);
	            length++;
	      }

	      else if (pos == s.length-1) // Special case for last char in String
	      {                             // This is needed since lastC must be set to point to this node
	            firstC = new CNode(s[pos]);
	            lastC = firstC;
	            length = 1;
	      }

	      else  // This case should never be reached, due to the way the constructor is set up.  However, I included it as a
	      {     // safeguard (in case some other method calls this one)
	            length = 0;
	            firstC = null;
	            lastC = null;
	      }
	}

	// Create a new empty MyStringBuilder2
	public MyStringBuilder2()
	{
		firstC = null;
		lastC = null;
		length = 0;
	}

	// Append MyStringBuilder2 b to the end of the current MyStringBuilder2, and
	// return the current MyStringBuilder2.  Be careful for special cases!
	public MyStringBuilder2 append(MyStringBuilder2 b)
	{
		if(b == null) //do nothing if there is no msb to attach
			return this;
		
		MyStringBuilder2 msb = this;
		b = new MyStringBuilder2(b.toString()); //copy of b with new node referencing
		
		if(msb.lastC != null) //ensuring that we have a next node
			msb.lastC.next = b.firstC;
		
		if(msb.firstC == null) //when msb front is empty/null we will update the front - case for adding to a initialized null
			msb.firstC = b.firstC;
		
		this.length = this.length + b.length; //increment the length of the current msb
		
		if(b.lastC != null) //update the last when lastC is not null
			msb.lastC = b.lastC;
		
		return msb;
	}

	// Append String s to the end of the current MyStringBuilder2, and return
	// the current MyStringBuilder2.  Be careful for special cases!
	public MyStringBuilder2 append(String s)
	{
		CNode curr = this.lastC;
		int start = 0;
		
		if(s.length() == 0) //if there is nothing
			return this;
		
		if(this.length == 0) //appending to the front
		{
			curr = new CNode(s.charAt(0)); //make new node
			this.firstC = curr; //make that the front
			start++; 
			this.length++; //increase length
			appendString(s,start,curr); //call recursive method
		}
		
		else //not appending to the front
			appendString(s,start,curr); //make call to recursive method
				
		return this;
	}
	
	//recurse to append the other nodes from the string
	private CNode appendString(String s, int pos, CNode curr)
	{
		if(pos < s.length()-1) //recursive case
		{
			this.lastC = curr;
			curr.next = new CNode(s.charAt(pos)); //make node
			
			appendString(s,pos+1,curr.next); //call function with updated variables
			
			this.length++;
		}
	
		else if(pos == s.length()-1) //base case
		{
			curr.next = new CNode(s.charAt(pos)); //make node
			curr = curr.next;
			this.lastC = curr; //update variables
			this.length++;
		}		
		
		return curr;
	}

	// Append char array c to the end of the current MyStringBuilder2, and
	// return the current MyStringBuilder2.  Be careful for special cases!
	public MyStringBuilder2 append(char [] c)
	{
		CNode curr = this.lastC;
		int start = 0;
		
		if(c.length == 0) //if there is nothing
			return this;
		
		if(this.length == 0) //appending to the front
		{
			curr = new CNode(c[0]); //making the nodes
			this.firstC = curr;
			start++;
			this.length++;
			appendCharArr(c,start,curr); //call to function
		}
		
		else //not appending to the front
			appendCharArr(c,start,curr);
		
		return this;
	}
	
	private CNode appendCharArr(char [] c, int pos, CNode curr)
	{
		if(pos < c.length-1) //recursive case
		{
			this.lastC = curr;
			curr.next = new CNode(c[pos]); //make node
			appendCharArr(c,pos+1,curr.next); //call function again with updated parameters
			this.length++;
		}
	
		else if(pos == c.length-1) //base case
		{
			curr.next = new CNode(c[pos]); //make node
			curr = curr.next;
			this.lastC = curr; //update variables
			this.length++;
		}
		
		return curr;
	}

	// Append char c to the end of the current MyStringBuilder2, and
	// return the current MyStringBuilder2.  Be careful for special cases!
	public MyStringBuilder2 append(char c)
	{
		CNode curr = new CNode(c);
		
		if(this.length == 0) //make front node the new c node
		{
			this.firstC = curr;
			this.length++; //increase length
		}
		
		else //add at end of msb
		{
			this.lastC.next = curr; //node after end equal to new node
			this.length++;
		}
		
		this.lastC = curr; //link
		
		return this;
	}

	// Return the character at location "index" in the current MyStringBuilder2.
	// If index is invalid, throw an IndexOutOfBoundsException.
	public char charAt(int index)
	{
		CNode curr = this.firstC;
		
		if(index >= this.length) //if invalid
			throw new IndexOutOfBoundsException("Index is out of bounds - cannot retrieve");
		
		else //valid - make call to recursive method
		{
			char data = charAtRec(index,0,curr); //passing index, a count, and node into method call
			return data; //return result
		}		
	}
	
	private char charAtRec(int index, int count, CNode curr)
	{
		if(count == index) //base case - just return data in the node found at index
			return curr.data;
		
		else //recursive case - return call to method with updated parameters until base is reached by incrementing count and moving to next node
			return charAtRec(index,count+1,curr.next);
	}

	// Delete the characters from index "start" to index "end" - 1 in the
	// current MyStringBuilder2, and return the current MyStringBuilder2.
	// If "start" is invalid or "end" <= "start" do nothing (just return the
	// MyStringBuilder2 as is).  If "end" is past the end of the MyStringBuilder2, 
	// only remove up until the end of the MyStringBuilder2. Be careful for special cases!
	public MyStringBuilder2 delete(int start, int end)
	{
		int pos; //var to keep track
		
		if(start < 0 || start > this.length || end <= start) //invalid case
			return this;
		
		if(start == 0) //if deleting from the front
		{
			pos = 0;
			CNode node = findNode(this.firstC, end,pos); //call method
			this.firstC = node.next;
			this.length -= end-start; //decrease length by the number of nodes removed
		}
		
		else if(end > this.length) //case for if end is larger than msb2 length
		{
			end = this.length; //make end == to msb2 length
			pos = 0;
			
			CNode node1 = findNode(this.firstC,start,pos); //find start node
			CNode node2 = findNode(this.firstC,end,pos); //find end node
			
			node1.next = node2.next; //link nodes (removes between start and end)
			this.length -= end-start; //update msb2 length
		}
		
		else //case not deleting at front or if end larger than msb2 length
		{
			pos = 0;
			
			CNode node1 = findNode(this.firstC,start,pos); //find the "start" and "end" nodes
			CNode node2 = findNode(this.firstC,end,pos);
			
			node1.next = node2.next; //link - removing nodes in between
			this.length -= end-start; //update length			
		}
		
		return this;
	}

	// Delete the character at location "index" from the current
	// MyStringBuilder2 and return the current MyStringBuilder2.  If "index" is
	// invalid, do nothing (just return the MyStringBuilder2 as is).
	// Be careful for special cases!
	public MyStringBuilder2 deleteCharAt(int index)
	{
		if(index < 0 || index > this.length) //invalid index
			return this;
		
		else //valid index
		{
			if(index == 0) //delete character at the front
			{
				this.firstC = this.firstC.next; //unlink unwanted node
				this.length--; //decrement length
			}
			
			else
			{
				int pos = 0;
				CNode node = findNode(this.firstC,index,pos); //call the findNode recursive method
				node.next = node.next.next; //connect to node after after to remove single character
				this.length--;	//decrement length
			}
		}
		
		return this;
	}
	
	private CNode findNode(CNode curr, int index, int pos) //recursive method for finding a node given an index
	{
		if(pos == index-1) //base case
			return curr;
		
		if(index == 0) //ensure index doesn't go past 0
			return curr;
		
		else //rec case - pass in updated parameters by moving to next node and increasing pos
			return findNode(curr.next,index,pos+1);
	}

	// Find and return the index within the current MyStringBuilder2 where
	// String str first matches a sequence of characters within the current
	// MyStringBuilder2.  If str does not match any sequence of characters
	// within the current MyStringBuilder2, return -1.  Think carefully about
	// what you need to do for this method before implementing it.
	public int indexOf(String str)
	{
		CNode checkNode = this.firstC;
		CNode trackerNode = this.firstC;
		int i = 0;
		int j = 0; //counter for position of checkNode
		
		int value = indexRecurse(checkNode, trackerNode, i, j, str); //call method
		
		return value;
	}
	
	private int indexRecurse(CNode check, CNode track, int i, int j, String str) //recursive case to find an index
	{
		if((j+str.length()) > this.length) //if the length of string to check is greater than the length of the MSB full match not found return -1
			return -1;
		
		if(i == str.length()-1) //if found return the value of j
			return j;
		
		else if(str.charAt(i) == check.data) //statement for comparing node data and string data
		{
			if(track.next == null && check.next == null) //case that none is found
				return -1;
			
			return indexRecurse(check.next, track, i+1,j,str); //recurse to finding if characters will meet sequence - increase i move next node
		}
		
		else if(str.charAt(i) != check.data) //statement for data not matching string data
		{
			i = 0; //reset this count
			
			if(track.next == null || check.next == null) //not found
				return -1;
			
			else //move to next node and recurse to repeat above statements
			{
				track =  track.next;
				return indexRecurse(check=track,track,i,j+1,str); //updated parameters passed in. check now = to track since we moved to next node
			}
		}
		
		return -1;
	}

	// Insert String str into the current MyStringBuilder2 starting at index
	// "offset" and return the current MyStringBuilder2.  if "offset" == 
	// length, this is the same as append.  If "offset" is invalid do nothing.
	public MyStringBuilder2 insert(int offset, String str)
	{
		MyStringBuilder2 msb2 = new MyStringBuilder2(str); //make a new msb from the string which creates the nodes
		CNode curr = this.firstC;
		int pos = 0;
		
		if(offset == this.length) //case if at end of current msb, then just append it
			this.append(str);
		
		else if(offset == 0) //inserting at the front
		{
			msb2.lastC.next = this.firstC; //linking up the nodes 
			this.firstC = msb2.firstC;
			this.length += msb2.length; //updating the length of the current msb
		}
		
		else //inserting elsewhere
		{
			CNode node = findNode(curr, offset, pos); //recursive call
			msb2.lastC.next = node.next; //link the nodes
			node.next = msb2.firstC;
			this.length += msb2.length;
		}
		
		return this;
	}
	
	// Insert character c into the current MyStringBuilder2 at index
	// "offset" and return the current MyStringBuilder2.  If "offset" ==
	// length, this is the same as append.  If "offset" is invalid, do nothing.
	public MyStringBuilder2 insert(int offset, char c)
	{
		CNode node = new CNode(c); //make the node first from char c
		CNode curr = this.firstC;
		int pos = 0;
		
		if(offset == this.length) //if at end append
			this.append(c);
		
		else if(offset == 0) //inserting the char at front
		{
			node.next = this.firstC;
			this.firstC = node;
			this.length += 1;
		}
		
		else //inserting the char c elsewhere
		{
			CNode find = findNode(curr,offset,pos); //find the node
			node.next = find.next;  //link 
			find.next = node; 		//nodes
			this.length += 1; //update length
		}
		
		return this;
	}

	// Insert char array c into the current MyStringBuilder2 starting at index
	// index "offset" and return the current MyStringBuilder2.  If "offset" is invalid, do nothing.
	public MyStringBuilder2 insert(int offset, char [] c)
	{
		MyStringBuilder2 msb2 = new MyStringBuilder2(c); //create nodes from the char array
		CNode curr = this.firstC;
		int pos = 0;
		
		if(offset == this.length) //append if at end
			this.append(c);
		
		else if(offset == 0) //insert at front
		{
			msb2.lastC.next = this.firstC;
			this.firstC = msb2.firstC;
			this.length += msb2.length;
		}
		
		else //insert at given position
		{
			CNode node = findNode(curr, offset, pos); //recursive call
			msb2.lastC.next = node.next; //link
			node.next = msb2.firstC;	//nodes
			this.length += msb2.length;
		}
		
		return this;
	}

	public int length() // Return the length of the current MyStringBuilder2
	{
		return this.length;
	}

	// Delete the substring from "start" to "end" - 1 in the current
	// MyStringBuilder2, then insert String "str" into the current
	// MyStringBuilder2 starting at index "start", then return the current
	// MyStringBuilder2.  If "start" is invalid or "end" <= "start", do nothing.
	// If "end" is past the end of the MyStringBuilder2, only delete until the
	// end of the MyStringBuilder2, then insert.  This method should be done
	// as efficiently as possible.  In particular, you may NOT simply call
	// the delete() method followed by the insert() method, since that will
	// require an extra traversal of the linked list.
	public MyStringBuilder2 replace(int start, int end, String str)
	{
		CNode curr = this.firstC;
		int i = 0; //parameter for the string position
		int pos = 0;
		
		if(end > this.length) //make end == to length of msb if end is over the max length
			end = this.length;
		
		if(start < 0 || start > this.length || end <= start) //invalid case
			return this;
		
		if(start == this.length)
			this.append(str); //append if start is at the end of the msb
		
		if(start >= 0 && end <= this.length)
		{
			if(start == 0)
			{
				MyStringBuilder2 msb = new MyStringBuilder2(str.substring(end,str.length())); //make new stringbuilder to make easier to connect nodes
				
				curr.data = str.charAt(0); //make front node the data from the first char in string
				//curr = findNode(curr,start,0); //call recursive method to find start position - realized this wasnt necessary as its just the start node
				
				curr = updateNodesRec(curr,start,end,str,i+1,pos+1); //replace necessary nodes and their data
				
				this.length += msb.length;	//change the length of the current msb			
				msb.lastC.next = curr.next; //reference to next node
				curr.next = msb.firstC;		 //connect that reference		
			}
			
			else
			{
				CNode nodeToStart = findNode(curr, start, 0); //find the start position
				CNode endNode = findNode(curr,end+1,0); //find the end position
				i = start; //make i = to start for node replacement

				nodeToStart = updateNodesRec(nodeToStart,start,end,str,i,pos); //replace necessary nodes
				i = end-start; //make sure we are at end of replacement position
				nodeToStart = addNodes(nodeToStart,i,str); //make the new nodes if needed

				nodeToStart.next = endNode; //connect the nodes to have a linked msb
			}
		}
		
		return this;
	}
	
	private CNode updateNodesRec(CNode startNode, int start, int end, String str, int i, int pos) //method to update the data in the nodes
	{
		if(i < end && pos < str.length()) //avoiding possible index out of bounds
		{
			startNode.next.data = str.charAt(pos); //replace data with that of the string character position
			return updateNodesRec(startNode.next,start,end,str,i+1,pos+1); //recurse to next, increase position
		}
		
		return startNode;
	}
	
	private CNode addNodes(CNode startNode, int i, String str) //method to add nodes
	{
		if(i < str.length()) //go to length of string if needed to make nodes
		{
			startNode.next = new CNode(str.charAt(i)); //create the node
			this.length++; //increment length
			return addNodes(startNode.next,i+1,str); //recurse to nextnode
		}
		
		return startNode;
	}

	// Reverse the characters in the current MyStringBuilder2 and then return the current MyStringBuilder2.
	public MyStringBuilder2 reverse()
	{		
		if(this.firstC == null) //invalid
			return this;

		else
			recReverse(this.firstC); //pass in front node to recursive call
				
		return this;
	}
	
	private CNode recReverse(CNode curr) //reverse the MSB
	{
		if(curr.next == null) //finding what to make front
			this.firstC = curr;
		
		else
			recReverse(curr.next).next = curr; //recurse and update curr

		curr.next = null; //make reference to next node null
		this.lastC = curr;  //the last node equals the front node technically
		
		return curr;
	}
	
	// Return as a String the substring of characters from index "start" to
	// index "end" - 1 within the current MyStringBuilder2
	public String substring(int start, int end)
	{
		if(start > end) //case for if start greater than end so it makes it invalid
			throw new IllegalArgumentException("Start cannot be greater then end");
		
		char[] arr = new char[end-start]; //set length of new char array for use to get to string
		CNode curr = firstC; //variable to reference to front node
		int j = 0; //counter
		int rLength = end-start; //length of number of characters to return
		
		CNode startNode = findNode(curr,start+1,j); //find startNode
		
		substringRec(startNode,arr,j,rLength); //pass in these variables to recursive method
		
		return new String(arr); //return the array of characters
	}
	
	private void substringRec(CNode start, char[] arr, int count, int rLength)
	{
		if(count == rLength) //if equal to length of the amount of characters to return - base case
			return;
		
		else
		{
			arr[count] = start.data; //put the data from the node into array
			substringRec(start.next,arr,count+1,rLength); //call the method with updated parameters - move to next node, increment count
		}
	}

	// Return the entire contents of the current MyStringBuilder2 as a String
	
	// Again note that the specified method is not actually recursive – rather it
	// calls a recursive method to do the work.  Note that in this case we also
	// create a char array before the recursive call, then pass it as a
	// parameter, then construct and return a new string from the char array.
	// Carefully think about the parameters you will be passing to your recursive
	// methods.  Through them you must be able to move through the list and
	// reduce the "problem size" with each call.
	public String toString()
	{
	      char [] c = new char[length];
	      getString(c, 0, firstC);
	      return (new String(c));
	}

	// Here we need the char array to store the characters, the pos value to
	// indicate the current index in the array and the curr node to access
	// the data in the actual MyStringBuilder2.  Note that these rec methods
	// are private – the user of the class should not be able to call them.
	private void getString(char [] c, int pos, CNode curr)
	{
	      if (curr != null)
	      {
	            c[pos] = curr.data;
	            getString(c, pos+1, curr.next);
	      }
	}

	// Find and return the index within the current MyStringBuilder2 where
	// String str LAST matches a sequence of characters within the current
	// MyStringBuilder2.  If str does not match any sequence of characters
	// within the current MyStringBuilder2, return -1.  Think carefully about
	// what you need to do for this method before implementing it.  For some
	// help with this see the Assignment 3 specifications.
	public int lastIndexOf(String str)
	{
		CNode checkNode = this.firstC;
		int i = 0;
		int j = 0; //counter for position of checkNode
		
		int value = lastIndexRecurse(checkNode, i, j, str); //call method
		
		return value;
	}
	
	private int lastIndexRecurse(CNode check, int i, int j, String str) //recursive case to find an index
	{
		if(check == null) //case not found/ passed the end of msb2
			return -1;
		
		int value = lastIndexRecurse(check.next,0,j+1,str); //go all the way to the end
		
		if(value != -1) //if value doesn't = -1 we can just return value
			return value;
		
		if(str.charAt(i) == check.data) //checking the data of string with data of node
		{
			if(i == str.length()-1) //checking that string comp does not go out of bounds
				return j; //if we reach end of string to match and the whole thing matches return value of j
			
			return lastIndexRecurse(check.next,i+1,j,str);  //else, move up i and check the next node
		}
		
		else //not found
			return -1;
	}
	
	// Find and return an array of MyStringBuilder2, where each MyStringBuilder2
	// in the return array corresponds to a part of the match of the array of
	// patterns in the argument.  If the overall match does not succeed, return
	// null.  For much more detail on the requirements of this method, see the Assignment 3 specifications.
	public MyStringBuilder2 [] regMatch(String [] pats)
	{
		int state = 1; //var for what state we in
		int i = 0; //what pats we on
		CNode curr = this.firstC;
		MyStringBuilder2 [] ans = new MyStringBuilder2[pats.length]; //return this array once done
		
		initialize(i,ans,pats.length); //make ans not null by initializing the array spaces
		
		if(regMatchRec(curr,i,state,ans,pats,0)) //if this returns something
				return ans;		
		
		return null;
	}
	
	private void initialize(int i, MyStringBuilder2 [] ans, int lengthPats) //making the array spaces non null in the MSB2 ans array
	{
		if(i < lengthPats) //go up to length of # of patterns
		{
			ans[i] = new MyStringBuilder2(); //make new msb in position of pattern
			initialize(i+1,ans,lengthPats); //call again
		}
		
		else
			return;		
	}
	
	private boolean regMatchRec(CNode curr, int i, int state, MyStringBuilder2 [] ans, String [] pats, int length) //backtracking method to find patterns :/
	{		
		if(state == 1) //we in state 1
		{
			if(length == this.length) //if we reached the end of the msb
				return false;
			
			else if(!pats[i].contains(String.valueOf(curr.data))) //if the data in the node does not match any character in the given pattern
				return regMatchRec(curr.next,i,1,ans,pats,length+1); //stay in state 1 but move to the next node to compare
						
			else //could be a match
			{
				if(regMatchRec(curr,i,2,ans,pats,length)) //go and check to see if we have a match by moving to state 2
					return true;
				
				else
					return regMatchRec(curr.next,i,1,ans,pats,length+1); //no match so just go back to state 1
			}
		}
		
		else if(state == 2) //now we in state 2
		{
			if(length == this.length || !pats[i].contains(String.valueOf(curr.data))) //if nothing matched
			{
				if(i == pats.length-1) //when all patterns have had a match
					return true;
				
				return regMatchRec(curr,i+1,3,ans,pats,length); //otherwise move to next pattern and move to state 3 to check
			}
			
			else //if there is a match
			{
				ans[i].append(curr.data); //add the current nodes data to the pats array position we are in
				
				if(regMatchRec(curr.next,i,2,ans,pats,length+1)) //try matching the next node with the same pattern by moving back to state 2
					return true;
				
				else //we didn't find a match
				{
					ans[i].deleteCharAt(ans[i].length-1); //be sure to delete the previous character we attempted to add
					
					if(regMatchRec(curr,i+1,3,ans,pats,length)) //move to next pattern and state 3 to retry
						return true;
					
					else //didn't work
						return false;
				}				
			}
		}
		
		else if(state == 3) //state 3 we in
		{
			if(length == this.length || !pats[i].contains(String.valueOf(curr.data))) //if nothing matched
			{
				return false;
			}
			
			else //if matched
			{
				ans[i].append(curr.data); //add the curr node data to the array by the position we are in in pats
				
				if(regMatchRec(curr.next,i,2,ans,pats,length+1)) //go and try to match next node to pat by moving back to state 2
					return true;
				
				else //if it doesn't match
				{
					ans[i].deleteCharAt(ans[i].length-1); //we couldn't match all so delete previously added data
					return false; //end up backtracking
				}
			}
		}
		return false;
	}
	
	// You must use this inner class exactly as specified below.  Note that
	// since it is an inner class, the MyStringBuilder2 class MAY access the
	// data and next fields directly.
	private class CNode
	{
		private char data;
		private CNode next;

		public CNode(char c)
		{
			data = c;
			next = null;
		}

		public CNode(char c, CNode n)
		{
			data = c;
			next = n;
		}
	}
}