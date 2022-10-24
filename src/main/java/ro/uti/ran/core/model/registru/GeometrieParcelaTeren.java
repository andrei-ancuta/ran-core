package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the GEOMETRIE_PARCELA_TEREN database table.
 */
@Entity
@Table(name = "GEOMETRIE_PARCELA_TEREN")
@NamedQuery(name = "GeometrieParcelaTeren.findAll", query = "SELECT g FROM GeometrieParcelaTeren g")
public class GeometrieParcelaTeren implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "GEOMETRIE_PARCELA_TEREN_IDGEOMETRIE_PARCELA_TEREN_GENERATOR", sequenceName = "SEQ_GEOMETRIE_PARCELA_TEREN", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEOMETRIE_PARCELA_TEREN_IDGEOMETRIE_PARCELA_TEREN_GENERATOR")
    @Column(name = "ID_GEOMETRIE_PARCELA_TEREN")
    private Long idGeometrieParcelaTeren;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;

    @Column(name = "IS_FOLOSINTA")
    private Integer isFolosinta;

    @Transient
    private String geometrieGML;

    //bi-directional one-to-one association to ParcelaTeren
    @OneToOne
    @JoinColumn(name = "FK_PARCELA_TEREN")
    private ParcelaTeren parcelaTeren;

    public GeometrieParcelaTeren() {
    }

    public Long getIdGeometrieParcelaTeren() {
        return this.idGeometrieParcelaTeren;
    }

    public void setIdGeometrieParcelaTeren(Long idGeometrieParcelaTeren) {
        this.idGeometrieParcelaTeren = idGeometrieParcelaTeren;
    }

    public Long getFkNomJudet() {
        return this.fkNomJudet;
    }

    public void setFkNomJudet(Long fkNomJudet) {
        this.fkNomJudet = fkNomJudet;
    }

    public ParcelaTeren getParcelaTeren() {
        return this.parcelaTeren;
    }

    public void setParcelaTeren(ParcelaTeren parcelaTeren) {
        this.parcelaTeren = parcelaTeren;
    }

    public Integer getIsFolosinta() {
        return isFolosinta;
    }

    public void setIsFolosinta(Integer isFolosinta) {
        this.isFolosinta = isFolosinta;
    }

    public String getGeometrieGML() {
        return geometrieGML;
    }

    public void setGeometrieGML(String geometrieGML) {
        this.geometrieGML = geometrieGML;
    }
}