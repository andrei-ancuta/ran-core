package ro.uti.ran.core.model.portal;

import ro.uti.ran.core.model.Model;

import javax.persistence.*;
import java.util.Date;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-17 12:12
 */
@Entity
@Table(name="VW_REGISTRU_PORTAL")
public class RegistruPortalDetails extends Model{
    @Id
    @Column(name = "ID_REGISTRU")
    private Long id;

    @Column(name = "INDEX_REGISTRU")
    private String indexRegistru;

    @Column(name = "DATA_REGISTRU")
    private Date dataRegistru;

    @Column(name = "DENUMIRE_FISIER")
    private String denumireFisier;

    @Column(name = "COD")
    private String codStareRegistruPortal;

    @Column(name = "DENUMIRE")
    private String denumireStareRegistruPortal;

    @Column(name = "DATA_STARE")
    private Date dataStareFluxRegistruPortal;

    @Column(name = "MESAJ_STARE")
    private String mesajStareFluxRegistruPortal;

    @Column(name = "COD_SRC")
    private String codStareRegistruCore;

    @Column(name = "DENUMIRE_SRC")
    private String denumireStareRegistruCore;

    @Column(name = "IS_RECIPISA_SEMNATA")
    private Boolean isRecipisaSemnata;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FK_INCARCARE", referencedColumnName = "ID_INCARCARE")
    private Incarcare incarcare;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCodStareRegistruPortal() {
        return codStareRegistruPortal;
    }

    public void setCodStareRegistruPortal(String codStareRegistruPortal) {
        this.codStareRegistruPortal = codStareRegistruPortal;
    }

    public String getDenumireStareRegistruPortal() {
        return denumireStareRegistruPortal;
    }

    public void setDenumireStareRegistruPortal(String denumireStareRegistruPortal) {
        this.denumireStareRegistruPortal = denumireStareRegistruPortal;
    }

    public Date getDataStareFluxRegistruPortal() {
        return dataStareFluxRegistruPortal;
    }

    public void setDataStareFluxRegistruPortal(Date dataStareFluxRegistruPortal) {
        this.dataStareFluxRegistruPortal = dataStareFluxRegistruPortal;
    }

    public String getMesajStareFluxRegistruPortal() {
        return mesajStareFluxRegistruPortal;
    }

    public void setMesajStareFluxRegistruPortal(String mesajStareFluxRegistruPortal) {
        this.mesajStareFluxRegistruPortal = mesajStareFluxRegistruPortal;
    }

    public String getCodStareRegistruCore() {
        return codStareRegistruCore;
    }

    public void setCodStareRegistruCore(String codStareRegistruCore) {
        this.codStareRegistruCore = codStareRegistruCore;
    }

    public String getDenumireStareRegistruCore() {
        return denumireStareRegistruCore;
    }

    public void setDenumireStareRegistruCore(String denumireStareRegistruCore) {
        this.denumireStareRegistruCore = denumireStareRegistruCore;
    }

    public Boolean getIsRecipisaSemnata() {
        return isRecipisaSemnata;
    }

    public void setIsRecipisaSemnata(Boolean isRecipisaSemnata) {
        this.isRecipisaSemnata = isRecipisaSemnata;
    }

    public Incarcare getIncarcare() {
        return incarcare;
    }

    public void setIncarcare(Incarcare incarcare) {
        this.incarcare = incarcare;
    }
}
