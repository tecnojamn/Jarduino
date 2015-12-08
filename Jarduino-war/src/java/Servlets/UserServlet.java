/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;
import org.hibernate.Session;
import HibernateConf.HibernateUtil;
import javax.servlet.annotation.WebServlet;

import HibernateConf.HibernateUtil;
import DAO.*;
import Arduino.*;

/**
 *
 * @author Julio
 */
@WebServlet(name = "UserServlet", urlPatterns = {"/User"})
public class UserServlet extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");
        HttpSession session;
        session = request.getSession();
        if (session != null) {
            //mostrar error, usted esta logueado
            response.sendRedirect("home");

            /**
             * *******************SIN ACCION*****************************
             */
        } else if (action == null) {
            RequestDispatcher view = request.getRequestDispatcher("login.html");
            view.forward(request, response);

            /**
             * *******************ACCION LOGIN***************************
             */
        } else if (action.equals("login")) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            User us = null;
            try {
                Session hibSession = HibernateUtil.getSessionFactory().openSession();
                us = (User) hibSession.createQuery("from User Where user='" + username + "' and password='" + password + "'").list().get(0);

            } catch (Exception e) {
            }
            if (us != null) {
                session = request.getSession(true); //getSession(true) indica que se va a crear la sesion
                session.setAttribute("user", us);
                response.sendRedirect("home");
            } else {
                //mostrar error, login incorrecto
            }
            RequestDispatcher view;
            view = request.getRequestDispatcher("login.html");
            view.forward(request, response);
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
