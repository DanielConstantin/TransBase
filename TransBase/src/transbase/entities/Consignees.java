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
import javax.persistence.Table;

/**
 *
 * @author daniel.constantin
 */
@Entity
@Table(name = "consignees")
@NamedQueries({
    @NamedQuery(name = "Consignees.findAll", query = "SELECT c FROM Consignees c")
    , @NamedQuery(name = "Consignees.findByIdcons", query = "SELECT c FROM Consignees c WHERE c.idcons = :idcons")
    , @NamedQuery(name = "Consignees.findByConsigneename", query = "SELECT c FROM Consignees c WHERE c.consigneename = :consigneename")
    , @NamedQuery(name = "Consignees.findByConsigneeaddress", query = "SELECT c FROM Consignees c WHERE c.consigneeaddress = :consigneeaddress")
    , @NamedQuery(name = "Consignees.findByConsigneecui", query = "SELECT c FROM Consignees c WHERE c.consigneecui = :consigneecui")
    , @NamedQuery(name = "Consignees.findByCodPostal", query = "SELECT c FROM Consignees c WHERE c.codPostal = :codPostal")
    , @NamedQuery(name = "Consignees.findByTara", query = "SELECT c FROM Consignees c WHERE c.tara = :tara")
    , @NamedQuery(name = "Consignees.findByCodISOtara", query = "SELECT c FROM Consignees c WHERE c.codISOtara = :codISOtara")})
public class Consignees implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idcons")
    private Integer idcons;
    @Column(name = "consigneename")
    private String consigneename;
    @Column(name = "consigneeaddress")
    private String consigneeaddress;
    @Column(name = "consigneecui")
    private String consigneecui;
    @Column(name = "cod_postal")
    private String codPostal;
    @Column(name = "tara")
    private String tara;
    @Column(name = "codISO_tara")
    private String codISOtara;
    @OneToMany(mappedBy = "consignee")
    private List<Inc2> inc2List;

    public Consignees() {
    }

    public Consignees(Integer idcons) {
        this.idcons = idcons;
    }

    public Integer getIdcons() {
        return idcons;
    }

    public void setIdcons(Integer idcons) {
        this.idcons = idcons;
    }

    public String getConsigneename() {
        return consigneename;
    }

    public void setConsigneename(String consigneename) {
        this.consigneename = consigneename;
    }

    public String getConsigneeaddress() {
        return consigneeaddress;
    }

    public void setConsigneeaddress(String consigneeaddress) {
        this.consigneeaddress = consigneeaddress;
    }

    public String getConsigneecui() {
        return consigneecui;
    }

    public void setConsigneecui(String consigneecui) {
        this.consigneecui = consigneecui;
    }

    public String getCodPostal() {
        return codPostal;
    }

    public void setCodPostal(String codPostal) {
        this.codPostal = codPostal;
    }

    public String getTara() {
        return tara;
    }

    public void setTara(String tara) {
        this.tara = tara;
    }

    public String getCodISOtara() {
        return codISOtara;
    }

    public void setCodISOtara(String codISOtara) {
        this.codISOtara = codISOtara;
    }

    public List<Inc2> getInc2List() {
        return inc2List;
    }

    public void setInc2List(List<Inc2> inc2List) {
        this.inc2List = inc2List;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idcons != null ? idcons.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Consignees)) {
            return false;
        }
        Consignees other = (Consignees) object;
        if ((this.idcons == null && other.idcons != null) || (this.idcons != null && !this.idcons.equals(other.idcons))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "transbase.entities.Consignees[ idcons=" + idcons + " ]";
    }
    
}
