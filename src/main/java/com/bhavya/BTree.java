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
        int i = noOfKeysInNode - 1;
        if (leaf) {
            while (i >= 0 && keys[i] > key) {
                keys[i + 1] = keys[i];
                i--;
            }

            keys[i + 1] = key;
            noOfKeysInNode++;
        } else {
            while (i >= 0 && keys[i] > key) {
                i--;
            }

            if (children[i].isNodeFull()) {
                splitChild(i + 1, children[i + 1]);

                if (keys[i + 1] < key) {
                    i++;
                }
            }
            children[i].insertNonFull(key);
        }
    }

    public void splitChild(int index, BTreeNode node) {
        BTreeNode newNode = new BTreeNode(node.degree, node.leaf);
        newNode.noOfKeysInNode = node.noOfKeysInNode - 1;

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

                int i = 0;
                if (newNode.keys[0] < key) {
                    i++;
                }
                newNode.children[i].insertNonFull(key);

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

    public static void main(String[] args) {
        BTree bTree = new BTree(3);
        bTree.insert(6);
        bTree.insert(2);
        bTree.insert(4);
        bTree.insert(8);
        bTree.insert(50);
        bTree.insert(30);
        bTree.insert(16);
        bTree.insert(89);

        ArrayList<Integer> result = bTree.traverse();

        for (int elem: result) {
            System.out.print(elem + " ");
        }
    }
}
