package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by miroslav.rusnac on 07/06/2016.
 */
@Entity
@Table(name = "GEOMETRIE_CULTURA")
@NamedQuery(name = "GeometrieCultura.findAll", query = "SELECT g FROM GeometrieCultura g")
public class GeometrieCultura implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "GEOMETRIE_CULTURA_IDGEOMETRIE_CULTURA_GENERATOR", sequenceName = "SEQ_GEOMETRIE_CULTURA", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEOMETRIE_CULTURA_IDGEOMETRIE_CULTURA_GENERATOR")
    @Column(name = "ID_GEOMETRIE_CULTURA")
    private Long idGeometrieCultura;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;

    @Column(name = "FK_CULTURA")
    private Long fkCultura;

    @Column(name = "IS_PRINCIPALA")
    private Integer isPrincipala;

    //bi-directional one-to-one association to Cultura
    @ManyToOne
    @JoinColumn(name = "FK_CULTURA")
    private Cultura cultura;


    @Transient
    private String geometrieGML;

    public GeometrieCultura() {
    }

    public Long getIdGeometrieCultura() {
        return this.idGeometrieCultura;
    }

    public void setIdGeometrieCultura(Long idGeometrieCultura) {
        this.idGeometrieCultura = idGeometrieCultura;
    }

    public Long getFkNomJudet() {
        return this.fkNomJudet;
    }

    public void setFkNomJudet(Long fkNomJudet) {
        this.fkNomJudet = fkNomJudet;
    }

    public Cultura getCultura() {
        return this.cultura;
    }

    public void setCultura(Cultura cultura) {
        this.cultura = cultura;
    }

    public String getGeometrieGML() {
        return geometrieGML;
    }

    public void setGeometrieGML(String geometrieGML) {
        this.geometrieGML = geometrieGML;
    }

    public Integer getIsPrincipala() {
        return isPrincipala;
    }

    public void setIsPrincipala(Integer isPrincipala) {
        this.isPrincipala = isPrincipala;
    }
}
