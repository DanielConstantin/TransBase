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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author daniel.constantin
 */
@Entity
@Table(name = "LISTA INCARCARI")
@NamedQueries({
    @NamedQuery(name = "ListaIncarcari.findAll", query = "SELECT l FROM ListaIncarcari l")
    , @NamedQuery(name = "ListaIncarcari.findByDataI", query = "SELECT l FROM ListaIncarcari l WHERE l.dataI = :dataI")
    , @NamedQuery(name = "ListaIncarcari.findByComanda", query = "SELECT l FROM ListaIncarcari l WHERE l.comanda = :comanda")
    , @NamedQuery(name = "ListaIncarcari.findByReady", query = "SELECT l FROM ListaIncarcari l WHERE l.ready = :ready")
    , @NamedQuery(name = "ListaIncarcari.findByFurnizor", query = "SELECT l FROM ListaIncarcari l WHERE l.furnizor = :furnizor")
    , @NamedQuery(name = "ListaIncarcari.findByDestinatie", query = "SELECT l FROM ListaIncarcari l WHERE l.destinatie = :destinatie")
    , @NamedQuery(name = "ListaIncarcari.findByTransport", query = "SELECT l FROM ListaIncarcari l WHERE l.transport = :transport")
    , @NamedQuery(name = "ListaIncarcari.findByComentarii", query = "SELECT l FROM ListaIncarcari l WHERE l.comentarii = :comentarii")
    , @NamedQuery(name = "ListaIncarcari.findByCamion", query = "SELECT l FROM ListaIncarcari l WHERE l.camion = :camion")
    , @NamedQuery(name = "ListaIncarcari.findByReferinta", query = "SELECT l FROM ListaIncarcari l WHERE l.referinta = :referinta")
    , @NamedQuery(name = "ListaIncarcari.findByNpaleti", query = "SELECT l FROM ListaIncarcari l WHERE l.npaleti = :npaleti")
    , @NamedQuery(name = "ListaIncarcari.findByAletele", query = "SELECT l FROM ListaIncarcari l WHERE l.aletele = :aletele")
    , @NamedQuery(name = "ListaIncarcari.findByFacturaMarfa", query = "SELECT l FROM ListaIncarcari l WHERE l.facturaMarfa = :facturaMarfa")
    , @NamedQuery(name = "ListaIncarcari.findByDataFactMarfa", query = "SELECT l FROM ListaIncarcari l WHERE l.dataFactMarfa = :dataFactMarfa")
    , @NamedQuery(name = "ListaIncarcari.findByFacturaTransport", query = "SELECT l FROM ListaIncarcari l WHERE l.facturaTransport = :facturaTransport")
    , @NamedQuery(name = "ListaIncarcari.findByDataFactTransp", query = "SELECT l FROM ListaIncarcari l WHERE l.dataFactTransp = :dataFactTransp")
    , @NamedQuery(name = "ListaIncarcari.findByDataS", query = "SELECT l FROM ListaIncarcari l WHERE l.dataS = :dataS")
    , @NamedQuery(name = "ListaIncarcari.findByUpdates", query = "SELECT l FROM ListaIncarcari l WHERE l.updates = :updates")
    , @NamedQuery(name = "ListaIncarcari.findByDataIncReal", query = "SELECT l FROM ListaIncarcari l WHERE l.dataIncReal = :dataIncReal")
    , @NamedQuery(name = "ListaIncarcari.findByDataSosBooking", query = "SELECT l FROM ListaIncarcari l WHERE l.dataSosBooking = :dataSosBooking")
    , @NamedQuery(name = "ListaIncarcari.findByDataSosReal", query = "SELECT l FROM ListaIncarcari l WHERE l.dataSosReal = :dataSosReal")
    , @NamedQuery(name = "ListaIncarcari.findByDataDescarcare", query = "SELECT l FROM ListaIncarcari l WHERE l.dataDescarcare = :dataDescarcare")
    , @NamedQuery(name = "ListaIncarcari.findByOfertaTarif", query = "SELECT l FROM ListaIncarcari l WHERE l.ofertaTarif = :ofertaTarif")
    , @NamedQuery(name = "ListaIncarcari.findByTermenpl", query = "SELECT l FROM ListaIncarcari l WHERE l.termenpl = :termenpl")})
public class ListaIncarcari implements Serializable {

    private static final long serialVersionUID = 1L;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataI;
    @Id
    @Basic(optional = false)
    private String comanda;
    private Boolean ready;
    private String furnizor;
    private Integer destinatie;
    private String transport;
    private String comentarii;
    private String camion;
    private String referinta;
    @Column(name = "N_paleti")
    private String npaleti;
    private String aletele;
    @Column(name = "Factura Marfa")
    private Boolean facturaMarfa;
    @Column(name = "data fact marfa")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFactMarfa;
    @Column(name = "factura transport")
    private Boolean facturaTransport;
    @Column(name = "data fact transp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFactTransp;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataS;
    private String updates;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataIncReal;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataSosBooking;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataSosReal;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataDescarcare;
    private Short ofertaTarif;
    @Column(name = "Termen_pl")
    private Short termenpl;
    @JoinColumn(name = "Tip", referencedColumnName = "IdTip")
    @ManyToOne
    private TipTranzactie tip;

    public ListaIncarcari() {
    }

    public ListaIncarcari(String comanda) {
        this.comanda = comanda;
    }

    public Date getDataI() {
        return dataI;
    }

    public void setDataI(Date dataI) {
        this.dataI = dataI;
    }

    public String getComanda() {
        return comanda;
    }

    public void setComanda(String comanda) {
        this.comanda = comanda;
    }

    public Boolean getReady() {
        return ready;
    }

    public void setReady(Boolean ready) {
        this.ready = ready;
    }

    public String getFurnizor() {
        return furnizor;
    }

    public void setFurnizor(String furnizor) {
        this.furnizor = furnizor;
    }

    public Integer getDestinatie() {
        return destinatie;
    }

    public void setDestinatie(Integer destinatie) {
        this.destinatie = destinatie;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getComentarii() {
        return comentarii;
    }

    public void setComentarii(String comentarii) {
        this.comentarii = comentarii;
    }

    public String getCamion() {
        return camion;
    }

    public void setCamion(String camion) {
        this.camion = camion;
    }

    public String getReferinta() {
        return referinta;
    }

    public void setReferinta(String referinta) {
        this.referinta = referinta;
    }

    public String getNpaleti() {
        return npaleti;
    }

    public void setNpaleti(String npaleti) {
        this.npaleti = npaleti;
    }

    public String getAletele() {
        return aletele;
    }

    public void setAletele(String aletele) {
        this.aletele = aletele;
    }

    public Boolean getFacturaMarfa() {
        return facturaMarfa;
    }

    public void setFacturaMarfa(Boolean facturaMarfa) {
        this.facturaMarfa = facturaMarfa;
    }

    public Date getDataFactMarfa() {
        return dataFactMarfa;
    }

    public void setDataFactMarfa(Date dataFactMarfa) {
        this.dataFactMarfa = dataFactMarfa;
    }

    public Boolean getFacturaTransport() {
        return facturaTransport;
    }

    public void setFacturaTransport(Boolean facturaTransport) {
        this.facturaTransport = facturaTransport;
    }

    public Date getDataFactTransp() {
        return dataFactTransp;
    }

    public void setDataFactTransp(Date dataFactTransp) {
        this.dataFactTransp = dataFactTransp;
    }

    public Date getDataS() {
        return dataS;
    }

    public void setDataS(Date dataS) {
        this.dataS = dataS;
    }

    public String getUpdates() {
        return updates;
    }

    public void setUpdates(String updates) {
        this.updates = updates;
    }

    public Date getDataIncReal() {
        return dataIncReal;
    }

    public void setDataIncReal(Date dataIncReal) {
        this.dataIncReal = dataIncReal;
    }

    public Date getDataSosBooking() {
        return dataSosBooking;
    }

    public void setDataSosBooking(Date dataSosBooking) {
        this.dataSosBooking = dataSosBooking;
    }

    public Date getDataSosReal() {
        return dataSosReal;
    }

    public void setDataSosReal(Date dataSosReal) {
        this.dataSosReal = dataSosReal;
    }

    public Date getDataDescarcare() {
        return dataDescarcare;
    }

    public void setDataDescarcare(Date dataDescarcare) {
        this.dataDescarcare = dataDescarcare;
    }

    public Short getOfertaTarif() {
        return ofertaTarif;
    }

    public void setOfertaTarif(Short ofertaTarif) {
        this.ofertaTarif = ofertaTarif;
    }

    public Short getTermenpl() {
        return termenpl;
    }

    public void setTermenpl(Short termenpl) {
        this.termenpl = termenpl;
    }

    public TipTranzactie getTip() {
        return tip;
    }

    public void setTip(TipTranzactie tip) {
        this.tip = tip;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (comanda != null ? comanda.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ListaIncarcari)) {
            return false;
        }
        ListaIncarcari other = (ListaIncarcari) object;
        if ((this.comanda == null && other.comanda != null) || (this.comanda != null && !this.comanda.equals(other.comanda))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "transbase.entities.ListaIncarcari[ comanda=" + comanda + " ]";
    }
    
}
