package ro.uti.ran.core.model.registru;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-15 14:33
 */
@Entity
@Table(name = "APP_OPERATIE_SESIUNE")
public class OperatieSesiuneSin implements Serializable {
    @Id
    @GeneratedValue(generator = "OperatieSesiuneSinSeq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "OperatieSesiuneSinSeq", sequenceName = "SEQ_APP_OPERATIE_SESIUNE", allocationSize = 1)
    @Column(name = "ID_APP_OPERATIE_SESIUNE", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_APP_SESIUNE", referencedColumnName = "ID_APP_SESIUNE", nullable = false)
    private SesiuneSin sesiuneSin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_APP_TIP_OPERATIE", referencedColumnName = "ID_APP_TIP_OPERATIE", nullable = false)
    private TipOperatieSin tipOperatieSin;

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

    public SesiuneSin getSesiuneSin() {
        return sesiuneSin;
    }

    public void setSesiuneSin(SesiuneSin sesiuneSin) {
        this.sesiuneSin = sesiuneSin;
    }

    public TipOperatieSin getTipOperatieSin() {
        return tipOperatieSin;
    }

    public void setTipOperatieSin(TipOperatieSin tipOperatieSin) {
        this.tipOperatieSin = tipOperatieSin;
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
