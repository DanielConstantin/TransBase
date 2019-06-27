/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transbase.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "comanda transp")
@NamedQueries({
    @NamedQuery(name = "ComandaTransp.findAll", query = "SELECT c FROM ComandaTransp c")
    , @NamedQuery(name = "ComandaTransp.findByCod", query = "SELECT c FROM ComandaTransp c WHERE c.cod = :cod")
    , @NamedQuery(name = "ComandaTransp.findByDenFurnizor0", query = "SELECT c FROM ComandaTransp c WHERE c.denFurnizor0 = :denFurnizor0")
    , @NamedQuery(name = "ComandaTransp.findByDenFurnizor", query = "SELECT c FROM ComandaTransp c WHERE c.denFurnizor = :denFurnizor")
    , @NamedQuery(name = "ComandaTransp.findByDepozit", query = "SELECT c FROM ComandaTransp c WHERE c.depozit = :depozit")
    , @NamedQuery(name = "ComandaTransp.findByStr", query = "SELECT c FROM ComandaTransp c WHERE c.str = :str")
    , @NamedQuery(name = "ComandaTransp.findByCodOras", query = "SELECT c FROM ComandaTransp c WHERE c.codOras = :codOras")
    , @NamedQuery(name = "ComandaTransp.findByOras", query = "SELECT c FROM ComandaTransp c WHERE c.oras = :oras")
    , @NamedQuery(name = "ComandaTransp.findByPaletiSchimb", query = "SELECT c FROM ComandaTransp c WHERE c.paletiSchimb = :paletiSchimb")
    , @NamedQuery(name = "ComandaTransp.findByP1", query = "SELECT c FROM ComandaTransp c WHERE c.p1 = :p1")
    , @NamedQuery(name = "ComandaTransp.findByP2", query = "SELECT c FROM ComandaTransp c WHERE c.p2 = :p2")
    , @NamedQuery(name = "ComandaTransp.findByP3", query = "SELECT c FROM ComandaTransp c WHERE c.p3 = :p3")
    , @NamedQuery(name = "ComandaTransp.findByP4", query = "SELECT c FROM ComandaTransp c WHERE c.p4 = :p4")
    , @NamedQuery(name = "ComandaTransp.findByP5", query = "SELECT c FROM ComandaTransp c WHERE c.p5 = :p5")})
public class ComandaTransp implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "cod")
    private Long cod;
    @Column(name = "den_furnizor0")
    private String denFurnizor0;
    @Column(name = "den_furnizor")
    private String denFurnizor;
    @Column(name = "depozit")
    private String depozit;
    @Column(name = "str")
    private String str;
    @Column(name = "cod_oras")
    private String codOras;
    @Column(name = "oras")
    private String oras;
    @Column(name = "paletiSchimb")
    private String paletiSchimb;
    @Column(name = "P1")
    private String p1;
    @Column(name = "P2")
    private String p2;
    @Column(name = "P3")
    private String p3;
    @Column(name = "P4")
    private String p4;
    @Column(name = "P5")
    private String p5;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lodpl")
    private List<Loadingpl> loadingplList;
    @JoinColumn(name = "CodFurnizor", referencedColumnName = "CodScala")
    @ManyToOne
    private FurnizoriClienti codFurnizor;
    @JoinColumn(name = "tara", referencedColumnName = "Tara2L")
    @ManyToOne
    private TariISO tara;

    public ComandaTransp() {
    }

    public ComandaTransp(Long cod) {
        this.cod = cod;
    }

    public Long getCod() {
        return cod;
    }

    public void setCod(Long cod) {
        this.cod = cod;
    }

    public String getDenFurnizor0() {
        return denFurnizor0;
    }

    public void setDenFurnizor0(String denFurnizor0) {
        this.denFurnizor0 = denFurnizor0;
    }

    public String getDenFurnizor() {
        return denFurnizor;
    }

    public void setDenFurnizor(String denFurnizor) {
        this.denFurnizor = denFurnizor;
    }

    public String getDepozit() {
        return depozit;
    }

    public void setDepozit(String depozit) {
        this.depozit = depozit;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public String getCodOras() {
        return codOras;
    }

    public void setCodOras(String codOras) {
        this.codOras = codOras;
    }

    public String getOras() {
        return oras;
    }

    public void setOras(String oras) {
        this.oras = oras;
    }

    public String getPaletiSchimb() {
        return paletiSchimb;
    }

    public void setPaletiSchimb(String paletiSchimb) {
        this.paletiSchimb = paletiSchimb;
    }

    public String getP1() {
        return p1;
    }

    public void setP1(String p1) {
        this.p1 = p1;
    }

    public String getP2() {
        return p2;
    }

    public void setP2(String p2) {
        this.p2 = p2;
    }

    public String getP3() {
        return p3;
    }

    public void setP3(String p3) {
        this.p3 = p3;
    }

    public String getP4() {
        return p4;
    }

    public void setP4(String p4) {
        this.p4 = p4;
    }

    public String getP5() {
        return p5;
    }

    public void setP5(String p5) {
        this.p5 = p5;
    }

    public List<Loadingpl> getLoadingplList() {
        return loadingplList;
    }

    public void setLoadingplList(List<Loadingpl> loadingplList) {
        this.loadingplList = loadingplList;
    }

    public FurnizoriClienti getCodFurnizor() {
        return codFurnizor;
    }

    public void setCodFurnizor(FurnizoriClienti codFurnizor) {
        this.codFurnizor = codFurnizor;
    }

    public TariISO getTara() {
        return tara;
    }

    public void setTara(TariISO tara) {
        this.tara = tara;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cod != null ? cod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComandaTransp)) {
            return false;
        }
        ComandaTransp other = (ComandaTransp) object;
        if ((this.cod == null && other.cod != null) || (this.cod != null && !this.cod.equals(other.cod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "transbase.entities.ComandaTransp[ cod=" + cod + " ]";
    }
    
}
