package ro.uti.ran.core.model.registru;

import ro.uti.ran.core.model.Editable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the NOM_POM_ARBUST database table.
 */
@Entity
@Table(name = "NOM_POM_ARBUST")
@NamedQuery(name = "NomPomArbust.findAll", query = "SELECT n FROM NomPomArbust n")
public class NomPomArbust extends Nomenclator implements Editable, NomTermeniAgricoli {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "NOM_POM_ARBUST_IDNOM_POM_ARBUST_GENERATOR", sequenceName = "SEQ_NOM_POM_ARBUST", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOM_POM_ARBUST_IDNOM_POM_ARBUST_GENERATOR")
    @Column(name = "ID_NOM_POM_ARBUST")
    private Long id;

    private String cod;

    private String denumire;

    private String descriere;

    //bi-directional many-to-one association to CapFructProd
    @OneToMany(mappedBy = "nomPomArbust")
    private List<CapFructProd> capFructProds = new ArrayList<>();

    //bi-directional many-to-one association to CapPlantatie
    @OneToMany(mappedBy = "nomPomArbust")
    private List<CapPlantatie> capPlantaties = new ArrayList<>();

    //bi-directional many-to-one association to CapPlantatieProd
    @OneToMany(mappedBy = "nomPomArbust")
    private List<CapPlantatieProd> capPlantatieProds = new ArrayList<>();

    //bi-directional many-to-one association to CapPomRazlet
    @OneToMany(mappedBy = "nomPomArbust")
    private List<CapPomRazlet> capPomRazlets = new ArrayList<>();

    //bi-directional many-to-one association to NomPomArbust
    @ManyToOne
    @JoinColumn(name = "FK_NOM_POM_ARBUST")
    private NomPomArbust nomPomArbust;

    //bi-directional many-to-one association to NomPomArbust
    @OneToMany(mappedBy = "nomPomArbust")
    private List<NomPomArbust> nomPomArbusts = new ArrayList<>();

    public NomPomArbust() {
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

    public List<CapFructProd> getCapFructProds() {
        return this.capFructProds;
    }

    public void setCapFructProds(List<CapFructProd> capFructProds) {
        this.capFructProds = capFructProds;
    }

    public CapFructProd addCapFructProd(CapFructProd capFructProd) {
        getCapFructProds().add(capFructProd);
        capFructProd.setNomPomArbust(this);

        return capFructProd;
    }

    public CapFructProd removeCapFructProd(CapFructProd capFructProd) {
        getCapFructProds().remove(capFructProd);
        capFructProd.setNomPomArbust(null);

        return capFructProd;
    }

    public List<CapPlantatie> getCapPlantaties() {
        return this.capPlantaties;
    }

    public void setCapPlantaties(List<CapPlantatie> capPlantaties) {
        this.capPlantaties = capPlantaties;
    }

    public CapPlantatie addCapPlantaty(CapPlantatie capPlantaty) {
        getCapPlantaties().add(capPlantaty);
        capPlantaty.setNomPomArbust(this);

        return capPlantaty;
    }

    public CapPlantatie removeCapPlantaty(CapPlantatie capPlantaty) {
        getCapPlantaties().remove(capPlantaty);
        capPlantaty.setNomPomArbust(null);

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
        capPlantatieProd.setNomPomArbust(this);

        return capPlantatieProd;
    }

    public CapPlantatieProd removeCapPlantatieProd(CapPlantatieProd capPlantatieProd) {
        getCapPlantatieProds().remove(capPlantatieProd);
        capPlantatieProd.setNomPomArbust(null);

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
        capPomRazlet.setNomPomArbust(this);

        return capPomRazlet;
    }

    public CapPomRazlet removeCapPomRazlet(CapPomRazlet capPomRazlet) {
        getCapPomRazlets().remove(capPomRazlet);
        capPomRazlet.setNomPomArbust(null);

        return capPomRazlet;
    }

    public NomPomArbust getNomPomArbust() {
        return this.nomPomArbust;
    }

    public void setNomPomArbust(NomPomArbust nomPomArbust) {
        this.nomPomArbust = nomPomArbust;
    }

    public List<NomPomArbust> getNomPomArbusts() {
        return this.nomPomArbusts;
    }

    public void setNomPomArbusts(List<NomPomArbust> nomPomArbusts) {
        this.nomPomArbusts = nomPomArbusts;
    }

    public NomPomArbust addNomPomArbust(NomPomArbust nomPomArbust) {
        getNomPomArbusts().add(nomPomArbust);
        nomPomArbust.setNomPomArbust(this);

        return nomPomArbust;
    }

    public NomPomArbust removeNomPomArbust(NomPomArbust nomPomArbust) {
        getNomPomArbusts().remove(nomPomArbust);
        nomPomArbust.setNomPomArbust(null);

        return nomPomArbust;
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