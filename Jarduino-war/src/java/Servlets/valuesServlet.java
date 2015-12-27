/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import DAO.Normalvalue;
import DAO.Registry;
import DAO.Sensor;
import GsonAdapters.RegistryAdapter;
import GsonAdapters.SensorAdapter;
import HibernateConf.HibernateUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Math.log;
import java.util.Date;
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
 * @author maxi
 */
@WebServlet(name = "valuesServlet", urlPatterns = {"/valuesS"})
public class valuesServlet extends HttpServlet {

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
        Session session = HibernateUtil.getSessionFactory().openSession();
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");
        String returnType = request.getParameter("returnType");
        if (action.equalsIgnoreCase("valuesPage")) {
            List<Normalvalue> values = (List<Normalvalue>) session.createCriteria(Normalvalue.class).list();

            /*if (returnType != null && returnType.equals("json")) {
             GsonBuilder gsonBuilder = new GsonBuilder();
             Gson gson = gsonBuilder.registerTypeAdapter(Sensor.class, new SensorAdapter()).create();
             String json = gson.toJson(sensors);
             response.setContentType("application/json");
             response.setCharacterEncoding("UTF-8");
             response.getWriter().write(json);

             } else {*/
            request.setAttribute("values", values);
            RequestDispatcher view = request.getRequestDispatcher("Values.jsp");
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
        String action = request.getParameter("action");
        if (action.equalsIgnoreCase("updateValue")) {
            String idValue = request.getParameter("idValue");
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = null;
            boolean success = false;
            try {
                tx = session.beginTransaction();
                tx.setTimeout(5);
                Normalvalue nval = (Normalvalue) session.get(Normalvalue.class, Integer.parseInt(idValue));
                String newValue = request.getParameter("newValue");
                nval.setValue(Integer.parseInt(newValue));
                tx.commit();
                success = true;
            } catch (RuntimeException e) {
                try {
                    success = false;
                    tx.rollback();
                } catch (RuntimeException rbe) {
                    throw rbe;
                }
                throw e;
            } finally {
                if (session != null) {
                    session.close();
                }
            }
            JSONObject resultJSON = new JSONObject();
            resultJSON.append("response", success?"ok" : "error");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(resultJSON.toString());
            return;
        }
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
