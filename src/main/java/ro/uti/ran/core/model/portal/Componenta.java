package ro.uti.ran.core.model.portal;

import ro.uti.ran.core.model.Model;

import javax.persistence.*;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-15 13:47
 */
@Entity
@Table(name = "APP_COMPONENTA")
public class Componenta extends Model {
    @Id
    @GeneratedValue(generator = "ComponenteSeq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "ComponenteSeq", sequenceName = "SEQ_APP_COMPONENTA", allocationSize = 1)
    @Column(name = "ID_APP_COMPONENTA", updatable = false)
    private Long id;

    @Column(name = "COD", length = 10, nullable = false)
    private String cod;

    @Column(name = "DENUMIRE", length = 100, nullable = false)
    private String denumire;

    @Column(name = "DESCRIERE", length = 500, nullable = true)
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
