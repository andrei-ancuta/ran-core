package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the MEMBRU_PF database table.
 */
@Entity
@Table(name = "MEMBRU_PF")
@NamedQuery(name = "MembruPf.findAll", query = "SELECT m FROM MembruPf m")
public class MembruPf implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "MEMBRU_PF_IDMEMBRUPF_GENERATOR", sequenceName = "SEQ_MEMBRU_PF", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBRU_PF_IDMEMBRUPF_GENERATOR")
    @Column(name = "ID_MEMBRU_PF")
    private Long idMembruPf;

    @Column(name = "COD_RAND")
    private Integer codRand;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;


    private String mentiune;

    //bi-directional many-to-one association to DetinatorPf
    @ManyToOne
    @JoinColumn(name = "FK_DETINATOR_PF")
    private DetinatorPf detinatorPf;

    //bi-directional many-to-one association to NomLegaturaRudenie
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_NOM_LEGATURA_RUDENIE")
    private NomLegaturaRudenie nomLegaturaRudenie;

    //bi-directional many-to-one association to PersoanaFizica
    @ManyToOne
    @JoinColumn(name = "FK_PERSOANA_FIZICA")
    private PersoanaFizica persoanaFizica;

    public MembruPf() {
    }

    public Long getIdMembruPf() {
        return this.idMembruPf;
    }

    public void setIdMembruPf(Long idMembruPf) {
        this.idMembruPf = idMembruPf;
    }

    public Integer getCodRand() {
        return this.codRand;
    }

    public void setCodRand(Integer codRand) {
        this.codRand = codRand;
    }

    public Long getFkNomJudet() {
        return this.fkNomJudet;
    }

    public void setFkNomJudet(Long fkNomJudet) {
        this.fkNomJudet = fkNomJudet;
    }


    public String getMentiune() {
        return this.mentiune;
    }

    public void setMentiune(String mentiune) {
        this.mentiune = mentiune;
    }

    public DetinatorPf getDetinatorPf() {
        return this.detinatorPf;
    }

    public void setDetinatorPf(DetinatorPf detinatorPf) {
        this.detinatorPf = detinatorPf;
    }

    public NomLegaturaRudenie getNomLegaturaRudenie() {
        return this.nomLegaturaRudenie;
    }

    public void setNomLegaturaRudenie(NomLegaturaRudenie nomLegaturaRudenie) {
        this.nomLegaturaRudenie = nomLegaturaRudenie;
    }

    public PersoanaFizica getPersoanaFizica() {
        return this.persoanaFizica;
    }

    public void setPersoanaFizica(PersoanaFizica persoanaFizica) {
        this.persoanaFizica = persoanaFizica;
    }

}