class TreeNode {
    int data;
    TreeNode left, right;

    public TreeNode(int data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }

    public void printElement() {
        System.out.print(this.data + " ");
    }
}

class Tree {
    private TreeNode root;

    public Tree() {
        root = null;
    }

    // =============================================
    // 1. Check if tree is empty
    // =============================================
    public boolean isEmpty() {
        return root == null;
    }

    // =============================================
    // 2. Insert a node (using recursion)
    // =============================================
    public void insertNode(int data) {
        root = insertNodeRec(root, data);
    }

    private TreeNode insertNodeRec(TreeNode node, int data) {
        if (node == null) {
            TreeNode newNode = new TreeNode(data);
            return newNode;
        }

        if (data < node.data) {
            node.left = insertNodeRec(node.left, data);
        } else if (data > node.data) {
            node.right = insertNodeRec(node.right, data);
        }
        // If equal, we don't insert (no duplicates)
        return node;
    }

    // =============================================
    // 3. Delete a node
    // =============================================
    public void deleteNode(int data) {
        root = deleteNodeRec(root, data);
    }

    private TreeNode deleteNodeRec(TreeNode node, int data) {
        if (node == null) {
            System.out.println("Node " + data + " not found!");
            return null;
        }

        if (data < node.data) {
            node.left = deleteNodeRec(node.left, data);
        } else if (data > node.data) {
            node.right = deleteNodeRec(node.right, data);
        } else {
            // Node found - handle deletion cases

            // Case 1: No child (leaf node)
            if (node.left == null && node.right == null) {
                return null;
            }

            // Case 2: One child
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }

            // Case 3: Two children
            // Find inorder successor (smallest in right subtree)
            TreeNode successor = findMin(node.right);
            node.data = successor.data;
            node.right = deleteNodeRec(node.right, successor.data);
        }
        return node;
    }

    // Helper method to find minimum value node
    private TreeNode findMin(TreeNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // =============================================
    // 4. Find depth of a node
    // =============================================
    public int depth(int data) {
        return depthRec(root, data, 0);
    }

    private int depthRec(TreeNode node, int data, int depth) {
        if (node == null) {
            return -1; // Node not found
        }

        if (node.data == data) {
            return depth;
        }

        int leftDepth = depthRec(node.left, data, depth + 1);
        if (leftDepth != -1) {
            return leftDepth;
        }

        return depthRec(node.right, data, depth + 1);
    }

    // =============================================
    // 5. Find height of the tree
    // =============================================
    public int height() {
        return heightRec(root);
    }

    private int heightRec(TreeNode node) {
        if (node == null) {
            return -1; // Empty tree has height -1
        }

        int leftHeight = heightRec(node.left);
        int rightHeight = heightRec(node.right);

        return Math.max(leftHeight, rightHeight) + 1;
    }

    // =============================================
    // 6. Find parent of a node
    // =============================================
    public Integer parent(int data) {
        if (root == null || root.data == data) {
            return null; // No parent for root or empty tree
        }
        return parentRec(root, data);
    }

    private Integer parentRec(TreeNode node, int data) {
        if (node == null) {
            return null;
        }

        // Check if either child has the target data
        if ((node.left != null && node.left.data == data) ||
            (node.right != null && node.right.data == data)) {
            return node.data;
        }

        // Search recursively
        Integer leftResult = parentRec(node.left, data);
        if (leftResult != null) {
            return leftResult;
        }

        return parentRec(node.right, data);
    }

    // =============================================
    // 7. Find children of a node
    // =============================================
    public String children(int data) {
        TreeNode node = searchNode(root, data);
        if (node == null) {
            return "Node " + data + " not found!";
        }

        StringBuilder result = new StringBuilder("Children of " + data + ": ");
        if (node.left == null && node.right == null) {
            result.append("No children (leaf node)");
        } else {
            if (node.left != null) {
                result.append("Left: ").append(node.left.data).append(" ");
            }
            if (node.right != null) {
                result.append("Right: ").append(node.right.data);
            }
        }
        return result.toString();
    }

    // Helper method to search for a node
    private TreeNode searchNode(TreeNode node, int data) {
        if (node == null || node.data == data) {
            return node;
        }

        if (data < node.data) {
            return searchNode(node.left, data);
        }
        return searchNode(node.right, data);
    }

    // =============================================
    // 8. Tree Traversals
    // =============================================
    
    // Inorder: Left - Root - Right
    public void inorder() {
        System.out.print("Inorder Traversal: ");
        inorderRec(root);
        System.out.println();
    }

    private void inorderRec(TreeNode node) {
        if (node != null) {
            inorderRec(node.left);
            node.printElement();
            inorderRec(node.right);
        }
    }

    // Preorder: Root - Left - Right
    public void preorder() {
        System.out.print("Preorder Traversal: ");
        preorderRec(root);
        System.out.println();
    }

    private void preorderRec(TreeNode node) {
        if (node != null) {
            node.printElement();
            preorderRec(node.left);
            preorderRec(node.right);
        }
    }

    // Postorder: Left - Right - Root
    public void postorder() {
        System.out.print("Postorder Traversal: ");
        postorderRec(root);
        System.out.println();
    }

    private void postorderRec(TreeNode node) {
        if (node != null) {
            postorderRec(node.left);
            postorderRec(node.right);
            node.printElement();
        }
    }

    // =============================================
    // 9. Build and evaluate expression tree
    // =============================================
    
    // Build expression tree for: ((5 + 6) * 30) / 300
    public TreeNode buildExpressionTree() {
        // Creating expression tree for ((5 + 6) * 30) / 300
        // Structure:
        //         /
        //       /   \
        //      *   300
        //    /   \
        //   +    30
        //  / \
        // 5   6

        TreeNode root = new TreeNode('/');
        root.left = new TreeNode('*');
        root.right = new TreeNode(300);

        root.left.left = new TreeNode('+');
        root.left.right = new TreeNode(30);

        root.left.left.left = new TreeNode(5);
        root.left.left.right = new TreeNode(6);

        return root;
    }

    // Evaluate expression tree
    public double evaluateExpression(TreeNode node) {
        if (node == null) {
            return 0;
        }

        // If leaf node (number)
        if (node.left == null && node.right == null) {
            return node.data;
        }

        // Evaluate left and right subtrees
        double leftVal = evaluateExpression(node.left);
        double rightVal = evaluateExpression(node.right);

        // Apply operator based on character value
        char operator = (char) node.data;
        switch (operator) {
            case '+': return leftVal + rightVal;
            case '-': return leftVal - rightVal;
            case '*': return leftVal * rightVal;
            case '/': 
                if (rightVal != 0) {
                    return leftVal / rightVal;
                } else {
                    System.out.println("Error: Division by zero!");
                    return 0;
                }
            default: return 0;
        }
    }

    // Display expression tree with parentheses
    public void displayExpression(TreeNode node) {
        if (node == null) {
            return;
        }

        // If leaf node (number)
        if (node.left == null && node.right == null) {
            System.out.print(node.data);
            return;
        }

        System.out.print("(");
        displayExpression(node.left);
        System.out.print(" " + (char) node.data + " ");
        displayExpression(node.right);
        System.out.print(")");
    }

    // =============================================
    // 10. Display tree structure (for visualization)
    // =============================================
    public void displayTree() {
        if (root == null) {
            System.out.println("Tree is empty!");
            return;
        }
        System.out.println("\nTree Structure:");
        displayTreeRec(root, 0);
    }

    private void displayTreeRec(TreeNode node, int level) {
        if (node == null) {
            return;
        }

        displayTreeRec(node.right, level + 1);

        for (int i = 0; i < level; i++) {
            System.out.print("    ");
        }
        System.out.println(node.data);

        displayTreeRec(node.left, level + 1);
    }

    // Get root for expression tree evaluation
    public TreeNode getRoot() {
        return root;
    }

    // Set root for expression tree
    public void setRoot(TreeNode root) {
        this.root = root;
    }
}

// =============================================
// MAIN CLASS
// =============================================
public class Lab5 {
    public static void main(String[] args) {
        System.out.println("======================================");
        System.out.println("LAB 5 - BINARY SEARCH TREE OPERATIONS");
        System.out.println("======================================\n");

        Tree tree = new Tree();

        // ===== Inserting nodes =====
        System.out.println("--- Inserting nodes: 10, 20, 5, 30, 15, 7 ---");
        int[] values = {10, 20, 5, 30, 15, 7};
        for (int val : values) {
            tree.insertNode(val);
            System.out.print("Inserted " + val + ": ");
            tree.inorder();
        }

        tree.displayTree();

        // ===== Tree traversals =====
        System.out.println("\n--- Tree Traversals ---");
        tree.inorder();
        tree.preorder();
        tree.postorder();

        // ===== Find height =====
        System.out.println("\n--- Tree Height ---");
        System.out.println("Height of tree: " + tree.height());

        // ===== Find depth =====
        System.out.println("\n--- Node Depth ---");
        int[] depthValues = {10, 20, 5, 30, 15, 7, 25};
        for (int val : depthValues) {
            int depth = tree.depth(val);
            if (depth != -1) {
                System.out.println("Depth of node " + val + ": " + depth);
            } else {
                System.out.println("Node " + val + " not found!");
            }
        }

        // ===== Find parent =====
        System.out.println("\n--- Parent of Nodes ---");
        int[] parentValues = {20, 5, 30, 15, 7, 25};
        for (int val : parentValues) {
            Integer parent = tree.parent(val);
            if (parent != null) {
                System.out.println("Parent of " + val + ": " + parent);
            } else {
                System.out.println("Node " + val + " not found or is root!");
            }
        }

        // ===== Find children =====
        System.out.println("\n--- Children of Nodes ---");
        int[] childValues = {10, 20, 5, 30, 15, 7, 25};
        for (int val : childValues) {
            System.out.println(tree.children(val));
        }

        // ===== Delete nodes =====
        System.out.println("\n--- Deleting Nodes ---");
        
        System.out.println("Deleting leaf node 15:");
        tree.deleteNode(15);
        tree.inorder();
        tree.displayTree();

        System.out.println("\nDeleting node with one child (20):");
        tree.deleteNode(20);
        tree.inorder();
        tree.displayTree();

        System.out.println("\nDeleting node with two children (10):");
        tree.deleteNode(10);
        tree.inorder();
        tree.displayTree();

        // =============================================
        // EXPRESSION TREE
        // =============================================
        System.out.println("\n======================================");
        System.out.println("EXPRESSION TREE EVALUATION");
        System.out.println("======================================\n");

        Tree exprTree = new Tree();
        TreeNode exprRoot = exprTree.buildExpressionTree();
        exprTree.setRoot(exprRoot);

        System.out.println("Expression: ((5 + 6) * 30) / 300");
        System.out.print("Infix notation: ");
        exprTree.displayExpression(exprRoot);
        System.out.println();

        double result = exprTree.evaluateExpression(exprRoot);
        System.out.println("Result: " + result);

        System.out.println("\nExpression Tree Structure:");
        System.out.println("        /");
        System.out.println("      /   \\");
        System.out.println("     *    300");
        System.out.println("   /   \\");
        System.out.println("  +    30");
        System.out.println(" / \\");
        System.out.println("5   6");

        // Verify calculation
        System.out.println("\nVerification:");
        System.out.println("(5 + 6) = 11");
        System.out.println("11 * 30 = 330");
        System.out.println("330 / 300 = " + (330.0/300.0));

        // =============================================
        // ADDITIONAL TESTS - Edge Cases
        // =============================================
        System.out.println("\n======================================");
        System.out.println("EDGE CASE TESTS");
        System.out.println("======================================\n");

        Tree emptyTree = new Tree();
        System.out.println("Empty tree:");
        System.out.println("Is empty? " + emptyTree.isEmpty());
        System.out.println("Height: " + emptyTree.height());
        emptyTree.inorder();
        emptyTree.deleteNode(5); // Should show not found
        emptyTree.displayTree();
    }
}