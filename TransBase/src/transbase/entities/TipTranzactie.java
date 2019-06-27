/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transbase.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author daniel.constantin
 */
@Entity
@Table(name = "TipTranzactie")
@NamedQueries({
    @NamedQuery(name = "TipTranzactie.findAll", query = "SELECT t FROM TipTranzactie t")
    , @NamedQuery(name = "TipTranzactie.findByIdTip", query = "SELECT t FROM TipTranzactie t WHERE t.idTip = :idTip")
    , @NamedQuery(name = "TipTranzactie.findByTip", query = "SELECT t FROM TipTranzactie t WHERE t.tip = :tip")})
public class TipTranzactie implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IdTip")
    private Integer idTip;
    @Column(name = "Tip")
    private String tip;

    public TipTranzactie() {
    }

    public TipTranzactie(Integer idTip) {
        this.idTip = idTip;
    }

    public Integer getIdTip() {
        return idTip;
    }

    public void setIdTip(Integer idTip) {
        this.idTip = idTip;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTip != null ? idTip.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipTranzactie)) {
            return false;
        }
        TipTranzactie other = (TipTranzactie) object;
        if ((this.idTip == null && other.idTip != null) || (this.idTip != null && !this.idTip.equals(other.idTip))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "transbase.entities.TipTranzactie[ idTip=" + idTip + " ]";
    }
    
}
