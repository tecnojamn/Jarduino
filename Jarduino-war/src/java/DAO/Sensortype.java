package DAO;
// Generated 02-dic-2015 22:22:59 by Hibernate Tools 4.3.1


/**
 * Sensortype generated by hbm2java
 */

public class Sensortype implements java.io.Serializable {


    private int id;
    private String name;

    public Sensortype() {
    }

    public Sensortype(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
