
import java.io.*;
import java.util.List;
import java.util.Scanner;

public class wayneConstruction {
    private RedBlackTree tree = new RedBlackTree();// Create a new instance of RBt
    private MinHeap heap = new MinHeap();// Create a new instance of MinHeap
    private HeapNode currentBuilding =null;//Current building being executed
    private int localTime = 0;//local timer to match with global command time
    private int currentSlotEndTime = 0;//Time at which the 5s slot will end
    private int currentBuildingCompletionTime = 0;//Time at which the current building being executed will end if continued indefinitely
    
  /*Main function contains calls to perform action for corresponding input command
    when local time matches with global time it reads input and triggers corresponding command(Insert,Print)
    If not it works on previously inserted buildings.*/
    public void wayneConstructionProject(String[] args) {
    	PrintStream o= null;
        try {
        	File file = new File(args[0]); 
			o = new PrintStream(new File("output_file.txt")); 
			System.setOut(o); 
    		Scanner sc= new Scanner(file); 
    		String st=sc.nextLine();
            String[] inputValuePair;
            while (st != null) {
                int globalTime =Integer.parseInt((st.substring(0,st.indexOf(":"))));
                if (st!=null) {                	
                	String inputValue=st.substring(st.indexOf("(")+1,st.indexOf(")"));
                    inputValuePair = inputValue.split(","); 
                    //If no input at local time then work on previously inserted buildings and update time.
                    while (globalTime != localTime){
                        Construction();
                        incrementLocalTime();
                    }
                    //If command is insert
                    if(st.toLowerCase().indexOf("insert")!=-1) {
                    	if(tree.getBuilding(Integer.parseInt(inputValuePair[0]))==null)
                    	{
                    	constructBuilding(inputValuePair);}
                    	//Duplicate insertion for building already present so terminate.
                    	else {
                    		System.out.println("Duplicate insertion for building number:"+inputValuePair[0]+" at time:"+st.substring(0,st.indexOf(":"))+" so stoping");
                    		return;
                    	}
                    }
                    //if command is PrintBuilding/Print, here procedure name is printBuilding.
                    else {
                            printBuilding(inputValuePair);
                        }
                }
                //checking for next command 
                if(sc.hasNextLine()){
					st=sc.nextLine();
					globalTime=Integer.parseInt((st.substring(0,st.indexOf(":"))));
				}
				else{
					st=null;
				}
            }
            //Now, we have no inputs but we have buildings which are not yet completed so finish them.
            finalPhaseConstruction();
            //Printing the final completion time.
            System.out.println("RaisingCity construction by WayneConstructions has been successfully completed on:"+ Integer.toString(localTime-1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Process the remaining buildings after all input commands are processed.
    private void finalPhaseConstruction() throws IOException {
        while (currentBuilding != null){
            Construction();
            incrementLocalTime();
        }
    }

    //Increment localTime and current building execution time.
    private void incrementLocalTime() {
        localTime=localTime+1;
        if (currentBuilding != null) {
            currentBuilding.executionTime = currentBuilding.executionTime + 1;
        }
    }

 /* Get building with least execution time and work on it.
    If building is completed delete from tree and update the current building info.
    If not completed re insert into heap*/
    private void Construction() throws IOException {
    	//& initially when 1 node is inserted still currentbuilding will be empty
        if (currentBuilding == null) {
            if (!heap.isEmpty()) {
                currentBuilding = heap.getBuilding();
                currentSlotEndTime = localTime+5;
                currentBuildingCompletionTime = localTime + currentBuilding.rbNode.totalTime - currentBuilding.executionTime;
            }
        }
        else {
        	//Checking if building completion time is within slot finish time
            if (currentBuildingCompletionTime <= currentSlotEndTime){
            	//checking if building is completed or not if yes then delete from tree and reset current building information.
                if (localTime == currentBuildingCompletionTime){
                	RBTNode bd=tree.getBuilding(currentBuilding.rbNode.buildingNumber);
                	System.out.println("("+ bd.buildingNumber+"," + localTime+")");
                    //printBuildingHelper(tree.getBuilding(currentBuilding.rbNode.buildingNumber), localTime);
                    tree.delete(currentBuilding.rbNode.buildingNumber);
                    currentBuilding = null;
                    currentSlotEndTime = 0;
                    currentBuildingCompletionTime = 0;
                    Construction();
                }
            }
            //if not slot ends
            else {
                if (localTime == currentSlotEndTime){
                    //again insert into heap and reset current building information.
                    heap.insert(currentBuilding);
                    currentBuilding = null;
                    currentSlotEndTime = 0;
                    currentBuildingCompletionTime = 0;
                    //work on other buildings
                    Construction();
                }
            }
        }
    }

  /*If parameters to print function contains only 1 value then print only that building
    If it contains 2 values the print all buildings in the range of 2 input values including them.
    If nothing found then print empty record.*/
    private void printBuilding(String[] inputValuePair) throws IOException {
    	// if only 1 input print that building
        if (inputValuePair.length == 1){
            int buildingNum = Integer.parseInt(inputValuePair[0]);
            RBTNode rbNode = tree.getBuilding(buildingNum);
            if (rbNode == null) {
            	System.out.println("(0,0,0)");
            }
            else {
                printBuildingHelper(rbNode);
            }
        }
        //if 2 inputs print buildings in that range
        else {
            int building1 = Integer.parseInt(inputValuePair[0]);
            int building2 = Integer.parseInt(inputValuePair[1]);
            List<RBTNode> list = tree.getBuildingsInRange(building1, building2);//Search in tree
            if (!list.isEmpty()){
                StringBuilder buildings = new StringBuilder();
                for (RBTNode node: list){
                    buildings.append("("+node.buildingNumber+","+node.heapNode.executionTime+","+node.totalTime + "),");
                }
                //removing last comma
                buildings.deleteCharAt(buildings.length()-1);
                System.out.println(buildings.toString());
            }
            else {
            	System.out.println("(0,0,0)");
            }
        }
    }


  /*Here we are using this function because we have 2 possibilities here
    If building to be printed is not current building then we can have its execution time from heap.
    If building to be printed is current building then we cannot have its execution time from heap
    as it was extracted so we have to use current building execution time.*/
    private void printBuildingHelper(RBTNode node) throws IOException {
        if (node.buildingNumber == currentBuilding.rbNode.buildingNumber) {            
            System.out.println("("+ node.buildingNumber+"," + currentBuilding.executionTime+"," + node.totalTime + ")");
        } else {            
            System.out.println("(" + node.buildingNumber + "," + node.heapNode.executionTime + "," + node.totalTime + ")");
        }
    }

  /*Called we have to insert, here we create nodes to both MinHeap and RBt
    Maintains a pointer to each other*/
    private void constructBuilding(String[] inputValuePair) {
        int num = Integer.parseInt(inputValuePair[0]);
        int tTime = Integer.parseInt(inputValuePair[1]);
        RBTNode rbNode = new RBTNode(num);
        rbNode.totalTime = tTime;
        HeapNode heapNode = new HeapNode(0);
        rbNode.heapNode = heapNode;
        heapNode.rbNode = rbNode;
        tree.insertNode(rbNode);
        heap.insert(heapNode);
    }
}