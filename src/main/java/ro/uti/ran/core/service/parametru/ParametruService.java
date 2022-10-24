package ro.uti.ran.core.service.parametru;

import ro.uti.ran.core.model.portal.Parametru;

import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-08 17:20
 */
public interface ParametruService {

    String RESTRICTIONARE_TRANSMISII_UAT_ADRESARENNS = "restrictionare.transmisii.uat.adresaRENNS";

    String RESTRICTIONARE_TRANSMISII_UAT_TOTALGOSP = "restrictionare.transmisii.uat.totalGosp";

    String PARAM_CONFIG_TIP_TRANS_COD = "ws.transmitereDate.async";

    String DATA_LIMITARE_ANRAPORTARE = "data.limitare.anRaportare";

    String PARAM_CONFIG_RESET_SECV = "ws.uat.reset.secv";

    String ACTIVEAZA_VALIDARE_CU_FUNCTII_SPATIALE_COD_PARAM = "139";

    String RAN_REFERENTIAL_EPSG = "140";

    String WS_RENNS_URL_COD = "141";

    String WS_RENNS_CREDENTIALS_PASSWORD_COD = "143";

    String WS_RENNS_CREDENTIALS_USERNAME_COD = "142";


    Parametru getParametru(String codParametru);

    List<Parametru> getListaParametri();

    Parametru salveazaParametru(Parametru parametru);
}
