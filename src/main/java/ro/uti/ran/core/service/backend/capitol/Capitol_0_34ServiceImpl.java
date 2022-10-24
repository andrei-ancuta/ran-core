package ro.uti.ran.core.service.backend.capitol;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.codes.DateRegistruValidationCodes;
import ro.uti.ran.core.model.registru.*;
import ro.uti.ran.core.model.registru.NomJudet;
import ro.uti.ran.core.model.registru.NomLocalitate;
import ro.uti.ran.core.model.registru.NomTara;
import ro.uti.ran.core.model.registru.NomTipAdresa;
import ro.uti.ran.core.model.registru.NomTipDetinator;
import ro.uti.ran.core.model.registru.NomTipExploatatie;
import ro.uti.ran.core.model.registru.NomUat;
import ro.uti.ran.core.model.utils.FormaOrganizareRc;
import ro.uti.ran.core.model.utils.IndicativXml;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.model.utils.TipAdresa;
import ro.uti.ran.core.service.backend.GeometrieService;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;
import ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType;
import ro.uti.ran.core.service.backend.utils.DataRaportareValabilitate;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.utils.CnpValidator;
import ro.uti.ran.core.xml.model.capitol.Capitol_0_34;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.*;

/**
 * Created by Dan on 14-Oct-15.
 */
@Service("capitol_0_34Service")
@Transactional(value = "registruTransactionManager", rollbackFor = RanBusinessException.class)
public class Capitol_0_34ServiceImpl extends AbstractCapitolService {

    private Logger logger = LoggerFactory.getLogger(Capitol_0_34ServiceImpl.class);

    @Autowired
    private GeometrieService geometrieService;

    public Capitol_0_34ServiceImpl() {
    }

    @Override
    public void valideazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        if (IndicativXml.ADAUGA_SI_INLOCUIESTE.equals(ranDocDTO.getIndicativ())) {
            valideazaDateRegistruLaEditare(ranDocDTO);
        } else if (IndicativXml.ANULEAZA.equals(ranDocDTO.getIndicativ())) {
            validareExistentaGospodarieNomUatHeader(ranDocDTO);
            //cap0_34 nu poate anula cap0_12
            Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
            List<DetinatorPf> lstPf = detinatorPfRepository.findByGospodarieAndFkNomJudet(oldGospodarie.getIdGospodarie(), oldGospodarie.getNomUat().getNomJudet().getId());
            if (lstPf != null && !lstPf.isEmpty()) {
                throw new DateRegistruValidationException(DateRegistruValidationCodes.CAP0_XX_UPDATE_CAP0_YY, TipCapitol.CAP0_12.name(), "anulata", TipCapitol.CAP0_34.name());
            }
            if (gospodarieService.isCapitolGospodarie(oldGospodarie.getIdGospodarie())) {
                throw new DateRegistruValidationException(DateRegistruValidationCodes.GOSPODARIA_ARE_CAPITOLE);
            }
        } else {
            validareExistentaGospodarieNomUatHeader(ranDocDTO);
            //cap0_34 nu poate dezactiva/reactiva cap0_12
            Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
            List<DetinatorPf> lstPf = detinatorPfRepository.findByGospodarieAndFkNomJudet(oldGospodarie.getIdGospodarie(), oldGospodarie.getNomUat().getNomJudet().getId());
            if (lstPf != null && !lstPf.isEmpty()) {
                throw new DateRegistruValidationException(DateRegistruValidationCodes.CAP0_XX_UPDATE_CAP0_YY, TipCapitol.CAP0_12.name(), "dezactiva/reactiva", TipCapitol.CAP0_34.name());
            }
            //validare de unicitate la reactivare
            if (IndicativXml.REACTIVARE.equals(ranDocDTO.getIndicativ())) {
                validareUnicitateDetinatorPJ(oldGospodarie, ranDocDTO.getSirutaUAT());
            }
        }
    }

    /**
     * logica de validare la editare
     *
     * @param ranDocDTO dto
     * @throws DateRegistruValidationException
     */
    private void valideazaDateRegistruLaEditare(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        /*populare cu nomenclatoare: identific dupa cod/dataExport linia din nomenclatoare corespunzatoare; daca nu gasesc ... eroare*/
        Gospodarie gospodarie = ranDocDTO.getGospodarie();
        DataRaportareValabilitate dataRaportare = new DataRaportareValabilitate();
        /*Header - NOM_UAT*/
        NomUat nomUatHeader = nomSrv.getNomenclatorForStringParam(NomUat, ranDocDTO.getSirutaUAT(), dataRaportare);
        if (nomUatHeader == null) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, NomUat.getLabel(), NomUat.getCodeColumn(), ranDocDTO.getSirutaUAT(), dataRaportare);
        }
        /*GOSPODARIE - NOM_TIP_DETINATOR*/
        NomTipDetinator nomTipDetinator = nomSrv.getNomenclatorForStringParam(NomenclatorCodeType.NomTipDetinator, gospodarie.getNomTipDetinator().getCod(), dataRaportare);
        if (nomTipDetinator == null) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, NomTipDetinator.getLabel(), NomTipDetinator.getCodeColumn(), gospodarie.getNomTipDetinator().getCod(), dataRaportare);
        }
        gospodarie.setNomTipDetinator(nomTipDetinator);
        /*GOSPODARIE - NomTipExploatatie*/
        NomTipExploatatie nomTipExploatatie = nomSrv.getNomenclatorForStringParam(NomenclatorCodeType.NomTipExploatatie, gospodarie.getNomTipExploatatie().getCod(), dataRaportare);
        if (nomTipExploatatie == null) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, NomTipExploatatie.getLabel(), NomTipExploatatie.getCodeColumn(), gospodarie.getNomTipExploatatie().getCod(), dataRaportare);
        }
        gospodarie.setNomTipExploatatie(nomTipExploatatie);
        //ADRESE
        valideazaDateRegistruLaEditare_Adrese(gospodarie, nomUatHeader, ranDocDTO, dataRaportare);
        //Detinator PJ
        valideazaDateRegistruLaEditare_DetinatorPJ(gospodarie, nomUatHeader, ranDocDTO);

    }

    private void valideazaDateRegistruLaEditare_DetinatorPJ(Gospodarie gospodarie, NomUat nomUatHeader, RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        if (gospodarie.getDetinatorPjs() != null && !gospodarie.getDetinatorPjs().isEmpty()) {
            DetinatorPj detinatorPj = gospodarie.getDetinatorPjs().get(0);
            //
            validareUnicitateDetinatorPJ(gospodarie, ranDocDTO.getSirutaUAT());
            /*fkNomJudet*/
            detinatorPj.setFkNomJudet(gospodarie.getNomUat().getNomJudet().getId());
            /*PersoanaFizica*/
            if (detinatorPj.getPersoanaFizica() != null) {
                //validare CNP
                if (detinatorPj.getPersoanaFizica().getCnp() != null &&
                        !CnpValidator.isValid(detinatorPj.getPersoanaFizica().getCnp())) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.CNP_INVALID, detinatorPj.getPersoanaFizica().getCnp());
                }
                //reutilizare PersoanaFizica
                PersoanaFizica dbPersoanaFizica = new PersoanaFizica();
                dbPersoanaFizica.setIdPersoanaFizica(reutilizareService.reutilizarePersoanaFizica(detinatorPj.getPersoanaFizica(), nomUatHeader.getId()));
                detinatorPj.setPersoanaFizica(dbPersoanaFizica);
            }
            /*PERSOANA_RC - NOM_FORMA_ORGANIZARE_RC*/
            ro.uti.ran.core.model.registru.NomFormaOrganizareRc nomFormaOrganizareRc = nomSrv.getNomenclatorForStringParam(NomFormaOrganizareRc, detinatorPj.getPersoanaRc().getNomFormaOrganizareRc().getCod());
            if (nomFormaOrganizareRc == null) {
                throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND, NomFormaOrganizareRc.getLabel(), NomFormaOrganizareRc.getCodeColumn(), detinatorPj.getPersoanaRc().getNomFormaOrganizareRc().getCod());
            }
            detinatorPj.getPersoanaRc().setNomFormaOrganizareRc(nomFormaOrganizareRc);
            /*NU contina una din valorile (PFA, II, IF)*/
            if (detinatorPj.getPersoanaRc().getNomFormaOrganizareRc().getCod().equals(FormaOrganizareRc.PERSOANA_FIZICA_AUTORIZATA.getCod()) ||
                    detinatorPj.getPersoanaRc().getNomFormaOrganizareRc().getCod().equals(FormaOrganizareRc.INTREPRINDERE_FAMILIALA.getCod()) ||
                    detinatorPj.getPersoanaRc().getNomFormaOrganizareRc().getCod().equals(FormaOrganizareRc.INTREPRINDERE_INDIVIDUALA.getCod())) {
                throw new DateRegistruValidationException(DateRegistruValidationCodes.FORMAORGANIZARERC_NECORESPUNZATOR_034, detinatorPj.getPersoanaRc().getNomFormaOrganizareRc().getCod());
            }
            /*PERSOANA_RC*/
            PersoanaRc dbPersoanaRc = new PersoanaRc();
            dbPersoanaRc.setIdPersoanaRc(reutilizareService.reutilizarePersoanaRc(detinatorPj.getPersoanaRc(), nomFormaOrganizareRc.getId(), nomUatHeader.getId()));
            detinatorPj.setPersoanaRc(dbPersoanaRc);
        }
    }


    private void valideazaDateRegistruLaEditare_Adrese(Gospodarie gospodarie, NomUat nomUatHeader, RanDocDTO ranDocDTO, DataRaportareValabilitate dataRaportare) throws DateRegistruValidationException {
        if (gospodarie.getAdresaGospodaries() != null && !gospodarie.getAdresaGospodaries().isEmpty()) {
            for (AdresaGospodarie adresaGospodarie : gospodarie.getAdresaGospodaries()) {
                /*ADRESA - NOM_TIP_ADRESA*/
                NomTipAdresa nomTipAdresa = nomSrv.getNomenclatorForStringParam(NomTipAdresa, adresaGospodarie.getNomTipAdresa().getCod());
                if (nomTipAdresa == null) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND, NomTipAdresa.getLabel(), NomTipAdresa.getCodeColumn(), adresaGospodarie.getNomTipAdresa().getCod());
                }
                adresaGospodarie.setNomTipAdresa(nomTipAdresa);
                /*ADRESA - NOM_TARA*/
                NomTara nomTara = nomSrv.getNomenclatorForStringParam(NomTara, adresaGospodarie.getAdresa().getNomTara().getCodNumeric(), dataRaportare);
                if (nomTara == null) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, NomTara.getLabel(), NomTara.getCodeColumn(), adresaGospodarie.getAdresa().getNomTara().getCodNumeric(), dataRaportare);
                }
                adresaGospodarie.getAdresa().setNomTara(nomTara);
                /*ADRESA - NOM_UAT*/
                if (adresaGospodarie.getAdresa().getNomUat() != null) {
                    NomUat nomUatAdresa = nomSrv.getNomenclatorForStringParam(NomUat, adresaGospodarie.getAdresa().getNomUat().getCodSiruta(), dataRaportare);
                    if (nomUatAdresa == null) {
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, NomUat.getLabel(), NomUat.getCodeColumn(), adresaGospodarie.getAdresa().getNomUat().getCodSiruta(), dataRaportare);
                    }
                    adresaGospodarie.getAdresa().setNomUat(nomUatAdresa);
                }
                /*ADRESA - NOM_JUDET*/
                if (adresaGospodarie.getAdresa().getNomJudet() != null) {
                    NomJudet nomJudet = nomSrv.getNomenclatorForStringParam(NomJudet, adresaGospodarie.getAdresa().getNomJudet().getCodSiruta(), dataRaportare);
                    if (nomJudet == null) {
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, NomJudet.getLabel(), NomJudet.getCodeColumn(), adresaGospodarie.getAdresa().getNomJudet().getCodSiruta(), dataRaportare);
                    }
                    adresaGospodarie.getAdresa().setNomJudet(nomJudet);
                    adresaGospodarie.setFkNomJudet(nomJudet.getId());
                }

                /*ADRESA - NOM_LOCALITATE*/
                if (adresaGospodarie.getAdresa().getNomLocalitate() != null) {
                    NomLocalitate nomLocalitate = nomSrv.getNomenclatorForStringParam(NomLocalitate, adresaGospodarie.getAdresa().getNomLocalitate().getCodSiruta(), dataRaportare);
                    if (nomLocalitate == null) {
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, NomLocalitate.getLabel(), NomLocalitate.getCodeColumn(), adresaGospodarie.getAdresa().getNomLocalitate().getCodSiruta(), dataRaportare);
                    }
                    adresaGospodarie.getAdresa().setNomLocalitate(nomLocalitate);
                }
                /*Localitate - UAT - Judet*/
                validareCoduriSiruta(adresaGospodarie.getAdresa().getNomUat(), adresaGospodarie.getAdresa().getNomJudet(), adresaGospodarie.getAdresa().getNomLocalitate());

                //
                validareAdresaRenns(adresaGospodarie.getAdresa(), "date_identificare_gospodarie_PJ");
                //
                validareAdresaCUAandReferintaGeoXml(adresaGospodarie.getAdresa(), "date_identificare_gospodarie_PJ");

                if (TipAdresa.GOSPODARIE.getCod().equals(adresaGospodarie.getNomTipAdresa().getCod())) {
                    /*validare UAT-adresa gospodarie cu UAT-header*/
                    if (!nomUatHeader.getCodSiruta().equals(adresaGospodarie.getAdresa().getNomUat().getCodSiruta())) {
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.UAT_ADRESA_GOSPODARIE_INVALID, adresaGospodarie.getAdresa().getNomUat().getCodSiruta(), nomUatHeader.getCodSiruta());
                    } else {
                        /*GOSPODARIE - NOM_UAT*/
                        gospodarie.setNomUat(adresaGospodarie.getAdresa().getNomUat());
                        /*GOSPODARIE - NOM_JUDET*/
                        gospodarie.setNomJudet(adresaGospodarie.getAdresa().getNomJudet());
                        /*GOSPODARIE - NOM_LOCALITATE*/
                        gospodarie.setNomLocalitate(adresaGospodarie.getAdresa().getNomLocalitate());
                    }
                    if (StringUtils.isNotEmpty(adresaGospodarie.getAdresa().getGeometrieGML())) {
                        //geometria va fi validata sa fie o geometrie corecta si de tip 'punct'
                        geometrieService.validateGeometrie(adresaGospodarie.getAdresa().getGeometrieGML());
                        //adresa gospodariei/cladirilor sunt exclusiv din UAT-ul care transmite
                        geometrieService.validateGeometriePunctUatLimit(ranDocDTO.getSirutaUAT(), adresaGospodarie.getAdresa().getGeometrieGML());
                    }
                }
            }
            for (AdresaGospodarie adresaGospodarie : gospodarie.getAdresaGospodaries()) {
                /*fkNomJudet*/
                adresaGospodarie.setFkNomJudet(gospodarie.getNomUat().getNomJudet().getId());
            }
        }
    }

    @Override
    public void salveazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        if (IndicativXml.ADAUGA_SI_INLOCUIESTE.equals(ranDocDTO.getIndicativ())) {
            if (oldGospodarie != null) {
                //cap0_34 nu poate modifica cap0_12
                List<DetinatorPf> lstPf = detinatorPfRepository.findByGospodarieAndFkNomJudet(oldGospodarie.getIdGospodarie(), oldGospodarie.getNomUat().getNomJudet().getId());
                if (lstPf != null && !lstPf.isEmpty()) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.CAP0_XX_UPDATE_CAP0_YY, TipCapitol.CAP0_12.name(), "modificata", TipCapitol.CAP0_34.name());
                }
                updateDateRegistru(oldGospodarie, ranDocDTO);
            } else {
                addDateRegistru(ranDocDTO);
            }
        } else if (IndicativXml.ANULEAZA.equals(ranDocDTO.getIndicativ())) {
            anuleazaDateRegistru(oldGospodarie);
        } else if (IndicativXml.DEZACTIVARE.equals(ranDocDTO.getIndicativ())) {
            oldGospodarie.setIsActiv(RanConstants.GOSPODARIE_IS_ACTIV_NU);
            gospodarieRepository.save(oldGospodarie);
        } else if (IndicativXml.REACTIVARE.equals(ranDocDTO.getIndicativ())) {
            oldGospodarie.setIsActiv(RanConstants.GOSPODARIE_IS_ACTIV_DA);
            gospodarieRepository.save(oldGospodarie);
        }

        // Altfel nu se face rollback pe constraint-uri DB
       // gospodarieRepository.flush();
    }

    /**
     * @param ranDocDTO info trimise prin xml
     */
    private void addDateRegistru(RanDocDTO ranDocDTO) {
        /*GOSPODARIE*/
        Gospodarie gospodarie = ranDocDTO.getGospodarie();
        gospodarie.setIsActiv(RanConstants.GOSPODARIE_IS_ACTIV_DA);
        gospodarie.setInsertDate(new Timestamp(new Date().getTime()));
        gospodarieRepository.save(gospodarie);
        /*DETINATOR_PJ*/
        if (gospodarie.getDetinatorPjs() != null && !gospodarie.getDetinatorPjs().isEmpty()) {
            DetinatorPj detinatorPj = gospodarie.getDetinatorPjs().get(0);
            detinatorPj.setGospodarie(gospodarie);
            detinatorPjRepository.save(detinatorPj);
        }
        /*ADRESA - reprezentant legal + sediu pct de lucru + domiciliu fiscal + gospodarie*/
        if (gospodarie.getAdresaGospodaries() != null) {
            /*adresele*/
            for (AdresaGospodarie adresaGospodarie : gospodarie.getAdresaGospodaries()) {
                adresaGospodarie.getAdresa().setDataStart(new Date());
                adresaRepository.save(adresaGospodarie.getAdresa());
                adresaGospodarie.setGospodarie(gospodarie);
                adresaGospodarieRepository.save(adresaGospodarie);
            }
            /*geometrie pentru fiecare adresa*/
            for (AdresaGospodarie adresa : gospodarie.getAdresaGospodaries()) {
                if (StringUtils.isNotEmpty(adresa.getAdresa().getGeometrieGML())) {
                    geometrieService.insertAdresaGIS(adresa.getAdresa().getIdAdresa(), adresa.getAdresa().getGeometrieGML(), gospodarie.getNomUat().getNomJudet().getId());
                }
            }
        }
    }

    /**
     * @param oldGospodarie gospodaria din baza de date
     * @param ranDocDTO     info trimise prin xml
     */
    private void updateDateRegistru(Gospodarie oldGospodarie, RanDocDTO ranDocDTO) {
        Gospodarie newGospodarie = ranDocDTO.getGospodarie();
        /*GOSPODARIE*/
        /*tipDetinator*/
        oldGospodarie.setNomTipDetinator(newGospodarie.getNomTipDetinator());
        /*pozitieGospodarie*/
        oldGospodarie.setIdentPozitieAnterioara(newGospodarie.getIdentPozitieAnterioara());
        oldGospodarie.setIdentPozitieCurenta(newGospodarie.getIdentPozitieCurenta());
        oldGospodarie.setIdentRolNominalUnic(newGospodarie.getIdentRolNominalUnic());
        oldGospodarie.setIdentVolum(newGospodarie.getIdentVolum());
        /*codExploatatie*/
        oldGospodarie.setCodExploatatie(newGospodarie.getCodExploatatie());
        /*tipExploatatie*/
        oldGospodarie.setNomTipExploatatie(newGospodarie.getNomTipExploatatie());
        /*nrUnicIdentificare*/
        oldGospodarie.setNrUnicIdentificare(newGospodarie.getNrUnicIdentificare());
        //
        gospodarieRepository.save(oldGospodarie);
        /*DETINATOR_PJ update*/
        if (oldGospodarie.getDetinatorPjs() != null && !oldGospodarie.getDetinatorPjs().isEmpty()) {
            DetinatorPj oldDetinatorPj = oldGospodarie.getDetinatorPjs().get(0);
            if (newGospodarie.getDetinatorPjs() != null && !newGospodarie.getDetinatorPjs().isEmpty()) {
                DetinatorPj newDetinatorPj = newGospodarie.getDetinatorPjs().get(0);
                oldDetinatorPj.setDenumireSubdiviziune(newDetinatorPj.getDenumireSubdiviziune());
                oldDetinatorPj.setPersoanaFizica(newDetinatorPj.getPersoanaFizica());
                oldDetinatorPj.setPersoanaRc(newDetinatorPj.getPersoanaRc());
                oldDetinatorPj.setFkNomJudet(newDetinatorPj.getFkNomJudet());
                detinatorPjRepository.save(oldDetinatorPj);
            }
        } else {
            if (newGospodarie.getDetinatorPjs() != null && !newGospodarie.getDetinatorPjs().isEmpty()) {
                DetinatorPj detinatorPj = newGospodarie.getDetinatorPjs().get(0);
                detinatorPj.setGospodarie(oldGospodarie);
                detinatorPjRepository.save(detinatorPj);
            }
        }
        /*ADRESA - adresa reprezentant legal + sediu pct de lucru + domiciliu fiscal + gospodarie*/
        List<AdresaGospodarie> oldAdresas = oldGospodarie.getAdresaGospodaries();
        /*stergere*/
        if (oldAdresas != null) {
            for (AdresaGospodarie oldAdresa : oldAdresas) {
                adresaGospodarieRepository.delete(oldAdresa);
            }
        }
        if (newGospodarie.getAdresaGospodaries() != null) {
            /*adresele*/
            for (AdresaGospodarie xmlAdresa : newGospodarie.getAdresaGospodaries()) {
                /*adaugare*/
                xmlAdresa.getAdresa().setDataStart(new Date());
                adresaRepository.save(xmlAdresa.getAdresa());
                //
                xmlAdresa.setGospodarie(oldGospodarie);
                adresaGospodarieRepository.save(xmlAdresa);
            }
            /*geometrie pentru fiecare adresa*/
            for (AdresaGospodarie xmlAdresa : newGospodarie.getAdresaGospodaries()) {
                if (StringUtils.isNotEmpty(xmlAdresa.getAdresa().getGeometrieGML())) {
                    geometrieService.insertAdresaGIS(xmlAdresa.getAdresa().getIdAdresa(), xmlAdresa.getAdresa().getGeometrieGML(), oldGospodarie.getNomUat().getNomJudet().getId());
                }
            }
        }
    }


    private void anuleazaDateRegistru(Gospodarie oldGospodarie) {
        gospodarieRepository.delete(oldGospodarie);
    }

    @Override
    public Class getCapitolClass() {
        return Capitol_0_34.class;
    }


    @Override
    public void populeazaDateCapitol(GospodarieDTO gospodarieDTO, Integer an, Byte semestru) {
        /*ADRESA - reprezentant legal*/
        AdresaGospodarie adresaReprezentantLegal = adresaGospodarieRepository.findByCodTipAdresaAndGospodarie(TipAdresa.DOMICILIU_REPREZENTANT_LEGAL.getCod(), gospodarieDTO.getGospodarie().getIdGospodarie());
        /*ADRESA - sediu pct de lucru*/
        AdresaGospodarie adresaSubdiviziuneEntitate = adresaGospodarieRepository.findByCodTipAdresaAndGospodarie(TipAdresa.SEDIU_PCT_LUCRU.getCod(), gospodarieDTO.getGospodarie().getIdGospodarie());
         /*ADRESA - domiciliuFiscal*/
        AdresaGospodarie domiciliuFiscal = adresaGospodarieRepository.findByCodTipAdresaAndGospodarie(TipAdresa.DOMICILIU_FISCAL.getCod(), gospodarieDTO.getGospodarie().getIdGospodarie());
        /*ADRESA - adresaGospodarie*/
        AdresaGospodarie adresaGospodarie = adresaGospodarieRepository.findByCodTipAdresaAndGospodarie(TipAdresa.GOSPODARIE.getCod(), gospodarieDTO.getGospodarie().getIdGospodarie());
        /*Geometrie*/
        if (adresaReprezentantLegal != null || adresaSubdiviziuneEntitate != null || domiciliuFiscal != null || adresaGospodarie != null) {
            List<AdresaGospodarie> lstAdresa = new ArrayList<>();
            if (adresaReprezentantLegal != null) {
                /*Geometrie*/
                if (adresaReprezentantLegal.getAdresa().getGeolocatorAdresa() != null) {
                    String geometrieGML = geometrieService.getAdresaGIS(adresaReprezentantLegal.getAdresa().getGeolocatorAdresa().getIdGeolocatorAdresa());
                    if (StringUtils.isNotEmpty(geometrieGML)) {
                        adresaReprezentantLegal.getAdresa().setGeometrieGML(geometrieGML);
                    }
                }
                //
                lstAdresa.add(adresaReprezentantLegal);
            }
            if (adresaSubdiviziuneEntitate != null) {
                /*Geometrie*/
                if (adresaSubdiviziuneEntitate.getAdresa().getGeolocatorAdresa() != null) {
                    String geometrieGML = geometrieService.getAdresaGIS(adresaSubdiviziuneEntitate.getAdresa().getGeolocatorAdresa().getIdGeolocatorAdresa());
                    if (StringUtils.isNotEmpty(geometrieGML)) {
                        adresaSubdiviziuneEntitate.getAdresa().setGeometrieGML(geometrieGML);
                    }
                }
                //
                lstAdresa.add(adresaSubdiviziuneEntitate);
            }
            if (domiciliuFiscal != null) {
                /*Geometrie*/
                if (domiciliuFiscal.getAdresa().getGeolocatorAdresa() != null) {
                    String geometrieGML = geometrieService.getAdresaGIS(domiciliuFiscal.getAdresa().getGeolocatorAdresa().getIdGeolocatorAdresa());
                    if (StringUtils.isNotEmpty(geometrieGML)) {
                        domiciliuFiscal.getAdresa().setGeometrieGML(geometrieGML);
                    }
                }
                //
                lstAdresa.add(domiciliuFiscal);
            }
            if (adresaGospodarie != null) {
                /*Geometrie*/
                if (adresaGospodarie.getAdresa().getGeolocatorAdresa() != null) {
                    String geometrieGML = geometrieService.getAdresaGIS(adresaGospodarie.getAdresa().getGeolocatorAdresa().getIdGeolocatorAdresa());
                    if (StringUtils.isNotEmpty(geometrieGML)) {
                        adresaGospodarie.getAdresa().setGeometrieGML(geometrieGML);
                    }
                }
                //
                lstAdresa.add(adresaGospodarie);
            }
            gospodarieDTO.setAdresaGospodaries(lstAdresa);
        }
        /*DETINATOR_PJ*/
        gospodarieDTO.setDetinatorPjs(detinatorPjRepository.findByGospodarieAndFkNomJudet(gospodarieDTO.getGospodarie().getIdGospodarie(), gospodarieDTO.getGospodarie().getNomUat().getNomJudet().getId()));
    }

}
