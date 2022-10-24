package ro.uti.ran.core.model.portal;

import ro.uti.ran.core.model.registru.Nomenclator;

import javax.persistence.*;
import java.util.Date;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-15 14:31
 */
@Entity
@Table(name = "NOM_UAT")
public class UAT extends Nomenclator {

    @Id
    @Column(name = "ID_NOM_UAT", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FK_NOM_JUDET")
    private Judet judet;

    @Column(name = "COD_SIRUTA")
    private int codSiruta;

    @Column(name = "DENUMIRE")
    private String denumire;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Judet getJudet() {
        return judet;
    }

    public void setJudet(Judet judet) {
        this.judet = judet;
    }

    public int getCodSiruta() {
        return codSiruta;
    }

    public void setCodSiruta(int codSiruta) {
        this.codSiruta = codSiruta;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }
}
