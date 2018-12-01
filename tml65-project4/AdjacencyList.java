import java.io.*;
import java.util.*;

public class AdjacencyList
{
	Scanner fileScan = new Scanner(System.in);
	int numVertices;
	String [] elements;
	String line = "";
	int endPoint1;
	int endPoint2;
	String cableType = "";
	int bandwidth;
	int cableLength;
	LinkedList<GraphEdge> adjList[];
	public static FlowNetwork flow;
	
	
	@SuppressWarnings("unchecked")
	public AdjacencyList(String file)
	{
		File graphFile = new File(file);
		//System.out.println(graphFile);
		
		try 
		{
			fileScan = new Scanner(graphFile);	//try reading in file, if failed, quit program and have user restart
		}
		
		catch (FileNotFoundException e)
		{
			System.out.println("Unable to find file. Please Restart the Program.");
			System.exit(0);
		}
		
		if(fileScan.hasNextLine())	//grab num vertices (first line of file)
			numVertices = Integer.parseInt(fileScan.nextLine());
		
		adjList = new LinkedList[numVertices];
		for(int i = 0; i < numVertices; i++)	//make a linked list array of length vertices
			adjList[i] = new LinkedList<>();	//make a linked list in each position of array
		
		flow = new FlowNetwork(numVertices); //create new flowNetwork with number of vertices
		
		while(fileScan.hasNextLine())
		{
			line = fileScan.nextLine();
			elements = line.split(" ");
			endPoint1 = Integer.parseInt(elements[0]);	//store values in linked list in position
			endPoint2 = Integer.parseInt(elements[1]);
			cableType = elements[2];
			bandwidth = Integer.parseInt(elements[3]);
			cableLength = Integer.parseInt(elements[4]);
			
			//System.out.println("LINE CONTAINS: "+endPoint1+" "+endPoint2+" "+cableType+" "+bandwidth+" "+cableLength);
			
			GraphEdge forward = new GraphEdge(endPoint1, endPoint2, cableType, bandwidth, cableLength); //create forward and backward edges containing all properties
			GraphEdge backward = new GraphEdge(endPoint2, endPoint1, cableType, bandwidth, cableLength);
			
			//create forward edge
			adjList[endPoint1].add(forward); //put in array
			flow.addEdge(new FlowEdge(forward)); //add to flow network
			
			//create "back" edge (undirected so it has to go both ways)
			adjList[endPoint2].add(backward); //put in array
			flow.addEdge(new FlowEdge(backward));
		}
	}
	
	public Object[] dijkstra(AdjacencyList graph, int start, int end, String path, double time, int currBandwidth)
	{		
		if(start == end) //if we have reached the end vertex from the start vertex then we should be done
			return new Object[] {path, time, currBandwidth};
		
		LinkedList<GraphEdge> checkEdges = graph.adjList[start]; //make a linkedlist of graphedge from the vertex start position. So this grabs all graph edges attached to vertex start
		double minLength = -1.0; //Length of the minimum length path
		String minPath = "";

		for(GraphEdge edge : checkEdges) //go through the edges contained in checkedges
		{ 									
			int edgeDestination = edge.getEnd(); //get the end vertex to what the user is checking

			if(path.contains("" + edgeDestination)) continue;	 //already have seen this edge so skip to next one
			String newPath = path + " " + edgeDestination; 		//otherwise, lets create a new path of string
			double newTime = 0.0; 								//variable to store time
			
			newTime = time + edge.getTime(); //time it takes for traveling along edges
				
			if(!edge.getCableType().equals("copper") && !edge.getCableType().equals("optical"))
				return null; //Invalid wire material type, so we shouldn't travel that path
		

			int newBandwidth = currBandwidth; //If the edge that's being travelled has a bandwidth lower than the current path's bandwidth, then set the new minimum
			if(currBandwidth == -1.0 || edge.getBandwidth() < currBandwidth) newBandwidth = edge.getBandwidth(); //Set the new minimum path bandwidth
		
			Object[] pathData = dijkstra(graph, edgeDestination, end, newPath, newTime, newBandwidth);

			if(pathData == null) continue; //If there are no more edges to take a look at in the list

			String edgePath =  pathData[0].toString(); //get path data
			double pathLength = (double) pathData[1];	//get path time essentially
			int minBandwidth = (int) pathData[2];		//get path bandwidth value

			if(minLength == -1 || pathLength < minLength) //we have a new minimum length so update variables
			{ 
				minLength = pathLength;
				minPath = edgePath;
				currBandwidth = minBandwidth;
			}
			
			else if(pathLength == minLength && minBandwidth < currBandwidth) //lengths remain the same but we check for a better bandwidth
			{ 
				minLength = pathLength;
				minPath = edgePath;
				currBandwidth = minBandwidth;
			}
		}

		if(minLength > -1.0) //A path to reach the destination from the current vertex exists
			return new Object[] {minPath, minLength, currBandwidth};
		
		return null; //otherwise return null	
	}
	
	public void checkCopperConnectivity(AdjacencyList graph)
	{
		Queue<Integer> checkEdges = new Queue<Integer>(); //create a queue
		int i = 0;
		Boolean[] seen = new Boolean[numVertices]; //make a seen array so we know when we have visited a vertex
		
		for(i = 0; i < numVertices; i++)	//initalize all spots to be false in seen array
			seen[i] = false;
		
		checkEdges.enqueue(0); //add vertex 0 to queue
	
		while(!checkEdges.isEmpty())	//while the queue is not empty
		{
			int start = checkEdges.dequeue(); //pull the item first item from the queue and mark it as seen
			seen[start] = true;
			
			for(GraphEdge edge: graph.adjList[start])	//for all the edges attached to vertex check to see if it contains copper
			{
				if(edge.getCableType().equals("copper") && seen[edge.getEnd()] == false)	//if copper and yet to be seen
					checkEdges.enqueue(edge.getEnd()); //add next vertex to the queue
			}
		}
		
		for(i = 0; i < numVertices; i++) //outside while loop so go through the seen array to see if any spot false
		{
			if(seen[i] == false)		//if so, we dont have copper only connect graph
			{
				System.out.println("This graph is not copper-only connected");
				return;
			}				
		}
		
		System.out.println("This graph is copper-connected");		//otherwise, we are copper connected
	}

	
	public void maxDataFlowFF(int start, int end)
	{
		FordFulkerson theFlow = new FordFulkerson(flow, start, end); //pass in flow graph to Ford Fulkerson algorithm with start and end vertices
		
		for(FlowEdge edge: flow.edges())	//reset flow so errors don't occur after one cycle through
		{
			edge.resetFlow();
		}
		
		//System.out.println("The flow = "+theFlow.value());
		return;
	}
	
	public Boolean vertexFailures(AdjacencyList graph)
	{
		Queue<Integer> checkEdges = new Queue<Integer>(); //make queue to store ints (vertex start positions)
		Boolean[] seen = new Boolean[numVertices]; //make a seen array so we know when we have visited a vertex
		
		int start = 0;
		
		for(int i = 0; i < numVertices-1; i++)
		{
			for(int j = i+1; j < numVertices; j++)
			{
				for(int s = 0; s < numVertices; s++)	//initalize all spots to be false in seen array for each iteration
					seen[s] = false;
				
				int vertexA = i; //make vertex A = to i and B = to j which will reach all edge removals
				int vertexB = j;
				
				//System.out.println("NumVertices = "+numVertices);
				//System.out.println("vertexA = "+vertexA);
				//System.out.println("vertexB = "+vertexB);
				
				seen[vertexA] = true;
				seen[vertexB] = true; //mark them seen so it is essentially "removed"
				
				while(true) //make sure we don't start at a position that is either vertexA or vertexB
				{
					if(start != vertexA && start != vertexB)
						break;
					
					start++;
				}
				
				checkEdges.enqueue(start); //add start vertex to queue
				
				while(!checkEdges.isEmpty())	//while the queue is not empty
				{
					start = checkEdges.dequeue(); //pull the item first item from the queue and mark it as seen
					seen[start] = true;
					
					for(GraphEdge edge: graph.adjList[start])	//for all the edges attached to vertex check to see if it contains copper
					{
						if(seen[edge.getEnd()] == false)	//if copper and yet to be seen
							checkEdges.enqueue(edge.getEnd()); //add next vertex to the queue
					}
				}
				
				for(int k = 0; k < numVertices; k++) //outside while loop so go through the seen array to see if any spot false
				{
					if(seen[k] == false)		//if so, we dont have copper only connect graph
						return false;	
				}
				
				start = 0;
			}
		}
		
		return true;
	}	

	public void printGraph(AdjacencyList graph)  //method to help me debug in completion of this program - just prints out the graph edges and values
	{        
		System.out.println("At index 0 we have: "+graph.adjList[0].get(0).getStart());
		for(GraphEdge v: graph.adjList[0])
			System.out.println("has adjacency connection to "+v.getEnd());
		
        for(int v = 0; v < graph.numVertices; v++) 
        { 
            System.out.println("Adjacency list of vertex "+ v); 
            System.out.print("head"); 
            for(GraphEdge pCrawl: graph.adjList[v]){ 
                System.out.print(" -> "+pCrawl); 
            } 
            System.out.println("\n"); 
	        } 
	}
	
}