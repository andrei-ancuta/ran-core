package ro.uti.ran.core.service.registru;

import ro.uti.ran.core.utils.SearchFilter;

import java.util.Date;

/**
 * Search filter registru.
 *
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-29 13:31
 */
public class RegistruSearchFilter extends SearchFilter {

    private Date dataRegistruStart;
    private Date dataRegistruStop;

    private Long indexRegistru;

    private Long idUat;
    private Long idStareRegistru;
    private Long idSursaRegistru;

    private Boolean isRecipisaSemnata;

    public Date getDataRegistruStart() {
        return dataRegistruStart;
    }

    public void setDataRegistruStart(Date dataRegistruStart) {
        this.dataRegistruStart = dataRegistruStart;
    }

    public Date getDataRegistruStop() {
        return dataRegistruStop;
    }

    public void setDataRegistruStop(Date dataRegistruStop) {
        this.dataRegistruStop = dataRegistruStop;
    }

    public Long getIndexRegistru() {
        return indexRegistru;
    }

    public void setIndexRegistru(Long indexRegistru) {
        this.indexRegistru = indexRegistru;
    }

    public Long getIdUat() {
        return idUat;
    }

    public void setIdUat(Long idUat) {
        this.idUat = idUat;
    }

    public Long getIdStareRegistru() {
        return idStareRegistru;
    }

    public void setIdStareRegistru(Long idStareRegistru) {
        this.idStareRegistru = idStareRegistru;
    }

    public Long getIdSursaRegistru() {
        return idSursaRegistru;
    }

    public void setIdSursaRegistru(Long idSursaRegistru) {
        this.idSursaRegistru = idSursaRegistru;
    }

    public Boolean getIsRecipisaSemnata() {
        return isRecipisaSemnata;
    }

    public void setIsRecipisaSemnata(Boolean isRecipisaSemnata) {
        this.isRecipisaSemnata = isRecipisaSemnata;
    }
}
