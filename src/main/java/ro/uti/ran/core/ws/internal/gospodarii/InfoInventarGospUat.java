package ro.uti.ran.core.ws.internal.gospodarii;

import ro.uti.ran.core.model.registru.InventarGospUat;

import java.util.Date;

public class InfoInventarGospUat {
    private Long id;
    private Integer codSiruta;
    private Integer an;
    private Integer valoare;
    private Date lastModificationDate;
    private String denumire;

    public InfoInventarGospUat() {
    }

    public InfoInventarGospUat(InventarGospUat totalGospodarie) {
        this.setId(totalGospodarie.getIdInventarGospUat());
        this.setCodSiruta(totalGospodarie.getNomUat().getCodSiruta());
        this.setAn(totalGospodarie.getAn());
        this.setValoare(totalGospodarie.getValoare());
        this.setLastModificationDate(totalGospodarie.getLastModifiedDate());
        this.setDenumire(totalGospodarie.getNomUat().getDenumire());

    }

    public Integer getCodSiruta() {
        return codSiruta;
    }

    public void setCodSiruta(Integer codSiruta) {
        this.codSiruta = codSiruta;
    }

    public Integer getAn() {
        return an;
    }

    public void setAn(Integer an) {
        this.an = an;
    }

    public Integer getValoare() {
        return valoare;
    }

    public void setValoare(Integer valoare) {
        this.valoare = valoare;
    }

    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }
}
