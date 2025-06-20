import java.util.*;

/**
 * Financial Projection System using Recursive Strategies
 *
 * Understanding Recursion:
 * Recursion is a method where a function calls itself to solve a problem
 * by breaking it down into smaller instances. It includes:
 * - Base Case: Condition to stop recursion.
 * - Recursive Case: Function calls itself with updated input.
 *
 * Relevance to Finance:
 * - Naturally models time-based growth.
 * - Simplifies compound interest and simulations.
 */

class ProjectionEngine {

    // Used to store previously computed results
    private Map<String, Double> cache;

    public ProjectionEngine() {
        cache = new HashMap<>();
    }

    /**
     * 1. Simple Recursive Forecast
     * Formula: FV = PV * (1 + r)^n
     */
    public double forecastBasic(double principal, double rate, int years) {
        if (years == 0) return principal;
        return forecastBasic(principal * (1 + rate), rate, years - 1);
    }

    /**
     * 2. Memoized Recursive Forecast
     * Optimized with dynamic programming (caching)
     */
    public double forecastWithCache(double principal, double rate, int years) {
        String key = principal + "-" + rate + "-" + years;
        if (cache.containsKey(key)) return cache.get(key);

        double result = (years == 0) ? principal : forecastWithCache(principal * (1 + rate), rate, years - 1);
        cache.put(key, result);
        return result;
    }

    /**
     * 3. Recursive CAGR Projection
     * Predicts future value using compound annual growth rate
     */
    public double projectUsingCAGR(double[] pastValues, int futureYears) {
        if (pastValues.length < 2) {
            throw new IllegalArgumentException("At least two data points are required.");
        }

        double start = pastValues[0];
        double end = pastValues[pastValues.length - 1];
        int duration = pastValues.length - 1;
        double cagr = Math.pow(end / start, 1.0 / duration) - 1;

        System.out.printf("Computed CAGR: %.4f (%.2f%%)%n", cagr, cagr * 100);
        return forecastBasic(end, cagr, futureYears);
    }

    /**
     * 4. Fibonacci-Inspired Growth
     * Represents trends resembling Fibonacci sequences
     */
    public double fibonacciBasedGrowth(double value, int term) {
        return value * computeFibonacci(term);
    }

    private double computeFibonacci(int n) {
        String key = "fib" + n;
        if (cache.containsKey(key)) return cache.get(key);

        double result = (n <= 1) ? n : computeFibonacci(n - 1) + computeFibonacci(n - 2);
        cache.put(key, result);
        return result;
    }

    /**
     * 5. Recursive Monte Carlo Forecast
     * Simulates multiple financial outcomes using randomness
     */
    public double[] simulateMonteCarlo(double startValue, double avgReturn, double stdDev, int periods, int trials) {
        double[] outcomes = new double[trials];
        Random rng = new Random();

        for (int i = 0; i < trials; i++) {
            outcomes[i] = simulatePath(startValue, avgReturn, stdDev, periods, rng);
        }

        return outcomes;
    }

    private double simulatePath(double current, double mean, double volatility, int periodsLeft, Random rng) {
        if (periodsLeft == 0) return current;
        double fluctuation = mean + volatility * rng.nextGaussian();
        return simulatePath(current * (1 + fluctuation), mean, volatility, periodsLeft - 1, rng);
    }

    /**
     * 6. Tail-Recursive Optimization
     * Uses an accumulator for improved efficiency
     */
    public double forecastTailRecursive(double principal, double rate, int years) {
        return tailRecursiveHelper(principal, rate, years, principal);
    }

    private double tailRecursiveHelper(double base, double rate, int years, double accumulated) {
        if (years == 0) return accumulated;
        return tailRecursiveHelper(base, rate, years - 1, accumulated * (1 + rate));
    }

    public void resetCache() {
        cache.clear();
    }

    public void displayCacheStats() {
        System.out.println("Cached entries: " + cache.size());
    }
}

class StatsUtility {

    public static void evaluate(String label, Runnable task) {
        System.out.println("\n>>> Running: " + label);
        long start = System.nanoTime();
        task.run();
        long end = System.nanoTime();
        System.out.printf("Duration: %,d ns (%.2f ms)%n", (end - start), (end - start) / 1e6);
    }

    public static void displayStats(double[] values, String label) {
        if (values.length == 0) return;

        Arrays.sort(values);
        double mean = Arrays.stream(values).average().orElse(0.0);
        double median = values[values.length / 2];
        double min = values[0], max = values[values.length - 1];

        System.out.println("\n--- " + label + " Summary ---");
        System.out.printf("Mean: $%.2f, Median: $%.2f, Min: $%.2f, Max: $%.2f, Range: $%.2f%n",
                mean, median, min, max, max - min);
    }
}

public class FinancialProjectionApp {

    public static void main(String[] args) {
        ProjectionEngine engine = new ProjectionEngine();

        double principal = 10_000;
        double rate = 0.08;
        int duration = 10;

        System.out.println("=== Financial Projection System ===");

        StatsUtility.evaluate("Basic Recursive Forecast", () -> {
            double result = engine.forecastBasic(principal, rate, duration);
            System.out.printf("Projected Value (Basic): $%.2f%n", result);
        });

        StatsUtility.evaluate("Memoized Forecast", () -> {
            double result = engine.forecastWithCache(principal, rate, duration);
            System.out.printf("Projected Value (Memoized): $%.2f%n", result);
        });

        engine.displayCacheStats();

        StatsUtility.evaluate("Tail Recursive Forecast", () -> {
            double result = engine.forecastTailRecursive(principal, rate, duration);
            System.out.printf("Projected Value (Tail Recursion): $%.2f%n", result);
        });

        double[] historicalData = {10_000, 10_800, 11_664, 12_597, 13_605, 14_693};
        System.out.println("\nHistorical Records: " + Arrays.toString(historicalData));

        StatsUtility.evaluate("CAGR-Based Projection", () -> {
            double result = engine.projectUsingCAGR(historicalData, 5);
            System.out.printf("Predicted Value after 5 Years: $%.2f%n", result);
        });

        StatsUtility.evaluate("Fibonacci Growth Simulation", () -> {
            double result = engine.fibonacciBasedGrowth(1_000, 10);
            System.out.printf("Fibonacci Growth Estimate: $%.2f%n", result);
        });

        StatsUtility.evaluate("Monte Carlo Simulation", () -> {
            int trials = 1000;
            double[] results = engine.simulateMonteCarlo(principal, 0.07, 0.15, duration, trials);
            StatsUtility.displayStats(results, "Monte Carlo Forecast");
        });

        System.out.println("\n=== Time Complexity Overview ===");
        System.out.println("Basic Recursive: O(n), Stack space: O(n)");
        System.out.println("Memoized Recursive: O(n), Cache + Stack: O(n)");
        System.out.println("Tail Recursive: O(n), Space optimized if tail-call elimination available");
        System.out.println("Fibonacci without Memoization: O(2^n)");
        System.out.println("Fibonacci with Memoization: O(n)");
        System.out.println();

        System.out.println("=== Optimization Techniques ===");
        System.out.println("1. Memoization for overlapping subproblems");
        System.out.println("2. Tail Recursion to reduce stack usage");
        System.out.println("3. Iterative rewriting to improve space complexity");
        System.out.println("4. Dynamic Programming (Bottom-up)");
        System.out.println("5. Mathematical shortcuts for compound formulas");

        engine.displayCacheStats();
    }
}
