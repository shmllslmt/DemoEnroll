import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseConnection {
    private Connection connection;
    public DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo", "root", "r00tpa55word");

            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS students (" +
                    "ssn VARCHAR(10) PRIMARY KEY, " +
                    "firstname VARCHAR(15), " +
                    "lastname VARCHAR(15), " +
                    "phone VARCHAR(15), " +
                    "birthdate DATE, " +
                    "street VARCHAR(250), " +
                    "zipcode VARCHAR(5), " +
                    "deptID VARCHAR(3), " +
                    "CONSTRAINT fk_departments FOREIGN KEY (deptID) REFERENCES departments (deptID)"+
                    ");");
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
    public Connection getConnection() {
        return connection;
    }
}
