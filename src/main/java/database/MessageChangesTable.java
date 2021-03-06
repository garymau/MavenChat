package database;

import connection.ConnectionPool;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.sql.*;


public class MessageChangesTable {
    public static String getEditedMessages(int messageEditId) {
        final String sql = "SELECT * from message_changes WHERE id > " + messageEditId;
        Connection connection = null;
        try {
            connection = ConnectionPool.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            JSONArray jsonArray = new JSONArray();
            while(resultSet.next()) {
                JSONObject tempObject = new JSONObject();
                tempObject.put("messageId", resultSet.getString("message_id"));
                tempObject.put("messageText", resultSet.getString("message_text"));
                jsonArray.add(tempObject);
            }
            return jsonArray.toJSONString();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionPool.closeConnection(connection);
        }
        return null;
    }


    public static void addMessageChange(int messageId, String messageText) {
        Connection connection = null;
        final String sql = "INSERT INTO message_changes (message_text, message_id, id)" + "VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionPool.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, messageText);
            preparedStatement.setInt(2, messageId);
            preparedStatement.setInt(3, Util.getRowsNumber("message_changes", connection)+1);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionPool.closeConnection(connection);
        }
    }
}
