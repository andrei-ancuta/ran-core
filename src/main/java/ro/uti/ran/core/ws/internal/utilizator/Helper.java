package ro.uti.ran.core.ws.internal.utilizator;

import ro.uti.ran.core.model.portal.Utilizator;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-30 01:12
 */
public class Helper {

    public static DetaliiUtilizator buildDetaliiUtilizator(Utilizator utilizator){
        DetaliiUtilizator detaliiUtilizator = new DetaliiUtilizator();
        detaliiUtilizator.setNumeUtilizator(utilizator.getNumeUtilizator());
        detaliiUtilizator.setNume(utilizator.getNume());
        detaliiUtilizator.setPrenume(utilizator.getPrenume());
        detaliiUtilizator.setCnp(utilizator.getCnp());
        detaliiUtilizator.setNif(utilizator.getNif());
        detaliiUtilizator.setEmail(utilizator.getEmail());
        detaliiUtilizator.setActiv(utilizator.getActiv());

        return detaliiUtilizator;
    }
}
