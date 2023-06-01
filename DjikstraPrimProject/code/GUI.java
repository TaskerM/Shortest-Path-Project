import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class GUI {
	JFrame frame = new JFrame();
	ShortPathT3 guiMST = null;
	private JTextArea edgeDisplay = new JTextArea("");
	Font exampleFont = new Font("SansSerif",Font.BOLD,9);
	
	//input from user
	private String nodeOne = "";
	private String nodeTwo = "";
	private String userWeight = "";
	
	public GUI(ShortPathT3 guiMST) {
		this.guiMST = guiMST;
		frame.setSize(800,500);
		
		//generate graph nodes/lines
		XYSeriesCollection nodeSet = new XYSeriesCollection();
		XYSeriesCollection lineSet = new XYSeriesCollection();
        XYLineAndShapeRenderer render_nodes = new XYLineAndShapeRenderer(); 
        XYLineAndShapeRenderer render_lines = new XYLineAndShapeRenderer();  
		generateNode(nodeSet);
		generateLines(lineSet);
		nodeRender(render_nodes);
		lineRender(render_lines);

		//Buttons and other components
		JButton addEdge = new JButton("Add Edge");
		JButton removeEdge = new JButton("Remove Edge");
		JButton createGraph = new JButton("Run Alg");
		JButton refreshChart = new JButton("Refresh Chart");
		JButton randomGrid = new JButton("Random Grid");
		JRadioButton A_Star = new JRadioButton("Djikstra");
		JRadioButton prim = new JRadioButton("Prim SP");
		JRadioButton prim_MST = new JRadioButton("Prim MST");
		JTextField userEdge = new JTextField();
		JScrollPane textScroll = new JScrollPane(edgeDisplay);
		JTextField addFormat = new JTextField("Input: Node1,Node2,Wt.");
		JTextField remFormat = new JTextField("Input: Node1,Node2");
		JTextField algFormat = new JTextField("Input: Source,Target");
		JTextField refrFormat = new JTextField("Input: None");
		JTextField randomFormat = new JTextField("Input: Any Integer");
		
		//formatting
		textGenerate(addFormat);
		textGenerate(remFormat);
		textGenerate(algFormat);
		textGenerate(refrFormat);
		textGenerate(randomFormat);
		edgeDisplay.setEditable(false);
		A_Star.setSelected(true);
		
		//button group to prevent double selection
		ButtonGroup algChoice = new ButtonGroup();		
		algChoice.add(A_Star);
		algChoice.add(prim);
		algChoice.add(prim_MST);
		
		//action command to update input recommendation
		A_Star.setActionCommand("Input: Source,Target");
		prim.setActionCommand("Input: Source,Target");
		prim_MST.setActionCommand("Input: Source");
		
		//sets size for each item on our frame
		A_Star.setBounds(550,35,85,30);
		prim.setBounds(640,35,85,30);
		prim_MST.setBounds(590,10,85,30);
		userEdge.setBounds(520,70,210,30);
		addEdge.setBounds(500,115,120,30);
		removeEdge.setBounds(635,115,120,30);
		createGraph.setBounds(500,170,120,30);
		refreshChart.setBounds(635,170,120,30);	
		randomGrid.setBounds(570,220,120,30);
		textScroll.setBounds(480,270,290,175);
		algFormat.setBounds(515,200,100,20);
		addFormat.setBounds(505,145,130,20);
		remFormat.setBounds(645,145,100,20);
		refrFormat.setBounds(665,200,100,20);
		randomFormat.setBounds(590,250,100,20);
		
		//Creates and encapsulates chart
        JFreeChart chart = ChartFactory.createXYLineChart( 
                "Team 3 Project",
                "",
                "",
                nodeSet,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);
        chart.removeLegend();
        XYPlot plot = chart.getXYPlot(); 
        plotPopulate(nodeSet,lineSet,render_nodes,render_lines,plot);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setBounds(15,15,450,400);
		
		//Adds all elements to JFrame for visibility by user
		frame.add(addEdge);
		frame.add(removeEdge);
		frame.add(createGraph);
		frame.add(randomGrid);
		frame.add(refreshChart);
		frame.add(A_Star);
		frame.add(prim);
		frame.add(prim_MST);
		frame.add(userEdge);
		frame.add(textScroll);
		frame.add(chartPanel);
		frame.add(addFormat);
		frame.add(remFormat);
		frame.add(algFormat);
		frame.add(refrFormat);
		frame.add(randomFormat);
		frame.setLayout(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//listen to radio buttons to update input recommendation
		A_Star.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	            algFormat.setText(e.getActionCommand());
	         }});
		prim.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	            algFormat.setText(e.getActionCommand());
	         }});
		prim_MST.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	            algFormat.setText(e.getActionCommand());
	         }});
		
		//Adds edge
		addEdge.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){ 
				try {
					inputProcess(userEdge.getText(),1);
					lineSet.addSeries(edgeCheckAdd(nodeOne,nodeTwo,Integer.parseInt(userWeight)));
					guiRefresh(nodeSet,lineSet,render_nodes,render_lines,plot);
					stringUpdate("You added edge " +nodeOne.toUpperCase()+"-"+
							nodeTwo.toUpperCase()+ " at weight " + userWeight);
				}
				catch (Exception event) { 
					stringUpdate("Failed to Add Edge. \nEnter Node1,Node2,Weight");
					{

				}}}});
		
		//Removes edge
		removeEdge.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				try {
					inputProcess(userEdge.getText(),2);
					lineSet.removeSeries(edgeCheckRemove(nodeOne,nodeTwo,lineSet));
					guiRefresh(nodeSet,lineSet,render_nodes,render_lines,plot);
				}
				catch (Exception event) {
					stringUpdate("You failed to Remove Edge. Try again.");
				}
				}}); 
		
		//Refreshed chart with normalized nodes. 
		refreshChart.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				guiMST.refreshNodesEdges();
				guiRefresh(nodeSet,lineSet,render_nodes,render_lines,plot);
				stringUpdate("Chart Refreshed.");
				}
			});
		
		//Runs either Djikstra or Prims_SP or Prims MST depending on radio button selected
		createGraph.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				try {
					if (A_Star.isSelected()) {
						inputProcess(userEdge.getText(),2);
						guiMST.DijkstraSP(nodeOne, nodeTwo);
						guiRefresh(nodeSet,lineSet,render_nodes,render_lines,plot);
						stringUpdate("A* algorithm attempt completed.");
						}
					if (prim.isSelected()) {
						inputProcess(userEdge.getText(),2);
						guiMST.Prims(nodeOne,nodeTwo);
						guiRefresh(nodeSet,lineSet,render_nodes,render_lines,plot);
						stringUpdate("Prim's SP attempt completed.");
					}
					if (prim_MST.isSelected()) {
						inputProcess(userEdge.getText(),3);
						guiMST.Prims(nodeOne,null);
						guiRefresh(nodeSet,lineSet,render_nodes,render_lines,plot);
						stringUpdate("MST algorithm attempt completed.");
					}
				}
				catch (Exception event) {
					stringUpdate("Error: Enter Correct Input");
				}}});
		
		//generates random grid for user
		randomGrid.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				try {
					inputProcess(userEdge.getText(),3);
					guiMST.newRandomGrid(Integer.parseInt(nodeOne));
					guiRefresh(nodeSet,lineSet,render_nodes,render_lines,plot);
					stringUpdate("Generated " +Integer.parseInt(nodeOne)+ " random nodes");
				}
				catch (Exception event) {
					stringUpdate("Error: Enter a single integer.");
				}}});}
	
	/*Formats textfields under each button
	 *  
	 */
	private void textGenerate(JTextField value) {
		value.setFont(exampleFont);
		value.setBorder(null);
		value.setEditable(false);}
	
	/*Populates a collection set based upon Nodes
	 * 
	 */
	
	private void generateNode(XYSeriesCollection set) {
		Iterator<Node> itr = guiMST.nodeIndex.iterator();
		while (itr.hasNext()) {
			Node workingNode = itr.next();
			int xVal = workingNode.getXVal();
			int yVal = workingNode.getYVal();
			String nodeName = workingNode.getName();
			
			XYSeries series = new XYSeries(nodeName,false);
			series.add(xVal,yVal);
			set.addSeries(series);	
	}}
	
	/*Renders lines between nodes visible
	 * 
	 */
	private void nodeRender(XYLineAndShapeRenderer line) {
		int nodeCount = 0;
		while (nodeCount < guiMST.nodeIndex.size()) {
			Color workingColor = guiMST.nodeIndex.get(nodeCount).getColor();
			line.setSeriesShape(nodeCount, new Ellipse2D.Double(-15,-15,30,30));
			line.setSeriesPaint(nodeCount,workingColor);
			line.setSeriesShapesFilled(nodeCount,true);
			nodeCount++;
		}}

	/*Traverses edge list to create edge dataset
	 * 
	 */
	private void generateLines(XYSeriesCollection set) {
		Iterator<Edge> itr = guiMST.edgeIndex.iterator();
		while (itr.hasNext()) {
			Edge workingline = itr.next();
			Node nodeStart = workingline.nodeEnds[0];
			Node nodeEnd = workingline.nodeEnds[1];
			
			int xVal1 = nodeStart.getXVal();
			int yVal1 = nodeStart.getYVal();
			int xVal2 = nodeEnd.getXVal();
			int yVal2 = nodeEnd.getYVal();
			
			XYSeries series = new XYSeries(nodeStart.getName()+nodeEnd.getName(),false);
			series.add(xVal1,yVal1);
			series.add(xVal2,yVal2);
			set.addSeries(series);	
	}}
	
	/*
	 * Makes lines visible on graph
	 */
	private void lineRender(XYLineAndShapeRenderer line) {
		int lineCount = 0;
		while (lineCount < guiMST.edgeIndex.size()) {
			Color workingColor = guiMST.edgeIndex.get(lineCount).color;
			line.setSeriesPaint(lineCount,workingColor);
			line.setSeriesShapesFilled(lineCount,true);
			line.setSeriesLinesVisible(lineCount, true); 
			lineCount++;
		}}
	
    /* 
     * Adds labels to nodes
     */
    private void addNodeLabels(XYPlot plotName) {
    	for (Node node : guiMST.nodeIndex) {
			XYTextAnnotation annotation = new XYTextAnnotation(node.getName(),
					node.getXVal(),node.getYVal());
			plotName.addAnnotation(annotation);	
    	}}
    
    /* Traverses edge list and averages middle of each edge to annotate label of weight to plot
     * Using array list with 2D point, if labels overlap, slight adjustment to differentiate labels
     */
    private void addLineLabel(XYPlot plotName) {
    	
    	ArrayList<Point2D.Double> tempList = new ArrayList<Point2D.Double>();
    	for (Edge edge : guiMST.edgeIndex) {
    		double xAvg = 0;
    		double yAvg = 0;
    		String weight = Integer.toString(edge.weight);
    		for (Node node : edge.nodeEnds) {
    			xAvg = xAvg + node.getXVal();
    			yAvg = yAvg + node.getYVal();
    		}
    		xAvg = (xAvg / 2) -0.04;
    		yAvg = (yAvg / 2) -0.10;
    		
    		for (Point2D.Double point : tempList) {
    			if (point.getX() == xAvg && point.getY() == yAvg) {
    				xAvg = xAvg +0.05;
    				yAvg = yAvg +0.19;
    			}
    		}
    		Point2D.Double pointDouble = new Point2D.Double(xAvg,yAvg);
			XYTextAnnotation annotate = new XYTextAnnotation(weight,xAvg,yAvg);
			tempList.add(pointDouble);
			plotName.addAnnotation(annotate);}}
    
    /*Refreshes plot, nodes, lines in GUI
     * when a button is pushed
     */
    public void guiRefresh(XYSeriesCollection dataset1,XYSeriesCollection dataset2,
    		XYLineAndShapeRenderer line1,XYLineAndShapeRenderer line2,XYPlot plot) {
		dataset1.removeAllSeries();
		dataset2.removeAllSeries();
		generateNode(dataset1);
		nodeRender(line1);
		generateLines(dataset2);
		lineRender(line2);
		plot.clearAnnotations();
		plotPopulate(dataset1,dataset2,line1,line2,plot);
    }
	/*Adds datasets and renderers to plot
	 * Annotates labels to plot for nodes
	 */
    private XYPlot plotPopulate(XYDataset dataset1,XYDataset dataset2,
    		XYLineAndShapeRenderer line1,XYLineAndShapeRenderer line2,XYPlot plot) {
    	
    	//additional plot point to make chart size correctly
    	XYSeriesCollection dataset_extra = new XYSeriesCollection();
		XYSeries extraPoint = new XYSeries("ExtraPoint",false);
		extraPoint.add(-.2,-.2);
		dataset_extra.addSeries(extraPoint);
		XYLineAndShapeRenderer renderer_extra = new XYLineAndShapeRenderer(true,true);
		renderer_extra.setSeriesLinesVisible(0, false); 
		renderer_extra.setSeriesShapesVisible(0, false); 
    	
    	//Makes tool tip of node points visible
        line1.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
        
        addNodeLabels(plot);
        addLineLabel(plot);
        
        //datasets add points to plots and renderers make them and their respective lines visible
		plot.setDataset(0,dataset1);
        plot.setRenderer(0,line1); 
        plot.setDataset(1,dataset2);
        plot.setRenderer(1,line2);
        plot.setDataset(2,dataset_extra);
        plot.setRenderer(2,renderer_extra);
        plot.setBackgroundPaint(Color.white); 
        plot.setRangeGridlinesVisible(false); 
        plot.setDomainGridlinesVisible(false); 
        plot.getRangeAxis().setVisible(false);
        plot.getDomainAxis().setVisible(false);
    	return plot;
    }
    	
    
    
    /*
     * Traverses array list of nodes adds a new edge to index of those two nodes + weight
     * @returns series to be added to GUI
     */
    
    private XYSeries edgeCheckAdd(String node1, String node2, int weight) {
		int nodeCount = 0;
		int nodeLoc1 = 0;
		int nodeLoc2 = 0;
		Color blueCol = Color.BLUE;

		XYSeries series = new XYSeries(node1+node2,false);
		
		while (nodeCount < guiMST.nodeIndex.size()) {
			Node workingNode = guiMST.nodeIndex.get(nodeCount);
			String nodeName = workingNode.getName();
			
			if (node1.equalsIgnoreCase(nodeName)) {
				nodeLoc1 = nodeCount;
			}
			if (node2.equalsIgnoreCase(nodeName)) {
				nodeLoc2 = nodeCount;
			}
			nodeCount++;
		}
		
		// add edge to SP edge list and individual nodes edge list
		Edge newEdge = new Edge(weight,guiMST.nodeIndex.get(nodeLoc1),guiMST.nodeIndex.get(nodeLoc2),blueCol);
		guiMST.nodeIndex.get(nodeLoc1).addEdge(newEdge);
		guiMST.nodeIndex.get(nodeLoc2).addEdge(newEdge);
		guiMST.edgeIndex.add(newEdge);
		//create series to add to line dataset
		series.add(guiMST.nodeIndex.get(nodeLoc1).getXVal(),guiMST.nodeIndex.get(nodeLoc1).getYVal());
		series.add(guiMST.nodeIndex.get(nodeLoc2).getXVal(),guiMST.nodeIndex.get(nodeLoc2).getYVal());
		return series; 
    }
    /*
     * Traverses shortpathT3 edge list for location of user requested edge
     * If found, traverses lineSet data series to locate series to remove.
     * Also locates duplicate edgelist within each node object to remove
     * @returns int of series that needs to be removed from line dataSet
     */
    private int edgeCheckRemove(String node1, String node2, XYSeriesCollection seriesSet) {
		int edgeCount = 0;
		int seriesLoc1 = -1;
		int seriesLoc2 = -2;
		int foundEdge = -1;
		int seriesNum = 0;
		int finLoc = 0;
		String userSubmission = node1+node2; //key to search for

		while (edgeCount < guiMST.edgeIndex.size()) {
			Edge workingEdge = guiMST.edgeIndex.get(edgeCount);
			Node nodeStart = workingEdge.nodeEnds[0];
			Node nodeEnd = workingEdge.nodeEnds[1];
			String edgeName = nodeStart.getName() + nodeEnd.getName();	
			String edgeNameReverse = nodeEnd.getName() + nodeStart.getName();
			
			//checks user submitted edge against working edge
			if (userSubmission.equalsIgnoreCase(edgeName) || userSubmission.equalsIgnoreCase(edgeNameReverse)) {
				foundEdge = edgeCount;
				
				//after match found, traverses duplicate edge list in individual nodes to remove edge list
				for (Node node : workingEdge.nodeEnds) {
					for (Edge edge : node.edgeList) {
						if (edge.equals(workingEdge)) {
						node.edgeList.remove(workingEdge);
						break;
						}}}
				
				//after match found, traverses line dataset to find dataset location to remove
				while (seriesNum < seriesSet.getSeriesCount()) {
					XYSeries series0 = seriesSet.getSeries(seriesNum);
					for (Object i : series0.getItems()) {
					  XYDataItem item = (XYDataItem) i;
					  if (item.getXValue() == nodeStart.getXVal() && item.getYValue() == nodeStart.getYVal()) {
						  seriesLoc1 = seriesNum;
					  }
					  if (item.getXValue() == nodeEnd.getXVal() && item.getYValue() == nodeEnd.getYVal()) {
						  seriesLoc2 = seriesNum;
					  }
					  if (seriesLoc1 == seriesLoc2) {
						  finLoc = seriesSet.getSeriesIndex(seriesSet.getSeriesKey(seriesNum));
						  seriesNum = seriesSet.getSeriesCount();
					  }}
					seriesNum++;
				}}
			edgeCount++;
		}
		//prints to user the edge and edge weight that was removed
		stringUpdate("You removed edge " +guiMST.edgeIndex.get(foundEdge).nodeEnds[0].getName().toUpperCase()
				+"-"+guiMST.edgeIndex.get(foundEdge).nodeEnds[1].getName().toUpperCase()+
				" at weight " + guiMST.edgeIndex.get(foundEdge).weight);
	
		guiMST.edgeIndex.remove(foundEdge);
		return finLoc;
    }
    
    /*Performs validation on strings
     * Validation type differs for each choice
     */
    public void inputProcess(String userAdd, int userChoice) {
    	String[] userLine = userAdd.replaceAll("\\s", "").split(",");;
    	
    	if (userChoice == 1)  {
    		boolean nodeExist = nodeCheck(userLine[0],userLine[1]);
    		if (userLine.length != 3 || nodeExist == false) {
    			IllegalArgumentException stopNow = new IllegalArgumentException();
    			throw stopNow; }
    		else {
        		this.nodeOne = userLine[0];
        		this.nodeTwo = userLine[1];
        		this.userWeight = userLine[2];
        		String userEdge = userLine[0]+userLine[1];
    
    			//checks user submitted edge against working edge to see if edge exists
    			for (Edge edge : guiMST.edgeIndex) {
    				String edgeName = edge.nodeEnds[0].getName() + edge.nodeEnds[1].getName();
    				String reverse = edge.nodeEnds[1].getName() + edge.nodeEnds[0].getName();
        			if (userEdge.equalsIgnoreCase(edgeName) || userEdge.equalsIgnoreCase(reverse)) {
            			IllegalArgumentException stopNow = new IllegalArgumentException();
            			throw stopNow;
        			}}}}
    	if (userChoice == 2) {
    		if (userLine.length != 2) {
    			IllegalArgumentException stopNow = new IllegalArgumentException();
    			throw stopNow; }
    		else {
            	this.nodeOne = userLine[0];
            	this.nodeTwo = userLine[1];   	
    		}}
    	if (userChoice == 3) {
    		if (userLine.length != 1) {
    			IllegalArgumentException stopNow = new IllegalArgumentException();
    			throw stopNow; }
    		else {
            	this.nodeOne = userLine[0];
    		}}}
    
	/*
	 * Accepts string and appends to StringBuilder
	 * Utilized to update textArea visible to user
	 */
	private void stringUpdate(String updateEvent) {
		guiMST.consoleOutputSB.append(updateEvent);
		guiMST.consoleOutputSB.append("\n");
		guiMST.consoleOutputSB.append("\n");
		edgeDisplay.setText(guiMST.consoleOutputSB.toString());
	}
	
	/* Checks nodelist to make sure nodes are present
	 * @returns true if they are, false if they arent
	 */
	
	private boolean nodeCheck(String node1, String node2) {
		boolean testOne = false;
		boolean testTwo = false;
		for (Node node : guiMST.nodeIndex) {
			if (node1.equalsIgnoreCase(node.getName())) {
				testOne = true;}
			if (node2.equalsIgnoreCase(node.getName())) {
				testTwo = true;}}
		if (testOne == true && testTwo == true) {
			return true;
			}
		if (testOne == true && node2 == null) {
			return true;
		}
		else {
			return false;
		}}}