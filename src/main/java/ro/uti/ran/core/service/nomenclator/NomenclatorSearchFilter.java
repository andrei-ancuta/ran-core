package ro.uti.ran.core.service.nomenclator;

import ro.uti.ran.core.model.registru.TipNomenclator;
import ro.uti.ran.core.utils.SearchFilter;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-02 15:16
 */
public class NomenclatorSearchFilter extends SearchFilter {

    @NotNull
    private TipNomenclator type;

    /**
     * Cautare dupa cod intrare nomenclator
     */
    private String cod;

    /**
     * Cautare dupa denumire intrare nomenclator
     */
    private String denumire;


    /**
     * Afiseaza inregistrarile active
     */
    private Boolean showActiveRecords;

    /**
     * Afiseaza inregistrarile istoric
     */
    private Boolean showHistoryRecords;

    /**
     * Inregistrarile valabile la dataHistoryRecords
     */
    private Date dataHistoryRecords;


    private List<NomenclatorSearchCriteria> criterias;

    public TipNomenclator getType() {
        return type;
    }

    public void setType(TipNomenclator type) {
        this.type = type;
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

    public Boolean getShowActiveRecords() {
        return showActiveRecords;
    }

    public void setShowActiveRecords(Boolean showActiveRecords) {
        this.showActiveRecords = showActiveRecords;
    }

    public Boolean getShowHistoryRecords() {
        return showHistoryRecords;
    }

    public void setShowHistoryRecords(Boolean showHistoryRecords) {
        this.showHistoryRecords = showHistoryRecords;
    }

    public Date getDataHistoryRecords() {
        return dataHistoryRecords;
    }

    public void setDataHistoryRecords(Date dataHistoryRecords) {
        this.dataHistoryRecords = dataHistoryRecords;
    }

    public List<NomenclatorSearchCriteria> getCriterias() {
        if( criterias == null){
            criterias = new LinkedList<>();
        }
        return criterias;
    }

    public void setCriterias(List<NomenclatorSearchCriteria> criterias) {
        this.criterias = criterias;
    }
}
