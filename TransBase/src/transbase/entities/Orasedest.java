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
@Table(name = "orasedest")
@NamedQueries({
    @NamedQuery(name = "Orasedest.findAll", query = "SELECT o FROM Orasedest o")
    , @NamedQuery(name = "Orasedest.findByCodOrdest", query = "SELECT o FROM Orasedest o WHERE o.codOrdest = :codOrdest")
    , @NamedQuery(name = "Orasedest.findByOras", query = "SELECT o FROM Orasedest o WHERE o.oras = :oras")
    , @NamedQuery(name = "Orasedest.findByTara", query = "SELECT o FROM Orasedest o WHERE o.tara = :tara")
    , @NamedQuery(name = "Orasedest.findByODestTara", query = "SELECT o FROM Orasedest o WHERE o.oDestTara = :oDestTara")})
public class Orasedest implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "cod_ordest")
    private Integer codOrdest;
    @Column(name = "oras")
    private String oras;
    @Column(name = "Tara")
    private String tara;
    @Column(name = "o_dest_tara")
    private String oDestTara;
    @OneToMany(mappedBy = "oras")
    private List<Vami> vamiList;

    public Orasedest() {
    }

    public Orasedest(Integer codOrdest) {
        this.codOrdest = codOrdest;
    }

    public Integer getCodOrdest() {
        return codOrdest;
    }

    public void setCodOrdest(Integer codOrdest) {
        this.codOrdest = codOrdest;
    }

    public String getOras() {
        return oras;
    }

    public void setOras(String oras) {
        this.oras = oras;
    }

    public String getTara() {
        return tara;
    }

    public void setTara(String tara) {
        this.tara = tara;
    }

    public String getODestTara() {
        return oDestTara;
    }

    public void setODestTara(String oDestTara) {
        this.oDestTara = oDestTara;
    }

    public List<Vami> getVamiList() {
        return vamiList;
    }

    public void setVamiList(List<Vami> vamiList) {
        this.vamiList = vamiList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codOrdest != null ? codOrdest.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Orasedest)) {
            return false;
        }
        Orasedest other = (Orasedest) object;
        if ((this.codOrdest == null && other.codOrdest != null) || (this.codOrdest != null && !this.codOrdest.equals(other.codOrdest))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "transbase.entities.Orasedest[ codOrdest=" + codOrdest + " ]";
    }
    
}
