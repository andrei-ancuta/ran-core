package ro.uti.ran.core.model.portal;

import ro.uti.ran.core.model.registru.Nomenclator;

import javax.persistence.*;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-23 14:10
 */
@Entity
@Table(name="NOM_INSTITUTIE")
public class Institutie extends Nomenclator {

    @Id
    @GeneratedValue(generator = "InstitutieSeq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "InstitutieSeq", sequenceName = "SEQ_NOM_INSTITUTIE", allocationSize = 1)
    @Column(name = "ID_NOM_INSTITUTIE", updatable = false)
    private Long id;

    @Column(name = "COD", length = 10, nullable = false)
    private String cod;

    @Column(name = "DENUMIRE", length = 100, nullable = false)
    private String denumire;

    @Column(name = "IS_ACCES_DATE_PRIMARE", nullable = false)
    private Boolean accesDatePrimare;

    @Column(name = "IS_ACCES_DATE_AGREGATE", nullable = false)
    private Boolean accesDateAgregate;

    @Column(name = "FK_NOM_TIP_INSTITUTIE")
    private Long fkNomTipInstitutie;

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

    public Long getFkNomTipInstitutie() {
        return fkNomTipInstitutie;
    }

    public void setFkNomTipInstitutie(Long fkNomTipInstitutie) {
        this.fkNomTipInstitutie = fkNomTipInstitutie;
    }

    public Boolean getAccesDatePrimare() { return accesDatePrimare; }

    public void setAccesDatePrimare(Boolean accesDatePrimare) { this.accesDatePrimare = accesDatePrimare; }

    public Boolean getAccesDateAgregate() {return accesDateAgregate; }

    public void setAccesDateAgregate(Boolean accesDateAgregate) {this.accesDateAgregate = accesDateAgregate; }
}
