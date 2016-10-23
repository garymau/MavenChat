package servlet;

import connection.ConnectionPool;
import connection.DatabaseConnection;
import database.*;
import ext.History;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
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


public class Servlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(Servlet.class.getName());
    private static final String historyFilePath = "d:/history.xml";
    private static final String lineStart = "\n++++++++++++++++++++\n";
    private static final String lineEnd = "\n--------------------\n";

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        try {
            File xmlFile = new File(historyFilePath);
            if (xmlFile.exists()) {
                History.fromXML(xmlFile);
            }
        }
        catch (ParserConfigurationException | SAXException e) {
            logger.info(lineStart + "couldn't parse your xml doc, sorry" + e.getStackTrace()+ lineEnd);
        } catch (IOException e) {
            logger.info(lineStart + "problem with xml file occured: " + e.getStackTrace() + lineEnd);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MessageDeletionsTable.deleteMessage(request);
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
                    MessagesTable.changeMessage(request, jsonObject);
                if(type.compareTo("CHANGE_USERNAME")==0) {
                    int userId = ((Long)((JSONObject)jsonObject.get("user")).get("userId")).intValue();
                    String username = (String)((JSONObject)jsonObject.get("user")).get("username");
                    UsernameChangesTable.changeUsername(userId, username);
                    UsersTable.changeUsernameById(userId, username);
                }
            } catch (ParseException e) {
                logger.info(lineStart + "ParseException" + e.getMessage() + lineEnd);
            }
        } catch (IOException e) {
            logger.info(lineStart + "IOException" + e.getMessage() + lineEnd);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MessagesTable.addMessage(request);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("type");
        if(type.compareTo("BASE_REQUEST")==0) {
            BaseRequest.proceedBaseRequest(request, response);
        }
        else if(type.compareTo("GET_UPDATE")==0) {
            try {
                UpdateRequest.proceedUpdateRequest(request, response);
            } catch (TransformerException | ParserConfigurationException e) {
                logger.info(lineStart + "TransformerException" + e.getMessage() + lineEnd);
            } catch (java.text.ParseException e) {
                logger.info(lineStart + "ParseException" + e.getMessage() + lineEnd);
            }
        }
        else {
            logger.info(lineStart + "Unsupported type of request!" +
                    " Expected: 'BASE_REQUEST' or 'GET_UPDATE' " + lineEnd);
        }
    }
}
