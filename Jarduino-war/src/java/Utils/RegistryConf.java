/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

/**
 *
 * @author Julio
 */
public class RegistryConf {

    public enum SaveType {
        ONTIME,
        ONCHANGE
    }
    private int idSensor;
    private SaveType saveType;
    private int value;

    public RegistryConf() {
    }

    public int getIdSensor() {
        return idSensor;
    }

    public void setIdSensor(int idSensor) {
        this.idSensor = idSensor;
    }

    public SaveType getSaveType() {
        return saveType;
    }

    public String getSaveTypeString() {
        return saveType.toString();
    }

    public void setSaveType(SaveType saveType) {
        this.saveType = saveType;
    }

    public void setSaveTypeString(String saveType) {
        if (saveType.equals("ONTIME")) {
            this.saveType = SaveType.ONTIME;
        } else if (saveType.equals("ONCHANGE")) {
            this.saveType = SaveType.ONCHANGE;
        }
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
