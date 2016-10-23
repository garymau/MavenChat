package database;

import connection.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Diman on 26.05.2015.
 */
public class ChattersTable {

    public static void insertChatter(String chatterName, String sessionId) throws SQLException {
        Connection connection = ConnectionPool.getConnection();
        int number = -1;
        final String selectCountQuery = "SELECT COUNT(*) AS count FROM chatters WHERE chattername=?";
        final String insertQuery = "INSERT INTO chatters (chattername, session_id) VALUES(?, ?)";
        PreparedStatement stmt = connection.prepareStatement(selectCountQuery);
        stmt.setString(1, chatterName);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            number = rs.getInt("count");
        }
        if (number > 0){
            updateChatter(chatterName, sessionId);
        }
        else {
            stmt = connection.prepareStatement(insertQuery);
            stmt.setString(1, chatterName);
            stmt.setString(2, sessionId);
            stmt.executeUpdate();
        }
        ConnectionPool.closeConnection(connection);
    }

    public static void updateChatter(String chatterName, String sessionId) throws SQLException {
        Connection connection = ConnectionPool.getConnection();
        final String updateQuery = "UPDATE chatters SET Session_Id=? WHERE chattername=?";
        PreparedStatement stmt = connection.prepareStatement(updateQuery);
        stmt.setString(1, sessionId);
        stmt.setString(2, chatterName);
        stmt.executeUpdate();
        ConnectionPool.closeConnection(connection);
    }

    public static void deleteChatter(String chatterName, String sessionId) throws SQLException {
        Connection connection = ConnectionPool.getConnection();
        final String deleteQuery = "DELETE FROM chatters WHERE chattername=? AND session_id=?";
        PreparedStatement stmt = connection.prepareStatement(deleteQuery);
        stmt.setString(1, chatterName);
        stmt.setString(2, sessionId);
        stmt.executeUpdate();
        ConnectionPool.closeConnection(connection);
    }
}
