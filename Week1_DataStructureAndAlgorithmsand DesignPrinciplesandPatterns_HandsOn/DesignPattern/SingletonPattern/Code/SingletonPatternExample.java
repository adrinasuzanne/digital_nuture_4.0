
public class SingletonPatternExample {


    static class Logger {

        private static volatile Logger instance;


        private Logger() {
            System.out.println("Logger initialized.");
        }


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


        public void log(String message) {
            System.out.println("[LOG] " + message);
        }
    }


    public static void main(String[] args) {
        System.out.println("Testing Singleton Logger...");

        Logger logger1 = Logger.getInstance();
        logger1.log("Logging from logger1.");

        Logger logger2 = Logger.getInstance();
        logger2.log("Logging from logger2.");


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
