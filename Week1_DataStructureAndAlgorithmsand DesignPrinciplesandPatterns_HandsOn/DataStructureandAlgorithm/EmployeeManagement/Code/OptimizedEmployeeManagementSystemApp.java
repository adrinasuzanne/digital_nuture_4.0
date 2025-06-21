import java.util.*;
import java.util.stream.Collectors;

class Employee {
    private final int id;
    private String name;
    private String role;
    private double salary;
    private boolean isActive;

    public Employee(int id, String name, String role, double salary) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.salary = salary;
        this.isActive = true;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public double getSalary() { return salary; }
    public boolean isActive() { return isActive; }

    public void setName(String name) { this.name = name; }
    public void setRole(String role) { this.role = role; }
    public void setSalary(double salary) { this.salary = salary; }
    public void deactivate() { this.isActive = false; }
    public void activate() { this.isActive = true; }

    @Override
    public String toString() {
        return String.format("[ID=%d, Name='%s', Role='%s', Salary=$%.2f, Status=%s]",
                id, name, role, salary, isActive ? "Active" : "Inactive");
    }

    @Override
    public boolean equals(Object obj) {
        return (this == obj || (obj instanceof Employee other && this.id == other.id));
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}

class OptimizedEmployeeManagementSystem {
    private final Map<Integer, Employee> idIndex = new HashMap<>();
    private final Map<String, Set<Employee>> nameIndex = new HashMap<>();
    private final Map<String, Set<Employee>> roleIndex = new HashMap<>();
    private final TreeMap<Integer, Employee> orderedById = new TreeMap<>();
    private final List<Employee> allEmployees = new ArrayList<>();
    private final Queue<Integer> reusableIds = new LinkedList<>();

    private int nextId = 1001;
    private int opCount = 0;
    private long totalSearchTime = 0;

    public boolean addEmployee(String name, String role, double salary) {
        long start = System.nanoTime();

        int id = reusableIds.isEmpty() ? nextId++ : reusableIds.poll();
        Employee emp = new Employee(id, name, role, salary);

        idIndex.put(id, emp);
        orderedById.put(id, emp);
        allEmployees.add(emp);

        nameIndex.computeIfAbsent(name.toLowerCase(), k -> new HashSet<>()).add(emp);
        roleIndex.computeIfAbsent(role.toLowerCase(), k -> new HashSet<>()).add(emp);

        long end = System.nanoTime();
        totalSearchTime += (end - start);
        opCount++;

        System.out.println("✅ Employee added: " + emp);
        return true;
    }

    public boolean removeEmployee(int id) {
        long start = System.nanoTime();

        Employee emp = idIndex.get(id);
        if (emp == null || !emp.isActive()) {
            System.out.println("❌ Employee not found or already removed.");
            return false;
        }

        emp.deactivate();
        reusableIds.offer(id);

        long end = System.nanoTime();
        totalSearchTime += (end - start);
        opCount++;

        System.out.println("🗑️ Soft deleted employee with ID: " + id);
        return true;
    }

    public boolean hardDelete(int id) {
        Employee emp = idIndex.remove(id);
        if (emp == null) {
            System.out.println("❌ Employee not found.");
            return false;
        }

        allEmployees.remove(emp);
        orderedById.remove(id);
        reusableIds.offer(id);

        nameIndex.getOrDefault(emp.getName().toLowerCase(), Set.of()).remove(emp);
        roleIndex.getOrDefault(emp.getRole().toLowerCase(), Set.of()).remove(emp);

        System.out.println("🧹 Hard deleted employee with ID: " + id);
        return true;
    }

    public Employee searchById(int id) {
        long start = System.nanoTime();

        Employee emp = idIndex.get(id);

        long end = System.nanoTime();
        totalSearchTime += (end - start);
        opCount++;

        if (emp != null) {
            System.out.println("🔍 Found: " + emp);
        } else {
            System.out.println("❌ No employee found with ID: " + id);
        }

        return emp;
    }

    public List<Employee> searchByName(String name) {
        long start = System.nanoTime();

        Set<Employee> results = nameIndex.getOrDefault(name.toLowerCase(), Set.of());

        long end = System.nanoTime();
        totalSearchTime += (end - start);
        opCount++;

        List<Employee> activeEmployees = results.stream().filter(Employee::isActive).collect(Collectors.toList());

        if (activeEmployees.isEmpty()) {
            System.out.println("❌ No active employee found with name: " + name);
        } else {
            System.out.println("🔍 Employees named '" + name + "':");
            activeEmployees.forEach(System.out::println);
        }

        return activeEmployees;
    }

    public List<Employee> searchByRole(String role) {
        long start = System.nanoTime();

        Set<Employee> results = roleIndex.getOrDefault(role.toLowerCase(), Set.of());

        long end = System.nanoTime();
        totalSearchTime += (end - start);
        opCount++;

        List<Employee> activeEmployees = results.stream().filter(Employee::isActive).collect(Collectors.toList());

        if (activeEmployees.isEmpty()) {
            System.out.println("❌ No active employee found with role: " + role);
        } else {
            System.out.println("🔍 Employees in role '" + role + "':");
            activeEmployees.forEach(System.out::println);
        }

        return activeEmployees;
    }

    public List<Employee> getAllActiveEmployees() {
        return allEmployees.stream().filter(Employee::isActive).collect(Collectors.toList());
    }

    public void displayActiveEmployees() {
        System.out.println("\n📄 List of Active Employees:");
        List<Employee> actives = getAllActiveEmployees();
        if (actives.isEmpty()) {
            System.out.println("No active employees found.");
        } else {
            actives.forEach(e -> System.out.println("• " + e));
        }
    }

    public void displaySortedById() {
        System.out.println("\n📊 Employees Sorted by ID:");
        for (var entry : orderedById.entrySet()) {
            if (entry.getValue().isActive()) {
                System.out.println("• " + entry.getValue());
            }
        }
    }

    public void showSystemStats() {
        long avgTime = opCount > 0 ? totalSearchTime / opCount : 0;
        System.out.println("\n📈 System Stats:");
        System.out.println("• Total employees: " + allEmployees.size());
        System.out.println("• Active employees: " + getAllActiveEmployees().size());
        System.out.println("• Total operations: " + opCount);
        System.out.println("• Avg search time: " + avgTime + " ns");
        System.out.println("• Reusable IDs: " + reusableIds.size());
        System.out.println("• Name index size: " + nameIndex.size());
        System.out.println("• Role index size: " + roleIndex.size());
    }
}

public class OptimizedEmployeeManagementSystemApp {
    public static void main(String[] args) {
        OptimizedEmployeeManagementSystem system = new OptimizedEmployeeManagementSystem();

        system.addEmployee("Alice", "Developer", 70000);
        system.addEmployee("Bob", "Designer", 65000);
        system.addEmployee("Charlie", "Manager", 90000);
        system.addEmployee("Alice", "Tester", 50000);

        system.displayActiveEmployees();

        system.searchById(1002);
        system.searchByName("Alice");
        system.searchByRole("Developer");

        system.removeEmployee(1002); // Soft delete Bob
        system.hardDelete(1003);     // Hard delete Charlie

        system.displayActiveEmployees();
        system.displaySortedById();
        system.showSystemStats();
    }
}
