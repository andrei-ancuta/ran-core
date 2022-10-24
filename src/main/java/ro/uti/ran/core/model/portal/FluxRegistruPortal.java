package ro.uti.ran.core.model.portal;

import ro.uti.ran.core.model.Model;

import javax.persistence.*;
import java.util.Date;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-17 12:18
 */
@Entity
@Table(name = "FLUX_REGISTRU")
public class FluxRegistruPortal extends Model {

    @Id
    @GeneratedValue(generator = "FluxRegistruSeq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "FluxRegistruSeq", sequenceName = "SEQ_FLUX_REGISTRU", allocationSize = 1)
    @Column(name = "ID_FLUX_REGISTRU", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FK_REGISTRU", referencedColumnName = "ID_REGISTRU", nullable = false)
    private RegistruPortal registru;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FK_NOM_STARE_REGISTRU", referencedColumnName = "ID_NOM_STARE_REGISTRU", nullable = false)
    private StareRegistru stareRegistru;

    @Column(name = "DATA_STARE", nullable = false, updatable = false)
    private Date dataStare;

    @Column(name="MESAJ_STARE")
    private String mesajStare;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RegistruPortal getRegistru() {
        return registru;
    }

    public void setRegistru(RegistruPortal registru) {
        this.registru = registru;
    }

    public StareRegistru getStareRegistru() {
        return stareRegistru;
    }

    public void setStareRegistru(StareRegistru stareRegistru) {
        this.stareRegistru = stareRegistru;
    }

    public Date getDataStare() {
        return dataStare;
    }

    public void setDataStare(Date dataStare) {
        this.dataStare = dataStare;
    }

    public String getMesajStare() {
        return mesajStare;
    }

    public void setMesajStare(String mesajStare) {
        this.mesajStare = mesajStare;
    }
}
