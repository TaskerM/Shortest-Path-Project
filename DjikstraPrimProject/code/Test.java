import java.util.ArrayList;
import java.lang.Math;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

class Test {

ArrayList<Double> power2Array = new ArrayList<>();
ArrayList<Long> timeResults = new ArrayList<>();
ArrayList<Integer> timeResults_NodeCount = new ArrayList<>();
ArrayList<Integer> distanceResults = new ArrayList<>();
ShortPathT3 TestSP = new ShortPathT3();
int testsPerNodeCount = 100;

Test() {
    this.TestSP = new ShortPathT3();
}
Test(ShortPathT3 SPToTest) {
    this.TestSP = SPToTest;
}
Test(ShortPathT3 SPToTest, int min_sq, int max_sq) {
    this.TestSP = SPToTest;
    this.power2Array = new ArrayList<>();
    for(int i = min_sq; i <= max_sq; i++) {
        power2Array.add(Math.pow(i, 2));
    }
}
void setBase2Range(int min_sq, int max_sq) {
    this.power2Array = new ArrayList<>();
    for(int i = min_sq; i <= max_sq; i++) {
        power2Array.add(Math.pow(i, 2));
    }
}

void runDijkstraTests() {

    if (power2Array.isEmpty()) return;

    for (Double i: power2Array) {
        
        for (int j = 0; j < testsPerNodeCount; j++) {
            
            //Generate the Random Grid with the current Node Count
            //and initialize the algorithm entity for the tests.
            this.TestSP.newRandomGrid(i.intValue());
            Dijkstra DijkstraSP = new Dijkstra(this.TestSP,  false);

            Node startNode = this.TestSP.nodeIndex.get(0);
            Node endNode = this.TestSP.nodeIndex.get(i.intValue() - 1);
            
            //Time Dijkstra SP given the parameters, and return results  in millisecounds
            long startTime = System.nanoTime();
            DijkstraSP.computeShortestPaths(startNode, endNode);
            long endTime = System.nanoTime();
            long duration = (endTime - startTime); 

            this.timeResults.add(duration);
            this.timeResults_NodeCount.add(i.intValue());
            this.distanceResults.add(DijkstraSP.totalCost);
        }
    }
}

void runPrimTests() {

    if (power2Array.isEmpty()) return;

    for (Double i: power2Array) {
        
        for (int j = 0; j < testsPerNodeCount; j++) {
            
            //Generate the Random Grid with the current Node Count
            //and initialize the algorithm entity for the tests.
            this.TestSP.newRandomGrid(i.intValue());
            Prims PrimsSP = new Prims(this.TestSP,  false);

            Node startNode = this.TestSP.nodeIndex.get(0);
            Node endNode = this.TestSP.nodeIndex.get(i.intValue() - 1);
            
            //Time Prims SP given the parameters, and return results  in millisecounds
            long startTime = System.nanoTime();
            PrimsSP.computePath(startNode, endNode);
            long endTime = System.nanoTime();
            long duration = (endTime - startTime); 

            this.timeResults.add(duration);
            this.timeResults_NodeCount.add(i.intValue());
            this.distanceResults.add(PrimsSP.totalCost);
        }
    }
}

void outputCSV(String filename) {
    
    try (PrintWriter writer = new PrintWriter(new File(filename))) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < timeResults.size(); i++) {
            sb.append(timeResults_NodeCount.get(i));
            sb.append(',');
            sb.append(timeResults.get(i));
            sb.append(',');
            sb.append(distanceResults.get(i));
            sb.append('\n');
        }
        
        writer.write(sb.toString());
        writer.close();

    }
    catch (FileNotFoundException e) {
    System.out.println(e.getMessage());
}
}

public static void main(String[] args) {

    Test DijkstraTest = new Test();
    DijkstraTest.setBase2Range(4, 32);
    DijkstraTest.runDijkstraTests();
    DijkstraTest.outputCSV("test_results_dijkstra.csv");

    Test PrimsTest = new Test();
    PrimsTest.setBase2Range(4, 32);
    PrimsTest.runPrimTests();
    PrimsTest.outputCSV("test_results_prims.csv");
}

}