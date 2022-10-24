package ro.uti.ran.core.service.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.exception.InterogareDateRegistruException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.codes.InterogareDateRegistruCodes;
import ro.uti.ran.core.model.portal.Institutie;
import ro.uti.ran.core.model.portal.Sistem;
import ro.uti.ran.core.model.registru.NomUat;
import ro.uti.ran.core.model.registru.Nomenclator;
import ro.uti.ran.core.repository.portal.SistemRepository;
import ro.uti.ran.core.repository.registru.NomUatRepository;
import ro.uti.ran.core.repository.registru.NomenclatorRepository;
import ro.uti.ran.core.service.backend.capitol.CapitolCentralizatorService;
import ro.uti.ran.core.service.backend.dto.CapitolCentralizatorDTO;
import ro.uti.ran.core.service.backend.jaxb.RanDocConversionHelper;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.ws.internal.RanAuthorization;
import ro.uti.ran.core.xml.model.RanDoc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ro.uti.ran.core.exception.codes.InterogareDateRegistruCodes.GOSPODARIE_NOT_FOUND;
import static ro.uti.ran.core.exception.codes.InterogareDateRegistruCodes.INSUFICIENT_PRIVELEGES;
import static ro.uti.ran.core.exception.codes.InterogareDateRegistruCodes.UNAUTH_ACCESS;

/**
 * Created by smash on 10/11/15.
 */

@Service
@Transactional("registruTransactionManager")
public class InterogareDateCentralizatorServiceImpl implements InterogareDateCentralizatorService {

    @Autowired
    private NomUatRepository nomUatRepository;

    @Autowired
    SistemRepository sistemRepository;

    @Autowired
    private NomenclatorRepository nomenclatorRepository;

    private Map<String, CapitolCentralizatorService> capitolCentralizatorServiceMap = new HashMap<String, CapitolCentralizatorService>();

    @Autowired
    public InterogareDateCentralizatorServiceImpl(List<? extends CapitolCentralizatorService> capitolService) {
        for (CapitolCentralizatorService c : capitolService) {
            capitolCentralizatorServiceMap.put(c.getCapitolClass().getSimpleName(), c);
        }
    }

    /**
     * Inspirat din ro.uti.ran.core.service.backend.InterogareDateServiceImpl.checkGospodarieRight
     *
     * @param codSirutaUAT     cod siruta uat pt care se cer date
     * @param ranAuthorization informatii autorizare
     * @throws InterogareDateRegistruException
     */
    @Override
    public void checkRight(Integer codSirutaUAT, RanAuthorization ranAuthorization) throws InterogareDateRegistruException {
        long idEntity = ranAuthorization.getIdEntity();
        String context = ranAuthorization.getContext().equals("UAT") ? checkUat(idEntity) : ranAuthorization.getContext();
        NomUat nomUat = nomUatRepository.findByCodSiruta(codSirutaUAT);
        if (nomUat == null) {
            throw new InterogareDateRegistruException(InterogareDateRegistruCodes.COD_SIRUTA_INVALID, codSirutaUAT);
        }

        switch (context) {
            case "UAT":
                if (nomUat.getId() != idEntity) {
                    throw new InterogareDateRegistruException(INSUFICIENT_PRIVELEGES, codSirutaUAT);
                }
                break;
            case "JUDET":
                if (nomUat.getNomJudet().getId() != idEntity) {
                    throw new InterogareDateRegistruException(INSUFICIENT_PRIVELEGES, codSirutaUAT);
                }
                break;
            default:
                if (!checkInstRight(ranAuthorization.getIdEntity())) {
                    throw new InterogareDateRegistruException(UNAUTH_ACCESS);
                }
                break;
        }
    }

    /**
     * Copy/Paste din ro.uti.ran.core.service.backend.InterogareDateServiceImpl.checkUat
     *
     * @param idContext
     * @return
     */
    private String checkUat(long idContext) {
        Sistem checkInstance = sistemRepository.findByJudet_Id(idContext);
        if (checkInstance == null) {
            return "UAT";
        } else {
            return "JUDET";
        }

    }

    /**
     * Copy/Paste din ro.uti.ran.core.service.backend.InterogareDateServiceImpl.checkInstRight
     *
     * @param idInst
     * @return
     */
    private boolean checkInstRight(long idInst) {
        Nomenclator institutie = nomenclatorRepository.findOne(Institutie.class, idInst);
        return ((Institutie) institutie).getAccesDatePrimare();
    }

    @Override
    public RanDoc getDateCapitol(Integer codSirutaUAT, Integer an, TipCapitol tipCapitol) throws RanBusinessException {

        if (an == null) {
            throw new UnsupportedOperationException("Anul raportarii trebuie specificat!");
        }
        NomUat nomUat = nomUatRepository.findByCodSiruta(codSirutaUAT);
        if (nomUat == null) {
            throw new InterogareDateRegistruException(InterogareDateRegistruCodes.COD_SIRUTA_INVALID, codSirutaUAT);
        }

        CapitolCentralizatorDTO capitolCentralizatorDTO = new CapitolCentralizatorDTO();
        capitolCentralizatorDTO.setAn(an);
        capitolCentralizatorDTO.setCodSiruta(codSirutaUAT);
        capitolCentralizatorDTO.setTipCapitol(tipCapitol);

        switch (tipCapitol) {
            case CAP12a:
                capitolCentralizatorServiceMap.get(getCapitolClassName(tipCapitol)).populeazaDateCapitol(capitolCentralizatorDTO);
                break;
            case CAP12a1:
                capitolCentralizatorServiceMap.get(getCapitolClassName(tipCapitol)).populeazaDateCapitol(capitolCentralizatorDTO);
                break;
            case CAP12b1:
                capitolCentralizatorServiceMap.get(getCapitolClassName(tipCapitol)).populeazaDateCapitol(capitolCentralizatorDTO);
                break;
            case CAP12b2:
                capitolCentralizatorServiceMap.get(getCapitolClassName(tipCapitol)).populeazaDateCapitol(capitolCentralizatorDTO);
                break;
            case CAP12c:
                capitolCentralizatorServiceMap.get(getCapitolClassName(tipCapitol)).populeazaDateCapitol(capitolCentralizatorDTO);
                break;
            case CAP12d:
                capitolCentralizatorServiceMap.get(getCapitolClassName(tipCapitol)).populeazaDateCapitol(capitolCentralizatorDTO);
                break;
            case CAP12e:
                capitolCentralizatorServiceMap.get(getCapitolClassName(tipCapitol)).populeazaDateCapitol(capitolCentralizatorDTO);
                break;
            case CAP12f:
                capitolCentralizatorServiceMap.get(getCapitolClassName(tipCapitol)).populeazaDateCapitol(capitolCentralizatorDTO);
                break;
            case CAP13cent:
                capitolCentralizatorServiceMap.get(getCapitolClassName(tipCapitol)).populeazaDateCapitol(capitolCentralizatorDTO);
                break;
            default:
                throw new UnsupportedOperationException("Capitolul " + tipCapitol + " nu este implementat!");
        }

        RanDoc ranDoc = RanDocConversionHelper.toSchemaType(capitolCentralizatorDTO);

        return ranDoc;

    }


    private String getCapitolClassName(TipCapitol tipCapitol) {
        try {
            String capitolClassName = tipCapitol.name().replace("CAP01", "Capitol_1").replace("CAP", "Capitol_").replace("cent", "_centralizator");
            return capitolClassName;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
