import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static ArrayList<Book> library = new ArrayList<>();
    public static ArrayList<IssueBook> booksIssued = new ArrayList<>();
    public static String bookName;
    public static int bookId;
    public static String writerName;
    public static double price;
    public static int quantity;
    public static boolean runProgram = true;
    public static boolean priceIsString = true;
    public static boolean selectOption = true;
    public static boolean deleteBook = true;
    public static boolean runIssueBook = true;
    public static boolean addBookToLibrary = true;
    public static boolean deleteBookNotFound = true;
    public static boolean searchBookNotFound = true;
    public static boolean issueBookNotFound = true;
    public static boolean studentIdNotString = true;
    public static boolean validAdminLogIn = true;
    public static int userSelection;
    public static String bookNameToDelete;
    public static String bookAuthorToDelete;
    public static double bookIdToDelete;
    public static String searchBook;
    public static int enteredUserId;
    public static String enteredPassword;
    public static String bookNameToIssue;
    public static String bookAuthorToIssue;
    private static String adminUsername = "admin101";
    private static String adminPassword = "admin101";


    public static void main(String[] args) {

        while (runProgram) {
            //Start Menu.
            Scanner userInput = new Scanner(System.in);
            while (selectOption) {
                System.out.println("Welcome to the San Francisco State Library");
                System.out.println("Please select an option: ");
                System.out.println("1. Add a book ");
                System.out.println("2. Delete a Book");
                System.out.println("3. Search for Book");
                System.out.println("4. Issue a Book");
                System.out.println("5. Exit");

                //Checking user selection is an integer.
                if (userInput.hasNextInt()) {
                    userSelection = Integer.parseInt((userInput.nextLine()));
                    selectOption = false;
                } else {
                    System.out.println("Error reading user selection: Please try again \n");
                    userInput.nextLine();
                }

            }
            selectOption = true;

            switch (userSelection) {
                case 1: //Adding new book to library.
                    System.out.println("Enter the book's name:");
                    bookName = userInput.nextLine();

                    System.out.println("Enter the book's author:");
                    writerName = userInput.nextLine();

                    //Checking if price is a string
                    while (priceIsString) {
                        System.out.println("Enter the book's price:");

                        if (userInput.hasNextDouble()) {
                            price = userInput.nextDouble();
                            priceIsString = false;
                        } else {
                            System.out.println("Error reading price: Please try again\n");
                            userInput.nextLine();
                        }
                    }
                    priceIsString = true;

                    Book newBook = new Book(bookName, writerName, bookId, price, quantity);

                    newBook.increaseQuantity();
                    checkBookDuplicate(newBook);

                    while (addBookToLibrary) {
                        newBook.setBookId();
                        library.add(newBook);
                        //Serialization of new Book.
                        try {
                            FileOutputStream fileOut = new FileOutputStream("bookDetails.txt");
                            ObjectOutputStream out = new ObjectOutputStream(fileOut);
                            out.writeObject(newBook);
                            out.close();
                            fileOut.close();
                            System.out.println("Book's serialized data is saved");

                        } catch (IOException i) {
                            i.printStackTrace();
                        }
                        newBook = null;
                        try {
                            FileInputStream fileIn = new FileInputStream("bookDetails.txt");
                            ObjectInputStream in = new ObjectInputStream((fileIn));
                            newBook = (Book) in.readObject();
                            in.close();
                            fileIn.close();
//                            System.out.println("Book has been deserialized");
                        } catch (IOException j) {
                            j.printStackTrace();

                        } catch (ClassNotFoundException c) {
                            System.out.println("Book class not found");
                            c.printStackTrace();
                            return;
                        }
                        System.out.println("New book has been added");
                        addBookToLibrary = false;
                    }

                    addBookToLibrary = true;
                    break;

                case 2: //Deleting a book from library.

                    if (library.isEmpty()) {
                        System.out.println("Unable to delete books.");
                        System.out.println("Library contains no books\n");
                        break;
                    }

                    while (deleteBook) {
                        System.out.println("Books that are available in library:");
                        System.out.println(library + "\n");

                        System.out.println("Enter the book's name that you wish to delete:");
                        bookNameToDelete = userInput.nextLine();

                        System.out.println("Enter the book's author that you wish to delete:");
                        bookAuthorToDelete = userInput.nextLine();

                        //Checks if the price is a string.
                        while (priceIsString) {
                            System.out.println("Enter the book's ID that you wish to delete:");
                            if (userInput.hasNextInt()) {
                                bookIdToDelete = userInput.nextDouble();
                                userInput.nextLine();
                                priceIsString = false;
                            } else {
                                System.out.println("Error reading book ID: Please try again\n");
                                userInput.nextLine();
                            }
                        }
                        for (int i = 0; i < library.size(); i++) {

                            if (library.get(i).getBookName().equals(bookNameToDelete) &&
                                    library.get(i).getAuthor().equals(bookAuthorToDelete) &&
                                    library.get(i).getBookId() == bookIdToDelete && library.get(i).getQuantity() > 1) {

                                library.get(i).decreaseQuantity();
                                System.out.println("Book has been deleted and quantity decreased");
                                deleteBookNotFound = false;


                            } else if (library.get(i).getBookName().equals(bookNameToDelete) &&
                                    library.get(i).getAuthor().equals(bookAuthorToDelete) &&
                                    library.get(i).getBookId() == bookIdToDelete) {

                                library.remove(i);
                                System.out.println("Book has been deleted");
                                deleteBookNotFound = false;

                            }
                            deleteBook = false;
                        }

                        while (deleteBookNotFound) {
                            System.out.println("Unfortunately the book was not found. Exiting to home screen...\n");
                            deleteBookNotFound = false;
                        }
                    }
                    deleteBookNotFound = true;
                    deleteBook = true;
                    priceIsString = true;
                    break;

                case 3: //Searching for book in library.
                    if (library.isEmpty()) {
                        System.out.println("Unable to search for books. There are no books in the library\n");
                        break;
                    }

                    System.out.println("Enter the book's name or the Author of the book:");
                    searchBook = userInput.nextLine();

                    for (int i = 0; i < library.size(); i++) {
                        if (library.get(i).getBookName().equals(searchBook) ||
                                library.get(i).getAuthor().equals(searchBook)) {

                            System.out.println("These are the books that were found:");
                            System.out.println(library.get(i));
                            searchBookNotFound = false;
                        }
                    }
                    while (searchBookNotFound) {
                        System.out.println("Unfortunately the book was not found. Exiting to home screen...\n");
                        searchBookNotFound = false;
                    }
                    searchBookNotFound = true;
                    break;

                case 4://Issuing book in library.

                    if (library.isEmpty()) {
                        System.out.println("Sorry, we unfortunately can't issue any books right now. Library is empty.\n");
                        break;
                    }

                    while (runIssueBook) {
                        //Checks if student ID is an integer.
                        while (studentIdNotString) {
                            System.out.println("Please enter your user ID number");
                            //Create your own student ID!
                            if (userInput.hasNextInt()) {
                                enteredUserId = userInput.nextInt();
                                studentIdNotString = false;

                            } else {
                                System.out.println("Error reading student ID: Please try again\n");
                                userInput.nextLine();
                            }
                        }

                        userInput.nextLine();
                        System.out.println("Enter Password:");
                        //Create your own student password!
                        enteredPassword = userInput.nextLine();

                        System.out.println(library + "\n");
                        System.out.println("Enter the name of the book would you like to checkout:");
                        bookNameToIssue = userInput.nextLine();
                        System.out.println("Enter the Author's name of the book you want to checkout:");
                        bookAuthorToIssue = userInput.nextLine();


                        for (int i = 0; i < library.size(); i++) {
                            if (library.get(i).getBookName().equals(bookNameToIssue) &&
                                    library.get(i).getAuthor().equals(bookAuthorToIssue)) {

                                IssueBook bookIssued = new IssueBook(library.get(i).getBookId(), enteredUserId);


                                if (booksIssued.contains(bookIssued) && library.get(i).getQuantity() < 1) {

                                    System.out.println("Sorry, unfortunately this book has been already checked out\n");

                                } else if (library.get(i).getQuantity() > 1) {
                                    adminLogIn(userInput);
                                    booksIssued.add(bookIssued);
                                    library.get(i).decreaseQuantity();
                                    runIssueBook = false;
                                    System.out.println("Book has been issued and quantity decreased.");
                                    System.out.println(booksIssued);
                                    issueBookNotFound = false;


                                } else {
                                    adminLogIn(userInput);
                                    booksIssued.add(bookIssued);
                                    library.remove(i);
                                    runIssueBook = false;
                                    System.out.println("Book has been issued.");
                                    System.out.println(booksIssued);
                                    //Serialization of Issued Books.
                                    try {
                                        FileOutputStream fileOut = new FileOutputStream("issueBook.txt");
                                        ObjectOutputStream out = new ObjectOutputStream((fileOut));
                                        out.writeObject(bookIssued);
                                        out.close();
                                        fileOut.close();
                                        System.out.println("Issued book's serialized data is saved");
                                    } catch (IOException j) {
                                        j.printStackTrace();
                                    }
                                    bookIssued = null;
                                    try {
                                        FileInputStream fileIn = new FileInputStream("issueBook.txt");
                                        ObjectInputStream in = new ObjectInputStream((fileIn));
                                        bookIssued = (IssueBook) in.readObject();
                                        in.close();
                                        fileIn.close();
                                        System.out.println("Issued book data has been deserialized");
                                    } catch (IOException j) {
                                        j.printStackTrace();

                                    } catch (ClassNotFoundException c) {
                                        System.out.println("Book class not found");
                                        c.printStackTrace();
                                        return;
                                    }
                                    issueBookNotFound = false;

                                }

                            }

                        }
                        while (issueBookNotFound) {
                            System.out.println("Sorry we not able to checkout this book. This book does not appear to be in our library.");
                            System.out.println("Exiting to Home Screen...\n");
                            issueBookNotFound = false;
                            runIssueBook = false;
                        }
                    }
                    studentIdNotString = true;
                    issueBookNotFound = true;
                    runIssueBook = true;
                    break;


                case 5://Exits entire program.
                    System.out.println("Your session has ended. Thank you");
                    runProgram = false;
                    break;

            }

        }


    }

    //Checks if book that is added is already stored in the library.
    public static void checkBookDuplicate(Book addBook) {

        for (int i = 0; i < library.size(); i++) {
            if (library.get(i).getBookName().equals(addBook.getBookName()) &&
                    library.get(i).getAuthor().equals(addBook.getAuthor()) &&
                    library.get(i).getPrice() == addBook.getPrice()) {

                library.get(i).increaseQuantity();
                addBookToLibrary = false;
                System.out.println("Book was duplicated");
            }
        }

    }

    // Admin can log in to issue a student a book.
    public static void adminLogIn(Scanner userInput) {

        while (validAdminLogIn) {
            System.out.println("Please enter admin username:");
            if (userInput.nextLine().equals(adminUsername)) {
                System.out.println("Please enter admin password:");
                if (userInput.nextLine().equals(adminPassword)) {
                    System.out.println("Successfully logged in");
                    validAdminLogIn = false;
                } else {
                    System.out.println("Incorrect password: Please try again");
                }

            } else {
                System.out.println("Incorrect username: Please try again");
            }
        }

        validAdminLogIn = true;


    }
}
