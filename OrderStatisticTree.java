import java.util.*;

class Main {

    static class Node {
        double rating;
        int size, height;
        Node left, right;

        Node(double rating) {
            this.rating = rating;
            size = 1;
            height = 1;
        }
    }

    static class OrderStatisticTree {

        Node root;

        int height(Node n) {
            return n == null ? 0 : n.height;
        }

        int size(Node n) {
            return n == null ? 0 : n.size;
        }

        void update(Node n) {
            n.height = Math.max(height(n.left), height(n.right)) + 1;
            n.size = size(n.left) + size(n.right) + 1;
        }

        Node rightRotate(Node y) {
            Node x = y.left;
            Node temp = x.right;

            x.right = y;
            y.left = temp;

            update(y);
            update(x);

            return x;
        }

        Node leftRotate(Node x) {
            Node y = x.right;
            Node temp = y.left;

            y.left = x;
            x.right = temp;

            update(x);
            update(y);

            return y;
        }

        int balance(Node n) {
            return n == null ? 0 : height(n.left) - height(n.right);
        }

        Node insert(Node node, double rating) {

            if(node == null)
                return new Node(rating);

            if(rating < node.rating)
                node.left = insert(node.left, rating);
            else
                node.right = insert(node.right, rating);

            update(node);

            int b = balance(node);

            if(b > 1 && rating < node.left.rating)
                return rightRotate(node);

            if(b < -1 && rating > node.right.rating)
                return leftRotate(node);

            if(b > 1 && rating > node.left.rating) {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }

            if(b < -1 && rating < node.right.rating) {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }

            return node;
        }

        int getRank(Node node, double rating) {

            if(node == null)
                return 0;

            if(rating < node.rating)
                return getRank(node.left, rating);

            return size(node.left) + 1 +
                   getRank(node.right, rating);
        }

        double select(Node node, int k) {

            if(node == null)
                return -1;

            int leftSize = size(node.left);

            if(k == leftSize + 1)
                return node.rating;

            if(k <= leftSize)
                return select(node.left, k);

            return select(node.right, k-leftSize-1);
        }

        void inorder(Node node) {

            if(node != null) {
                inorder(node.left);
                System.out.println(node.rating);
                inorder(node.right);
            }
        }
    }

    public static void main(String[] args) {

        OrderStatisticTree tree =
        new OrderStatisticTree();

        double ratings[] =
        {4.8,4.5,3.9,4.2,5.0,3.5};

        for(double r:ratings)
            tree.root = tree.insert(tree.root,r);

        System.out.println("Sorted App Ratings:");
        tree.inorder(tree.root);

        System.out.println(
        "Rank of 4.5 = "+
        tree.getRank(tree.root,4.5));

        int rank =
        (int)Math.ceil(87/100.0*
        tree.size(tree.root));

        System.out.println(
        "87th Percentile Rating = "+
        tree.select(tree.root,rank));
    }
}
