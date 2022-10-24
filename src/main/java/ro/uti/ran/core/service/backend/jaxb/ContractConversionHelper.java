package ro.uti.ran.core.service.backend.jaxb;

import org.apache.commons.lang.StringUtils;
import ro.uti.ran.core.model.registru.NomCategorieFolosinta;
import ro.uti.ran.core.xml.model.types.INTREG_POZITIV;
import ro.uti.ran.core.xml.model.types.NString;

import java.math.BigDecimal;

/**
 * Created by Dan on 02-Nov-15.
 */
public class ContractConversionHelper {

    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param contractEntity entity pojo
     * @param contractJaxb   jaxb pojo
     */
    public static void toSchemaType(ro.uti.ran.core.model.registru.Contract contractEntity, ro.uti.ran.core.xml.model.capitol.nested.Contract contractJaxb) {
        if (contractEntity == null) {
            throw new IllegalArgumentException("Contract entity nedefinit!");
        }
        if (contractJaxb == null) {
            throw new IllegalArgumentException("Contract jaxb nedefinit!");
        }
        /*nrCrt*/
        contractJaxb.setNrCrt(contractEntity.getNrCrt());
        /*nrContract*/
        contractJaxb.setNrContract(new NString(contractEntity.getNrContract()));
        /*dataContract*/
        contractJaxb.setDataContract(contractEntity.getDataContract());
       /*suprafataMP*/
        if (contractEntity.getSuprafata() != null) {
            INTREG_POZITIV intreg_pozitiv = new INTREG_POZITIV();
            intreg_pozitiv.setValue(contractEntity.getSuprafata());
            contractJaxb.setSuprafataMP(intreg_pozitiv);
        }
        /*codCategFolosinta*/
        if (contractEntity.getNomCategorieFolosinta() != null) {
            contractJaxb.setCodCategFolosinta(new NString(contractEntity.getNomCategorieFolosinta().getCod()));
        }
        /*dataStart*/
        contractJaxb.setDataStart(contractEntity.getDataStart());
       /*dataStop*/
        contractJaxb.setDataStop(contractEntity.getDataStop());
       /*redeventaLei*/
        if (contractEntity.getRedeventa() != null) {
            contractJaxb.setRedeventaLei(contractEntity.getRedeventa().floatValue());
        }
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param contractJaxb   jaxb pojo
     * @param contractEntity entity pojo
     */
    public static void populeazaFromSchemaType(ro.uti.ran.core.xml.model.capitol.nested.Contract contractJaxb, ro.uti.ran.core.model.registru.Contract contractEntity) {
        if (contractEntity == null) {
            throw new IllegalArgumentException("Contract entity nedefinit!");
        }
        if (contractJaxb == null) {
            throw new IllegalArgumentException("Contract jaxb nedefinit!");
        }
        /*nrCrt*/
        contractEntity.setNrCrt(contractJaxb.getNrCrt());
        /*nrContract*/
        contractEntity.setNrContract(contractJaxb.getNrContract().getValue());
        /*dataContract*/
        contractEntity.setDataContract(contractJaxb.getDataContract());
       /*suprafataMP*/
        if (contractJaxb.getSuprafataMP() != null) {
            contractEntity.setSuprafata(contractJaxb.getSuprafataMP().getValue());
        }
        /*codCategFolosinta*/
        if (StringUtils.isNotEmpty(contractJaxb.getCodCategFolosinta().getValue())) {
            NomCategorieFolosinta nomCategorieFolosinta = new NomCategorieFolosinta();
            nomCategorieFolosinta.setCod(contractJaxb.getCodCategFolosinta().getValue());
            contractEntity.setNomCategorieFolosinta(nomCategorieFolosinta);
        }
        /*dataStart*/
        contractEntity.setDataStart(contractJaxb.getDataStart());
       /*dataStop*/
        contractEntity.setDataStop(contractJaxb.getDataStop());
       /*redeventaLei*/
        if (contractJaxb.getRedeventaLei() != null) {
            contractEntity.setRedeventa(BigDecimal.valueOf(contractJaxb.getRedeventaLei()));
        }
    }
}
