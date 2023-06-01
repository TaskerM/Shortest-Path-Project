import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class represent a single Node.
 */
public class Node implements Comparable<Node> {

    private String node_name;
    private boolean visited;
    private int total_distance;
    private double a_star;
    private Color color;
    private int grid_x;
    private int grid_y;
    PriorityQueue.PriorityNode pq_node;

    // Used in Djikstra
    Node prior;

    /**
     * List of connected edges. Should only be connected to
     * edges that contain it as an end node. Needs to be sorted
     **/
    ArrayList<Edge> edgeList = new ArrayList<Edge>();

    /**
     * Constructor
     * 
     * @param nodeChar the node character.
     * @param x the nodes x coordinate.
     * @param y the nodes y coordinate.
     */
    Node(String nodeName, int x, int y) {
        this.node_name = nodeName;
        this.visited = false;
        this.total_distance = Integer.MAX_VALUE;
        this.grid_x = x;
        this.grid_y = y;
        this.color = Color.RED;
    }

    /**
     * This method adds a single Edge to the edgeList.
     * @param edge the edge to add to the edgeList. 
     */
    void addEdge(Edge edge) {
        edgeList.add(edge);
        sortEdgeList();
    }
    void removeEdge(int edgeLoc) {
    	edgeList.remove(edgeLoc);
    	sortEdgeList();
    }

    /**
     * This method adds multiple Edge's to the edgeList.
     * @param edges an array of Edges to add to the edgeList.
     */
    void addEdgeArray(Edge[] edges) {
        Collections.addAll(edgeList, edges);
        sortEdgeList();
    }

     /**
     * This method returns the total distance of the Node.
     * @return the total distance
     */
    public int getDistance() {
	    return this.total_distance;
    }

    /**
     * This method sets the total distance.
     * @param distance the distance
     */
    public void setDistance(int distance) {
        this.total_distance = distance;
    }

         /**
     * This method returns the visited state of the Node.
     * @return the visited state
     */
    public boolean getVisited() {
	    return this.visited;
    }

    /**
     * Sets visited to TRUE
     */
    public void setVisited() {
        this.visited = true;
    }

    /**
     * Returns the prior Node
     * @return prior node
     */
    public Node getPriorNode() {
        return this.prior;
    }

    /**
     * Sets the prior Node
     * @param prior node
     */
    public void setPriorNode(Node prior) {
        this.prior = prior;
    }

    /**
     * Sets the pq node for this object. Used for the PriorityNode constructors.
     * @param priority queue node
     */
    void setPQNode(PriorityQueue.PriorityNode NewPQNode) {
        this.pq_node = NewPQNode;
    }

    void setAStar(Node destinationNode) {
        double x_diff =  Math.pow(destinationNode.getXVal() - this.getXVal(), 2);
        double y_diff =  Math.pow(destinationNode.getYVal() - this.getYVal() , 2);
        double total_dist = Math.sqrt(x_diff + y_diff);
        this.a_star = total_dist;
    }

    /**
     * This method updates the pq node priority. Use whenever you change value of object after initial queue.
     */
    void updatePriority() {
        if (pq_node != null) 
            this.pq_node.updateValue(total_distance + a_star);
    }

    /**
     * This method returns the priority value of the object.
     */
    double getPriority() {
        return total_distance + a_star;
    }

    /**
     * This method returns the x coordinate of the Node.
     * @return the x coordinate
     */
    public int getXVal () {
	    return this.grid_x;
    }

    /**
     * This method returns the y coordinate of the Node.
     * @return the y coordinate
     */
    public int getYVal () {
	    return this.grid_y;
    }

    /**
     * This method returns the Name of the Node.
     * @return the name
     */
    public String getName() {
	    return this.node_name;
    }

    void setColor(Color color) {
        this.color = color;
    }
    /**
     * This method returns color
     * @return color
     */
    public Color getColor() {
    	return color;
    }
    

    /**
     * This method clears the edgeList.
     */
    void clearEdgeList() {
        this.edgeList.clear();
    }

    /**
     * This method sorts the edgeList.
     */
    void sortEdgeList() {
        Collections.sort(edgeList);
    }

    @Override
    public int compareTo(Node otherNode) {
        return Integer.compare(this.total_distance, otherNode.getDistance());
    }

    /**
     * This method reverts the node back to default values
     */
    void refreshNode() {
        visited = false;
        total_distance = Integer.MAX_VALUE;
        a_star = 0;
        prior = null;
        pq_node = null;
        color = Color.RED;
    }

    
}