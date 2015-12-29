/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import org.json.JSONArray;
import org.json.JSONObject;
import DAO.Alert;
import DAO.Normalvalue;
import DAO.Sensor;
import HibernateConf.HibernateUtil;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Julio
 */
public class AlertController {

    public boolean checkAndSaveAlert(String dataJson) {
        List<Normalvalue> nomValList = null;
        //Carga valores normales
        nomValList = getNormalValues();
        if (nomValList != null) {

            //Crea objeto json con string por parametro
            JSONObject json = new JSONObject(dataJson);

            //Obtiene el array de Registos
            JSONArray arr = json.getJSONArray("Registry");
            //Recorre el array
            for (int i = 0; i < arr.length(); i++) {
                //Obtiene los datos idSensor y value
                int idSensor = arr.getJSONObject(i).getInt("idSensor");
                int value = arr.getJSONObject(i).getInt("value");
                for (Normalvalue nomVal : nomValList) {
                    if (nomVal.getSensor().getId() == idSensor) {
                        if (nomVal.getValue() < value) {
                            saveAlert(idSensor, value);
                        }
                        break;
                    }
                }
            }
        }

        return false;
    }

    private List<Normalvalue> getNormalValues() {
        List<Normalvalue> nomValList = null;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            org.hibernate.Transaction tx = session.beginTransaction();
            nomValList = (List<Normalvalue>) session.createQuery("from Normalvalue").list();
        } catch (Exception e) {
        }
        return nomValList;
    }

    private boolean saveAlert(int idSensor, int value) {
        try {
            Sensor s = new Sensor();
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            s = (Sensor)session.createQuery("from Sensor Where id="+Integer.toString(idSensor)).list().get(0);
            Alert a = new Alert();
            a.setIdsensor(s);
            a.setValue(value);
            a.setDate(new Date());
            a.setSeen(false);
            System.out.println("After save");
            session.save(a);
            System.out.println("After comit");
            tx.commit();
            session.close();
            return true;
        } catch (Exception e) {
        }

        return false;
    }
}
