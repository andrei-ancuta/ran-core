package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the PARCELA_LOCALIZARE database table.
 */
@Entity
@Table(name = "PARCELA_LOCALIZARE")
@NamedQuery(name = "ParcelaLocalizare.findAll", query = "SELECT p FROM ParcelaLocalizare p")
public class ParcelaLocalizare implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "PARCELA_LOCALIZARE_IDPARCELALOCALIZARE_GENERATOR", sequenceName = "SEQ_PARCELA_LOCALIZARE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PARCELA_LOCALIZARE_IDPARCELALOCALIZARE_GENERATOR")
    @Column(name = "ID_PARCELA_LOCALIZARE")
    private Long idParcelaLocalizare;

    private String valoare;

    //bi-directional many-to-one association to NomTipLocalizare
    @ManyToOne
    @JoinColumn(name = "FK_NOM_TIP_LOCALIZARE")
    private NomTipLocalizare nomTipLocalizare;

    //bi-directional many-to-one association to ParcelaTeren
    @ManyToOne
    @JoinColumn(name = "FK_PARCELA_TEREN")
    private ParcelaTeren parcelaTeren;

    public ParcelaLocalizare() {
    }

    public Long getIdParcelaLocalizare() {
        return this.idParcelaLocalizare;
    }

    public void setIdParcelaLocalizare(Long idParcelaLocalizare) {
        this.idParcelaLocalizare = idParcelaLocalizare;
    }

    public String getValoare() {
        return this.valoare;
    }

    public void setValoare(String valoare) {
        this.valoare = valoare;
    }

    public NomTipLocalizare getNomTipLocalizare() {
        return this.nomTipLocalizare;
    }

    public void setNomTipLocalizare(NomTipLocalizare nomTipLocalizare) {
        this.nomTipLocalizare = nomTipLocalizare;
    }

    public ParcelaTeren getParcelaTeren() {
        return this.parcelaTeren;
    }

    public void setParcelaTeren(ParcelaTeren parcelaTeren) {
        this.parcelaTeren = parcelaTeren;
    }

}