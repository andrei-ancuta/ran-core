package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the PERSOANA_RC database table.
 */
@Entity
@Table(name = "PERSOANA_RC")
@NamedQuery(name = "PersoanaRc.findAll", query = "SELECT p FROM PersoanaRc p")
public class PersoanaRc implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "PERSOANA_RC_IDPERSOANARC_GENERATOR", sequenceName = "SEQ_PERSOANA_RC", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PERSOANA_RC_IDPERSOANARC_GENERATOR")
    @Column(name = "ID_PERSOANA_RC")
    private Long idPersoanaRc;

    @Column(name = "BASE_ID")
    private Long baseId;

    private String cif;

    private String cui;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_START")
    private Date dataStart;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_STOP")
    private Date dataStop;

    private String denumire;

    //bi-directional many-to-one association to Contract
    @OneToMany(mappedBy = "persoanaRc")
    private List<Contract> contracts = new ArrayList<>();

    //bi-directional many-to-one association to DetinatorPf
    @OneToMany(mappedBy = "persoanaRc")
    private List<DetinatorPf> detinatorPfs = new ArrayList<>();

    //bi-directional many-to-one association to DetinatorPj
    @OneToMany(mappedBy = "persoanaRc")
    private List<DetinatorPj> detinatorPjs = new ArrayList<>();

    //bi-directional many-to-one association to PersoanaPreemptiune
    @OneToMany(mappedBy = "persoanaRc")
    private List<PersoanaPreemptiune> persoanaPreemptiunes = new ArrayList<>();

    //bi-directional many-to-one association to NomFormaOrganizareRc
    @ManyToOne
    @JoinColumn(name = "FK_NOM_FORMA_ORGANIZARE_RC")
    private NomFormaOrganizareRc nomFormaOrganizareRc;

    public PersoanaRc() {
    }

    public Long getIdPersoanaRc() {
        return this.idPersoanaRc;
    }

    public void setIdPersoanaRc(Long idPersoanaRc) {
        this.idPersoanaRc = idPersoanaRc;
    }

    public Long getBaseId() {
        return this.baseId;
    }

    public void setBaseId(Long baseId) {
        this.baseId = baseId;
    }

    public String getCif() {
        return this.cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getCui() {
        return this.cui;
    }

    public void setCui(String cui) {
        this.cui = cui;
    }

    public Date getDataStart() {
        return this.dataStart;
    }

    public void setDataStart(Date dataStart) {
        this.dataStart = dataStart;
    }

    public Date getDataStop() {
        return this.dataStop;
    }

    public void setDataStop(Date dataStop) {
        this.dataStop = dataStop;
    }

    public String getDenumire() {
        return this.denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public List<Contract> getContracts() {
        return this.contracts;
    }

    public void setContracts(List<Contract> contracts) {
        this.contracts = contracts;
    }

    public Contract addContract(Contract contract) {
        getContracts().add(contract);
        contract.setPersoanaRc(this);

        return contract;
    }

    public Contract removeContract(Contract contract) {
        getContracts().remove(contract);
        contract.setPersoanaRc(null);

        return contract;
    }

    public List<DetinatorPf> getDetinatorPfs() {
        return this.detinatorPfs;
    }

    public void setDetinatorPfs(List<DetinatorPf> detinatorPfs) {
        this.detinatorPfs = detinatorPfs;
    }

    public DetinatorPf addDetinatorPf(DetinatorPf detinatorPf) {
        getDetinatorPfs().add(detinatorPf);
        detinatorPf.setPersoanaRc(this);

        return detinatorPf;
    }

    public DetinatorPf removeDetinatorPf(DetinatorPf detinatorPf) {
        getDetinatorPfs().remove(detinatorPf);
        detinatorPf.setPersoanaRc(null);

        return detinatorPf;
    }

    public List<DetinatorPj> getDetinatorPjs() {
        return this.detinatorPjs;
    }

    public void setDetinatorPjs(List<DetinatorPj> detinatorPjs) {
        this.detinatorPjs = detinatorPjs;
    }

    public DetinatorPj addDetinatorPj(DetinatorPj detinatorPj) {
        getDetinatorPjs().add(detinatorPj);
        detinatorPj.setPersoanaRc(this);

        return detinatorPj;
    }

    public DetinatorPj removeDetinatorPj(DetinatorPj detinatorPj) {
        getDetinatorPjs().remove(detinatorPj);
        detinatorPj.setPersoanaRc(null);

        return detinatorPj;
    }

    public List<PersoanaPreemptiune> getPersoanaPreemptiunes() {
        return this.persoanaPreemptiunes;
    }

    public void setPersoanaPreemptiunes(List<PersoanaPreemptiune> persoanaPreemptiunes) {
        this.persoanaPreemptiunes = persoanaPreemptiunes;
    }

    public PersoanaPreemptiune addPersoanaPreemptiune(PersoanaPreemptiune persoanaPreemptiune) {
        getPersoanaPreemptiunes().add(persoanaPreemptiune);
        persoanaPreemptiune.setPersoanaRc(this);

        return persoanaPreemptiune;
    }

    public PersoanaPreemptiune removePersoanaPreemptiune(PersoanaPreemptiune persoanaPreemptiune) {
        getPersoanaPreemptiunes().remove(persoanaPreemptiune);
        persoanaPreemptiune.setPersoanaRc(null);

        return persoanaPreemptiune;
    }

    public NomFormaOrganizareRc getNomFormaOrganizareRc() {
        return this.nomFormaOrganizareRc;
    }

    public void setNomFormaOrganizareRc(NomFormaOrganizareRc nomFormaOrganizareRc) {
        this.nomFormaOrganizareRc = nomFormaOrganizareRc;
    }

}