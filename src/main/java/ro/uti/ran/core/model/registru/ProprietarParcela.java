package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the PROPRIETAR_PARCELA database table.
 */
@Entity
@Table(name = "PROPRIETAR_PARCELA")
@NamedQuery(name = "ProprietarParcela.findAll", query = "SELECT p FROM ProprietarParcela p")
public class ProprietarParcela implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "PROPRIETAR_PARCELA_ID_PROPRIETAR_PARCELA_GENERATOR", sequenceName = "SEQ_PROPRIETAR_PARCELA", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROPRIETAR_PARCELA_ID_PROPRIETAR_PARCELA_GENERATOR")
    @Column(name = "ID_PROPRIETAR_PARCELA")
    private Long idProprietarParcela;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;


    //bi-directional many-to-one association to ParcelaTeren
    @ManyToOne
    @JoinColumn(name = "FK_PARCELA_TEREN")
    private ParcelaTeren parcelaTeren;

    //bi-directional many-to-one association to PersoanaFizica
    @ManyToOne
    @JoinColumn(name = "FK_PERSOANA_FIZICA")
    private PersoanaFizica persoanaFizica;

    public ProprietarParcela() {
    }

    public Long getIdProprietarParcela() {
        return this.idProprietarParcela;
    }

    public void setIdProprietarParcela(Long idProprietarParcela) {
        this.idProprietarParcela = idProprietarParcela;
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

    public PersoanaFizica getPersoanaFizica() {
        return this.persoanaFizica;
    }

    public void setPersoanaFizica(PersoanaFizica persoanaFizica) {
        this.persoanaFizica = persoanaFizica;
    }

}