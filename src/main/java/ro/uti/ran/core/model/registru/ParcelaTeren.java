package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the PARCELA_TEREN database table.
 */
@Entity
@Table(name = "PARCELA_TEREN")
@NamedQuery(name = "ParcelaTeren.findAll", query = "SELECT p FROM ParcelaTeren p")
public class ParcelaTeren implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "PARCELA_TEREN_IDPARCELATEREN_GENERATOR", sequenceName = "SEQ_PARCELA_TEREN", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PARCELA_TEREN_IDPARCELATEREN_GENERATOR")
    @Column(name = "ID_PARCELA_TEREN")
    private Long idParcelaTeren;

    @Column(name = "\"AN\"")
    private Integer an;

    @Column(name = "COD_RAND")
    private Integer codRand;

    private String denumire;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;

    @Column(name = "INTRAVILAN_EXTRAVILAN")
    private Integer intravilanExtravilan;


    private String mentiune;

    @Column(name = "NR_BLOC_FIZIC")
    private String nrBlocFizic;

    private Integer suprafata;

    //bi-directional many-to-one association to ActDetinere
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parcelaTeren", fetch = FetchType.EAGER)
    private List<ActDetinere> actDetineres = new ArrayList<ActDetinere>();

    //bi-directional one-to-one association to GeometrieParcelaTeren
    @OneToOne(mappedBy = "parcelaTeren",cascade = CascadeType.REMOVE)
    GeometrieParcelaTeren geometrieParcelaTeren;

    //bi-directional many-to-one association to ParcelaLocalizare
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parcelaTeren", fetch = FetchType.EAGER)
    private List<ParcelaLocalizare> parcelaLocalizares = new ArrayList<ParcelaLocalizare>();

    //bi-directional many-to-one association to Act
    @ManyToOne
    @JoinColumn(name = "FK_ACT_INSTRAINARE")
    private Act actInstrainare;

    //bi-directional many-to-one association to CapCategorieFolosinta
    @ManyToOne
    @JoinColumn(name = "FK_CAP_CATEGORIE_FOLOSINTA")
    private CapCategorieFolosinta capCategorieFolosinta;

    //bi-directional many-to-one association to Gospodarie
    @ManyToOne
    @JoinColumn(name = "FK_GOSPODARIE")
    private Gospodarie gospodarie;

    //bi-directional many-to-one association to NomModalitateDetinere
    @ManyToOne
    @JoinColumn(name = "FK_NOM_MODALITATE_DETINERE")
    private NomModalitateDetinere nomModalitateDetinere;

    //bi-directional many-to-one association to ProprietarParcela
    @OneToMany(mappedBy = "parcelaTeren", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ProprietarParcela> proprietarParcelas = new ArrayList<>();

    public ParcelaTeren() {
    }

    public Long getIdParcelaTeren() {
        return this.idParcelaTeren;
    }

    public void setIdParcelaTeren(Long idParcelaTeren) {
        this.idParcelaTeren = idParcelaTeren;
    }

    public Integer getAn() {
        return this.an;
    }

    public void setAn(Integer an) {
        this.an = an;
    }

    public Integer getCodRand() {
        return this.codRand;
    }

    public void setCodRand(Integer codRand) {
        this.codRand = codRand;
    }

    public String getDenumire() {
        return this.denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public Long getFkNomJudet() {
        return this.fkNomJudet;
    }

    public void setFkNomJudet(Long fkNomJudet) {
        this.fkNomJudet = fkNomJudet;
    }

    public Integer getIntravilanExtravilan() {
        return this.intravilanExtravilan;
    }

    public void setIntravilanExtravilan(Integer intravilanExtravilan) {
        this.intravilanExtravilan = intravilanExtravilan;
    }


    public String getMentiune() {
        return this.mentiune;
    }

    public void setMentiune(String mentiune) {
        this.mentiune = mentiune;
    }

    public String getNrBlocFizic() {
        return this.nrBlocFizic;
    }

    public void setNrBlocFizic(String nrBlocFizic) {
        this.nrBlocFizic = nrBlocFizic;
    }

    public Integer getSuprafata() {
        return this.suprafata;
    }

    public void setSuprafata(Integer suprafata) {
        this.suprafata = suprafata;
    }

    public List<ActDetinere> getActDetineres() {
        return this.actDetineres;
    }

    public void setActDetineres(List<ActDetinere> actDetineres) {
        this.actDetineres = actDetineres;
    }

    public ActDetinere addActDetinere(ActDetinere actDetinere) {
        getActDetineres().add(actDetinere);
        actDetinere.setParcelaTeren(this);

        return actDetinere;
    }

    public ActDetinere removeActDetinere(ActDetinere actDetinere) {
        getActDetineres().remove(actDetinere);
        actDetinere.setParcelaTeren(null);

        return actDetinere;
    }


    public GeometrieParcelaTeren getGeometrieParcelaTeren() {
        return geometrieParcelaTeren;
    }

    public void setGeometrieParcelaTeren(GeometrieParcelaTeren geometrieParcelaTeren) {
        this.geometrieParcelaTeren = geometrieParcelaTeren;
    }

    public List<ParcelaLocalizare> getParcelaLocalizares() {
        return this.parcelaLocalizares;
    }

    public void setParcelaLocalizares(List<ParcelaLocalizare> parcelaLocalizares) {
        this.parcelaLocalizares = parcelaLocalizares;
    }

    public ParcelaLocalizare addParcelaLocalizare(ParcelaLocalizare parcelaLocalizare) {
        getParcelaLocalizares().add(parcelaLocalizare);
        parcelaLocalizare.setParcelaTeren(this);

        return parcelaLocalizare;
    }

    public ParcelaLocalizare removeParcelaLocalizare(ParcelaLocalizare parcelaLocalizare) {
        getParcelaLocalizares().remove(parcelaLocalizare);
        parcelaLocalizare.setParcelaTeren(null);

        return parcelaLocalizare;
    }

    public Act getActInstrainare() {
        return actInstrainare;
    }

    public void setActInstrainare(Act actInstrainare) {
        this.actInstrainare = actInstrainare;
    }

    public CapCategorieFolosinta getCapCategorieFolosinta() {
        return this.capCategorieFolosinta;
    }

    public void setCapCategorieFolosinta(CapCategorieFolosinta capCategorieFolosinta) {
        this.capCategorieFolosinta = capCategorieFolosinta;
    }

    public Gospodarie getGospodarie() {
        return this.gospodarie;
    }

    public void setGospodarie(Gospodarie gospodarie) {
        this.gospodarie = gospodarie;
    }

    public NomModalitateDetinere getNomModalitateDetinere() {
        return this.nomModalitateDetinere;
    }

    public void setNomModalitateDetinere(NomModalitateDetinere nomModalitateDetinere) {
        this.nomModalitateDetinere = nomModalitateDetinere;
    }

    public List<ProprietarParcela> getProprietarParcelas() {
        return this.proprietarParcelas;
    }

    public void setProprietarParcelas(List<ProprietarParcela> proprietarParcelas) {
        this.proprietarParcelas = proprietarParcelas;
    }

    public ProprietarParcela addProprietarParcela(ProprietarParcela proprietarParcela) {
        getProprietarParcelas().add(proprietarParcela);
        proprietarParcela.setParcelaTeren(this);

        return proprietarParcela;
    }

    public ProprietarParcela removeProprietarParcela(ProprietarParcela proprietarParcela) {
        getProprietarParcelas().remove(proprietarParcela);
        proprietarParcela.setParcelaTeren(null);

        return proprietarParcela;
    }

}