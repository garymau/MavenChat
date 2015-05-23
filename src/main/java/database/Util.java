package database;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class Util {
    public static void sendResponse(HttpServletResponse response, String jsonString) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.println(jsonString);
        } catch (IOException e) {
            System.out.println("Sending response error.");
            e.printStackTrace();
        }
        finally {
            if (out!=null){
                out.flush();
                out.close();
            }
        }
    }


    public static int getRowsNumber(String tableName, Connection connection){
        try {
            final String sql = "SELECT COUNT(*) AS maxid FROM ";
            PreparedStatement maxId = connection.prepareStatement(sql + tableName);
            ResultSet resultSet = maxId.executeQuery();
            if (resultSet.next())
                return resultSet.getInt("maxid");
            else{
                System.out.println("there are no rows in \""+tableName+"\" table");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
