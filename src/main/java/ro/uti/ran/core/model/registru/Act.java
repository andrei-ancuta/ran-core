package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the ACT database table.
 */
@Entity
@NamedQuery(name = "Act.findAll", query = "SELECT a FROM Act a")
public class Act implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "ACT_IDACT_GENERATOR", sequenceName = "SEQ_ACT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACT_IDACT_GENERATOR")
    @Column(name = "ID_ACT")
    private Long idAct;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_ACT")
    private Date dataAct;

    private String emitent;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;

    @Column(name = "NUMAR_ACT")
    private String numarAct;

    //bi-directional many-to-one association to NomTipAct
    @ManyToOne
    @JoinColumn(name = "FK_NOM_TIP_ACT")
    private NomTipAct nomTipAct;

    //bi-directional many-to-one association to ActDetinere
    @OneToMany(mappedBy = "act",cascade = CascadeType.REMOVE)
    private List<ActDetinere> actDetineres = new ArrayList<>();

    //bi-directional many-to-one association to Atestat
    @OneToMany(mappedBy = "act",cascade = CascadeType.REMOVE)
    private List<Atestat> atestats = new ArrayList<>();

    //bi-directional many-to-one association to ParcelaTeren
    @OneToMany(mappedBy = "actInstrainare",cascade = CascadeType.REMOVE)
    private List<ParcelaTeren> parcelaTerens = new ArrayList<>();

    //bi-directional many-to-one association to Preemptiune
    @OneToMany(mappedBy = "actAvizMadrDadr",cascade = CascadeType.REMOVE)
    private List<Preemptiune> preemptiunes1 = new ArrayList<>();

    //bi-directional many-to-one association to Preemptiune
    @OneToMany(mappedBy = "actAdevVanzareLib",cascade = CascadeType.REMOVE)
    private List<Preemptiune> preemptiunes2 = new ArrayList<>();

    public Act() {
    }

    public Long getIdAct() {
        return this.idAct;
    }

    public void setIdAct(Long idAct) {
        this.idAct = idAct;
    }

    public Date getDataAct() {
        return this.dataAct;
    }

    public void setDataAct(Date dataAct) {
        this.dataAct = dataAct;
    }

    public String getEmitent() {
        return this.emitent;
    }

    public void setEmitent(String emitent) {
        this.emitent = emitent;
    }

    public Long getFkNomJudet() {
        return this.fkNomJudet;
    }

    public void setFkNomJudet(Long fkNomJudet) {
        this.fkNomJudet = fkNomJudet;
    }

    public String getNumarAct() {
        return this.numarAct;
    }

    public void setNumarAct(String numarAct) {
        this.numarAct = numarAct;
    }

    public NomTipAct getNomTipAct() {
        return this.nomTipAct;
    }

    public void setNomTipAct(NomTipAct nomTipAct) {
        this.nomTipAct = nomTipAct;
    }

    public List<ActDetinere> getActDetineres() {
        return this.actDetineres;
    }

    public void setActDetineres(List<ActDetinere> actDetineres) {
        this.actDetineres = actDetineres;
    }

    public ActDetinere addActDetinere(ActDetinere actDetinere) {
        getActDetineres().add(actDetinere);
        actDetinere.setAct(this);

        return actDetinere;
    }

    public ActDetinere removeActDetinere(ActDetinere actDetinere) {
        getActDetineres().remove(actDetinere);
        actDetinere.setAct(null);

        return actDetinere;
    }

    public List<Atestat> getAtestats() {
        return this.atestats;
    }

    public void setAtestats(List<Atestat> atestats) {
        this.atestats = atestats;
    }

    public Atestat addAtestat(Atestat atestat) {
        getAtestats().add(atestat);
        atestat.setAct(this);

        return atestat;
    }

    public Atestat removeAtestat(Atestat atestat) {
        getAtestats().remove(atestat);
        atestat.setAct(null);

        return atestat;
    }

    public List<ParcelaTeren> getParcelaTerens() {
        return this.parcelaTerens;
    }

    public void setParcelaTerens(List<ParcelaTeren> parcelaTerens) {
        this.parcelaTerens = parcelaTerens;
    }

    public ParcelaTeren addParcelaTeren(ParcelaTeren parcelaTeren) {
        getParcelaTerens().add(parcelaTeren);
        parcelaTeren.setActInstrainare(this);

        return parcelaTeren;
    }

    public ParcelaTeren removeParcelaTeren(ParcelaTeren parcelaTeren) {
        getParcelaTerens().remove(parcelaTeren);
        parcelaTeren.setActInstrainare(null);

        return parcelaTeren;
    }

    public List<Preemptiune> getPreemptiunes1() {
        return this.preemptiunes1;
    }

    public void setPreemptiunes1(List<Preemptiune> preemptiunes1) {
        this.preemptiunes1 = preemptiunes1;
    }

    public Preemptiune addPreemptiunes1(Preemptiune preemptiunes1) {
        getPreemptiunes1().add(preemptiunes1);
        preemptiunes1.setActAvizMadrDadr(this);

        return preemptiunes1;
    }

    public Preemptiune removePreemptiunes1(Preemptiune preemptiunes1) {
        getPreemptiunes1().remove(preemptiunes1);
        preemptiunes1.setActAvizMadrDadr(null);

        return preemptiunes1;
    }

    public List<Preemptiune> getPreemptiunes2() {
        return this.preemptiunes2;
    }

    public void setPreemptiunes2(List<Preemptiune> preemptiunes2) {
        this.preemptiunes2 = preemptiunes2;
    }

    public Preemptiune addPreemptiunes2(Preemptiune preemptiunes2) {
        getPreemptiunes2().add(preemptiunes2);
        preemptiunes2.setActAdevVanzareLib(this);

        return preemptiunes2;
    }

    public Preemptiune removePreemptiunes2(Preemptiune preemptiunes2) {
        getPreemptiunes2().remove(preemptiunes2);
        preemptiunes2.setActAdevVanzareLib(null);

        return preemptiunes2;
    }

}