
public class RBTNode {

    public static final RBTNode nil = new RBTNode(-1, RedBlackTree.COLOR.BLACK);

    public int buildingNumber;
    public int totalTime;
    public RBTNode left = nil;
    public RBTNode right = nil;
    public RBTNode parent = nil;
    public RedBlackTree.COLOR color;
    public HeapNode heapNode;//Object reference to heapNode

    public RBTNode(int buildingNumber, HeapNode heapNode){
        this.buildingNumber = buildingNumber;
        this.heapNode = heapNode;
    }

    public RBTNode(int buildingNumber){
        this.buildingNumber = buildingNumber;
    }

    public RBTNode(int buildingNumber, RedBlackTree.COLOR color){
        this.buildingNumber = buildingNumber;
        this.color = color;
    }

    @Override
    public String toString() {
        return "Key:"+this.buildingNumber+",Color:"+this.color.name();
    }
}