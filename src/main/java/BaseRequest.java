import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

public class BaseRequest {
    public static void proceedBaseRequest(HttpServletRequest request, HttpServletResponse response, Connection connection){
        String username = request.getParameter("username");
        int id = Util.getUserId(username);
        if(id == -1)
            id = Util.addNewUser(username);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("currentUserId", id);
        jsonObject.put("messageToken", 0);
        jsonObject.put("messageEditToken", 0);
        jsonObject.put("messageDeleteToken", 0);
        jsonObject.put("userToken", 0);
        jsonObject.put("userChangeToken", 0);
        Util.sendResponse(response, jsonObject.toJSONString());
    }
}
