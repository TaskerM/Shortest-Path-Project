Shortest Paths and Minimum Spanning Trees Project
![GUI](/images/GUI1.jpg "GUI")

This project contains a Java program with a graphical user interface (GUI) that uses Dijkstras A (star) heuristic shortest path algorithm, Prims minimum spanning tree algorithm, and Prims shortest path algorithm.

## Description

This program implements the A (star) heuristic variation of Dijkstras shortest path algorithm, Prims minimum spanning tree, and Prims shortest path algorithms. Adjacency matrices are used to represent the input graphs, and each algorithm uses a custom priority queue class.

## Getting Started

### Dependencies

* [JFreeChart v1.0.19 ](https://www.jfree.org/jfreechart/)
    * Used by the GUI.

### Installing
* Clone or download the repository from GitHub.

### Executing Program
* Execute the JAR File:
`ShortPathT3.jar`

* Run from command line
	* Windows:
	
	`java -cp ".\bin\;.\bin\lib\jcommon-1.0.23.jar;.\bin\lib\jfreechart-1.0.19.jar" ShortPathT3 grid_data.txt adj_data.txt`
	
	* Unix
	
	`java -cp "bin/:bin/lib/jcommon-1.0.23.jar:bin/lib/jfreechart-1.0.19.jar" ShortPathT3 grid_data.txt adj_data.txt`

### Using Program
**Run Shortest Path Algorithms**
* Dijkstra
1.  Select the Dijkstra option.
2. Enter a starting Node and target Node into the input field.
	Ex. A,J or N1,N36
3. Click Run Alg

![Dijkstra](/images/dijkstraSP.png "Dijkstra")

* Prim's
1. Select the Prim SP option.
2. Enter a starting Node and target Node into the input field.
	Ex. A,J or N1,N36
3. Click Run Alg

![PrimSP](/images/primSP.png "PrimSP")

**Run Minimum Spanning Tree (MST) Algorithm**
* Prim's
1. Select the Prim MST option.
2. Enter a starting Node.
	Ex. A or N1
3. Click Run Alg

![PrimMST](/images/primMST.png "PrimMST")

**Generate Random Grid**
1. Enter the number of Nodes you would like to generate.
2. Click Random Grid

![RandomGrid](/images/randomGrid.png "RandomGrid")

## Authors
* [**@TaskerM** Mark Tasker](https://github.com/TaskerM)

## Version History

* 1.0
    * Initial Release
