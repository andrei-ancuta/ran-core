package ro.uti.ran.core.model.registru.view;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the VW_UAT_GOSPODARIE view.
 */
@Entity
@IdClass(ViewUatGospodariePK.class)
@Table(name = "VW_UAT_GOSPODARIE")
public class ViewUatGospodarie implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID_NOM_UAT")
    private Long uat;

    @Id
    @Column(name = "AN")
    private Integer an;

    @Column(name = "ID_NOM_JUDET")
    private Long idNomJudet;

    @Column(name = "TOTAL_UAT_GOSPODARIE")
    private Long totalUatGospodarie;

    @Column(name = "TOTAL_UAT_DECLARATIE")
    private Long totalUatDeclaratie;

    public ViewUatGospodarie() {
    }

    public Integer getAn() {
        return an;
    }

    public void setAn(Integer an) {
        this.an = an;
    }

    public Long getUat() {
        return uat;
    }

    public void setUat(Long uat) {
        this.uat = uat;
    }

    public Long getTotalUatGospodarie() {
        return totalUatGospodarie;
    }

    public void setTotalUatGospodarie(Long totalUatGospodarie) {
        this.totalUatGospodarie = totalUatGospodarie;
    }

    public Long getTotalUatDeclaratie() {
        return totalUatDeclaratie;
    }

    public void setTotalUatDeclaratie(Long totalUatDeclaratie) {
        this.totalUatDeclaratie = totalUatDeclaratie;
    }

    public Long getIdNomJudet() {
        return idNomJudet;
    }

    public void setIdNomJudet(Long idNomJudet) {
        this.idNomJudet = idNomJudet;
    }

    @Override
    public boolean equals(Object obj) {
        return getUat() == ((ViewUatGospodarie) obj).getUat() && getAn() == ((ViewUatGospodarie) obj).getAn();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}