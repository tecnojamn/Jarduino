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
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;

/**
 *
 * @author Nicolas
 */
@WebServlet(name = "SensorServlet", urlPatterns = {"/Sensor"})
public class SensorServlet extends HttpServlet {

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
            throws ServletException, IOException, InterruptedException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");
        String returnType = request.getParameter("returnType");
        if (action == null || action.isEmpty()) {
            action = "index";
        }

        if (action.equalsIgnoreCase("index")) {
            List<Sensor> sensors = (List<Sensor>) session.createCriteria(Sensor.class).list();

            if (returnType != null && returnType.equals("json")) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.registerTypeAdapter(Sensor.class, new SensorAdapter()).create();
                String json = gson.toJson(sensors);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json);

            } else {
                request.setAttribute("sensors", sensors);
                RequestDispatcher view = request.getRequestDispatcher("monitor.jsp");
                view.forward(request, response);
            }
        } else if (action.equalsIgnoreCase("getSensorReg")) {
            if (returnType != null && returnType.equals("json")) {
                String sensorId = request.getParameter("sId");
                session.clear();
                HibernateUtil.getSessionFactory().getCache().evictEntityRegions();
                HibernateUtil.getSessionFactory().getCache().evictCollectionRegions();
                HibernateUtil.getSessionFactory().getCache().evictDefaultQueryRegion();
                HibernateUtil.getSessionFactory().getCache().evictQueryRegions();
                Thread.sleep(2000);
                String hql = "SELECT * FROM registry R WHERE R.idsensor =" + sensorId + " ORDER BY R.date DESC";
                Registry r = (Registry) session.createSQLQuery(hql).addEntity(Registry.class).setMaxResults(1).uniqueResult();
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.registerTypeAdapter(Registry.class, new RegistryAdapter()).create();
                String json = gson.toJson(r);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json);

            }
        }else if (action.equalsIgnoreCase("createRegistry")) {
            Registry r = new Registry();
            r.setDate(new Date());
            r.setIdsensor(Integer.parseInt(request.getParameter("id")));
            r.setValue(Integer.parseInt(request.getParameter("val")));

            session.save(r);
        } else if (action.equalsIgnoreCase("getSensorNormalVal")) {
            String sid = request.getParameter("sId");
            String hql = "SELECT * FROM normalvalue N WHERE N.idsensor =" + sid + "";
            Normalvalue n = (Normalvalue) session.createSQLQuery(hql).addEntity(Normalvalue.class).setMaxResults(1).uniqueResult();
            if (n == null) {
                response.setStatus(404);
            } else {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{'val':" + n.getValue() + "}");
            }
        }
        session.flush();
        session.clear();
        session.close();

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
        try {
            processRequest(request, response);
        } catch (InterruptedException ex) {
            Logger.getLogger(SensorServlet.class.getName()).log(Level.SEVERE, null, ex);
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
        try {
            processRequest(request, response);
        } catch (InterruptedException ex) {
            Logger.getLogger(SensorServlet.class.getName()).log(Level.SEVERE, null, ex);
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
