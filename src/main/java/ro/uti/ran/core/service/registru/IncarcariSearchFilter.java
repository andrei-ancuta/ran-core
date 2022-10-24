package ro.uti.ran.core.service.registru;

import ro.uti.ran.core.utils.SearchFilter;

import java.util.Date;
import java.util.List;

/**
 * Search filter incarcari
 *
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-17 15:56
 */
public class IncarcariSearchFilter extends SearchFilter {
    private String indexIncarcare;
    private Date dataIncarcareStart;
    private Date dataIncarcareStop;
    private List<Long> idUat;
    private String denumireFisier;
    private String numeUtilizator;

    public String getIndexIncarcare() {
        return indexIncarcare;
    }

    public void setIndexIncarcare(String indexIncarcare) {
        this.indexIncarcare = indexIncarcare;
    }

    public Date getDataIncarcareStart() {
        return dataIncarcareStart;
    }

    public void setDataIncarcareStart(Date dataIncarcareStart) {
        this.dataIncarcareStart = dataIncarcareStart;
    }

    public Date getDataIncarcareStop() {
        return dataIncarcareStop;
    }

    public void setDataIncarcareStop(Date dataIncarcareStop) {
        this.dataIncarcareStop = dataIncarcareStop;
    }

    public List<Long> getIdUat() {
        return idUat;
    }

    public void setIdUat(List<Long> idUat) {
        this.idUat = idUat;
    }

    public String getDenumireFisier() {
        return denumireFisier;
    }

    public void setDenumireFisier(String denumireFisier) {
        this.denumireFisier = denumireFisier;
    }

    public String getNumeUtilizator() {
        return numeUtilizator;
    }

    public void setNumeUtilizator(String numeUtilizator) {
        this.numeUtilizator = numeUtilizator;
    }
}
