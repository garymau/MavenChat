package requests;

import connection.ConnectionPool;
import database.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.sql.Connection;
import java.text.ParseException;

import ext.History;
import org.json.simple.JSONValue;

public class UpdateRequest {
    public static void proceedUpdateRequest(HttpServletRequest request, HttpServletResponse response) throws TransformerException, ParserConfigurationException, ParseException {
        Connection connection = ConnectionPool.getConnection();

        int messageId = Integer.parseInt(request.getParameter("messageToken"));
        int messageEditId = Integer.parseInt(request.getParameter("messageEditToken"));
        int messageDeletedId = Integer.parseInt(request.getParameter("messageDeleteToken"));
        int userId = Integer.parseInt(request.getParameter("userToken"));
        int userChangeId = Integer.parseInt(request.getParameter("userChangeToken"));

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("messageToken", Util.getRowsNumber("messages", connection));
        jsonObject.put("messages", MessagesTable.getMessages(messageId));

        jsonObject.put("messageEditToken", Util.getRowsNumber("message_changes", connection));
        jsonObject.put("editedMessages", MessageChangesTable.getEditedMessages(messageEditId));

        jsonObject.put("messageDeleteToken", Util.getRowsNumber("message_deletions", connection));
        jsonObject.put("deletedMessagesIds", MessageDeletionsTable.getDeletedMessages(messageDeletedId));

        jsonObject.put("userToken", Util.getRowsNumber("users", connection));
        jsonObject.put("users", UsersTable.getUsers(userId));

        jsonObject.put("userChangeToken", Util.getRowsNumber("username_changes", connection));
        jsonObject.put("changedUsers", UsernameChangesTable.getChangedUsers(userChangeId));

        JSONArray users = (JSONArray)JSONValue.parse(jsonObject.get("users").toString());
        JSONArray messages = (JSONArray)JSONValue.parse(jsonObject.get("messages").toString());
        if (!users.isEmpty() && !messages.isEmpty())
            History.toXML(users, messages, request);

        ConnectionPool.closeConnection(connection);

        Util.sendResponse(response, jsonObject.toJSONString());
    }
}
