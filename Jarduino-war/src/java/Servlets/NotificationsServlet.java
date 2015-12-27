/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import DAO.Alert;
import DAO.Registry;
import DAO.Sensor;
import HibernateConf.HibernateUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONObject;

/**
 *
 * @author Andres
 */
@WebServlet(name = "NotificationsServlet", urlPatterns = {"/Notifications"})
public class NotificationsServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession httpSession;
        httpSession = request.getSession(true);

        if (httpSession.getAttribute("user") == null) {
            RequestDispatcher view;
            view = request.getRequestDispatcher("login.jsp");
            view.forward(request, response);
            return;
        }

        response.setContentType("text/html;charset=UTF-8");
        Session session = HibernateUtil.getSessionFactory().openSession();
        String hql = "SELECT * FROM alert a ORDER BY a.date DESC";
        List<Alert> alerts = (List<Alert>) session.createSQLQuery(hql).addEntity(Alert.class).setMaxResults(50).list();
        List<Alert> newAlerts = new ArrayList<Alert>(alerts) {
        };
        Collections.copy(newAlerts, alerts);

        request.setAttribute("alerts", alerts);
        RequestDispatcher view = request.getRequestDispatcher("notifications.jsp");
        view.forward(request, response);

        //apply filter
        newAlerts.removeIf(a -> a.getSeen() == true);

        for (Alert alert : newAlerts) {
            alert.setSeen(true);
        }

        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            for (Alert alert : newAlerts) {
                session.update(alert);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String returnType = request.getParameter("returnType");

        if (action!= null &&  action.equals("checkNewAlerts")) {
            Session session = HibernateUtil.getSessionFactory().openSession();
            String hql = "SELECT * FROM alert a WHERE a.seen = 0";
            boolean newAlerts = session.createSQLQuery(hql).addEntity(Alert.class).setMaxResults(1).list().size() > 0;
            String newAlertsString = newAlerts == true ? "true": "false";
            JSONObject resultJSON = new JSONObject();
            resultJSON.append("newAlerts", newAlertsString);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(resultJSON.toString());
            return;
        }

        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
