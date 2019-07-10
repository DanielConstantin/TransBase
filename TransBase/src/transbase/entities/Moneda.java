/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transbase.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author daniel.constantin
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Moneda.findAll", query = "SELECT m FROM Moneda m")
    , @NamedQuery(name = "Moneda.findByIdMoneda", query = "SELECT m FROM Moneda m WHERE m.idMoneda = :idMoneda")
    , @NamedQuery(name = "Moneda.findByMoneda", query = "SELECT m FROM Moneda m WHERE m.moneda = :moneda")})
public class Moneda implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_Moneda")
    private String idMoneda;
    @Basic(optional = false)
    private String moneda;
    @OneToMany(mappedBy = "moneda")
    private List<FurnizoriClienti> furnizoriClientiList;

    public Moneda() {
    }

    public Moneda(String idMoneda) {
        this.idMoneda = idMoneda;
    }

    public Moneda(String idMoneda, String moneda) {
        this.idMoneda = idMoneda;
        this.moneda = moneda;
    }

    public String getIdMoneda() {
        return idMoneda;
    }

    public void setIdMoneda(String idMoneda) {
        this.idMoneda = idMoneda;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public List<FurnizoriClienti> getFurnizoriClientiList() {
        return furnizoriClientiList;
    }

    public void setFurnizoriClientiList(List<FurnizoriClienti> furnizoriClientiList) {
        this.furnizoriClientiList = furnizoriClientiList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMoneda != null ? idMoneda.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Moneda)) {
            return false;
        }
        Moneda other = (Moneda) object;
        if ((this.idMoneda == null && other.idMoneda != null) || (this.idMoneda != null && !this.idMoneda.equals(other.idMoneda))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return idMoneda;
    }
    
}
