package ro.uti.ran.core.model.portal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.openjpa.persistence.Persistent;
import ro.uti.ran.core.model.HasPath;
import ro.uti.ran.core.model.Model;

import javax.persistence.*;
import java.io.InputStream;
import java.util.Date;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-17 12:15
 */
@Entity
@Table(name = "INCARCARE")
public class Incarcare extends Model{

    @Id
    @GeneratedValue(generator="IncarcareSeq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="IncarcareSeq",sequenceName="SEQ_INCARCARE", allocationSize = 1)
    @Column(name = "ID_INCARCARE", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="FK_APP_UTILIZATOR")
    private Utilizator utilizator;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="FK_NOM_UAT")
    private UAT uat;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="FK_NOM_STARE_INCARCARE")
    private StareIncarcare stareIncarcare;

    @Column(name = "INDEX_INCARCARE")
    private String indexIncarcare;

    @Column(name = "DATA_INCARCARE")
    private Date dataIncarcare;

    @Column(name = "DENUMIRE_FISIER")
    private String denumireFisier;

    @Lob
    @Column(name = "CONTINUT_FISIER")
    @Persistent
    @JsonIgnore
    private InputStream continutFisier;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Utilizator getUtilizator() {
        return utilizator;
    }

    public void setUtilizator(Utilizator utilizator) {
        this.utilizator = utilizator;
    }

    public UAT getUat() {
        return uat;
    }

    public void setUat(UAT uat) {
        this.uat = uat;
    }

    public StareIncarcare getStareIncarcare() {
        return stareIncarcare;
    }

    public void setStareIncarcare(StareIncarcare stareIncarcare) {
        this.stareIncarcare = stareIncarcare;
    }

    public String getIndexIncarcare() {
        return indexIncarcare;
    }

    public void setIndexIncarcare(String indexIncarcare) {
        this.indexIncarcare = indexIncarcare;
    }

    public Date getDataIncarcare() {
        return dataIncarcare;
    }

    public void setDataIncarcare(Date dataIncarcare) {
        this.dataIncarcare = dataIncarcare;
    }

    public String getDenumireFisier() {
        return denumireFisier;
    }

    public void setDenumireFisier(String denumireFisier) {
        this.denumireFisier = denumireFisier;
    }

    public InputStream getContinutFisier() {
        return continutFisier;
    }

    public void setContinutFisier(InputStream continutFisier) {
        this.continutFisier = continutFisier;
    }
}
