public class IssueBook {
    private int bookId;
    private int userId;


    public IssueBook(int bookId, int userId) {
        this.bookId = bookId;
        this.userId = userId;

    }


    public int getBookId() {
        return bookId;
    }

    public int getUserId() {
        return userId;
    }

    public String toString() {
        return "Book ID: " + bookId + ", userID: " + userId;
    }


}
