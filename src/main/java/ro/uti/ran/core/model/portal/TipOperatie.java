package ro.uti.ran.core.model.portal;

import ro.uti.ran.core.model.Model;

import javax.persistence.*;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-15 16:20
 */
@Entity
@Table(name = "APP_TIP_OPERATIE")
public class TipOperatie extends Model {

    @Id
    @GeneratedValue(generator = "TipOperatieSesiuneSeq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "TipOperatieSesiuneSeq", sequenceName = "SEQ_APP_TIP_OPERATIE_SESIUNE", allocationSize = 1)
    @Column(name = "ID_APP_TIP_OPERATIE", updatable = false)
    private Long id;

    @Column(name = "COD", length = 10, nullable = false)
    private String cod;

    @Column(name = "DENUMIRE", length = 100, nullable = false)
    private String denumire;

    @Column(name = "DESCRIERE", length = 500, nullable = false)
    private String descriere;


    @Column(name = "IS_AUTOMATA")
    private Integer isAutomata;


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


    public Integer getIsAutomata() {
        return isAutomata;
    }

    public void setIsAutomata(Integer isAutomata) {
        this.isAutomata = isAutomata;
    }
}
