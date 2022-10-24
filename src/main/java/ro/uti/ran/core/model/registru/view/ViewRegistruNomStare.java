package ro.uti.ran.core.model.registru.view;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the VW_REGISTRU_NOM_STARE view.
 */
@Entity
@Table(name = "VW_REGISTRU_NOM_STARE")
public class ViewRegistruNomStare implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID_REGISTRU")
    private Long idRegistru;

    @Column(name = "COD")
    private String cod;

    @Column(name = "IS_RECIPISA_SEMNATA")
    private Boolean isRecipisaSemnata = false;

    @Column(name = "RECIPISA")
    @Lob
    private  byte[] recipisa;

    @Column(name = "INDEX_REGISTRU")
    private String indexRegistru;

    public ViewRegistruNomStare() {
    }

    public Long getIdRegistru() {
        return this.idRegistru;
    }

    public void setIdRegistru(Long idRegistru) {
        this.idRegistru = idRegistru;
    }

    public String getCod() {
        return this.cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public byte[] getRecipisa() {
        return this.recipisa;
    }

    public void setRecipisa( byte[] recipisa) {
        this.recipisa = recipisa;
    }

    public Boolean getIsRecipisaSemnata() {
        return this.isRecipisaSemnata;
    }

    public void setIsRecipisaSemnata(Boolean isRecipisaSemnata) {
        this.isRecipisaSemnata = isRecipisaSemnata;
    }

    public String getIndexRegistru() {
        return this.indexRegistru;
    }

    public void setIndexRegistru(String indexRegistru) {
        this.indexRegistru = indexRegistru;
    }


}