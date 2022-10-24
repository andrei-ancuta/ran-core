package ro.uti.ran.core.ws.internal.sesiune;

import java.io.Serializable;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-15 16:34
 */
public class InfoSesiune implements Serializable{
    private String numeUtilizator;
    private boolean valida;

    public String getNumeUtilizator() {
        return numeUtilizator;
    }

    public void setNumeUtilizator(String numeUtilizator) {
        this.numeUtilizator = numeUtilizator;
    }

    public boolean isValida() {
        return valida;
    }

    public void setValida(boolean valida) {
        this.valida = valida;
    }
}
