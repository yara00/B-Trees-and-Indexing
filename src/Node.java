import java.util.List;

public class Node<K extends Comparable<K>, V> implements IBTreeNode {
    private int keysNumber;
    private int childrenNum;   // number of children
    private List<IBTreeNode> children;
    private List<V> values;
    private List<K> keys;
    private boolean isLeaf;

    public Node(int k) {
        this.childrenNum = k;
    }

    @Override
    public int getNumOfKeys() {
        return keysNumber;
    }

    @Override
    public void setNumOfKeys(int numOfKeys) {
        this.keysNumber = numOfKeys;
    }

    @Override
    public boolean isLeaf() {
        return this.isLeaf;
    }

    @Override
    public void setLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    @Override
    public List getKeys() {
        return keys;
    }

    @Override
    public void setKeys(List keys) {
        this.keys = keys;
    }

    @Override
    public List getValues() {
        return values;
    }

    @Override
    public void setValues(List values) {
        this.values = values;
    }

    @Override
    public List<IBTreeNode> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List children) {
        this.children = children;
    }
}
