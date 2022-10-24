package ro.uti.ran.core.service.backend.renns;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ro.uti.ran.core.model.registru.Adresa;
import ro.uti.ran.core.utils.GeometrieHelper;
import ro.uti.ran.core.ws.client.renns.RennsAddressesService;
import ro.uti.ran.core.ws.client.renns.addresses.AddressWS;
import ro.uti.ran.core.ws.client.renns.addresses.GetAddressWSRequest;
import ro.uti.ran.core.ws.client.renns.addresses.GetAddressWSResponse;

import java.util.List;

/**
 * Created by Dan on 29-Jan-16.
 */
@Component
public class AsyncUpdateFromRenns {
    private static final Logger logger = LoggerFactory.getLogger(AsyncUpdateFromRenns.class);
    @Autowired
    private AdresaService adresaService;
    @Autowired
    private RennsAddressesService rennsAddressesService;

    @Async("catalogsExportExecutor")
    public void updateFromRenns(String cua, String denumireJudet) {
        try {
            //pas1: interogare WS
            GetAddressWSRequest request = new GetAddressWSRequest();
            request.setCua(cua);
            request.setCounty(denumireJudet);
            GetAddressWSResponse response = rennsAddressesService.findAddressByCua(request);
            List<AddressWS> addressWSList = response.getAddressWS();
            //daca am raspuns
            if (addressWSList != null && !addressWSList.isEmpty()) {
                AddressWS addressWS = addressWSList.get(0);
                //pas2: reimpachetare date primite de la WS intr-un obiect Adresa
                Adresa renns = convertToEntity(addressWS);
                //pas3: daca WS-ul raspunde cu succes actualizare adresa
                adresaService.updateFromRenns(renns);
            }
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }
    }

    private Adresa convertToEntity(AddressWS addressWS) {
        Adresa adresa = new Adresa();
        adresa.setUidRenns(addressWS.getCua());
        adresa.setStrada(addressWS.getName());
        adresa.setNrStrada(addressWS.getAdministratifNo());
        if (StringUtils.isNotEmpty(addressWS.getXCordinate()) || StringUtils.isNotEmpty(addressWS.getYCordinate())) {
            adresa.setGeometrieGML(GeometrieHelper.buildGmlPoint(addressWS.getXCordinate(), addressWS.getYCordinate()));
        }
        return adresa;
    }

}
