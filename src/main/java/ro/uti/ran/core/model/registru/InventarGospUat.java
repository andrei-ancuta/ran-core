package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * The persistent class for the INVENTAR_GOSP_UAT database table.
 */
@Entity
@Table(name = "INVENTAR_GOSP_UAT")
@NamedQuery(name = "InventarGospUat.findAll", query = "SELECT i FROM InventarGospUat i")
public class InventarGospUat implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "INVENTAR_GOSP_UAT_IDINVENTAR_GOSP_UAT_GENERATOR", sequenceName = "SEQ_INVENTAR_GOSP_UAT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INVENTAR_GOSP_UAT_IDINVENTAR_GOSP_UAT_GENERATOR")
    @Column(name = "ID_INVENTAR_GOSP_UAT")
    private Long idInventarGospUat;

    @Column(name = "\"AN\"")
    private Integer an;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="LAST_MODIFIED_DATE")
    private Date lastModifiedDate;


    private Integer valoare;

    //bi-directional many-to-one association to NomUat
    @ManyToOne
    @JoinColumn(name = "FK_NOM_UAT")
    private NomUat nomUat;

    public InventarGospUat() {
    }

    public Long getIdInventarGospUat() {
        return this.idInventarGospUat;
    }

    public void setIdInventarGospUat(Long idInventarGospUat) {
        this.idInventarGospUat = idInventarGospUat;
    }

    public Integer getAn() {
        return this.an;
    }

    public void setAn(Integer an) {
        this.an = an;
    }


    public Integer getValoare() {
        return this.valoare;
    }

    public void setValoare(Integer valoare) {
        this.valoare = valoare;
    }

    public NomUat getNomUat() {
        return this.nomUat;
    }

    public void setNomUat(NomUat nomUat) {
        this.nomUat = nomUat;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

}