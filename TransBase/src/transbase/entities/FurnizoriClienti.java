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
@Table(name = "Furnizori_Clienti")
@NamedQueries({
    @NamedQuery(name = "FurnizoriClienti.findAll", query = "SELECT f FROM FurnizoriClienti f")
    , @NamedQuery(name = "FurnizoriClienti.findByCodScala", query = "SELECT f FROM FurnizoriClienti f WHERE f.codScala = :codScala")
    , @NamedQuery(name = "FurnizoriClienti.findByDenumire", query = "SELECT f FROM FurnizoriClienti f WHERE f.denumire = :denumire")
    , @NamedQuery(name = "FurnizoriClienti.findByPersContact", query = "SELECT f FROM FurnizoriClienti f WHERE f.persContact = :persContact")
    , @NamedQuery(name = "FurnizoriClienti.findByPersContactemail", query = "SELECT f FROM FurnizoriClienti f WHERE f.persContactemail = :persContactemail")
    , @NamedQuery(name = "FurnizoriClienti.findByPersRespemail", query = "SELECT f FROM FurnizoriClienti f WHERE f.persRespemail = :persRespemail")
    , @NamedQuery(name = "FurnizoriClienti.findByTara", query = "SELECT f FROM FurnizoriClienti f WHERE f.tara = :tara")
    , @NamedQuery(name = "FurnizoriClienti.findByTransPredef", query = "SELECT f FROM FurnizoriClienti f WHERE f.transPredef = :transPredef")
    , @NamedQuery(name = "FurnizoriClienti.findByTipPredef", query = "SELECT f FROM FurnizoriClienti f WHERE f.tipPredef = :tipPredef")
    , @NamedQuery(name = "FurnizoriClienti.findByTermenPl", query = "SELECT f FROM FurnizoriClienti f WHERE f.termenPl = :termenPl")
    , @NamedQuery(name = "FurnizoriClienti.findByMoneda", query = "SELECT f FROM FurnizoriClienti f WHERE f.moneda = :moneda")})
public class FurnizoriClienti implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CodScala")
    private String codScala;
    @Column(name = "Denumire")
    private String denumire;
    @Column(name = "pers_contact")
    private String persContact;
    @Column(name = "PersContact_email")
    private String persContactemail;
    @Column(name = "PersResp_email")
    private String persRespemail;
    @Column(name = "Tara")
    private String tara;
    @Column(name = "TransPredef")
    private String transPredef;
    @Column(name = "TipPredef")
    private Integer tipPredef;
    @Column(name = "Termen_Pl")
    private Short termenPl;
    @Column(name = "moneda")
    private String moneda;
    @OneToMany(mappedBy = "codFurnizor")
    private List<ComandaTransp> comandaTranspList;

    public FurnizoriClienti() {
    }

    public FurnizoriClienti(String codScala) {
        this.codScala = codScala;
    }

    public String getCodScala() {
        return codScala;
    }

    public void setCodScala(String codScala) {
        this.codScala = codScala;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public String getPersContact() {
        return persContact;
    }

    public void setPersContact(String persContact) {
        this.persContact = persContact;
    }

    public String getPersContactemail() {
        return persContactemail;
    }

    public void setPersContactemail(String persContactemail) {
        this.persContactemail = persContactemail;
    }

    public String getPersRespemail() {
        return persRespemail;
    }

    public void setPersRespemail(String persRespemail) {
        this.persRespemail = persRespemail;
    }

    public String getTara() {
        return tara;
    }

    public void setTara(String tara) {
        this.tara = tara;
    }

    public String getTransPredef() {
        return transPredef;
    }

    public void setTransPredef(String transPredef) {
        this.transPredef = transPredef;
    }

    public Integer getTipPredef() {
        return tipPredef;
    }

    public void setTipPredef(Integer tipPredef) {
        this.tipPredef = tipPredef;
    }

    public Short getTermenPl() {
        return termenPl;
    }

    public void setTermenPl(Short termenPl) {
        this.termenPl = termenPl;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
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
        hash += (codScala != null ? codScala.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FurnizoriClienti)) {
            return false;
        }
        FurnizoriClienti other = (FurnizoriClienti) object;
        if ((this.codScala == null && other.codScala != null) || (this.codScala != null && !this.codScala.equals(other.codScala))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "transbase.entities.FurnizoriClienti[ codScala=" + codScala + " ]";
    }
    
}
