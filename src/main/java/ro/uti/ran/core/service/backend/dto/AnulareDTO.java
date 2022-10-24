package ro.uti.ran.core.service.backend.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 16-Nov-15.
 */
public class AnulareDTO implements Serializable {

    private String codCapitol;
    private String denumire;
    private Integer anRaportare;
    private Integer semestruRaportare;

    public AnulareDTO() {

    }

    public String getCodCapitol() {
        return codCapitol;
    }

    public void setCodCapitol(String codCapitol) {
        this.codCapitol = codCapitol;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public Integer getAnRaportare() {
        return anRaportare;
    }

    public void setAnRaportare(Integer anRaportare) {
        this.anRaportare = anRaportare;
    }

    public Integer getSemestruRaportare() {
        return semestruRaportare;
    }

    public void setSemestruRaportare(Integer semestruRaportare) {
        this.semestruRaportare = semestruRaportare;
    }

}
