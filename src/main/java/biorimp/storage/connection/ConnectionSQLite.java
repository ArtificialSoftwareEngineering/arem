package biorimp.storage.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by David on 17/04/2017.
 */
public class ConnectionSQLite {

    private static ConnectionSQLite instance;
    private static Connection conn;

    private ConnectionSQLite(){

    }
    /**
     * Connect to a sample database
     */
    public Connection connect() {
        if (conn != null)
            return conn;

        try {
            // db parameters
            String url = "jdbc:sqlite:refactoring.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            return conn;

        }
    }

    public static ConnectionSQLite getInstance() throws Exception {
        if (instance == null) {
            instance = new ConnectionSQLite();
        }
        return instance;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        getInstance();
    }
}
