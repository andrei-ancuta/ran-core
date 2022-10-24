package ro.uti.ran.core.model.registru;

import ro.uti.ran.core.model.Editable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the NOM_PLANTA_CULTURA database table.
 */
@Entity
@Table(name = "NOM_PLANTA_CULTURA")
@NamedQuery(name = "NomPlantaCultura.findAll", query = "SELECT n FROM NomPlantaCultura n")
public class NomPlantaCultura extends Nomenclator implements Editable, NomTermeniAgricoli{
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "NOM_PLANTA_CULTURA_IDNOM_PLANTA_CULTURA_GENERATOR", sequenceName = "SEQ_NOM_PLANTA_CULTURA", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOM_PLANTA_CULTURA_IDNOM_PLANTA_CULTURA_GENERATOR")
    @Column(name = "ID_NOM_PLANTA_CULTURA")
    private Long id;

    private String cod;

    private String denumire;

    private String descriere;

    //bi-directional many-to-one association to CapCultura
    @OneToMany(mappedBy = "nomPlantaCultura")
    private List<CapCultura> capCulturas = new ArrayList<>();

    //bi-directional many-to-one association to CapCulturaProd
    @OneToMany(mappedBy = "nomPlantaCultura")
    private List<CapCulturaProd> capCulturaProds = new ArrayList<>();

    //bi-directional many-to-one association to
    @ManyToOne
    @JoinColumn(name = "FK_NOM_PLANTA_CULTURA")
    private NomPlantaCultura nomPlantaCultura;

    //bi-directional many-to-one association to NomPlantaCultura
    @OneToMany(mappedBy = "nomPlantaCultura")
    private List<NomPlantaCultura> nomPlantaCulturas = new ArrayList<>();

    public NomPlantaCultura() {
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

    public String getDescriere() {
        return this.descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public List<CapCultura> getCapCulturas() {
        return this.capCulturas;
    }

    public void setCapCulturas(List<CapCultura> capCulturas) {
        this.capCulturas = capCulturas;
    }

    public CapCultura addCapCultura(CapCultura capCultura) {
        getCapCulturas().add(capCultura);
        capCultura.setNomPlantaCultura(this);

        return capCultura;
    }

    public CapCultura removeCapCultura(CapCultura capCultura) {
        getCapCulturas().remove(capCultura);
        capCultura.setNomPlantaCultura(null);

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
        capCulturaProd.setNomPlantaCultura(this);

        return capCulturaProd;
    }

    public CapCulturaProd removeCapCulturaProd(CapCulturaProd capCulturaProd) {
        getCapCulturaProds().remove(capCulturaProd);
        capCulturaProd.setNomPlantaCultura(null);

        return capCulturaProd;
    }

    public NomPlantaCultura getNomPlantaCultura() {
        return this.nomPlantaCultura;
    }

    public void setNomPlantaCultura(NomPlantaCultura nomPlantaCultura) {
        this.nomPlantaCultura = nomPlantaCultura;
    }

    public List<NomPlantaCultura> getNomPlantaCulturas() {
        return this.nomPlantaCulturas;
    }

    public void setNomPlantaCulturas(List<NomPlantaCultura> nomPlantaCulturas) {
        this.nomPlantaCulturas = nomPlantaCulturas;
    }

    public NomPlantaCultura addNomPlantaCultura(NomPlantaCultura nomPlantaCultura) {
        getNomPlantaCulturas().add(nomPlantaCultura);
        nomPlantaCultura.setNomPlantaCultura(this);

        return nomPlantaCultura;
    }

    public NomPlantaCultura removeNomPlantaCultura(NomPlantaCultura nomPlantaCultura) {
        getNomPlantaCulturas().remove(nomPlantaCultura);
        nomPlantaCultura.setNomPlantaCultura(null);

        return nomPlantaCultura;
    }

    @Override
    public String getCodePrim() {
        return this.cod;
    }

    @Override
    public String getCodePrimName() {
        return "cod";
    }

    @Override
    public Integer getCodeSec() {
        return null;
    }

    @Override
    public String getCodeSecName() {
        return null;
    }

    @Override
    public Date getStarting(){
        return null;
    }

}