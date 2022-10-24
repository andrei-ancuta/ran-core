package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-15 16:20
 */
@Entity
@Table(name = "APP_TIP_OPERATIE")
public class TipOperatieSin implements Serializable {

    @Id
    @GeneratedValue(generator = "TipOperatieSesiuneSinSeq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "TipOperatieSesiuneSinSeq", sequenceName = "SEQ_APP_TIP_OPERATIE_SESIUNE", allocationSize = 1)
    @Column(name = "ID_APP_TIP_OPERATIE", updatable = false)
    private Long id;

    @Column(name = "COD", length = 10, nullable = false)
    private String cod;

    @Column(name = "DENUMIRE", length = 100, nullable = false)
    private String denumire;

    @Column(name = "DESCRIERE", length = 500, nullable = false)
    private String descriere;

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

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }
}
