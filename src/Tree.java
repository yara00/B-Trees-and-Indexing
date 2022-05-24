import java.util.LinkedList;
import java.util.List;

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
            newNode.getKeys().add(childAtIndex.getKeys().get(i + this.minDegree));
            newNode.getValues().add(childAtIndex.getValues().get(i + this.minDegree));
        }
        while(newNode.getKeys().size() > newNode.getNumOfKeys()) {
            newNode.getKeys().remove(newNode.getKeys().size()-1);
            newNode.getValues().remove(newNode.getValues().size()-1);
        }
        if(!childAtIndex.isLeaf()) {
            for(int i=0; i<this.minDegree; i++) { // momken t-1
                newNode.getChildren().add(childAtIndex.getChildren().get(i + this.minDegree));
            }
        }
        childAtIndex.setNumOfKeys(this.minDegree - 1);
        for(int i= node.getNumOfKeys(); i >= (index + 1); i--) {
            node.getChildren().add(i+1, node.getChildren().get(i));
        }
        node.getChildren().add(index+1, newNode);
        for(int i=node.getNumOfKeys()-1; i >= index; i--) {
            node.getKeys().add(i + 1, node.getKeys().get(i));
            node.getValues().add(i + 1, node.getValues().get(i));

        }
        node.getKeys().add(index, childAtIndex.getKeys().get(this.minDegree - 1));
        node.getValues().add(index, childAtIndex.getValues().get(this.minDegree - 1));
        node.setNumOfKeys(node.getNumOfKeys() + 1);
        while(node.getKeys().size() > node.getNumOfKeys()) {
            node.getKeys().remove(node.getKeys().size()-1);
            node.getValues().remove(node.getValues().size()-1);
        }
    }

    private void insertNonFull(IBTreeNode node, Comparable key, Object value) {

        if(node.isLeaf()) {
            int numOfKeys = node.getNumOfKeys() - 1;
            while(numOfKeys >= 0 && key.compareTo(node.getKeys().get(numOfKeys)) < 0) {
                node.getKeys().add(numOfKeys + 1, node.getKeys().get(numOfKeys));
                node.getValues().add(numOfKeys + 1, node.getValues().get(numOfKeys));
                numOfKeys--;
            }
            node.getKeys().add(numOfKeys + 1, key);
            node.getValues().add(numOfKeys + 1, value);
            node.setNumOfKeys(node.getNumOfKeys() + 1);
            while(node.getKeys().size() > node.getNumOfKeys()) {
                node.getKeys().remove(node.getKeys().size()-1);
                node.getValues().remove(node.getValues().size()-1);
            }
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

            List values = temp.getValues();

            int index = temp.search(key);
            if (index < temp.getKeys().size() && key.compareTo(temp.getKeys().get(index)) == 0) {
                return true;
            } else {
                if (temp.getChildren() != null)
                    temp = (IBTreeNode) temp.getChildren().get(index);
                else return false;

            }
        }

        return false;
    }

    @Override
    public boolean delete(Comparable key) {
        return false;
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
    public static void main(String[] args) {
        IBTree tree = new Tree(2);
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
        tree.insert(870, "p");
        tree.Show(tree.getRoot());
        System.out.println();
        tree.insert(5, "q");
        tree.insert(50, "s");
        tree.insert(1, "v");
        tree.insert(99, "y");
        tree.insert(8, "x");
        tree.Show(tree.getRoot());

        /*
        Tree tree=new Tree(3);

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

        System.out.println( tree.search("c"));
        System.out.println( tree.search("a"));
        System.out.println( tree.search("g"));
        System.out.println( tree.search("e"));
        System.out.println( tree.search("o"));
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