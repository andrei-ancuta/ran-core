package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The persistent class for the GEOMETRIE_SUPRAFATA_UTILIZ database table.
 */
@Entity
@Table(name = "GEOMETRIE_SUPRAFATA_UTILIZ")
public class GeometrieSuprafataUtiliz implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "GEOMETRIE_SUPRAFATA_UTILIZ_IDGEOMETRIE_SUPRAFATA_UTILIZ_GENERATOR", sequenceName = "SEQ_GEOMETRIE_SUPRAFATA_UTILIZ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEOMETRIE_SUPRAFATA_UTILIZ_IDGEOMETRIE_SUPRAFATA_UTILIZ_GENERATOR")
    @Column(name = "ID_GEOMETRIE_SUPRAFATA_UTILIZ")
    private Long idGeometrieSuprafataUtiliz;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;

    @Transient
    private String geometrieGML;

    //bi-directional one-to-one association to SuprafataUtilizare
    @ManyToOne
    @JoinColumn(name = "FK_SUPRAFATA_UTILIZARE")
    private SuprafataUtilizare suprafataUtilizare;

    public GeometrieSuprafataUtiliz() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getIdGeometrieSuprafataUtiliz() {
        return idGeometrieSuprafataUtiliz;
    }

    public void setIdGeometrieSuprafataUtiliz(Long idGeometrieSuprafataUtiliz) {
        this.idGeometrieSuprafataUtiliz = idGeometrieSuprafataUtiliz;
    }

    public Long getFkNomJudet() {
        return fkNomJudet;
    }

    public void setFkNomJudet(Long fkNomJudet) {
        this.fkNomJudet = fkNomJudet;
    }

    public SuprafataUtilizare getSuprafataUtilizare() {
        return suprafataUtilizare;
    }

    public void setSuprafataUtilizare(SuprafataUtilizare suprafataUtilizare) {
        this.suprafataUtilizare = suprafataUtilizare;
    }

    public String getGeometrieGML() {
        return geometrieGML;
    }

    public void setGeometrieGML(String geometrieGML) {
        this.geometrieGML = geometrieGML;
    }
}
