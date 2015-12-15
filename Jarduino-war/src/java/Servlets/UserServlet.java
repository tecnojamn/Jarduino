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
import Utils.MD5;
import java.util.logging.Level;

import java.util.logging.Logger;

/**
 *
 * @author Julio
 */
@WebServlet(name = "UserServlet", urlPatterns = {"/User"})
public class UserServlet extends HttpServlet {

    // assumes the current class is called logger
    private final static Logger LOGGER = Logger.getLogger(UserServlet.class.getName());

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

        HttpSession session;
        session = request.getSession(true);
        String action = request.getParameter("action");

        if (session.getAttribute("user") == null) {
            RequestDispatcher view;
            view = request.getRequestDispatcher("login.jsp");
            view.forward(request, response);
        } else if (action != null && action.equals("logout")) {
            session.setAttribute("user", null);
            session.invalidate();
            RequestDispatcher view;
            view = request.getRequestDispatcher("login.jsp");
            view.forward(request, response);
        } else {
            response.sendRedirect("home");
        }
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
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session;
        session = request.getSession();
        String action = request.getParameter("action");

        if (session.getAttribute("user") == null && action != null && action.equals("login")) {
            LOGGER.info("login");

            String username = request.getParameter("username");
            String password = request.getParameter("password");
            if (username != null && password != null) {

                password = MD5.getMD5(password);
                User us = null;
                //Intenta traer el user de la BD
                try {
                    Session hibSession = HibernateUtil.getSessionFactory().openSession();
                    us = (User) hibSession.createQuery("from User Where user='" + username + "' and password='" + password + "'").list().get(0);

                } catch (Exception e) {
                }
                if (us != null) {
                    session.setAttribute("user", us);
                    response.sendRedirect("home");
                } else {
                    //login incorrecto
                    request.setAttribute("error", 1);
                    request.setAttribute("errorMessage", "Login incorrecto, intentelo nuevamente");
                    RequestDispatcher view;
                    view = request.getRequestDispatcher("login.jsp");
                    view.forward(request, response);
                }
            } else {
                RequestDispatcher view;
                view = request.getRequestDispatcher("login.jsp");
                view.forward(request, response);
            }
        }

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
