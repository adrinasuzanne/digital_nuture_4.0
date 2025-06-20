// File: SingletonPatternExample.java

public class SingletonPatternExample {

    // Singleton Logger class
    static class Logger {
        // Volatile static instance for thread safety
        private static volatile Logger instance;

        // Private constructor prevents instantiation from outside
        private Logger() {
            System.out.println("Logger initialized.");
        }

        // Public method to provide access to the singleton instance
        public static Logger getInstance() {
            if (instance == null) {
                synchronized (Logger.class) {
                    if (instance == null) {
                        instance = new Logger();
                    }
                }
            }
            return instance;
        }

        // Utility logging method
        public void log(String message) {
            System.out.println("[LOG] " + message);
        }
    }

    // Main method to test Singleton functionality
    public static void main(String[] args) {
        System.out.println("Testing Singleton Logger...");

        Logger logger1 = Logger.getInstance();
        logger1.log("Logging from logger1.");

        Logger logger2 = Logger.getInstance();
        logger2.log("Logging from logger2.");

        // Validate that both logger references point to the same object
        System.out.println("\nVerifying Singleton Instance:");
        System.out.println("logger1 hashcode: " + logger1.hashCode());
        System.out.println("logger2 hashcode: " + logger2.hashCode());

        if (logger1 == logger2) {
            System.out.println("✅ Singleton confirmed: Both references are the same instance.");
        } else {
            System.out.println("❌ Singleton failed: Instances are different.");
        }
    }
}
