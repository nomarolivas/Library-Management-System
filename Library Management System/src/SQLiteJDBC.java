import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SQLiteJDBC {
    public static void main(String args[]) {

        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:Library.db");
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "CREATE TABLE CHECKEDBOOK " +
                    "(StudentID   INT        NOT NULL, " +
                    "BookName     TEXT       NOT NULL," +
                    "Author       TEXT       NOT NULL," +
                    "BookID       INT        NOT NULL," +
                    "Price        DOUBLE     NOT NULL," +
                    "Quantity     INT        NOT NULL)";

            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
            System.out.println("Table created successfully");

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }
}
