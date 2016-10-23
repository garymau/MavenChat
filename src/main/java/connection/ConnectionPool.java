package connection;

/**
 * Created by Diman on 09.10.2016.
 */
import connection.DatabaseConnection;
import org.apache.commons.dbcp.cpdsadapter.DriverAdapterCPDS;
import org.apache.commons.dbcp.datasources.SharedPoolDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public final class ConnectionPool {

    private static final Logger logger = LogManager.getLogger(DatabaseConnection.class.getName());
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:ORADB";
    private static final String DRIVER = "oracle.jdbc.OracleDriver";
    private static final String USERNAME = "c##mydb";
    private static final String PASSWORD = "1234";
    private static final int MAX_TOTAL = 10;
    private static DataSource dataSource;

    static {
        DriverAdapterCPDS cpds = new DriverAdapterCPDS();
        try {
            cpds.setDriver(DRIVER);
            cpds.setUrl(URL);
            cpds.setUser(USERNAME);
            cpds.setPassword(PASSWORD);

            SharedPoolDataSource pool = new SharedPoolDataSource();
            pool.setConnectionPoolDataSource(cpds);
            pool.setMaxActive(MAX_TOTAL);
            dataSource = pool;
            logger.info("connection pool acquired");
        } catch (ClassNotFoundException e) {
            logger.info("CONNECTION POOL WAS NOT ACQUIRED");
            e.printStackTrace();
        }
    }

    public static Connection getConnection(){
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            logger.info("***ERROR*** UNABLE TO GET A CONNECTION");
            e.printStackTrace();
        }
        return null;
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            logger.info("***ERROR*** UNABLE TO CLOSE THE CONNECTION");
            e.printStackTrace();
        }
    }
}