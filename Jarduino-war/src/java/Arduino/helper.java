/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arduino;
import HibernateConf.HibernateUtil;
import org.hibernate.Session;
import DAO.*;

/**
 *
 * @author Julio
 */
public class helper {
    Session session = null;
    
    public helper() {
        this.session = HibernateUtil.getSessionFactory().openSession();
    }
    
    public User getUser(int userId){
        User user = null;
        try {
            org.hibernate.Transaction tx = session.beginTransaction();
            user = (User)session.createQuery("from User Where id="+Integer.toString(userId)).list().get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
    
}
