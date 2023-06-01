import java.awt.Color;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Collections;

/**
 * This class supports the Prim's Minimum Spanning Tree algorithm and
 * the Prim's version of the Shortest Path Algorithm.
 */
public class Prims {

    ArrayList<Node> nodeList = new ArrayList<>();
    PriorityQueue priorityQueue = new PriorityQueue();
    StringBuilder consoleOutputSB = new StringBuilder();

    ArrayList<String> mst = new ArrayList<>();
    int totalCost = -1;

    private boolean showConsoleOutput;

    /**
     * Constructor without show console output parameter. With this
     * constructor the console output will always be displayed.
     * 
     * @param sp ShortPathT3
     */
    public Prims(ShortPathT3 sp) {
        nodeList = sp.nodeIndex;
        this.showConsoleOutput = true;
    }

    /**
     * Constructor with show console output.
     * 
     * @param sp ShortPathT3
     * @param showConsoleOutput TRUE  = Show console output. 
     *                          FALSE = Hide console output.
     */
    public Prims(ShortPathT3 sp, boolean showConsoleOutput) {
        nodeList = sp.nodeIndex;
        this.showConsoleOutput = showConsoleOutput;
    }

    /**
     * This method produces a Minimum Spanning Tree (MST) or it finds the 
     * shortest path between Nodes using Prim's
     * 
     * @param startNode the starting Node
     * @param targetNode Optional: if this parameter is provided, Prim's
     *                   will use the shortest path algorithm.
     */
    public StringBuilder computePath(Node startNode, Node targetNode) {
        boolean targetNodeProvided = Optional.ofNullable(targetNode).isPresent();
        if (!targetNodeProvided)
            this.totalCost = 0;
        Node currentNode = startNode;
        startNode.setVisited();
        startNode.setColor(Color.GREEN);

        // Add all Edges of the startNode to the PQ
        for (Edge edge : startNode.edgeList) {
            if (startNode != edge.nodeEnds[0]) 
                edge.swapNodes();
            if(targetNodeProvided)
                edge.setAStar(edge.nodeEnds[1], targetNode);
            edge.nodeEnds[1].setPriorNode(edge.nodeEnds[0]);
            priorityQueue.enqueue(edge);
        }

        while (!priorityQueue.isEmpty()) {
            // Get the Edge with the smallest cost
            Edge currentEdge = (Edge) priorityQueue.pop();

            // Skip if Node has already been added to the MST
            if (currentEdge.returnNextNode(currentEdge.nodeEnds[0]).getVisited())
                continue;

            // Update the currentNode to the Node at the other end of the Edge
            currentNode = currentEdge.returnNextNode(currentEdge.nodeEnds[0]);

            // Add path to MST for console output
            if(!targetNodeProvided) 
                mst.add(currentEdge.returnNextNode(currentNode).getName() + " -> " + currentNode.getName());

            // Set currentNode to visited and color
            currentNode.setVisited();
            
            if(targetNodeProvided) {
                currentNode.setColor(Color.ORANGE);
            }
            else {
                currentNode.setColor(Color.GREEN);
                currentEdge.setColor(Color.GREEN); 
                // Increment the cost by the cost (weight) of the Edge
                totalCost += currentEdge.getWeight();
            }

            // If a target Node was provided, stop when the target Node is found.
            if (targetNodeProvided) {
                if (currentNode.equals(targetNode))
                    break;
            }
            // Add all edges of the currentNode to the PQ
            for (Edge edge : currentNode.edgeList) {
                if (currentNode != edge.nodeEnds[0]) {
                    edge.swapNodes();
                }
                if (!edge.nodeEnds[1].getVisited()) {
                    if(targetNodeProvided) {
                        edge.setAStar(edge.nodeEnds[1], targetNode);
                        edge.setColor(Color.ORANGE);
                    }
                    else edge.setColor(Color.WHITE);
                    edge.nodeEnds[1].setPriorNode(edge.nodeEnds[0]);
                    priorityQueue.enqueue(edge);
                }
            }
        }
        
        //Check to see if targetNode was reachable by the algorithm.
        if (targetNodeProvided) { 
            if ( targetNode != null && targetNode.prior == null) {
                if (showConsoleOutput ) 
                    System.out.println("Prim's - Could not find path from source node " + startNode.getName() + " to target node " + targetNode.getName() + "\n");
                return null;
            }
            else colorShortPath(startNode, currentNode);
        }
        
        // Path complete, print Path   
        printPath(targetNodeProvided, startNode, targetNode);    
        return consoleOutputSB;
    }

    private void colorShortPath(Node startNode,Node targetNode) {

        Node current = targetNode;
        totalCost = 0;
        this.mst.clear();
        this.mst.add(current.getName());

        while (current != startNode) {
            current.setColor(Color.GREEN);

            // Color the Edges along the path green
            for (Edge edge : current.edgeList) {
                if (edge.nodeEnds[0].equals(current.prior) || edge.nodeEnds[1].equals(current.prior)) {
                    edge.setColor(Color.GREEN);
                    totalCost += edge.weight;
                }
            }
            current = current.prior;
            this.mst.add(current.getName() + ", ");
        }
        Collections.reverse(mst);
    }

    /**
     * This method prints the Minimum Spanning Tree or
     * the shortest path between two Nodes.
     * 
     * @param targetNodeProvided TRUE  = target Node was provided.
     *                           FALSE = target Node was not provided.
     */
    void printPath(boolean targetNodeProvided, Node startNode, Node targetNode) {
        if (targetNodeProvided) 
                consoleOutputSB.append("Prim's - Shortest Path from " + startNode.getName() + " to " + targetNode.getName() + ": [");
            else
                consoleOutputSB.append("Prim's - Minimum Spanning Tree:\n");

            // Print path
            for (String path : mst) {
                if(targetNodeProvided) {
                    consoleOutputSB.append(path);
                }
                else
                    consoleOutputSB.append(path + "\n");
            }

            // Print total cost
            if (targetNodeProvided)
                consoleOutputSB.append("]" + "\n" + "Total Path Cost: " + totalCost + "\n");
            else
                consoleOutputSB.append("Minimum Spanning Tree Total Cost: " + totalCost + "\n");
        
        if (showConsoleOutput)
            System.out.println(consoleOutputSB.toString());
    }
}
