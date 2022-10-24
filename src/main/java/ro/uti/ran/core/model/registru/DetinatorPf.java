package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the DETINATOR_PF database table.
 */
@Entity
@Table(name = "DETINATOR_PF")
@NamedQuery(name = "DetinatorPf.findAll", query = "SELECT d FROM DetinatorPf d")
public class DetinatorPf implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "DETINATOR_PF_IDDETINATORPF_GENERATOR", sequenceName = "SEQ_DETINATOR_PF", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DETINATOR_PF_IDDETINATORPF_GENERATOR")
    @Column(name = "ID_DETINATOR_PF")
    private Long idDetinatorPf;

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

    //bi-directional many-to-one association to MembruPf
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "detinatorPf",fetch = FetchType.EAGER)
    private List<MembruPf> membruPfs = new ArrayList<MembruPf>();

    public DetinatorPf() {
    }

    public Long getIdDetinatorPf() {
        return this.idDetinatorPf;
    }

    public void setIdDetinatorPf(Long idDetinatorPf) {
        this.idDetinatorPf = idDetinatorPf;
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

    public List<MembruPf> getMembruPfs() {
        return this.membruPfs;
    }

    public void setMembruPfs(List<MembruPf> membruPfs) {
        this.membruPfs = membruPfs;
    }

    public MembruPf addMembruPf(MembruPf membruPf) {
        getMembruPfs().add(membruPf);
        membruPf.setDetinatorPf(this);

        return membruPf;
    }

    public MembruPf removeMembruPf(MembruPf membruPf) {
        getMembruPfs().remove(membruPf);
        membruPf.setDetinatorPf(null);

        return membruPf;
    }

}