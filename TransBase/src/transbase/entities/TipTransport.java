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
@Table(name = "Tip_Transport")
@NamedQueries({
    @NamedQuery(name = "TipTransport.findAll", query = "SELECT t FROM TipTransport t")
    , @NamedQuery(name = "TipTransport.findByTiptransport", query = "SELECT t FROM TipTransport t WHERE t.tiptransport = :tiptransport")
    , @NamedQuery(name = "TipTransport.findByDescriere", query = "SELECT t FROM TipTransport t WHERE t.descriere = :descriere")})
public class TipTransport implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Tip_transport")
    private String tiptransport;
    @Column(name = "Descriere")
    private String descriere;
    @OneToMany(mappedBy = "tipTransp")
    private List<Inc2> inc2List;

    public TipTransport() {
    }

    public TipTransport(String tiptransport) {
        this.tiptransport = tiptransport;
    }

    public String getTiptransport() {
        return tiptransport;
    }

    public void setTiptransport(String tiptransport) {
        this.tiptransport = tiptransport;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
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
        hash += (tiptransport != null ? tiptransport.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipTransport)) {
            return false;
        }
        TipTransport other = (TipTransport) object;
        if ((this.tiptransport == null && other.tiptransport != null) || (this.tiptransport != null && !this.tiptransport.equals(other.tiptransport))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "transbase.entities.TipTransport[ tiptransport=" + tiptransport + " ]";
    }
    
}
