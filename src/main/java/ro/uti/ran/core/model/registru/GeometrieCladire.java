package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the GEOMETRIE_CLADIRE database table.
 */
@Entity
@Table(name = "GEOMETRIE_CLADIRE")
@NamedQuery(name = "GeometrieCladire.findAll", query = "SELECT g FROM GeometrieCladire g")
public class GeometrieCladire implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "GEOMETRIE_CLADIRE_IDGEOMETRIE_CLADIRE_GENERATOR", sequenceName = "SEQ_GEOMETRIE_CLADIRE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEOMETRIE_CLADIRE_IDGEOMETRIE_CLADIRE_GENERATOR")
    @Column(name = "ID_GEOMETRIE_CLADIRE")
    private Long idGeometrieCladire;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;

    //bi-directional many-to-one association to Cladire
    @OneToOne
    @JoinColumn(name = "FK_CLADIRE")
    private Cladire cladire;

    public GeometrieCladire() {
    }

    public Long getIdGeometrieCladire() {
        return this.idGeometrieCladire;
    }

    public void setIdGeometrieCladire(Long idGeometrieCladire) {
        this.idGeometrieCladire = idGeometrieCladire;
    }

    public Long getFkNomJudet() {
        return this.fkNomJudet;
    }

    public void setFkNomJudet(Long fkNomJudet) {
        this.fkNomJudet = fkNomJudet;
    }

    public Cladire getCladire() {
        return this.cladire;
    }

    public void setCladire(Cladire cladire) {
        this.cladire = cladire;
    }

}