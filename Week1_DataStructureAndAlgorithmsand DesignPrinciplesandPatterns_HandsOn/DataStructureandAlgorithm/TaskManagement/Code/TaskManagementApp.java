import java.util.*;

class WorkItem {
    private int id;
    private String title;
    private String state;

    public WorkItem(int id, String title, String state) {
        this.id = id;
        this.title = title;
        this.state = (state == null || state.isEmpty()) ? "Pending" : state;
    }

    public WorkItem(int id, String title) {
        this(id, title, "Pending");
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getState() {
        return state;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return String.format("WorkItem{ID=%d, Title='%s', Status='%s'}", id, title, state);
    }
}

class Node {
    WorkItem data;
    Node next;

    public Node(WorkItem data) {
        this.data = data;
        this.next = null;
    }
}

class TaskService {
    private Node head;
    private int totalTasks;

    public TaskService() {
        this.head = null;
        this.totalTasks = 0;
    }

    public boolean addTask(WorkItem item) {
        if (item == null || findTaskById(item.getId()) != null) {
            System.out.println("Invalid or duplicate task: " + item);
            return false;
        }

        Node newNode = new Node(item);
        newNode.next = head;
        head = newNode;
        totalTasks++;
        return true;
    }

    public WorkItem findTaskById(int id) {
        Node current = head;
        while (current != null) {
            if (current.data.getId() == id) {
                return current.data;
            }
            current = current.next;
        }
        return null;
    }

    public boolean removeTask(int id) {
        if (head == null) return false;

        if (head.data.getId() == id) {
            head = head.next;
            totalTasks--;
            return true;
        }

        Node current = head;
        while (current.next != null && current.next.data.getId() != id) {
            current = current.next;
        }

        if (current.next == null) return false;

        current.next = current.next.next;
        totalTasks--;
        return true;
    }

    public void displayTasks() {
        if (head == null) {
            System.out.println("No tasks found.");
            return;
        }

        System.out.println("\n--- Task List ---");
        Node current = head;
        int count = 1;
        while (current != null) {
            System.out.println(count++ + ". " + current.data);
            current = current.next;
        }
        System.out.println("Total Tasks: " + totalTasks);
    }

    public List<WorkItem> filterTasksByStatus(String status) {
        List<WorkItem> results = new ArrayList<>();
        Node current = head;

        while (current != null) {
            if (current.data.getState().equalsIgnoreCase(status)) {
                results.add(current.data);
            }
            current = current.next;
        }
        return results;
    }

    public boolean updateStatus(int id, String newStatus) {
        WorkItem task = findTaskById(id);
        if (task != null) {
            task.setState(newStatus);
            return true;
        }
        return false;
    }

    public int countTasks() {
        return totalTasks;
    }

    public boolean isTaskListEmpty() {
        return head == null;
    }
}

public class TaskManagementApp {
    public static void main(String[] args) {
        TaskService service = new TaskService();
        System.out.println("=== Task Management Console ===");

        service.addTask(new WorkItem(101, "Design system architecture", "Pending"));
        service.addTask(new WorkItem(102, "Develop REST API", "InProgress"));
        service.addTask(new WorkItem(103, "Write test cases"));  // Defaults to Pending
        service.addTask(new WorkItem(104, "Deploy to production", "Completed"));

        service.addTask(new WorkItem(101, "Duplicate Task"));  // Should be rejected


        service.displayTasks();


        System.out.println("\nSearching for task with ID 102:");
        WorkItem found = service.findTaskById(102);
        System.out.println(found != null ? found : "Task not found.");

        System.out.println("\nUpdating task 103 status to 'Completed':");
        System.out.println(service.updateStatus(103, "Completed") ? "Status updated." : "Task not found.");


        System.out.println("\nTasks with status 'Completed':");
        for (WorkItem item : service.filterTasksByStatus("Completed")) {
            System.out.println(" - " + item);
        }


        System.out.println("\nRemoving task with ID 102:");
        System.out.println(service.removeTask(102) ? "Task deleted." : "Task not found.");

        System.out.println("\nUpdated Task List:");
        service.displayTasks();

        System.out.println("\nAdding 1000 sample tasks for performance test...");
        long startTime = System.nanoTime();
        for (int i = 200; i < 1200; i++) {
            service.addTask(new WorkItem(i, "Task " + i, "Pending"));
        }
        long endTime = System.nanoTime();
        System.out.println("Time taken to add 1000 tasks: " + (endTime - startTime) + " ns");
        System.out.println("Total tasks after bulk insert: " + service.countTasks());


        System.out.println("\nSearching for Task ID 750:");
        startTime = System.nanoTime();
        WorkItem result = service.findTaskById(750);
        endTime = System.nanoTime();
        System.out.println((result != null ? "Found: " + result : "Not found.") +
                " Search time: " + (endTime - startTime) + " ns");
    }
}
