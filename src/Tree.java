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
        if (parent == null)return null;

        if (parent.getChildren().contains(child)){
            this.parent=parent;
            return parent;

        }
        else {

            while (i < parent.getChildren().size())
                getParent((IBTreeNode) parent.getChildren().get(i) , child,++i);

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

    public void borrowFromLeft(IBTreeNode node){

        IBTreeNode leftSibling = (IBTreeNode) getLeftSibling(node);
        int i=leftSibling.getKeys().size()-1;
        //index of the maximum element

        parent.getKeys().add(leftSibling.getKeys().get(i));
        parent.getValues().add(leftSibling.getValues().get(i));

        node.getKeys().add(parent.getKeys().get(1));
        node.getValues().add(parent.getValues().get(1));

        parent.getKeys().remove(1);
        parent.getValues().remove(1);

        leftSibling.getKeys().remove(leftSibling.getKeys().size()-1);
        leftSibling.getValues().remove(leftSibling.getValues().size()-1);


    }
    public void borrowFromRight(IBTreeNode node){

        IBTreeNode rightSibling = (IBTreeNode) getRightSibling(node);
        parent.getKeys().add(rightSibling.getKeys().get(0));
        parent.getValues().add(rightSibling.getValues().get(0));

        node.getKeys().add(parent.getKeys().get(parent.getKeys().size()-2 ) );
        node.getValues().add(parent.getValues().get( parent.getValues().size()-2));

        parent.getKeys().remove(parent.getKeys().size()-2 );
        parent.getValues().remove(parent.getValues().size()-2);

        rightSibling.getKeys().remove(0);
        rightSibling.getValues().remove(0);


    }

    public void borrow(IBTreeNode node){
        if (getLeftSibling(node)!=null)
            borrowFromLeft(node);
        else borrowFromRight(node);

    }
    @Override
    public boolean delete(Comparable key) {

        IBTreeNode nodeContainingToBeRemovedKey = (IBTreeNode) search(key);
        if(nodeContainingToBeRemovedKey!=null ){

            int index =nodeContainingToBeRemovedKey.getKeys().indexOf(key);

            if (nodeContainingToBeRemovedKey.isLeaf()){
                //the  case that should be done when the key is in a leaf node
                //when we borrow from a parent and that parent will have less than min

                if (nodeContainingToBeRemovedKey.getKeys().size() > this.minDegree-1){
                    //1st case we remove the key from the leave node
                    //and the number of keys are more than min degree
                    nodeContainingToBeRemovedKey.getKeys().remove(key);
                    nodeContainingToBeRemovedKey.getValues().remove(index);

                    return true;

                }

                else {
                    //2nd case we remove the key from the leave node
                    //and the number of keys are equal or less than min degree
                    IBTreeNode leftSibling = (IBTreeNode) getLeftSibling(nodeContainingToBeRemovedKey);
                    IBTreeNode rightSibling = (IBTreeNode) getRightSibling(nodeContainingToBeRemovedKey);

                    if (leftSibling != null &&  leftSibling.getKeys().size() > this.minDegree-1){

                        //we see if there exist a left sibling and if that sibling has more than
                        //the min element in the keys list
                        // so we will but the maximum of that sibling in the parent node
                        //and put the parent in the current node to remove the key to be removed and the size will be more that the min
                        //if that sibling has more than the min element in the keys list

                            borrowFromLeft(nodeContainingToBeRemovedKey);

                            nodeContainingToBeRemovedKey.getKeys().remove(index);
                            nodeContainingToBeRemovedKey.getValues().remove(index);

                        return true;

                    }
                    else if (  rightSibling != null && rightSibling.getKeys().size() > this.minDegree-1 ) {
                           //3rd case the node to be deleted is leaf and has number of elements less oe equal min§Degree
                           // and it has a right sibling and that right sibling has more than minDegree

                        borrowFromRight(nodeContainingToBeRemovedKey);

                        nodeContainingToBeRemovedKey.getKeys().remove(index);
                        nodeContainingToBeRemovedKey.getValues().remove(index);

                        return true;
                    }

                    else  {
                        //case both LC an RC has number of keys less than or equal minDegree

                        if (leftSibling==null && (parent.getNumOfKeys()>minDegree-1 || parent == root)){

                            IBTreeNode newLeftNode = merge(nodeContainingToBeRemovedKey,rightSibling);
                            //there is no left sibling so the node is at most left
                            //the element to be merged with RL N is at index 0
                            int i=0;
                            while ((int)newLeftNode.getKeys().get(i) < (int)parent.getKeys().get(0)) i++ ;

                            newLeftNode.getKeys().add(i,parent.getKeys().get(0));
                            newLeftNode.getValues().add(i,parent.getValues().get(0));

                            parent.getKeys().remove(0);
                            parent.getValues().remove(0);
                            parent.getChildren().remove(0);

                            parent.getChildren().remove(0);

                            parent.getChildren().add(0,newLeftNode);

                            newLeftNode.getKeys().remove(key);
                            return true;


                        }

                        else if (leftSibling!=null && !( parent.getNumOfKeys() > minDegree-1 || parent == root )){

                            IBTreeNode newRightNode = merge(leftSibling,nodeContainingToBeRemovedKey);
                            int i=0;
                            while ((int)newRightNode.getKeys().get(i) < (int)parent.getKeys().get(parent.getChildren().indexOf(leftSibling))) i++ ;

                            newRightNode.getKeys().add( i , parent.getKeys().get(parent.getChildren().indexOf(leftSibling)));
                            newRightNode.getValues().add( i , parent.getValues().get(parent.getChildren().indexOf(leftSibling)));

                            int newChildIndex =parent.getChildren().indexOf(leftSibling);

                            parent.getKeys().remove(newChildIndex);
                            parent.getValues().remove(newChildIndex);
                            parent.getChildren().remove(newChildIndex);

                            parent.getChildren().remove(newChildIndex);

                            parent.getChildren().add(newChildIndex,newRightNode);

                            newRightNode.getKeys().remove(key);
/*
                            if (!( parent.getNumOfKeys() > minDegree-1 || parent == root)){
                                IBTreeNode parentOfthisnode = parent;
                                IBTreeNode toBorrowFrom = (IBTreeNode) getRightSibling(parent);
                                if (toBorrowFrom==null) toBorrowFrom = (IBTreeNode) getLeftSibling(parent);

                                if (toBorrowFrom.getKeys().size()>minDegree-1){
                                    borrowFromRight(parentOfthisnode);


                                }


                            }*/
                            return true;

                        }

                    }
                }
            }
            else {
                //when the node to be removed is noe leaf so we have 2 cases
                //replace it with maximum left key or minimum right key
                index = nodeContainingToBeRemovedKey.getKeys().indexOf(key);
                IBTreeNode leafChild =null;
                IBTreeNode leftChild =inOrderPredecessor(nodeContainingToBeRemovedKey,key);
                IBTreeNode rightChild = inOrderSuccessor(nodeContainingToBeRemovedKey,key);
                int indexOfKeyInLeaf=-1;
                if ( leftChild.getKeys().size() > minDegree-1)   {
                    leafChild =inOrderPredecessor(nodeContainingToBeRemovedKey,key);
                    indexOfKeyInLeaf=leafChild.getKeys().size()-1;
                }
                else if (rightChild.getKeys().size() > minDegree-1 ) {
                    leafChild = inOrderSuccessor(nodeContainingToBeRemovedKey, key);
                    indexOfKeyInLeaf = 0;
                }
                if (leafChild!=null && leafChild.getKeys().size() > minDegree-1){
                    //remove a key from non leaf node using  inOrderPredecessor or inOrderSuccessor
                    nodeContainingToBeRemovedKey.getKeys().remove(index);
                    nodeContainingToBeRemovedKey.getValues().remove(index);

                    nodeContainingToBeRemovedKey.getKeys().add(index,leafChild.getKeys().get(indexOfKeyInLeaf));
                    nodeContainingToBeRemovedKey.getValues().add(index,leafChild.getValues().get(indexOfKeyInLeaf));

                    leafChild.getKeys().remove(indexOfKeyInLeaf);
                    leafChild.getValues().remove(indexOfKeyInLeaf);


                    return true;


                }

                //if non of Lc nor Rc has more than minimum number of keys ??
                //we merge Rc and Lc then remove the p key
                else {


                    nodeContainingToBeRemovedKey.getKeys().remove(index);
                    nodeContainingToBeRemovedKey.getValues().remove(index);

                    nodeContainingToBeRemovedKey.getChildren().remove(index);

                    nodeContainingToBeRemovedKey.getChildren().set(index, merge(leftChild,rightChild));
                    return true;

                }
            }


        }

        return false;
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
        assert (x == null);
        int i;
        for ( i = 0; i < x.getNumOfKeys(); i++){
            if(!x.isLeaf()){
                Show((IBTreeNode) x.getChildren().get(i));
            }
            System.out.print(" " +x.getKeys().get(i));
            System.out.print(" " + x.getValues().get(i));
        }
        if(!x.isLeaf()){
            Show((IBTreeNode) x.getChildren().get(i));
        }
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
                i++;
                continue;
            } else {
                merged.getKeys().add(node2.getKeys().get(i));
                merged.getValues().add(node2.getValues().get(i));
                j++;
                continue;

            }
        }

        while ( i < node1.getKeys().size()){
            merged.getKeys().add(node1.getKeys().get(i));
            merged.getValues().add(node1.getValues().get(i));
            i++;
        }
        while (j < node2.getKeys().size()){
            merged.getKeys().add(node2.getKeys().get(j));
            merged.getValues().add(node2.getValues().get(j));
            j++;
        }

        return merged;
}

    public static void main(String[] args) {

        IBTree tree = new Tree(2);
        for(int i = 2 ; i < 22; i+=2){
            tree.insert(i, "a"+i);
        }

        /*
        tree.insert(100, "tt");
        tree.Show(tree.getRoot());
        System.out.println();
        tree.insert(2, "uu");
        tree.Show(tree.getRoot());
        System.out.println();
        tree.insert(32, "y");
        tree.Show(tree.getRoot());
        System.out.println();
        tree.insert(3, "l");
        tree.Show(tree.getRoot());
        System.out.println();
        tree.insert(500, "yy");
        tree.Show(tree.getRoot());
        System.out.println();
        tree.insert(14, "rr");
        tree.Show(tree.getRoot());
        System.out.println();
        tree.insert(1, "oo");
        tree.insert(22222,"e");
*/

        tree.Show(tree.getRoot());
      //  tree.insert(15,"Wfg");
        tree.insert(11,"www");
        tree.delete(8);

        tree.delete(16);

        tree.delete(14);
        tree.insert(1,"qq");
        tree.insert(-1,"qq");
        tree.insert(-2,"qq");
        tree.delete(4);


        tree.insert(-1,"eeee");
        tree.insert(-3,"ff");
        tree.delete(2);
        tree.delete(5);

  /*      Tree tree=new Tree(3);

        Node r = new Node(3);
        tree.root=r;
        List<String> keysRoot = new LinkedList<>();
        keysRoot.add("f");
        r.setKeys(keysRoot);


        List<IBTreeNode> children =new LinkedList<>();

        Node child1 =new Node(3);
        List<String> keyschild1 = new LinkedList<>();
        keyschild1.add("c");
        child1.setKeys(keyschild1);

        Node child2 =new Node(3);
        List<String> keyschild2 = new LinkedList<>();
        keyschild2.add("q");
        child2.setKeys(keyschild2);

        //////////////////////
        Node child3 =new Node(3);
        List<String> keyschild3 = new LinkedList<>();
        keyschild3.add("0");
        keyschild3.add("a");
        child3.setKeys(keyschild3);


        Node child4 =new Node(3);
        List<String> keyschild4 = new LinkedList<>();
        keyschild4.add("d");
        keyschild4.add("e");
        child4.setKeys(keyschild4);
        List<IBTreeNode> children1 =new LinkedList<>();




        Node child5 =new Node(3);
        List<String> keyschild5 = new LinkedList<>();
        keyschild5.add("g");
        child5.setKeys(keyschild5);


        Node child6 =new Node(3);
        List<String> keyschild6 = new LinkedList<>();

        keyschild6.add("s");

        child6.setKeys(keyschild6);

        List<IBTreeNode> children2 =new LinkedList<>();

        children2.clear();
        children2.add(child5);
        children2.add(child6);

        child2.setChildren(children2);
        children1.clear();
        children1.add(child3);
        children1.add(child4);

        child1.setChildren(children1);

        children.add(child1);
        children.add(child2);
        r.setChildren(children);

      /*  System.out.println( tree.search("c"));
        System.out.println( tree.search("a"));
        System.out.println( tree.search("g"));
        System.out.println( tree.search("e"));
        System.out.println( tree.search("o"));*/

/*
        System.out.println( tree.search(9));
        System.out.println( tree.search(3));
        System.out.println( tree.search(10));
        System.out.println( tree.search(5));
        System.out.println( tree.search(15));
        System.out.println( tree.search(-7));
        System.out.println( tree.search(1));





*/

    }
}