package ext;

import com.sun.deploy.net.HttpRequest;
import com.sun.xml.internal.bind.v2.TODO;
import database.UsersTable;
import jdk.nashorn.internal.parser.JSONParser;
import jdk.nashorn.internal.runtime.JSONFunctions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by Diman on 16.05.2015.
 */

public class History {

    public static synchronized void toXML(JSONArray users, JSONArray messages, HttpServletRequest request) throws ParserConfigurationException, TransformerException, ParseException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element rootElement = document.createElement("messages");
        document.appendChild(rootElement);

        for (JSONObject message : (Iterable<JSONObject>) messages) {
            Element msg = document.createElement("message");
            Element msgChild = document.createElement("id");
            msgChild.setTextContent(message.get("messageId").toString());
            msg.appendChild(msgChild);

            int userID = Integer.parseInt(message.get("userId").toString()) - 1;
            JSONObject particularUser = (JSONObject)users.get(userID);
            msgChild = document.createElement("username");
            msgChild.setTextContent(particularUser.get("username").toString());
            msg.appendChild(msgChild);

            msgChild = document.createElement("text");
            msgChild.setTextContent(message.get("messageText").toString());
            msg.appendChild(msgChild);

            DateFormat dateFormat= new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(message.get("messageTime").toString()));
            msgChild = document.createElement("time");
            msgChild.setTextContent(dateFormat.format(calendar.getTime()));
            msg.appendChild(msgChild);

            rootElement.appendChild(msg);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "windows-1251");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(request.getServletContext().getRealPath("/")+"/history.xml"));
        transformer.transform(source, streamResult);
    }

    public static synchronized void fromXML(File xmlFile) throws ParserConfigurationException, SAXException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();

        DefaultHandler

    }
}
