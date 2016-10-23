package database;

import connection.ConnectionPool;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;


public class UsernameChangesTable {
    public static String getChangedUsers(int userChangeId){
        Connection connection = null;
        final String sql = "SELECT * from username_changes WHERE id > " + userChangeId;
        try {
            connection = ConnectionPool.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            JSONArray jsonArray = new JSONArray();
            while(resultSet.next()) {
                JSONObject tempObject = new JSONObject();
                tempObject.put("userId", resultSet.getString("id"));
                tempObject.put("username", resultSet.getString("username"));
                tempObject.put("userImage", "icon/doge.jpg");
                jsonArray.add(tempObject);
            }
            return jsonArray.toJSONString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            ConnectionPool.closeConnection(connection);
        }
        return null;
    }


    public static void changeUsername(int userId, String username){
        Connection connection = null;
        final String sql = "INSERT INTO username_changes (id, username) VALUES (?, ?)";
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionPool.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionPool.closeConnection(connection);
        }
    }
}
