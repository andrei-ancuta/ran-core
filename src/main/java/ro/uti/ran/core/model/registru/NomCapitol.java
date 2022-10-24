package ro.uti.ran.core.model.registru;

import ro.uti.ran.core.model.Versioned;
import ro.uti.ran.core.service.backend.utils.TipCapitol;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the NOM_CAPITOL database table.
 */
@Entity
@Table(name = "NOM_CAPITOL")
@NamedQuery(name = "NomCapitol.findAll", query = "SELECT n FROM NomCapitol n")
public class NomCapitol extends Nomenclator implements Versioned, NomCapRegistrulAgricol {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "NOM_CAPITOL_ID_GENERATOR", sequenceName = "SEQ_NOM_CAPITOL", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOM_CAPITOL_ID_GENERATOR")
    @Column(name = "ID_NOM_CAPITOL", updatable = false)
    private Long id;

    @Column(name = "\"ALIAS\"")
    private String alias;

    @Column(name = "BASE_ID")
    private Long baseId;

    @Enumerated(EnumType.STRING)
    private TipCapitol cod;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_START")
    private Date dataStart;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_STOP")
    private Date dataStop;

    private String denumire;

    private String descriere;

    //bi-directional many-to-one association to CapAnimalProd
    @OneToMany(mappedBy = "nomCapitol")
    private List<CapAnimalProd> capAnimalProds = new ArrayList<>();

    //bi-directional many-to-one association to CapAplicareIngrasamant
    @OneToMany(mappedBy = "nomCapitol")
    private List<CapAplicareIngrasamant> capAplicareIngrasamants = new ArrayList<>();

    //bi-directional many-to-one association to CapCategorieAnimal
    @OneToMany(mappedBy = "nomCapitol")
    private List<CapCategorieAnimal> capCategorieAnimals = new ArrayList<>();

    //bi-directional many-to-one association to CapCategorieFolosinta
    @OneToMany(mappedBy = "nomCapitol")
    private List<CapCategorieFolosinta> capCategorieFolosintas = new ArrayList<>();

    //bi-directional many-to-one association to CapCultura
    @OneToMany(mappedBy = "nomCapitol")
    private List<CapCultura> capCulturas = new ArrayList<>();

    //bi-directional many-to-one association to CapCulturaProd
    @OneToMany(mappedBy = "nomCapitol")
    private List<CapCulturaProd> capCulturaProds = new ArrayList<>();

    //bi-directional many-to-one association to CapFructProd
    @OneToMany(mappedBy = "nomCapitol")
    private List<CapFructProd> capFructProds = new ArrayList<>();

    //bi-directional many-to-one association to CapModUtilizare
    @OneToMany(mappedBy = "nomCapitol")
    private List<CapModUtilizare> capModUtilizares = new ArrayList<>();

    //bi-directional many-to-one association to CapPlantatie
    @OneToMany(mappedBy = "nomCapitol")
    private List<CapPlantatie> capPlantaties = new ArrayList<>();

    //bi-directional many-to-one association to CapPlantatieProd
    @OneToMany(mappedBy = "nomCapitol")
    private List<CapPlantatieProd> capPlantatieProds = new ArrayList<>();

    //bi-directional many-to-one association to CapPomRazlet
    @OneToMany(mappedBy = "nomCapitol")
    private List<CapPomRazlet> capPomRazlets = new ArrayList<>();

    //bi-directional many-to-one association to CapSistemTehnic
    @OneToMany(mappedBy = "nomCapitol")
    private List<CapSistemTehnic> capSistemTehnics = new ArrayList<>();

    //bi-directional many-to-one association to CapSubstantaChimica
    @OneToMany(mappedBy = "nomCapitol")
    private List<CapSubstantaChimica> capSubstantaChimicas = new ArrayList<>();

    //bi-directional many-to-one association to CapTerenIrigat
    @OneToMany(mappedBy = "nomCapitol")
    private List<CapTerenIrigat> capTerenIrigats = new ArrayList<>();

    @Transient
    private boolean isLatestVersion;

    public NomCapitol() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Long getBaseId() {
        return this.baseId;
    }

    public void setBaseId(Long baseId) {
        this.baseId = baseId;
    }

    public TipCapitol getCod() {
        return this.cod;
    }

    public void setCod(TipCapitol cod) {
        this.cod = cod;
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

    public String getDenumire() {
        return this.denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public String getDescriere() {
        return this.descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public List<CapAnimalProd> getCapAnimalProds() {
        return this.capAnimalProds;
    }

    public void setCapAnimalProds(List<CapAnimalProd> capAnimalProds) {
        this.capAnimalProds = capAnimalProds;
    }

    public CapAnimalProd addCapAnimalProd(CapAnimalProd capAnimalProd) {
        getCapAnimalProds().add(capAnimalProd);
        capAnimalProd.setNomCapitol(this);

        return capAnimalProd;
    }

    public CapAnimalProd removeCapAnimalProd(CapAnimalProd capAnimalProd) {
        getCapAnimalProds().remove(capAnimalProd);
        capAnimalProd.setNomCapitol(null);

        return capAnimalProd;
    }

    public List<CapAplicareIngrasamant> getCapAplicareIngrasamants() {
        return this.capAplicareIngrasamants;
    }

    public void setCapAplicareIngrasamants(List<CapAplicareIngrasamant> capAplicareIngrasamants) {
        this.capAplicareIngrasamants = capAplicareIngrasamants;
    }

    public CapAplicareIngrasamant addCapAplicareIngrasamant(CapAplicareIngrasamant capAplicareIngrasamant) {
        getCapAplicareIngrasamants().add(capAplicareIngrasamant);
        capAplicareIngrasamant.setNomCapitol(this);

        return capAplicareIngrasamant;
    }

    public CapAplicareIngrasamant removeCapAplicareIngrasamant(CapAplicareIngrasamant capAplicareIngrasamant) {
        getCapAplicareIngrasamants().remove(capAplicareIngrasamant);
        capAplicareIngrasamant.setNomCapitol(null);

        return capAplicareIngrasamant;
    }

    public List<CapCategorieAnimal> getCapCategorieAnimals() {
        return this.capCategorieAnimals;
    }

    public void setCapCategorieAnimals(List<CapCategorieAnimal> capCategorieAnimals) {
        this.capCategorieAnimals = capCategorieAnimals;
    }

    public CapCategorieAnimal addCapCategorieAnimal(CapCategorieAnimal capCategorieAnimal) {
        getCapCategorieAnimals().add(capCategorieAnimal);
        capCategorieAnimal.setNomCapitol(this);

        return capCategorieAnimal;
    }

    public CapCategorieAnimal removeCapCategorieAnimal(CapCategorieAnimal capCategorieAnimal) {
        getCapCategorieAnimals().remove(capCategorieAnimal);
        capCategorieAnimal.setNomCapitol(null);

        return capCategorieAnimal;
    }

    public List<CapCategorieFolosinta> getCapCategorieFolosintas() {
        return this.capCategorieFolosintas;
    }

    public void setCapCategorieFolosintas(List<CapCategorieFolosinta> capCategorieFolosintas) {
        this.capCategorieFolosintas = capCategorieFolosintas;
    }

    public CapCategorieFolosinta addCapCategorieFolosinta(CapCategorieFolosinta capCategorieFolosinta) {
        getCapCategorieFolosintas().add(capCategorieFolosinta);
        capCategorieFolosinta.setNomCapitol(this);

        return capCategorieFolosinta;
    }

    public CapCategorieFolosinta removeCapCategorieFolosinta(CapCategorieFolosinta capCategorieFolosinta) {
        getCapCategorieFolosintas().remove(capCategorieFolosinta);
        capCategorieFolosinta.setNomCapitol(null);

        return capCategorieFolosinta;
    }

    public List<CapCultura> getCapCulturas() {
        return this.capCulturas;
    }

    public void setCapCulturas(List<CapCultura> capCulturas) {
        this.capCulturas = capCulturas;
    }

    public CapCultura addCapCultura(CapCultura capCultura) {
        getCapCulturas().add(capCultura);
        capCultura.setNomCapitol(this);

        return capCultura;
    }

    public CapCultura removeCapCultura(CapCultura capCultura) {
        getCapCulturas().remove(capCultura);
        capCultura.setNomCapitol(null);

        return capCultura;
    }

    public List<CapCulturaProd> getCapCulturaProds() {
        return this.capCulturaProds;
    }

    public void setCapCulturaProds(List<CapCulturaProd> capCulturaProds) {
        this.capCulturaProds = capCulturaProds;
    }

    public CapCulturaProd addCapCulturaProd(CapCulturaProd capCulturaProd) {
        getCapCulturaProds().add(capCulturaProd);
        capCulturaProd.setNomCapitol(this);

        return capCulturaProd;
    }

    public CapCulturaProd removeCapCulturaProd(CapCulturaProd capCulturaProd) {
        getCapCulturaProds().remove(capCulturaProd);
        capCulturaProd.setNomCapitol(null);

        return capCulturaProd;
    }

    public List<CapFructProd> getCapFructProds() {
        return this.capFructProds;
    }

    public void setCapFructProds(List<CapFructProd> capFructProds) {
        this.capFructProds = capFructProds;
    }

    public CapFructProd addCapFructProd(CapFructProd capFructProd) {
        getCapFructProds().add(capFructProd);
        capFructProd.setNomCapitol(this);

        return capFructProd;
    }

    public CapFructProd removeCapFructProd(CapFructProd capFructProd) {
        getCapFructProds().remove(capFructProd);
        capFructProd.setNomCapitol(null);

        return capFructProd;
    }

    public List<CapModUtilizare> getCapModUtilizares() {
        return this.capModUtilizares;
    }

    public void setCapModUtilizares(List<CapModUtilizare> capModUtilizares) {
        this.capModUtilizares = capModUtilizares;
    }

    public CapModUtilizare addCapModUtilizare(CapModUtilizare capModUtilizare) {
        getCapModUtilizares().add(capModUtilizare);
        capModUtilizare.setNomCapitol(this);

        return capModUtilizare;
    }

    public CapModUtilizare removeCapModUtilizare(CapModUtilizare capModUtilizare) {
        getCapModUtilizares().remove(capModUtilizare);
        capModUtilizare.setNomCapitol(null);

        return capModUtilizare;
    }

    public List<CapPlantatie> getCapPlantaties() {
        return this.capPlantaties;
    }

    public void setCapPlantaties(List<CapPlantatie> capPlantaties) {
        this.capPlantaties = capPlantaties;
    }

    public CapPlantatie addCapPlantaty(CapPlantatie capPlantaty) {
        getCapPlantaties().add(capPlantaty);
        capPlantaty.setNomCapitol(this);

        return capPlantaty;
    }

    public CapPlantatie removeCapPlantaty(CapPlantatie capPlantaty) {
        getCapPlantaties().remove(capPlantaty);
        capPlantaty.setNomCapitol(null);

        return capPlantaty;
    }

    public List<CapPlantatieProd> getCapPlantatieProds() {
        return this.capPlantatieProds;
    }

    public void setCapPlantatieProds(List<CapPlantatieProd> capPlantatieProds) {
        this.capPlantatieProds = capPlantatieProds;
    }

    public CapPlantatieProd addCapPlantatieProd(CapPlantatieProd capPlantatieProd) {
        getCapPlantatieProds().add(capPlantatieProd);
        capPlantatieProd.setNomCapitol(this);

        return capPlantatieProd;
    }

    public CapPlantatieProd removeCapPlantatieProd(CapPlantatieProd capPlantatieProd) {
        getCapPlantatieProds().remove(capPlantatieProd);
        capPlantatieProd.setNomCapitol(null);

        return capPlantatieProd;
    }

    public List<CapPomRazlet> getCapPomRazlets() {
        return this.capPomRazlets;
    }

    public void setCapPomRazlets(List<CapPomRazlet> capPomRazlets) {
        this.capPomRazlets = capPomRazlets;
    }

    public CapPomRazlet addCapPomRazlet(CapPomRazlet capPomRazlet) {
        getCapPomRazlets().add(capPomRazlet);
        capPomRazlet.setNomCapitol(this);

        return capPomRazlet;
    }

    public CapPomRazlet removeCapPomRazlet(CapPomRazlet capPomRazlet) {
        getCapPomRazlets().remove(capPomRazlet);
        capPomRazlet.setNomCapitol(null);

        return capPomRazlet;
    }

    public List<CapSistemTehnic> getCapSistemTehnics() {
        return this.capSistemTehnics;
    }

    public void setCapSistemTehnics(List<CapSistemTehnic> capSistemTehnics) {
        this.capSistemTehnics = capSistemTehnics;
    }

    public CapSistemTehnic addCapSistemTehnic(CapSistemTehnic capSistemTehnic) {
        getCapSistemTehnics().add(capSistemTehnic);
        capSistemTehnic.setNomCapitol(this);

        return capSistemTehnic;
    }

    public CapSistemTehnic removeCapSistemTehnic(CapSistemTehnic capSistemTehnic) {
        getCapSistemTehnics().remove(capSistemTehnic);
        capSistemTehnic.setNomCapitol(null);

        return capSistemTehnic;
    }

    public List<CapSubstantaChimica> getCapSubstantaChimicas() {
        return this.capSubstantaChimicas;
    }

    public void setCapSubstantaChimicas(List<CapSubstantaChimica> capSubstantaChimicas) {
        this.capSubstantaChimicas = capSubstantaChimicas;
    }

    public CapSubstantaChimica addCapSubstantaChimica(CapSubstantaChimica capSubstantaChimica) {
        getCapSubstantaChimicas().add(capSubstantaChimica);
        capSubstantaChimica.setNomCapitol(this);

        return capSubstantaChimica;
    }

    public CapSubstantaChimica removeCapSubstantaChimica(CapSubstantaChimica capSubstantaChimica) {
        getCapSubstantaChimicas().remove(capSubstantaChimica);
        capSubstantaChimica.setNomCapitol(null);

        return capSubstantaChimica;
    }

    public List<CapTerenIrigat> getCapTerenIrigats() {
        return this.capTerenIrigats;
    }

    public void setCapTerenIrigats(List<CapTerenIrigat> capTerenIrigats) {
        this.capTerenIrigats = capTerenIrigats;
    }

    public CapTerenIrigat addCapTerenIrigat(CapTerenIrigat capTerenIrigat) {
        getCapTerenIrigats().add(capTerenIrigat);
        capTerenIrigat.setNomCapitol(this);

        return capTerenIrigat;
    }

    public CapTerenIrigat removeCapTerenIrigat(CapTerenIrigat capTerenIrigat) {
        getCapTerenIrigats().remove(capTerenIrigat);
        capTerenIrigat.setNomCapitol(null);

        return capTerenIrigat;
    }

    @Override
    @XmlElement(name="isLatestVersion")
    public boolean isLatestVersion() {
        return isLatestVersion;
    }

}