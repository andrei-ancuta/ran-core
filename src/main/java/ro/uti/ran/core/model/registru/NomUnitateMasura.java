package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the NOM_UNITATE_MASURA database table.
 */
@Entity
@Table(name = "NOM_UNITATE_MASURA")
@NamedQuery(name = "NomUnitateMasura.findAll", query = "SELECT n FROM NomUnitateMasura n")
public class NomUnitateMasura extends Nomenclator implements NomRegistrulAgricol {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "NOM_UNITATE_MASURA_ID_GENERATOR", sequenceName = "SEQ_NOM_UNITATE_MASURA", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOM_UNITATE_MASURA_ID_GENERATOR")
    @Column(name = "ID_NOM_UNITATE_MASURA", updatable = false)
    private Long id;

    private String cod;

    private String denumire;

    //bi-directional many-to-one association to CapAnimalProd
    @OneToMany(mappedBy = "nomUnitateMasura")
    private List<CapAnimalProd> capAnimalProds = new ArrayList<>();

    //bi-directional many-to-one association to CapCulturaProd
    @OneToMany(mappedBy = "nomUnitateMasura")
    private List<CapCulturaProd> capCulturaProds = new ArrayList<>();

    //bi-directional many-to-one association to NomTipUnitateMasura
    @ManyToOne
    @JoinColumn(name = "FK_NOM_TIP_UNITATE_MASURA")
    private NomTipUnitateMasura nomTipUnitateMasura;

    public NomUnitateMasura() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCod() {
        return this.cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getDenumire() {
        return this.denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public List<CapAnimalProd> getCapAnimalProds() {
        return this.capAnimalProds;
    }

    public void setCapAnimalProds(List<CapAnimalProd> capAnimalProds) {
        this.capAnimalProds = capAnimalProds;
    }

    public CapAnimalProd addCapAnimalProd(CapAnimalProd capAnimalProd) {
        getCapAnimalProds().add(capAnimalProd);
        capAnimalProd.setNomUnitateMasura(this);

        return capAnimalProd;
    }

    public CapAnimalProd removeCapAnimalProd(CapAnimalProd capAnimalProd) {
        getCapAnimalProds().remove(capAnimalProd);
        capAnimalProd.setNomUnitateMasura(null);

        return capAnimalProd;
    }

    public List<CapCulturaProd> getCapCulturaProds() {
        return this.capCulturaProds;
    }

    public void setCapCulturaProds(List<CapCulturaProd> capCulturaProds) {
        this.capCulturaProds = capCulturaProds;
    }

    public CapCulturaProd addCapCulturaProd(CapCulturaProd capCulturaProd) {
        getCapCulturaProds().add(capCulturaProd);
        capCulturaProd.setNomUnitateMasura(this);

        return capCulturaProd;
    }

    public CapCulturaProd removeCapCulturaProd(CapCulturaProd capCulturaProd) {
        getCapCulturaProds().remove(capCulturaProd);
        capCulturaProd.setNomUnitateMasura(null);

        return capCulturaProd;
    }

    public NomTipUnitateMasura getNomTipUnitateMasura() {
        return this.nomTipUnitateMasura;
    }

    public void setNomTipUnitateMasura(NomTipUnitateMasura nomTipUnitateMasura) {
        this.nomTipUnitateMasura = nomTipUnitateMasura;
    }

}