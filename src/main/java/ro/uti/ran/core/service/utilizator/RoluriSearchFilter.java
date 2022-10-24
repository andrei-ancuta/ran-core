package ro.uti.ran.core.service.utilizator;

import ro.uti.ran.core.utils.SearchFilter;

import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-28 17:47
 */
public class RoluriSearchFilter extends SearchFilter {
    private String cod;
    private String denumire;
    private List<String> coduri;
    private String codContext;

    public List<String> getCoduri() {
        return coduri;
    }

    public void setCoduri(List<String> coduri) {
        this.coduri = coduri;
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

    public String getCodContext() {
        return codContext;
    }

    public void setCodContext(String codContext) {
        this.codContext = codContext;
    }
}
