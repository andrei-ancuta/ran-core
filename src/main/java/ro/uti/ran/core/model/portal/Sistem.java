package ro.uti.ran.core.model.portal;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-15 16:17
 */
@Entity
@Table(name = "APP_SISTEM")
public class Sistem implements Serializable{

    @Id
    @GeneratedValue(generator = "SistemSeq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SistemSeq", sequenceName = "SEQ_APP_SISTEM", allocationSize = 1)
    @Column(name = "ID_APP_SISTEM", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="FK_NOM_INSTITUTIE")
    private Institutie institutie;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="FK_NOM_UAT")
    private UAT uat;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="FK_NOM_JUDET")
    private Judet judet;

    private String cod;

    private String denumire;

    @Column(name = "COD_LICENTA")
    private String licenta;

    @Column(name = "DATA_GENERARE_LICENTA")
    private Date dataGenerareLicenta;

    @Column(name = "IS_ACTIV")
    private Boolean activ;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Institutie getInstitutie() {
        return institutie;
    }

    public void setInstitutie(Institutie institutie) {
        this.institutie = institutie;
    }

    public UAT getUat() {
        return uat;
    }

    public void setUat(UAT uat) {
        this.uat = uat;
    }

    public Judet getJudet() {
        return judet;
    }

    public void setJudet(Judet judet) {
        this.judet = judet;
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

    public String getLicenta() {
        return licenta;
    }

    public void setLicenta(String licenta) {
        this.licenta = licenta;
    }

    public Date getDataGenerareLicenta() {
        return dataGenerareLicenta;
    }

    public void setDataGenerareLicenta(Date dataGenerareLicenta) {
        this.dataGenerareLicenta = dataGenerareLicenta;
    }

    public Boolean getActiv() {
        return activ;
    }

    public void setActiv(Boolean activ) {
        this.activ = activ;
    }
}
