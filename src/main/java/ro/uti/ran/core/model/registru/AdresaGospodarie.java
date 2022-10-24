package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the ADRESA_GOSPODARIE database table.
 */
@Entity
@Table(name = "ADRESA_GOSPODARIE")
@NamedQuery(name = "AdresaGospodarie.findAll", query = "SELECT a FROM AdresaGospodarie a")
public class AdresaGospodarie implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "ADRESAGOSPODARIE_IDADRESAGOSPODARIE_GENERATOR", sequenceName = "SEQ_ADRESA_GOSPODARIE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADRESAGOSPODARIE_IDADRESAGOSPODARIE_GENERATOR")
    @Column(name = "ID_ADRESA_GOSPODARIE")
    private Long idAdresaGospodarie;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;


    //bi-directional many-to-one association to Adresa
    @ManyToOne
    @JoinColumn(name = "FK_ADRESA")
    private Adresa adresa;

    //bi-directional many-to-one association to Gospodarie
    @ManyToOne
    @JoinColumn(name = "FK_GOSPODARIE")
    private Gospodarie gospodarie;

    //bi-directional many-to-one association to NomTipAdresa
    @ManyToOne
    @JoinColumn(name = "FK_NOM_TIP_ADRESA")
    private NomTipAdresa nomTipAdresa;

    public AdresaGospodarie() {
    }

    public Long getIdAdresaGospodarie() {
        return this.idAdresaGospodarie;
    }

    public void setIdAdresaGospodarie(Long idAdresaGospodarie) {
        this.idAdresaGospodarie = idAdresaGospodarie;
    }

    public Long getFkNomJudet() {
        return this.fkNomJudet;
    }

    public void setFkNomJudet(Long fkNomJudet) {
        this.fkNomJudet = fkNomJudet;
    }


    public Adresa getAdresa() {
        return this.adresa;
    }

    public void setAdresa(Adresa adresa) {
        this.adresa = adresa;
    }

    public Gospodarie getGospodarie() {
        return this.gospodarie;
    }

    public void setGospodarie(Gospodarie gospodarie) {
        this.gospodarie = gospodarie;
    }

    public NomTipAdresa getNomTipAdresa() {
        return this.nomTipAdresa;
    }

    public void setNomTipAdresa(NomTipAdresa nomTipAdresa) {
        this.nomTipAdresa = nomTipAdresa;
    }

}