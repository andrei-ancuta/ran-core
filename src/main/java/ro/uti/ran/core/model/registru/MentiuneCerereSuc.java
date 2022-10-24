package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the MENTIUNE_CERERE_SUC database table.
 */
@Entity
@Table(name = "MENTIUNE_CERERE_SUC")
@NamedQuery(name = "MentiuneCerereSuc.findAll", query = "SELECT m FROM MentiuneCerereSuc m")
public class MentiuneCerereSuc implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "MENTIUNE_CERERE_SUC_IDMENTIUNECERERESUC_GENERATOR", sequenceName = "SEQ_MENTIUNE_CERERE_SUC", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MENTIUNE_CERERE_SUC_IDMENTIUNECERERESUC_GENERATOR")
    @Column(name = "ID_MENTIUNE_CERERE_SUC")
    private Long idMentiuneCerereSuc;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_DECES")
    private Date dataDeces;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_INREGISTRARE")
    private Date dataInregistrare;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;


    @Column(name = "NR_INREGISTRARE")
    private String nrInregistrare;

    @Column(name = "SPN_BIN")
    private String spnBin;

    //bi-directional many-to-one association to Gospodarie
    @ManyToOne
    @JoinColumn(name = "FK_GOSPODARIE")
    private Gospodarie gospodarie;

    //bi-directional many-to-one association to PersoanaFizica
    @ManyToOne
    @JoinColumn(name = "FK_PERSOANA_FIZICA")
    private PersoanaFizica persoanaFizica;

    //bi-directional many-to-one association to Succesibil
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mentiuneCerereSuc", fetch = FetchType.EAGER)
    private List<Succesibil> succesibils = new ArrayList<Succesibil>();

    public MentiuneCerereSuc() {
    }

    public Long getIdMentiuneCerereSuc() {
        return this.idMentiuneCerereSuc;
    }

    public void setIdMentiuneCerereSuc(Long idMentiuneCerereSuc) {
        this.idMentiuneCerereSuc = idMentiuneCerereSuc;
    }

    public Date getDataDeces() {
        return this.dataDeces;
    }

    public void setDataDeces(Date dataDeces) {
        this.dataDeces = dataDeces;
    }

    public Date getDataInregistrare() {
        return this.dataInregistrare;
    }

    public void setDataInregistrare(Date dataInregistrare) {
        this.dataInregistrare = dataInregistrare;
    }

    public Long getFkNomJudet() {
        return this.fkNomJudet;
    }

    public void setFkNomJudet(Long fkNomJudet) {
        this.fkNomJudet = fkNomJudet;
    }


    public String getNrInregistrare() {
        return this.nrInregistrare;
    }

    public void setNrInregistrare(String nrInregistrare) {
        this.nrInregistrare = nrInregistrare;
    }

    public String getSpnBin() {
        return this.spnBin;
    }

    public void setSpnBin(String spnBin) {
        this.spnBin = spnBin;
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

    public List<Succesibil> getSuccesibils() {
        return this.succesibils;
    }

    public void setSuccesibils(List<Succesibil> succesibils) {
        this.succesibils = succesibils;
    }

    public Succesibil addSuccesibil(Succesibil succesibil) {
        getSuccesibils().add(succesibil);
        succesibil.setMentiuneCerereSuc(this);

        return succesibil;
    }

    public Succesibil removeSuccesibil(Succesibil succesibil) {
        getSuccesibils().remove(succesibil);
        succesibil.setMentiuneCerereSuc(null);

        return succesibil;
    }

}