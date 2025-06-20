import java.util.*;

/**
 * Represents a product in the online catalog
 */
class CatalogItem {
    private int itemId;
    private String itemName;
    private String itemCategory;

    public CatalogItem(int itemId, String itemName, String itemCategory) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemCategory = itemCategory;
    }

    public int getItemId() { return itemId; }
    public String getItemName() { return itemName; }
    public String getItemCategory() { return itemCategory; }

    @Override
    public String toString() {
        return String.format("CatalogItem{id=%d, name='%s', category='%s'}", itemId, itemName, itemCategory);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CatalogItem)) return false;
        CatalogItem other = (CatalogItem) obj;
        return this.itemId == other.itemId;
    }
}

/**
 * OnlineStore handles item search and catalog management
 */
class OnlineStore {
    private List<CatalogItem> catalogList;
    private List<CatalogItem> sortedCatalogList;

    public OnlineStore() {
        catalogList = new ArrayList<>();
        sortedCatalogList = new ArrayList<>();
    }

    public void addItem(CatalogItem item) {
        catalogList.add(item);

        int index = Collections.binarySearch(
                sortedCatalogList,
                item,
                Comparator.comparingInt(CatalogItem::getItemId)
        );
        if (index < 0) index = -index - 1;
        sortedCatalogList.add(index, item);
    }

    public SearchOutcome searchLinearById(int id) {
        long start = System.nanoTime();
        int steps = 0;

        for (int i = 0; i < catalogList.size(); i++) {
            steps++;
            if (catalogList.get(i).getItemId() == id) {
                return new SearchOutcome(catalogList.get(i), i, steps, System.nanoTime() - start, "Linear Search");
            }
        }

        return new SearchOutcome(null, -1, steps, System.nanoTime() - start, "Linear Search");
    }

    public SearchOutcome searchBinaryById(int id) {
        long start = System.nanoTime();
        int steps = 0;
        int left = 0, right = sortedCatalogList.size() - 1;

        while (left <= right) {
            steps++;
            int mid = (left + right) / 2;
            CatalogItem midItem = sortedCatalogList.get(mid);

            if (midItem.getItemId() == id) {
                return new SearchOutcome(midItem, mid, steps, System.nanoTime() - start, "Binary Search");
            } else if (midItem.getItemId() < id) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return new SearchOutcome(null, -1, steps, System.nanoTime() - start, "Binary Search");
    }

    public SearchOutcome searchLinearByName(String name) {
        long start = System.nanoTime();
        int steps = 0;

        for (int i = 0; i < catalogList.size(); i++) {
            steps++;
            if (catalogList.get(i).getItemName().equalsIgnoreCase(name)) {
                return new SearchOutcome(catalogList.get(i), i, steps, System.nanoTime() - start, "Linear Search (Name)");
            }
        }

        return new SearchOutcome(null, -1, steps, System.nanoTime() - start, "Linear Search (Name)");
    }

    public int getTotalItems() {
        return catalogList.size();
    }
}

/**
 * Stores search outcome and performance metrics
 */
class SearchOutcome {
    private CatalogItem foundItem;
    private int position;
    private int comparisons;
    private long timeElapsedNanos;
    private String method;

    public SearchOutcome(CatalogItem item, int index, int comparisons, long time, String algorithm) {
        this.foundItem = item;
        this.position = index;
        this.comparisons = comparisons;
        this.timeElapsedNanos = time;
        this.method = algorithm;
    }

    public boolean isItemFound() { return foundItem != null; }

    public int getComparisons() {
        return comparisons;
    }

    public double getExecutionTimeMicroseconds() {
        return timeElapsedNanos / 1000.0;
    }

    @Override
    public String toString() {
        if (isItemFound()) {
            return String.format("%s: Found %s at index %d\nComparisons: %d, Time: %.2f μs",
                    method, foundItem, position, comparisons, getExecutionTimeMicroseconds());
        } else {
            return String.format("%s: Item not found\nComparisons: %d, Time: %.2f μs",
                    method, comparisons, getExecutionTimeMicroseconds());
        }
    }
}


/**
 * Main driver for testing the OnlineStore search system
 */
public class OnlineStoreDemo {
    public static void main(String[] args) {
        OnlineStore store = new OnlineStore();

        System.out.println("=== Initializing Product Catalog ===");
        loadSampleData(store);
        System.out.println("Catalog Size: " + store.getTotalItems() + " items\n");

        int existingId = 105;
        int missingId = 999;

        System.out.println("=== Running Search Tests ===");
        System.out.println("Searching for ID: " + existingId);
        System.out.println(store.searchLinearById(existingId));
        System.out.println(store.searchBinaryById(existingId));
        System.out.println();

        System.out.println("Searching for Non-Existent ID: " + missingId);
        System.out.println(store.searchLinearById(missingId));
        System.out.println(store.searchBinaryById(missingId));
        System.out.println();

        System.out.println("Searching for Name: 'Laptop'");
        System.out.println(store.searchLinearByName("Laptop"));
        System.out.println();

        benchmarkSearch(store);
        summarizeAnalysis();
    }

    private static void loadSampleData(OnlineStore store) {
        CatalogItem[] items = {
                new CatalogItem(101, "Smartphone", "Electronics"),
                new CatalogItem(103, "Laptop", "Electronics"),
                new CatalogItem(105, "Headphones", "Electronics"),
                new CatalogItem(107, "Coffee Maker", "Appliances"),
                new CatalogItem(109, "Running Shoes", "Sports"),
                new CatalogItem(111, "Backpack", "Accessories"),
                new CatalogItem(113, "Tablet", "Electronics"),
                new CatalogItem(115, "Fitness Tracker", "Sports"),
                new CatalogItem(117, "Desk Chair", "Furniture"),
                new CatalogItem(119, "Water Bottle", "Sports")
        };

        for (CatalogItem item : items) {
            store.addItem(item);
        }
    }

    private static void benchmarkSearch(OnlineStore store) {
        System.out.println("=== Performance Benchmark ===");

        int[] testIds = {101, 107, 115, 119, 999};
        long totalLinear = 0, totalBinary = 0;

        for (int id : testIds) {
            totalLinear += store.searchLinearById(id).getComparisons();
            totalBinary += store.searchBinaryById(id).getComparisons();
        }

        double avgLinear = totalLinear / (double) testIds.length;
        double avgBinary = totalBinary / (double) testIds.length;

        System.out.printf("Average Comparisons - Linear: %.1f, Binary: %.1f\n", avgLinear, avgBinary);
        System.out.printf("Binary Search is approximately %.1fx more efficient\n\n", avgLinear / avgBinary);
    }

    private static void summarizeAnalysis() {
        System.out.println("=== Search Algorithm Overview ===");

        System.out.println("Linear Search:");
        System.out.println("• Time: O(n), Space: O(1)");
        System.out.println("• Use Case: Small or unsorted data");
        System.out.println();

        System.out.println("Binary Search:");
        System.out.println("• Time: O(log n), Space: O(1)");
        System.out.println("• Use Case: Sorted data, fast lookup");
        System.out.println();

        System.out.println("Recommendation:");
        System.out.println("• ID Search: Prefer Binary Search with sorted list");
        System.out.println("• Name Search: Use indexing or advanced text search tools (e.g. Trie, Elasticsearch)");
        System.out.println();

        System.out.println("Scalability Note:");
        System.out.println("• For 1,000,000 items: Binary ~20 steps vs Linear ~500,000 steps!");
    }
}
