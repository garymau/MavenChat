package servlet;

import connection.DatabaseConnection;
import database.*;
import ext.History;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;
import requests.BaseRequest;
import requests.UpdateRequest;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;


public class Servlet extends HttpServlet {
    protected static Connection connection = null;
    private static final Logger logger = Logger.getLogger(Servlet.class.getName());
    private static final String lineStart = "\n++++++++++++++++++++\n";
    private static final String lineEnd = "\n--------------------\n";

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        try {
            LogManager.getLogManager().readConfiguration(
                    getClass().getResourceAsStream("/logging.properties")
            );
            connection = DatabaseConnection.setupDBConnection();
            File xmlFile = new File("/history.xml");
            if (xmlFile.exists())
                History.fromXML(xmlFile);
        } catch (IOException e) {
            logger.log(Level.CONFIG, lineStart + "!!!!!!!Could not setup logging config " + e.toString() + lineEnd);
        } catch (ParserConfigurationException | SAXException e) {
            logger.log(Level.INFO, lineStart + "couldn't parse your xml doc, sorry" + e.getCause()+ lineEnd);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MessageDeletionsTable.deleteMessage(request, connection);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String type = null;
        try(BufferedReader br = request.getReader()){
            JSONParser parser = new JSONParser();
            try {
                JSONObject jsonObject = (JSONObject)parser.parse(br.readLine());
                type = (String)jsonObject.get("type");
                if(type.compareTo("CHANGE_MESSAGE")==0)
                    MessagesTable.changeMessage(request, connection, br);
                if(type.compareTo("CHANGE_USERNAME")==0) {
                    int userId = ((Long)((JSONObject)jsonObject.get("user")).get("userId")).intValue();
                    String username = (String)((JSONObject)jsonObject.get("user")).get("username");
                    UsernameChangesTable.changeUsername(userId, username, connection);
                    UsersTable.changeUsernameById(userId, username, connection);
                }
            } catch (ParseException e) {
                logger.log(Level.INFO, lineStart + "ParseException" + e.getMessage() + lineEnd);
            }
        } catch (IOException e) {
            logger.log(Level.INFO, lineStart + "IOException" + e.getMessage() + lineEnd);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MessagesTable.addMessage(request, connection);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("type");
        if(type.compareTo("BASE_REQUEST")==0) {
            BaseRequest.proceedBaseRequest(request, response, connection);
        }
        else if(type.compareTo("GET_UPDATE")==0) {
            try {
                UpdateRequest.proceedUpdateRequest(request, response, connection);
            } catch (TransformerException | ParserConfigurationException e) {
                logger.log(Level.INFO, lineStart + "TransformerException" + e.getMessage() + lineEnd);
            } catch (java.text.ParseException e) {
                logger.log(Level.INFO, lineStart + "ParseException" + e.getMessage() + lineEnd);
            }
        }
        else {
            logger.log(Level.INFO, lineStart + "Unsupported type of request!" +
                    " Expected: 'BASE_REQUEST' or 'GET_UPDATE' " + lineEnd);
        }
    }
}
