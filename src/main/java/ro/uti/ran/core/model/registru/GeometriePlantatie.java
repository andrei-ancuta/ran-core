package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by miroslav.rusnac on 08/06/2016.
 */
@Entity
@Table(name = "GEOMETRIE_PLANTATIE")
@NamedQuery(name = "GeometriePlantatie.findAll", query = "SELECT g FROM GeometriePlantatie g")
public class GeometriePlantatie implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "GEOMETRIE_PLANTATIE_IDGEOMETRIE_PLANTATIE_GENERATOR", sequenceName = "SEQ_GEOMETRIE_PLANTATIE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEOMETRIE_PLANTATIE_IDGEOMETRIE_PLANTATIE_GENERATOR")
    @Column(name = "ID_GEOMETRIE_PLANTATIE")
    private Long idGeometriePlantatie;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;

    @Column(name = "FK_PLANTATIE")
    private Long fkPlantatie;

    //bi-directional one-to-one association to Plantatie
    @ManyToOne
    @JoinColumn(name = "FK_PLANTATIE")
    private Plantatie plantatie;


    @Transient
    private String geometrieGML;

    public GeometriePlantatie(){}

    public Long getIdGeometriePlantatie() {
        return idGeometriePlantatie;
    }

    public void setIdGeometriePlantatie(Long idGeometriePlantatie) {
        this.idGeometriePlantatie = idGeometriePlantatie;
    }

    public Long getFkNomJudet() {
        return fkNomJudet;
    }

    public void setFkNomJudet(Long fkNomJudet) {
        this.fkNomJudet = fkNomJudet;
    }

    public Long getFkPlantatie() {
        return fkPlantatie;
    }

    public void setFkPlantatie(Long fkPlantatie) {
        this.fkPlantatie = fkPlantatie;
    }

    public Plantatie getPlantatie() {
        return plantatie;
    }

    public void setPlantatie(Plantatie plantatie) {
        this.plantatie = plantatie;
    }

    public String getGeometrieGML() {
        return geometrieGML;
    }

    public void setGeometrieGML(String geometrieGML) {
        this.geometrieGML = geometrieGML;
    }
}
