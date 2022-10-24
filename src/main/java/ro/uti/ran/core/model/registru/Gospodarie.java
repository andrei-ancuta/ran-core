package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the GOSPODARIE database table.
 */
@Entity
@NamedQuery(name = "Gospodarie.findAll", query = "SELECT g FROM Gospodarie g")
public class Gospodarie implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "GOSPODARIE_IDGOSPODARIE_GENERATOR", sequenceName = "SEQ_GOSPODARIE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GOSPODARIE_IDGOSPODARIE_GENERATOR")
    @Column(name = "ID_GOSPODARIE")
    private Long idGospodarie;

    @Column(name = "COD_EXPLOATATIE")
    private String codExploatatie;

    @Column(name = "IDENT_POZITIE_ANTERIOARA")
    private Integer identPozitieAnterioara;

    @Column(name = "IDENT_POZITIE_CURENTA")
    private Integer identPozitieCurenta;

    @Column(name = "IDENT_ROL_NOMINAL_UNIC")
    private Integer identRolNominalUnic;

    @Column(name = "IDENT_VOLUM")
    private String identVolum;

    private String identificator;

    @Column(name = "INSERT_DATE")
    private Timestamp insertDate;

    @Column(name = "IS_ACTIV")
    private Integer isActiv;

    @Column(name = "NR_UNIC_IDENTIFICARE")
    private String nrUnicIdentificare;

    //bi-directional many-to-one association to AdresaGospodarie
    @OneToMany(mappedBy = "gospodarie", cascade = CascadeType.REMOVE)
    private List<AdresaGospodarie> adresaGospodaries = new ArrayList<>();

    //bi-directional many-to-one association to AplicareIngrasamant
    @OneToMany(mappedBy = "gospodarie", cascade = CascadeType.REMOVE)
    private List<AplicareIngrasamant> aplicareIngrasamants = new ArrayList<>();

    //bi-directional many-to-one association to Atestat
    @OneToMany(mappedBy = "gospodarie", cascade = CascadeType.REMOVE)
    private List<Atestat> atestats = new ArrayList<>();

    //bi-directional many-to-one association to CategorieAnimal
    @OneToMany(mappedBy = "gospodarie", cascade = CascadeType.REMOVE)
    private List<CategorieAnimal> categorieAnimals = new ArrayList<>();

    //bi-directional many-to-one association to Cladire
    @OneToMany(mappedBy = "gospodarie", cascade = CascadeType.REMOVE)
    private List<Cladire> cladires = new ArrayList<>();

    //bi-directional many-to-one association to Contract
    @OneToMany(mappedBy = "gospodarie", cascade = CascadeType.REMOVE)
    private List<Contract> contracts = new ArrayList<>();

    //bi-directional many-to-one association to Cultura
    @OneToMany(mappedBy = "gospodarie", cascade = CascadeType.REMOVE)
    private List<Cultura> culturas = new ArrayList<>();

    //bi-directional many-to-one association to DetinatorPf
    @OneToMany(mappedBy = "gospodarie", cascade = CascadeType.REMOVE)
    private List<DetinatorPf> detinatorPfs = new ArrayList<>();

    //bi-directional many-to-one association to DetinatorPj
    @OneToMany(mappedBy = "gospodarie", cascade = CascadeType.REMOVE)
    private List<DetinatorPj> detinatorPjs = new ArrayList<>();

    //bi-directional many-to-one association to NomJudet
    @ManyToOne
    @JoinColumn(name = "FK_NOM_JUDET")
    private NomJudet nomJudet;

    //bi-directional many-to-one association to NomLocalitate
    @ManyToOne
    @JoinColumn(name = "FK_NOM_LOCALITATE")
    private NomLocalitate nomLocalitate;

    //bi-directional many-to-one association to NomTipDetinator
    @ManyToOne
    @JoinColumn(name = "FK_NOM_TIP_DETINATOR")
    private NomTipDetinator nomTipDetinator;

    //bi-directional many-to-one association to NomTipExploatatie
    @ManyToOne
    @JoinColumn(name = "FK_NOM_TIP_EXPLOATATIE")
    private NomTipExploatatie nomTipExploatatie;

    //bi-directional many-to-one association to NomUat
    @ManyToOne
    @JoinColumn(name = "FK_NOM_UAT")
    private NomUat nomUat;

    //bi-directional many-to-one association to MentiuneCerereSuc
    @OneToMany(mappedBy = "gospodarie", cascade = CascadeType.REMOVE)
    private List<MentiuneCerereSuc> mentiuneCerereSucs = new ArrayList<MentiuneCerereSuc>();

    //bi-directional many-to-one association to MentiuneSpeciala
    @OneToMany(mappedBy = "gospodarie", cascade = CascadeType.REMOVE)
    private List<MentiuneSpeciala> mentiuneSpecialas = new ArrayList<MentiuneSpeciala>();

    //bi-directional many-to-one association to ParcelaTeren
    @OneToMany(mappedBy = "gospodarie", cascade = CascadeType.REMOVE)
    private List<ParcelaTeren> parcelaTerens = new ArrayList<ParcelaTeren>();

    //bi-directional many-to-one association to Plantatie
    @OneToMany(mappedBy = "gospodarie", cascade = CascadeType.REMOVE)
    private List<Plantatie> plantaties = new ArrayList<>();

    //bi-directional many-to-one association to PomRazlet
    @OneToMany(mappedBy = "gospodarie", cascade = CascadeType.REMOVE)
    private List<PomRazlet> pomRazlets = new ArrayList<PomRazlet>();

    //bi-directional many-to-one association to Preemptiune
    @OneToMany(mappedBy = "gospodarie", cascade = CascadeType.REMOVE)
    private List<Preemptiune> preemptiunes = new ArrayList<Preemptiune>();

    //bi-directional many-to-one association to Registru
    @OneToMany(mappedBy = "gospodarie", cascade = CascadeType.REMOVE)
    private List<Registru> registrus = new ArrayList<>();

    //bi-directional many-to-one association to SistemTehnic
    @OneToMany(mappedBy = "gospodarie", cascade = CascadeType.REMOVE)
    private List<SistemTehnic> sistemTehnics = new ArrayList<>();

    //bi-directional many-to-one association to SubstantaChimica
    @OneToMany(mappedBy = "gospodarie", cascade = CascadeType.REMOVE)
    private List<SubstantaChimica> substantaChimicas = new ArrayList<SubstantaChimica>();

    //bi-directional many-to-one association to SuprafataCategorie
    @OneToMany(mappedBy = "gospodarie", cascade = CascadeType.REMOVE)
    private List<SuprafataCategorie> suprafataCategories = new ArrayList<SuprafataCategorie>();

    //bi-directional many-to-one association to SuprafataUtilizare
    @OneToMany(mappedBy = "gospodarie", cascade = CascadeType.REMOVE)
    private List<SuprafataUtilizare> suprafataUtilizares = new ArrayList<SuprafataUtilizare>();

    //bi-directional many-to-one association to TerenIrigat
    @OneToMany(mappedBy = "gospodarie", cascade = CascadeType.REMOVE)
    private List<TerenIrigat> terenIrigats = new ArrayList<>();

    public Gospodarie() {
    }

    public Long getIdGospodarie() {
        return this.idGospodarie;
    }

    public void setIdGospodarie(Long idGospodarie) {
        this.idGospodarie = idGospodarie;
    }

    public String getCodExploatatie() {
        return this.codExploatatie;
    }

    public void setCodExploatatie(String codExploatatie) {
        this.codExploatatie = codExploatatie;
    }

    public Integer getIdentPozitieAnterioara() {
        return this.identPozitieAnterioara;
    }

    public void setIdentPozitieAnterioara(Integer identPozitieAnterioara) {
        this.identPozitieAnterioara = identPozitieAnterioara;
    }

    public Integer getIdentPozitieCurenta() {
        return this.identPozitieCurenta;
    }

    public void setIdentPozitieCurenta(Integer identPozitieCurenta) {
        this.identPozitieCurenta = identPozitieCurenta;
    }

    public Integer getIdentRolNominalUnic() {
        return this.identRolNominalUnic;
    }

    public void setIdentRolNominalUnic(Integer identRolNominalUnic) {
        this.identRolNominalUnic = identRolNominalUnic;
    }

    public String getIdentVolum() {
        return this.identVolum;
    }

    public void setIdentVolum(String identVolum) {
        this.identVolum = identVolum;
    }

    public String getIdentificator() {
        return this.identificator;
    }

    public void setIdentificator(String identificator) {
        this.identificator = identificator;
    }

    public Timestamp getInsertDate() {
        return this.insertDate;
    }

    public void setInsertDate(Timestamp insertDate) {
        this.insertDate = insertDate;
    }

    public Integer getIsActiv() {
        return this.isActiv;
    }

    public void setIsActiv(Integer isActiv) {
        this.isActiv = isActiv;
    }


    public String getNrUnicIdentificare() {
        return this.nrUnicIdentificare;
    }

    public void setNrUnicIdentificare(String nrUnicIdentificare) {
        this.nrUnicIdentificare = nrUnicIdentificare;
    }

    public List<AdresaGospodarie> getAdresaGospodaries() {
        return this.adresaGospodaries;
    }

    public void setAdresaGospodaries(List<AdresaGospodarie> adresaGospodaries) {
        this.adresaGospodaries = adresaGospodaries;
    }

    public AdresaGospodarie addAdresaGospodary(AdresaGospodarie adresaGospodary) {
        getAdresaGospodaries().add(adresaGospodary);
        adresaGospodary.setGospodarie(this);

        return adresaGospodary;
    }

    public AdresaGospodarie removeAdresaGospodary(AdresaGospodarie adresaGospodary) {
        getAdresaGospodaries().remove(adresaGospodary);
        adresaGospodary.setGospodarie(null);

        return adresaGospodary;
    }

    public List<AplicareIngrasamant> getAplicareIngrasamants() {
        return this.aplicareIngrasamants;
    }

    public void setAplicareIngrasamants(List<AplicareIngrasamant> aplicareIngrasamants) {
        this.aplicareIngrasamants = aplicareIngrasamants;
    }

    public AplicareIngrasamant addAplicareIngrasamant(AplicareIngrasamant aplicareIngrasamant) {
        getAplicareIngrasamants().add(aplicareIngrasamant);
        aplicareIngrasamant.setGospodarie(this);

        return aplicareIngrasamant;
    }

    public AplicareIngrasamant removeAplicareIngrasamant(AplicareIngrasamant aplicareIngrasamant) {
        getAplicareIngrasamants().remove(aplicareIngrasamant);
        aplicareIngrasamant.setGospodarie(null);

        return aplicareIngrasamant;
    }

    public List<Atestat> getAtestats() {
        return this.atestats;
    }

    public void setAtestats(List<Atestat> atestats) {
        this.atestats = atestats;
    }

    public Atestat addAtestat(Atestat atestat) {
        getAtestats().add(atestat);
        atestat.setGospodarie(this);

        return atestat;
    }

    public Atestat removeAtestat(Atestat atestat) {
        getAtestats().remove(atestat);
        atestat.setGospodarie(null);

        return atestat;
    }

    public List<CategorieAnimal> getCategorieAnimals() {
        return this.categorieAnimals;
    }

    public void setCategorieAnimals(List<CategorieAnimal> categorieAnimals) {
        this.categorieAnimals = categorieAnimals;
    }

    public CategorieAnimal addCategorieAnimal(CategorieAnimal categorieAnimal) {
        getCategorieAnimals().add(categorieAnimal);
        categorieAnimal.setGospodarie(this);

        return categorieAnimal;
    }

    public CategorieAnimal removeCategorieAnimal(CategorieAnimal categorieAnimal) {
        getCategorieAnimals().remove(categorieAnimal);
        categorieAnimal.setGospodarie(null);

        return categorieAnimal;
    }

    public List<Cladire> getCladires() {
        return this.cladires;
    }

    public void setCladires(List<Cladire> cladires) {
        this.cladires = cladires;
    }

    public Cladire addCladire(Cladire cladire) {
        getCladires().add(cladire);
        cladire.setGospodarie(this);

        return cladire;
    }

    public Cladire removeCladire(Cladire cladire) {
        getCladires().remove(cladire);
        cladire.setGospodarie(null);

        return cladire;
    }

    public List<Contract> getContracts() {
        return this.contracts;
    }

    public void setContracts(List<Contract> contracts) {
        this.contracts = contracts;
    }

    public Contract addContract(Contract contract) {
        getContracts().add(contract);
        contract.setGospodarie(this);

        return contract;
    }

    public Contract removeContract(Contract contract) {
        getContracts().remove(contract);
        contract.setGospodarie(null);

        return contract;
    }

    public List<Cultura> getCulturas() {
        return this.culturas;
    }

    public void setCulturas(List<Cultura> culturas) {
        this.culturas = culturas;
    }

    public Cultura addCultura(Cultura cultura) {
        getCulturas().add(cultura);
        cultura.setGospodarie(this);

        return cultura;
    }

    public Cultura removeCultura(Cultura cultura) {
        getCulturas().remove(cultura);
        cultura.setGospodarie(null);

        return cultura;
    }

    public List<DetinatorPf> getDetinatorPfs() {
        return this.detinatorPfs;
    }

    public void setDetinatorPfs(List<DetinatorPf> detinatorPfs) {
        this.detinatorPfs = detinatorPfs;
    }

    public DetinatorPf addDetinatorPf(DetinatorPf detinatorPf) {
        getDetinatorPfs().add(detinatorPf);
        detinatorPf.setGospodarie(this);

        return detinatorPf;
    }

    public DetinatorPf removeDetinatorPf(DetinatorPf detinatorPf) {
        getDetinatorPfs().remove(detinatorPf);
        detinatorPf.setGospodarie(null);

        return detinatorPf;
    }

    public List<DetinatorPj> getDetinatorPjs() {
        return this.detinatorPjs;
    }

    public void setDetinatorPjs(List<DetinatorPj> detinatorPjs) {
        this.detinatorPjs = detinatorPjs;
    }

    public DetinatorPj addDetinatorPj(DetinatorPj detinatorPj) {
        getDetinatorPjs().add(detinatorPj);
        detinatorPj.setGospodarie(this);

        return detinatorPj;
    }

    public DetinatorPj removeDetinatorPj(DetinatorPj detinatorPj) {
        getDetinatorPjs().remove(detinatorPj);
        detinatorPj.setGospodarie(null);

        return detinatorPj;
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

    public NomTipDetinator getNomTipDetinator() {
        return this.nomTipDetinator;
    }

    public void setNomTipDetinator(NomTipDetinator nomTipDetinator) {
        this.nomTipDetinator = nomTipDetinator;
    }

    public NomTipExploatatie getNomTipExploatatie() {
        return this.nomTipExploatatie;
    }

    public void setNomTipExploatatie(NomTipExploatatie nomTipExploatatie) {
        this.nomTipExploatatie = nomTipExploatatie;
    }

    public NomUat getNomUat() {
        return this.nomUat;
    }

    public void setNomUat(NomUat nomUat) {
        this.nomUat = nomUat;
    }

    public List<MentiuneCerereSuc> getMentiuneCerereSucs() {
        return this.mentiuneCerereSucs;
    }

    public void setMentiuneCerereSucs(List<MentiuneCerereSuc> mentiuneCerereSucs) {
        this.mentiuneCerereSucs = mentiuneCerereSucs;
    }

    public MentiuneCerereSuc addMentiuneCerereSuc(MentiuneCerereSuc mentiuneCerereSuc) {
        getMentiuneCerereSucs().add(mentiuneCerereSuc);
        mentiuneCerereSuc.setGospodarie(this);

        return mentiuneCerereSuc;
    }

    public MentiuneCerereSuc removeMentiuneCerereSuc(MentiuneCerereSuc mentiuneCerereSuc) {
        getMentiuneCerereSucs().remove(mentiuneCerereSuc);
        mentiuneCerereSuc.setGospodarie(null);

        return mentiuneCerereSuc;
    }

    public List<MentiuneSpeciala> getMentiuneSpecialas() {
        return this.mentiuneSpecialas;
    }

    public void setMentiuneSpecialas(List<MentiuneSpeciala> mentiuneSpecialas) {
        this.mentiuneSpecialas = mentiuneSpecialas;
    }

    public MentiuneSpeciala addMentiuneSpeciala(MentiuneSpeciala mentiuneSpeciala) {
        getMentiuneSpecialas().add(mentiuneSpeciala);
        mentiuneSpeciala.setGospodarie(this);

        return mentiuneSpeciala;
    }

    public MentiuneSpeciala removeMentiuneSpeciala(MentiuneSpeciala mentiuneSpeciala) {
        getMentiuneSpecialas().remove(mentiuneSpeciala);
        mentiuneSpeciala.setGospodarie(null);

        return mentiuneSpeciala;
    }

    public List<ParcelaTeren> getParcelaTerens() {
        return this.parcelaTerens;
    }

    public void setParcelaTerens(List<ParcelaTeren> parcelaTerens) {
        this.parcelaTerens = parcelaTerens;
    }

    public ParcelaTeren addParcelaTeren(ParcelaTeren parcelaTeren) {
        getParcelaTerens().add(parcelaTeren);
        parcelaTeren.setGospodarie(this);

        return parcelaTeren;
    }

    public ParcelaTeren removeParcelaTeren(ParcelaTeren parcelaTeren) {
        getParcelaTerens().remove(parcelaTeren);
        parcelaTeren.setGospodarie(null);

        return parcelaTeren;
    }

    public List<Plantatie> getPlantaties() {
        return this.plantaties;
    }

    public void setPlantaties(List<Plantatie> plantaties) {
        this.plantaties = plantaties;
    }

    public Plantatie addPlantaty(Plantatie plantaty) {
        getPlantaties().add(plantaty);
        plantaty.setGospodarie(this);

        return plantaty;
    }

    public Plantatie removePlantaty(Plantatie plantaty) {
        getPlantaties().remove(plantaty);
        plantaty.setGospodarie(null);

        return plantaty;
    }

    public List<PomRazlet> getPomRazlets() {
        return this.pomRazlets;
    }

    public void setPomRazlets(List<PomRazlet> pomRazlets) {
        this.pomRazlets = pomRazlets;
    }

    public PomRazlet addPomRazlet(PomRazlet pomRazlet) {
        getPomRazlets().add(pomRazlet);
        pomRazlet.setGospodarie(this);

        return pomRazlet;
    }

    public PomRazlet removePomRazlet(PomRazlet pomRazlet) {
        getPomRazlets().remove(pomRazlet);
        pomRazlet.setGospodarie(null);

        return pomRazlet;
    }

    public List<Preemptiune> getPreemptiunes() {
        return this.preemptiunes;
    }

    public void setPreemptiunes(List<Preemptiune> preemptiunes) {
        this.preemptiunes = preemptiunes;
    }

    public Preemptiune addPreemptiune(Preemptiune preemptiune) {
        getPreemptiunes().add(preemptiune);
        preemptiune.setGospodarie(this);

        return preemptiune;
    }

    public Preemptiune removePreemptiune(Preemptiune preemptiune) {
        getPreemptiunes().remove(preemptiune);
        preemptiune.setGospodarie(null);

        return preemptiune;
    }

    public List<Registru> getRegistrus() {
        return this.registrus;
    }

    public void setRegistrus(List<Registru> registrus) {
        this.registrus = registrus;
    }

    public Registru addRegistrus(Registru registrus) {
        getRegistrus().add(registrus);
        registrus.setGospodarie(this);

        return registrus;
    }

    public Registru removeRegistrus(Registru registrus) {
        getRegistrus().remove(registrus);
        registrus.setGospodarie(null);

        return registrus;
    }

    public List<SistemTehnic> getSistemTehnics() {
        return this.sistemTehnics;
    }

    public void setSistemTehnics(List<SistemTehnic> sistemTehnics) {
        this.sistemTehnics = sistemTehnics;
    }

    public SistemTehnic addSistemTehnic(SistemTehnic sistemTehnic) {
        getSistemTehnics().add(sistemTehnic);
        sistemTehnic.setGospodarie(this);

        return sistemTehnic;
    }

    public SistemTehnic removeSistemTehnic(SistemTehnic sistemTehnic) {
        getSistemTehnics().remove(sistemTehnic);
        sistemTehnic.setGospodarie(null);

        return sistemTehnic;
    }

    public List<SubstantaChimica> getSubstantaChimicas() {
        return this.substantaChimicas;
    }

    public void setSubstantaChimicas(List<SubstantaChimica> substantaChimicas) {
        this.substantaChimicas = substantaChimicas;
    }

    public SubstantaChimica addSubstantaChimica(SubstantaChimica substantaChimica) {
        getSubstantaChimicas().add(substantaChimica);
        substantaChimica.setGospodarie(this);

        return substantaChimica;
    }

    public SubstantaChimica removeSubstantaChimica(SubstantaChimica substantaChimica) {
        getSubstantaChimicas().remove(substantaChimica);
        substantaChimica.setGospodarie(null);

        return substantaChimica;
    }

    public List<SuprafataCategorie> getSuprafataCategories() {
        return this.suprafataCategories;
    }

    public void setSuprafataCategories(List<SuprafataCategorie> suprafataCategories) {
        this.suprafataCategories = suprafataCategories;
    }

    public SuprafataCategorie addSuprafataCategory(SuprafataCategorie suprafataCategory) {
        getSuprafataCategories().add(suprafataCategory);
        suprafataCategory.setGospodarie(this);

        return suprafataCategory;
    }

    public SuprafataCategorie removeSuprafataCategory(SuprafataCategorie suprafataCategory) {
        getSuprafataCategories().remove(suprafataCategory);
        suprafataCategory.setGospodarie(null);

        return suprafataCategory;
    }

    public List<SuprafataUtilizare> getSuprafataUtilizares() {
        return this.suprafataUtilizares;
    }

    public void setSuprafataUtilizares(List<SuprafataUtilizare> suprafataUtilizares) {
        this.suprafataUtilizares = suprafataUtilizares;
    }

    public SuprafataUtilizare addSuprafataUtilizare(SuprafataUtilizare suprafataUtilizare) {
        getSuprafataUtilizares().add(suprafataUtilizare);
        suprafataUtilizare.setGospodarie(this);

        return suprafataUtilizare;
    }

    public SuprafataUtilizare removeSuprafataUtilizare(SuprafataUtilizare suprafataUtilizare) {
        getSuprafataUtilizares().remove(suprafataUtilizare);
        suprafataUtilizare.setGospodarie(null);

        return suprafataUtilizare;
    }

    public List<TerenIrigat> getTerenIrigats() {
        return this.terenIrigats;
    }

    public void setTerenIrigats(List<TerenIrigat> terenIrigats) {
        this.terenIrigats = terenIrigats;
    }

    public TerenIrigat addTerenIrigat(TerenIrigat terenIrigat) {
        getTerenIrigats().add(terenIrigat);
        terenIrigat.setGospodarie(this);

        return terenIrigat;
    }

    public TerenIrigat removeTerenIrigat(TerenIrigat terenIrigat) {
        getTerenIrigats().remove(terenIrigat);
        terenIrigat.setGospodarie(null);

        return terenIrigat;
    }

}