//  Static Linked List — Java Implementation
class labs2 {

    private int[] LList;
    private int size;
    private int index;

    // Constructor — initialises the array with a fixed capacity
    public labs2(int size) {
        this.size  = size;
        this.index = -1;           // -1 means the list is empty
        this.LList = new int[size];
    }

    // Returns true when no elements have been inserted yet
    public boolean isEmpty() {
        return index == -1;
    }

    // Returns true when every slot in the array is occupied
    public boolean isFull() {
        return index == size - 1;
    }

    // Adds an element to the end of the list
    public void insert(int data) {
        if (!isFull()) {
            ++this.index;
            this.LList[index] = data;
            System.out.println("Inserted " + data + " at index " + index);
        } else {
            System.out.println("List is full — cannot insert " + data);
        }
    }

    // Removes the last element by moving the index boundary back
    public void delete() {
        if (!isEmpty()) {
            System.out.println("Deleted " + LList[index] + " from index " + index);
            --this.index;
        } else {
            System.out.println("List is empty — nothing to delete");
        }
    }

    // Prints every active element (index 0 → index)
    public void printList() {
        if (isEmpty()) {
            System.out.println("(list is empty)");
            return;
        }
        System.out.println("List contents:");
        for (int i = 0; i <= index; ++i) {
            System.out.println("  [" + i + "] = " + LList[i]);
        }
    }

    // Returns the current number of elements in the list
    public int size() {
        return index + 1;
    }
}

//  Test Driver — testLabs2.java

class testLabs2 {

    public static void main(String[] args) {

        System.out.println("=== Creating list with capacity 3 ===");
        labs2 SLL = new labs2(3);

        System.out.println("\n--- Inserting elements ---");
        SLL.insert(10);
        SLL.insert(20);
        SLL.insert(30);
        SLL.insert(40);   // should print "List is full"

        System.out.println("\n--- Printing list ---");
        SLL.printList();

        System.out.println("\n--- Deleting elements ---");
        SLL.delete();
        SLL.delete();
        SLL.delete();
        SLL.delete();     // should print "List is empty"

        System.out.println("\n--- Printing list after deletions ---");
        SLL.printList();
    }
}