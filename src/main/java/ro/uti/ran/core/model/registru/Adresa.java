package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the ADRESA database table.
 */
@Entity
@NamedQuery(name = "Adresa.findAll", query = "SELECT a FROM Adresa a")
public class Adresa implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "ADRESA_IDADRESA_GENERATOR", sequenceName = "SEQ_ADRESA", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADRESA_IDADRESA_GENERATOR")
    @Column(name = "ID_ADRESA")
    private Long idAdresa;

    private String apartament;

    @Column(name = "BASE_ID")
    private Long baseId;

    private String bloc;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_START")
    private Date dataStart;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_STOP")
    private Date dataStop;

    private String etaj;

    @Column(name = "EXCEPTIE_ADRESA")
    private String exceptieAdresa;

    @Column(name = "NR_STRADA")
    private String nrStrada;

    private String scara;

    private String strada;

    @Column(name = "UID_RENNS")
    private String uidRenns;

    @Column(name = "RENNS_MODIFIED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date rennsModifiedDate;

    //bi-directional many-to-one association to NomJudet
    @ManyToOne
    @JoinColumn(name = "FK_NOM_JUDET")
    private NomJudet nomJudet;

    //bi-directional many-to-one association to NomLocalitate
    @ManyToOne
    @JoinColumn(name = "FK_NOM_LOCALITATE")
    private NomLocalitate nomLocalitate;

    //bi-directional many-to-one association to NomTara
    @ManyToOne
    @JoinColumn(name = "FK_NOM_TARA")
    private NomTara nomTara;

    //bi-directional many-to-one association to NomUat
    @ManyToOne
    @JoinColumn(name = "FK_NOM_UAT")
    private NomUat nomUat;

    //bi-directional many-to-one association to AdresaGospodarie
    @OneToMany(mappedBy = "adresa", cascade = CascadeType.REMOVE)
    private List<AdresaGospodarie> adresaGospodaries = new ArrayList<>();

    //bi-directional many-to-one association to Cladire
    @OneToMany(mappedBy = "adresa", cascade = CascadeType.REMOVE)
    private List<Cladire> cladires = new ArrayList<>();

    @OneToOne(mappedBy = "adresa", cascade = CascadeType.REMOVE)
    private GeolocatorAdresa geolocatorAdresa;

    //bi-directional many-to-one association to Succesibil
    @OneToMany(mappedBy = "adresa", cascade = CascadeType.ALL)
    private List<Succesibil> succesibils = new ArrayList<>();


    @Transient
    private String geometrieGML;


    public Adresa() {
    }

    public Long getIdAdresa() {
        return this.idAdresa;
    }

    public void setIdAdresa(Long idAdresa) {
        this.idAdresa = idAdresa;
    }

    public String getApartament() {
        return this.apartament;
    }

    public void setApartament(String apartament) {
        this.apartament = apartament;
    }

    public Long getBaseId() {
        return this.baseId;
    }

    public void setBaseId(Long baseId) {
        this.baseId = baseId;
    }

    public String getBloc() {
        return this.bloc;
    }

    public void setBloc(String bloc) {
        this.bloc = bloc;
    }

    public Date getDataStart() {
        return this.dataStart;
    }

    public void setDataStart(Date dataStart) {
        this.dataStart = dataStart;
    }

    public Date getDataStop() {
        return this.dataStop;
    }

    public void setDataStop(Date dataStop) {
        this.dataStop = dataStop;
    }

    public String getEtaj() {
        return etaj;
    }

    public void setEtaj(String etaj) {
        this.etaj = etaj;
    }

    public String getExceptieAdresa() {
        return this.exceptieAdresa;
    }

    public void setExceptieAdresa(String exceptieAdresa) {
        this.exceptieAdresa = exceptieAdresa;
    }

    public String getNrStrada() {
        return this.nrStrada;
    }

    public void setNrStrada(String nrStrada) {
        this.nrStrada = nrStrada;
    }

    public String getScara() {
        return this.scara;
    }

    public void setScara(String scara) {
        this.scara = scara;
    }

    public String getStrada() {
        return this.strada;
    }

    public void setStrada(String strada) {
        this.strada = strada;
    }

    public String getUidRenns() {
        return this.uidRenns;
    }

    public void setUidRenns(String uidRenns) {
        this.uidRenns = uidRenns;
    }

    public NomJudet getNomJudet() {
        return this.nomJudet;
    }

    public void setNomJudet(NomJudet nomJudet) {
        this.nomJudet = nomJudet;
    }

    public NomLocalitate getNomLocalitate() {
        return this.nomLocalitate;
    }

    public void setNomLocalitate(NomLocalitate nomLocalitate) {
        this.nomLocalitate = nomLocalitate;
    }

    public NomTara getNomTara() {
        return this.nomTara;
    }

    public void setNomTara(NomTara nomTara) {
        this.nomTara = nomTara;
    }

    public NomUat getNomUat() {
        return this.nomUat;
    }

    public void setNomUat(NomUat nomUat) {
        this.nomUat = nomUat;
    }

    public List<AdresaGospodarie> getAdresaGospodaries() {
        return this.adresaGospodaries;
    }

    public void setAdresaGospodaries(List<AdresaGospodarie> adresaGospodaries) {
        this.adresaGospodaries = adresaGospodaries;
    }

    public AdresaGospodarie addAdresaGospodary(AdresaGospodarie adresaGospodary) {
        getAdresaGospodaries().add(adresaGospodary);
        adresaGospodary.setAdresa(this);

        return adresaGospodary;
    }

    public AdresaGospodarie removeAdresaGospodary(AdresaGospodarie adresaGospodary) {
        getAdresaGospodaries().remove(adresaGospodary);
        adresaGospodary.setAdresa(null);

        return adresaGospodary;
    }

    public List<Cladire> getCladires() {
        return this.cladires;
    }

    public void setCladires(List<Cladire> cladires) {
        this.cladires = cladires;
    }

    public Cladire addCladire(Cladire cladire) {
        getCladires().add(cladire);
        cladire.setAdresa(this);

        return cladire;
    }

    public Cladire removeCladire(Cladire cladire) {
        getCladires().remove(cladire);
        cladire.setAdresa(null);

        return cladire;
    }

    public GeolocatorAdresa getGeolocatorAdresa() {
        return geolocatorAdresa;
    }

    public void setGeolocatorAdresa(GeolocatorAdresa geolocatorAdresa) {
        this.geolocatorAdresa = geolocatorAdresa;
    }

    public List<Succesibil> getSuccesibils() {
        return this.succesibils;
    }

    public void setSuccesibils(List<Succesibil> succesibils) {
        this.succesibils = succesibils;
    }

    public Succesibil addSuccesibil(Succesibil succesibil) {
        getSuccesibils().add(succesibil);
        succesibil.setAdresa(this);

        return succesibil;
    }

    public Succesibil removeSuccesibil(Succesibil succesibil) {
        getSuccesibils().remove(succesibil);
        succesibil.setAdresa(null);

        return succesibil;
    }

    public String getGeometrieGML() {
        return geometrieGML;
    }

    public void setGeometrieGML(String geometrieGML) {
        this.geometrieGML = geometrieGML;
    }

    public Date getRennsModifiedDate() {
        return rennsModifiedDate;
    }

    public void setRennsModifiedDate(Date rennsModifiedDate) {
        this.rennsModifiedDate = rennsModifiedDate;
    }
}