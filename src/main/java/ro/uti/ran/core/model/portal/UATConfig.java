package ro.uti.ran.core.model.portal;

import ro.uti.ran.core.model.registru.NomSursaRegistru;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-15 14:31
 */
@Entity
@Table(name = "UAT_CONFIG")
public class UATConfig implements Serializable{

    @Id
    @GeneratedValue(generator = "UatConfigSeq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "UatConfigSeq", sequenceName = "SEQ_UAT_CONFIG", allocationSize = 1, initialValue = 1)
    @Column(name = "ID_UAT_CONFIG", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_NOM_UAT")
    private UAT uat;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_NOM_SURSA_REGISTRU")
    private NomSursaRegistru nomSursaRegistru;

    @Column(name = "IS_NOTIFICARE_RAP")
    private boolean notificareRaportare;

    @Column(name = "IS_MOD_TRANSMITERE_MANUAL")
    private boolean transmitereManuala;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UAT getUat() {
        return uat;
    }

    public void setUat(UAT uat) {
        this.uat = uat;
    }

    public boolean isNotificareRaportare() {
        return notificareRaportare;
    }

    public void setNotificareRaportare(boolean notificareRaportare) {
        this.notificareRaportare = notificareRaportare;
    }

    public boolean isTransmitereManuala() {
        return transmitereManuala;
    }

    public void setTransmitereManuala(boolean transmitereManuala) {
        this.transmitereManuala = transmitereManuala;
    }

    public NomSursaRegistru getNomSursaRegistru() {
        return nomSursaRegistru;
    }

    public void setNomSursaRegistru(NomSursaRegistru nomSursaRegistru) {
        this.nomSursaRegistru = nomSursaRegistru;
    }
}


