package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the CULTURA database table.
 */
@Entity
@NamedQuery(name = "Cultura.findAll", query = "SELECT c FROM Cultura c")
public class Cultura implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "CULTURA_IDCULTURA_GENERATOR", sequenceName = "SEQ_CULTURA", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CULTURA_IDCULTURA_GENERATOR")
    @Column(name = "ID_CULTURA")
    private Long idCultura;

    @Column(name = "\"AN\"")
    private Integer an;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;


    private Integer suprafata;

    //bi-directional many-to-one association to GometrieCultura
    @OneToMany(mappedBy = "cultura", cascade = CascadeType.REMOVE)
    private List<GeometrieCultura> geometrieCulturi = new ArrayList<>();

    //bi-directional many-to-one association to CapCultura
    @ManyToOne
    @JoinColumn(name = "FK_CAP_CULTURA")
    private CapCultura capCultura;

    //bi-directional many-to-one association to Gospodarie
    @ManyToOne
    @JoinColumn(name = "FK_GOSPODARIE")
    private Gospodarie gospodarie;

    //bi-directional many-to-one association to NomTipSpatiuProt
    @ManyToOne
    @JoinColumn(name = "FK_NOM_TIP_SPATIU_PROT")
    private NomTipSpatiuProt nomTipSpatiuProt;


    public Cultura() {
    }

    public Long getIdCultura() {
        return this.idCultura;
    }

    public void setIdCultura(Long idCultura) {
        this.idCultura = idCultura;
    }

    public Integer getAn() {
        return this.an;
    }

    public void setAn(Integer an) {
        this.an = an;
    }

    public Long getFkNomJudet() {
        return this.fkNomJudet;
    }

    public void setFkNomJudet(Long fkNomJudet) {
        this.fkNomJudet = fkNomJudet;
    }


    public Integer getSuprafata() {
        return this.suprafata;
    }

    public void setSuprafata(Integer suprafata) {
        this.suprafata = suprafata;
    }

    public CapCultura getCapCultura() {
        return this.capCultura;
    }

    public void setCapCultura(CapCultura capCultura) {
        this.capCultura = capCultura;
    }

    public Gospodarie getGospodarie() {
        return this.gospodarie;
    }

    public void setGospodarie(Gospodarie gospodarie) {
        this.gospodarie = gospodarie;
    }

    public NomTipSpatiuProt getNomTipSpatiuProt() {
        return this.nomTipSpatiuProt;
    }

    public void setNomTipSpatiuProt(NomTipSpatiuProt nomTipSpatiuProt) {
        this.nomTipSpatiuProt = nomTipSpatiuProt;
    }

    public List<GeometrieCultura> getGeometrieCulturi() {
        return geometrieCulturi;
    }

    public void setGeometrieCulturi(List<GeometrieCultura> geometrieCulturi) {
        this.geometrieCulturi = geometrieCulturi;
    }
}