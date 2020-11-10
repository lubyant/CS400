// --== CS400 File Header Information ==--
// Name: Josh Neerdaels
// Email: jrneerdaels@wisc.edu
// Team: GG
// Role: Data Wrangler 1
// TA: Daniel Kiel
// Lecturer: Florian Heimerl
// Notes to Grader: N/A

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class loads the initial network into the graph per the file with nodes and edges.
 * 
 * @author Josh Neerdaels
 *
 */
public class NetworkDataWrangler {

	public final static String FILE_PATH = "ComputerConnections.csv"; // String containing the name of the file holding edges
	public final static int EDGE_DATA_COUNT = 3; // number of data items needed for edge
	public final static int SOURCE_NODE_INDEX = 0; // index of source node in array
	public final static int DESTINATION_NODE_INDEX = 1; // index of destination node in array
	public final static int EDGE_WEIGHT_INDEX = 2; // index of edge weight node in array

	/**
	 * This method loads the edges that are in the string file into the provided graph
	 * 
	 * @param graph - the provided graph data structure to store nodes and edges
	 * @throws FileNotFoundException if file given cannot found in directory
	 */
	public static void loadInitialNetwork(CS400Graph<Integer> graph) throws FileNotFoundException {

		// create scanner object of file
		File inputFile = new File(FILE_PATH);
		Scanner inputScanner = new Scanner(inputFile);

		// read lines of file
		while (inputScanner.hasNextLine()) {
			String nextLine = inputScanner.nextLine();
			
			// split string into source node, target node, edge weight
			String[] splitString = nextLine.split("\t");
			int[] edge = new int[EDGE_DATA_COUNT];

			// get into correct format
			for (int i = 0; i < splitString.length; ++i) {
				edge[i] = Integer.parseInt(splitString[i]);
			}

			// add nodes to graph
			graph.insertVertex(edge[SOURCE_NODE_INDEX]);
			graph.insertVertex(edge[DESTINATION_NODE_INDEX]);
			
			// add edge to graph
			graph.insertEdge(edge[SOURCE_NODE_INDEX], edge[DESTINATION_NODE_INDEX], edge[EDGE_WEIGHT_INDEX]);

		}

		// close scanner
		inputScanner.close();

	}
	
}
