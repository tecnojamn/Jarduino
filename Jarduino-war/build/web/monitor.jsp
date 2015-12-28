<%-- 
    Document   : monitor
    Created on : Dec 5, 2015, 8:15:57 PM
    Author     : Nicolas
--%>

<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Monitor</title>
        <!-- Bootstrap Core CSS -->
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <!-- Custom CSS -->
        <link href="css/sb-admin.css" rel="stylesheet">
        <link href="css/custom.css" rel="stylesheet">
        <!-- Morris Charts CSS -->
        <link href="css/plugins/morris.css" rel="stylesheet">
        <!-- Custom Fonts -->
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    </head>
    <body>

        <div id="wrapper" >

            <jsp:include page="header.jsp" />
            
            <div id="page-wrapper">

                <div class="container-fluid">

                    <!-- Page Heading -->
                    <div class="row">

                        <div class="col-lg-12">
                            <h1 class="page-header">
                                Monitor <small>Javarduino</small>
                            </h1>
                            <ol class="breadcrumb">
                                <li class="active">
                                    <i class="fa fa-desktop"></i> Monitor
                                </li>
                            </ol>

                        </div>
                    </div>
                    <!-- /.row -->
                    <div class="row" style="    margin-bottom: 10px;                         padding: 0 15px;">
                        <button id="monitor-power" type="button" class="btn btn-lg btn-success">ON/OFF</button>
                    </div>
                    <div class="row" style="    margin-bottom: 10px;                         padding: 0 15px;">
                        <div id="monitor-log"></div>
                    </div>

                    <div class="row">
                        <div class="col-lg-12">
                            <div class="alert alert-info alert-dismissable">
                                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                Status : ${sensors.size()} sensors available

                            </div>
                        </div>
                    </div>
                    <!-- /.row -->
                    <div class='row monitor-container' id='sensors_container'></div>

                    <!--<ul class="registry-item">
                        <li>1</li>
                        <li>24/12/87 00:00:12</li>
                        <li>26</li>
                    </ul>
                    <ul class="registry-item">
                        <li>1</li>
                        <li>24/12/87 00:00:12</li>
                        <li>26</li>
                    </ul>
                    <ul class="registry-item">
                        <li>1</li>
                        <li>24/12/87 00:00:12</li>
                        <li>26</li>
                    </ul>-->


                </div>
                <!-- /.row -->


            </div>
            <!-- /.container-fluid -->

        </div>
        <!-- /#page-wrapper -->

    </div>
    <!-- jQuery -->
    <script src="js/jquery.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="js/bootstrap.min.js"></script>


    <script src="js/ajax/javarduino_monitor.js"></script>
</body>
</html>
