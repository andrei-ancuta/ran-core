package ro.uti.ran.core.service.backend.jaxb;

import org.apache.commons.lang.StringUtils;
import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.*;
import ro.uti.ran.core.model.utils.TipAct;
import ro.uti.ran.core.xml.model.capitol.nested.Produs;
import ro.uti.ran.core.xml.model.capitol.nested.Viza;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_12;
import ro.uti.ran.core.xml.model.types.NString;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 02-Nov-15.
 */
public class RandCapitol_12ConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param atestat entity pojo
     * @return jaxb pojo
     */
    public static SectiuneCapitol_12 toSchemaType(Atestat atestat) {
        if (atestat == null) {
            throw new IllegalArgumentException("Atestat nedefinit!");
        }
        SectiuneCapitol_12 sectiuneCapitol_12 = new SectiuneCapitol_12();
        /*serieNumar*/
        sectiuneCapitol_12.setSerieNumar(new NString(atestat.getSerieNumarAtestat()));
        /*dataEliberare*/
        sectiuneCapitol_12.setDataEliberare(atestat.getDataEliberareAtestat());
        if (atestat.getAct() != null) {
            /*nrAvizConsulativ*/
            sectiuneCapitol_12.setNrAvizConsulativ(new NString(atestat.getAct().getNumarAct()).getValue());
            /*dataAvizConsultativ*/
            sectiuneCapitol_12.setDataAvizConsultativ(atestat.getAct().getDataAct());
        }
        /*produs*/
        if (atestat.getAtestatProduses() != null) {
            List<Produs> listProduses = new ArrayList<Produs>();
            for (AtestatProdus atestatProdus : atestat.getAtestatProduses()) {
                listProduses.add(ProdusConversionHelper.toSchemaType(atestatProdus));
            }
            if (!listProduses.isEmpty()) {
                sectiuneCapitol_12.setProdus(listProduses);
            }
        }
        /*viza*/
        if (atestat.getAtestatVizas() != null) {
            List<Viza> listVizas = new ArrayList<Viza>();
            for (AtestatViza atestatViza : atestat.getAtestatVizas()) {
                listVizas.add(VizaConversionHelper.toSchemaType(atestatViza));
            }
            if (!listVizas.isEmpty()) {
                sectiuneCapitol_12.setViza(listVizas);
            }
        }
        /*certificatComercializare*/
        if (atestat.getCertificatComs() != null && !atestat.getCertificatComs().isEmpty()) {
            CertificatCom certificatCom = atestat.getCertificatComs().get(0);
            sectiuneCapitol_12.setCertificatComercializare(CertificatComercializareConversionHelper.toSchemaType(certificatCom));
        }
        return sectiuneCapitol_12;
    }

    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param sectiuneCapitol_12 jaxb pojo
     * @param gospodarie         entity pojo
     */
    public static void populeazaFromSchemaType(SectiuneCapitol_12 sectiuneCapitol_12, Gospodarie gospodarie) throws RequestValidationException {
        if (sectiuneCapitol_12 == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"sectiuneCapitol_12");
        }
        if (gospodarie == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"gospodarie");
        }
        Atestat atestat = new Atestat();
        /*serieNumar*/
        atestat.setSerieNumarAtestat(sectiuneCapitol_12.getSerieNumar().getValue());
        /*dataEliberare*/
        atestat.setDataEliberareAtestat(sectiuneCapitol_12.getDataEliberare());
        if (StringUtils.isNotEmpty(sectiuneCapitol_12.getNrAvizConsulativ()) ||
                sectiuneCapitol_12.getDataAvizConsultativ() != null) {
            Act act = new Act();
            atestat.setAct(act);
            /*nrAvizConsulativ*/
            act.setNumarAct(sectiuneCapitol_12.getNrAvizConsulativ());
            /*dataAvizConsultativ*/
            act.setDataAct(sectiuneCapitol_12.getDataAvizConsultativ());
            /**/
            NomTipAct nomTipAct = new NomTipAct();
            nomTipAct.setCod(TipAct.AVIZ_CONSULTATIV.getCod());
            act.setNomTipAct(nomTipAct);
        }
        /*produs*/
        if (sectiuneCapitol_12.getProdus() != null) {
            for (Produs produs : sectiuneCapitol_12.getProdus()) {
                ProdusConversionHelper.populeazaFromSchemaType(produs, atestat);
            }
        }
        /*viza*/
        if (sectiuneCapitol_12.getViza() != null) {
            for (Viza viza : sectiuneCapitol_12.getViza()) {
                VizaConversionHelper.populeazaFromSchemaType(viza, atestat);
            }
        }
        /*certificatComercializare*/
        if (sectiuneCapitol_12.getCertificatComercializare() != null) {
            CertificatComercializareConversionHelper.populeazaFromSchemaType(sectiuneCapitol_12.getCertificatComercializare(), atestat);
        }
        /*populare entity pojo*/
        gospodarie.addAtestat(atestat);
    }
}
