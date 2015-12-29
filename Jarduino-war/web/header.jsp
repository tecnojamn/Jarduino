<%-- 
    Document   : header
    Created on : 26-dic-2015, 19:31:52
    Author     : Andres
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<script src="js/jquery.js" type="text/javascript"></script>
<script>
    $(document).ready(function () {
            
            
        //Check for new alerts (notifications)
        var stopTheFuckingBeep = false;
        
        setInterval( function (){
            $.get(
                //url
                "Notifications?action=checkNewAlerts",
                //success
                function (data) {
                    if (data.newAlerts[0] === "true") {
                        
                        if(!stopTheFuckingBeep){  beep(); }          
           
                        if(pgurl.indexOf("Monitor") > -1){ stopTheFuckingBeep = true;}
                        
                        $("#newAlertsSpan").show();
                    }
                },
                //datatype
                "json"
            );
        }, 5000);
        
        
        
        //Set active navbar option
        var pgurl = window.location.href.substr(window.location.href.lastIndexOf("/")+1);
        $("ul.nav li").each(function(){
            if(pgurl.indexOf($(this).find("a").attr("href")) > -1)
            $(this).addClass("active");
        })
        
    });
    
    function beep() {
        var snd = new Audio("js/beep.wav"); 
        snd.play();
    }
</script>
<!-- Navigation -->
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="home">Jarduino - for the people</a>
    </div>
    <!-- Top Menu Items -->
    <ul class="nav navbar-right top-nav">
        <li class="dropdown">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-envelope"></i> <b class="caret"></b></a>
            <ul class="dropdown-menu message-dropdown">
                <li class="message-preview">
                    <a href="#">
                        <div class="media">
                            <span class="pull-left">
                                <img class="media-object" src="http://placehold.it/50x50" alt="">
                            </span>
                            <div class="media-body">
                                <h5 class="media-heading"><strong>Usuario 1</strong>
                                </h5>
                                <p class="small text-muted"><i class="fa fa-clock-o"></i> Yesterday at 4:32 PM</p>
                                <p>Lorem ipsum dolor sit amet, consectetur...</p>
                            </div>
                        </div>
                    </a>
                </li>
                <li class="message-preview">
                    <a href="#">
                        <div class="media">
                            <span class="pull-left">
                                <img class="media-object" src="http://placehold.it/50x50" alt="">
                            </span>
                            <div class="media-body">
                                <h5 class="media-heading"><strong>User</strong>
                                </h5>
                                <p class="small text-muted"><i class="fa fa-clock-o"></i> Yesterday at 4:32 PM</p>
                                <p>Lorem ipsum dolor sit amet, consectetur...</p>
                            </div>
                        </div>
                    </a>
                </li>
                <li class="message-preview">
                    <a href="#">
                        <div class="media">
                            <span class="pull-left">
                                <img class="media-object" src="http://placehold.it/50x50" alt="">
                            </span>
                            <div class="media-body">
                                <h5 class="media-heading"><strong>User</strong>
                                </h5>
                                <p class="small text-muted"><i class="fa fa-clock-o"></i> Yesterday at 4:32 PM</p>
                                <p>Lorem ipsum dolor sit amet, consectetur...</p>
                            </div>
                        </div>
                    </a>
                </li>
                <li class="message-footer">
                    <a href="#">Read All New Messages</a>
                </li>
            </ul>
        </li>
        <%
            HttpSession httpSession;
            httpSession = request.getSession(true);
        %>
        <li class="dropdown">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-user"></i> <%= ((DAO.User) httpSession.getAttribute("user")).getUser()%> <b class="caret"></b></a>
            <ul class="dropdown-menu">
                <li>
                    <a href="#"><i class="fa fa-fw fa-user"></i> Profile</a>
                </li>
                <li>
                    <a href="#"><i class="fa fa-fw fa-envelope"></i> Inbox</a>
                </li>
                <li>
                    <a href="#"><i class="fa fa-fw fa-gear"></i> Settings</a>
                </li>
                <li class="divider"></li>
                <li>
                    <a href="User?action=logout"><i class="fa fa-fw fa-power-off"></i> Log Out</a>
                </li>
            </ul>
        </li>
    </ul>
    <!-- Sidebar Menu Items - These collapse to the responsive navigation menu on small screens -->
    <div class="collapse navbar-collapse navbar-ex1-collapse">
        <ul class="nav navbar-nav side-nav">
            <li>
                <a href="home"><i class="fa fa-fw fa-dashboard"></i> Dashboard</a>
            </li>
            <li>
                <a href="Monitor"><i class="fa fa-fw fa-desktop"></i> Live Monitor</a>
            </li>
            <li>
                <a href="Notifications"><i class="fa fa-fw fa-bell"></i> Notifications  <span id="newAlertsSpan" title="You have new notifications!" style="display:none;color:#ff9966;"><i class="fa fa-exclamation-triangle"></i></span></a>            </li>
            <li>
                <a href="valuesS?action=valuesPage"><i class="fa fa-fw fa-wrench"></i> Settings</a>
            </li>
        </ul>
    </div>
    <!-- /.navbar-collapse -->
</nav>
</body>

</html>
