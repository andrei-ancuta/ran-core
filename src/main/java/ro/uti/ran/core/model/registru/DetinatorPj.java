package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the DETINATOR_PJ database table.
 */
@Entity
@Table(name = "DETINATOR_PJ")
@NamedQuery(name = "DetinatorPj.findAll", query = "SELECT d FROM DetinatorPj d")
public class DetinatorPj implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "DETINATOR_PJ_IDDETINATORPJ_GENERATOR", sequenceName = "SEQ_DETINATOR_PJ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DETINATOR_PJ_IDDETINATORPJ_GENERATOR")
    @Column(name = "ID_DETINATOR_PJ")
    private Long idDetinatorPj;

    @Column(name = "DENUMIRE_SUBDIVIZIUNE")
    private String denumireSubdiviziune;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;


    //bi-directional many-to-one association to Gospodarie
    @ManyToOne
    @JoinColumn(name = "FK_GOSPODARIE")
    private Gospodarie gospodarie;

    //bi-directional many-to-one association to PersoanaFizica
    @ManyToOne
    @JoinColumn(name = "FK_PERSOANA_FIZICA")
    private PersoanaFizica persoanaFizica;

    //bi-directional many-to-one association to PersoanaRc
    @ManyToOne
    @JoinColumn(name = "FK_PERSOANA_RC")
    private PersoanaRc persoanaRc;

    public DetinatorPj() {
    }

    public Long getIdDetinatorPj() {
        return this.idDetinatorPj;
    }

    public void setIdDetinatorPj(Long idDetinatorPj) {
        this.idDetinatorPj = idDetinatorPj;
    }

    public String getDenumireSubdiviziune() {
        return this.denumireSubdiviziune;
    }

    public void setDenumireSubdiviziune(String denumireSubdiviziune) {
        this.denumireSubdiviziune = denumireSubdiviziune;
    }

    public Long getFkNomJudet() {
        return this.fkNomJudet;
    }

    public void setFkNomJudet(Long fkNomJudet) {
        this.fkNomJudet = fkNomJudet;
    }


    public Gospodarie getGospodarie() {
        return this.gospodarie;
    }

    public void setGospodarie(Gospodarie gospodarie) {
        this.gospodarie = gospodarie;
    }

    public PersoanaFizica getPersoanaFizica() {
        return this.persoanaFizica;
    }

    public void setPersoanaFizica(PersoanaFizica persoanaFizica) {
        this.persoanaFizica = persoanaFizica;
    }

    public PersoanaRc getPersoanaRc() {
        return this.persoanaRc;
    }

    public void setPersoanaRc(PersoanaRc persoanaRc) {
        this.persoanaRc = persoanaRc;
    }

}