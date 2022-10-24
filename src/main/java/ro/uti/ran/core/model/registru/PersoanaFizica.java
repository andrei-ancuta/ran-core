package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the PERSOANA_FIZICA database table.
 */
@Entity
@Table(name = "PERSOANA_FIZICA")
@NamedQuery(name = "PersoanaFizica.findAll", query = "SELECT p FROM PersoanaFizica p")
public class PersoanaFizica implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "PERSOANA_FIZICA_IDPERSOANAFIZICA_GENERATOR", sequenceName = "SEQ_PERSOANA_FIZICA", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PERSOANA_FIZICA_IDPERSOANAFIZICA_GENERATOR")
    @Column(name = "ID_PERSOANA_FIZICA")
    private Long idPersoanaFizica;

    @Column(name = "BASE_ID")
    private Long baseId;

    private String cnp;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_START")
    private Date dataStart;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_STOP")
    private Date dataStop;

    @Column(name = "INITIALA_TATA")
    private String initialaTata;


    private String nif;

    private String nume;

    private String prenume;

    //bi-directional many-to-one association to DetinatorPf
    @OneToMany(mappedBy = "persoanaFizica")
    private List<DetinatorPf> detinatorPfs = new ArrayList<>();

    //bi-directional many-to-one association to MembruPf
    @OneToMany(mappedBy = "persoanaFizica")
    private List<MembruPf> membruPfs = new ArrayList<>();

    //bi-directional many-to-one association to ProprietarParcela
    @OneToMany(mappedBy = "persoanaFizica")
    private List<ProprietarParcela> proprietarParcelas = new ArrayList<>();


    //bi-directional many-to-one association to MentiuneCerereSuc
    @OneToMany(mappedBy = "persoanaFizica")
    private List<MentiuneCerereSuc> mentiuneCerereSucs = new ArrayList<>();


    //bi-directional many-to-one association to Succesibil
    @OneToMany(mappedBy = "persoanaFizica")
    private List<Succesibil> succesibils = new ArrayList<>();


    //bi-directional many-to-one association to PersoanaPreemptiune
    @OneToMany(mappedBy = "persoanaFizica")
    private List<PersoanaPreemptiune> persoanaPreemptiunes = new ArrayList<>();


    //bi-directional many-to-one association to Contract
    @OneToMany(mappedBy = "persoanaFizica")
    private List<Contract> contracts = new ArrayList<>();


    //bi-directional many-to-one association to DetinatorPj
    @OneToMany(mappedBy = "persoanaFizica")
    private List<DetinatorPj> detinatorPjs = new ArrayList<>();


    public PersoanaFizica() {
    }

    public Long getIdPersoanaFizica() {
        return this.idPersoanaFizica;
    }

    public void setIdPersoanaFizica(Long idPersoanaFizica) {
        this.idPersoanaFizica = idPersoanaFizica;
    }

    public Long getBaseId() {
        return this.baseId;
    }

    public void setBaseId(Long baseId) {
        this.baseId = baseId;
    }

    public String getCnp() {
        return this.cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
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

    public String getInitialaTata() {
        return this.initialaTata;
    }

    public void setInitialaTata(String initialaTata) {
        this.initialaTata = initialaTata;
    }


    public String getNif() {
        return this.nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getNume() {
        return this.nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return this.prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public List<Contract> getContracts() {
        return this.contracts;
    }

    public void setContracts(List<Contract> contracts) {
        this.contracts = contracts;
    }

    public Contract addContract(Contract contract) {
        getContracts().add(contract);
        contract.setPersoanaFizica(this);

        return contract;
    }

    public Contract removeContract(Contract contract) {
        getContracts().remove(contract);
        contract.setPersoanaFizica(null);

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
        detinatorPf.setPersoanaFizica(this);

        return detinatorPf;
    }

    public DetinatorPf removeDetinatorPf(DetinatorPf detinatorPf) {
        getDetinatorPfs().remove(detinatorPf);
        detinatorPf.setPersoanaFizica(null);

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
        detinatorPj.setPersoanaFizica(this);

        return detinatorPj;
    }

    public DetinatorPj removeDetinatorPj(DetinatorPj detinatorPj) {
        getDetinatorPjs().remove(detinatorPj);
        detinatorPj.setPersoanaFizica(null);

        return detinatorPj;
    }

    public List<MembruPf> getMembruPfs() {
        return this.membruPfs;
    }

    public void setMembruPfs(List<MembruPf> membruPfs) {
        this.membruPfs = membruPfs;
    }

    public MembruPf addMembruPf(MembruPf membruPf) {
        getMembruPfs().add(membruPf);
        membruPf.setPersoanaFizica(this);

        return membruPf;
    }

    public MembruPf removeMembruPf(MembruPf membruPf) {
        getMembruPfs().remove(membruPf);
        membruPf.setPersoanaFizica(null);

        return membruPf;
    }

    public List<MentiuneCerereSuc> getMentiuneCerereSucs() {
        return this.mentiuneCerereSucs;
    }

    public void setMentiuneCerereSucs(List<MentiuneCerereSuc> mentiuneCerereSucs) {
        this.mentiuneCerereSucs = mentiuneCerereSucs;
    }

    public MentiuneCerereSuc addMentiuneCerereSuc(MentiuneCerereSuc mentiuneCerereSuc) {
        getMentiuneCerereSucs().add(mentiuneCerereSuc);
        mentiuneCerereSuc.setPersoanaFizica(this);

        return mentiuneCerereSuc;
    }

    public MentiuneCerereSuc removeMentiuneCerereSuc(MentiuneCerereSuc mentiuneCerereSuc) {
        getMentiuneCerereSucs().remove(mentiuneCerereSuc);
        mentiuneCerereSuc.setPersoanaFizica(null);

        return mentiuneCerereSuc;
    }

    public List<PersoanaPreemptiune> getPersoanaPreemptiunes() {
        return this.persoanaPreemptiunes;
    }

    public void setPersoanaPreemptiunes(List<PersoanaPreemptiune> persoanaPreemptiunes) {
        this.persoanaPreemptiunes = persoanaPreemptiunes;
    }

    public PersoanaPreemptiune addPersoanaPreemptiune(PersoanaPreemptiune persoanaPreemptiune) {
        getPersoanaPreemptiunes().add(persoanaPreemptiune);
        persoanaPreemptiune.setPersoanaFizica(this);

        return persoanaPreemptiune;
    }

    public PersoanaPreemptiune removePersoanaPreemptiune(PersoanaPreemptiune persoanaPreemptiune) {
        getPersoanaPreemptiunes().remove(persoanaPreemptiune);
        persoanaPreemptiune.setPersoanaFizica(null);

        return persoanaPreemptiune;
    }

    public List<ProprietarParcela> getProprietarParcelas() {
        return this.proprietarParcelas;
    }

    public void setProprietarParcelas(List<ProprietarParcela> proprietarParcelas) {
        this.proprietarParcelas = proprietarParcelas;
    }

    public ProprietarParcela addProprietarParcela(ProprietarParcela proprietarParcela) {
        getProprietarParcelas().add(proprietarParcela);
        proprietarParcela.setPersoanaFizica(this);

        return proprietarParcela;
    }

    public ProprietarParcela removeProprietarParcela(ProprietarParcela proprietarParcela) {
        getProprietarParcelas().remove(proprietarParcela);
        proprietarParcela.setPersoanaFizica(null);

        return proprietarParcela;
    }

    public List<Succesibil> getSuccesibils() {
        return this.succesibils;
    }

    public void setSuccesibils(List<Succesibil> succesibils) {
        this.succesibils = succesibils;
    }

    public Succesibil addSuccesibil(Succesibil succesibil) {
        getSuccesibils().add(succesibil);
        succesibil.setPersoanaFizica(this);

        return succesibil;
    }

    public Succesibil removeSuccesibil(Succesibil succesibil) {
        getSuccesibils().remove(succesibil);
        succesibil.setPersoanaFizica(null);

        return succesibil;
    }

}