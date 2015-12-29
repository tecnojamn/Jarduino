/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.List;
import org.json.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import Utils.RegistryConf;
import DAO.Registry;
import HibernateConf.HibernateUtil;
import com.google.gson.JsonObject;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Julio
 */
public class RegistryController {

    //Es la lista de los sensores y de como se deben guardar sus registro
    private List<RegistryConf> registryConf = null;
    //Lista de ultimos registros de cada sensor, Map<idSensor,Valor>;
    //Si el registro es de guardado ONCHANGE guarda el ultimo valor como valor del mapa
    //Si el registro es de guardado ONTIME se guarda el timestamp del ultimo valor como valor del mapa
    private Map<Integer, Integer> lastRead = null;

    public RegistryController() {
        loadRegistryConf();
    }

    //Lee la forma que debe guardar cada registro
    private void loadRegistryConf() {
        if (registryConf == null) {
            registryConf = new ArrayList<RegistryConf>();
            lastRead = new HashMap<Integer, Integer>();
        }
        readConfXml();
    }

    //Lee la configuracion que se encuentra en conf/RegistryConf
    private void readConfXml() {
        try {
            File inputFile = new File("C:/Users/Julio/Documents/GitHub/Jarduino/Jarduino-war/src/java/Conf/RegistryConf.xml");
            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("RegistryConf");
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    RegistryConf reg = new RegistryConf();
                    String aux;
                    int idSensor;
                    int value;
                    aux = eElement.getElementsByTagName("idSensor").item(0).getTextContent();
                    idSensor = Integer.parseInt(aux);
                    reg.setIdSensor(idSensor);

                    aux = eElement.getElementsByTagName("saveType").item(0).getTextContent();
                    reg.setSaveTypeString(aux);

                    aux = eElement.getElementsByTagName("value").item(0).getTextContent();
                    value = Integer.parseInt(aux);
                    reg.setValue(value);

                    registryConf.add(reg);
                    lastRead.put(idSensor, 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //El string debe estar en formato json
    public boolean updateRegistry(String dataJson) {
        try {
            //Crea objeto json con string por parametro
            JSONObject json = new JSONObject(dataJson);
            
            //Obtiene el array de Registos
            JSONArray arr = json.getJSONArray("Registry");
            //Recorre el array
            for (int i = 0; i < arr.length(); i++) {
                //Obtiene los datos idSensor y value
                int idSensor = arr.getJSONObject(i).getInt("idSensor");
                
                int value = arr.getJSONObject(i).getInt("value");
                //Recorre la configuracion de registro
                for (RegistryConf reg : registryConf) {

                    //Se fija si el registro corresponde a esta configuracion
                    if (reg.getIdSensor() == idSensor) {

                        //Checkea el criterio para guardar, o no en la BD
                        //Checkea tambien si el valor es igual al anterior
                        if (reg.getSaveTypeString() == "ONCHANGE" && lastRead.get(idSensor) != value) {
                            //Actualizo la ultima lectura y guardo en la BD
                            lastRead.put(idSensor, value);
                            saveRegistry(idSensor, value);

                        } else if (reg.getSaveTypeString() == "ONTIME") {
                            //Variables auxiliares, para checkear tiempo
                            Long auxLong = System.currentTimeMillis()/1000;
                            int now = auxLong.intValue();
                            int timeToSave = lastRead.get(idSensor) + reg.getValue();
                            //Checkea si ya es tiempo para guerdar un nuevo registro
                            if (now >= timeToSave){
                                //Actualizo el ultimo guardado
                                lastRead.put(idSensor, now);
                                saveRegistry(idSensor, value);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //Guarda registro en la BD     
    public boolean saveRegistry(int idSensor, int value) {
        try {
            Registry r = new Registry();
            r.setIdsensor(idSensor);
            r.setValue(value);
            r.setDate(new Date());
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            session.save(r);
            tx.commit();
            session.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
