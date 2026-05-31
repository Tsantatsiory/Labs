// ═══════════════════════════════════════════════════════════
// Lab 3 – Stack and Queue  (dynamic + static)
// Single file: one public class + package-private classes
// ═══════════════════════════════════════════════════════════

// ─── Node ───────────────────────────────────────────────────
class Node<T> {
    T data;
    Node<T> next;
    Node(T data) { this.data = data; this.next = null; }
}

// ─── Dynamic Stack (LIFO) ────────────────────────────────────
class Stack<T> {
    private Node<T> top;
    private int size;

    public boolean isEmpty() { return top == null; }
    public boolean isFull()  { return false; } // dynamic: never full

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
        StringBuilder sb = new StringBuilder("TOP [ ");
        for (Node<T> c = top; c != null; c = c.next)
            sb.append(c.data).append(c.next != null ? " -> " : "");
        return sb.append(" ] BOTTOM").toString();
    }
}

// ─── Dynamic Queue (FIFO) ────────────────────────────────────
class Queue<T> {
    private Node<T> front, rear;
    private int size;

    public boolean isEmpty() { return front == null; }
    public boolean isFull()  { return false; } // dynamic: never full

    /** Adds an element at the rear (EnQueue / AddQueue / InsertQueue). */
    public void enQueue(T data) {
        Node<T> n = new Node<>(data);
        if (isEmpty()) { front = rear = n; }
        else           { rear.next = n; rear = n; }
        size++;
    }

    /** Removes the element at the front (DeQueue / RemoveQueue / DeleteQueue). */
    public T deQueue() {
        if (isEmpty()) throw new RuntimeException("Queue underflow: queue is empty");
        T v = front.data; front = front.next;
        if (front == null) rear = null; // queue is now empty
        size--; return v;
    }

    /** Returns the front element without removing it (Peek). */
    public T peek() {
        if (isEmpty()) throw new RuntimeException("Queue is empty");
        return front.data;
    }

    public int size() { return size; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("FRONT [ ");
        for (Node<T> c = front; c != null; c = c.next)
            sb.append(c.data).append(c.next != null ? " -> " : "");
        return sb.append(" ] REAR").toString();
    }
}

// ─── Static Stack (array) ────────────────────────────────────
class StaticStack<T> {
    private final T[] data;
    private int top = -1;           // -1 means the stack is empty
    private final int capacity;

    @SuppressWarnings("unchecked")
    StaticStack(int capacity) {
        this.capacity = capacity;
        data = (T[]) new Object[capacity];
    }

    /** EmptyStack / IsEmpty */
    public boolean isEmpty() { return top == -1; }

    /** FullStack / IsFull */
    public boolean isFull()  { return top == capacity - 1; }

    /** Adds an element to the top (Push). */
    public void push(T item) {
        if (isFull()) throw new RuntimeException("Stack overflow: stack is full");
        data[++top] = item;
    }

    /** Removes and returns the top element (Pop). */
    public T pop() {
        if (isEmpty()) throw new RuntimeException("Stack underflow: stack is empty");
        T v = data[top]; data[top--] = null; // help the GC
        return v;
    }

    /** Returns the top element without removing it (Peek). */
    public T peek() {
        if (isEmpty()) throw new RuntimeException("Stack is empty");
        return data[top];
    }

    public int size() { return top + 1; }

    @Override
    public String toString() {
        if (isEmpty()) return "[ empty ]";
        StringBuilder sb = new StringBuilder("TOP [ ");
        for (int i = top; i >= 0; i--)
            sb.append(data[i]).append(i > 0 ? " -> " : "");
        return sb.append(" ] BOTTOM").toString();
    }
}

// ─── Static Queue (circular array) ──────────────────────────
class StaticQueue<T> {
    private final T[] data;
    private int front = 0, rear = 0, size = 0;
    private final int capacity;

    @SuppressWarnings("unchecked")
    StaticQueue(int capacity) {
        this.capacity = capacity;
        data = (T[]) new Object[capacity];
    }

    /** EmptyQueue / IsEmpty */
    public boolean isEmpty() { return size == 0; }

    /** FullQueue / IsFull */
    public boolean isFull()  { return size == capacity; }

    /** Adds an element at the rear (EnQueue / AddQueue / InsertQueue). */
    public void enQueue(T item) {
        if (isFull()) throw new RuntimeException("Queue overflow: queue is full");
        data[rear] = item;
        rear = (rear + 1) % capacity; // circular wrap-around
        size++;
    }

    /** Removes the element at the front (DeQueue / RemoveQueue / DeleteQueue). */
    public T deQueue() {
        if (isEmpty()) throw new RuntimeException("Queue underflow: queue is empty");
        T v = data[front]; data[front] = null; // help the GC
        front = (front + 1) % capacity;        // circular wrap-around
        size--; return v;
    }

    /** Returns the front element without removing it (Peek). */
    public T peek() {
        if (isEmpty()) throw new RuntimeException("Queue is empty");
        return data[front];
    }

    public int size() { return size; }

    @Override
    public String toString() {
        if (isEmpty()) return "[ empty ]";
        StringBuilder sb = new StringBuilder("FRONT [ ");
        for (int i = 0; i < size; i++) {
            sb.append(data[(front + i) % capacity]);
            if (i < size - 1) sb.append(" -> ");
        }
        return sb.append(" ] REAR").toString();
    }
}

// ─── Main class (must match the filename) ───────────────────
public class Lab3 {
    public static void main(String[] args) {

        System.out.println("=== Dynamic Stack ===");
        Stack<String> stack = new Stack<>();
        stack.push("A"); stack.push("B"); stack.push("C"); stack.push("D");
        System.out.println(stack);                           // TOP [ D -> C -> B -> A ] BOTTOM
        System.out.println("Pop: " + stack.pop());         // D
        System.out.println(stack);                           // TOP [ C -> B -> A ] BOTTOM

        System.out.println("\n=== Dynamic Queue ===");
        Queue<String> queue = new Queue<>();
        queue.enQueue("A"); queue.enQueue("B"); queue.enQueue("C");
        System.out.println(queue);                           // FRONT [ A -> B -> C ] REAR
        System.out.println("DeQueue: " + queue.deQueue()); // A
        queue.enQueue("D"); queue.enQueue("E");
        System.out.println(queue);                           // FRONT [ B -> C -> D -> E ] REAR

        System.out.println("\n=== Static Stack ===");
        StaticStack<String> ss = new StaticStack<>(5);
        ss.push("A"); ss.push("B"); ss.push("C"); ss.push("D");
        System.out.println(ss);                              // TOP [ D -> C -> B -> A ] BOTTOM
        System.out.println("isFull: " + ss.isFull());      // false (4 out of 5 slots used)
        System.out.println("Pop: "    + ss.pop());          // D
        System.out.println(ss);                              // TOP [ C -> B -> A ] BOTTOM

        System.out.println("\n=== Static Queue ===");
        StaticQueue<String> sq = new StaticQueue<>(6);
        sq.enQueue("A"); sq.enQueue("B"); sq.enQueue("C");
        System.out.println(sq);                              // FRONT [ A -> B -> C ] REAR
        System.out.println("DeQueue: " + sq.deQueue());    // A
        sq.enQueue("D"); sq.enQueue("E");
        System.out.println(sq);                              // FRONT [ B -> C -> D -> E ] REAR
        System.out.println("isFull: " + sq.isFull());      // false (4 out of 6 slots used)
    }
}