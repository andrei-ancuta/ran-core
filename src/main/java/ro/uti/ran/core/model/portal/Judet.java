package ro.uti.ran.core.model.portal;

import ro.uti.ran.core.model.registru.Nomenclator;

import javax.persistence.*;
import java.util.Date;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-15 14:11
 */
@Entity
@Table(name="NOM_JUDET")
public class Judet extends Nomenclator {

    @Id
    @Column(name = "ID_NOM_JUDET", updatable = false)
    private Long id;

    @Column(name = "COD_ALFA")
    private String codAlfa;

    @Column(name = "COD_SIRUTA")
    private Long codSiruta;

    @Column(name = "DENUMIRE")
    private String denumire;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodAlfa() {
        return codAlfa;
    }

    public void setCodAlfa(String codAlfa) {
        this.codAlfa = codAlfa;
    }

    public Long getCodSiruta() {
        return codSiruta;
    }

    public void setCodSiruta(Long codSiruta) {
        this.codSiruta = codSiruta;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }
}
