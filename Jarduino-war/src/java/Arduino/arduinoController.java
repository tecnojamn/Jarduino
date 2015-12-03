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
import javax.swing.text.StyledEditorKit;
import sun.security.jca.GetInstance;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import HibernateConf.HibernateUtil;
import DAO.*;
import java.util.Date;

/**
 *
 * @author Julio
 */
public class arduinoController implements SerialPortEventListener {

    private static arduinoController instance = new arduinoController();
    SerialPort serialPort;
    private BufferedReader input;
    private OutputStream output = null;

    private String portName = "COM4";
    private int timeOut = 2000;
    private int dataRate = 9600;

    private String lastRead = "0";

    private boolean isConected = false;

    public String getLastRead() {
        return lastRead;
    }

    private arduinoController() {

    }

    public static arduinoController GetInstance() {
        return instance;
    }

    public boolean isConected() {
        return isConected;
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

    public synchronized void serialEvent(SerialPortEvent oEvent) {

        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                String inputLine = input.readLine();
                lastRead = inputLine;
                System.out.println(inputLine);
                try {
//                    int value = Integer.parseInt(inputLine);
//                    Session session = HibernateUtil.getSessionFactory().openSession();
//                    Transaction tx = session.beginTransaction();
//                    Registry reg = new Registry();
//                    reg.setDate(new Date());
//                    reg.setIdsensor(1);
//                    reg.setValue(value);
//                    session.save(reg);
//                    tx.commit();
//                    session.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
        // Ignore all the other eventTypes, but you should consider the other ones.
    }

    public static void main(String[] args) throws Exception {
        arduinoController main = arduinoController.GetInstance();
        main.connect();
        Thread t = new Thread() {
            public void run() {
                //the following line will keep this app alive for 1000 seconds,
                //waiting for events to occur and responding to them (printing incoming messages to console).
                try {
                    Thread.sleep(1000000);
                } catch (InterruptedException ie) {
                }
            }
        };
        t.start();
        System.out.println("Started");
    }
}
