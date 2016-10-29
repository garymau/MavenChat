<%@ page contentType="text/html;charset=UTF-8" language="java"  %>
<html>
<head>
  <link rel="stylesheet" href='bootstrap.min.css' type="text/css" />
  <title>Welcome!!!</title>
</head>
<body>
<div class="container-fluid">
  <div class="row">
    <div class="col-md-4"></div>
    <div class="col-md-4" style="display:flex;justify-content:center;align-items:center;">
      <h1>Welcome!!!</h1>
      <form role="form" action="${pageContext.request.contextPath}/login" method="post" name="Login">
        <label class="control-label" for="nickname" >Nickname</label>
        <input type="text" name="nickname" id="nickname" class="form-control" placeholder="Enter nickname..">
        <input class="btn btn-default" name="submit" type="submit" value="Login">
      </form>
    </div>
    <div class="col-md-4"></div>
  </div>
</div>

</body>
</html>