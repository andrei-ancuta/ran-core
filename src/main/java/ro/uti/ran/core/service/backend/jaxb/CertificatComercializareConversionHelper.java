package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.model.registru.Atestat;
import ro.uti.ran.core.model.registru.CertificatCom;
import ro.uti.ran.core.xml.model.capitol.nested.CertificatComercializare;
import ro.uti.ran.core.xml.model.types.NString;

/**
 * Created by Dan on 02-Nov-15.
 */
public class CertificatComercializareConversionHelper {

    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param certificatCom entity pojo
     * @return jaxb pojo
     */
    public static CertificatComercializare toSchemaType(CertificatCom certificatCom) {
        if (certificatCom == null) {
            throw new IllegalArgumentException("CertificatCom  nedefinit!");
        }
        CertificatComercializare certificatComercializare = new CertificatComercializare();
        certificatComercializare.setSerie(new NString(certificatCom.getSerie()));
        certificatComercializare.setDataEliberare(certificatCom.getDataEliberare());
        return certificatComercializare;
    }

    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param certificatComercializare jaxb pojo
     * @param atestat                  entity pojo
     */
    public static void populeazaFromSchemaType(CertificatComercializare certificatComercializare, Atestat atestat) {
        if (certificatComercializare == null) {
            throw new IllegalArgumentException("CertificatComercializare nedefinit!");
        }
        if (atestat == null) {
            throw new IllegalArgumentException("Atestat nedefinit!");
        }
        CertificatCom certificatCom = new CertificatCom();
        certificatCom.setDataEliberare(certificatComercializare.getDataEliberare());
        certificatCom.setSerie(certificatComercializare.getSerie().getValue());
        atestat.addCertificatCom(certificatCom);
    }
}
