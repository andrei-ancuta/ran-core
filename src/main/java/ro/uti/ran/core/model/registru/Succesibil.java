package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the SUCCESIBIL database table.
 */
@Entity
@NamedQuery(name = "Succesibil.findAll", query = "SELECT s FROM Succesibil s")
public class Succesibil implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "SUCCESIBIL_IDSUCCESIBIL_GENERATOR", sequenceName = "SEQ_SUCCESIBIL", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUCCESIBIL_IDSUCCESIBIL_GENERATOR")
    @Column(name = "ID_SUCCESIBIL")
    private Long idSuccesibil;


    //bi-directional many-to-one association to Adresa
    @ManyToOne
    @JoinColumn(name = "FK_ADRESA")
    private Adresa adresa;

    //bi-directional many-to-one association to MentiuneCerereSuc
    @ManyToOne
    @JoinColumn(name = "FK_MENTIUNE_CERERE_SUC")
    private MentiuneCerereSuc mentiuneCerereSuc;

    //bi-directional many-to-one association to PersoanaFizica
    @ManyToOne
    @JoinColumn(name = "FK_PERSOANA_FIZICA")
    private PersoanaFizica persoanaFizica;

    public Succesibil() {
    }

    public Long getIdSuccesibil() {
        return this.idSuccesibil;
    }

    public void setIdSuccesibil(Long idSuccesibil) {
        this.idSuccesibil = idSuccesibil;
    }


    public Adresa getAdresa() {
        return this.adresa;
    }

    public void setAdresa(Adresa adresa) {
        this.adresa = adresa;
    }

    public MentiuneCerereSuc getMentiuneCerereSuc() {
        return this.mentiuneCerereSuc;
    }

    public void setMentiuneCerereSuc(MentiuneCerereSuc mentiuneCerereSuc) {
        this.mentiuneCerereSuc = mentiuneCerereSuc;
    }

    public PersoanaFizica getPersoanaFizica() {
        return this.persoanaFizica;
    }

    public void setPersoanaFizica(PersoanaFizica persoanaFizica) {
        this.persoanaFizica = persoanaFizica;
    }

}