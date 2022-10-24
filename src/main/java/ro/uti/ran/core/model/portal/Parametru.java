package ro.uti.ran.core.model.portal;

import ro.uti.ran.core.model.Model;

import javax.persistence.*;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-15 14:37
 */
@Entity
@Table(name = "APP_PARAMETRU")
public class Parametru extends Model {

    @Id
    @GeneratedValue(generator = "ParametruSeq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "ParametruSeq", sequenceName = "SEQ_APP_PARAMETRU", allocationSize = 1)
    @Column(name = "ID_APP_PARAMETRU", updatable = false)
    private Long id;

    @Column(name = "COD", length = 10, nullable = false)
    private String cod;

    @Column(name = "DENUMIRE", length = 100, nullable = false)
    private String denumire;

    @Column(name = "DESCRIERE", length = 500, nullable = true)
    private String descriere;

    @Column(name = "VALOARE", length = 50, nullable = false)
    private String valoare;

    @Column(name = "VALOARE_IMPLICITA", length = 50, nullable = false)
    private String valoareImplicita;

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

    public String getValoare() {
        return valoare;
    }

    public void setValoare(String valoare) {
        this.valoare = valoare;
    }

    public String getValoareImplicita() {
        return valoareImplicita;
    }

    public void setValoareImplicita(String valoareImplicita) {
        this.valoareImplicita = valoareImplicita;
    }
}
