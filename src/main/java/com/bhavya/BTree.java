package com.bhavya;

import java.util.ArrayList;

class BTreeNode {
    int[] keys;
    int degree;
    BTreeNode[] children;
    boolean leaf;
    int noOfKeysInNode;

    public BTreeNode(int degree, boolean leaf) {
        this.keys = new int[2 * degree - 1];
        this.children = new BTreeNode[2 * degree];
        this.leaf = leaf;
        this.degree = degree;
        this.noOfKeysInNode = 0;
    }

    public void insertNonFull(int key) {
        if (leaf) {
            int i = 0;
            for(i = noOfKeysInNode - 1; i >= 0 && keys[i] > key; i--) {
                keys[i + 1] = keys[i];
            }

            keys[i + 1] = key;
            noOfKeysInNode++;
        } else {
            int i = 0;
            for (i = noOfKeysInNode - 1; i >= 0 && keys[i] > key; i--) {

            }
            i++;
            BTreeNode tmp = children[i];

            if (tmp.isNodeFull()) {
                splitChild(i, tmp);

                if (keys[i] < key) {
                    i++;
                }
            }
            children[i].insertNonFull(key);
        }
    }

    public void splitChild(int index, BTreeNode node) {
        BTreeNode newNode = new BTreeNode(node.degree, node.leaf);
        newNode.noOfKeysInNode = node.degree - 1;

        for (int i = 0; i < degree - 1; i++) {
            newNode.keys[i] = node.keys[i + degree];
        }

        if (!node.leaf) {
            for (int i = 0; i < degree; i++) {
                newNode.children[i] = node.children[i + degree];
            }
        }

        node.noOfKeysInNode = degree - 1;

        for(int i = noOfKeysInNode; i >= index; i--) {
            children[i + 1] = children[i];
        }

        children[index + 1] = newNode;

        for(int i = noOfKeysInNode - 1; i >= index; i--) {
            keys[i + 1] = keys[i];
        }

        keys[index] = node.keys[degree - 1];
        noOfKeysInNode++;
    }

    public void traverse(ArrayList<Integer> result) {
        for (int i = 0; i < noOfKeysInNode; i++) {
            result.add(keys[i]);
        }

        if (!leaf) {
            for (int i = 0; i < noOfKeysInNode + 1; i++) {
                children[i].traverse(result);
            }
        }
    }

    public boolean search(int key) {
        int i = 0;
        for(i = 0; i < noOfKeysInNode; i++) {
            if (key < keys[i]) {
                break;
            }
            if (key == keys[i]) {
                return true;
            }
        }
        if (!leaf) {
            return children[i].search(key);
        }
        return false;
    }

    public boolean isNodeFull() {
        return noOfKeysInNode == 2 * degree - 1;
    }
}

public class BTree {

    // range allowed (maximum & minimum no of children) for each node
    private final int degree;

    private BTreeNode root;

    public BTree(int degree) {
        this.degree = degree;
        this.root = null;
    }

    public void insert(int key) {
        if (root == null) {
            BTreeNode node = new BTreeNode(degree, true);
            node.keys[0] = key;
            node.noOfKeysInNode++;
            root = node;
        } else {
            if (root.isNodeFull()) {
                BTreeNode newNode = new BTreeNode(degree, false);
                newNode.children[0] = root;

                newNode.splitChild(0, root);
                newNode.insertNonFull(key);

                root = newNode;
            } else {
                root.insertNonFull(key);
            }
        }
    }

    public ArrayList<Integer> traverse() {
        ArrayList<Integer> result = new ArrayList<>();
        if (root != null) {
            root.traverse(result);
        }
        return result;
    }

    public boolean contain(int k) {
        if (root == null) {
            return false;
        }
        return root.search(k);
    }

    public static void main(String[] args) {
        BTree bTree = new BTree(3);
        bTree.insert(8);
        bTree.insert(9);
        bTree.insert(10);
        bTree.insert(11);
        bTree.insert(15);
        bTree.insert(20);
        bTree.insert(17);

        ArrayList<Integer> result = bTree.traverse();

        for (int elem: result) {
            System.out.print(elem + " ");
        }

        System.out.println("Contains " + bTree.contain(15));
    }
}
