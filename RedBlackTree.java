
import java.util.LinkedList;
import java.util.List;

public class RedBlackTree {

    public RBTNode nil;
    public RBTNode root;

    public enum COLOR {
        RED, BLACK
    }

    public RedBlackTree() {
        nil = RBTNode.nil;
        root = nil;
        root.left = nil;
        root.right = nil;
    }

    public RBTNode getBuilding(int buildingNumber){
    	// calling helper with root and buildingNumber
        return getBuilding(root, buildingNumber);
    }

    /* BST search*/
    private RBTNode getBuilding(RBTNode root, int buildingNumber) {
        if (root == nil) {
            return null;
        }
        if (root.buildingNumber == buildingNumber) {
            return root;
        }
        else if (buildingNumber < root.buildingNumber) {
            return getBuilding(root.left, buildingNumber);
        }
        else {
            return getBuilding(root.right, buildingNumber);
        }
    }
    
    /* Get list of buildings in the range of bNum1 and bNum2*/
    public List<RBTNode> getBuildingsInRange(int buildingNumber1, int buildingNumber2) {
        List<RBTNode> list = new LinkedList<RBTNode>();
        // calls helper with root and list used for print buildings
        getBuildingsInRange(root, list, buildingNumber1, buildingNumber2);
        return list;
    }

    /* search for nodes between bNum1 and bNum2*/
    private void getBuildingsInRange(RBTNode root, List<RBTNode> list, int buildingNumber1, int buildingNumber2) {
        if (root == nil) {
            return;
        }
        if (buildingNumber1 < root.buildingNumber) {
            getBuildingsInRange(root.left, list, buildingNumber1, buildingNumber2);
        }
        // if building found in range add to list
        if (buildingNumber1 <= root.buildingNumber && buildingNumber2 >= root.buildingNumber) {
            list.add(root);
        }

        if (buildingNumber2 > root.buildingNumber) {
            getBuildingsInRange(root.right, list, buildingNumber1, buildingNumber2);
        }
    }

  /*Right rotate and update parent and children*/
    private RBTNode rotateRight(RBTNode pp) {
    	//figuring out what node will be pp will have on left
    	//it will contain p's right child
        RBTNode p =pp.left;
        RBTNode pr = p.right;
        pp.left = pr;
        if(pr != nil) {
            pr.parent = pp;//setting prc parent to pp
        }
        p.parent = pp.parent;//setting p's new parent to gp or pp's previous parent
        if (pp.parent == nil){
            root = p;//if no gp then p is root
        }
        //if gp is not null then i have to update its child info
        else if (pp == pp.parent.left){
            pp.parent.left = p;//if pp was of left of gp then set p to be gp left child
        }
        else {
            pp.parent.right = p;//if pp was of right of gp then set p to be gp right child
        }
        p.right = pp;//set p's right child to pp
        pp.parent = p;//setting pp's parent to p
        return p;//returning p now gpl-->pr-->pp
    }

  /*Rotate left and update parent and children*/
    private RBTNode rotateLeft(RBTNode pp) {
    	//figuring out what node will pp will have on its right
    	//it will contain p's left child
        RBTNode p = pp.right;//fetching p
        RBTNode pl = p.left;//fetching left child of p

        pp.right = pl;//assigning it to pp's right child
        if(pl != nil) {
            pl.parent = pp;//if p had left child, now changing its parent to pp
        }
        p.parent = pp.parent;//setting p new parent to be pp's parent gp
        if (pp.parent == nil) {
            root = p;//if gp was nil then p will be root
        }
        //have to change gp child information also
        else if (pp == pp.parent.left) {
            pp.parent.left = p;//if pp was on left of gp then gp's left will be p
        }
        else {
            pp.parent.right = p;//if pp was on right of gp then gp's right will be p
        }
        p.left = pp;//changing p's left to be pp
        pp.parent = p;//setting pp's parent to be p now
        return p;//returning p now gpl-->pl-->pp
    }

    public void insert(int buildingNumber) {
        RBTNode p = new RBTNode(buildingNumber);
        insertNode(p);
    }

    /*calls two more function 1 for inserting into rbt and other for fixing violations if any*/
    public void insertNode(RBTNode p) {
        p.color = COLOR.RED;
        if (root == nil || root.buildingNumber == p.buildingNumber) {
            root = p;
            root.color = COLOR.BLACK;
            root.parent = nil;
            return;
        }
        //BST insert
        insertHelper(root, p);
        //Fix RBT
        insertFix(p);
    }
    /* inserts node into rbt*/
    private void insertHelper(RBTNode root, RBTNode p) {
        if (p.buildingNumber < root.buildingNumber) {
            if (root.left == nil) {
                root.left = p;
                p.parent = root;
            }
            else {
                insertHelper(root.left, p);
            }
        }
        else {
            if (root.right == nil) {
                root.right = p;
                p.parent = root;
            }
            else {
                insertHelper(root.right, p);
            }
        }
    }
    /* fixes if any voilations occurred during insertion*/
    private void insertFix(RBTNode p) {
        RBTNode pp = nil;
        RBTNode gp = nil;
        if (p.buildingNumber == root.buildingNumber) {
            p.color = COLOR.BLACK;
            return;
        }
		//we will terminate once any of the condition violates means no violation in RBT
        while (root.buildingNumber != p.buildingNumber && p.color != COLOR.BLACK && p.parent.color == COLOR.RED){
            pp = p.parent;
            gp = pp.parent;
            //if p is on left side of gp
            if (pp == gp.left) {
                RBTNode y = gp.right;
                //Case when uncle is red
                if (y != nil && y.color == COLOR.RED) {
                    //Recolor
                    gp.color = COLOR.RED;
                    pp.color = COLOR.BLACK;
                    y.color = COLOR.BLACK;
                    //Update current Node repeat until we reach root
                    p = gp;
                }
                else {
                    //Case when uncle is Black
                    //LRb Case when p is on right of pp
					//if LRb we make it to LLb then rotate right and swap colors.
                    if (pp.right == p) {
                        pp = rotateLeft(pp);//making p to be pp
                        p = pp.left;//pp as p
                    }
                    //LLb Case when p is on left of pp
                    rotateRight(gp);//made LRb to be LLb now rotate right around gp
                    swapColors(pp, gp);//if LRb was present the pp will be p now or else old pp
                    p = pp; //restoring p which is in pp back to p again
                }

            }
            //if p is on right side of gp
            else if (pp == gp.right) {
                RBTNode u = gp.left;
                //Case when sibling of parent is red
                if (u != nil && u.color == COLOR.RED) {
                    //Recolor
                    gp.color = COLOR.RED;
                    pp.color = COLOR.BLACK;
                    u.color = COLOR.BLACK;
                    //Update current Node repeat until we reach root
                    p = gp;
                }
                else {
                    //Case when uncle is Black
                    //if p is on left of pp
                    if (pp.left == p){
                        pp = rotateRight(pp);
                        p = pp.right;
                    }
                    //if p is on right of pp
                    rotateLeft(gp);
                    swapColors(pp,gp);
                    p = pp;
                }
            }
        }
        root.color = COLOR.BLACK;
    }

    /* swap both buildings*/
    private void Transplant(RBTNode a, RBTNode b) {
        if (a.parent == nil) {
            root = b;
        }
        else if (a == a.parent.left) {//checking node to be deleted is on which side of parent
        	//so as to insert it other child on that side
            a.parent.left = b;
        }
        else {
            a.parent.right = b;
        }
        b.parent = a.parent;//setting its parent to like gp
    }
    /* delete as bst if red or else fix*/
    public boolean delete(int buildingNumber) {
        RBTNode deleteNode = getBuilding(root,buildingNumber);
        if (deleteNode == null) {
            return false;
        }
        RBTNode v;
        RBTNode temp = deleteNode;
        COLOR origColor = deleteNode.color;
        //If left child is nil
        if (deleteNode.left == nil) {
            v = deleteNode.right;
            //swap with right child
            Transplant(deleteNode, deleteNode.right);
        }
        //If right child is nil
        else if (deleteNode.right == nil) {
            v = deleteNode.left;
            //swap with left child
            Transplant(deleteNode, deleteNode.left);
        }
        //If both children are not nil
        else {
        	RBTNode r=deleteNode.right;
        	//getting predecessor
        	while (r.left != nil){
                r = r.left;
            }
        	temp=r;
            origColor = temp.color;
            v = temp.right;
            //temp is root of deleteNode.right subtree
            if (temp.parent == deleteNode) {
                v.parent = temp;
            }
            //temp is root of deleteNode.right subtree so transplant with temp.right
            else {
            	Transplant(temp, temp.right);
                temp.right = deleteNode.right;
                temp.right.parent = temp;
            }
            Transplant(deleteNode, temp);
            temp.left = deleteNode.left;
            temp.left.parent = temp;
            temp.color = deleteNode.color;
        }
        //if deleted node color is red no violations, if not fix
        if (origColor == COLOR.BLACK) {
            deleteFix(v);
        }
        return true;
    }

    /* contains code for handling deletion fixes , double blackness*/
    private void deleteFix(RBTNode py){
        //loop until py is root or py is red
        while(py!=root && py.color == COLOR.BLACK) {
            //If py is left child of ppy
            if(py == py.parent.left) {
                //v holds the ppy's right child
                RBTNode v = py.parent.right;
                //if v is red, left rotation is required
                //case3.2 not terminal left rotation color swap of sibling with parent
                if(v.color == COLOR.RED) {
                    v.color = COLOR.BLACK;
                    py.parent.color = COLOR.RED;
                    rotateLeft(py.parent);
                    v = py.parent.right;
                }
                //if v's both children are black, recolor
                //case 3.3 siblings color to red and double black to parent
                if(v.left.color == COLOR.BLACK && v.right.color == COLOR.BLACK) {
                    v.color = COLOR.RED;
                    py = py.parent;
                    continue;
                }
                //if only right child is black, recolor and right rotate
                //case 3.5 siblings left child is red right rotate color swap left child with uncle
                else if(v.right.color == COLOR.BLACK) {
                    v.left.color = COLOR.BLACK;
                    v.color = COLOR.RED;
                    rotateRight(v);
                    v = py.parent.right;
                }
                //if v's right child is red, recolor and left rotate
                //case 3.6 terminal
                if(v.right.color == COLOR.RED) {
                    v.color = py.parent.color;
                    py.parent.color = COLOR.BLACK;
                    v.right.color = COLOR.BLACK;
                    rotateLeft(py.parent);
                    py = root;
                }
            //if right child same as symmetric to left
            } else {
                RBTNode v = py.parent.left;
                if(v.color == COLOR.RED) {
                    v.color = COLOR.BLACK;
                    py.parent.color = COLOR.RED;
                    rotateRight(py.parent);
                    v = py.parent.left;
                }
                if(v.right.color == COLOR.BLACK && v.left.color == COLOR.BLACK) {
                    v.color = COLOR.RED;
                    py = py.parent;
                    continue;
                }
                else if(v.left.color == COLOR.BLACK) {
                    v.right.color = COLOR.BLACK;
                    v.color = COLOR.RED;
                    rotateLeft(v);
                    v = py.parent.left;
                }
                if(v.left.color == COLOR.RED) {
                    v.color = py.parent.color;
                    py.parent.color = COLOR.BLACK;
                    v.left.color = COLOR.BLACK;
                    rotateRight(py.parent);
                    py = root;
                }
            }
        }
        //py should be black
        //case 3.1 node to be deleted is black and root and 3.4 deleted is black with red parent
        py.color = COLOR.BLACK;
    }

    private void swapColors(RBTNode pp, RBTNode gp) {
        COLOR temp = pp.color;
        pp.color = gp.color;
        gp.color = temp;
    }

}