package ro.uti.ran.core.model.portal;

import ro.uti.ran.core.model.Model;

import javax.persistence.*;
import java.util.Date;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-15 14:33
 */
@Entity
@Table(name = "APP_OPERATIE_SESIUNE")
public class OperatieSesiune extends Model {
    @Id
    @GeneratedValue(generator = "OperatieSesiuneSeq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "OperatieSesiuneSeq", sequenceName = "SEQ_APP_OPERATIE_SESIUNE", allocationSize = 1)
    @Column(name = "ID_APP_OPERATIE_SESIUNE", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FK_APP_SESIUNE", referencedColumnName = "ID_APP_SESIUNE", nullable = false)
    private Sesiune sesiune;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FK_APP_TIP_OPERATIE", referencedColumnName = "ID_APP_TIP_OPERATIE", nullable = false)
    private TipOperatie tipOperatie;

    @Column(name = "DATA_OPERATIE", nullable = false)
    private Date dataOperatie;

    @Column(name = "DESCRIERE", length = 500, nullable = false)
    private String descriere;

    @Lob
    @Column(name = "DESCRIERE_COMPLET")
    private String descriereComplet;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sesiune getSesiune() {
        return sesiune;
    }

    public void setSesiune(Sesiune sesiune) {
        this.sesiune = sesiune;
    }

    public TipOperatie getTipOperatie() {
        return tipOperatie;
    }

    public void setTipOperatie(TipOperatie tipOperatie) {
        this.tipOperatie = tipOperatie;
    }

    public Date getDataOperatie() {
        return dataOperatie;
    }

    public void setDataOperatie(Date dataOperatie) {
        this.dataOperatie = dataOperatie;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public String getDescriereComplet() {
        return descriereComplet;
    }

    public void setDescriereComplet(String descriereComplet) {
        this.descriereComplet = descriereComplet;
    }
}
