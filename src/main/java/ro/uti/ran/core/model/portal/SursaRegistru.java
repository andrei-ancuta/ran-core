package ro.uti.ran.core.model.portal;

import ro.uti.ran.core.model.HasCode;
import ro.uti.ran.core.model.HasName;
import ro.uti.ran.core.model.registru.NomSistem;
import ro.uti.ran.core.model.registru.Nomenclator;

import javax.persistence.*;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-17 12:30
 */
@Entity
@Table(name = "NOM_SURSA_REGISTRU")
public class SursaRegistru extends Nomenclator implements HasCode, HasName, NomSistem {
    @Id
    @GeneratedValue(generator = "SursaRegistruSeq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SursaRegistruSeq", sequenceName = "SEQ_NOM_SURSA_REGISTRU", allocationSize = 1)
    @Column(name = "ID_NOM_SURSA_REGISTRU", updatable = false)
    private Long id;

    @Column(name = "COD", length = 10, nullable = false)
    private String cod;

    @Column(name = "DENUMIRE", length = 100, nullable = false)
    private String denumire;

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
}
