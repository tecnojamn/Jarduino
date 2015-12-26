<%
    Integer error = (Integer) request.getAttribute("error");
    String errorMessage = (String) request.getAttribute("errorMessage");
%>
<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<html>
    <head>

        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">

        <title>J-ARDUINO for the people</title>

        <!-- Bootstrap Core CSS -->
        <link href="css/bootstrap.min.css" rel="stylesheet">

        <!-- Custom CSS -->
        <link href="css/sb-admin.css" rel="stylesheet">

        <!-- Morris Charts CSS -->
        <link href="css/plugins/morris.css" rel="stylesheet">

        <!-- Custom Fonts -->
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

    </head>
    <body>
        <%
            if (error != null) {
        %>
        <div class="alert alert-danger" role="alert">
            <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
            <span class="sr-only">Error:</span>
            <%=errorMessage%>
        </div>
        <%
            }
        %>
        <div class="container-fluid">
            <div class="row">

                <div class="col-lg-6 col-lg-offset-3" style="    background: white;padding: 20px;border-radius: 5px;">
                    <h1 class="page-header" style="    margin-top: 0px;">
                        Sign in
                    </h1>
                    <form role="form" action="User?action=login" method="post">
                        <div class="form-group">
                            <label>User : </label>
                            <input class="form-control" name="username" placeholder="Enter name">
                        </div>
                        <div class="form-group">
                            <label>Password : </label>
                            <input class="form-control" type="password" name="password" placeholder="Enter password">
                        </div>
                        <button type="submit" class="btn btn-default">Enter</button>
                    </form> 
                </div>
            </div> 
        </div>
    </body>
</html>
