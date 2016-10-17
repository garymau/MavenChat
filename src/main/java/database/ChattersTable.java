package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Diman on 26.05.2015.
 */
public class ChattersTable {

    public static void insertChatter(Connection connection, String chatterName, String sessionId) throws SQLException {

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
            updateChatter(connection, chatterName, sessionId);
        }
        else {
            stmt = connection.prepareStatement(insertQuery);
            stmt.setString(1, chatterName);
            stmt.setString(2, sessionId);
            stmt.executeUpdate();
        }
    }

    public static void updateChatter(Connection connection, String chatterName, String sessionId) throws SQLException {
        final String updateQuery = "UPDATE chatters SET Session_Id=? WHERE chattername=?";
        PreparedStatement stmt = connection.prepareStatement(updateQuery);
        stmt.setString(1, sessionId);
        stmt.setString(2, chatterName);
        stmt.executeUpdate();
    }

    public static void deleteChatter(Connection connection, String chatterName, String sessionId) throws SQLException {
        final String deleteQuery = "DELETE FROM chatters WHERE chattername=? AND session_id=?";
        PreparedStatement stmt = connection.prepareStatement(deleteQuery);
        stmt.setString(1, chatterName);
        stmt.setString(2, sessionId);
        stmt.executeUpdate();
    }
}
