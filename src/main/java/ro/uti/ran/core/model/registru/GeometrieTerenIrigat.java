package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by miroslav.rusnac on 09/06/2016.
 */
@Entity
@Table(name = "GEOMETRIE_TEREN_IRIGAT")
@NamedQuery(name = "GeometrieTerenIrigat.findAll", query = "SELECT g FROM GeometrieTerenIrigat g")
public class GeometrieTerenIrigat implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "GEOMETRIE_TEREN_IRIGAT_IDGEOMETRIE_TEREN_IRIGAT_GENERATOR", sequenceName = "SEQ_GEOMETRIE_TEREN_IRIGAT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEOMETRIE_TEREN_IRIGAT_IDGEOMETRIE_TEREN_IRIGAT_GENERATOR")
    @Column(name = "ID_GEOMETRIE_TEREN_IRIGAT")
    private Long idGeometrieTerenIrigat;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;

    @Column(name = "FK_TEREN_IRIGAT")
    private Long fkTerenIrigat;

    //bi-directional one-to-one association to TerenIrigat
    @ManyToOne
    @JoinColumn(name = "FK_TEREN_IRIGAT")
    private
    TerenIrigat terenIrigat;

    @Transient
    private String geometrieGML;

    public Long getIdGeometrieTerenIrigat() {
        return idGeometrieTerenIrigat;
    }

    public void setIdGeometrieTerenIrigat(Long idGeometrieTerenIrigat) {
        this.idGeometrieTerenIrigat = idGeometrieTerenIrigat;
    }

    public Long getFkNomJudet() {
        return fkNomJudet;
    }

    public void setFkNomJudet(Long fkNomJudet) {
        this.fkNomJudet = fkNomJudet;
    }

    public Long getFkTerenIrigat() {
        return fkTerenIrigat;
    }

    public void setFkTerenIrigat(Long fkTerenIrigat) {
        this.fkTerenIrigat = fkTerenIrigat;
    }

    public TerenIrigat getTerenIrigat() {
        return terenIrigat;
    }

    public void setTerenIrigat(TerenIrigat terenIrigat) {
        this.terenIrigat = terenIrigat;
    }

    public String getGeometrieGML() {
        return geometrieGML;
    }

    public void setGeometrieGML(String geometrieGML) {
        this.geometrieGML = geometrieGML;
    }
}
