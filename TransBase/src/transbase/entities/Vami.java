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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author daniel.constantin
 */
@Entity
@Table(name = "vami")
@NamedQueries({
    @NamedQuery(name = "Vami.findAll", query = "SELECT v FROM Vami v")
    , @NamedQuery(name = "Vami.findByCodVama", query = "SELECT v FROM Vami v WHERE v.codVama = :codVama")
    , @NamedQuery(name = "Vami.findByDenumire", query = "SELECT v FROM Vami v WHERE v.denumire = :denumire")
    , @NamedQuery(name = "Vami.findByOrVama", query = "SELECT v FROM Vami v WHERE v.orVama = :orVama")
    , @NamedQuery(name = "Vami.findByAdr", query = "SELECT v FROM Vami v WHERE v.adr = :adr")
    , @NamedQuery(name = "Vami.findByUnloading", query = "SELECT v FROM Vami v WHERE v.unloading = :unloading")
    , @NamedQuery(name = "Vami.findBySorting", query = "SELECT v FROM Vami v WHERE v.sorting = :sorting")})
public class Vami implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "cod_vama")
    private Integer codVama;
    @Column(name = "denumire")
    private String denumire;
    @Column(name = "or_vama")
    private String orVama;
    @Column(name = "adr")
    private String adr;
    @Column(name = "unloading")
    private String unloading;
    @Column(name = "sorting")
    private Integer sorting;
    @JoinColumn(name = "oras", referencedColumnName = "cod_ordest")
    @ManyToOne
    private Orasedest oras;
    @OneToMany(mappedBy = "codVama")
    private List<Inc2> inc2List;

    public Vami() {
    }

    public Vami(Integer codVama) {
        this.codVama = codVama;
    }

    public Integer getCodVama() {
        return codVama;
    }

    public void setCodVama(Integer codVama) {
        this.codVama = codVama;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public String getOrVama() {
        return orVama;
    }

    public void setOrVama(String orVama) {
        this.orVama = orVama;
    }

    public String getAdr() {
        return adr;
    }

    public void setAdr(String adr) {
        this.adr = adr;
    }

    public String getUnloading() {
        return unloading;
    }

    public void setUnloading(String unloading) {
        this.unloading = unloading;
    }

    public Integer getSorting() {
        return sorting;
    }

    public void setSorting(Integer sorting) {
        this.sorting = sorting;
    }

    public Orasedest getOras() {
        return oras;
    }

    public void setOras(Orasedest oras) {
        this.oras = oras;
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
        hash += (codVama != null ? codVama.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Vami)) {
            return false;
        }
        Vami other = (Vami) object;
        if ((this.codVama == null && other.codVama != null) || (this.codVama != null && !this.codVama.equals(other.codVama))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "transbase.entities.Vami[ codVama=" + codVama + " ]";
    }
    
}
