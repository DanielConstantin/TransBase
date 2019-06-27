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
@Table(name = "contacte")
@NamedQueries({
    @NamedQuery(name = "Contacte.findAll", query = "SELECT c FROM Contacte c")
    , @NamedQuery(name = "Contacte.findByIdPersoanaCont", query = "SELECT c FROM Contacte c WHERE c.idPersoanaCont = :idPersoanaCont")
    , @NamedQuery(name = "Contacte.findByNume", query = "SELECT c FROM Contacte c WHERE c.nume = :nume")
    , @NamedQuery(name = "Contacte.findByCompanie", query = "SELECT c FROM Contacte c WHERE c.companie = :companie")
    , @NamedQuery(name = "Contacte.findByTel", query = "SELECT c FROM Contacte c WHERE c.tel = :tel")
    , @NamedQuery(name = "Contacte.findByFax", query = "SELECT c FROM Contacte c WHERE c.fax = :fax")
    , @NamedQuery(name = "Contacte.findByTermenPl", query = "SELECT c FROM Contacte c WHERE c.termenPl = :termenPl")
    , @NamedQuery(name = "Contacte.findByEmail", query = "SELECT c FROM Contacte c WHERE c.email = :email")})
public class Contacte implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_persoana_cont")
    private Integer idPersoanaCont;
    @Column(name = "nume")
    private String nume;
    @Column(name = "companie")
    private String companie;
    @Column(name = "tel")
    private String tel;
    @Column(name = "fax")
    private String fax;
    @Column(name = "termen pl")
    private Integer termenPl;
    @Column(name = "email")
    private String email;
    @OneToMany(mappedBy = "contacte")
    private List<Inc2> inc2List;

    public Contacte() {
    }

    public Contacte(Integer idPersoanaCont) {
        this.idPersoanaCont = idPersoanaCont;
    }

    public Integer getIdPersoanaCont() {
        return idPersoanaCont;
    }

    public void setIdPersoanaCont(Integer idPersoanaCont) {
        this.idPersoanaCont = idPersoanaCont;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getCompanie() {
        return companie;
    }

    public void setCompanie(String companie) {
        this.companie = companie;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Integer getTermenPl() {
        return termenPl;
    }

    public void setTermenPl(Integer termenPl) {
        this.termenPl = termenPl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        hash += (idPersoanaCont != null ? idPersoanaCont.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Contacte)) {
            return false;
        }
        Contacte other = (Contacte) object;
        if ((this.idPersoanaCont == null && other.idPersoanaCont != null) || (this.idPersoanaCont != null && !this.idPersoanaCont.equals(other.idPersoanaCont))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "transbase.entities.Contacte[ idPersoanaCont=" + idPersoanaCont + " ]";
    }
    
}
