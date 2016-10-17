<%--
  Created by IntelliJ IDEA.
  User: Diman
  Date: 24.05.2015
  Time: 8:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"  isErrorPage="true" import="java.io.*"%>
<html>
<head>
    <title>ServerError</title>
</head>
<body>
  <h1>Message:</h1>
    <%
      if (exception.getMessage()!=null){
        out.println(exception.getMessage());
      }
      else {
        out.println("server error occured, try another time, pal");
      }
      out.flush();
    %>
  <p><a href="../homepage.jsp">I wanna chat, actually</a></p>


</body>
</html>
