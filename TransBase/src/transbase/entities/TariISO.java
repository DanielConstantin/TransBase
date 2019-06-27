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
@Table(name = "Tari_ISO")
@NamedQueries({
    @NamedQuery(name = "TariISO.findAll", query = "SELECT t FROM TariISO t")
    , @NamedQuery(name = "TariISO.findByTara2L", query = "SELECT t FROM TariISO t WHERE t.tara2L = :tara2L")
    , @NamedQuery(name = "TariISO.findByTara3L", query = "SELECT t FROM TariISO t WHERE t.tara3L = :tara3L")
    , @NamedQuery(name = "TariISO.findByTaraDen", query = "SELECT t FROM TariISO t WHERE t.taraDen = :taraDen")
    , @NamedQuery(name = "TariISO.findByTaraNum", query = "SELECT t FROM TariISO t WHERE t.taraNum = :taraNum")})
public class TariISO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Tara2L")
    private String tara2L;
    @Column(name = "Tara3L")
    private String tara3L;
    @Column(name = "TaraDen")
    private String taraDen;
    @Column(name = "TaraNum")
    private Integer taraNum;
    @OneToMany(mappedBy = "tara")
    private List<ComandaTransp> comandaTranspList;

    public TariISO() {
    }

    public TariISO(String tara2L) {
        this.tara2L = tara2L;
    }

    public String getTara2L() {
        return tara2L;
    }

    public void setTara2L(String tara2L) {
        this.tara2L = tara2L;
    }

    public String getTara3L() {
        return tara3L;
    }

    public void setTara3L(String tara3L) {
        this.tara3L = tara3L;
    }

    public String getTaraDen() {
        return taraDen;
    }

    public void setTaraDen(String taraDen) {
        this.taraDen = taraDen;
    }

    public Integer getTaraNum() {
        return taraNum;
    }

    public void setTaraNum(Integer taraNum) {
        this.taraNum = taraNum;
    }

    public List<ComandaTransp> getComandaTranspList() {
        return comandaTranspList;
    }

    public void setComandaTranspList(List<ComandaTransp> comandaTranspList) {
        this.comandaTranspList = comandaTranspList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tara2L != null ? tara2L.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TariISO)) {
            return false;
        }
        TariISO other = (TariISO) object;
        if ((this.tara2L == null && other.tara2L != null) || (this.tara2L != null && !this.tara2L.equals(other.tara2L))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return tara2L;
    }
    
}
