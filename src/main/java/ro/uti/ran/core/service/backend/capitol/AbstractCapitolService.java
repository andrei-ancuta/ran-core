package ro.uti.ran.core.service.backend.capitol;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.codes.DateRegistruValidationCodes;
import ro.uti.ran.core.model.portal.Parametru;
import ro.uti.ran.core.model.registru.*;
import ro.uti.ran.core.repository.registru.*;
import ro.uti.ran.core.service.backend.GospodarieService;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;
import ro.uti.ran.core.service.backend.nomenclator.NomenclatorService;
import ro.uti.ran.core.service.backend.nomenclator.ReutilizareService;
import ro.uti.ran.core.service.backend.utils.DataRaportareValabilitate;
import ro.uti.ran.core.service.parametru.ParametruService;

import java.util.List;

import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.NomUat;

/**
 * Created by Anastasia cea micuta on 10/21/2015.
 */
public abstract class AbstractCapitolService implements CapitolService {

    @Autowired
    protected GospodarieRepository gospodarieRepository;
    @Autowired
    protected DetinatorPfRepository detinatorPfRepository;
    @Autowired
    protected DetinatorPjRepository detinatorPjRepository;
    @Autowired
    protected ReutilizareService reutilizareService;
    @Autowired
    protected AdresaRepository adresaRepository;
    @Autowired
    protected ActRepository actRepository;
    @Autowired
    protected AdresaGospodarieRepository adresaGospodarieRepository;
    @Autowired
    protected SuprafataCategorieRepository suprafataCategorieRepository;
    @Autowired
    protected SuprafataUtilizareRepository suprafataUtilizareRepository;
    @Autowired
    protected GeometrieSuprafataUtilizRepository geometrieSuprafataUtilizRepository;
    @Autowired
    protected MembruPfRepository membruPfRepository;
    @Autowired
    protected CulturaRepository culturaRepository;
    @Autowired
    protected PomRazletRepository pomRazletRepository;
    @Autowired
    protected PlantatieRepository plantatieRepository;
    @Autowired
    protected TerenIrigatRepository terenIrigatRepository;
    @Autowired
    protected CategorieAnimalRepository categorieAnimalRepository;
    @Autowired
    protected SistemTehnicRepository sistemTehnicRepository;
    @Autowired
    protected SubstantaChimicaRepository substantaChimicaRepository;
    @Autowired
    protected AplicareIngrasamantRepository aplicareIngrasamantRepository;
    @Autowired
    protected CladireRepository cladireRepository;
    @Autowired
    protected ParcelaTerenRepository parcelaTerenRepository;
    @Autowired
    protected PreemptiuneRepository preemptiuneRepository;
    @Autowired
    protected ContractRepository contractRepository;
    @Autowired
    protected AtestatRepository atestatRepository;
    @Autowired
    protected MentiuneSpecialaRepository mentiuneSpecialaRepository;
    @Autowired
    protected MentiuneCerereSucRepository mentiuneCerereSucRepository;
    @Autowired
    protected GeometrieCulturaRepository geometrieCulturaRepository;
    @Autowired
    protected GeometriePlantatieRepository geometriePlantatieRepository;
    @Autowired
    protected GeometrieAplicareIngrasRepository geometrieAplicareIngrasRepository;
    @Autowired
    protected GeometrieTerenIrigatRepository geometrieTerenIrigatRepository;
    @Autowired
    protected NomenclatorService nomSrv;
    @Autowired
    protected ParametruService parametruService;
    @Autowired
    protected GospodarieService gospodarieService;
    private Logger logger = LoggerFactory.getLogger(AbstractCapitolService.class);

    @Override
    public void populeazaDateCapitol(GospodarieDTO gospodarieDTO) {
        populeazaDateCapitol(gospodarieDTO, null, null);
    }

    @Override
    public void populeazaDateCapitol(GospodarieDTO gospodarieDTO, Integer an) {
        populeazaDateCapitol(gospodarieDTO, an, null);
    }

    protected void validareCoduriSiruta(NomUat nomUat, NomJudet nomJudet, NomLocalitate nomLocalitate) throws DateRegistruValidationException {
        /*UAT in judet*/
        if (nomJudet != null && nomUat != null) {
            if (nomJudet.getCodSiruta() != (nomUat.getNomJudet().getCodSiruta())) {
                throw new DateRegistruValidationException(DateRegistruValidationCodes.UAT_JUDET_NECORESPUNZATOR, nomUat.getCodSiruta(), nomJudet.getCodSiruta());
            }
        }
        /*Localitate in UAT*/
        if (nomLocalitate != null && nomUat != null) {
            if (!nomLocalitate.getNomUat().getCodSiruta().equals(nomUat.getCodSiruta())) {
                throw new DateRegistruValidationException(DateRegistruValidationCodes.LOCALITATE_UAT_NECORESPUNZATOR, nomLocalitate.getCodSiruta(), nomUat.getCodSiruta());
            }
        }
    }

    /**
     * Daca parametru de sistem ParametruService.RESTRICTIONARE_TRANSMISII_UAT_ADRESARENNS este true se cere obligatoriu campul 'cua' = UidRenns
     *
     * @param adresa           adresa primita
     * @param xmlAdresaTagname denumire tag xml in care este incapsulata adresa
     * @throws DateRegistruValidationException
     */
    protected void validareAdresaRenns(Adresa adresa, String xmlAdresaTagname) throws DateRegistruValidationException {
        //adresa din RO
        if (adresa != null && adresa.getNomJudet() != null) {
            boolean valParam = false;
            try {
                String tmp = getParamValue(ParametruService.RESTRICTIONARE_TRANSMISII_UAT_ADRESARENNS);
                if (tmp != null) {
                    valParam = Boolean.valueOf(tmp);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            if (valParam) {
                if (StringUtils.isEmpty(adresa.getUidRenns())) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.RESTRICTIE_ADRESA_RENNS, xmlAdresaTagname);
                }
            }
        }
    }

    /**
     * nu se va permite completarea concomitenta a elementelor: CUA si referintaGeoXml
     *
     * @param adresa           adresa primita
     * @param xmlAdresaTagname denumire tag xml in care este incapsulata adresa
     * @throws DateRegistruValidationException
     */
    protected void validareAdresaCUAandReferintaGeoXml(Adresa adresa, String xmlAdresaTagname) throws DateRegistruValidationException {
        //adresa din RO
        if (adresa != null && adresa.getNomJudet() != null) {
            if (StringUtils.isNotEmpty(adresa.getGeometrieGML()) && StringUtils.isNotEmpty(adresa.getUidRenns())) {
                throw new DateRegistruValidationException(DateRegistruValidationCodes.RESTRICTIE_ADRESA_CUA_AND_GEOMETRIE, xmlAdresaTagname);
            }
        }
    }

    /**
     * @param paramName denumire parametru
     * @return valoare parametru
     */

    protected String getParamValue(String paramName) {
        Parametru parametru = parametruService.getParametru(paramName);
        if (parametru != null) {
            return parametru.getValoare();
        }
        return null;
    }

    /**
     * logica de validare comuna in capitole
     *
     * @param ranDocDTO dto
     * @throws DateRegistruValidationException
     */
    protected void validareExistentaGospodarieNomUatHeader(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        /*gospodaria trebuie sa existe in baza*/
        if (!gospodarieService.isGospodarie(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie())) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.GOSPODARIE_NOT_FOUND, ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        }
        DataRaportareValabilitate dataRaportare = new DataRaportareValabilitate();
        /*Header - NOM_UAT*/
        NomUat nomUatHeader = nomSrv.getNomenclatorForStringParam(NomUat, ranDocDTO.getSirutaUAT(), dataRaportare);
        if (nomUatHeader == null) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, NomUat.getLabel(), NomUat.getCodeColumn(), ranDocDTO.getSirutaUAT(), dataRaportare);
        }
    }


    @Transactional(value = "registruTransactionManager", rollbackFor = RanBusinessException.class)
    protected void validareUnicitateDetinatorPF(Gospodarie gospodarie, Integer codSirutaUAT) throws DateRegistruValidationException {
        //Un CNP/NIF sa nu fie desemnat detinator la mai mult de 1 gospodarie activa din acelasi UAT
        DetinatorPf detinatorPf = gospodarie.getDetinatorPfs().get(0);
        List<String> identificatoriGosp = null;
        String cnp_nif = null;
        if (StringUtils.isNotEmpty(detinatorPf.getPersoanaFizica().getCnp())) {
            cnp_nif = detinatorPf.getPersoanaFizica().getCnp().toUpperCase();
            identificatoriGosp = detinatorPfRepository.unicitateByCnp(detinatorPf.getPersoanaFizica().getCnp().toUpperCase(), codSirutaUAT);
        }
        if (StringUtils.isNotEmpty(detinatorPf.getPersoanaFizica().getNif())) {
            cnp_nif = detinatorPf.getPersoanaFizica().getNif().toUpperCase();
            identificatoriGosp = detinatorPfRepository.unicitateByNif(detinatorPf.getPersoanaFizica().getNif().toUpperCase(), codSirutaUAT);
        }
        if (identificatoriGosp != null) {
            if (identificatoriGosp.size() > 1) {
                StringBuilder tmp = new StringBuilder();
                for (String idg : identificatoriGosp) {
                    tmp.append(idg.toUpperCase()).append("; ");
                }
                throw new DateRegistruValidationException(DateRegistruValidationCodes.CNP_NIF_DETINATOR_PF_UNIC, codSirutaUAT, cnp_nif, tmp);
            }
            if (identificatoriGosp.size() == 1 && !gospodarie.getIdentificator().equalsIgnoreCase(identificatoriGosp.get(0))) {
                StringBuilder tmp = new StringBuilder(gospodarie.getIdentificator().toUpperCase());
                tmp.append("; ").append(identificatoriGosp.get(0).toUpperCase());
                throw new DateRegistruValidationException(DateRegistruValidationCodes.CNP_NIF_DETINATOR_PF_UNIC, codSirutaUAT, cnp_nif, tmp);
            }
        }
    }

    @Transactional(value = "registruTransactionManager", rollbackFor = RanBusinessException.class)
    protected void validareUnicitateMembruPF(Gospodarie oldGospodarie, Integer codSirutaUAT, MembruPf membruPf) throws DateRegistruValidationException {
        //Un CNP/NIF sa nu fie desemnat membru la mai mult de 1 gospodarie activa din acelasi UAT
        List<String> identificatoriGosp = null;
        String cnp_nif = null;
        if (StringUtils.isNotEmpty(membruPf.getPersoanaFizica().getCnp())) {
            cnp_nif = membruPf.getPersoanaFizica().getCnp().toUpperCase();
            identificatoriGosp = membruPfRepository.unicitateByCnp(membruPf.getPersoanaFizica().getCnp().toUpperCase(), codSirutaUAT);
        }
        if (StringUtils.isNotEmpty(membruPf.getPersoanaFizica().getNif())) {
            cnp_nif = membruPf.getPersoanaFizica().getNif().toUpperCase();
            identificatoriGosp = membruPfRepository.unicitateByNif(membruPf.getPersoanaFizica().getNif().toUpperCase(), codSirutaUAT);
        }
        if (identificatoriGosp != null) {
            if (identificatoriGosp.size() > 1) {
                StringBuilder tmp = new StringBuilder();
                for (String idg : identificatoriGosp) {
                    tmp.append(idg.toUpperCase()).append("; ");
                }
                throw new DateRegistruValidationException(DateRegistruValidationCodes.CNP_NIF_MEMBRU_UNIC, codSirutaUAT, cnp_nif, tmp);
            }
            if (identificatoriGosp.size() == 1 && !oldGospodarie.getIdentificator().equalsIgnoreCase(identificatoriGosp.get(0))) {
                StringBuilder tmp = new StringBuilder(oldGospodarie.getIdentificator().toUpperCase());
                tmp.append("; ").append(identificatoriGosp.get(0).toUpperCase());
                throw new DateRegistruValidationException(DateRegistruValidationCodes.CNP_NIF_MEMBRU_UNIC, codSirutaUAT, cnp_nif, tmp);
            }
        }
    }

    @Transactional(value = "registruTransactionManager", rollbackFor = RanBusinessException.class)
    protected void validareUnicitateDetinatorPJ(Gospodarie gospodarie, Integer codSirutaUAT) throws DateRegistruValidationException {
        DetinatorPj detinatorPj = gospodarie.getDetinatorPjs().get(0);
        //Un CUI sa nu fie desemnat detinator la mai mult de 1 gospodarie activa din acelasi UAT
        String cui = detinatorPj.getPersoanaRc().getCui().toUpperCase();
        List<String> identificatoriGosp = detinatorPjRepository.unicitateByCui(detinatorPj.getPersoanaRc().getCui().toUpperCase(), codSirutaUAT);
        if (identificatoriGosp != null) {
            if (identificatoriGosp.size() > 1) {
                StringBuilder tmp = new StringBuilder();
                for (String idg : identificatoriGosp) {
                    tmp.append(idg.toUpperCase()).append("; ");
                }
                throw new DateRegistruValidationException(DateRegistruValidationCodes.CUI_DETINATOR_PJ_UNIC, codSirutaUAT, cui, tmp);
            }
            if (identificatoriGosp.size() == 1 && !gospodarie.getIdentificator().equalsIgnoreCase(identificatoriGosp.get(0))) {
                StringBuilder tmp = new StringBuilder(gospodarie.getIdentificator().toUpperCase());
                tmp.append("; ").append(identificatoriGosp.get(0).toUpperCase());
                throw new DateRegistruValidationException(DateRegistruValidationCodes.CUI_DETINATOR_PJ_UNIC, codSirutaUAT, cui, tmp);
            }
        }
    }
}
