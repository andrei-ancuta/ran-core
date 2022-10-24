package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the ACT_DETINERE database table.
 */
@Entity
@Table(name = "ACT_DETINERE")
@NamedQuery(name = "ActDetinere.findAll", query = "SELECT a FROM ActDetinere a")
public class ActDetinere implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "ACT_DETINERE_IDACTDETINERE_GENERATOR", sequenceName = "SEQ_ACT_DETINERE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACT_DETINERE_IDACTDETINERE_GENERATOR")
    @Column(name = "ID_ACT_DETINERE")
    private Long idActDetinere;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;

    //bi-directional many-to-one association to Act
    @ManyToOne
    @JoinColumn(name = "FK_ACT")
    private Act act;

    //bi-directional many-to-one association to ParcelaTeren
    @ManyToOne
    @JoinColumn(name = "FK_PARCELA_TEREN")
    private ParcelaTeren parcelaTeren;

    public ActDetinere() {
    }

    public Long getIdActDetinere() {
        return this.idActDetinere;
    }

    public void setIdActDetinere(Long idActDetinere) {
        this.idActDetinere = idActDetinere;
    }

    public Long getFkNomJudet() {
        return this.fkNomJudet;
    }

    public void setFkNomJudet(Long fkNomJudet) {
        this.fkNomJudet = fkNomJudet;
    }

    public Act getAct() {
        return this.act;
    }

    public void setAct(Act act) {
        this.act = act;
    }

    public ParcelaTeren getParcelaTeren() {
        return this.parcelaTeren;
    }

    public void setParcelaTeren(ParcelaTeren parcelaTeren) {
        this.parcelaTeren = parcelaTeren;
    }

}