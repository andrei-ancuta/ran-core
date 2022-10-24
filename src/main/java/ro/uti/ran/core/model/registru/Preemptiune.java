package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the PREEMPTIUNE database table.
 */
@Entity
@NamedQuery(name = "Preemptiune.findAll", query = "SELECT p FROM Preemptiune p")
public class Preemptiune implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "PREEMPTIUNE_IDPREEMPTIUNE_GENERATOR", sequenceName = "SEQ_PREEMPTIUNE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PREEMPTIUNE_IDPREEMPTIUNE_GENERATOR")
    @Column(name = "ID_PREEMPTIUNE")
    private Long idPreemptiune;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_OFERTA_VANZARE")
    private Date dataOfertaVanzare;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;


    @Column(name = "NR_CARTE_FUNCIARA")
    private String nrCarteFunciara;

    @Column(name = "NR_OFERTA_VANZARE")
    private String nrOfertaVanzare;

    @Column(name = "PRET_OFERTA_VANZARE")
    private Integer pretOfertaVanzare;

    private Integer suprafata;

    //bi-directional many-to-one association to PersoanaPreemptiune
    @OneToMany(mappedBy = "preemptiune", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PersoanaPreemptiune> persoanaPreemptiunes = new ArrayList<>();

    //bi-directional many-to-one association to Act
    @ManyToOne
    @JoinColumn(name = "FK_ACT_AVIZ_MADR_DADR")
    private Act actAvizMadrDadr;

    //bi-directional many-to-one association to Act
    @ManyToOne
    @JoinColumn(name = "FK_ACT_ADEV_VANZARE_LIB")
    private Act actAdevVanzareLib;

    //bi-directional many-to-one association to Gospodarie
    @ManyToOne
    @JoinColumn(name = "FK_GOSPODARIE")
    private Gospodarie gospodarie;

    public Preemptiune() {
    }

    public Long getIdPreemptiune() {
        return this.idPreemptiune;
    }

    public void setIdPreemptiune(Long idPreemptiune) {
        this.idPreemptiune = idPreemptiune;
    }

    public Date getDataOfertaVanzare() {
        return this.dataOfertaVanzare;
    }

    public void setDataOfertaVanzare(Date dataOfertaVanzare) {
        this.dataOfertaVanzare = dataOfertaVanzare;
    }

    public Long getFkNomJudet() {
        return this.fkNomJudet;
    }

    public void setFkNomJudet(Long fkNomJudet) {
        this.fkNomJudet = fkNomJudet;
    }


    public String getNrCarteFunciara() {
        return this.nrCarteFunciara;
    }

    public void setNrCarteFunciara(String nrCarteFunciara) {
        this.nrCarteFunciara = nrCarteFunciara;
    }

    public String getNrOfertaVanzare() {
        return this.nrOfertaVanzare;
    }

    public void setNrOfertaVanzare(String nrOfertaVanzare) {
        this.nrOfertaVanzare = nrOfertaVanzare;
    }

    public Integer getPretOfertaVanzare() {
        return this.pretOfertaVanzare;
    }

    public void setPretOfertaVanzare(Integer pretOfertaVanzare) {
        this.pretOfertaVanzare = pretOfertaVanzare;
    }

    public Integer getSuprafata() {
        return this.suprafata;
    }

    public void setSuprafata(Integer suprafata) {
        this.suprafata = suprafata;
    }

    public List<PersoanaPreemptiune> getPersoanaPreemptiunes() {
        return this.persoanaPreemptiunes;
    }

    public void setPersoanaPreemptiunes(List<PersoanaPreemptiune> persoanaPreemptiunes) {
        this.persoanaPreemptiunes = persoanaPreemptiunes;
    }

    public PersoanaPreemptiune addPersoanaPreemptiune(PersoanaPreemptiune persoanaPreemptiune) {
        getPersoanaPreemptiunes().add(persoanaPreemptiune);
        persoanaPreemptiune.setPreemptiune(this);

        return persoanaPreemptiune;
    }

    public PersoanaPreemptiune removePersoanaPreemptiune(PersoanaPreemptiune persoanaPreemptiune) {
        getPersoanaPreemptiunes().remove(persoanaPreemptiune);
        persoanaPreemptiune.setPreemptiune(null);

        return persoanaPreemptiune;
    }

    public Act getActAvizMadrDadr() {
        return actAvizMadrDadr;
    }

    public void setActAvizMadrDadr(Act actAvizMadrDadr) {
        this.actAvizMadrDadr = actAvizMadrDadr;
    }

    public Act getActAdevVanzareLib() {
        return actAdevVanzareLib;
    }

    public void setActAdevVanzareLib(Act actAdevVanzareLib) {
        this.actAdevVanzareLib = actAdevVanzareLib;
    }

    public Gospodarie getGospodarie() {
        return this.gospodarie;
    }

    public void setGospodarie(Gospodarie gospodarie) {
        this.gospodarie = gospodarie;
    }

}