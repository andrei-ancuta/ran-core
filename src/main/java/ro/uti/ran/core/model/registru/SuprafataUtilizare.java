package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


/**
 * The persistent class for the SUPRAFATA_UTILIZARE database table.
 */
@Entity
@Table(name = "SUPRAFATA_UTILIZARE")
@NamedQuery(name = "SuprafataUtilizare.findAll", query = "SELECT s FROM SuprafataUtilizare s")
public class SuprafataUtilizare implements Serializable {
    private static final long serialVersionUID = 1L;
    //bi-directional one-to-many association to GeometrieSuprafataUtiliz
    @OneToMany(mappedBy = "suprafataUtilizare", cascade = CascadeType.REMOVE)
    List<GeometrieSuprafataUtiliz> geometrieSuprafataUtiliz;
    @Id
    @SequenceGenerator(name = "SUPRAFATA_UTILIZARE_IDSUPRAFATAUTILIZARE_GENERATOR", sequenceName = "SEQ_SUPRAFATA_UTILIZARE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUPRAFATA_UTILIZARE_IDSUPRAFATAUTILIZARE_GENERATOR")
    @Column(name = "ID_SUPRAFATA_UTILIZARE")
    private Long idSuprafataUtilizare;
    @Column(name = "\"AN\"")
    private Integer an;
    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;
    private Integer suprafata;
    //bi-directional many-to-one association to CapModUtilizare
    @ManyToOne
    @JoinColumn(name = "FK_CAP_MOD_UTILIZARE")
    private CapModUtilizare capModUtilizare;
    //bi-directional many-to-one association to Gospodarie
    @ManyToOne
    @JoinColumn(name = "FK_GOSPODARIE")
    private Gospodarie gospodarie;

    public SuprafataUtilizare() {
    }

    public Long getIdSuprafataUtilizare() {
        return this.idSuprafataUtilizare;
    }

    public void setIdSuprafataUtilizare(Long idSuprafataUtilizare) {
        this.idSuprafataUtilizare = idSuprafataUtilizare;
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

    public CapModUtilizare getCapModUtilizare() {
        return this.capModUtilizare;
    }

    public void setCapModUtilizare(CapModUtilizare capModUtilizare) {
        this.capModUtilizare = capModUtilizare;
    }

    public Gospodarie getGospodarie() {
        return this.gospodarie;
    }

    public void setGospodarie(Gospodarie gospodarie) {
        this.gospodarie = gospodarie;
    }

    public List<GeometrieSuprafataUtiliz> getGeometrieSuprafataUtiliz() {
        return geometrieSuprafataUtiliz;
    }

    public void setGeometrieSuprafataUtiliz(List<GeometrieSuprafataUtiliz> geometrieSuprafataUtiliz) {
        this.geometrieSuprafataUtiliz = geometrieSuprafataUtiliz;
    }


}