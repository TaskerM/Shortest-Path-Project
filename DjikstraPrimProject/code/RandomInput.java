import java.awt.Color;
import java.util.Random;

public class RandomInput {
    ShortPathT3 SPItem;
    int[][] gridArray;

    double connect_modifier;
    int connect_range; 
    double adj_max;
    int nodeCount;
    int grid_height;

    RandomInput(ShortPathT3 SPItem, int nodeCount) {
        this.SPItem = SPItem;
        this.nodeCount = nodeCount;
        grid_height = (int)Math.ceil(Math.sqrt((double)nodeCount));
        connect_modifier = 0.33;
        connect_range = 10;
        adj_max = 1.5;
        updateGridArray();
        updateNodeIndex();
    }

    private void updateGridArray() {
        
        int[][] returnArray = new int[this.nodeCount][2];
        int next = 0;

        for (int i = 0; i < this.grid_height; i++) {
            for (int j = 0; j < this.grid_height; j++) {  
                if(next < nodeCount) {
                returnArray[next][0] = i;
                returnArray[next][1] = j;
                next++; 
                }
            }
        }
        this.gridArray = returnArray;
    }

    private void updateNodeIndex() {
        
        for (int i =  0; i < nodeCount; i++) {
            String name = "N" + (i+1);
            Node newNode = new Node(name, gridArray[i][0], gridArray[i][1]);
            SPItem.nodeIndex.add(newNode);
        }

        for (int i = 0; i < nodeCount; i++) {
            Node current = SPItem.nodeIndex.get(i);
            
            for (int j = 0; j < nodeCount; j++) {
                
                Node other = SPItem.nodeIndex.get(j);
                
                if (other == current) {
                    continue;
                }
                
                double x_diff =  Math.pow(other.getXVal() - current.getXVal(), 2);
                double y_diff =  Math.pow(current.getYVal() - other.getYVal() , 2);
                double total_dist = Math.sqrt(x_diff + y_diff);
                
                if (total_dist > adj_max)  continue;
                
                boolean present = false;

                for (int k = 0; k < current.edgeList.size(); k++) {
                    Node edgeNode = current.edgeList.get(k).returnNextNode(current);
                    if (edgeNode == other) {
                        present = true;
                        break;
                    }
                }

                if (present == true) continue;
                
                Random rand = new Random();
                double randVal =  rand.nextDouble()*100;
                double percent_connect =  (connect_modifier/(total_dist))*100.0;
                
                if( randVal < percent_connect) {
                    Edge newEdge = new Edge(rand.nextInt(1, connect_range), current, other,Color.BLUE);
					current.addEdge(newEdge);
					other.addEdge(newEdge);
					SPItem.edgeIndex.add(newEdge);
                }
            }
            
            Random rand = new Random();
            int tgt = rand.nextInt(1, 3);

            if ( current.edgeList.size() < tgt) {
                i--;
            }
        }
    }

   /* public static void main(String[] args) {

    ShortPathT3 SPTest = new ShortPathT3(args);
    SPTest.newRandomGrid(64);
    new GUI(SPTest);

    }*/

}