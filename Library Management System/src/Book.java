import java.util.Random;

public class Book implements java.io.Serializable {
    private String bookName;
    private int bookId;
    private String author;
    private double price;

    public Book(String bookName, String author, int bookId, double price) {
        this.bookName = bookName;
        this.author = author;
        this.bookId = bookId;
        this.price = price;
    }

    public String getBookName() {
        return bookName;
    }

    public String getAuthor() {
        return author;
    }

    public int getBookId() {
        return bookId;
    }

    public int setBookId() {
        Random r = new Random();
        bookId = r.nextInt(1000);
        return bookId;
    }

    public double getPrice() {
        return price;
    }


    public String toString() {
        return "Name of Book:" + bookName + ", Author:" + author + ", Price:$" + price + ", Book ID:" + bookId+ "\n";
    }


}
