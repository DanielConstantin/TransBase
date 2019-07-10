/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transbase.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
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
@Table(name = "tip_op")
@NamedQueries({
    @NamedQuery(name = "TipOp.findAll", query = "SELECT t FROM TipOp t")
    , @NamedQuery(name = "TipOp.findById", query = "SELECT t FROM TipOp t WHERE t.id = :id")
    , @NamedQuery(name = "TipOp.findByTip", query = "SELECT t FROM TipOp t WHERE t.tip = :tip")})
public class TipOp implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    private Integer id;
    private String tip;
    @OneToMany(mappedBy = "tipOp")
    private List<Loadingpl> loadingplList;

    public TipOp() {
    }

    public TipOp(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public List<Loadingpl> getLoadingplList() {
        return loadingplList;
    }

    public void setLoadingplList(List<Loadingpl> loadingplList) {
        this.loadingplList = loadingplList;
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
        if (!(object instanceof TipOp)) {
            return false;
        }
        TipOp other = (TipOp) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "transbase.entities.TipOp[ id=" + id + " ]";
    }
    
}
