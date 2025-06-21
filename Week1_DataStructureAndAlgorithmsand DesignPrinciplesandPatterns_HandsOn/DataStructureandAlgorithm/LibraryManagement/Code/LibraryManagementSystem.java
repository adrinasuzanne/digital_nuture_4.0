import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

class Book {
    int bookId;
    String title;
    String author;

    public Book(int bookId, String title, String author) {
        this.bookId = bookId;
        this.title = title.toLowerCase();
        this.author = author;
    }

    @Override
    public String toString() {
        return "Book ID: " + bookId + ", Title: " + title + ", Author: " + author;
    }
}

public class LibraryManagementSystem {

    public static Book linearSearch(Book[] books, String targetTitle) {
        targetTitle = targetTitle.toLowerCase();
        for (Book book : books) {
            if (book.title.equals(targetTitle)) {
                return book;
            }
        }
        return null;
    }

    public static Book binarySearch(Book[] books, String targetTitle) {
        targetTitle = targetTitle.toLowerCase();
        int low = 0, high = books.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int cmp = books[mid].title.compareTo(targetTitle);
            if (cmp == 0) return books[mid];
            else if (cmp < 0) low = mid + 1;
            else high = mid - 1;
        }

        return null;
    }

    public static void viewAllBooks(Book[] books) {
        System.out.println("📚 List of All Books:");
        for (Book book : books) {
            System.out.println(book);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Book[] books = {
                new Book(101, "Data Structures", "Mark Allen"),
                new Book(102, "Java Programming", "James Gosling"),
                new Book(103, "Clean Code", "Robert C. Martin"),
                new Book(104, "Algorithms", "Thomas Cormen"),
                new Book(105, "Ends With Us", "Colleen Hoover")
        };

        System.out.println("===== Library Management System =====");
        System.out.println("1. Search Book by Title (Linear Search)");
        System.out.println("2. Search Book by Title (Binary Search)");
        System.out.println("3. View All Books");
        System.out.print("Enter your choice (1-3): ");
        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1:
                System.out.print("Enter the book title to search: ");
                String title1 = sc.nextLine();
                Book result1 = linearSearch(books, title1);
                if (result1 != null) {
                    System.out.println("✅ Book Found:\n" + result1);
                } else {
                    System.out.println("❌ Book not found with title: " + title1);
                }
                break;

            case 2:
                Arrays.sort(books, Comparator.comparing(b -> b.title));
                System.out.print("Enter the book title to search: ");
                String title2 = sc.nextLine();
                Book result2 = binarySearch(books, title2);
                if (result2 != null) {
                    System.out.println("✅ Book Found:\n" + result2);
                } else {
                    System.out.println("❌ Book not found with title: " + title2);
                }
                break;

            case 3:
                viewAllBooks(books);
                break;

            default:
                System.out.println("⚠️ Invalid choice. Please enter 1-3.");
        }

        sc.close();
    }
}
