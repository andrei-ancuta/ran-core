package ro.uti.ran.core.service.gospodarii;

import ro.uti.ran.core.utils.SearchFilter;

/**
 * Created by adrian.boldisor on 2/25/2016.
 */
public class GospodariiSearchFilter extends SearchFilter {
    private String denumireFirma;
    private String cui;


    public String getDenumireFirma() {
        return denumireFirma;
    }

    public void setDenumireFirma(String denumireFirma) {
        this.denumireFirma = denumireFirma;
    }

    public String getCui() {
        return cui;
    }

    public void setCui(String cui) {
        this.cui = cui;
    }
}
