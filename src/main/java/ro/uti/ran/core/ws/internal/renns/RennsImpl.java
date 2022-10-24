package ro.uti.ran.core.ws.internal.renns;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import ro.uti.ran.core.model.registru.NomJudet;
import ro.uti.ran.core.model.registru.NomLocalitate;
import ro.uti.ran.core.service.backend.nomenclator.NomenclatorService;
import ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType;
import ro.uti.ran.core.utils.GeometrieHelper;
import ro.uti.ran.core.ws.client.renns.RennsAddressesService;
import ro.uti.ran.core.ws.client.renns.addresses.AddressWS;
import ro.uti.ran.core.ws.client.renns.addresses.GetAddressWSRequest;
import ro.uti.ran.core.ws.client.renns.addresses.GetAddressWSResponse;
import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;

import javax.jws.WebService;
import java.net.MalformedURLException;
import java.util.List;


/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-15 16:40
 */
@WebService(
        serviceName = "RennsService",
        endpointInterface = "ro.uti.ran.core.ws.internal.renns.Renns",
        targetNamespace = "http://internal.ws.core.ran.uti.ro",
        portName = "RennsServicePort")
@Service("rennsService")
public class RennsImpl implements Renns {

    private static final Logger LOGGER = LoggerFactory.getLogger(RennsImpl.class);

    @Autowired
    private Environment env;

    @Autowired
    private RennsAddressesService rennsAddressesService;

    @Autowired
    private NomenclatorService nomSrv;

    @Override
    public AdministratifAddress findAddressByCUA(String cua, String sirutaJudet, String sirutaLocalitate) throws RanException, RanRuntimeException {
        try {
            GetAddressWSRequest req = new GetAddressWSRequest();
            req.setCua(cua);

            NomJudet nomJudet = nomSrv.getNomenclatorForStringParam(NomenclatorCodeType.NomJudet, sirutaJudet);
            if (nomJudet != null) {
                req.setCounty(nomJudet.getDenumire());
            } else {
                return null;
            }

            if (null != sirutaLocalitate) {
                NomLocalitate nomLocalitate = nomSrv.getNomenclatorForStringParam(NomenclatorCodeType.NomLocalitate, sirutaLocalitate);
                if (nomLocalitate != null) {
                    req.setLocalityName(nomLocalitate.getDenumire());
                } else {
                    return null;
                }
            }

            if (env.getProperty("mock.renns", Boolean.class, true)) {
                AdministratifAddress administratifAddress = new AdministratifAddress();
                if ("12345".equals(cua)) {
                    // returns dummy administratif address
                    administratifAddress.setAdministratifNo("14A");
                    administratifAddress.setStreetName("Aleea Banul Udrea");
                    //<gml:Point srsName="EPSG:3844" xmlns:gml="http://www.opengis.net/gml"><gml:pos srsDimension="2">337751.345830827 476285.760315125 </gml:pos></gml:Point>
                    administratifAddress.setGml311String(GeometrieHelper.buildGmlPoint("337751.345830827", "476285.760315125"));
                    return administratifAddress;
                } else if ("54321".equals(cua)) {
                    // returns empty administratif address
                    return administratifAddress;
                }
            }
            GetAddressWSResponse rsp = rennsAddressesService.findAddressByCua(req);
            List<AddressWS> addresses = rsp.getAddressWS();
            if (null != addresses && !addresses.isEmpty()) {
                AddressWS addressWS = addresses.get(0);

                AdministratifAddress administratifAddress = new AdministratifAddress();
                administratifAddress.setAdministratifNo(addressWS.getAdministratifNo());
                administratifAddress.setStreetName(addressWS.getName());
                //
                administratifAddress.setGml311String(GeometrieHelper.buildGmlPoint(addressWS.getXCordinate(), addressWS.getYCordinate()));
                return administratifAddress;
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RanRuntimeException(e);
        }
    }


}
