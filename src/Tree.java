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
        IBTreeNode temp = this.root;
        if(this.root.getNumOfKeys() == (2 * this.minDegree) - 1) {
            IBTreeNode node = new Node((2 * this.minDegree) - 1);
            this.root = node;
            node.setLeaf(false);
            node.setNumOfKeys(0);
            node.getChildren().add(temp);
            split(node, 1);
            insertNonFull(node, key, value);
        }
        else insertNonFull(temp, key, value);
    }

    private void split(IBTreeNode node, int index) {
        IBTreeNode newNode = new Node((2 * this.minDegree) - 1);
        IBTreeNode childAtIndex = (IBTreeNode) node.getChildren().get(index);
        newNode.setLeaf(childAtIndex.isLeaf());
        newNode.setNumOfKeys(this.minDegree - 1);
        for(int i=1; i< this.minDegree-1; i++) {
            newNode.getKeys().add(childAtIndex.getKeys().get(i + this.minDegree));
        }
        if(!childAtIndex.isLeaf()) {
            for(int i=1; i<this.minDegree; i++) {
                newNode.getChildren().add(childAtIndex.getChildren().get(i + this.minDegree));
            }
        }
        childAtIndex.setNumOfKeys(this.minDegree - 1);
        for(int i= node.getNumOfKeys() + 1; i > (index + 1); i--) {
            node.getChildren().add(i+1, node.getChildren().get(i));
        }
        node.getChildren().add(index+1, newNode);
        for(int i=node.getNumOfKeys(); i > index; i--) {
            node.getKeys().add(i+1,  node.getKeys().get(i));
            node.getValues().add(i+1,  node.getValues().get(i));
        }
        node.getKeys().add(index, childAtIndex.getKeys().get(this.minDegree));
        node.getValues().add(index, childAtIndex.getValues().get(this.minDegree));
        node.setNumOfKeys(node.getNumOfKeys() + 1);
    }

    private void insertNonFull(IBTreeNode node, Comparable key, Object value) {
        int numOfKeys = node.getNumOfKeys();
        if(node.isLeaf()) {
            while (numOfKeys>=1 & key < node.getKeys().get(numOfKeys))
        }
    }
    @Override
    public Object search(Comparable key) {
        return null;
    }

    @Override
    public boolean delete(Comparable key) {
        return false;
    }
}
