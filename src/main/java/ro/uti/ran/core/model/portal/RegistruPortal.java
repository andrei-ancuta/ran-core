package ro.uti.ran.core.model.portal;

import ro.uti.ran.core.model.Model;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Date;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-17 12:12
 */
@Entity
@Table(name = "REGISTRU")
public class RegistruPortal extends Model{
    @Id
    @GeneratedValue(generator = "RegistruSeq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "RegistruSeq", sequenceName = "SEQ_REGISTRU", allocationSize = 1)
    @Column(name = "ID_REGISTRU", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FK_INCARCARE", referencedColumnName = "ID_INCARCARE", nullable = false)
    private Incarcare incarcare;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="FK_NOM_STARE_REGISTRU", referencedColumnName = "ID_NOM_STARE_REGISTRU", nullable = false)
    private StareRegistru stareRegistru;

    @Column(name = "INDEX_REGISTRU")
    private String indexRegistru;

    @Column(name = "DATA_REGISTRU")
    private Date dataRegistru;

    @Column(name = "DENUMIRE_FISIER")
    private String denumireFisier;

    @Lob
    @Column(name = "CONTINUT_FISIER")
    private String continutFisier;

    @Lob
    @Column(name = "RECIPISA")
    private byte[] recipisa;

    @Column(name = "IS_RECIPISA_SEMNATA")
    private Boolean isRecipisaSemnata;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FK_NOM_CAPITOL", referencedColumnName = "ID_NOM_CAPITOL", nullable = false)
    private NomCapitolPortal nomCapitolPortal;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Incarcare getIncarcare() {
        return incarcare;
    }

    public void setIncarcare(Incarcare incarcare) {
        this.incarcare = incarcare;
    }

    public StareRegistru getStareRegistru() {
        return stareRegistru;
    }

    public void setStareRegistru(StareRegistru stareRegistru) {
        this.stareRegistru = stareRegistru;
    }

    public String getIndexRegistru() {
        return indexRegistru;
    }

    public void setIndexRegistru(String indexRegistru) {
        this.indexRegistru = indexRegistru;
    }

    public Date getDataRegistru() {
        return dataRegistru;
    }

    public void setDataRegistru(Date dataRegistru) {
        this.dataRegistru = dataRegistru;
    }

    public String getDenumireFisier() {
        return denumireFisier;
    }

    public void setDenumireFisier(String denumireFisier) {
        this.denumireFisier = denumireFisier;
    }

    public String getContinutFisier() {
        return continutFisier;
    }

    public void setContinutFisier(String continutFisier) {
        this.continutFisier = continutFisier;
    }

    public byte[] getRecipisa() {
        return recipisa;
    }

    public void setRecipisa(byte[] recipisa) {
        this.recipisa = recipisa;
    }

    public Boolean getIsRecipisaSemnata() {
        return isRecipisaSemnata;
    }

    public void setIsRecipisaSemnata(Boolean isRecipisaSemnata) {
        this.isRecipisaSemnata = isRecipisaSemnata;
    }

    public NomCapitolPortal getNomCapitolPortal() {
        return nomCapitolPortal;
    }

    public void setNomCapitolPortal(NomCapitolPortal nomCapitolPortal) {
        this.nomCapitolPortal = nomCapitolPortal;
    }

    @Override
    public String toString() {
        return "RegistruPortal{" +
                "id=" + id +
                ", incarcare=" + incarcare +
                ", stareRegistru=" + stareRegistru +
                ", indexRegistru='" + indexRegistru + '\'' +
                ", dataRegistru=" + dataRegistru +
                ", denumireFisier='" + denumireFisier + '\'' +
                ", continutFisier='" + continutFisier + '\'' +
                ", recipisa=" + Arrays.toString(recipisa) +
                ", isRecipisaSemnata=" + isRecipisaSemnata +
                '}';
    }
}
