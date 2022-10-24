package ro.uti.ran.core.model.portal;

import ro.uti.ran.core.model.Model;

import javax.persistence.*;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-15 13:43
 */
@Entity
@Table(name = "APP_ROL")
public class Rol extends Model {
    @Id
    @GeneratedValue(generator = "RolSeq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "RolSeq", sequenceName = "SEQ_APP_ROL", allocationSize = 1)
    @Column(name = "ID_APP_ROL", updatable = false)
    private Long id;

    @Column(name = "COD", length = 10, nullable = false)
    private String cod;

    @Column(name = "DENUMIRE", length = 100, nullable = false)
    private String denumire;

    @Column(name = "DESCRIERE", length = 500, nullable = true)
    private String descriere;

    @Column(name = "IS_ACTIV", nullable = false)
    private Boolean activ;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="FK_APP_CONTEXT", referencedColumnName = "ID_APP_CONTEXT")
    private Context context;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="FK_APP_COMPONENTA", referencedColumnName = "ID_APP_COMPONENTA")
    private Componenta componenta;

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

    public Boolean getActiv() {
        return activ;
    }

    public void setActiv(Boolean activ) {
        this.activ = activ;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Componenta getComponenta() {
        return componenta;
    }

    public void setComponenta(Componenta componenta) {
        this.componenta = componenta;
    }
}
