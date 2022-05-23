import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Node<K extends Comparable<K>, V> implements IBTreeNode {
    private int keysNumber;
    private int childrenNum;   // number of children
    private List<IBTreeNode> children;
    private List<V> values;
    private List<K> keys;
    private boolean isLeaf = true;

    public Node(int k) {
        this.childrenNum = k;
        this.keysNumber = 0;
        this.children = new ArrayList<>(k);
        this.values = new ArrayList<>();
        this.keys = new ArrayList<>();
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


    public int search(Comparable key) {

        int start = 0;
        int end = this.keys.size() ;
        int mid = 0;
        while (start != end) {

            mid = (start + end) / 2;

            if (keys.get(mid).equals(key))
                return mid;

            else if (keys.get(mid).compareTo((K) key) > 0) {

                end = mid;
                if (mid +1 < keys.size() &&  keys.get(mid + 1).compareTo((K) key) < 0)
                    return mid;

                }else {
                    start = mid + 1;
                    if (mid - 1 > 0 && keys.get(mid - 1).compareTo((K) key) > 0) {
                        return mid + 1;
                    }

                }
            }


        return end;
    }

    public static void main(String[] args) {
















        }

}


