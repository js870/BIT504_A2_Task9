import java.io.*;                       //Import standard Java libraries
import java.util.*;
import java.util.regex.Pattern;

// Main class for the library management system             **
// * Main class of library management system.
// * Starts the program and initializes the library manager.
// *
public class LibraryManagementSystem1  {
    public static void main(String[] args) {
        System.out.println("Library Management System Starting...");
        LibraryManager libraryManager = LibraryManager.getInstance();
        libraryManager.run();
    }
}

// Singleton class to manage library operations
// Singleton-class (single), which controls the entire logic of the library.
// Contains lists of books and participants, as well as menus and actions.
class LibraryManager {
    private static LibraryManager instance;
     private List<Member> members;
    private List<Book> books;
    private Scanner scanner;

    private LibraryManager() {                          //Private designer - prevents objects from being created from outside
        members = new ArrayList<>();
        books = new ArrayList<>();
        scanner = new Scanner(System.in);
        loadData();
    }

    public static LibraryManager getInstance() {        //Method to obtain an instance of the class (Singleton
        if (instance == null) {
            instance = new LibraryManager();
        }
        return instance;
    }

    // Load data from files
    private void loadData() {
        try {
            loadMembers("members.txt");
            loadBooks("books.txt");
            System.out.println("Data loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    private void loadMembers(String filename) throws IOException {                      //Method to read data about participants from a txt file.
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {   //The loadMembers method reads member (participant) data
                                                                                        // from a text file and adds them to the members list.
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");               //The split(",") method divides a string by commas.
                if (parts.length == 4) {
                    members.add(new Member(parts[0].trim(), parts[1].trim(), parts[2].trim(),
                            Integer.parseInt(parts[3].trim())));
                }   //Checks that the line actually contains 4 elements (to avoid errors if the format is incorrect).
            }
        }
    }

    private void loadBooks(String filename) throws IOException { //Method to read book data from PDF file. String format: id,isbn,title,author,publishDate,genre,ageRating//String format: id,isbn,title,author,publishDate,genre,ageRating
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 7) {
                    books.add(new Book(parts[0].trim(), parts[1].trim(), parts[2].trim(),
                            parts[3].trim(), parts[4].trim(), parts[5].trim(),
                            Integer.parseInt(parts[6].trim())));
                }
            }
        }
    }

    // Main program loop
    public void run() {     //Main cycle of the program - shows main menu and allows the user to select actions.
        while (true) {
            displayMainMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1": bookManagementMenu(); break;
                case "2": memberManagementMenu(); break;
                case "3": loanManagementMenu(); break;
                case "4": searchMenu(); break;
                case "5": System.out.println("Exiting..."); return;
                default: System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Display main menu
    private void displayMainMenu() {
        System.out.println("\n=== Library Management System ===");
        System.out.println("1. Book Management");
        System.out.println("2. Member Management");
        System.out.println("3. Loan Management");
        System.out.println("4. Search");
        System.out.println("5. Exit");
        System.out.print("Select an option: ");
    }

    // Book Management Menu
    private void bookManagementMenu() {
        while (true) {
            System.out.println("\n=== Book Management ===");
            System.out.println("a. Display all books");
            System.out.println("b. Display borrowed books");
            System.out.println("c. Display unborrowed books");
            System.out.println("d. Add a new book");
            System.out.println("e. Return");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine().toLowerCase();

            switch (choice) {
                case "a": displayAllBooks(); break;
                case "b": displayBorrowedBooks(); break;
                case "c": displayUnborrowedBooks(); break;
                case "d": addNewBook(); break;
                case "e": return;
                default: System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Member Management Menu           //keeps information about the member of the library.
    private void memberManagementMenu() {
        while (true) {
            System.out.println("\n=== Member Management ===");
            System.out.println("a. Display all members");
            System.out.println("b. Add a new member");
            System.out.println("c. Return");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine().toLowerCase();

            switch (choice) {
                case "a": displayAllMembers(); break;
                case "b": addNewMember(); break;
                case "c": return;
                default: System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Loan Management Menu
    private void loanManagementMenu() {
        while (true) {
            System.out.println("\n=== Loan Management ===");
            System.out.println("a. Check out a book");
            System.out.println("b. Check in a book");
            System.out.println("c. Return");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine().toLowerCase();

            switch (choice) {
                case "a": checkOutBook(); break;
                case "b": checkInBook(); break;
                case "c": return;
                default: System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Search Menu
    private void searchMenu() {
        while (true) {
            System.out.println("\n=== Search ===");
            System.out.println("a. Find a member");
            System.out.println("b. Find a book");
            System.out.println("c. Return");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine().toLowerCase();

            switch (choice) {
                case "a": findMember(); break;
                case "b": findBook(); break;
                case "c": return;
                default: System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Book Management Methods
    private void displayAllBooks() {
        displayBookTable(books, "All Books");   //Method to display list of all taken (leased) books
    }

    private void displayBorrowedBooks() {
        List<Book> borrowedBooks = new ArrayList<>();
        for (Book book : books) {                          // Review all books in the library
            if (book.isBorrowed()) {
                borrowedBooks.add(book);                   //If the book is in a "taken" state, add it to the list
            }
        }       //Call a common method to display the table of books with title "Borrowed Books
        displayBookTable(borrowedBooks, "Borrowed Books");
    }

    private void displayUnborrowedBooks() {
        List<Book> unborrowedBooks = new ArrayList<>(); //Create a new list to store only free books
        for (Book book : books) {
            if (!book.isBorrowed()) {
                unborrowedBooks.add(book);
            }
        }       //Call a common method to display the table of books with title "Unborrowed Books
        displayBookTable(unborrowedBooks, "Unborrowed Books");
    }

    private void displayBookTable(List<Book> bookList, String header) {     //Show table header with column alignment
        System.out.println("\n=== " + header + " ===");
        System.out.printf("%-5s %-15s %-30s %-20s %-15s %-15s %-10s%n",
                "ID", "ISBN", "Title", "Author", "Publish Date", "Genre", "Age Rating");
        System.out.println("-".repeat(100));                            //Print dividing line
        for (Book book : bookList) {
            System.out.printf("%-5s %-15s %-30s %-20s %-15s %-15s %-10d%n",
                    book.getId(), book.getIsbn(), book.getTitle(), book.getAuthor(),
                    book.getPublishDate(), book.getGenre(), book.getAgeRating());
        }                                           //Browse all books in the transferred list and extract their data
    }   //The System.out.printf() method is used to align columns (so that the table looks neat in the console).

    private void addNewBook() {         //Method for adding a new book to the system
        System.out.println("\n=== Add New Book ===");       //Show section heading
        final String id = "";       //Create variables to store book data
        final String isbn = "";    // values of variables cannot be changed after assignment
        String title, author, publishDate, genre;   //Book ID (usually unique)International
        int ageRating;         //Standard Book Number (ISBBuchname Date of Publication Author Book Genre Age Rating Book

        // Get and validate ID  //Method for adding a new book with checking all fields
        do {
            System.out.print("Enter book ID: "); // Check ID, remove spaces
            String inputId = scanner.nextLine().trim();
            if (inputId.isEmpty()) {
                System.out.println("ID cannot be empty.");
            } else if (books.stream().anyMatch(b -> b.getId().equals(inputId))) {  //Check if there is a book with the same ID in the list (uniqueness
                System.out.println("ID already exists.");
            } else {
                break;
            }
        } while (true);

        // Get and validate ISBN
        do {
            System.out.print("Enter ISBN (10 characters): ");
            String inputIsbn = scanner.nextLine().trim();       //Read ISBN
            if (inputIsbn.isEmpty()) {
                System.out.println("ISBN cannot be empty.");
            } else if (inputIsbn.length() != 10) {              //ISBN lengh
                System.out.println("ISBN must be 10 characters long.");
            } else if (books.stream().anyMatch(b -> b.getIsbn().equals(inputIsbn))) { //Check ISBN uniqueness
                System.out.println("ISBN already exists.");
            } else {
                break;
            }
        } while (true);

        // Get and validate title
        do {
            System.out.print("Enter title: ");
            title = scanner.nextLine().trim();  //Read title
            if (title.isEmpty()) {
                System.out.println("Title cannot be empty.");
            } else {
                break;
            }
        } while (true);

        // Get and validate author
        do {
            System.out.print("Enter author: ");
            author = scanner.nextLine().trim();  //Read author
            if (author.isEmpty()) {
                System.out.println("Author cannot be empty.");
            } else {
                break;
            }
        } while (true);

        // Get and validate publish date
        do {
            System.out.print("Enter publish date (YYYY-MM-DD): ");
            publishDate = scanner.nextLine().trim();                //Read date
            if (publishDate.isEmpty()) {
                System.out.println("Publish date cannot be empty.");
            } else if (!Pattern.matches("\\d{4}-\\d{2}-\\d{2}", publishDate)) { //Check the date format with a regular expression
                System.out.println("Invalid date format. Use YYYY-MM-DD.");
            } else {
                break;
            }
        } while (true);

        // Get and validate genre
        do {
            System.out.print("Enter genre: ");
            genre = scanner.nextLine().trim();      //Read genre
            if (genre.isEmpty()) {
                System.out.println("Genre cannot be empty.");
            } else {
                break;
            }
        } while (true);

        // Get and validate age rating
        do {
            System.out.print("Enter age rating: ");
            try {
                ageRating = Integer.parseInt(scanner.nextLine().trim()); //Convert input to number
                if (ageRating < 0) {
                    System.out.println("Age rating cannot be negative."); //Negative values are not allowed
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Age rating must be a number.");
            }
        } while (true);

        books.add(new Book(id, isbn, title, author, publishDate, genre, ageRating)); //Add and save book in a list
        saveBooksToFile();
        System.out.println("Book added successfully!"); //Confermation
    }

    // Member Management Methods
    private void displayAllMembers() {
        System.out.println("\n=== All Members ===");
        System.out.printf("%-5s %-15s %-15s %-5s%n", "ID", "First Name", "Last Name", "Age");
        System.out.println("-".repeat(50));
        for (Member member : members) { //We review all participants and extract their data
            System.out.printf("%-5s %-15s %-15s %-5d%n",
                    member.getId(), member.getFirstName(), member.getLastName(), member.getAge());
        }
    }

    private void addNewMember() {   //Method to add new participant
        System.out.println("\n=== Add New Member ===");
        final String id = "";                              //id is set after checking
        String  firstName, lastName;
        int age;

        // Get and validate ID
        do {
            System.out.print("Enter member ID: ");
            String inputId = scanner.nextLine().trim();
            if (inputId.isEmpty()) {
                System.out.println("ID cannot be empty.");
            } else if (members.stream().anyMatch(m -> m.getId().equals(inputId))) {
                System.out.println("ID already exists.");     //Uniqueness check
            } else {
                break;
            }
        } while (true);

        // Get and validate first name
        do {
            System.out.print("Enter first name: ");
            firstName = scanner.nextLine().trim();
            if (firstName.isEmpty()) {
                System.out.println("First name cannot be empty.");
            } else {
                break;
            }
        } while (true);

        // Get and validate last name
        do {
            System.out.print("Enter last name: ");
            lastName = scanner.nextLine().trim();
            if (lastName.isEmpty()) {
                System.out.println("Last name cannot be empty.");
            } else {
                break;
            }
        } while (true);

        // Get and validate age
        do {
            System.out.print("Enter age (0-125): ");
            try {
                age = Integer.parseInt(scanner.nextLine().trim());
                if (age < 0 || age > 125) {
                    System.out.println("Age must be between 0 and 125.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Age must be a number.");
            }
        } while (true);

        members.add(new Member(id, firstName, lastName, age));
        saveMembersToFile();
        System.out.println("Member added successfully!");
    }

    // Loan Management Methods
    private void checkOutBook() {
        System.out.println("\n=== Check Out Book ===");
        System.out.print("Enter book ID: ");            //Book ID
        String bookId = scanner.nextLine().trim();
        Book book = books.stream()
                .filter(b -> b.getId().equals(bookId))
                .findFirst()
                .orElse(null);
        //books.stream() converts the books list into a stream (Stream<Book>) so that it can be processed by functional operations (filtering, searching, etc. ).
        //.filter(b -> b.getId().equals(bookId))  //- filter the flow: b -> ... is a lambda expression, where b is one book in the list.
        //. b. getId(). equals(bookId) is a condition that returns true if the book ID matches the bookId.
        //findFirst() - finds the first element that passed the filter. Returns an object of type Optional<Book> (can be empty if nothing is found).
        if (book == null) { //.orElse(null) - if there is a book in Optional - returns it, if not - returns null.
            System.out.println("Book not found.");
            return;
        }

        if (book.isBorrowed()) {        //Checking if the book is available
            System.out.println("Book is already borrowed.");
            return;
        }

        System.out.print("Enter member ID: ");          //Enter Member ID
        String memberId = scanner.nextLine().trim();
        Member member = members.stream()                //Member search
                .filter(m -> m.getId().equals(memberId))
                .findFirst()
                .orElse(null);

        if (member == null) {                           //Participant verification
            System.out.println("Member not found.");
            return;
        }

        if (book.getAgeRating() > member.getAge()) {        //Age restriction check
            System.out.println("Member is too young for this book (age rating: " +
                    book.getAgeRating() + ").");
            return;
        }

        System.out.println("Book: " + book.getTitle());  //Checkout and confermation
        System.out.print("Confirm checkout (y/n)? ");
        if (scanner.nextLine().trim().toLowerCase().startsWith("y")) {
            book.setBorrowedBy(member.getId());
            saveBooksToFile();
            System.out.println("Book checked out successfully!");
        }
    }

    private void checkInBook() {                        //Method for returning the book
        System.out.println("\n=== Check In Book ===");
        System.out.print("Enter book ID: ");
        String bookId = scanner.nextLine().trim();
        Book book = books.stream()              // search for a book
                .filter(b -> b.getId().equals(bookId))
                .findFirst()
                .orElse(null);

        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        if (!book.isBorrowed()) {                   //Check if the book has been taken
            System.out.println("Book is not currently borrowed.");
            return;
        }

        book.setBorrowedBy(null);                   //Drop status "taken
        saveBooksToFile();
        System.out.println("Book checked in successfully!");
    }

    // Search Methods                           //Search for participant by surname (partial match
    private void findMember() {
        System.out.println("\n=== Find Member ===");
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine().trim().toLowerCase();
        List<Member> foundMembers = new ArrayList<>();

        for (Member member : members) {             //Add all matching participants
            if (member.getLastName().toLowerCase().contains(lastName)) {
                foundMembers.add(member);
            }
        }

        if (foundMembers.isEmpty()) {
            System.out.println("No members found with last name containing: " + lastName);
            return;
        }

        for (Member member : foundMembers) {        //For each participant found we show the books that he/she took
            System.out.println("\nMember: " + member.getFirstName() + " " +
                    member.getLastName() + " (ID: " + member.getId() + ")");
            System.out.println("Age: " + member.getAge());
            System.out.println("Borrowed books:");
            List<Book> borrowedBooks = books.stream()       //List of books taken by the participant
                    .filter(b -> member.getId().equals(b.getBorrowedBy()))
                    .toList();
            if (borrowedBooks.isEmpty()) {
                System.out.println("No books borrowed.");
            } else {
                for (Book book : borrowedBooks) {
                    System.out.println("- " + book.getTitle() + " (ID: " + book.getId() + ")");
                }
            }
        }
    }

    private void findBook() {  //Book search by title (partial match
        System.out.println("\n=== Find Book ===");
        System.out.print("Enter book title (partial match): ");
        String title = scanner.nextLine().trim().toLowerCase();
        List<Book> foundBooks = new ArrayList<>();

        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(title)) {
                foundBooks.add(book);
            }
        }

        if (foundBooks.isEmpty()) {
            System.out.println("No books found with title containing: " + title);
            return;
        }

        for (Book book : foundBooks) {          //Retrieving information from found books
            System.out.println("\nID: " + book.getId());
            System.out.println("ISBN: " + book.getIsbn());
            System.out.println("Title: " + book.getTitle());
            System.out.println("Author: " + book.getAuthor());
            System.out.println("Publish date: " + book.getPublishDate());
            System.out.println("Genre: " + book.getGenre());
            System.out.println("Age rating: " + book.getAgeRating());
            if (book.isBorrowed()) {                                    //If the book is taken, find out who
                Member borrower = members.stream()
                        .filter(m -> m.getId().equals(book.getBorrowedBy()))
                        .findFirst()
                        .orElse(null);
                if (borrower != null) {
                    System.out.println("Borrowed by: " + borrower.getFirstName() + " " +
                            borrower.getLastName());
                }
            } else {
                System.out.println("Status: Available");
            }
        }
    }

    // Save data to files
    private void saveBooksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("books.txt"))) {
            for (Book book : books) {
                writer.write(String.format("%s,%s,%s,%s,%s,%s,%d%n",    //Each line is a separate book, the values are separated by commas:
                        book.getId(), book.getIsbn(), book.getTitle(), book.getAuthor(),
                        book.getPublishDate(), book.getGenre(), book.getAgeRating()));
            }
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    private void saveMembersToFile() {          //Save member list to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("members.txt"))) {
            for (Member member : members) {
                writer.write(String.format("%s,%s,%s,%d%n",
                        member.getId(), member.getFirstName(), member.getLastName(),
                        member.getAge()));
            }
        } catch (IOException e) {
            System.out.println("Error saving members: " + e.getMessage());
        }
    }
}

// Member class to store member information
class Member {
    private String id;
    private String firstName;
    private String lastName;
    private int age;

    public Member(String id, String firstName, String lastName, int age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    // Getters
    public String getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public int getAge() { return age; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setAge(int age) { this.age = age; }
}

// Book class to store book information
class Book {
    private String id;
    private String isbn;
    private String title;
    private String author;
    private String publishDate;
    private String genre;
    private int ageRating;
    private String borrowedBy;

    public Book(String id, String isbn, String title, String author,
                String publishDate, String genre, int ageRating) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publishDate = publishDate;
        this.genre = genre;
        this.ageRating = ageRating;
        this.borrowedBy = null;
    }

    // Getters
    public String getId() { return id; }
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getPublishDate() { return publishDate; }
    public String getGenre() { return genre; }
    public int getAgeRating() { return ageRating; }
    public String getBorrowedBy() { return borrowedBy; }
    public boolean isBorrowed() { return borrowedBy != null; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setPublishDate(String publishDate) { this.publishDate = publishDate; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setAgeRating(int ageRating) { this.ageRating = ageRating; }
    public void setBorrowedBy(String memberId) { this.borrowedBy = memberId; }
}

/*
Brief summary:

Methods are divided by purpose: members, books, search, storage.

All input correctness checks protect against user errors.

Using stream() makes searching and checking concise.

The data is saved in . txt files so that the program can recover it later.
 */