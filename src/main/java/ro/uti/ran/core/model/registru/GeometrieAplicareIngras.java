package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by miroslav.rusnac on 09/06/2016.
 */
@Entity
@Table(name = "GEOMETRIE_APLICARE_INGRAS")
@NamedQuery(name = "GeometrieAplicareIngras.findAll", query = "SELECT g FROM GeometrieAplicareIngras g")
public class GeometrieAplicareIngras  implements Serializable {
        private static final long serialVersionUID = 1L;

        @Id
        @SequenceGenerator(name = "GEOMETRIE_APLICARE_INGRAS_IDGEOMETRIE_APLICARE_INGRAS_GENERATOR", sequenceName = "SEQ_GEOMETRIE_APLICARE_INGRAS", allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEOMETRIE_APLICARE_INGRAS_IDGEOMETRIE_APLICARE_INGRAS_GENERATOR")
        @Column(name = "ID_GEOMETRIE_APLICARE_INGRAS")
        private Long idGeometrieAplicareIngras;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;

    @Column(name = "FK_APLICARE_INGRASAMANT")
    private Long fkAplicareIngrasamant;

    //bi-directional one-to-one association to Cultura
    @ManyToOne
    @JoinColumn(name = "FK_APLICARE_INGRASAMANT")
    private
    AplicareIngrasamant aplicareIngrasamant;

    @Transient
    private String geometrieGML;

    public Long getIdGeometrieAplicareIngras() {
        return idGeometrieAplicareIngras;
    }

    public void setIdGeometrieAplicareIngras(Long idGeometrieAplicareIngras) {
        this.idGeometrieAplicareIngras = idGeometrieAplicareIngras;
    }

    public Long getFkNomJudet() {
        return fkNomJudet;
    }

    public void setFkNomJudet(Long fkNomJudet) {
        this.fkNomJudet = fkNomJudet;
    }

    public Long getFkAplicareIngrasamant() {
        return fkAplicareIngrasamant;
    }

    public void setFkAplicareIngrasamant(Long fkAplicareIngrasamant) {
        this.fkAplicareIngrasamant = fkAplicareIngrasamant;
    }

    public AplicareIngrasamant getAplicareIngrasamant() {
        return aplicareIngrasamant;
    }

    public void setAplicareIngrasamant(AplicareIngrasamant aplicareIngrasamant) {
        this.aplicareIngrasamant = aplicareIngrasamant;
    }

    public String getGeometrieGML() {
        return geometrieGML;
    }

    public void setGeometrieGML(String geometrieGML) {
        this.geometrieGML = geometrieGML;
    }
}
