package ro.uti.ran.core.model.portal;

import ro.uti.ran.core.model.registru.*;
import ro.uti.ran.core.ws.model.transmitere.ModalitateTransmitere;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the REGISTRU database table.
 */
@Entity
@NamedQuery(name = "RegistruCore.findAll", query = "SELECT r FROM RegistruCore r")
@Table(name = "REGISTRU_CORE")
public class RegistruCore implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "REGISTRU_CORE_IDREGISTRU_GENERATOR", sequenceName = "SEQ_REGISTRU", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REGISTRU_IDREGISTRU_GENERATOR")
    @Column(name = "ID_REGISTRU")
    private Long idRegistru;

    @Column(name = "\"AN\"")
    private Integer an;

    @Lob
    private String continut;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_EXPORT")
    private Date dataExport;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_REGISTRU")
    private Date dataRegistru;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;

    @Column(name = "IDENTIFICATOR_GOSPODARIE")
    private String identificatorGospodarie;

    @Column(name = "INDEX_REGISTRU")
    private String indexRegistru;

    @Column(name = "STARE_CORELARE")
    private Integer stareCorelare = 1;

    @Column(name = "IS_RECIPISA_SEMNATA")
    private Boolean isRecipisaSemnata = false;

    @Column(name = "MODALITATE_TRANSMITERE")
    private Integer modalitateTransmitere;

    @Column(name = "TOTAL_PROCESARE")
    private Integer totalProcesare;

    @Lob
    private byte[] recipisa;

    //bi-directional many-to-one association to FluxRegistru
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registru")
    private List<FluxRegistruCore> fluxRegistrus = new ArrayList<FluxRegistruCore>();


    public RegistruCore() {
    }

    public Long getIdRegistru() {
        return this.idRegistru;
    }

    public void setIdRegistru(Long idRegistru) {
        this.idRegistru = idRegistru;
    }

    public Integer getAn() {
        return this.an;
    }

    public void setAn(Integer an) {
        this.an = an;
    }

    public String getContinut() {
        return this.continut;
    }

    public void setContinut(String continut) {
        this.continut = continut;
    }

    public Date getDataExport() {
        return this.dataExport;
    }

    public void setDataExport(Date dataExport) {
        this.dataExport = dataExport;
    }

    public Date getDataRegistru() {
        return this.dataRegistru;
    }

    public void setDataRegistru(Date dataRegistru) {
        this.dataRegistru = dataRegistru;
    }

    public Long getFkNomJudet() {
        return this.fkNomJudet;
    }

    public void setFkNomJudet(Long fkNomJudet) {
        this.fkNomJudet = fkNomJudet;
    }

    public String getIdentificatorGospodarie() {
        return this.identificatorGospodarie;
    }

    public void setIdentificatorGospodarie(String identificatorGospodarie) {
        this.identificatorGospodarie = identificatorGospodarie;
    }

    public String getIndexRegistru() {
        return this.indexRegistru;
    }

    public void setIndexRegistru(String indexRegistru) {
        this.indexRegistru = indexRegistru;
    }

    public Integer getStareCorelare() {
        return stareCorelare;
    }

    public void setStareCorelare(Integer stareCorelare) {
        this.stareCorelare = stareCorelare;
    }

    public Boolean getIsRecipisaSemnata() {
        return this.isRecipisaSemnata;
    }

    public void setIsRecipisaSemnata(Boolean isRecipisaSemnata) {
        this.isRecipisaSemnata = isRecipisaSemnata;
    }

    public ModalitateTransmitere getModalitateTransmitere() {
        return ModalitateTransmitere.parse(modalitateTransmitere);
    }

    public void setModalitateTransmitere(ModalitateTransmitere modalitateTransmitere) {
        this.modalitateTransmitere = modalitateTransmitere.getValue();
    }

    public byte[] getRecipisa() {
        return this.recipisa;
    }

    public void setRecipisa(byte[] recipisa) {
        this.recipisa = recipisa;
    }

    public List<FluxRegistruCore> getFluxRegistrus() {
        return this.fluxRegistrus;
    }

    public void setFluxRegistrus(List<FluxRegistruCore> fluxRegistrus) {
        this.fluxRegistrus = fluxRegistrus;
    }

    public FluxRegistruCore addFluxRegistrus(FluxRegistruCore fluxRegistrus) {
        getFluxRegistrus().add(fluxRegistrus);
        fluxRegistrus.setRegistru(this);

        return fluxRegistrus;
    }

    public FluxRegistruCore removeFluxRegistrus(FluxRegistruCore fluxRegistrus) {
        getFluxRegistrus().remove(fluxRegistrus);
        fluxRegistrus.setRegistru(null);

        return fluxRegistrus;
    }

    public Integer getTotalProcesare() {
        return totalProcesare;
    }

    public void setTotalProcesare(Integer totalProcesare) {
        this.totalProcesare = totalProcesare;
    }
}