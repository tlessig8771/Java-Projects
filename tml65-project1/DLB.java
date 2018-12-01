import java.util.ArrayList;

public class DLB 
{
	//private static final int totalNumSuggs = 5;
	private final char terminator = '^'; //how we know if we reach a valid word
	Node root;
	//ArrayList<String> suggest = new ArrayList<>();
	String addThis = "";
	int addedSuggs = 0;
	

	public DLB() 	//Constructor to initialize a new node
	{
		root = new Node();
	}

	public boolean add(String word) //add our strings from the dictionary file to our dlb trie
	{
		boolean added = false; //boolean statement to determine if we actually add the string to the trie

		word += terminator; //add our end of string character to know its the end of the word

		Node curr = root; //have a var that contains the root so we don't modify the root

		for (int i = 0; i < word.length(); i++) 	  //add each char of the string to a node including the terminator so we know where to stop in the future
		{
			char letter = word.charAt(i); 			//get each character of the string 
			addNode set = setChild(curr, letter);  //set the child node to the first node created for the word we are adding
			curr = set.node; 					  //move curr to the next node
			added = set.added;					 //check if add was successful
		}

		return added;
	}

//------------------------------------------------------------------------------------------//
//DO I REALLY NEED THIS METHOD//
	public int search(String c) 
	{
		Node curr = root;
		
		for (int i = 0; i < c.length(); i++) 
		{

			char letter = c.charAt(i); //get each letter from the string
			
			curr = getChild(curr, letter); //get the child of the current node
		
			if (curr == null) 
			{
				//System.out.println("NOT WORD");
				return 0; //if curr is null, we have not found a word
			}
		}

		Node keyNode = getChild(curr, terminator); //getting the value of what comes after our terminator

		if (keyNode == null) //keyNode is the node holding our terminator character
		{
			//System.out.println("PREFIX, NOT WORD");
			return 1; //we found a pre fix to the word
		}

		else if (keyNode.sibling == null) 
		{
			//System.out.println("WORD, NOT PREFIX");
			return 2; //if the sibling to our terminator node is null, we have found a word only
		}

		else 
		{
			//System.out.println("BOTH");
			return 3; //if there is a sibling and terminator, we have found both a prefix and a word
		}
	}
	
	public Node searchPrefix(String s, int countPreds)
	{
		Node curr = root;
		
		if(countPreds == 0)
			return root;
		
		for (int i = 0; i < s.length(); i++) 
		{
			char letter = s.charAt(i); //get each letter from the string
			
			curr = getChild(curr, letter); //get the child of the current node
		
			if (curr == null) //if our current pattern of searching for suggested words results in nothing being found, print that out
			{
				//System.out.println("\nNO SUGGESTIONS CAN BE MADE");
				return null; //if curr is null, we have not found a valid word
			}
		}

		if (curr.child == null) //keyNode is the node holding our terminator character
		{
			return null; //we found a pre fix to the word
		}

		return curr.child;
	}
	
	//we will have another searchSug method to pass in a node
	
	public void searchSug(ArrayList<String> suggestions, String s, int countPreds, boolean go)
	{
		String tempStr = s;
		Node searchNode = searchPrefix(s, countPreds); //find the current string of letters to get a start position to find suggestions from
		if(searchNode == null) //if we got nothing
			return;
		if(go) //if true  we will check user DLB first
			 searchUserSug(searchNode, suggestions,  tempStr, countPreds);
		else //false, we will check dictionary DLB 
			 searchSug(searchNode, suggestions, tempStr, countPreds);
	}
	
	public void searchSug(Node search, ArrayList<String> suggestions, String tempStr, int countPreds)
	{
		if(countPreds != 0 && suggestions.size() >= 5) 
			return;
		
		String appendChar = ""+search.value; //attach the current value of the node we are to the current string
		
		if(search.value == '^') //if we match our terminator character, we add it to the arraylist and do not add that terminator to the string
		{
			if(checkDuplicates(suggestions,tempStr)) 
			{
				suggestions.add(tempStr);
				appendChar = "";
			}
		}
		
		if(search.child != null) //if we can go down in our DLB recurse down
		{
			searchSug(search.child, suggestions, tempStr+appendChar, countPreds);
		}
		if(search.sibling != null) //if we can go right, recurse right in the DLB
		{
			searchSug(search.sibling, suggestions, tempStr, countPreds);
		}
		
	}
	
	public void searchUserSug(Node search, ArrayList<String> suggestions, String tempStr, int countPreds) //user history dlb
	{
		if(countPreds != 0 && suggestions.size() >= 5)
			return;
		
		String appendChar = ""+search.value; //same as above, this method just flips our recursive calls
		
		if(search.value == '^')
		{
			suggestions.add(tempStr);
			appendChar = "";
		}
		
		if(search.sibling != null) //here we go right first then down in order to get the most recently added word to the user dlb
		{
			searchUserSug(search.sibling, suggestions, tempStr, countPreds);
		}
		
		if(search.child != null)
		{
			searchUserSug(search.child, suggestions, tempStr+appendChar, countPreds);
		}

		
	}
	
	public boolean checkDuplicates(ArrayList<String> suggestWords, String tempStr)
	{
		for(int i = 0; i < suggestWords.size(); i++)
		{
			if(tempStr.equals(suggestWords.get(i)))
				return false;
		}
		
		return true;
	}

//---------------------------------------------------------------------------------------------//
//Add new sibling nodes method

	public addNode setSibling(Node sibling, char letter) 
	{

		if (sibling == null) 
		{
			sibling = new Node(letter);
			return new addNode(sibling, true); //add a new the new node with the character since we don't already have it
		}

		else 
		{
			Node next = sibling;

			while (next.sibling != null) //find sibling 
			{

				if (next.value == letter) //if current node equals given letter, we already have the node and do not need to add
				{
					break;
				}
				
				next = next.sibling; //move to next node
			}

			if (next.value == letter) 
			{
				return new addNode(next, false);  //don't add the sibling we already have this letter sibling
			}

			else 
			{
				next.sibling = new Node(letter); //make new node with current letter
				return new addNode(next.sibling, true); //add it
			}
		}
	}

//-------------------------------------------------------------------------------------//

	public Node getSibling(Node sibling, char letter) //return node containing char that is specified
	{
		Node next = sibling; //make reference to sibling node

		while (next != null) 
		{
			if (next.value == letter) 
			{
				break;
			}
			next = next.sibling;
		}

		return next;
	}

//-----------------------------------------------------------------------------------------------//

	public addNode setChild(Node parent, char letter) 
	{
		if (parent.child == null) 
		{
			parent.child = new Node(letter); //assign child node to parent node if there isnt already

			return new addNode (parent.child, true);
		}

		else 
		{
			//Otherwise set the child node to that letter
			return setSibling(parent.child, letter);
		}
	}

//-------------------------------------------------------------------------------------//

	public Node getChild(Node parent, char letter) 	 //return the child of the specified node given a certain letter
	{
		return getSibling(parent.child, letter);
	}

//-------------------------------------------------------------------------------------------------//
/**Our node class is private but our ac_test will still be able to access it because
 * we have this as an inner class within on DLB class
 **/

	public class Node 
	{
		Node sibling;    //node for siblings (i.e. each level in the trie could have a through z
		Node child;      //node for child (i.e. any node in the trie may have a node below it
		char value;
		
		public Node() 
		{
			this('$');
		} //constructors for nodes


		public Node(char letter) 
		{
			this(letter, null, null);  //node that contains a char with null references
		}
		
		public Node getChild()
		{
			return child;
		}

		public Node(char letter, Node sibling, Node child) 
		{
			this.value = letter;
			this.sibling = sibling; //node that will have a value, a valid sibling and a valid child
			this.child = child;
		}
	}

//--------------------------------------------------------------------------//
	
	private class addNode  //to determine whether or not a node was already added
	{
		Node node;
		boolean added;

		public addNode(Node node, boolean added) //when adding
		{
			this.node = node;
			this.added = added;
		}
	}
}