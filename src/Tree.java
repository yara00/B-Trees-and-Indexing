import java.util.LinkedList;
import java.util.Queue;

public class Tree implements IBTree{

    private int minDegree;
    private IBTreeNode root;

    public Tree(int minDegree) {
        this.minDegree = minDegree;
    }

    @Override
    public int getMinimumDegree() {
        return this.minDegree;
    }

    @Override
    public IBTreeNode getRoot() {
        return this.root;
    }

    @Override
    public void insert(Comparable key, Object value) {
        if(this.root == null) {
            this.root = new Node(2 * this.minDegree);
        }
        IBTreeNode temp = this.root;
        if(this.root.getNumOfKeys() == (2 * this.minDegree) - 1) {
            IBTreeNode node = new Node(2 * this.minDegree);
            this.root = node;
            node.setLeaf(false);
            node.setNumOfKeys(0);
            node.getChildren().add(temp);
            split(node, 0);
            insertNonFull(node, key, value);
        }
        else insertNonFull(temp, key, value);
    }

    private void split(IBTreeNode node, int index) {
        IBTreeNode newNode = new Node(2 * this.minDegree);
        IBTreeNode childAtIndex = (IBTreeNode) node.getChildren().get(index);
        newNode.setLeaf(childAtIndex.isLeaf());
        newNode.setNumOfKeys(this.minDegree - 1); // check
        for(int i=0; i< this.minDegree-1; i++) {
            newNode.getKeys().add(childAtIndex.getKeys().remove(this.minDegree));
            newNode.getValues().add(childAtIndex.getValues().remove(this.minDegree));
        }
        /*
        while(childAtIndex.getKeys().size() > childAtIndex.getNumOfKeys()) {
            newNode.getKeys().remove(newNode.getKeys().size()-1);
            newNode.getValues().remove(newNode.getValues().size()-1);
        }
         */
        if(!childAtIndex.isLeaf()) {
            for(int i=0; i<this.minDegree; i++) { // momken t-1
                newNode.getChildren().add(childAtIndex.getChildren().remove(this.minDegree));
            }

        }
        childAtIndex.setNumOfKeys(this.minDegree - 1);
        /*
        for(int i= node.getNumOfKeys(); i >= (index + 1); i--) {
            node.getChildren().add(i+1, node.getChildren().get(i));
        }
         */
        node.getChildren().add(index+1, newNode);
        /*
        for(int i=node.getNumOfKeys()-1; i >= index; i--) {
            node.getKeys().add(i + 1, node.getKeys().get(i));
            node.getValues().add(i + 1, node.getValues().get(i));

        }
         */
        node.getKeys().add(index, childAtIndex.getKeys().remove(this.minDegree - 1));
        node.getValues().add(index, childAtIndex.getValues().remove(this.minDegree - 1));
        node.setNumOfKeys(node.getNumOfKeys() + 1);
        /*
        while(node.getKeys().size() > node.getNumOfKeys()) {
            node.getKeys().remove(node.getKeys().size()-1);
            node.getValues().remove(node.getValues().size()-1);
        }

         */
    }

    private void insertNonFull(IBTreeNode node, Comparable key, Object value) {

        if(node.isLeaf()) {
            int numOfKeys = node.getNumOfKeys() - 1;
            while(numOfKeys >= 0 && key.compareTo(node.getKeys().get(numOfKeys)) < 0) {
               // node.getKeys().add(numOfKeys + 1, node.getKeys().get(numOfKeys));
              //  node.getValues().add(numOfKeys + 1, node.getValues().get(numOfKeys));
                numOfKeys--;
            }
            node.getKeys().add(numOfKeys + 1, key);
            node.getValues().add(numOfKeys + 1, value);
            node.setNumOfKeys(node.getNumOfKeys() + 1);
            /*
            while(node.getKeys().size() > node.getNumOfKeys()) {
                node.getKeys().remove(node.getKeys().size()-1);
                node.getValues().remove(node.getValues().size()-1);
            }

             */
        }
        else {
            int numOfKeys = node.getNumOfKeys() - 1;
            while (numOfKeys >= 0 && key.compareTo(node.getKeys().get(numOfKeys)) < 0) {
                numOfKeys--;
            }
            numOfKeys++;

            IBTreeNode child = (IBTreeNode) node.getChildren().get(numOfKeys);
            if (child.getNumOfKeys() == 2 * this.minDegree - 1) {
                split(node, numOfKeys);
                if (key.compareTo(node.getKeys().get(numOfKeys)) > 0) {
                    numOfKeys++;
                }
            }

            insertNonFull((IBTreeNode) node.getChildren().get(numOfKeys), key, value);
        }
    }
    @Override
    public Object search(Comparable key) {

        IBTreeNode temp = root;

        while (temp != null ) {
            int index = temp.search(key);
            if (index < temp.getKeys().size() && key.compareTo(temp.getKeys().get(index)) == 0) {
                return temp;
            } else {

                if (temp.getChildren() != null && index < temp.getChildren().size() )

                    temp = (IBTreeNode) temp.getChildren().get(index);

                else return null;

            }
        }

        return null;
    }

    //I was un able to get the P without using a global variable
    static IBTreeNode parent;
    public IBTreeNode getParent(IBTreeNode parent,IBTreeNode child ,int i ){

        if (parent == null) return null;

        if (parent.getChildren().contains(child)){
            this.parent=parent;
            return parent;

        }
        else {

            while (i<parent.getChildren().size()) {
                int j = 0;
                while (j < parent.getChildren().size()) {
                    getParent((IBTreeNode) parent.getChildren().get(j), child, i++);
                    j++;
                }
            }

        }

        return null;
    }


    public Object getLeftSibling(IBTreeNode node){
        if (node==null)return null;
        getParent(root,node,0);
        int index = parent.getChildren().indexOf(node);
        if (index > 0){
            index-=1;
            return parent.getChildren().get(index);
        }
        return null;
    }

    public Object getRightSibling(IBTreeNode node){

        if (node==null)return null;
        getParent(root,node,0);
        int index = parent.getChildren().indexOf(node);
        if (index < parent.getChildren().size()-1){
            index+=1;
            return parent.getChildren().get(index);
        }
        return null;
    }

    /**
     * this function borrow a key from its left sibling
     */

    public void borrowFromLeft(IBTreeNode parentNode , IBTreeNode childNode){

        int i = parentNode.getChildren().indexOf(childNode);
        IBTreeNode leftSibling = (IBTreeNode) parentNode.getChildren().get(i-1);
       // int i = parent.getChildren().indexOf(node);
        //index of the maximum element

        parentNode.getKeys().add(i-1,leftSibling.getKeys().get(leftSibling.getKeys().size()-1));
        parentNode.getValues().add(i-1,leftSibling.getValues().get(leftSibling.getKeys().size()-1));

        childNode.getKeys().add(0,parentNode.getKeys().get(i));
        childNode.getValues().add(0,parentNode.getValues().get(i));

        parentNode.getKeys().remove(i);
        parentNode.getValues().remove(i);

        leftSibling.getKeys().remove(leftSibling.getKeys().size()-1);
        leftSibling.getValues().remove(leftSibling.getValues().size()-1);

        if(!leftSibling.isLeaf()) {
            childNode.getChildren().add(0, leftSibling.getChildren().get(leftSibling.getNumOfKeys()));
            leftSibling.getChildren().remove(leftSibling.getNumOfKeys());
        }

    }
    public void borrowFromRight(IBTreeNode parentNode,IBTreeNode childNode){

        int i = parentNode.getChildren().indexOf(childNode);

        IBTreeNode rightSibling = (IBTreeNode) parentNode.getChildren().get(i+1);

        //but the new value in the parent node
        parentNode.getKeys().add(i+1 , rightSibling.getKeys().get(0));
        parentNode.getValues().add(i+1 , rightSibling.getValues().get(0));

        childNode.getKeys().add( parentNode.getKeys().get(i) );
        childNode.getValues().add(  parentNode.getValues().get(i) );

        parentNode.getKeys().remove(i);
        parentNode.getValues().remove(i);

        rightSibling.getKeys().remove(0);
        rightSibling.getValues().remove(0);

        if(!rightSibling.isLeaf()) {

            childNode.getChildren().add(rightSibling.getChildren().get(0));
            rightSibling.getChildren().remove(0);

        }


    }

    public void borrow(IBTreeNode parentNode,IBTreeNode childNode){
       if (parentNode.getChildren().indexOf(childNode)==0)borrowFromRight(parentNode,childNode) ;

        else     borrowFromLeft(parentNode,childNode);


    }

    @Override
    public boolean delete(Comparable key) {
        IBTreeNode nodeContainingToBeRemovedKey = (IBTreeNode) search(key);
        if (nodeContainingToBeRemovedKey == null) return false;
        else {
            delete(root, key);
            return true;
        }
    }
    public boolean delete(IBTreeNode node,Comparable key) {

        int index = 0;
        boolean found = false;
        IBTreeNode merged;

        if (node.getKeys().contains(key)) {
            index = node.getKeys().indexOf(key);
            found = true;
        } else {
            while (index < node.getNumOfKeys() && key.compareTo(node.getKeys().get(index)) > 0)
                index++;

        }
        if (!found) {

            if (node.isLeaf())
                return false;//case we do not found the key and the noe to be deleted is leaf node

            IBTreeNode childNode = (IBTreeNode) node.getChildren().get(index);

            if (childNode.getNumOfKeys() == minDegree - 1) {

                if ((index - 1 >= 0 && ((IBTreeNode) node.getChildren().get(index - 1)).getNumOfKeys() > minDegree - 1)
                        || index + 1 <= node.getNumOfKeys() && ((IBTreeNode) node.getChildren().get(index + 1)).getNumOfKeys() > minDegree - 1) {
                    //this is the case where one of the parent of the nodeContainingToBeRemovedKey has minDegree
                    borrow(node, childNode);
                } else {

                    if (index > node.getKeys().size() - 1) {

                        merged = merge(childNode, (IBTreeNode) node.getChildren().get(index - 1));
                 /*       if (node == root) {
                            root = childNode;

                        }
*/
                    } else {

                        merged = merge(childNode, (IBTreeNode) node.getChildren().get(index + 1));

                    }

                    if (node == root && node.getNumOfKeys() == 0)

                        root = merged;
                }


            }
            return delete(childNode, key);
        }
        IBTreeNode leftSibling = (IBTreeNode) getLeftSibling(node);
        IBTreeNode rightSibling = (IBTreeNode) getRightSibling(node);

        if (node.isLeaf()) {
            if (node.getKeys().size() > this.minDegree-1) {

                node.getKeys().remove(index);
                node.getValues().remove(index);
                return true;
            }
            else {

                leftSibling = (IBTreeNode) getLeftSibling(node);
                rightSibling = (IBTreeNode) getRightSibling(node);


                /**
                 * If the left sibling has more than minDegree-1
                 * So we will borrow a key from if and remove the nodeToBeRemoved
                 * **/
                if (leftSibling != null &&  leftSibling.getKeys().size() > this.minDegree-1){
                    borrowFromLeft(node,leftSibling);

                    node.getKeys().remove(index);
                    node.getValues().remove(index);

                    return true;

                }
                /**
                 * If the left sibling has  not more than minDegree-1  we will check it's rightSibling if right Sibling has more than minDegree-1
                 * So we will borrow a key from if and remove the nodeToBeRemoved
                 * **/
                else if (  rightSibling != null && rightSibling.getKeys().size() > this.minDegree-1 ) {

                    borrowFromRight(node,rightSibling);

                    node.getKeys().remove(index);
                    node.getValues().remove(index);

                    return true;
                }
                /**
                 * This case when the both rightSibling and leftSibling  has no more than minDegree-1
                 * so in this case if there exist leftSibling we merge the leftSibling and node and the parent
                 * and put that node as new child then remove the leftSibling and node
                 *
                 * /////Whet if the parent node has not more than midDegree-1 or is not a root ???
                 * ///////////// here we should get a solution
                 * */
                else  {

                    //case both LC an RC has number of keys less than or equal minDegree
                    IBTreeNode nodeToBeMerged=leftSibling;
                    if (leftSibling==null) nodeToBeMerged=rightSibling;

                    if ( nodeToBeMerged != null ){

                        IBTreeNode newToChild = merge(nodeToBeMerged,node);
                        int i=0;

                        int newChildIndex =parent.getChildren().indexOf(nodeToBeMerged);
                        if (leftSibling==null) newChildIndex=0;
                        while ( i <  newToChild.getKeys().size() &&(int) newToChild.getKeys().get(i) < (int) parent.getKeys().get( parent.getChildren().indexOf(nodeToBeMerged) ) )  i++ ;

                        newToChild.getKeys().add( i , parent.getKeys().get(newChildIndex));
                        newToChild.getValues().add( i , parent.getValues().get(newChildIndex));



                        parent.getKeys().remove(newChildIndex);
                        parent.getValues().remove(newChildIndex);
                        parent.getChildren().remove(newChildIndex);

                        parent.getChildren().remove(newChildIndex);

                        parent.getChildren().add(newChildIndex , newToChild);


                        newToChild.getValues().remove(newToChild.getKeys().indexOf(key));
                        newToChild.getKeys().remove(key);

                        if (!( parent.getNumOfKeys() > minDegree-1 || parent == root)){

                            IBTreeNode parentOfThisNode = parent;

                            int indexOfChlidToBeTransfered = 0;

                            IBTreeNode toBorrowFrom = (IBTreeNode) getRightSibling(parentOfThisNode);

                            if (toBorrowFrom==null){

                                toBorrowFrom = (IBTreeNode) getLeftSibling(parentOfThisNode);
                                indexOfChlidToBeTransfered = toBorrowFrom.getChildren().size()-1;

                            }

                            if (toBorrowFrom.getKeys().size()>minDegree-1){

                                borrow(parentOfThisNode,toBorrowFrom);
                                if (indexOfChlidToBeTransfered!=0)
                                    parentOfThisNode.getChildren().add(0,toBorrowFrom.getChildren().get(indexOfChlidToBeTransfered));
                                else
                                    parentOfThisNode.getChildren().add(toBorrowFrom.getChildren().get(indexOfChlidToBeTransfered));

                                toBorrowFrom.getChildren().remove(indexOfChlidToBeTransfered);

                            } else {


                                //Now I am working in case of right merging
                                do {

                                    int j=0;
                                    if (indexOfChlidToBeTransfered!=0) {

                                        parentOfThisNode.getKeys().add(0,
                                                parent.getKeys().get(parent.getChildren().indexOf(parentOfThisNode) - 1));
                                        parentOfThisNode.getValues().add(0,
                                                parent.getValues().get(parent.getChildren().indexOf(parentOfThisNode) - 1));

                                        parent.getKeys().remove(parent.getChildren().indexOf(parentOfThisNode)-1);
                                        parent.getValues().remove(parent.getChildren().indexOf(parentOfThisNode)-1);

                                    }else {
                                        parentOfThisNode.getKeys().add(
                                                parent.getKeys().get(parent.getChildren().indexOf(parentOfThisNode)));
                                        parentOfThisNode.getValues().add(
                                                parent.getValues().get(parent.getChildren().indexOf(parentOfThisNode)));

                                        parent.getKeys().remove(parent.getChildren().indexOf(parentOfThisNode));
                                        parent.getValues().remove(parent.getChildren().indexOf(parentOfThisNode));

                                    }

                                    while (j < toBorrowFrom.getKeys().size()) {


                                        if (indexOfChlidToBeTransfered != 0) {

                                            parentOfThisNode.getChildren().add(0, toBorrowFrom.getChildren().get(toBorrowFrom.getChildren().size()-1-j));

                                            parentOfThisNode.getKeys().add(0, toBorrowFrom.getKeys().get(toBorrowFrom.getKeys().size()-1-j));
                                            parentOfThisNode.getValues().add(0, toBorrowFrom.getValues().get(toBorrowFrom.getValues().size()-1-j));

                                        }
                                        else {
                                            parentOfThisNode.getChildren().add( toBorrowFrom.getChildren().get(j));

                                            parentOfThisNode.getKeys().add( toBorrowFrom.getKeys().get(j));
                                            parentOfThisNode.getValues().add( toBorrowFrom.getValues().get(j));
                                        }

                                        j++;

                                    }

                                    if (indexOfChlidToBeTransfered != 0) {

                                        parentOfThisNode.getChildren().add(0, toBorrowFrom.getChildren().get(toBorrowFrom.getChildren().size()-1-j));

                                    }
                                    else
                                        parentOfThisNode.getChildren().add( toBorrowFrom.getChildren().get(j));


                                    parent.getChildren().remove(toBorrowFrom);

                                    if ( parent.getKeys().size()==0 && parent==root ){

                                        root=parentOfThisNode;
                                        break;

                                    }

                                    parentOfThisNode = parent;

                                    indexOfChlidToBeTransfered = 0;

                                    toBorrowFrom = (IBTreeNode) getRightSibling(parentOfThisNode);

                                    if (toBorrowFrom==null){
                                        toBorrowFrom = (IBTreeNode) getLeftSibling(parentOfThisNode);
                                        indexOfChlidToBeTransfered = toBorrowFrom.getChildren().size()-1;
                                    }

                                } while (parentOfThisNode!=root);


                        }
                        return true;


                    }
                 }
               }
            }
        }
        else {
            index = node.getKeys().indexOf(key);
            IBTreeNode leafChild =null;
            IBTreeNode leftChild =inOrderPredecessor(node,key);
            IBTreeNode rightChild = inOrderSuccessor(node,key);
            int indexOfKeyInLeaf=-1;
            if ( leftChild.getKeys().size() > minDegree-1)   {
                leafChild =inOrderPredecessor(node,key);
                indexOfKeyInLeaf=leafChild.getKeys().size()-1;
            }
            else if (rightChild.getKeys().size() > minDegree-1 ) {
                leafChild = inOrderSuccessor(node, key);
                indexOfKeyInLeaf = 0;
            }
            if (leafChild!=null && leafChild.getKeys().size() > minDegree-1){
                //remove a key from non leaf node using  inOrderPredecessor or inOrderSuccessor
                node.getKeys().remove(index);
                node.getValues().remove(index);

                node.getKeys().add(index,leafChild.getKeys().get(indexOfKeyInLeaf));
                node.getValues().add(index,leafChild.getValues().get(indexOfKeyInLeaf));

                leafChild.getKeys().remove(indexOfKeyInLeaf);
                leafChild.getValues().remove(indexOfKeyInLeaf);


                return true;


            }

            //if non of Lc nor Rc has more than minimum number of keys ??
            //we merge Rc and Lc then remove the p key
            /**
             * case where both RC and LC have less than minDegree
             * we merge both left and right child in case those are leaf nodes
             * */
            else {

                // this case is not working i should solve it's problem
                node.getKeys().remove(index);
                node.getValues().remove(index);

                node.getChildren().remove(index);

                node.getChildren().set(index, merge(leftChild,rightChild));

                return true;
            }
        }


        return true;
    }
    public IBTreeNode inOrderSuccessor(IBTreeNode node,Comparable key){
        IBTreeNode temp =node;
        int i = temp.getKeys().indexOf(key);
        while (temp.getChildren().size()!=0){
            temp= (IBTreeNode) temp.getChildren().get(i+1);
            i=-1;
        }
        return temp;
}
    public IBTreeNode inOrderPredecessor(IBTreeNode node , Comparable key){
        IBTreeNode temp =node;
        int i = temp.getKeys().indexOf(key);

        while (temp.getChildren().size() != 0){

            temp= (IBTreeNode) temp.getChildren().get(i);
            i=temp.getChildren().size()-1;

        }
        return temp;
    }

    public void Show(IBTreeNode x) {
        int level = 0;
        Queue<IBTreeNode> nodes = new LinkedList<>();
        Queue<Integer> levels = new LinkedList<>();
        nodes.add(root);
        levels.add(level);
        System.out.print("Level 0: ");
        while (!nodes.isEmpty()) {
            IBTreeNode cur = nodes.poll();
            int curlevel = levels.poll();

            if (level < curlevel)  {
                level = curlevel;
                System.out.print("\nLevel " + level + ": ");
            }
            printNode(cur);
            for (Object child: cur.getChildren()) {
                nodes.add((IBTreeNode) child);
                levels.add(level+1);
            }
        }
        System.out.println();
    }

    public IBTreeNode merge(IBTreeNode node1, IBTreeNode node2) {
        if (node1==null)return node2;
        if (node2==null)return node1;
        int i=0;
        int j=0;

        IBTreeNode merged= new Node(node1.getChildrenNum());

        while ( i < node1.getKeys().size() && j < node2.getKeys().size()) {

            if ((int) node1.getKeys().get(i) <= (int) node2.getKeys().get(j)) {

                merged.getKeys().add(node1.getKeys().get(i));
                merged.getValues().add(node1.getValues().get(i));
                if (!node1.isLeaf()){

                    merged.getChildren().add(node1.getChildren().get(i));

                    merged.setLeaf(false);
                }

                i++;
                continue;
            } else {
                merged.getKeys().add(node2.getKeys().get(j));
                merged.getValues().add(node2.getValues().get(j));

                if (!node2.isLeaf())
                {
                    merged.getChildren().add(node2.getChildren().get(j));
                    if (j == node2.getKeys().size()-1){
                        merged.getChildren().add(node2.getChildren().get(j+1));
                    }
                }
                j++;
                continue;

            }
        }

        while ( i < node1.getKeys().size()){

            merged.getKeys().add(node1.getKeys().get(i));
            merged.getValues().add(node1.getValues().get(i));
            if (!node1.isLeaf()){

                merged.getChildren().add(node1.getChildren().get(i));
            }
            i++;
        }

        while (j < node2.getKeys().size()){
            merged.getKeys().add(node2.getKeys().get(j));
            merged.getValues().add(node2.getValues().get(j));
            if (!node2.isLeaf())
            {
                merged.getChildren().add(node2.getChildren().get(j));
                if (j == node2.getKeys().size()-1){
                    merged.getChildren().add(node2.getChildren().get(j+1));
                }
            }
            j++;
        }

        return merged;
}
    @Override
    public Object tempSearch(Comparable key) {
        IBTreeNode node = (IBTreeNode) this.search(key);
        if(node == null) return null;

        for(int i=0; i<node.getNumOfKeys(); i++) {
            if(key.compareTo(node.getKeys().get(i)) == 0) return node.getValues().get(i);
        }
        return null;
    }

    public void print() {

    }

    private void printNode(IBTreeNode node) {
        System.out.print("[");
        for (Object key : node.getKeys())
            System.out.print(key+",");
        System.out.print("]");
    }

    public static void main(String[] args) {

        IBTree tree = new Tree(2);
        for(int i = 2 ; i < 22; i+=2){
            tree.insert(i, "a"+i);
        }


        tree.Show(tree.getRoot());

      //  tree.insert(15,"Wfg");

        System.out.println("Delete 4 ");
        tree.delete(4);
        tree.Show(tree.getRoot());
        System.out.println("Delete 2 ");
        tree.delete(2);
        tree.Show(tree.getRoot());
        System.out.println("Delete 20 ");

        tree.delete(20);
        tree.Show(tree.getRoot());
        System.out.println("Delete 18 ");

        tree.delete(18);
        tree.Show(tree.getRoot());
        System.out.println("Delete 16 ");

        tree.delete(16);
        tree.Show(tree.getRoot());





    }
}