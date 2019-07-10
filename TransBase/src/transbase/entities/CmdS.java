/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transbase.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author daniel.constantin
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "CmdS.findAll", query = "SELECT c FROM CmdS c")
    , @NamedQuery(name = "CmdS.findByComanda", query = "SELECT c FROM CmdS c WHERE c.comanda = :comanda")
    , @NamedQuery(name = "CmdS.findByLpl", query = "SELECT c FROM CmdS c WHERE c.lpl = :lpl")})
public class CmdS implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    private String comanda;
    private String lpl;
    @JoinColumn(name = "idcoms", referencedColumnName = "id")
    @ManyToOne
    private Inc2 idcoms;

    public CmdS() {
    }

    public CmdS(String comanda) {
        this.comanda = comanda;
    }

    public String getComanda() {
        return comanda;
    }

    public void setComanda(String comanda) {
        this.comanda = comanda;
    }

    public String getLpl() {
        return lpl;
    }

    public void setLpl(String lpl) {
        this.lpl = lpl;
    }

    public Inc2 getIdcoms() {
        return idcoms;
    }

    public void setIdcoms(Inc2 idcoms) {
        this.idcoms = idcoms;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (comanda != null ? comanda.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CmdS)) {
            return false;
        }
        CmdS other = (CmdS) object;
        if ((this.comanda == null && other.comanda != null) || (this.comanda != null && !this.comanda.equals(other.comanda))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "transbase.entities.CmdS[ comanda=" + comanda + " ]";
    }
    
}
