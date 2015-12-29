/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arduino;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.util.Enumeration;
import org.json.*;
import Controller.RegistryController;
import Controller.AlertController;
import javax.swing.text.StyledEditorKit;
import sun.security.jca.GetInstance;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import HibernateConf.HibernateUtil;
import DAO.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Julio
 */
public class arduinoController implements SerialPortEventListener {

    private static arduinoController instance = new arduinoController();
    SerialPort serialPort;
    private BufferedReader input;
    private OutputStream output = null;

    private String portName = "COM3";
    private int timeOut = 2000;
    private int dataRate = 9600;

    private RegistryController regCont = new RegistryController();
    private AlertController alertCont = new AlertController();

    private Map<Integer, Integer> outputStat = new HashMap<Integer, Integer>();
    private boolean isConected = false;

    private arduinoController() {

    }

    public static arduinoController GetInstance() {
        return instance;
    }

    public Integer getOutStatus(int key) {
        if (outputStat.get(key) == null) {
            return 0;
        }
        return outputStat.get(key);
    }

    public void setOutStatus(int key) {
        if (outputStat.get(key) == 1) {
            outputStat.put(key, 0);
        } else {
            outputStat.put(key, 1);
        }
    }

    public boolean isConected() {
        return isConected;
    }

    private void UpdateOutStat(String outStatJson) {
        JSONObject json = new JSONObject(outStatJson);

        //Obtiene el array de Registos
        JSONArray arr = json.getJSONArray("OutputStatus");

        for (int i = 0; i < arr.length(); i++) {
            int id = arr.getJSONObject(i).getInt("idOutput");
            int value = arr.getJSONObject(i).getInt("value");
            outputStat.put(id, value);
        }
    }

    public void setPort(String portName) {
        this.portName = portName;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public void setDataRate(int dataRate) {
        this.dataRate = dataRate;
    }

    public boolean connect() {
        if (!isConected) {
            CommPortIdentifier portId = null;
            Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

            while (portEnum.hasMoreElements()) {
                CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
                if (currPortId.getName().equals(portName)) {
                    portId = currPortId;
                    break;
                }
            }
            if (portId == null) {
                System.out.println("Could not find COM port.");
                return false;
            }
            try {
                // open serial port, and use class name for the appName.
                serialPort = (SerialPort) portId.open(this.getClass().getName(),
                        timeOut);

                // set port parameters
                serialPort.setSerialPortParams(dataRate,
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);

                // open the streams
                input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
                output = serialPort.getOutputStream();

                // add event listeners
                serialPort.addEventListener(this);
                serialPort.notifyOnDataAvailable(true);
            } catch (Exception e) {
                System.err.println(e.toString());
                return false;
            }
            isConected = true;
            return true;
        } else {
            return false;
        }
    }

    public boolean sendData(String data) {
        try {
            output.write(data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public synchronized void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    @Override
    public synchronized void serialEvent(SerialPortEvent oEvent) {

        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                String inputLine = input.readLine();
                System.out.println(inputLine);
                JSONObject json = new JSONObject(inputLine);
                System.out.println(inputLine);
                try {
                    if (json.has("OutputStatus")) {
                        UpdateOutStat(inputLine);
                    } else if (json.has("Registry")) {
                        regCont.updateRegistry(inputLine);
                        alertCont.checkAndSaveAlert(inputLine);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
        // Ignore all the other eventTypes, but you should consider the other ones.
    }
}
