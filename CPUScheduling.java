import java.util.*;

/**
 * CPU Scheduling Simulator
 * Demonstrates FCFS, SJF (Non-Preemptive), and Priority Scheduling (Non-Preemptive)
 * Computes average waiting time and average turnaround time for N processes.
 */
public class CPUScheduling {

    static Scanner sc = new Scanner(System.in);

    // Represents a single process
    static class Process {
        int id;
        int arrivalTime;
        int burstTime;
        int priority;       // lower number = higher priority
        int waitingTime;
        int turnaroundTime;
        int completionTime;

        Process(int id, int arrivalTime, int burstTime, int priority) {
            this.id = id;
            this.arrivalTime = arrivalTime;
            this.burstTime = burstTime;
            this.priority = priority;
        }
    }

    public static void main(String[] args) {
        int choice;
        do {
            printMenu();
            choice = readInt();
            switch (choice) {
                case 1:
                    runFCFS();
                    break;
                case 2:
                    runSJF();
                    break;
                case 3:
                    runPriority();
                    break;
                case 4:
                    System.out.println("Exiting program. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1-4.");
            }
        } while (choice != 4);

        sc.close();
    }

    static void printMenu() {
        System.out.println("\n===================================");
        System.out.println("     CPU SCHEDULING ALGORITHMS");
        System.out.println("===================================");
        System.out.println("1. First Come First Served (FCFS)");
        System.out.println("2. Shortest Job First (SJF - Non-Preemptive)");
        System.out.println("3. Priority Scheduling (Non-Preemptive)");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    static int readInt() {
        while (!sc.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            sc.next();
        }
        return sc.nextInt();
    }

    // Reads process data from the user. needPriority controls whether the
    // priority value is requested (not needed for FCFS / SJF).
    static List<Process> inputProcesses(boolean needPriority) {
        System.out.print("Enter number of processes (N): ");
        int n = readInt();
        while (n <= 0) {
            System.out.print("N must be greater than 0. Enter again: ");
            n = readInt();
        }

        List<Process> list = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            System.out.println("\nProcess P" + i + ":");

            System.out.print("  Arrival Time: ");
            int at = readInt();

            System.out.print("  Burst Time: ");
            int bt = readInt();
            while (bt <= 0) {
                System.out.print("  Burst Time must be > 0. Enter again: ");
                bt = readInt();
            }

            int pr = 0;
            if (needPriority) {
                System.out.print("  Priority (lower number = higher priority): ");
                pr = readInt();
            }

            list.add(new Process(i, at, bt, pr));
        }
        return list;
    }

    // ---------------------- FCFS ----------------------
    static void runFCFS() {
        List<Process> processes = inputProcesses(false);

        // Order of execution = order of arrival (tie broken by input order/id)
        List<Process> order = new ArrayList<>(processes);
        order.sort((a, b) -> a.arrivalTime != b.arrivalTime
                ? a.arrivalTime - b.arrivalTime
                : a.id - b.id);

        int currentTime = 0;
        for (Process p : order) {
            if (currentTime < p.arrivalTime) {
                currentTime = p.arrivalTime;
            }
            p.waitingTime = currentTime - p.arrivalTime;
            currentTime += p.burstTime;
            p.completionTime = currentTime;
            p.turnaroundTime = p.completionTime - p.arrivalTime;
        }

        printResults("FCFS Scheduling", processes, false);
    }

    // ---------------------- SJF (Non-Preemptive) ----------------------
    static void runSJF() {
        List<Process> processes = inputProcesses(false);
        int n = processes.size();
        boolean[] done = new boolean[n];
        int completed = 0;
        int currentTime = 0;

        while (completed < n) {
            int idx = -1;

            // Find the shortest job among arrived, not-yet-done processes
            for (int i = 0; i < n; i++) {
                if (done[i]) continue;
                Process p = processes.get(i);
                if (p.arrivalTime > currentTime) continue;

                if (idx == -1) {
                    idx = i;
                } else {
                    Process best = processes.get(idx);
                    if (p.burstTime < best.burstTime
                            || (p.burstTime == best.burstTime && p.arrivalTime < best.arrivalTime)
                            || (p.burstTime == best.burstTime && p.arrivalTime == best.arrivalTime && p.id < best.id)) {
                        idx = i;
                    }
                }
            }

            if (idx == -1) {
                // No process has arrived yet; jump to the next arrival time
                int nextArrival = Integer.MAX_VALUE;
                for (int i = 0; i < n; i++) {
                    if (!done[i]) {
                        nextArrival = Math.min(nextArrival, processes.get(i).arrivalTime);
                    }
                }
                currentTime = nextArrival;
                continue;
            }

            Process p = processes.get(idx);
            p.waitingTime = currentTime - p.arrivalTime;
            currentTime += p.burstTime;
            p.completionTime = currentTime;
            p.turnaroundTime = p.completionTime - p.arrivalTime;
            done[idx] = true;
            completed++;
        }

        printResults("SJF Scheduling (Non-Preemptive)", processes, false);
    }

    // ---------------------- Priority Scheduling (Non-Preemptive) ----------------------
    static void runPriority() {
        List<Process> processes = inputProcesses(true);
        int n = processes.size();
        boolean[] done = new boolean[n];
        int completed = 0;
        int currentTime = 0;

        while (completed < n) {
            int idx = -1;

            // Find the highest priority (lowest priority number) among arrived processes
            for (int i = 0; i < n; i++) {
                if (done[i]) continue;
                Process p = processes.get(i);
                if (p.arrivalTime > currentTime) continue;

                if (idx == -1) {
                    idx = i;
                } else {
                    Process best = processes.get(idx);
                    if (p.priority < best.priority
                            || (p.priority == best.priority && p.arrivalTime < best.arrivalTime)
                            || (p.priority == best.priority && p.arrivalTime == best.arrivalTime && p.id < best.id)) {
                        idx = i;
                    }
                }
            }

            if (idx == -1) {
                int nextArrival = Integer.MAX_VALUE;
                for (int i = 0; i < n; i++) {
                    if (!done[i]) {
                        nextArrival = Math.min(nextArrival, processes.get(i).arrivalTime);
                    }
                }
                currentTime = nextArrival;
                continue;
            }

            Process p = processes.get(idx);
            p.waitingTime = currentTime - p.arrivalTime;
            currentTime += p.burstTime;
            p.completionTime = currentTime;
            p.turnaroundTime = p.completionTime - p.arrivalTime;
            done[idx] = true;
            completed++;
        }

        printResults("Priority Scheduling (Non-Preemptive)", processes, true);
    }

    // ---------------------- Output ----------------------
    static void printResults(String title, List<Process> processes, boolean showPriority) {
        // Always display in original process order (P1, P2, ...)
        List<Process> display = new ArrayList<>(processes);
        display.sort((a, b) -> a.id - b.id);

        System.out.println("\n--- " + title + " : Results ---");
        if (showPriority) {
            System.out.printf("%-6s%-10s%-10s%-10s%-14s%-12s%-12s%n",
                    "PID", "Arrival", "Burst", "Priority", "Completion", "Waiting", "Turnaround");
        } else {
            System.out.printf("%-6s%-10s%-10s%-14s%-12s%-12s%n",
                    "PID", "Arrival", "Burst", "Completion", "Waiting", "Turnaround");
        }

        int totalWaiting = 0;
        int totalTurnaround = 0;

        for (Process p : display) {
            if (showPriority) {
                System.out.printf("%-6s%-10d%-10d%-10d%-14d%-12d%-12d%n",
                        "P" + p.id, p.arrivalTime, p.burstTime, p.priority,
                        p.completionTime, p.waitingTime, p.turnaroundTime);
            } else {
                System.out.printf("%-6s%-10d%-10d%-14d%-12d%-12d%n",
                        "P" + p.id, p.arrivalTime, p.burstTime,
                        p.completionTime, p.waitingTime, p.turnaroundTime);
            }
            totalWaiting += p.waitingTime;
            totalTurnaround += p.turnaroundTime;
        }

        double avgWaiting = (double) totalWaiting / display.size();
        double avgTurnaround = (double) totalTurnaround / display.size();

        System.out.printf("%nAverage Waiting Time    = %.2f%n", avgWaiting);
        System.out.printf("Average Turnaround Time = %.2f%n", avgTurnaround);
    }
}
