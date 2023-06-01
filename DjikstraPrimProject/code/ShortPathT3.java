import java.util.Scanner;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

/**
 * Documentation
 */
public class ShortPathT3 {

	ArrayList<Node> nodeIndex = new ArrayList<Node>();
	ArrayList<Edge> edgeIndex = new ArrayList<Edge>();
	StringBuilder consoleOutputSB = new StringBuilder();
	int defaultSize = 64;

	public ShortPathT3() {
		String args[] = new String[]{};
		initiate(args);
	}
	
	public ShortPathT3(String[] args) {
		initiate(args);
	}

	
	// Read in grid_data, update this.nodeIndex
	void generateNodes(String fileName) throws Exception {

		ArrayList<String> temp;

		try { 
			temp = readFile(fileName);
		} catch (FileNotFoundException e) {
			throw e;
		}

		for (int i = 0; i < temp.size(); i++) {

			// handling double digit vertices
			String[] elements = temp.get(i).split(",");

			String NodeN;
			int x;
			int y;

			for (int b = 0; b < elements.length; b++) {

				// for every third number after commas assign vertices and Node
				if (b % 3 == 0) {

					NodeN = (elements[b]);
					x = Integer.parseInt(elements[b + 1].toString());
					y = Integer.parseInt(elements[b + 2].toString());

					// System.out.println(NodeN + "," + x + "," + y);

					// creating new Node object
					Node v0 = new Node(NodeN, x, y);

					// add node to nodeIndex to keep track of instances
					nodeIndex.add(v0);
				}
			}
		}
	}

	// Read in adj_data, update this.edgeIndex and this.adjArray
	void generateAdj(String fileName) throws Exception{
		ArrayList<String> temp;
		
		try { 
			temp = readFile(fileName);
		} catch (FileNotFoundException e) {
			throw e;
		}

		String[] elements = temp.get(0).split(",");
		Node[] nodeArray = new Node[elements.length - 1];

		for (int i = 1; i < elements.length; i++) {
			for (int j = 0; j < nodeIndex.size(); j++) {
				if ( nodeIndex.get(j).getName().equals(elements[i]) ) {
					nodeArray[i-1] = nodeIndex.get(j);
					break;
				}
			}
		}

		for (int i = 1; i < temp.size(); i++) {
			String[] edge_weights = temp.get(i).split(",");
			for (int b = 1; b < edge_weights.length; b++) {				
				int weightVal = Integer.parseInt(edge_weights[b]);
				if ( b <= i || weightVal == 0) {
					continue;
				}
				else {
					Edge newEdge = new Edge(weightVal, nodeArray[i-1], nodeArray[b-1], Color.BLUE);
					nodeArray[i-1].addEdge(newEdge);
					nodeArray[b-1].addEdge(newEdge);
					edgeIndex.add(newEdge);
				}
			}
		}
	}

	// method to readfiles
	public static ArrayList<String> readFile(String fileName) throws FileNotFoundException {
		File file = new File(fileName);
		Scanner sc = new Scanner(file);
		ArrayList<String> arr = new ArrayList<String>();

		//StringBuffer sb = new StringBuffer();
		while (sc.hasNext()) {
			String temp = sc.nextLine();
			String[] splitted = temp.split(" ");

			for (int k = 0; k < splitted.length; k++) {
				arr.add(splitted[k].replaceAll("\\s", ","));
			}
		}
		sc.close();
		return arr;
	}

	// method to readargument
	void readArgs(String[] args) throws Exception{
		
		if (args.length == 2) {
			String gridFileName = args[0];
			int arg1Size = gridFileName.length();
			String adjFileName = args[1];
			int arg2size = adjFileName.length();

			try {
				if (gridFileName.substring(arg1Size - 4).equals(".txt")) {
					generateNodes(gridFileName);
				}
				if (adjFileName.substring(arg2size - 4).equals(".txt") && nodeIndex.size() > 0) {
					generateAdj(adjFileName);
				}
			} catch (Exception e) {
				throw e;
			}
		}
	}

	public void initiate(String[] args) {

		if (args.length != 2 ) {
			//System.out.println("Generating random grid with " + this.defaultSize + " nodes.");
			this.newRandomGrid(this.defaultSize);
		}
		else {
			try{
				this.readArgs(args);
			}
			catch (Exception e) {
				System.out.println("Could not read in files " + args[0] + " and " + args[1] + ", generating random grid with " + this.defaultSize + " nodes.");
				this.newRandomGrid(this.defaultSize);
			}
			if ( this.edgeIndex.size() == 0 || this.nodeIndex.size() == 0) {
				//System.out.println("Generating random grid with " + this.defaultSize + " nodes.");
				this.newRandomGrid(this.defaultSize);
			}
		}
	}

	// Method to revert value of Nodes and Edges to default values. 
	// Maintains edge weights and the edge lists of nodes.
	public void refreshNodesEdges() {
		for (int i = 0; i < this.nodeIndex.size(); i++) {
			Node current = nodeIndex.get(i);
			current.refreshNode();
		}
		for (int i = 0; i < this.edgeIndex.size(); i++) {
			Edge current = edgeIndex.get(i);
			current.refreshEdge();
		}
	}

	//Method to updatethe weights of the existing edges with random values for all of
	//those currently in the edge index.
	public void randomizeEdgeWeights(int min, int max) {
		int min_range = 0;
		int max_range = 0;

		if (min < 0) {
			min_range = 0;
		}
		else min_range = min;

		if (max < min_range) {
			max_range = min_range + 1;
		}
		else max_range = max;

		Random randResult = new Random();

		for (Edge i : edgeIndex) {
			i.weight =  randResult.nextInt(min_range, max_range);
		}
	}

	/**
	 * This method runs the Dijkstra Shortest Path Algorithm (A*)
	 * @param startName The name of the start Node
	 * @param destName The name of the destination (Target) Node
	 */
	public void DijkstraSP(String startName, String destName) {
		Node startNode = null;
		Node destNode = null;

		for (Node node : nodeIndex ) {
			if(startNode != null && destNode != null)
				break;
			if(node.getName().equalsIgnoreCase(startName)) {
				startNode = node;
			}
			else if (node.getName().equalsIgnoreCase(destName)) {
				destNode = node;
			}
		}
		
		if ( startNode == null || destNode == null ) {
			consoleOutputSB.append("Could not find Nodes " + startName + " and " + destName);
			consoleOutputSB.append("\n");
			consoleOutputSB.append("\n");
			return;
		}
		refreshNodesEdges();
		Dijkstra dijkstra = new Dijkstra(this);
		consoleOutputSB.append(dijkstra.computeShortestPaths(startNode, destNode));
		consoleOutputSB.append("\n");
		consoleOutputSB.append("\n");
	}

	/**
	 * This method runs the Prim’s Minimum Spanning Tree Algorithm, or
	 * a Prim's version of the Shorest Path algorithm when a targetName
	 * is provided to the method.
	 * 
	 * @param startName The name of the start Node
	 * @param targetName Optional: The name of the target Node
	 */
	public void Prims(String startName, String targetName) {
		boolean targetNameProvided = Optional.ofNullable(targetName).isPresent();
		Node startNode = null;
		Node targetNode = null;

		// Search for the starting Node object
		for (Node node : nodeIndex ) {
			if(startNode != null)
				break;

			if(node.getName().equalsIgnoreCase(startName))
				startNode = node;
		}

		// Check to see if a starting Node was found.
		if (startNode == null) {
			consoleOutputSB.append("Could not find Node: " + startName);
			consoleOutputSB.append("\n");
			consoleOutputSB.append("\n");
			return;
		}

		// If a targetName was provided, search for the target Node object
		if (targetNameProvided) {
			for (Node node : nodeIndex ) {
				if(targetNode != null)
					break;
	
				if(node.getName().equalsIgnoreCase(targetName))
					targetNode = node;
			}
			
			// Check to see if a target Node was found
			if (targetNode == null) {
				consoleOutputSB.append("Could not find Node: " + targetName);
				consoleOutputSB.append("\n");
				consoleOutputSB.append("\n");
				return;
			}
		}

		refreshNodesEdges();
		Prims prims = new Prims(this);
		consoleOutputSB.append(prims.computePath(startNode, targetNode));
		
		consoleOutputSB.append("\n");
	}

	/**
	 * This method creates new Node and Edge indexes, then loads in a randomized grid
	 * with a number of nodes equal to the nodeCount.
	 * 
	 * @param nodeCount the number of nodes to create
	 */
	public void newRandomGrid(int nodeCount) {
			this.nodeIndex = new ArrayList<Node>();
			this.edgeIndex = new ArrayList<Edge>();
			new RandomInput(this , nodeCount);
	}

	public static void main(String[] args) {
		ShortPathT3 newSP = new ShortPathT3(args);
		//newSP.newRandomGrid(16);
		new GUI(newSP);

		return;
	}
}
