import java.util.Scanner;
class Node<T> {
    T data;
    Node<T> next;
    Node(T data) { this.data = data; this.next = null; }
}

class MyStack<T> {
    private Node<T> top;
    private int size;

    public boolean isEmpty() { return top == null; }
    public boolean isFull()  { return false; }

    public void push(T data) {
        Node<T> n = new Node<>(data);
        n.next = top; top = n; size++;
    }

    public T pop() {
        if (isEmpty()) throw new RuntimeException("Stack underflow: stack is empty");
        T v = top.data; top = top.next; size--; return v;
    }

    public T peek() {
        if (isEmpty()) throw new RuntimeException("Stack is empty");
        return top.data;
    }

    public int size() { return size; }

    @Override
    public String toString() {
        if (isEmpty()) return "TOP [ empty ] BOTTOM";
        StringBuilder sb = new StringBuilder("TOP [ ");
        for (Node<T> c = top; c != null; c = c.next)
            sb.append(c.data).append(c.next != null ? " -> " : "");
        return sb.append(" ] BOTTOM").toString();
    }
}

class MyQueue<T> {
    private Node<T> front, rear;
    private int size;

    public boolean isEmpty() { return front == null; }
    public boolean isFull()  { return false; }

    public void enQueue(T data) {
        Node<T> n = new Node<>(data);
        if (isEmpty()) { front = rear = n; }
        else           { rear.next = n; rear = n; }
        size++;
    }

    public T deQueue() {
        if (isEmpty()) throw new RuntimeException("Queue underflow: queue is empty");
        T v = front.data; front = front.next;
        if (front == null) rear = null;
        size--; return v;
    }

    public T peek() {
        if (isEmpty()) throw new RuntimeException("Queue is empty");
        return front.data;
    }

    public int size() { return size; }

    @Override
    public String toString() {
        if (isEmpty()) return "FRONT [ empty ] REAR";
        StringBuilder sb = new StringBuilder("FRONT [ ");
        for (Node<T> c = front; c != null; c = c.next)
            sb.append(c.data).append(c.next != null ? " -> " : "");
        return sb.append(" ] REAR").toString();
    }
}

public class Lab3 {

    static Scanner sc = new Scanner(System.in);
    static MyStack<Integer> stack = new MyStack<>();
    static MyQueue<Integer> queue = new MyQueue<>();

    static void printMenu() {
        System.out.println("\n=== Lab 3 Menu ===");
        System.out.println("--- Stack (LIFO) ---");
        System.out.println("1. Push");
        System.out.println("2. Pop");
        System.out.println("3. Print stack");
        System.out.println("4. Stack isEmpty / isFull");
        System.out.println("--- Queue (FIFO) ---");
        System.out.println("5. Enqueue");
        System.out.println("6. Dequeue");
        System.out.println("7. Print queue");
        System.out.println("8. Queue isEmpty / isFull");
        System.out.println("--- Demo ---");
        System.out.println("9. Lecture stack demo (push A,B,C,D)");
        System.out.println("10. Lecture queue demo");
        System.out.println("11. Exit");
        System.out.print("Enter choice: ");
    }

    static void pause() {
        System.out.print("\nPress Enter to continue...");
        sc.nextLine();
    }

    static int readInt(String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextInt()) {
            sc.next();
            System.out.print("Enter a valid integer: ");
        }
        int val = sc.nextInt();
        sc.nextLine();
        return val;
    }

    public static void main(String[] args) {
        System.out.print("Press Enter to continue...");
        sc.nextLine();

        int choice = 0;

        do {
            printMenu();
            while (!sc.hasNextInt()) { sc.next(); }
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    int pushVal = readInt("Enter number to push: ");
                    stack.push(pushVal);
                    System.out.println("Pushed: " + pushVal);
                    System.out.println(stack);
                    pause();
                    break;

                case 2:
                    try {
                        int popped = stack.pop();
                        System.out.println("Popped: " + popped);
                        System.out.println(stack);
                    } catch (RuntimeException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    pause();
                    break;

                case 3:
                    System.out.println(stack);
                    pause();
                    break;

                case 4:
                    System.out.println("Stack isEmpty: " + stack.isEmpty());
                    System.out.println("Stack isFull:  " + stack.isFull());
                    pause();
                    break;

                case 5:
                    int enqVal = readInt("Enter number to enqueue: ");
                    queue.enQueue(enqVal);
                    System.out.println("Enqueued: " + enqVal);
                    System.out.println(queue);
                    pause();
                    break;

                case 6:
                    try {
                        int dequeued = queue.deQueue();
                        System.out.println("Dequeued: " + dequeued);
                        System.out.println(queue);
                    } catch (RuntimeException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    pause();
                    break;

                case 7:
                    System.out.println(queue);
                    pause();
                    break;

                case 8:
                    System.out.println("Queue isEmpty: " + queue.isEmpty());
                    System.out.println("Queue isFull:  " + queue.isFull());
                    pause();
                    break;

                case 9:
                    MyStack<String> demoStack = new MyStack<>();
                    System.out.println("\n-- Lecture stack demo (push A, B, C, D) --");
                    for (String s : new String[]{"A","B","C","D"}) {
                        demoStack.push(s);
                        System.out.println("push(" + s + ") -> " + demoStack);
                    }
                    System.out.println("pop()        -> " + demoStack.pop() + " | " + demoStack);
                    pause();
                    break;

                case 10:
                    MyQueue<String> demoQueue = new MyQueue<>();
                    System.out.println("\n-- Lecture queue demo --");
                    for (String s : new String[]{"A","B","C"}) {
                        demoQueue.enQueue(s);
                        System.out.println("enQueue(" + s + ") -> " + demoQueue);
                    }
                    System.out.println("deQueue()    -> " + demoQueue.deQueue() + " | " + demoQueue);
                    demoQueue.enQueue("D");
                    demoQueue.enQueue("E");
                    System.out.println("enQueue(D,E) -> " + demoQueue);
                    pause();
                    break;

                case 11:
                    System.out.println("Exit.");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
                    pause();
            }

        } while (choice != 11);

        sc.close();
    }
}