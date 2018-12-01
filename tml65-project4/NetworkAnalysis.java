import java.util.*;

public class NetworkAnalysis
{
	public static double maxFlow; //have static variable to obtain max data flow for option 3
	
	public static void main(String[] args)
	{
		if(args[0] == null)
		{
			System.out.println("We cannot use the functionality of this program without a specified text file regarding graphs. Please try again");
			System.exit(0);
		}

		else
		{
			AdjacencyList graph = new AdjacencyList(args[0]);
			Scanner scanner = new Scanner(System.in);
			String optSelect;
			//graph.printGraph(graph);
			//System.out.println(graph.printGraph(graph))
			
			while(true)
			{
				System.out.print("\nMAIN MENU\n\n\t1) Lowest Latency Path (between two points)\n\t2) Copper Connectivity\n\t3) Max Data Transfer From One Vertex to Another"
						+ "\n\t4) Graph Disconnection if 2+ Vertices Fail\n\t5) Quit the Program\n\nEnter Your Selection Here (numbers only): ");
				
				optSelect = scanner.nextLine(); //obtain option selection from menu
				
				if(optSelect.equals("1"))
				{
					System.out.println("Option 1 - Finding Lowest Latency Path\n");
					System.out.print("\nPlease enter a vertex (integer): ");	//ask for two vertices
					int vertexStart = Integer.parseInt(scanner.nextLine());
					
					while(vertexStart < 0 || vertexStart > graph.numVertices-1)	//error check to make sure it is in list
					{
						System.out.print("\nInvalid Vertex.\nPlease enter a vertex (integer): ");
						vertexStart = Integer.parseInt(scanner.nextLine());
					}
					
					System.out.print("\nPlease enter a second vertex (integer): ");
					int vertexEnd = Integer.parseInt(scanner.nextLine());
				
					while(vertexEnd < 0  || vertexEnd > graph.numVertices-1 || vertexEnd == vertexStart) //need the extra case because we cant find anything if we start and end at same vertex
					{
						System.out.print("\nInvalid Vertex.\nPlease enter a second vertex (integer): ");
						vertexEnd = Integer.parseInt(scanner.nextLine());
					}
										
					Object [] shortestPathData = graph.dijkstra(graph, vertexStart, vertexEnd, ""+vertexStart, 0, -1); //pass in adjacency list, start and end vertexes, a string, two ints in determing time and bandwidth
					
					if(shortestPathData == null) //if nothing
						return;
					
					String pathData = (String) shortestPathData[0]; //pathData contains the shortest path
					//System.out.println(pathData);
					String [] minPath = pathData.split(" ");
					int band = Integer.MAX_VALUE;
					
					for(int a = 0; a < minPath.length-1; a++)	//obtain the correct bandwidth along the minPath we have obtained
					{
						for(GraphEdge e: graph.adjList[Integer.parseInt(minPath[a])])
						{
							if(minPath[a+1].equals(e.getEnd()+"") && band > e.getBandwidth())
								band = e.getBandwidth(); //getting the min bandwidth along path
						}
					}
					
					String shortestPath = "";

					for(int i = 0; i < pathData.length(); i++) //for easier reading during execution, print out arrows to show vertex goes to another vertex when 
					{
						if(pathData.charAt(i) == ' ') continue; //since I have a space between vertices obtained (to help prevent error) we need to skip over the space
						if(i < pathData.length()-1)				//printing out min path
							shortestPath += pathData.charAt(i) + " --> ";

						else
							shortestPath += pathData.charAt(i);
					}

					pathData = shortestPath;
					double travelTime = (double) shortestPathData[1]; //Time to send a set of data from the start vertex to the destination vertex in nanoseconds

					System.out.println("\nShortest Path: "+ pathData+"\nBandwidth: "+band+" Mbps"); //print out values
					System.out.printf("Time Of Travel: %.2f nanoseconds\n",travelTime*Math.pow(10, 9));					
				}
				
				else if(optSelect.equals("2"))
				{
					System.out.println("Option 2 - Checking Copper Connectivity\n");
					
					graph.checkCopperConnectivity(graph);				
				}
				
				else if(optSelect.equals("3"))
				{
					System.out.println("Option 3 - Checking For Max Data Flow From One Vertex To Another\n");
					
					System.out.print("\nPlease enter a vertex (integer): ");	//ask for two vertices
					int vertexStart = Integer.parseInt(scanner.nextLine());
					
					while(vertexStart < 0 || vertexStart > graph.numVertices-1)	//error check to make sure it is in list
					{
						System.out.print("\nInvalid Vertex.\nPlease enter a vertex (integer): ");
						vertexStart = Integer.parseInt(scanner.nextLine());
					}
					
					System.out.print("Please enter a second vertex (integer): ");
					int vertexEnd = Integer.parseInt(scanner.nextLine());	//obtain vertex to end at
				
					while(vertexEnd < 0  || vertexEnd > graph.numVertices-1 || vertexEnd == vertexStart) //need the extra case because we cant find anything if we start and end at same vertex
					{
						System.out.print("\nInvalid Vertex.\nPlease enter a second vertex (integer): ");
						vertexEnd = Integer.parseInt(scanner.nextLine());
					}
					
					graph.maxDataFlowFF(vertexStart, vertexEnd);	//call maxDataFlowFF - which calls ford fulkerson - to obtain max data flow among the vertices
					
					System.out.println("\nThe Maximum Data Flow From "+vertexStart+" To "+vertexEnd+" Is: "+maxFlow+" Mbps"); //print out the maxflow between the vertices
					
					maxFlow = 0; //reset maxFlow so we dont continously add it on top of one another
				}
				
				else if(optSelect.equals("4"))
				{
					System.out.println("Option 4 - Checking Failure Of Vertices\n");
					
					Boolean doesNotFail = graph.vertexFailures(graph); //if returns true we print first statement, otherwise print second statement
					
					if(doesNotFail)
						System.out.println("The Graph Will Remain Connected When Any Two Vertices Fail!!");
					
					else
						System.out.println("The Graph Will Become Disconnected If Any Pair Of Vertices Decides To Fail");
				}
				
				else if(optSelect.equals("5"))
				{
					System.out.println("Option 5 - Program Terminated\nThanks For Using!");
					scanner.close(); //close scanner and terminate the program
					System.exit(0);
				}
				
			//	else if(optSelect.equals("6"))
			//	{
			//		graph.printGraph(graph);
			//	}
				
				else
					System.out.print("Sorry that option does not exist. Please try again\n ");
			}
		}
	}	
}