
public class MinHeap {

    public int heapSize = 0;
    public HeapNode[] buildings;//Array that holds buildings

    //Insert the new building at the end and percolate it up
    public void insert(HeapNode p) {
        int i = heapSize;
        heapSize++;
        buildings[i] = p;
        while(i != 0 && buildings[i].executionTime <= buildings[(i-1)/2].executionTime) {
            if(buildings[i].executionTime < buildings[(i-1)/2].executionTime || buildings[i].rbNode.buildingNumber < buildings[(i-1)/2].rbNode.buildingNumber) {
                swap(i, (i-1)/2);
                i = (i-1)/2;
            } else {
                break;
            }
        }
    }

    /*Percolate Down we do this after extracting minimum 
      and we replace root with last element and bring it down*/
    private void heapify(int i) {
        int left = i*2 + 1;
        int right = i*2 + 2;
        int smallest = i;
        if(left < heapSize && buildings[i].executionTime >= buildings[left].executionTime && (buildings[i].executionTime > buildings[left].executionTime || buildings[i].rbNode.buildingNumber > buildings[left].rbNode.buildingNumber)) {
            smallest = left;
        }
        if(right < heapSize && buildings[smallest].executionTime >= buildings[right].executionTime && (buildings[smallest].executionTime > buildings[right].executionTime || buildings[smallest].rbNode.buildingNumber > buildings[right].rbNode.buildingNumber)) {
            smallest = right;
        }

        if (smallest != i && buildings[i].executionTime >= buildings[smallest].executionTime &&
                (buildings[i].executionTime > buildings[smallest].executionTime || buildings[i].rbNode.buildingNumber > buildings[smallest].rbNode.buildingNumber)) {
            swap(i, smallest);
            heapify(smallest);
        }
    }

    /*Extract Minimum i.e building with least execution time in heap*/
    public HeapNode getBuilding() {
        if (heapSize == 1) {
            HeapNode min = buildings[0];
            heapSize--;
            buildings[0] = null;
            return min;
        }
        HeapNode min = buildings[0];
        buildings[0] = buildings[heapSize-1];
        buildings[heapSize-1] = null;
        heapSize--;
        heapify(0);
        return min;
    }
    
  /*swap buildings*/
    private void swap(int i, int j) {
        HeapNode temp = buildings[i];
        buildings[i] = buildings[j];
        buildings[j] = temp;
    }

    public MinHeap(){
        buildings = new HeapNode[2001];
    }

    public boolean isEmpty(){
        return heapSize == 0;
    }
}