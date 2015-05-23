package connection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;


public class DatabaseConnection {

    private static final Logger logger = Logger.getLogger(DatabaseConnection.class.getName());

    public static Connection setupDBConnection() throws IOException {
        LogManager.getLogManager().readConfiguration(
                DatabaseConnection.class.getResourceAsStream("/logging.properties")
        );
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.log(Level.INFO,"Where is your MySQL JDBC Driver? Include in your library path!\n"+e.getStackTrace()+"\n");
            return null;
        }
        logger.log(Level.INFO, Calendar.getInstance().toString()+"MySQL JDBC Driver Registered!");
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mydb", "root", "root");
        } catch (SQLException e) {
            logger.log(Level.INFO,"Connection Failed! Check output console \n"+e.getStackTrace()+"\n");
            e.printStackTrace();
            return null;
        }
        if (connection != null) {
            logger.log(Level.INFO, "You made it, take control your database now!");
        } else {
            logger.log(Level.INFO,"Failed to make connection!");
        }
        return connection;
    }
}
