import java.awt.Color;

/**
 * Documentation
 */
public class Edge implements Comparable<Edge> {

    int weight;
    double a_star;
    Color color;
    Node nodeEnds[] = new Node[2];
    PriorityQueue.PriorityNode pq_node;

    Edge(int weight, Node a, Node b, Color color) {
        this.weight = weight;
        nodeEnds[0] = a;
        nodeEnds[1] = b;
        this.color = color;
    }

    // Need to return node from nodeEnds thats not in the function
    Node returnNextNode(Node x) {
        if (x == nodeEnds[0]) {
            return nodeEnds[1];
        } else
            return nodeEnds[0];
    }

    public int compareTo(Edge T) {
        return this.weight - T.weight;
    }

    /**
     * Returns the weight of the Edge
     * @return weight
     */
    public int getWeight() {
        return this.weight;
    }

    public String getName() {
        return nodeEnds[0].getName() + "-" + nodeEnds[1].getName();
    }

    /**
     * Sets the pq node for this object. Used for the PriorityNode constructors.
     * @param priority queue node
     */
    void setPQNode(PriorityQueue.PriorityNode NewPQNode) {
        this.pq_node = NewPQNode;
    }

    void setAStar(Node current, Node destinationNode) {
        double x_diff =  Math.pow(destinationNode.getXVal() - current.getXVal(), 2);
        double y_diff =  Math.pow(destinationNode.getYVal() - current.getYVal() , 2);
        double total_dist = Math.sqrt(x_diff + y_diff);
        this.a_star = total_dist;
    }

    /**
     * This method updates the pq node priority. Use whenever you change value of object after initial queue.
     */
    void updatePriority() {
        if (pq_node != null) 
            this.pq_node.updateValue(weight + a_star);
    }

     /**
     * This method returns the priority value of the object.
     */
    double getPriority() {
        return weight + a_star;
    }

    /**
     * This method sets the color of the Edge.
     * @param color the color.
     */
    void setColor(Color color) {
        this.color = color;
    }

    /**
     * This method reverts the edge back to default values
     */
    void refreshEdge() {
        a_star = 0;
        color =  Color.blue;
        pq_node = null;
    }

    /**
     * This method swaps the order of the Nodes assigned to the Edge.
     */
    void swapNodes() {
        Node temp = nodeEnds[0];
        nodeEnds[0] = nodeEnds[1];
        nodeEnds[1] = temp;
    }
}