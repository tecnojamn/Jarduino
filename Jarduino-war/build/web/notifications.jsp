<%-- 
    Document   : monitor
    Created on : Dec 5, 2015, 8:15:57 PM
    Author     : Nicolas
--%>

<%@page import="DAO.Alert"%>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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


        <div id="wrapper"  >

            <jsp:include page="header.jsp" />


            <div id="page-wrapper">

                <div class="container-fluid">

                    <!-- Page Heading -->
                    <div class="row">

                        <div class="col-lg-12">
                            <h1 class="page-header">
                                Notifications <small>Javarduino</small>
                            </h1>
                            <ol class="breadcrumb">
                                <li class="active">
                                    <i class="fa fa-dashboard"></i> Notifications
                                </li>
                            </ol>

                        </div>
                    </div>



                    <!-- /.row -->
                    <div class='row' id=''>
                        <div class="notifications-container  col-lg-12" id="notifications_container">
                            <table class="table-striped table">
                                <tr> 
                                    <th>#</th>
                                    <th>Sensor</th>
                                    <th>Value</th>
                                    <th>Date</th>
                                    <th>Seen</th>
                                </tr>
                                <c:forEach items="${alerts}" var="alert">
                                    <fmt:formatDate value="${alert.date}" var="formattedDate" 
                                                    type="date" pattern="MM-dd-yyyy HH:mm" />
                                    <tr>
                                        <td><c:out value="${alert.id}"/></td>
                                        <td><c:out value="${alert.idsensor}"/></td>  
                                        <td><c:out value="${alert.value}"/></td>
                                        <td><c:out value="${formattedDate}"/></td>
                                        <c:choose>
                                            <c:when test="${alert.seen==false}">
                                                <td><img style="width:20px;" src="img/alert.gif" />New</td>
                                                </c:when>
                                                <c:otherwise >
                                                <td></td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                </c:forEach>
                            </table>
                        </div>

                    </div>
                    <!-- /.container-fluid -->

                </div>
                <!-- /#page-wrapper -->

            </div>
            <!-- /#wrapper -->

        </div>
        <!-- jQuery -->
        <script src="js/jquery.js"></script>

        <!-- Bootstrap Core JavaScript -->
        <script src="js/bootstrap.min.js"></script>


        <script src="js/ajax/javarduino_monitor.js"></script>
    </body>
</html>
