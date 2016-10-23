package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class DatabaseConnection {

    private static final Logger logger = LogManager.getLogger(DatabaseConnection.class.getName());
    private static final String url = "jdbc:oracle:thin:@localhost:1521:ORADB";
    private static final String username = "c##mydb";
    private static final String password = "1234";

    public static Connection setupDBConnection(){
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            logger.info("Where is your ORACLE JDBC Driver? Include in your library path!\n"+e.getStackTrace()+"\n");
            return null;
        }
        logger.info("Oracle JDBC Driver Registered!");
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            logger.info("Connection Failed! Check output console \n"+e.getStackTrace()+"\n");
            e.printStackTrace();
            return null;
        }
        if (connection != null) {
            logger.info("You made it, take control your database now!");
        } else {
            logger.info("Failed to make connection!");
        }
        return connection;
    }
}
