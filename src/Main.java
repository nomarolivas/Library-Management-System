import java.sql.*;
import java.util.Scanner;

public class Main {
    public static String bookName;
    public static int bookId;
    public static String authorName;
    public static double price;
    public static boolean runProgram = true;
    public static boolean checkIfNumber = true;
    public static boolean selectOption = true;
    public static boolean libraryIsEmpty = true;
    public static boolean validAdminLogIn = true;
    public static int userSelection;
    public static int enteredUserId;
    public static String enteredPassword;
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
                    break;
                } else {
                    System.out.println("Error reading user selection: Please try again \n");
                    userInput.nextLine();
                }
            }

            switch (userSelection) {

                case 1: //Adding new book to library.
                    System.out.println("Enter the book's name:");
                    bookName = userInput.nextLine().toLowerCase();

                    System.out.println("Enter the book's author:");
                    authorName = userInput.nextLine().toLowerCase();

                    //Checking if price is a string
                    while (checkIfNumber) {
                        System.out.println("Enter the book's price:");

                        if (userInput.hasNextDouble()) {
                            price = userInput.nextDouble();
                            break;
                        } else {
                            System.out.println("Error reading price: Please try again.\n");
                            userInput.nextLine();
                        }
                    }

                    Book newBook = new Book(bookName, authorName, bookId, price);
                    newBook.setBookId();
                    insertItemDB(newBook.getBookName(), newBook.getAuthor(), newBook.getBookId(), newBook.getPrice());
                    break;

                case 2: //Deleting a book from library.
                    checkLibrarySize();
                    while (libraryIsEmpty) {
                        checkLibrarySize();
                        System.out.println("Books that are available in library:");
                        printAllBooks();

                        System.out.println("Enter the book's name that you wish to delete:");
                        bookName = userInput.nextLine();

                        System.out.println("Enter the book's author that you wish to delete:");
                        authorName = userInput.nextLine();

                        //Checks if the price is a string.
                        while (checkIfNumber) {
                            System.out.println("Enter the book's ID that you wish to delete:");
                            if (userInput.hasNextInt()) {
                                bookId = userInput.nextInt();
                                userInput.nextLine();
                                break;
                            } else {
                                System.out.println("Error reading book ID: Please try again\n");
                                userInput.nextLine();
                            }
                        }
                        deleteItemDB(bookName, authorName, bookId);
                    }

                    libraryIsEmpty = true;
                    break;

                case 3: //Searching for book in library.
                    checkLibrarySize();
                    while (libraryIsEmpty) {
                        System.out.println("Enter the book's name or the Author of the book:");
                        bookName = userInput.nextLine().toLowerCase();
                        searchForBook(bookName);
                    }
                    libraryIsEmpty = true;
                    break;


                case 4://Issuing book in library.
                    checkLibrarySize();
                    while (libraryIsEmpty) {
                        while (checkIfNumber) {
                            System.out.println("Please enter your user ID number");
                            //Create your own student ID!
                            if (userInput.hasNextInt()) {
                                enteredUserId = userInput.nextInt();
                                break;

                            } else {
                                System.out.println("Error reading student ID: Please try again\n");
                                userInput.nextLine();
                            }
                        }

                        userInput.nextLine();
                        System.out.println("Enter Password:");
                        //Create your own student password!
                        enteredPassword = userInput.nextLine();
                        System.out.println();
                        System.out.println("Admin Log In:");

                        adminLogIn(userInput);
                        System.out.println("All books in Library:");
                        printAllBooks();

                        System.out.println("Enter the name of the book you would like to checkout:");
                        bookName = userInput.nextLine().toLowerCase();
                        System.out.println("Enter the Author's name of the book you would like to checkout:");
                        authorName = userInput.nextLine().toLowerCase();

                        while (checkIfNumber) {
                            System.out.println("Admin, please enter the book's ID:");

                            if (userInput.hasNextInt()) {
                                bookId = userInput.nextInt();
                                break;
                            } else {
                                System.out.println("Error reading bookId: Please try again.\n");
                                userInput.nextLine();
                            }
                        }

                        while (checkIfNumber) {
                            System.out.println("Admin, please enter the book's price:");

                            if (userInput.hasNextDouble()) {
                                price = userInput.nextDouble();
                                break;
                            } else {
                                System.out.println("Error reading price: Please try again.\n");
                                userInput.nextLine();
                            }
                        }
                        checkOutBook(enteredUserId, bookName, authorName, bookId, price);
                    }

                    libraryIsEmpty = true;
                    break;


                case 5://Exits entire program.
                    System.out.println("Your session has ended. Thank you");
                    runProgram = false;
                    break;

                default:
                    break;
            }
        }
    }


    protected static void insertItemDB(String bookName, String author, int bookId, double price) {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:Library.db");
            conn.setAutoCommit(false);
//            System.out.println("Opened database successfully");

            String sql = "INSERT INTO BOOK (BookName, Author, BookID, Price) " +
                    "VALUES (?,?,?,?);";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookName);
            pstmt.setString(2, author);
            pstmt.setInt(3, bookId);
            pstmt.setDouble(4, price);

            pstmt.executeUpdate();
            conn.commit();
            conn.close();
            System.out.println("Book has been added to the Library.\n");

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }


    protected static void deleteItemDB(String bookName, String author, int bookId) {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:Library.db");
            conn.setAutoCommit(false);

            String sql = "DELETE FROM BOOK WHERE BookName ='" + bookName + "' AND Author='" + author + "' AND BookID='" + bookId + "'";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            int deleted = pstmt.executeUpdate();
            if (deleted == 0) {
                System.out.println("Book was not found. \n");
                libraryIsEmpty = false;

            } else {
                System.out.println("Book has been deleted from the Library.\n");
                libraryIsEmpty = false;
            }

            conn.commit();
            conn.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }


    protected static void checkLibrarySize() {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:Library.db");
            conn.setAutoCommit(false);

            String sql = "SELECT count(*) FROM BOOK ";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            try (ResultSet rs = pstmt.executeQuery()) {
                rs.next();
                int count = rs.getInt(1);
                if (count == 0) {
                    System.out.println("ERROR: Library contains no books. \n");
                    libraryIsEmpty = false;

                }
            }
            conn.commit();
            conn.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }


    protected static void printAllBooks() {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:Library.db");
            conn.setAutoCommit(false);

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM BOOK");
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i == 3) {
                        System.out.print("ID:" + rs.getString(i) + " ");

                    } else if (i == 4) {
                        System.out.print("$" + rs.getString(i) + " ");

                    } else {
                        System.out.print(rs.getString(i) + " ");
                    }
                }
                System.out.println();
            }
            System.out.println();

            conn.commit();
            conn.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }


    protected static void searchForBook(String bookName) {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:Library.db");
            conn.setAutoCommit(false);

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM BOOK WHERE BookName='" + bookName + "' OR Author='" + bookName + "'");
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            System.out.println("All books that matched:");
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i == 3) {
                        System.out.print("ID:" + rs.getString(i) + " ");

                    } else if (i == 4) {
                        System.out.print("$" + rs.getString(i) + " ");

                    } else {
                        System.out.print(rs.getString(i) + " ");
                    }
                }
                System.out.println();
            }
            System.out.println();


            libraryIsEmpty = false;
            conn.commit();
            conn.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }


    protected static void checkOutBook(int enteredUserId, String bookName, String author, int bookId, double price) {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:Library.db");
            conn.setAutoCommit(false);

            String sql = "DELETE FROM BOOK WHERE BookName ='" + bookName + "' AND Author='" + author + "' AND BookID='" + bookId + "'";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            int deleted = pstmt.executeUpdate();
            if (deleted == 0) {
                System.out.println("Book was not found. \n");
                libraryIsEmpty = false;

            } else {
//                System.out.println("Book has been deleted from BOOK db.\n");
                libraryIsEmpty = false;
                pstmt.executeUpdate();
                conn.commit();

                String sql2 = "INSERT INTO CHECKEDBOOK (StudentID, BookName, Author, BookID, Price) " +
                        "VALUES (?,?,?,?,?);";

                PreparedStatement pstmt2 = conn.prepareStatement(sql2);

                pstmt2.setInt(1, enteredUserId);
                pstmt2.setString(2, bookName);
                pstmt2.setString(3, author);
                pstmt2.setInt(4, bookId);
                pstmt2.setDouble(5, price);

                pstmt2.executeUpdate();
                System.out.println("Book has been successfully checked out.\n");
            }
            conn.commit();
            conn.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    // Admin can log in to issue a student a book.
    public static void adminLogIn(Scanner userInput) {

        while (validAdminLogIn) {
            System.out.println("Please enter admin username:");
            if (userInput.nextLine().equals(adminUsername)) {
                System.out.println("Please enter admin password:");
                if (userInput.nextLine().equals(adminPassword)) {
                    System.out.println("Successfully logged in. \n");
                    break;
                } else {
                    System.out.println("Incorrect password: Please try again");
                }

            } else {
                System.out.println("Incorrect username: Please try again");
            }
        }

    }
}
