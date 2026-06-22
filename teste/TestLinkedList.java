package teste;
import java.util.Scanner;


// ---------- Node class ----------
class Node {
    int data;   // data part
    Node next;  // pointer to next node

    public Node(int data) {
        this.data = data;
    }

    public void printElement() {
        System.out.println(this.data);
    }
}

// ---------- LinkedList class ----------
class LinkedList {
    private Node LList;

    public LinkedList() {
        LList = null;
    }

    public boolean isEmpty() {
        return LList == null;
    }

    //inserts a new node at the end of the list
    public void insert(int data) {
        Node L = LList;
        Node node = new Node(data);
        if (isEmpty()) {
            node.next = null;
            LList = node;
        } else {
            while (L.next != null) {
                L = L.next;
            }
            node.next = null;
            L.next = node;
        }
    }

    //removes the node at the front of the list
    public void delete() {
        if (!isEmpty())
            LList = LList.next;
    }

    //displays every element in the list
    public void printList() {
        Node temp = LList;
        while (temp != null) {
            temp.printElement();
            temp = temp.next;
        }
    }

    // ================= New methods required for Practical Test 1 =================

    /**
     * AddBefore: inserts a new node containing newData so that it ends up
     * occupying the given position in the list. The node previously at
     * that position (and everything after it) shifts one place down.
     * Positions are counted starting at 1.
     *
     * Example: list 4,5,7,8 -> addBefore(3, 3) gives 4,5,3,7,8
     */
    public void addBefore(int position, int newData) {
        if (position < 1) {
            System.out.println("Error: position must be 1 or greater.");
            return;
        }

        Node node = new Node(newData);

        // Inserting at the very front, or into an empty list
        if (position == 1 || isEmpty()) {
            node.next = LList;
            LList = node;
            System.out.println(newData + " was inserted at position 1.");
            return;
        }

        // Walk to the node just BEFORE the target position
        Node current = LList;
        int count = 1;
        while (current != null && count < position - 1) {
            current = current.next;
            count++;
        }

        if (current == null) {
            System.out.println("Error: position " + position + " is out of range.");
            return;
        }

        node.next = current.next;
        current.next = node;
        System.out.println(newData + " was inserted at position " + position + ".");
    }

    /**
     * SearchNode: looks for the given data value and returns its 1-based
     * position in the list. If the value is not found, an error message
     * is displayed and -1 is returned.
     */
    public int searchNode(int data) {
        Node current = LList;
        int position = 1;

        while (current != null) {
            if (current.data == data) {
                return position;
            }
            current = current.next;
            position++;
        }

        System.out.println("Error: " + data + " was not found in the list.");
        return -1;
    }

    /**
     * DeleteAnyNode: deletes the node containing the given data value,
     * regardless of where it sits in the list.
     *
     * Example: list 4,5,7,8 -> deleteAnyNode(7) gives 4,5,8
     */
    public void deleteAnyNode(int data) {
        if (isEmpty()) {
            System.out.println("Error: the list is empty.");
            return;
        }

        // Special case: the node to delete is the head
        if (LList.data == data) {
            LList = LList.next;
            System.out.println("Node with data " + data + " was deleted.");
            return;
        }

        Node current = LList;
        while (current.next != null && current.next.data != data) {
            current = current.next;
        }

        if (current.next == null) {
            System.out.println("Error: " + data + " was not found in the list.");
            return;
        }

        current.next = current.next.next;
        System.out.println("Node with data " + data + " was deleted.");
    }
}

// ---------- Main class - menu driven test program ----------
public class TestLinkedList {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LinkedList list = new LinkedList();

        // Build the initial test list used in the practical test brief: 4, 5, 7, 8
        list.insert(4);
        list.insert(5);
        list.insert(7);
        list.insert(8);

        System.out.println("Initial list:");
        list.printList();

        int choice = 0;

        while (choice != 5) {
            System.out.println("\n----- LINKED LIST MENU -----");
            System.out.println("1. Add Before");
            System.out.println("2. Search Node");
            System.out.println("3. Delete Any Node");
            System.out.println("4. Display");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            choice = Integer.parseInt(scanner.nextLine().trim());

            switch (choice) {
                case 1:
                    System.out.print("Enter the position where the new value should go: ");
                    int position = Integer.parseInt(scanner.nextLine().trim());
                    System.out.print("Enter the value to insert: ");
                    int newValue = Integer.parseInt(scanner.nextLine().trim());
                    list.addBefore(position, newValue);
                    System.out.println("Updated list:");
                    list.printList();
                    break;

                case 2:
                    System.out.print("Enter the value to search for: ");
                    int searchValue = Integer.parseInt(scanner.nextLine().trim());
                    int foundPosition = list.searchNode(searchValue);
                    if (foundPosition != -1) {
                        System.out.println(searchValue + " was found at position " + foundPosition + ".");
                    }
                    break;

                case 3:
                    System.out.print("Enter the value of the node to delete: ");
                    int deleteValue = Integer.parseInt(scanner.nextLine().trim());
                    list.deleteAnyNode(deleteValue);
                    System.out.println("Updated list:");
                    list.printList();
                    break;

                case 4:
                    System.out.println("Current list:");
                    list.printList();
                    break;

                case 5:
                    System.out.println("Exiting program. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        }

        scanner.close();
    }
}
