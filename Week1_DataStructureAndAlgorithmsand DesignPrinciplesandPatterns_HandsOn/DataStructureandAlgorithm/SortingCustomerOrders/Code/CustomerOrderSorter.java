import java.util.Arrays;

class Order {
    private String orderId;
    private String customerName;
    private double totalPrice;

    public Order(String orderId, String customerName, double totalPrice) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.totalPrice = totalPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String toString() {
        return String.format("Order ID: %-6s | Customer: %-10s | Total: $%.2f", orderId, customerName, totalPrice);
    }
}

public class CustomerOrderSorter {


    public static void bubbleSort(Order[] orders) {
        int n = orders.length;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (orders[j].getTotalPrice() > orders[j + 1].getTotalPrice()) {
                    Order temp = orders[j];
                    orders[j] = orders[j + 1];
                    orders[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }

    public static void quickSort(Order[] orders, int low, int high) {
        if (low < high) {
            int pi = partition(orders, low, high);
            quickSort(orders, low, pi - 1);
            quickSort(orders, pi + 1, high);
        }
    }

    private static int partition(Order[] orders, int low, int high) {
        double pivot = orders[high].getTotalPrice();
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (orders[j].getTotalPrice() <= pivot) {
                i++;
                Order temp = orders[i];
                orders[i] = orders[j];
                orders[j] = temp;
            }
        }
        Order temp = orders[i + 1];
        orders[i + 1] = orders[high];
        orders[high] = temp;
        return i + 1;
    }


    public static void displayOrders(Order[] orders, String message) {
        System.out.println("\n" + message);
        System.out.println("=".repeat(50));
        for (Order order : orders) {
            System.out.println(order);
        }
    }

    public static void main(String[] args) {
        Order[] sampleOrders = {
                new Order("A101", "Alice", 275.50),
                new Order("A102", "Bob", 120.00),
                new Order("A103", "Charlie", 999.99),
                new Order("A104", "Daisy", 450.25),
                new Order("A105", "Ethan", 300.75)
        };


        Order[] bubbleSorted = Arrays.copyOf(sampleOrders, sampleOrders.length);
        long startBubble = System.nanoTime();
        bubbleSort(bubbleSorted);
        long endBubble = System.nanoTime();
        displayOrders(bubbleSorted, "Orders Sorted by Bubble Sort");
        System.out.printf("🟡 Bubble Sort Execution Time: %.2f ms\n", (endBubble - startBubble) / 1e6);


        Order[] quickSorted = Arrays.copyOf(sampleOrders, sampleOrders.length);
        long startQuick = System.nanoTime();
        quickSort(quickSorted, 0, quickSorted.length - 1);
        long endQuick = System.nanoTime();
        displayOrders(quickSorted, "Orders Sorted by Quick Sort");
        System.out.printf("🟢 Quick Sort Execution Time: %.2f ms\n", (endQuick - startQuick) / 1e6);

        System.out.println("\n📊 Performance Analysis:");
        System.out.println("- Bubble Sort: O(n²) — inefficient for large datasets.");
        System.out.println("- Quick Sort : O(n log n) average — faster and widely used in real-world applications.");
        System.out.println("- Quick Sort is preferred for its divide-and-conquer approach and better scalability.");
    }
}
