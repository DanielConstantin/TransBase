/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transbase.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author daniel.constantin
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Loadingpl.findAll", query = "SELECT l FROM Loadingpl l")
    , @NamedQuery(name = "Loadingpl.findByIdLp", query = "SELECT l FROM Loadingpl l WHERE l.idLp = :idLp")
    , @NamedQuery(name = "Loadingpl.findByDataInc", query = "SELECT l FROM Loadingpl l WHERE l.dataInc = :dataInc")
    , @NamedQuery(name = "Loadingpl.findByGoods", query = "SELECT l FROM Loadingpl l WHERE l.goods = :goods")
    , @NamedQuery(name = "Loadingpl.findByTemp", query = "SELECT l FROM Loadingpl l WHERE l.temp = :temp")
    , @NamedQuery(name = "Loadingpl.findByNrpaleti", query = "SELECT l FROM Loadingpl l WHERE l.nrpaleti = :nrpaleti")
    , @NamedQuery(name = "Loadingpl.findByOrdine", query = "SELECT l FROM Loadingpl l WHERE l.ordine = :ordine")
    , @NamedQuery(name = "Loadingpl.findByReferinta", query = "SELECT l FROM Loadingpl l WHERE l.referinta = :referinta")
    , @NamedQuery(name = "Loadingpl.findByOra", query = "SELECT l FROM Loadingpl l WHERE l.ora = :ora")})
public class Loadingpl implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_lp")
    private Long idLp;
    @Column(name = "DATA_INC")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInc;
    private String goods;
    private String temp;
    private String nrpaleti;
    private Integer ordine;
    private String referinta;
    @Temporal(TemporalType.TIMESTAMP)
    private Date ora;
    @JoinColumn(name = "lodpl", referencedColumnName = "cod")
    @ManyToOne(optional = false)
    private ComandaTransp lodpl;
    @JoinColumn(name = "cod", referencedColumnName = "id")
    @ManyToOne
    private Inc2 cod;
    @JoinColumn(name = "tip_op", referencedColumnName = "ID")
    @ManyToOne
    private TipOp tipOp;

    public Loadingpl() {
    }

    public Loadingpl(Long idLp) {
        this.idLp = idLp;
    }

    public Long getIdLp() {
        return idLp;
    }

    public void setIdLp(Long idLp) {
        this.idLp = idLp;
    }

    public Date getDataInc() {
        return dataInc;
    }

    public void setDataInc(Date dataInc) {
        this.dataInc = dataInc;
    }

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getNrpaleti() {
        return nrpaleti;
    }

    public void setNrpaleti(String nrpaleti) {
        this.nrpaleti = nrpaleti;
    }

    public Integer getOrdine() {
        return ordine;
    }

    public void setOrdine(Integer ordine) {
        this.ordine = ordine;
    }

    public String getReferinta() {
        return referinta;
    }

    public void setReferinta(String referinta) {
        this.referinta = referinta;
    }

    public Date getOra() {
        return ora;
    }

    public void setOra(Date ora) {
        this.ora = ora;
    }

    public ComandaTransp getLodpl() {
        return lodpl;
    }

    public void setLodpl(ComandaTransp lodpl) {
        this.lodpl = lodpl;
    }

    public Inc2 getCod() {
        return cod;
    }

    public void setCod(Inc2 cod) {
        this.cod = cod;
    }

    public TipOp getTipOp() {
        return tipOp;
    }

    public void setTipOp(TipOp tipOp) {
        this.tipOp = tipOp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLp != null ? idLp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Loadingpl)) {
            return false;
        }
        Loadingpl other = (Loadingpl) object;
        if ((this.idLp == null && other.idLp != null) || (this.idLp != null && !this.idLp.equals(other.idLp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "transbase.entities.Loadingpl[ idLp=" + idLp + " ]";
    }
    
}
