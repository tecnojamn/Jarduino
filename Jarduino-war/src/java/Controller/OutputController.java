/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.Output;
import HibernateConf.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Julio
 */
public class OutputController {

    public List<Output> getOutputs() {
        List<Output> output;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            org.hibernate.Transaction tx = session.beginTransaction();
            output = (List<Output>)session.createQuery("from Output").list();
            return output;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}