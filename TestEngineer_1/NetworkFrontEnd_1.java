// --== CS400 File Header Information ==--
// Name: Jorge Tenorio
// Email: jltenorio@wisc.edu
// Team: GG
// Role: Front End Developer 1
// TA: Dan Kiel
// Lecturer: Florian Heimrel
// Notes to Grader: <optional extra notes>

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * The front end of the UW-Madison Computer Network Graph.
 * 
 * Glossary: computer = vertex, connection = edge, network = graph, latency = edge
 * weight
 * 
 * @author Jorge Tenorio
 *
 */
public class NetworkFrontEnd_1 {

	/**
	 * Launches the program and displays the menu of options for the user to
	 * interact with the network.
	 * 
	 * @param args
	 * @throws FileNotFoundException if file given cannot be found in directory.
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws FileNotFoundException, InterruptedException {

		// Loads the network to interact with.
		NetworkBackend.create();

		// Scanner
		Scanner scnr = new Scanner(System.in);

		// Display main menu
		printLine();
		System.out.println("   _______________                        |*\\_/*|________\r\n"
				+ "  |  ___________  |     .-.     .-.      ||_/-\\_|______  |\r\n"
				+ "  | |           | |    .****. .****.     | |           | |\r\n"
				+ "  | |   0   0   | |    .*****.*****.     | |   0   0   | |\r\n"
				+ "  | |     -     | |     .*********.      | |     -     | |\r\n"
				+ "  | |   \\___/   | |      .*******.       | |   \\___/   | |\r\n"
				+ "  | |___     ___| |       .*****.        | |___________| |\r\n"
				+ "  |_____|\\_/|_____|        .***.         |_______________|\r\n"
				+ "    _|__|/ \\|_|_.............*.............._|________|_\r\n"
				+ "   / ********** \\                          / ********** \\\r\n"
				+ " /  ************  \\                      /  ************  \\\r\n"
				+ "--------------------                    --------------------");
		printLine();
		System.out.println("Welcome to the UW Madison Computer Network Manager!");
		System.out.println("A program to interact with the UW Madison Computer Network!");

		// Prompts the user for an input.
		String option = commandList(scnr);

		// Operates the network until the user exits.
		do {
			if (option.equals("A")) {
				ping(scnr);
			} else if (option.equals("B")) {
				addComputer(scnr);
			} else if (option.equals("C")) {
				removeComputer(scnr);
			} else if (option.equals("D")) {
				addConnection(scnr);
			} else if (option.equals("E")) {
				removeConnection(scnr);
			} else if (option.equals("F")) {
				listFullNetwork(scnr);
			} else if (option.equals("G")) {
				clear(scnr);
			} else if (option.equals("H")) {
				break;
			} else {
				System.out.println("Invalid option entered, enter from the list.");
			}

			option = commandList(scnr);
		} while (!option.equals("H"));

		scnr.close();

		// Exit Message
		printEndMessage();
	}

	/**
	 * Prints out the total weight and path from a source node to a destination
	 * node.
	 * 
	 * @param scnr Scanner used to read user input.
	 */
	private static void ping(Scanner scnr) {
		Integer intStart = null; // The source computer name.
		Integer intEnd = null; // The destination computer name.

		// Prompt the user for a source computer. Prints a success message if found.
		printLine();
		System.out.println("Printing computer connection paths.\n");
		System.out.println("What computer would you like to source from? Enter a number from the list.: ");
		listComputerNames(scnr);
		System.out.println("Or enter H to cancel");

		intStart = findOnNetwork(scnr);

		// Exits the program if desired.
		if (intStart != null && intStart.equals(Integer.MIN_VALUE)) {
			System.out.println("Cancelling ping operation...");
			return;
		}

		// Success message.
		System.out.println("Computer " + intStart + " found on network.");
		printLine();

		// Prompts the user for a destination computer. Prints a success message if
		// found.
		System.out.println("Using computer " + intStart + ", select a computer to path to from the list");
		listComputerNames(scnr);
		System.out.println("Or enter H to exit.");

		intEnd = findOnNetwork(scnr);

		// Exits the program if desired.
		if (intEnd != null && intEnd.equals(Integer.MIN_VALUE)) {
			printLine();
			System.out.println("Cancelling ping operation...");
			return;
		}

		// Success message.
		System.out.println("Computer " + intEnd + " found on network.");

		// Attempt to print the connection path and connection latency.
		// If NoSuchElementException is thrown, then no path exists. Exit the operation.
		try {
			NetworkBackend.pathFinder(intStart, intEnd);
			printLine();
			System.out.println("Connection path from " + intStart + " to " + intEnd + ": \n"
					+ NetworkBackend.pathFinder(intStart, intEnd));
		} catch (NoSuchElementException e) {
			printLine();
			System.out
					.println("No connection from computer " + intStart + " to " + intEnd + ". Returning to main menu.");
			return;
		}
	}

	/**
	 * Prompts the user in adding a new computer along with its connections.
	 * 
	 * @param scnr Scanner used to read user input.
	 */
	private static void addComputer(Scanner scnr) {
		String input = ""; // Stores the user's current input
		Integer newName = null; // The new computer name.
		Integer newEnd = null; // The destination computer of any new connection.
		Integer newLatency = null; // The latency of the new connection.
		boolean validInput = false; // Used to check if an input is valid.
		boolean connectionToAdd = false; // Used to check if more edges are desired.
		scnr = new Scanner(System.in);

		// Prompt the user for adding a new computer.
		printLine();
		System.out.println(
				"Adding a new computer.\n\nEnter the new computer name as a positive integer. New name cannot already exist on the network: ");
		listComputerNames(scnr);
		System.out.println("Or enter H to cancel");

		// Prompts the user for the name of a new computer that does not already exist
		// on the network.
		// Will continue to prompt the user until a valid unique name is entered.
		while (!validInput) {
			input = scnr.nextLine().trim();

			// Arranges for the operation to exit.
			if (input != null && (input.toUpperCase().equals("H"))) {
				validInput = true;
				continue;
			}

			newName = verifyInput(input);

			if (newName == -1) {
				printLine();
				System.out.println(
						"Enter the new computer name as a positive integer. New name cannot already exist on the network:");
				listComputerNames(scnr);
				System.out.println("Or enter H to cancel.");
				continue;
			}

			if (newName != null && !(NetworkBackend.graph.containsVertex(newName))) {
				printLine();
				System.out.println("Valid Input. Adding new computer " + newName + " to network.");
				validInput = true;
			} else {
				printLine();
				System.out.println(
						"Invalid input, a computer with the name " + newName + " already exists on the network.\n");
				System.out.println(
						"Enter the new computer name as a positive integer. New name cannot already exist on the network:");
				listComputerNames(scnr);
				System.out.println("Or enter H to cancel.");
			}

		}

		// Exits the operation.
		if (input != null && input.equals("H")) {
			printLine();
			System.out.println("Cancelling add computer operation...");
			return;
		}

		// Add the computer to the network.
		printLine();
		NetworkBackend.add(newName, -1, 0, 0);
		// Prompt the user for connections to add from the newly added computer.
		System.out.println("Add any connections from new computer " + newName + "? (Y/N):");
		if (!scnr.next().toUpperCase().equals("Y")) {
			printLine();
			System.out
					.println("No new connections will be aded to new computer " + newName + ". Exiting add operation.");
			return;
		}

		// Reset scanner.
		scnr.nextLine();

		// Continuously prompts the user for new connections until the user decides to
		// exit the operation.
		while (!connectionToAdd) {
			printLine();
			System.out.println("From new computer " + newName + ", which existing computer to connect to?\n");
			System.out.println("Enter a computer from the network.:");
			listComputerNames(scnr);
			System.out.println("Or enter H once all desired edges have been added.");
			input = scnr.nextLine().trim();

			// Arranging to exit the operation.
			if (input != null && (input.toUpperCase().equals("H"))) {
				connectionToAdd = true;
				continue;
			}

			newEnd = verifyInput(input);

			// Verify a valid user input.
			if (newEnd < 0) {
				printLine();
				System.out.println("Invalid input.");
				continue;
			}

			if (input != null && !(NetworkBackend.graph.containsVertex(newEnd))) {
				printLine();
				System.out.println("Computer NOT found on network.");
				continue;
			}

			// Prompt the user for the latency of the new connection. Verify a valid input.
			printLine();
			System.out.println("Computer " + newEnd + " found.\n\nEnter the latency as a positive integer.:");
			validInput = false;
			while (!validInput) {
				input = scnr.nextLine().trim();

				if (verifyInput(input) < 0) {
					System.out.println("Invalid input. Enter latency as a positive integer:");
				} else {
					validInput = true;
				}
				newLatency = verifyInput(input);
			}

			// Confirmation message that the user has added a connection from the new
			// computer to an already existing one.
			printLine();
			System.out.println("New connection from computer " + newName + " to computer " + newEnd
					+ " with a latency of " + newLatency + ". ");
			NetworkBackend.connect(newName, newEnd, newLatency);
		}

		// Exit the operation.
		printLine();
		System.out.println("Exiting add operation.");
		return;
	}

	/**
	 * Removes a node from the network along with its edges.
	 * 
	 * @param scnr Scanner used to read user input.
	 */
	private static void removeComputer(Scanner scnr) {
		Integer removeComputer = null; // Computer to remove.

		// Prompts the user for the name of the desired computer to remove.
		printLine();
		System.out.println("Removing a computer.\n");
		System.out.println("Enter a computer from the network to remove.: ");
		listComputerNames(scnr);
		System.out.println("Or enter H to cancel");

		removeComputer = findOnNetwork(scnr);

		// Exits the operation if desired.
		if (removeComputer != null && removeComputer.equals(Integer.MIN_VALUE)) {
			printLine();
			System.out.println("Cancelling remove computer operation...");
			return;
		}

		// Removes the desired computer. Prints a message of which computer was removed.
		System.out.println("Removing computer " + removeComputer + " and its connections:");
		System.out.println(NetworkBackend.listNodeInfo(removeComputer));
		NetworkBackend.remove(removeComputer);
		return;
	}

	/**
	 * Prompts the user in adding a new edge.
	 * 
	 * @param scnr Scanner used to read user input.
	 */
	private static void addConnection(Scanner scnr) {
		Integer source = null; // The source computer of the new connection.
		Integer destination = null; // The destination computer of the new connection.
		Integer latency = null; // The latency of the new connection.
		String input; // User's current input
		boolean validWeight = false; // Used to check if the entered latency is valid.
		scnr = new Scanner(System.in);

		// Prompts the user for a source computer. Prints a message when a valid source
		// computer is found.
		printLine();
		System.out.println(
				"What computer are you looking to source the new connection from? Enter a number from the list.: ");
		listComputerNames(scnr);
		System.out.println("Or enter H to cancel");

		source = findOnNetwork(scnr);

		// Arranges for the operation to exit before completion.
		if (source != null && source.equals(Integer.MIN_VALUE)) {
			System.out.println("Cancelling add connection operation...");
			return;
		}

		// Valid source computer message.
		System.out.println("Computer found on network.");
		printLine();

		// Prompts the user for a destination computer. Prints a message when a valid
		// source computer is found.
		System.out
				.println("What computer will be the desination of this new connection? Enter a number from the list.:");
		listComputerNames(scnr);
		System.out.println("Or enter H to cancel");

		destination = findOnNetwork(scnr);

		// Arranges for the operation to exit before completion.
		if (destination != null && destination.equals(Integer.MIN_VALUE)) {
			System.out.println("Cancelling add connection operation...");
			return;
		}

		// Valid destination computer message.
		System.out.println("Computer found on network.");
		printLine();
		System.out.println("Now, enter the latency (as a positive integer) of the new connection.");

		// Repeatedly prompts the user for a valid latency.
		while (!validWeight) {

			input = scnr.nextLine().trim();

			latency = verifyInput(input);

			if (latency == -1 || latency < 1) {
				System.out.println("Invalid Input, enter connection latency as a positive integer.");
				continue;
			}

			validWeight = true;
		}

		System.out.println("Latency of new connection entered as: " + latency + " ms");
		printLine();

		// Confirms a new connection.
		System.out.println("Confirm a new connection from computer " + source + " to computer " + destination
				+ " with a latency of " + latency + "? (Y/N)");

		// Adds the connection to the network.
		if (scnr.next().trim().toUpperCase().equals("Y")) {
			if (NetworkBackend.connect(source, destination, latency)) {
				printLine();
				System.out.println("New connection successfully added. Returning to main menu.");
			} else {
				System.out.println("Connection from computer " + source + " to " + destination
						+ " already exists. Exiting add connection operation.");
			}
		} else {
			// Exits the program if the new connection isn't desired.
			System.out.println("Add connection cancelled, returning to main menu.");
		}
	}

	/**
	 * Removes an edge from the network.
	 * 
	 * @param scnr Scanner used to read the user input.
	 */
	private static void removeConnection(Scanner scnr) {
		Integer source = null; // Source computer of the connection to remove.
		Integer destination = null; // Destination computer of the connection to remove.
		scnr = new Scanner(System.in);

		// Prompts the user for the source computer of the connection to remove.
		printLine();
		System.out.println(
				"What computer is the source of the connection to be removed? Enter a number from the list.: ");
		listComputerNames(scnr);
		System.out.println("Or enter H to cancel");

		source = findOnNetwork(scnr);

		// Arranges for the operation to exit before completion if desired.
		if (source != null && source.equals(Integer.MIN_VALUE)) {
			System.out.println("Cancelling remove connection operation...");
			return;
		}

		System.out.println("Computer found on network.");
		printLine();

		// Prompts the user for the destination computer of the connection to remove.
		System.out.println(
				"What computer is the destination of the connection to be removed? Enter a number from the list.:");
		listComputerNames(scnr);
		System.out.println("Or enter H to cancel");

		destination = findOnNetwork(scnr);

		// Exits the operation before completed if desired.
		if (destination != null && destination.equals(Integer.MIN_VALUE)) {
			System.out.println("Cancelling remove connection operation...");
			return;
		}

		System.out.println("Computer found on network.");

		// Checks that a connection from the source to destination computer exists.
		// If not, tells the user that no connection exists and exists the operation.
		printLine();
		if (!(NetworkBackend.graph.containsEdge(source, destination))) {
			System.out.println("No connection from computers " + source + " to " + destination
					+ " exists.\nCancelling remove connection operation...");
			return;
		} else {
			System.out.println("Connection found from computer " + source + " to computer " + destination + ".");
			printLine();
		}

		// Confirms with the user to remove the desired connection.
		// If so, removes the connection, otherwise, exits the operation.
		System.out.println("Confirm remove connection from computer " + source + " to computer " + destination + "? (Y/N)");
		if (scnr.next().trim().toUpperCase().equals("Y")) {
			NetworkBackend.disconnect(source, destination);
			printLine();
			System.out.println("Connection removed");
		} else {
			System.out.println("Remove connection cancelled, returning to main menu.");
		}
	}

	/**
	 * Displays the entire network and total cost.
	 * 
	 * @param scnr Scanner used to keep track of position in program.
	 */
	private static void listFullNetwork(Scanner scnr) {
		String fullNetwork = NetworkBackend.listAllInfo();
		if (fullNetwork.equals("")) {
			printLine();
			System.out.println("Network Empty.");
		} else {
			printLine();
			System.out.println(NetworkBackend.listAllInfo());
		}
	}

	/**
	 * Displays the only the computer names.
	 * 
	 * @param scnr Scanner used to keep track of position in program.
	 */
	private static void listComputerNames(Scanner scnr) {
		String listComputers = "Computer names on network: ";
		Enumeration<Integer> computers = NetworkBackend.graph.vertices.keys();
		if (!computers.hasMoreElements()) {
			System.out.println("No computers currently loaded onto network.");
			return;
		}
		while (computers.hasMoreElements()) {
			listComputers += computers.nextElement() + ", ";
		}
		System.out.println(listComputers.substring(0, listComputers.length() - 2));
	}

	/**
	 * Clears the network of existing nodes and edges.
	 */
	private static void clear(Scanner scnr) {

		// Confirms with the user to clear the network.
		System.out.println("Are you sure you want to clear the network? (Y/N)");
		if (scnr.next().trim().toUpperCase().equals("Y")) {
			// If confirmed, clear the network.
			printLine();
			System.out.println("Network cleared.");
			NetworkBackend.clear();
		} else {
			printLine();
			System.out.println("Exiting clear operation...");
		}

	}

	/**
	 * Displays the options available to the user and returns their input.
	 * 
	 * @param scnr Reads the user's input.
	 * @return A formatted version of the user's input.
	 */
	private static String commandList(Scanner scnr) {

		// Print out menu options
		printLine();
		System.out.println("MAIN MENU");
		System.out.println("Select one of the following options to interact with or exit the network:");
		System.out.println(" A: PRINT a path and total latency from one node to another.");
		System.out.println(" B: ADD a computer to the network along with its connections.");
		System.out.println(" C: REMOVE a computer from the network along with its connections.");
		System.out.println(" D: ADD a connection between two computers to the network.");
		System.out.println(" E: REMOVE a connection between two computers from the network.");
		System.out.println(" F: PRINT the entire network.");
		System.out.println(" G: CLEAR the network.");
		System.out.println(" H: EXIT the program");
		System.out.println("What would you like to do?");

		// Read, edit, and return the user input.
		return scnr.next().trim().toUpperCase();
	}

	/**
	 * This method verifies that the user's input is only an integer. If so, returns
	 * the integer value, otherwise returns -1 to indicate that the user's input is
	 * NOT an integer.
	 * 
	 * @param input The user's input.
	 * @return The value of the user's input as an integer, -1 otherwise.
	 */
	private static int verifyInput(String input) {
		int intOfInput = -1; // The integer value of the input. Assigned with -1 as a failure indicator.

		// Checks if the user's input is a single integer value. If not, returns -1.
		try {
			intOfInput = Integer.parseInt(input);
			if (intOfInput < 0) {
				return -1;
			}
		} catch (NumberFormatException e) {
			return intOfInput;
		}

		return intOfInput;
	}

	/**
	 * Determines if the user's input exists as a computer on the network.
	 * 
	 * @param scnr Scanner used to read user input.
	 * @return The integer name of the computer as requested by the user.
	 */
	private static int findOnNetwork(Scanner scnr) {
		boolean onNetwork = false; // Used to check that the user's input exists as a computer on the network.
		String input = ""; // The user's current input.
		Integer returnComputer = -1; // The integer value of the user's input. Initialized with -1 as a failure
										// indicator.
		scnr = new Scanner(System.in);

		// Repeatedly prompts the user for the name of a computer on the network. Also
		// used to exit the caller operation before completion.
		while (!onNetwork) {
			input = scnr.nextLine().trim();

			// Arranges for the caller operation to exit before completion.
			if (input != null && (input.trim().toUpperCase().equals("H"))) {
				returnComputer = Integer.MIN_VALUE;
				onNetwork = true;
				continue;
			}

			returnComputer = verifyInput(input);

			// Exits the caller operation to exit before completion.
			if (returnComputer == -1) {
				System.out.println("Invalid input. Select a computer name from the list.:");
				listComputerNames(scnr);
				System.out.println("Or enter H to cancel.");
				continue;
			}

			// Checks that the name of the user's input exists on the network.
			// Prompts the user for another input if not.
			if (returnComputer != null && NetworkBackend.graph.containsVertex(returnComputer)) {
				printLine();
				onNetwork = true;
			} else {
				printLine();
				System.out.println("Computer " + returnComputer + " NOT found on network.");
				printLine();
				System.out.println("Select a computer number from the list.:");
				listComputerNames(scnr);
				System.out.println("Or enter H to cancel.");
			}

		}

		return returnComputer;
	}

	/**
	 * Prints out a dashed line to assist in formatting.
	 */
	private static void printLine() {
		System.out.println("----------------------------------------------------------------------");
	}

	/**
	 * Prints an animated exit message of the team members and their project role.
	 */
	private static void printEndMessage() {
		ArrayList<String> names = new ArrayList<String>();
		String currName = "";
		names.add("Josh Neerdaels ......................................... Data Wrangler");
		names.add("Owen Quinlan ...................................... Back End Developer");
		names.add("Jorge Tenorio .................................. Front End Developer 1");
		names.add("Kendra Davis ................................... Front End Developer 2");
		names.add("Boyuan Lu ............................................ Test Engineer 1");
		names.add("Caleb Brown .......................................... Test Engineer 2");

		printLine();
		System.out.println("Thank you for using  UW-Madison Computer Network Graph!");
		printLine();
		System.out.println("			Developed by Team GG");

		try {
			for (int i = 0; i < names.size(); i++) {
				currName = names.get(i);
				for (int j = 0; j < currName.length(); j++) {
					TimeUnit.MILLISECONDS.sleep(18);
					System.out.print(currName.charAt(j));
				}
				System.out.println("\n");
			}
			TimeUnit.MILLISECONDS.sleep(18);
			printLine();
		} catch (Exception e) {
			return;
		}
	}
}
