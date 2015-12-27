/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Andres
 */

public class Alert implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Sensor idsensor;
    private int value;
    private Date date;
    private boolean seen;

    public Alert() {
    }

    public Alert(Integer id) {
        this.id = id;
    }

    public Alert(Integer id, Sensor idsensor, int value, Date date, boolean seen) {
        this.id = id;
        this.idsensor = idsensor;
        this.value = value;
        this.date = date;
        this.seen = seen;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Sensor getIdsensor() {
        return this.idsensor;
    }

    public void setIdsensor(Sensor idsensor) {
        this.idsensor = idsensor;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean getSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Alert)) {
            return false;
        }
        Alert other = (Alert) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DAO.Alert[ id=" + id + " ]";
    }
    
}
