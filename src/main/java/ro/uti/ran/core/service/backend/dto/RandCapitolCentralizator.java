package ro.uti.ran.core.service.backend.dto;

import java.io.Serializable;

/**
 * Created by smash on 03/11/15.
 */
public class RandCapitolCentralizator implements Serializable {


    private Long id;
    private Long nomCapitolId;
    private Long uatId;
    private Long nomId;

    private String cod;
    private Integer codRand;
    private String denumire;

    private Integer an;
    private Integer codSiruta;


    public RandCapitolCentralizator() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public Integer getCodRand() {
        return codRand;
    }

    public void setCodRand(Integer codRand) {
        this.codRand = codRand;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public Integer getAn() {
        return an;
    }

    public void setAn(Integer an) {
        this.an = an;
    }

    public Integer getCodSiruta() {
        return codSiruta;
    }

    public void setCodSiruta(Integer codSiruta) {
        this.codSiruta = codSiruta;
    }

    public Long getUatId() {
        return uatId;
    }

    public void setUatId(Long uatId) {
        this.uatId = uatId;
    }

    public Long getNomId() {
        return nomId;
    }

    public void setNomId(Long nomId) {
        this.nomId = nomId;
    }

    public Long getNomCapitolId() {
        return nomCapitolId;
    }

    public void setNomCapitolId(Long nomCapitolId) {
        this.nomCapitolId = nomCapitolId;
    }
}
