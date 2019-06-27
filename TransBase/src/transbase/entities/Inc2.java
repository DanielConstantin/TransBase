/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transbase.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author daniel.constantin
 */
@Entity
@Table(name = "INC2")
@NamedQueries({
    @NamedQuery(name = "Inc2.findAll", query = "SELECT i FROM Inc2 i")
    , @NamedQuery(name = "Inc2.findById", query = "SELECT i FROM Inc2 i WHERE i.id = :id")
    , @NamedQuery(name = "Inc2.findByCod", query = "SELECT i FROM Inc2 i WHERE i.cod = :cod")
    , @NamedQuery(name = "Inc2.findByPctFrontiera", query = "SELECT i FROM Inc2 i WHERE i.pctFrontiera = :pctFrontiera")
    , @NamedQuery(name = "Inc2.findByTarif", query = "SELECT i FROM Inc2 i WHERE i.tarif = :tarif")
    , @NamedQuery(name = "Inc2.findByTARIFext", query = "SELECT i FROM Inc2 i WHERE i.tARIFext = :tARIFext")
    , @NamedQuery(name = "Inc2.findByTarifIntern", query = "SELECT i FROM Inc2 i WHERE i.tarifIntern = :tarifIntern")
    , @NamedQuery(name = "Inc2.findByMoneda", query = "SELECT i FROM Inc2 i WHERE i.moneda = :moneda")
    , @NamedQuery(name = "Inc2.findByTermenPl", query = "SELECT i FROM Inc2 i WHERE i.termenPl = :termenPl")
    , @NamedQuery(name = "Inc2.findByTransportator", query = "SELECT i FROM Inc2 i WHERE i.transportator = :transportator")
    , @NamedQuery(name = "Inc2.findByObservatii", query = "SELECT i FROM Inc2 i WHERE i.observatii = :observatii")
    , @NamedQuery(name = "Inc2.findByGbruto", query = "SELECT i FROM Inc2 i WHERE i.gbruto = :gbruto")
    , @NamedQuery(name = "Inc2.findByDataSOSIRElimita", query = "SELECT i FROM Inc2 i WHERE i.dataSOSIRElimita = :dataSOSIRElimita")
    , @NamedQuery(name = "Inc2.findByDataSosireReala", query = "SELECT i FROM Inc2 i WHERE i.dataSosireReala = :dataSosireReala")
    , @NamedQuery(name = "Inc2.findByDataDescarcare", query = "SELECT i FROM Inc2 i WHERE i.dataDescarcare = :dataDescarcare")
    , @NamedQuery(name = "Inc2.findByCcurMarfa", query = "SELECT i FROM Inc2 i WHERE i.ccurMarfa = :ccurMarfa")
    , @NamedQuery(name = "Inc2.findByHandling", query = "SELECT i FROM Inc2 i WHERE i.handling = :handling")
    , @NamedQuery(name = "Inc2.findByVamaExport", query = "SELECT i FROM Inc2 i WHERE i.vamaExport = :vamaExport")
    , @NamedQuery(name = "Inc2.findByTaxevet", query = "SELECT i FROM Inc2 i WHERE i.taxevet = :taxevet")
    , @NamedQuery(name = "Inc2.findByValtaxev", query = "SELECT i FROM Inc2 i WHERE i.valtaxev = :valtaxev")
    , @NamedQuery(name = "Inc2.findByMonedataxev", query = "SELECT i FROM Inc2 i WHERE i.monedataxev = :monedataxev")
    , @NamedQuery(name = "Inc2.findByTarifReferinta", query = "SELECT i FROM Inc2 i WHERE i.tarifReferinta = :tarifReferinta")
    , @NamedQuery(name = "Inc2.findByCmdSc", query = "SELECT i FROM Inc2 i WHERE i.cmdSc = :cmdSc")
    , @NamedQuery(name = "Inc2.findByLplCam", query = "SELECT i FROM Inc2 i WHERE i.lplCam = :lplCam")
    , @NamedQuery(name = "Inc2.findByLplTara", query = "SELECT i FROM Inc2 i WHERE i.lplTara = :lplTara")})
public class Inc2 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "cod")
    private Integer cod;
    @Column(name = "PCT FRONTIERA")
    private String pctFrontiera;
    @Column(name = "TARIF")
    private Integer tarif;
    @Column(name = "TARIF ext")
    private Integer tARIFext;
    @Column(name = "tarif intern")
    private Integer tarifIntern;
    @Column(name = "MONEDA")
    private String moneda;
    @Column(name = "TERMEN PL")
    private Integer termenPl;
    @Column(name = "transportator")
    private String transportator;
    @Column(name = "observatii")
    private String observatii;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Gbruto")
    private Double gbruto;
    @Column(name = "data_SOSIRE_limita")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataSOSIRElimita;
    @Column(name = "data_sosire_reala")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataSosireReala;
    @Column(name = "data_descarcare")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataDescarcare;
    @Column(name = "CCUR_MARFA")
    private String ccurMarfa;
    @Column(name = "handling")
    private String handling;
    @Column(name = "vama_export")
    private String vamaExport;
    @Column(name = "taxevet")
    private Boolean taxevet;
    @Column(name = "valtaxev")
    private Double valtaxev;
    @Column(name = "monedataxev")
    private String monedataxev;
    @Column(name = "tarif referinta")
    private Double tarifReferinta;
    @Column(name = "cmd_sc")
    private String cmdSc;
    @Column(name = "lpl_cam")
    private String lplCam;
    @Column(name = "lpl_tara")
    private String lplTara;
    @OneToMany(mappedBy = "cod")
    private List<Loadingpl> loadingplList;
    @JoinColumn(name = "consignee", referencedColumnName = "idcons")
    @ManyToOne
    private Consignees consignee;
    @JoinColumn(name = "contacte", referencedColumnName = "id_persoana_cont")
    @ManyToOne
    private Contacte contacte;
    @JoinColumn(name = "tip_transp", referencedColumnName = "Tip_transport")
    @ManyToOne
    private TipTransport tipTransp;
    @JoinColumn(name = "cod_vama", referencedColumnName = "cod_vama")
    @ManyToOne
    private Vami codVama;
    @OneToMany(mappedBy = "idcoms")
    private List<CmdS> cmdSList;

    public Inc2() {
    }

    public Inc2(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public String getPctFrontiera() {
        return pctFrontiera;
    }

    public void setPctFrontiera(String pctFrontiera) {
        this.pctFrontiera = pctFrontiera;
    }

    public Integer getTarif() {
        return tarif;
    }

    public void setTarif(Integer tarif) {
        this.tarif = tarif;
    }

    public Integer getTARIFext() {
        return tARIFext;
    }

    public void setTARIFext(Integer tARIFext) {
        this.tARIFext = tARIFext;
    }

    public Integer getTarifIntern() {
        return tarifIntern;
    }

    public void setTarifIntern(Integer tarifIntern) {
        this.tarifIntern = tarifIntern;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public Integer getTermenPl() {
        return termenPl;
    }

    public void setTermenPl(Integer termenPl) {
        this.termenPl = termenPl;
    }

    public String getTransportator() {
        return transportator;
    }

    public void setTransportator(String transportator) {
        this.transportator = transportator;
    }

    public String getObservatii() {
        return observatii;
    }

    public void setObservatii(String observatii) {
        this.observatii = observatii;
    }

    public Double getGbruto() {
        return gbruto;
    }

    public void setGbruto(Double gbruto) {
        this.gbruto = gbruto;
    }

    public Date getDataSOSIRElimita() {
        return dataSOSIRElimita;
    }

    public void setDataSOSIRElimita(Date dataSOSIRElimita) {
        this.dataSOSIRElimita = dataSOSIRElimita;
    }

    public Date getDataSosireReala() {
        return dataSosireReala;
    }

    public void setDataSosireReala(Date dataSosireReala) {
        this.dataSosireReala = dataSosireReala;
    }

    public Date getDataDescarcare() {
        return dataDescarcare;
    }

    public void setDataDescarcare(Date dataDescarcare) {
        this.dataDescarcare = dataDescarcare;
    }

    public String getCcurMarfa() {
        return ccurMarfa;
    }

    public void setCcurMarfa(String ccurMarfa) {
        this.ccurMarfa = ccurMarfa;
    }

    public String getHandling() {
        return handling;
    }

    public void setHandling(String handling) {
        this.handling = handling;
    }

    public String getVamaExport() {
        return vamaExport;
    }

    public void setVamaExport(String vamaExport) {
        this.vamaExport = vamaExport;
    }

    public Boolean getTaxevet() {
        return taxevet;
    }

    public void setTaxevet(Boolean taxevet) {
        this.taxevet = taxevet;
    }

    public Double getValtaxev() {
        return valtaxev;
    }

    public void setValtaxev(Double valtaxev) {
        this.valtaxev = valtaxev;
    }

    public String getMonedataxev() {
        return monedataxev;
    }

    public void setMonedataxev(String monedataxev) {
        this.monedataxev = monedataxev;
    }

    public Double getTarifReferinta() {
        return tarifReferinta;
    }

    public void setTarifReferinta(Double tarifReferinta) {
        this.tarifReferinta = tarifReferinta;
    }

    public String getCmdSc() {
        return cmdSc;
    }

    public void setCmdSc(String cmdSc) {
        this.cmdSc = cmdSc;
    }

    public String getLplCam() {
        return lplCam;
    }

    public void setLplCam(String lplCam) {
        this.lplCam = lplCam;
    }

    public String getLplTara() {
        return lplTara;
    }

    public void setLplTara(String lplTara) {
        this.lplTara = lplTara;
    }

    public List<Loadingpl> getLoadingplList() {
        return loadingplList;
    }

    public void setLoadingplList(List<Loadingpl> loadingplList) {
        this.loadingplList = loadingplList;
    }

    public Consignees getConsignee() {
        return consignee;
    }

    public void setConsignee(Consignees consignee) {
        this.consignee = consignee;
    }

    public Contacte getContacte() {
        return contacte;
    }

    public void setContacte(Contacte contacte) {
        this.contacte = contacte;
    }

    public TipTransport getTipTransp() {
        return tipTransp;
    }

    public void setTipTransp(TipTransport tipTransp) {
        this.tipTransp = tipTransp;
    }

    public Vami getCodVama() {
        return codVama;
    }

    public void setCodVama(Vami codVama) {
        this.codVama = codVama;
    }

    public List<CmdS> getCmdSList() {
        return cmdSList;
    }

    public void setCmdSList(List<CmdS> cmdSList) {
        this.cmdSList = cmdSList;
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
        if (!(object instanceof Inc2)) {
            return false;
        }
        Inc2 other = (Inc2) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "transbase.entities.Inc2[ id=" + id + " ]";
    }
    
}
