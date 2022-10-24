package ro.uti.ran.core.service.backend;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.exception.InterogareDateRegistruException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.model.portal.Institutie;
import ro.uti.ran.core.model.portal.Sistem;
import ro.uti.ran.core.model.registru.DetinatorPf;
import ro.uti.ran.core.model.registru.DetinatorPj;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.Nomenclator;
import ro.uti.ran.core.repository.portal.SistemRepository;
import ro.uti.ran.core.repository.registru.DetinatorPfRepository;
import ro.uti.ran.core.repository.registru.DetinatorPjRepository;
import ro.uti.ran.core.repository.registru.GospodarieRepository;
import ro.uti.ran.core.repository.registru.NomenclatorRepository;
import ro.uti.ran.core.service.backend.capitol.CapitolService;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.service.backend.dto.ParametriiInterogare;
import ro.uti.ran.core.service.backend.jaxb.RanDocConversionHelper;
import ro.uti.ran.core.service.backend.nomenclator.NomenclatorService;
import ro.uti.ran.core.service.backend.utils.DataRaportareValabilitate;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.ws.internal.RanAuthorization;
import ro.uti.ran.core.ws.model.interogare.IdentificatorGospodarie;
import ro.uti.ran.core.ws.model.interogare.IdentificatorPF;
import ro.uti.ran.core.xml.model.AnRaportare;
import ro.uti.ran.core.xml.model.RanDoc;
import ro.uti.ran.core.xml.model.capitol.*;
import ro.uti.ran.core.xml.model.types.CNP;
import ro.uti.ran.core.xml.model.types.NIF;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ro.uti.ran.core.exception.codes.InterogareDateRegistruCodes.*;
import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.NomCapitol;

/**
 * Created by Dan on 19-Oct-15.
 */
@Service
@Transactional("registruTransactionManager")
public class InterogareDateServiceImpl implements InterogareDateService {

    private static final Logger logger = LoggerFactory.getLogger(InterogareDateServiceImpl.class);
    @Autowired
    SistemRepository sistemRepository;
    @Autowired
    private NomenclatorService nomSrv;
    @Autowired
    private GospodarieRepository gospodarieRepository;
    @Autowired
    private DetinatorPfRepository detinatorPfRepository;
    @Autowired
    private DetinatorPjRepository detinatorPjRepository;
    @Autowired
    private NomenclatorRepository nomenclatorRepository;
    @PersistenceContext(unitName = "ran-registru")

    private EntityManager em;
    private Map<String, CapitolService> capitolServiceMap = new HashMap<String, CapitolService>();


    @Autowired
    public InterogareDateServiceImpl(List<? extends CapitolService> capitolServices) {
        for (CapitolService capitolService : capitolServices) {
            this.capitolServiceMap.put(capitolService.getCapitolClass().getSimpleName(), capitolService);
        }
    }

    public ArrayList<IdentificatorGospodarie> getListaGospodariiPF(IdentificatorPF identificatorPF, Boolean activ, RanAuthorization ranAuthorization) throws InterogareDateRegistruException {
        String sql = "select g.IDENTIFICATOR, g.IS_ACTIV, uat.COD_SIRUTA, uat.DENUMIRE " +
                "from gospodarie g " +
                "join detinator_pf dpf on dpf.fk_gospodarie = g.id_gospodarie " +
                "join PERSOANA_FIZICA pf on pf.ID_PERSOANA_FIZICA = dpf.FK_PERSOANA_FIZICA " +
                "join nom_uat uat on uat.id_nom_uat = g.fk_nom_uat where ";

        String id;
        if (identificatorPF.getIdentificator() instanceof CNP) {
            sql += "pf.cnp = ?";
            id = ((CNP) identificatorPF.getIdentificator()).getValue();
        } else if (identificatorPF.getIdentificator() instanceof NIF) {
            sql += "pf.nif = ?";
            id = ((NIF) identificatorPF.getIdentificator()).getValue();
        } else {
            throw new java.lang.UnsupportedOperationException("Invalid identifier provided of type: " + identificatorPF.getIdentificator().getClass());
        }
        if (activ != null) {
            if (activ) {
                sql += " and g.IS_ACTIV = 1 ";
            } else {
                sql += " and g.IS_ACTIV = 0 ";
            }
        }
        //block autorizare
        long idEntity = 0l;
        String context = null;

        idEntity = ranAuthorization.getIdEntity();
        context = ranAuthorization.getContext().equals("UAT") ? checkUat(idEntity) : ranAuthorization.getContext();
        switch (context) {
            case "UAT":
                sql += " and g.fk_nom_uat = ?";
                break;
            case "JUDET":
                sql += " and g.fk_nom_judet = ?";
                break;
            default:
                if (!checkInstRight(ranAuthorization.getIdEntity())) {
                    throw new InterogareDateRegistruException(UNAUTH_ACCESS);
                }
                break;
        }

        Query query = em.createNativeQuery(sql, HashMap.class);
        query.setParameter(1, id);
        if (context.equals("UAT") || context.equals("JUDET")) {
            query.setParameter(2, idEntity);
        }

        return getIdGospodarii(query);
    }

    public ArrayList<IdentificatorGospodarie> getListaGospodariiPJ(String cui, Boolean activ, RanAuthorization ranAuthorization) throws InterogareDateRegistruException {
        String sql = "select g.IDENTIFICATOR, g.IS_ACTIV, uat.COD_SIRUTA, uat.DENUMIRE " +
                "from gospodarie g " +
                "join DETINATOR_PJ dpj on dpj.fk_gospodarie = g.id_gospodarie " +
                "join PERSOANA_RC rc on rc.ID_PERSOANA_RC = dpj.FK_PERSOANA_RC " +
                "join nom_uat uat on uat.id_nom_uat = g.fk_nom_uat where rc.cui = ?";

        //block autorizare
        long idEntity = 0l;
        String context = null;
        idEntity = ranAuthorization.getIdEntity();
        context = ranAuthorization.getContext().equals("UAT") ? checkUat(idEntity) : ranAuthorization.getContext();
        switch (context) {
            case "UAT":
                sql += " and g.fk_nom_uat = ?";
                break;
            case "JUDET":
                sql += " and g.fk_nom_judet = ?";
                break;
            default:
                if (!checkInstRight(ranAuthorization.getIdEntity())) {
                    throw new InterogareDateRegistruException(UNAUTH_ACCESS);
                }
                break;
        }

        if (activ != null) {
            if (activ) {
                sql += " and g.IS_ACTIV = 1 ";
            } else {
                sql += " and g.IS_ACTIV = 0 ";
            }
        }


        Query query = em.createNativeQuery(sql, HashMap.class);
        query.setParameter(1, cui);

        if (context.equals("UAT") || context.equals("JUDET")) {
            query.setParameter(2, idEntity);
        }
        return getIdGospodarii(query);
    }

    public IdentificatorGospodarie getGospodariePJ(String cui, Integer sirutaUAT, RanAuthorization ranAuthorization) throws InterogareDateRegistruException {

        List<DetinatorPj> detinatorPjList = detinatorPjRepository.findByCui(cui);
        if (detinatorPjList == null || detinatorPjList.isEmpty()) {
            throw new InterogareDateRegistruException(CUI_NOT_FOUND, cui);
        }

        String sql = "select g.IDENTIFICATOR, g.IS_ACTIV, uat.COD_SIRUTA, uat.DENUMIRE " +
                "from gospodarie g " +
                "join DETINATOR_PJ dpj on dpj.fk_gospodarie = g.id_gospodarie " +
                "join PERSOANA_RC rc on rc.ID_PERSOANA_RC = dpj.FK_PERSOANA_RC " +
                "join nom_uat uat on uat.id_nom_uat = g.fk_nom_uat " +
                "where rc.cui = ? and uat.cod_siruta = ? and rownum = 1";
        //block autorizare
        long idEntity = 0l;
        String context = null;

        idEntity = ranAuthorization.getIdEntity();
        context = ranAuthorization.getContext().equals("UAT") ? checkUat(idEntity) : ranAuthorization.getContext();
        switch (context) {
            case "UAT":
                sql += " and g.fk_nom_uat = ?";
                break;
            case "JUDET":
                sql += " and g.fk_nom_judet = ?";
                break;
            default:
                if (!checkInstRight(ranAuthorization.getIdEntity())) {
                    throw new InterogareDateRegistruException(UNAUTH_ACCESS);
                }
                break;
        }


        Query query = em.createNativeQuery(sql, HashMap.class);
        query.setParameter(1, cui);
        query.setParameter(2, sirutaUAT);
        if (context.equals("UAT") || context.equals("JUDET")) {
            query.setParameter(3, idEntity);
        }
        return getIdentificatorGospodarie(query);
    }

    public IdentificatorGospodarie getGospodariePF(IdentificatorPF identificatorPF, Integer sirutaUAT, RanAuthorization ranAuthorization) throws InterogareDateRegistruException {

        List<DetinatorPf> detinatorPfList;
        String idName;
        String idValue;
        if (identificatorPF.getIdentificator() instanceof CNP) {
            idName = "cnp";
            idValue = ((CNP) identificatorPF.getIdentificator()).getValue();
            detinatorPfList = detinatorPfRepository.findByCnp(idValue);
        } else if (identificatorPF.getIdentificator() instanceof NIF) {
            idName = "nif";
            idValue = ((NIF) identificatorPF.getIdentificator()).getValue();
            detinatorPfList = detinatorPfRepository.findByNif(idValue);
        } else {
            throw new java.lang.UnsupportedOperationException("Invalid identificator PF");
        }

        if (detinatorPfList == null || detinatorPfList.isEmpty()) {
            if (identificatorPF.getIdentificator() instanceof CNP) {
                throw new InterogareDateRegistruException(CNP_NOT_FOUND, idValue);
            } else {
                throw new InterogareDateRegistruException(NIF_NOT_FOUND, idValue);
            }

        }

        String sql = "select g.IDENTIFICATOR, g.IS_ACTIV, uat.COD_SIRUTA, uat.DENUMIRE " +
                "from gospodarie g " +
                "join detinator_pf dpf on dpf.fk_gospodarie = g.id_gospodarie " +
                "join PERSOANA_FIZICA pf on pf.ID_PERSOANA_FIZICA = dpf.FK_PERSOANA_FIZICA " +
                "join nom_uat uat on uat.id_nom_uat = g.fk_nom_uat " +
                "where pf." + idName + " = ? and uat.cod_siruta = ? and rownum = 1";
        //block autorizare
        long idEntity = 0l;
        String context = null;
        idEntity = ranAuthorization.getIdEntity();
        context = ranAuthorization.getContext().equals("UAT") ? checkUat(idEntity) : ranAuthorization.getContext();
        switch (context) {
            case "UAT":
                sql += " and g.fk_nom_uat = ?";
                break;
            case "JUDET":
                sql += " and g.fk_nom_judet = ?";
                break;
            default:
                if (!checkInstRight(ranAuthorization.getIdEntity())) {
                    throw new InterogareDateRegistruException(UNAUTH_ACCESS);
                }
                break;
        }

        Query query = em.createNativeQuery(sql, HashMap.class);
        query.setParameter(1, idValue);
        query.setParameter(2, sirutaUAT);
        if (context.equals("UAT") || context.equals("JUDET")) {
            query.setParameter(3, idEntity);
        }
        return getIdentificatorGospodarie(query);
    }

    @Override
    public void checkGospodarieRight(int codSiruta, String identificatorGospodarie, RanAuthorization ranAuthorization) throws InterogareDateRegistruException {
        long idEntity = ranAuthorization.getIdEntity();
        String context = ranAuthorization.getContext().equals("UAT") ? checkUat(idEntity) : ranAuthorization.getContext();
        Gospodarie gospodarie = gospodarieRepository.findByUatAndIdentificator(codSiruta, identificatorGospodarie);
        if (gospodarie == null) {
            throw new InterogareDateRegistruException(GOSPODARIE_NOT_FOUND, identificatorGospodarie, codSiruta);
        }

        switch (context) {
            case "UAT":
                if (gospodarie.getNomUat().getId() != idEntity) {
                    throw new InterogareDateRegistruException(INSUFICIENT_PRIVELEGES, codSiruta);
                }
                break;
            case "JUDET":
                if (gospodarie.getNomJudet().getId() != idEntity) {
                    throw new InterogareDateRegistruException(INSUFICIENT_PRIVELEGES, codSiruta);
                }
                break;
            default:
                if (!checkInstRight(ranAuthorization.getIdEntity())) {
                    throw new InterogareDateRegistruException(UNAUTH_ACCESS);
                }
                break;
        }
    }

    private IdentificatorGospodarie getIdentificatorGospodarie(Query query) {
        List<Map> results = (List<Map>) query.getResultList();

        if (null != results && 1 == results.size()) {
            Map result = results.get(0);
            IdentificatorGospodarie identificatorGospodarie = new IdentificatorGospodarie();
            identificatorGospodarie.setId(MapUtils.getString(result, "IDENTIFICATOR"));
            identificatorGospodarie.setSirutaUAT(MapUtils.getInteger(result, "COD_SIRUTA"));
            identificatorGospodarie.setDenumireUAT(MapUtils.getString(result, "DENUMIRE"));
            identificatorGospodarie.setActiv(MapUtils.getBooleanValue(result, "IS_ACTIV"));
            return identificatorGospodarie;
        } else if (null != results && results.size() > 1) {
            throw new java.lang.UnsupportedOperationException("getGospodariePF returns more than 1 result (actual: " + results.size() + ")");
        }

        return null;
    }

    private ArrayList<IdentificatorGospodarie> getIdGospodarii(Query query) {
        List<Map> results = query.getResultList();

        ArrayList<IdentificatorGospodarie> idGospodarii = new ArrayList<IdentificatorGospodarie>();
        if (results != null && !results.isEmpty()) {
            for (Map result : results) {
                IdentificatorGospodarie identificatorGospodarie = new IdentificatorGospodarie();
                identificatorGospodarie.setId(MapUtils.getString(result, "IDENTIFICATOR"));
                identificatorGospodarie.setSirutaUAT(MapUtils.getInteger(result, "COD_SIRUTA"));
                identificatorGospodarie.setDenumireUAT(MapUtils.getString(result, "DENUMIRE"));
                identificatorGospodarie.setActiv(MapUtils.getBooleanValue(result, "IS_ACTIV"));
                idGospodarii.add(identificatorGospodarie);
            }
        }

        return idGospodarii;
    }

    /**
     * @see InterogareDateService
     */
    @Override
    public RanDoc getDateCapitol(ParametriiInterogare parametriiInterogare) throws RanBusinessException {
        /*GOSPODARIE*/
        GospodarieDTO gospodarieDTO = new GospodarieDTO();
        Gospodarie gospodarie = gospodarieRepository.findByUatAndIdentificator(parametriiInterogare.getCodSirutaUAT(), parametriiInterogare.getIdentificatorGospodarie());
        if (gospodarie == null) {
            throw new InterogareDateRegistruException(GOSPODARIE_NOT_FOUND, parametriiInterogare.getIdentificatorGospodarie(), parametriiInterogare.getCodSirutaUAT());
        }
        gospodarieDTO.setGospodarie(gospodarie);
        /*Capitolul*/
        switch (parametriiInterogare.getTipCapitol()) {
            case CAP0_12:
            case CAP0_34:
            case CAP1:
            case CAP12:
            case CAP13:
            case CAP14:
            case CAP15a:
            case CAP15b:
            case CAP16:
                if (parametriiInterogare.getAn() != null || parametriiInterogare.getSemestru() != null) {
                    throw new java.lang.UnsupportedOperationException("Pentru interogare capitol " + parametriiInterogare.getTipCapitol() + " NU este necesar ca parametru: an/semestru raportare.");
                } else {
                    capitolServiceMap.get(getCapitolClassName(parametriiInterogare.getTipCapitol())).populeazaDateCapitol(gospodarieDTO);
                }
                break;
            case CAP2a:
            case CAP2b:
            case CAP3:
            case CAP4a:
            case CAP4a1:
            case CAP4b1:
            case CAP4b2:
            case CAP4c:
            case CAP5a:
            case CAP5b:
            case CAP5c:
            case CAP5d:
            case CAP6:
            case CAP9:
            case CAP10a:
            case CAP10b:
            case CAP11:
                if (parametriiInterogare.getAn() != null) {
                    capitolServiceMap.get(getCapitolClassName(parametriiInterogare.getTipCapitol())).populeazaDateCapitol(gospodarieDTO, parametriiInterogare.getAn());
                } else {
                    throw new java.lang.UnsupportedOperationException("Pentru interogare capitol " + parametriiInterogare.getTipCapitol() + " este necesar ca parametru: an raportare.");
                }
                break;
            case CAP7:
            case CAP8:
                if (parametriiInterogare.getAn() != null && parametriiInterogare.getSemestru() != null) {
                    capitolServiceMap.get(getCapitolClassName(parametriiInterogare.getTipCapitol())).populeazaDateCapitol(gospodarieDTO, parametriiInterogare.getAn(), parametriiInterogare.getSemestru());
                } else {
                    throw new java.lang.UnsupportedOperationException("Pentru interogare capitol " + parametriiInterogare.getTipCapitol() + " sunt necesari ca parametrii: an raportare, semestru raportare.");
                }
                break;
            default:
                throw new java.lang.UnsupportedOperationException("Capitolul " + parametriiInterogare.getTipCapitol() + " nu este implementat pentru getDateCapitol(" + parametriiInterogare.getTipCapitol() + ")");
        }

        RanDoc ranDoc = RanDocConversionHelper.toSchemaType(gospodarieDTO, parametriiInterogare.getTipCapitol(), parametriiInterogare.getAn(), (parametriiInterogare.getSemestru() != null ? parametriiInterogare.getSemestru().intValue() : null));
        if (ranDoc.getBody().

                getGospodarieSauRaportare()

                instanceof ro.uti.ran.core.xml.model.Gospodarie)

        {
            ro.uti.ran.core.xml.model.Gospodarie gospodarieJaxb = (ro.uti.ran.core.xml.model.Gospodarie) ranDoc.getBody().getGospodarieSauRaportare();
            valideaza(gospodarieJaxb.getUnitateGospodarie(), parametriiInterogare);
            if (gospodarieJaxb.getUnitateGospodarie() instanceof Capitol) {
                Capitol capitol = (Capitol) gospodarieJaxb.getUnitateGospodarie();
                capitol.setCodCapitol(parametriiInterogare.getTipCapitol().toString());
                ro.uti.ran.core.model.registru.NomCapitol nomCapitol = nomSrv.getNomenclatorForStringParam(NomCapitol, parametriiInterogare.getTipCapitol().toString(), new DataRaportareValabilitate());
                capitol.setDenumire(nomCapitol.getDenumire());
            }
            if (gospodarieJaxb.getUnitateGospodarie() instanceof AnRaportare) {
                AnRaportare anRaportare = (AnRaportare) gospodarieJaxb.getUnitateGospodarie();
                Capitol capitol = (Capitol) anRaportare.getCapitolCuAnRaportare();
                if (capitol != null) {
                    capitol.setCodCapitol(parametriiInterogare.getTipCapitol().toString());
                    ro.uti.ran.core.model.registru.NomCapitol nomCapitol = nomSrv.getNomenclatorForStringParam(NomCapitol, parametriiInterogare.getTipCapitol().toString(), new DataRaportareValabilitate());
                    capitol.setDenumire(nomCapitol.getDenumire());
                }
            }
        }

        return ranDoc;
    }

    /**
     * @param unitateGospodarie    informatia extrasa din baza  in urma interogarii
     * @param parametriiInterogare parametri de interogare
     * @throws RanBusinessException eroare in urma unei validari business
     */
    private void valideaza(Object unitateGospodarie, ParametriiInterogare parametriiInterogare) throws RanBusinessException {
        Capitol capitol = null;
        if (unitateGospodarie instanceof Capitol) {
            capitol = (Capitol) unitateGospodarie;
        }
        if (unitateGospodarie instanceof AnRaportare) {
            AnRaportare anRaportare = (AnRaportare) unitateGospodarie;
            capitol = (Capitol) anRaportare.getCapitolCuAnRaportare();
        }
        switch (parametriiInterogare.getTipCapitol()) {
            case CAP0_12:
                //daca e de validat ceva aici se face
                break;
            case CAP0_34:
                //daca e de validat ceva aici se face
                break;
            case CAP1:
                if (unitateGospodarie == null || capitol == null || ((Capitol_1) capitol).getRandCapitol() == null || ((Capitol_1) capitol).getRandCapitol().isEmpty()) {
                    throw new InterogareDateRegistruException(CAPITOL_NOT_FOUND, parametriiInterogare.getTipCapitol().name(), parametriiInterogare.getIdentificatorGospodarie(), parametriiInterogare.getCodSirutaUAT());
                }
                break;
            case CAP2a:
                if (unitateGospodarie == null || capitol == null || ((Capitol_2a) capitol).getRandCapitol() == null || ((Capitol_2a) capitol).getRandCapitol().isEmpty()) {
                    throw new InterogareDateRegistruException(CAPITOL_AN_NOT_FOUND, parametriiInterogare.getTipCapitol().name(), parametriiInterogare.getIdentificatorGospodarie(), parametriiInterogare.getCodSirutaUAT(), parametriiInterogare.getAn());
                }
                break;
            case CAP2b:
                if (unitateGospodarie == null || capitol == null || ((Capitol_2b) capitol).getRandCapitol() == null || ((Capitol_2b) capitol).getRandCapitol().isEmpty()) {
                    throw new InterogareDateRegistruException(CAPITOL_AN_NOT_FOUND, parametriiInterogare.getTipCapitol().name(), parametriiInterogare.getIdentificatorGospodarie(), parametriiInterogare.getCodSirutaUAT(), parametriiInterogare.getAn());
                }
                break;
            case CAP3:
                if (unitateGospodarie == null || capitol == null || ((Capitol_3) capitol).getRandCapitol() == null || ((Capitol_3) capitol).getRandCapitol().isEmpty()) {
                    throw new InterogareDateRegistruException(CAPITOL_AN_NOT_FOUND, parametriiInterogare.getTipCapitol().name(), parametriiInterogare.getIdentificatorGospodarie(), parametriiInterogare.getCodSirutaUAT(), parametriiInterogare.getAn());
                }
                break;
            case CAP4a:
                if (unitateGospodarie == null || capitol == null || ((Capitol_4a) capitol).getRandCapitol() == null || ((Capitol_4a) capitol).getRandCapitol().isEmpty()) {
                    throw new InterogareDateRegistruException(CAPITOL_AN_NOT_FOUND, parametriiInterogare.getTipCapitol().name(), parametriiInterogare.getIdentificatorGospodarie(), parametriiInterogare.getCodSirutaUAT(), parametriiInterogare.getAn());
                }
                break;
            case CAP4a1:
                if (unitateGospodarie == null || capitol == null || ((Capitol_4a1) capitol).getRandCapitol() == null || ((Capitol_4a1) capitol).getRandCapitol().isEmpty()) {
                    throw new InterogareDateRegistruException(CAPITOL_AN_NOT_FOUND, parametriiInterogare.getTipCapitol().name(), parametriiInterogare.getIdentificatorGospodarie(), parametriiInterogare.getCodSirutaUAT(), parametriiInterogare.getAn());
                }
                break;
            case CAP4b1:
                if (unitateGospodarie == null || capitol == null || ((Capitol_4b1) capitol).getRandCapitol() == null || ((Capitol_4b1) capitol).getRandCapitol().isEmpty()) {
                    throw new InterogareDateRegistruException(CAPITOL_AN_NOT_FOUND, parametriiInterogare.getTipCapitol().name(), parametriiInterogare.getIdentificatorGospodarie(), parametriiInterogare.getCodSirutaUAT(), parametriiInterogare.getAn());
                }
                break;
            case CAP4b2:
                if (unitateGospodarie == null || capitol == null || ((Capitol_4b2) capitol).getRandCapitol() == null || ((Capitol_4b2) capitol).getRandCapitol().isEmpty()) {
                    throw new InterogareDateRegistruException(CAPITOL_AN_NOT_FOUND, parametriiInterogare.getTipCapitol().name(), parametriiInterogare.getIdentificatorGospodarie(), parametriiInterogare.getCodSirutaUAT(), parametriiInterogare.getAn());
                }
                break;
            case CAP4c:
                if (unitateGospodarie == null || capitol == null || ((Capitol_4c) capitol).getRandCapitol() == null || ((Capitol_4c) capitol).getRandCapitol().isEmpty()) {
                    throw new InterogareDateRegistruException(CAPITOL_AN_NOT_FOUND, parametriiInterogare.getTipCapitol().name(), parametriiInterogare.getIdentificatorGospodarie(), parametriiInterogare.getCodSirutaUAT(), parametriiInterogare.getAn());
                }
                break;
            case CAP5a:
                if (unitateGospodarie == null || capitol == null || ((Capitol_5a) capitol).getRandCapitol() == null || ((Capitol_5a) capitol).getRandCapitol().isEmpty()) {
                    throw new InterogareDateRegistruException(CAPITOL_AN_NOT_FOUND, parametriiInterogare.getTipCapitol().name(), parametriiInterogare.getIdentificatorGospodarie(), parametriiInterogare.getCodSirutaUAT(), parametriiInterogare.getAn());
                }
                break;
            case CAP5b:
                if (unitateGospodarie == null || capitol == null || ((Capitol_5b) capitol).getRandCapitol() == null || ((Capitol_5b) capitol).getRandCapitol().isEmpty()) {
                    throw new InterogareDateRegistruException(CAPITOL_AN_NOT_FOUND, parametriiInterogare.getTipCapitol().name(), parametriiInterogare.getIdentificatorGospodarie(), parametriiInterogare.getCodSirutaUAT(), parametriiInterogare.getAn());
                }
                break;
            case CAP5c:
                if (unitateGospodarie == null || capitol == null || ((Capitol_5c) capitol).getRandCapitol() == null || ((Capitol_5c) capitol).getRandCapitol().isEmpty()) {
                    throw new InterogareDateRegistruException(CAPITOL_AN_NOT_FOUND, parametriiInterogare.getTipCapitol().name(), parametriiInterogare.getIdentificatorGospodarie(), parametriiInterogare.getCodSirutaUAT(), parametriiInterogare.getAn());
                }
                break;
            case CAP5d:
                if (unitateGospodarie == null || capitol == null || ((Capitol_5d) capitol).getRandCapitol() == null || ((Capitol_5d) capitol).getRandCapitol().isEmpty()) {
                    throw new InterogareDateRegistruException(CAPITOL_AN_NOT_FOUND, parametriiInterogare.getTipCapitol().name(), parametriiInterogare.getIdentificatorGospodarie(), parametriiInterogare.getCodSirutaUAT(), parametriiInterogare.getAn());
                }
                break;
            case CAP6:
                if (unitateGospodarie == null || capitol == null || ((Capitol_6) capitol).getRandCapitol() == null || ((Capitol_6) capitol).getRandCapitol().isEmpty()) {
                    throw new InterogareDateRegistruException(CAPITOL_AN_NOT_FOUND, parametriiInterogare.getTipCapitol().name(), parametriiInterogare.getIdentificatorGospodarie(), parametriiInterogare.getCodSirutaUAT(), parametriiInterogare.getAn());
                }
                break;
            case CAP7:
                if (unitateGospodarie == null || capitol == null || ((Capitol_7) capitol).getRandCapitol() == null || ((Capitol_7) capitol).getRandCapitol().isEmpty()) {
                    throw new InterogareDateRegistruException(CAPITOL_AN_SEMESTRU_NOT_FOUND, parametriiInterogare.getTipCapitol().name(), parametriiInterogare.getIdentificatorGospodarie(), parametriiInterogare.getCodSirutaUAT(), parametriiInterogare.getAn(), parametriiInterogare.getSemestru());
                }
                break;
            case CAP8:
                if (unitateGospodarie == null || capitol == null || ((Capitol_8) capitol).getRandCapitol() == null || ((Capitol_8) capitol).getRandCapitol().isEmpty()) {
                    throw new InterogareDateRegistruException(CAPITOL_AN_SEMESTRU_NOT_FOUND, parametriiInterogare.getTipCapitol().name(), parametriiInterogare.getIdentificatorGospodarie(), parametriiInterogare.getCodSirutaUAT(), parametriiInterogare.getAn(), parametriiInterogare.getSemestru());
                }
                break;
            case CAP9:
                if (unitateGospodarie == null || capitol == null || ((Capitol_9) capitol).getRandCapitol() == null || ((Capitol_9) capitol).getRandCapitol().isEmpty()) {
                    throw new InterogareDateRegistruException(CAPITOL_AN_NOT_FOUND, parametriiInterogare.getTipCapitol().name(), parametriiInterogare.getIdentificatorGospodarie(), parametriiInterogare.getCodSirutaUAT(), parametriiInterogare.getAn());
                }
                break;
            case CAP10a:
                if (unitateGospodarie == null || capitol == null || ((Capitol_10a) capitol).getRandCapitol() == null || ((Capitol_10a) capitol).getRandCapitol().isEmpty()) {
                    throw new InterogareDateRegistruException(CAPITOL_AN_NOT_FOUND, parametriiInterogare.getTipCapitol().name(), parametriiInterogare.getIdentificatorGospodarie(), parametriiInterogare.getCodSirutaUAT(), parametriiInterogare.getAn());
                }
                break;
            case CAP10b:
                if (unitateGospodarie == null || capitol == null || ((Capitol_10b) capitol).getRandCapitol() == null || ((Capitol_10b) capitol).getRandCapitol().isEmpty()) {
                    throw new InterogareDateRegistruException(CAPITOL_AN_NOT_FOUND, parametriiInterogare.getTipCapitol().name(), parametriiInterogare.getIdentificatorGospodarie(), parametriiInterogare.getCodSirutaUAT(), parametriiInterogare.getAn());
                }
                break;
            case CAP11:
                if (unitateGospodarie == null || capitol == null || ((Capitol_11) capitol).getRandCapitol() == null || ((Capitol_11) capitol).getRandCapitol().isEmpty()) {
                    throw new InterogareDateRegistruException(CAPITOL_AN_NOT_FOUND, parametriiInterogare.getTipCapitol().name(), parametriiInterogare.getIdentificatorGospodarie(), parametriiInterogare.getCodSirutaUAT(), parametriiInterogare.getAn());
                }
                break;
            case CAP12:
                if (unitateGospodarie == null || capitol == null || ((Capitol_12) capitol).getRandCapitol() == null || ((Capitol_12) capitol).getRandCapitol().isEmpty()) {
                    throw new InterogareDateRegistruException(CAPITOL_NOT_FOUND, parametriiInterogare.getTipCapitol().name(), parametriiInterogare.getIdentificatorGospodarie(), parametriiInterogare.getCodSirutaUAT());
                }
                break;
            case CAP13:
                if (unitateGospodarie == null || capitol == null || ((Capitol_13) capitol).getRandCapitol() == null || ((Capitol_13) capitol).getRandCapitol().isEmpty()) {
                    throw new InterogareDateRegistruException(CAPITOL_NOT_FOUND, parametriiInterogare.getTipCapitol().name(), parametriiInterogare.getIdentificatorGospodarie(), parametriiInterogare.getCodSirutaUAT());
                }
                break;
            case CAP14:
                if (unitateGospodarie == null || capitol == null || ((Capitol_14) capitol).getRandCapitol() == null || ((Capitol_14) capitol).getRandCapitol().isEmpty()) {
                    throw new InterogareDateRegistruException(CAPITOL_NOT_FOUND, parametriiInterogare.getTipCapitol().name(), parametriiInterogare.getIdentificatorGospodarie(), parametriiInterogare.getCodSirutaUAT());
                }
                break;
            case CAP15a:
                if (unitateGospodarie == null || capitol == null || ((Capitol_15a) capitol).getRandCapitol() == null || ((Capitol_15a) capitol).getRandCapitol().isEmpty()) {
                    throw new InterogareDateRegistruException(CAPITOL_NOT_FOUND, parametriiInterogare.getTipCapitol().name(), parametriiInterogare.getIdentificatorGospodarie(), parametriiInterogare.getCodSirutaUAT());
                }
                break;
            case CAP15b:
                if (unitateGospodarie == null || capitol == null || ((Capitol_15b) capitol).getRandCapitol() == null || ((Capitol_15b) capitol).getRandCapitol().isEmpty()) {
                    throw new InterogareDateRegistruException(CAPITOL_NOT_FOUND, parametriiInterogare.getTipCapitol().name(), parametriiInterogare.getIdentificatorGospodarie(), parametriiInterogare.getCodSirutaUAT());
                }
                break;
            case CAP16:
                if (unitateGospodarie == null || capitol == null || ((Capitol_16) capitol).getRandCapitol() == null) {
                    throw new InterogareDateRegistruException(CAPITOL_NOT_FOUND, parametriiInterogare.getTipCapitol().name(), parametriiInterogare.getIdentificatorGospodarie(), parametriiInterogare.getCodSirutaUAT());
                }
                break;
        }
    }


    private String getCapitolClassName(TipCapitol tipCapitol) {
        try {
            return tipCapitol.name().replace("CAP", "Capitol_");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkInstRight(long idInst) {
        Nomenclator institutie = nomenclatorRepository.findOne(Institutie.class, idInst);
        return ((Institutie) institutie).getAccesDatePrimare();
    }

    private String checkUat(long idContext) {
        Sistem checkInstance = sistemRepository.findByJudet_Id(idContext);
        if (checkInstance == null) {
            return "UAT";
        } else {
            return "JUDET";
        }

    }
}
