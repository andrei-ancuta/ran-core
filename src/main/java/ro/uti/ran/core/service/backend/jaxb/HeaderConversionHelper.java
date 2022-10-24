package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.DateRegistruValidationCodes;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.utils.IndicativXml;
import ro.uti.ran.core.service.backend.dto.CapitolCentralizatorDTO;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;
import ro.uti.ran.core.service.backend.exception.SyntaxXmlException;
import ro.uti.ran.core.xml.model.Header;
import ro.uti.ran.core.xml.model.types.TipIndicativ;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Dan on 13-Oct-15.
 */
public class HeaderConversionHelper {


    public static Header toSchemaType(CapitolCentralizatorDTO capitolCentralizatorDTO) {
        if (capitolCentralizatorDTO == null) {
            throw new IllegalArgumentException("GospodarieDTO nedefinit!");
        }
        Header header = new Header();
        /*codXml*/
        ro.uti.ran.core.xml.model.types.UUID uuid = new ro.uti.ran.core.xml.model.types.UUID();
        uuid.setValue(UUID.randomUUID().toString());
        header.setCodXml(uuid);
        /*sirutaUAT*/
        header.setSirutaUAT(capitolCentralizatorDTO.getCodSiruta());
        /*dataExport*/
        header.setDataExport(new Date());
        /*indicativ*/
        header.setIndicativ(TipIndicativ.INCARCA);
        return header;
    }

    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static Header toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("GospodarieDTO nedefinit!");
        }
        Header header = new Header();
        /*codXml*/
        ro.uti.ran.core.xml.model.types.UUID uuid = new ro.uti.ran.core.xml.model.types.UUID();
        uuid.setValue(UUID.randomUUID().toString());
        header.setCodXml(uuid);
        /*sirutaUAT*/
        header.setSirutaUAT(gospodarieDTO.getGospodarie().getNomUat().getCodSiruta());
        /*dataExport*/
        header.setDataExport(new Date());
        /*indicativ*/
        header.setIndicativ(TipIndicativ.INCARCA);
        return header;
    }

    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param header    jaxb pojo
     * @param ranDocDTO entity pojo
     */
    public static void populeazaFromSchemaType(Header header, RanDocDTO ranDocDTO) throws RequestValidationException {
        if (header == null) {
            throw new IllegalArgumentException("Header nedefinit!");
        }
        if (ranDocDTO == null) {
            throw new IllegalArgumentException("RanDocDTO nedefinit!");
        }
        if (null == header.getCodXml()) {
            throw new RequestValidationException(RequestCodes.HEADER_ELEMENT_MISSING, "codXml");
        }
        if (null == header.getSirutaUAT()) {
            throw new RequestValidationException(RequestCodes.HEADER_ELEMENT_MISSING, "sirutaUAT");
        }
        if (null == header.getDataExport()) {
            throw new RequestValidationException(RequestCodes.HEADER_ELEMENT_MISSING, "dataExport");
        }
        if (null == header.getIndicativ()) {
            throw new RequestValidationException(RequestCodes.HEADER_ELEMENT_MISSING, "indicativ");
        }

        ranDocDTO.setCodXml(UUID.fromString(header.getCodXml().getValue()));
        ranDocDTO.setDataExport(header.getDataExport());
        switch (header.getIndicativ()) {
            case ADAUGA_SI_INLOCUIESTE:
                ranDocDTO.setIndicativ(IndicativXml.ADAUGA_SI_INLOCUIESTE);
                break;
            case ANULEAZA:
                ranDocDTO.setIndicativ(IndicativXml.ANULEAZA);
                break;
            case DEZACTIVARE_GOSPODARIE:
                ranDocDTO.setIndicativ(IndicativXml.DEZACTIVARE);
                break;
            case REACTIVARE_GOSPODARIE:
                ranDocDTO.setIndicativ(IndicativXml.REACTIVARE);
                break;

        }
        ranDocDTO.setSirutaUAT(header.getSirutaUAT());
    }
}
