import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseUtil {

    private static final String URL = "jdbc:mysql://localhost:33061/mysql";
    private static final String USER = "root";
    private static final String PASSWORD = "Akee1209$";

    public static Connection getConnection() throws  SQLException{
        return DriverManager.getConnection(URL,USER,PASSWORD);
    }
}
