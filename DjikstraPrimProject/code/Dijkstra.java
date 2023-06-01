import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dijkstra {
    
    ArrayList<Node> nodeList = new ArrayList<>();
    PriorityQueue priorityQueue = new PriorityQueue();
    StringBuilder consoleOutputSB = new StringBuilder();
    private boolean showConsoleOutput;
    int totalCost = -1;

    /**
     * Constructor
     * @param sp
     */
    public Dijkstra(ShortPathT3 sp) {
        nodeList = sp.nodeIndex;
        this.showConsoleOutput = true;
    }

    /**
     * Constructor
     * @param sp
     * @param showConsoleOutput
     */
    public Dijkstra(ShortPathT3 sp, boolean showConsoleOutput) {
        nodeList = sp.nodeIndex;
        this.showConsoleOutput = showConsoleOutput;
    }

    /**
     * This method finds the shortest path from a starting node to a 
     * tartget node using Dijkstra's A* Heuristic
     * @param startNode the starting node
     * @param targetNode the target node
     */
    public StringBuilder computeShortestPaths(Node startNode, Node targetNode) {
        // Set distance for starting Node to 0
        startNode.setDistance(0);

        // Add starting Node to priority queue
        priorityQueue.enqueue(startNode);

        while(!priorityQueue.isEmpty()) {
            Node currentNode = (Node) priorityQueue.pop();

            if (!currentNode.getVisited()) {
                currentNode.setVisited();
                currentNode.setColor(Color.ORANGE);

                if (currentNode.getName().equals(targetNode.getName())) {
                    // Target Node Found, Reconstruct Path
                    reconstructPath(startNode, targetNode);
                    break;
                }

                for(Edge edge : currentNode.edgeList) {
                    Node n = edge.returnNextNode(currentNode);

                    if(!n.getVisited()) {
                        edge.setColor(Color.ORANGE);
                        
                        // Calculate Distance from Start Node
                        int newDistance = currentNode.getDistance() + edge.getWeight();
    
                        if (newDistance < n.getDistance()) {
                            n.setDistance(newDistance);
                            n.setAStar(targetNode);
                            n.updatePriority(); // Distance + A Star
                            n.setPriorNode(currentNode);
                            if (n.pq_node == null)
                                priorityQueue.enqueue(n);
                        }
                    }
                }
            }
        }

        if (targetNode != null && showConsoleOutput && targetNode.prior == null) {
            System.out.println("Dijkstra - Could not find path from source node " + startNode.getName() + " to target node " + targetNode.getName() + "\n");
        }

        return consoleOutputSB;
    }

    /**
     * This method reconstructs the shortest path and displays it to the
     * user.
     * @param startNode the starting node
     * @param targetNode the target node
     */
    void reconstructPath(Node startNode, Node targetNode) {
        List<String> path = new ArrayList<>();
        this.totalCost = targetNode.getDistance();
        Node current = targetNode;

        while (current != startNode) {
            path.add(current.getName());
            current.setColor(Color.GREEN);

            // Color the Edges along the path green
            for (Edge edge : current.edgeList) {
                if (edge.nodeEnds[0].equals(current.prior) || edge.nodeEnds[1].equals(current.prior)) {
                    edge.setColor(Color.GREEN);
                }
            }
            current = current.prior;
        }

        path.add(startNode.getName());
        startNode.setColor(Color.GREEN);

        Collections.reverse(path);

        consoleOutputSB.append("Dijkstra - Shortest Path from " + startNode.getName() + " to " + targetNode.getName() + ": ");
        consoleOutputSB.append(path);
        consoleOutputSB.append("\n" + "Total Path Cost: " + totalCost + "\n");

        if (showConsoleOutput)
            System.out.println(consoleOutputSB);
    }
}
