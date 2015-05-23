package ext;

import org.xml.sax.Attributes;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

    public static synchronized void fromXML(File xmlFile) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();

        DefaultHandler defaultHandler = new DefaultHandler(){

            JSONObject message ;
            boolean fmessage = false;
            boolean fid = false;
            boolean fusername = false;
            boolean ftext = false;
            boolean ftime = false;

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                super.startElement(uri, localName, qName, attributes);

                if (qName.equalsIgnoreCase("message")){
                    fmessage = true;
                }

                if (qName.equalsIgnoreCase("id")){
                    fid = true;
                }

                if (qName.equalsIgnoreCase("username")){
                    fusername = true;
                }

                if (qName.equalsIgnoreCase("text")){
                    ftext = true;
                }

                if (qName.equalsIgnoreCase("time")){
                    ftime = true;
                }
            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {
                super.endElement(uri, localName, qName);

                if (qName.equalsIgnoreCase("message")){
                    System.out.println("\n***message: "+message.toJSONString()+"***");
                }
            }

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                super.characters(ch, start, length);

                if (fmessage){
                    message = new JSONObject();
                    fmessage = false;
                }

                if (fid){
                    if (message==null)
                        message = new JSONObject();
                    message.put("messageid", new String(ch, start, length));
                    fid = false;
                }

                if (fusername){
                    if (message==null)
                        message = new JSONObject();
                    message.put("username", new String(ch, start, length));
                    fusername = false;
                }

                if (ftext){
                    if (message==null)
                        message = new JSONObject();
                    message.put("messagetext", new String(ch, start, length));
                    ftext = false;
                }

                if (ftime){
                    if (message==null)
                        message = new JSONObject();
                    message.put("messagetime", new String(ch, start, length));
                    ftime = false;
                }
            }
        };

        if (xmlFile!=null)
            saxParser.parse(xmlFile,defaultHandler);

    }
}
