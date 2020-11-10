// --== CS400 File Header Information ==--
// Name: Owen Quinlan
// Email: oquinlan@wisc.edu
// Team: GG
// Role: Backend Developer
// TA: Dan Kiel
// Lecturer: Florian Heimerl
// Notes to Grader: N/A

import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.List;

/**
 * Simulates a computer network with one-way connections. Each computer is named with a unique integer. 
 * 
 * @author Owen
 *
 */
public class NetworkBackend {
	public static CS400Graph<Integer> graph; //the graph where the network information is stored
	
	/**
	 * Should be called by the Frontend before presenting the user with any options. Creates a new network and loads
	 * in the default data by calling NetworkDataWrangler.
	 */
	public static void create() {
		graph = new CS400Graph<Integer>(); //creates a new network graph
		
		try {
			NetworkDataWrangler.loadInitialNetwork(graph); //tries to load the CSV file via NetworkDataWrangler
		} catch (FileNotFoundException fnfe) {
			System.out.println("The default network list \"" + NetworkDataWrangler.FILE_PATH + "\" could not be found."); //printed if file can't be found
		}
	}
	
	/**
	 * Adds a new computer to the network. Allows a connection between the new computer and a pre-existing one to be established.
	 * Returns true if the addition was successful, returns false if the addition fails (most likely because the computer already exists)
	 * 
	 * @param name - name of the new computer (cannot already exist in the network)
	 * @param connectedTo - optional name of a pre-existing computer to connect the new addition to. (-1 if they decline)
	 * @param weightTo - ping (ms) of an optional connection to the new computer from connectedTo
	 * @param weightFrom - ping (ms) of an optional connection to connectedTo from the new computer
	 * @return - the success of the addition if an exception hasn't occurred (usually false if the computer already exists)
	 * @throws NullPointerException - thrown if any input is null
	 * @throws IllegalArgumentException - thrown if connectedTo doesn't exist
	 */
	public static boolean add(int name, int connectedTo, int weightTo, int weightFrom) throws NullPointerException, IllegalArgumentException {
		boolean success = graph.insertVertex(name); //adds the new computer and creates a boolean that tells if this occurred successfully.
		if ((connectedTo > -1) && success) { //if the computer was added correctly and the user hasn't declined to make a new connection, proceed
			if (weightTo > -1) { //if the user hasn't declined to make a connection TO the new computer...
				success = graph.insertEdge(connectedTo, name, weightTo); //...try to create this new connection (should invariably be true)
			}
			if (weightFrom > -1) { //if the user hasn't declined to make a connection FROM the new computer...
				success = graph.insertEdge(name, connectedTo, weightFrom); //...try to create this new connection (should invariably be true)
			}
		}
		return success; //returns false if there were any non-fatal failures, returns true otherwise
	}
	
	/**
	 * Connects two previously unconnected computers. Returns true if successful or false if failed (such as if the connection already exists)
	 * 
	 * @param leavingFrom - the computer the connection is leaving from
	 * @param connectedTo - the computer the connection is going to
	 * @param weight - the ping of this connection in ms
	 * @return the success of the connection if an exception hasn't occurred (usually false if the connection already exists)
	 * @throws NullPointerException - thrown if the source or target is null
	 * @throws IllegalArgumentException - thrown if the specified weight is negative or if one the specified vertices doesn't exist
	 */
	public static boolean connect(int leavingFrom, int connectedTo, int weight) throws NullPointerException, IllegalArgumentException {
		boolean success = graph.insertEdge(leavingFrom, connectedTo, weight);
		return success;
	}
	
	/**
	 * Returns formatted info about the specified computer. (name, connections, and their ping)
	 * 
	 * @param name - name of the computer being queried
	 * @return - formatted string containing information about the specified computer
	 * @throws NoSuchElementException - thrown if the computer doesn't exist
	 */
	public static String listNodeInfo(int name) throws NoSuchElementException {
		if (graph.containsVertex(name)) { //checks if the vertex exists
			return listNodeInfo(graph.vertices.get(name)); //attempts to get the computer and pass it to overloaded listNodeInfo()
		} else {
			throw new NoSuchElementException("The specified node could not be found in the graph"); //thrown if vertex doesn't exist
		}
	}
	
	/**
	 * Returns formatted info about a node/computer passed directly. (name, connections, and their ping)
	 * 
	 * @param node - the node in question
	 * @return - formatted string containing information about the specified computer
	 */
	protected static String listNodeInfo(CS400Graph<Integer>.Vertex node) {
			String listAll = ""; //begins with empty string
			
			listAll = listAll + ("Computer Name: " + node.data + "\n\tOutgoing Connections:"); //adds information about the computer name and prefaces the connections to be listed
			String before = listAll; //used for comparison below
			
			for (CS400Graph<Integer>.Edge e : node.edgesLeaving) { //for all outgoing connections, list them and their ping
				listAll = listAll + (" (Name: " + e.target.data + ", Ping: " + e.weight + " ms)");
			}
			
			if (listAll.equals(before)) { //if the string hasn't been changed (no outgoing connections found), specify that none were found
				listAll = listAll + " NONE";
			}
			
			return listAll; //returns the string
	}
	
	/**
	 * Lists all information about every computer in the network using listNodeInfo()
	 * 
	 * @return - information about every computer in the network
	 */
	public static String listAllInfo() {
		String listAll = ""; //begins with empty string
		for (CS400Graph<Integer>.Vertex v : graph.vertices.values()) { //repeats listNodeInfo() for every computer in the network and breaks with two newlines
			listAll = listAll + listNodeInfo(v);
			listAll = listAll + ("\n\n");
		}
		return listAll; //returns the string
	}
	
	/**
	 * Returns the ping between two computers as an int. (in ms)
	 * 
	 * @param pathFrom - source computer
	 * @param pathTo - target computer
	 * @return - ping in ms
	 * @throws NoSuchElementException - if a connection between the two computers cannot be found
	 */
	public static int pingFinder(int pathFrom, int pathTo) throws NoSuchElementException{
		return graph.getPathCost(pathFrom, pathTo);
	}
	
	/**
	 * Returns a string detailing the computers traversed to get from a source computer to a target computer, as well as the time taken.
	 * 
	 * @param pathFrom - source computer
	 * @param pathTo - target computer
	 * @return - computers traversed 
	 * @throws NoSuchElementException - if a connection between the two computers cannot be found
	 */
	public static String pathFinder(int pathFrom, int pathTo) throws NoSuchElementException {
		String pathList = ""; //begins with empty string
		List<Integer> traversed; //initializes a list for the traversed computers
		try {
			traversed = graph.shortestPath(pathFrom, pathTo); //gets the shortest path between two computers
			for (int i = 0; i < traversed.size(); ++i) { //lists every traversed computer and the time it took to reach it
				pathList = pathList + ("Computer " + i + ": " + traversed.get(i) + ", Reached in: " + graph.getPathCost(pathFrom, traversed.get(i)) + " ms \n");
			}
			pathList = pathList + ("Finished! Done in " + graph.getPathCost(pathFrom, pathTo) + " ms"); //declares the final time taken to reach the computer
		} catch (NoSuchElementException nse){
			throw new NoSuchElementException("No path could be found between the starting computer and target computer.");
		}
		return pathList; //returns the string
	}
	
	/**
	 * Attempts to remove a computer and it's connections from the network.
	 * 
	 * @param name - name of computer to be removed
	 * @return - returns the success of this removal (true if successful, false if it doesn't exist)
	 * @throws NullPointerException - thrown if passed a null argument
	 */
	public static boolean remove(int name) throws NullPointerException {
		return graph.removeVertex(name);
	}
	
	/**
	 * Attempts to remove a pre-existing connection between two computers.
	 * 
	 * @param leavingFrom - the source computer
	 * @param connectedTo - the target computer
	 * @return - returns the success of this removal (true if successful, false if it doesn't exist)
	 * @throws NullPointerException - thrown if passed a null argument
	 * @throws IllegalArgumentException - thrown if the specified vertices don't exist
	 */
	public static boolean disconnect(int leavingFrom, int connectedTo) throws NullPointerException, IllegalArgumentException {
		return graph.removeEdge(leavingFrom, connectedTo); 
	}
	
	/**
	 * Removes all computers from the network.
	 */
	public static void clear() {
		graph = new CS400Graph<Integer>(); //makes new empty network! :)
	}

}
