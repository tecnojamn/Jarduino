<%-- 
    Document   : Values
    Created on : 26-dic-2015, 23:26:23
    Author     : maxi
--%>

<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">

    <head>
        <script src="js/jquery.js" type="text/javascript"></script>
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

        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
            <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
            <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
        <![endif]-->
        <script>
            $(document).ready(function () {
                $("body").on("click", "a.btn-update-value", function (e) {
                    var id = $(e.target).attr('id');
                    $("#newValue_" + id).show();
                    $("#confirm_" + id).show();
                });
                $("body").on("click", "button.btn-confirm-value", function (e) {
                    console.log(e.target);
                    var idValue = $(e.target).attr('data-value-id');
                    console.log(idValue);
                    var newValue = $("#newValue_" + idValue);
                    console.log(newValue);
                    var okAlert = $("#ok-alert");
                    var errorAlert = $("#error-alert");
                    okAlert.hide();
                    errorAlert.hide();
                    $.ajax({
                        url: "valuesS",
                        method: "POST",
                        data: {
                            "action": "updateValue",
                            "idValue": idValue,
                            "newValue": newValue.val()
                        },
                        dataType: "json",
                        success: function (data) {
                            console.log(data);
                            if (data.response[0] === "ok")
                            {
                                okAlert.show();
                                $("#oldValue_"+idValue).text(newValue.val());
                                newValue.hide();
                                $("#confirm_" + idValue).hide();

                            }
                            else if (data.response[0] === "error") {
                                errorAlert.show();
                            }
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            errorAlert.show();
                        }
                    });
                });
            });</script>
    </head>

    <body>

        <div id="wrapper">
            <jsp:include page="header.jsp" />
            <div id="page-wrapper">

                <div class="container-fluid">

                    <!-- Page Heading -->
                    <div class="row">

                        <div class="col-lg-12">
                            <h1 class="page-header">
                                Settings  <small>Jarduino</small>
                            </h1>
                            <ol class="breadcrumb">
                                <li class="active">
                                    <i class="fa fa-wrench"></i> Settings
                                </li>
                            </ol>
                        </div>
                    </div>                   
                    <!-- /.row -->
                    
                    <div class="alert alert-danger" role="alert" id="error-alert" style="display:none;">
                        <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                        <span class="sr-only">Error: </span>
                        Ocurrio un error inesperado
                    </div>
                    <div class="alert alert-success" role="alert" id="ok-alert" style="display:none;">
                        <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
                        <span class="sr-only">Éxito</span>
                        Se ha modificado el valor correctamente
                    </div>
                    <div class="row">
                        <div class="row col-md-12 col-md-offset-0 custyle">
                            <table class="table table-striped custab" id="ValuesTable">
                                <thead>
                                    <tr>
                                        <th>Id Sensor</th>
                                        <th>Sensor Name</th>
                                        <th>Sensor Type</th>
                                        <th>Normal Value</th>
                                        <th class="text-center">Action</th>
                                    </tr>
                                    <c:forEach items="${values}" var="value">
                                        <tr>
                                            <td>${value.sensor.id} </td>
                                            <td>${value.sensor.name} </td>
                                            <td>${value.sensor.type.name} </td>
                                            <td id="oldValue_${value.id}">${value.value} </td>
                                            <td><a style="margin: 2px;" class="btn btn-info btn-xs btn-update-value" href="#" id='${value.id}'>
                                                    <span class="glyphicon glyphicon-edit"> 
                                                    </span> Edit </a> <input type='number' id='newValue_${value.id}' name="newValue_${value.id}" hidden>
                                                <button style="margin: 2px; display:none;" id='confirm_${value.id}' class="btn btn-success btn-xs btn-confirm-value">
                                                    <span class="glyphicon glyphicon-check" data-value-id="${value.id}">Confirm</span>
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </thead>
                            </table>                     
                        </div>
                    </div>


                </div>
                <!-- /.row -->


            </div>
            <!-- /.container-fluid -->

        </div>
        <!-- /#page-wrapper -->

    </div>
    <!-- /#wrapper -->

    <!-- jQuery -->
    <script src="js/jquery.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="js/bootstrap.min.js"></script>

    <!-- Morris Charts JavaScript -->
    <script src="js/plugins/morris/raphael.min.js"></script>
    <script src="js/plugins/morris/morris.min.js"></script>
    <script src="js/plugins/morris/morris-data.js"></script>

</body>

</html>