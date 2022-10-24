package ro.uti.ran.core.model.registru;

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
@NamedQuery(name = "Registru.findAll", query = "SELECT r FROM Registru r")
public class Registru implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "REGISTRU_IDREGISTRU_GENERATOR", sequenceName = "SEQ_REGISTRU", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REGISTRU_IDREGISTRU_GENERATOR")
    @Column(name = "ID_REGISTRU")
    private Long idRegistru;

    @Column(name = "\"AN\"")
    private Integer an;

    @Column(name = "SEMESTRU")
    private Integer semestru;

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
    private Integer totalProcesare = 0;

    @Lob
    private byte[] recipisa;

    //bi-directional many-to-one association to FluxRegistru
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registru")
    private List<FluxRegistru> fluxRegistrus = new ArrayList<FluxRegistru>();

    //bi-directional many-to-one association to Gospodarie
    @ManyToOne
    @JoinColumn(name = "FK_GOSPODARIE")
    private Gospodarie gospodarie;

    //bi-directional many-to-one association to NomCapitol
    @ManyToOne
    @JoinColumn(name = "FK_NOM_CAPITOL")
    private NomCapitol nomCapitol;

    //bi-directional many-to-one association to NomIndicativXml
    @ManyToOne
    @JoinColumn(name = "FK_NOM_INDICATIV_XML")
    private NomIndicativXml nomIndicativXml;

    //bi-directional many-to-one association to NomStareRegistru
    @ManyToOne
    @JoinColumn(name = "FK_NOM_STARE_REGISTRU")
    private NomStareRegistru nomStareRegistru;

    //bi-directional many-to-one association to NomSursaRegistru
    @ManyToOne
    @JoinColumn(name = "FK_NOM_SURSA_REGISTRU")
    private NomSursaRegistru nomSursaRegistru;

    //bi-directional many-to-one association to NomUat
    @ManyToOne
    @JoinColumn(name = "FK_NOM_UAT")
    private NomUat nomUat;

    public Registru() {
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

    public List<FluxRegistru> getFluxRegistrus() {
        return this.fluxRegistrus;
    }

    public void setFluxRegistrus(List<FluxRegistru> fluxRegistrus) {
        this.fluxRegistrus = fluxRegistrus;
    }

    public FluxRegistru addFluxRegistrus(FluxRegistru fluxRegistrus) {
        getFluxRegistrus().add(fluxRegistrus);
        fluxRegistrus.setRegistru(this);

        return fluxRegistrus;
    }

    public FluxRegistru removeFluxRegistrus(FluxRegistru fluxRegistrus) {
        getFluxRegistrus().remove(fluxRegistrus);
        fluxRegistrus.setRegistru(null);

        return fluxRegistrus;
    }

    public Gospodarie getGospodarie() {
        return this.gospodarie;
    }

    public void setGospodarie(Gospodarie gospodarie) {
        this.gospodarie = gospodarie;
    }

    public NomCapitol getNomCapitol() {
        return this.nomCapitol;
    }

    public void setNomCapitol(NomCapitol nomCapitol) {
        this.nomCapitol = nomCapitol;
    }

    public NomIndicativXml getNomIndicativXml() {
        return this.nomIndicativXml;
    }

    public void setNomIndicativXml(NomIndicativXml nomIndicativXml) {
        this.nomIndicativXml = nomIndicativXml;
    }

    public NomStareRegistru getNomStareRegistru() {
        return this.nomStareRegistru;
    }

    public void setNomStareRegistru(NomStareRegistru nomStareRegistru) {
        this.nomStareRegistru = nomStareRegistru;
    }

    public NomSursaRegistru getNomSursaRegistru() {
        return this.nomSursaRegistru;
    }

    public void setNomSursaRegistru(NomSursaRegistru nomSursaRegistru) {
        this.nomSursaRegistru = nomSursaRegistru;
    }

    public NomUat getNomUat() {
        return this.nomUat;
    }

    public void setNomUat(NomUat nomUat) {
        this.nomUat = nomUat;
    }

    public Integer getTotalProcesare() {
        return totalProcesare;
    }

    public void setTotalProcesare(Integer totalProcesare) {
        this.totalProcesare = totalProcesare;
    }

    public Integer getSemestru() {
        return semestru;
    }

    public void setSemestru(Integer semestru) {
        this.semestru = semestru;
    }
}