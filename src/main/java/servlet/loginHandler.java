package servlet;

import connection.ConnectionPool;
import connection.DatabaseConnection;
import database.ChattersTable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Diman on 19.04.2015.
 */

public class loginHandler extends HttpServlet {

    protected static volatile Connection connection = null;
    private static final Logger logger = LogManager.getLogger(DatabaseConnection.class.getName());
    private static final String lineStart = "\n++++++++++++++++++++\n";
    private static final String lineEnd = "\n--------------------\n";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        //connection = DatabaseConnection.setupDBConnection();
        connection = ConnectionPool.getConnection();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        connection = ConnectionPool.getConnection();
        req.setCharacterEncoding("UTF-8");
        if (req.getServletPath().equals("/logout")){
            HttpSession session = req.getSession(false);
            String nickName = (String)session.getAttribute("nickname");
            Cookie loginCookie = null;
            Cookie[] cookies = req.getCookies();
            if (cookies!=null){
                for (Cookie cookie: cookies){
                    if (cookie.getName().equals("nickname")){
                        loginCookie = cookie;
                        break;
                    }
                }
            }
            if (loginCookie!=null){
                loginCookie.setMaxAge(0);
                resp.addCookie(loginCookie);
            }
            try {
                ChattersTable.deleteChatter(nickName, session.getId());
            } catch (SQLException e) {
                logger.info("problems with deleting from chatters table" + e.getMessage());
            }
            session.invalidate();
            resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
            resp.setDateHeader("Expires", 0); // Proxies.
            resp.sendRedirect(req.getContextPath() + "/homepage.jsp");
        }
        else
            if (req.getServletPath().equals("/login")) {
                String nickName = req.getParameter("nickname");
                HttpSession session = req.getSession(false);
                if (nickName!=null && !nickName.equals("") && session!=null) {
                    int timeout = Integer.parseInt(getServletContext().getInitParameter("sessionTimeout"));
                    timeout *= 60;
                    session.setMaxInactiveInterval(timeout);
                    session.setAttribute("nickname", nickName);
                    Cookie userCookie = new Cookie("nickname", nickName);
                    userCookie.setMaxAge(60 * 60);
                    resp.addCookie(userCookie);
                    try {
                        ChattersTable.insertChatter(nickName, session.getId());
                    } catch (SQLException e) {
                        logger.info("problems with insert into chatters table" + e.getMessage());
                    }
                    resp.sendRedirect(req.getContextPath() + "/homepage.jsp");
                }
            }
        else
                resp.sendRedirect(req.getContextPath() + "/login.jsp");
        ConnectionPool.closeConnection(connection);
    }
}
