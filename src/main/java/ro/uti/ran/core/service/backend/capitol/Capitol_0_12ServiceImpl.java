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
import ro.uti.ran.core.model.utils.*;
import ro.uti.ran.core.service.backend.GeometrieService;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;
import ro.uti.ran.core.service.backend.utils.DataRaportareValabilitate;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.utils.CnpValidator;
import ro.uti.ran.core.xml.model.capitol.Capitol_0_12;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.*;

/**
 * Se va lucra cu GML 3.1.1
 * Created by Dan on 14-Oct-15.
 */
@Service("capitol_0_12Service")
@Transactional(value = "registruTransactionManager", rollbackFor = RanBusinessException.class)
public class Capitol_0_12ServiceImpl extends AbstractCapitolService {

    private Logger logger = LoggerFactory.getLogger(Capitol_0_12ServiceImpl.class);

    @Autowired
    private GeometrieService geometrieService;


    @Override
    public void valideazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        if (IndicativXml.ADAUGA_SI_INLOCUIESTE.equals(ranDocDTO.getIndicativ())) {
            valideazaDateRegistruLaEditare(ranDocDTO);
        } else if (IndicativXml.ANULEAZA.equals(ranDocDTO.getIndicativ())) {
            validareExistentaGospodarieNomUatHeader(ranDocDTO);
            //cap0_12 nu poate anula cap0_34
            Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
            List<DetinatorPj> lstPj = detinatorPjRepository.findByGospodarieAndFkNomJudet(oldGospodarie.getIdGospodarie(), oldGospodarie.getNomUat().getNomJudet().getId());
            if (lstPj != null && !lstPj.isEmpty()) {
                throw new DateRegistruValidationException(DateRegistruValidationCodes.CAP0_XX_UPDATE_CAP0_YY, TipCapitol.CAP0_34.name(), "anulata", TipCapitol.CAP0_12.name());
            }
            if (gospodarieService.isCapitolGospodarie(oldGospodarie.getIdGospodarie())) {
                throw new DateRegistruValidationException(DateRegistruValidationCodes.GOSPODARIA_ARE_CAPITOLE);
            }
        } else {
            validareExistentaGospodarieNomUatHeader(ranDocDTO);
            //cap0_12 nu poate  dezactiva/reactiva cap0_34
            Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
            List<DetinatorPj> lstPj = detinatorPjRepository.findByGospodarieAndFkNomJudet(oldGospodarie.getIdGospodarie(), oldGospodarie.getNomUat().getNomJudet().getId());
            if (lstPj != null && !lstPj.isEmpty()) {
                throw new DateRegistruValidationException(DateRegistruValidationCodes.CAP0_XX_UPDATE_CAP0_YY, TipCapitol.CAP0_34.name(), "dezactiva/reactiva", TipCapitol.CAP0_12.name());
            }
            //validare de unicitate la reactivare
            if (IndicativXml.REACTIVARE.equals(ranDocDTO.getIndicativ())) {
                validareUnicitateDetinatorPF(oldGospodarie, ranDocDTO.getSirutaUAT());
                //
                DetinatorPf detinatorPf = oldGospodarie.getDetinatorPfs().get(0);
                for (MembruPf membruPf : detinatorPf.getMembruPfs()) {
                    validareUnicitateMembruPF(oldGospodarie, ranDocDTO.getSirutaUAT(), membruPf);
                }
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
        /*populare cu nomenclatoare: identific dupa cod/dataRaportare linia din nomenclatoare corespunzatoare; daca nu gasesc ... eroare*/
        Gospodarie gospodarie = ranDocDTO.getGospodarie();
        DataRaportareValabilitate dataRaportare = new DataRaportareValabilitate();
        /*Header - NOM_UAT*/
        NomUat nomUatHeader = nomSrv.getNomenclatorForStringParam(NomUat, ranDocDTO.getSirutaUAT(), dataRaportare);
        if (nomUatHeader == null) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, NomUat.getLabel(), NomUat.getCodeColumn(), ranDocDTO.getSirutaUAT(), dataRaportare);
        }
        /*GOSPODARIE - NOM_TIP_DETINATOR*/
        NomTipDetinator nomTipDetinator = nomSrv.getNomenclatorForStringParam(NomTipDetinator, gospodarie.getNomTipDetinator().getCod(), dataRaportare);
        if (nomTipDetinator == null) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, NomTipDetinator.getLabel(), NomTipDetinator.getCodeColumn(), gospodarie.getNomTipDetinator().getCod(), dataRaportare);
        }
        gospodarie.setNomTipDetinator(nomTipDetinator);
        /*GOSPODARIE - NomTipExploatatie*/
        NomTipExploatatie nomTipExploatatie = nomSrv.getNomenclatorForStringParam(NomTipExploatatie, gospodarie.getNomTipExploatatie().getCod(), dataRaportare);
        if (nomTipExploatatie == null) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, NomTipExploatatie.getLabel(), NomTipExploatatie.getCodeColumn(), gospodarie.getNomTipExploatatie().getCod(), dataRaportare);
        }
        gospodarie.setNomTipExploatatie(nomTipExploatatie);
        /*ADRESE*/
        valideazaDateRegistruLaEditare_Adrese(gospodarie, nomUatHeader, ranDocDTO, dataRaportare);
        /*DETINATOR_PF*/
        valideazaDateRegistruLaEditare_DetinatorPF(gospodarie, nomUatHeader, ranDocDTO);
    }

    private void valideazaDateRegistruLaEditare_DetinatorPF(Gospodarie gospodarie, NomUat nomUatHeader, RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        /*O gospodarie are un singur DetinatorPf*/
        DetinatorPf detinatorPf = gospodarie.getDetinatorPfs().get(0);
        /*fkNomJudet*/
        detinatorPf.setFkNomJudet(gospodarie.getNomUat().getNomJudet().getId());
        /*PersoanaFizica*/
        PersoanaFizica persoanaFizica = new PersoanaFizica();
        persoanaFizica.setInitialaTata(detinatorPf.getPersoanaFizica().getInitialaTata().toUpperCase());
        persoanaFizica.setPrenume(detinatorPf.getPersoanaFizica().getPrenume().toUpperCase());
        persoanaFizica.setNume(detinatorPf.getPersoanaFizica().getNume().toUpperCase());
        if (StringUtils.isNotEmpty(detinatorPf.getPersoanaFizica().getCnp())) {
            persoanaFizica.setCnp(detinatorPf.getPersoanaFizica().getCnp().toUpperCase());
        }
        if (StringUtils.isNotEmpty(detinatorPf.getPersoanaFizica().getNif())) {
            persoanaFizica.setNif(detinatorPf.getPersoanaFizica().getNif().toUpperCase());
        }
        //validare CNP
        if (persoanaFizica.getCnp() != null &&
                !CnpValidator.isValid(persoanaFizica.getCnp())) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.CNP_INVALID, persoanaFizica.getCnp());
        }
        //
        validareUnicitateDetinatorPF(gospodarie, ranDocDTO.getSirutaUAT());
        //reutilizare PersoanaFizica
        PersoanaFizica dbPersoanaFizica = new PersoanaFizica();
        dbPersoanaFizica.setIdPersoanaFizica(reutilizareService.reutilizarePersoanaFizica(persoanaFizica, nomUatHeader.getId()));
        //
        if (StringUtils.isNotEmpty(detinatorPf.getPersoanaFizica().getCnp())) {
            dbPersoanaFizica.setCnp(detinatorPf.getPersoanaFizica().getCnp().toUpperCase());
        }
        if (StringUtils.isNotEmpty(detinatorPf.getPersoanaFizica().getNif())) {
            dbPersoanaFizica.setNif(detinatorPf.getPersoanaFizica().getNif().toUpperCase());
        }
        detinatorPf.setPersoanaFizica(dbPersoanaFizica);
        /**
         *persoanaRc se completeaza doar pentru tip detinator 1 si tip exploatatie 2;
         *se completeaza doar cu 'PFA', 'II', 'IF'
         */
        if (TipDetinator.PF_DOM_FISCAL_IN_LOC.getCod().equals(gospodarie.getNomTipDetinator().getCod()) &&
                TipExploatatie.PFA_II_IF.getCod().equals(gospodarie.getNomTipExploatatie().getCod())) {
            if (detinatorPf.getPersoanaRc() == null) {
                throw new DateRegistruValidationException(DateRegistruValidationCodes.ELEMENTE_JURIDICE_NECORESPUNZATOR, TipDetinator.PF_DOM_FISCAL_IN_LOC, TipExploatatie.PFA_II_IF);
            } else {
                if (!detinatorPf.getPersoanaRc().getNomFormaOrganizareRc().getCod().equals(FormaOrganizareRc.PERSOANA_FIZICA_AUTORIZATA.getCod()) &&
                        !detinatorPf.getPersoanaRc().getNomFormaOrganizareRc().getCod().equals(FormaOrganizareRc.INTREPRINDERE_FAMILIALA.getCod()) &&
                        !detinatorPf.getPersoanaRc().getNomFormaOrganizareRc().getCod().equals(FormaOrganizareRc.INTREPRINDERE_INDIVIDUALA.getCod())) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.FORMAORGANIZARERC_NECORESPUNZATOR_012, detinatorPf.getPersoanaRc().getNomFormaOrganizareRc().getCod());
                }
            }
        } else {
            if (detinatorPf.getPersoanaRc() != null) {
                throw new DateRegistruValidationException(DateRegistruValidationCodes.ELEMENTE_JURIDICE_NECORESPUNZATOR, TipDetinator.PF_DOM_FISCAL_IN_LOC.getCod(), TipExploatatie.PFA_II_IF.getCod());
            }
        }
        //
        if (detinatorPf.getPersoanaRc() != null) {
            /*PERSOANA_RC - NOM_FORMA_ORGANIZARE_RC*/
            ro.uti.ran.core.model.registru.NomFormaOrganizareRc nomFormaOrganizareRc = nomSrv.getNomenclatorForStringParam(NomFormaOrganizareRc, detinatorPf.getPersoanaRc().getNomFormaOrganizareRc().getCod());
            if (nomFormaOrganizareRc == null) {
                throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND, NomFormaOrganizareRc.getLabel(), NomFormaOrganizareRc.getCodeColumn(), detinatorPf.getPersoanaRc().getNomFormaOrganizareRc().getCod());
            }
            detinatorPf.getPersoanaRc().setNomFormaOrganizareRc(nomFormaOrganizareRc);
            /*PERSOANA_RC*/
            PersoanaRc dbPersoanaRc = new PersoanaRc();
            dbPersoanaRc.setIdPersoanaRc(reutilizareService.reutilizarePersoanaRc(detinatorPf.getPersoanaRc(), nomFormaOrganizareRc.getId(), nomUatHeader.getId()));
            detinatorPf.setPersoanaRc(dbPersoanaRc);
        }
    }


    private void valideazaDateRegistruLaEditare_Adrese(Gospodarie gospodarie, NomUat nomUatHeader, RanDocDTO ranDocDTO, DataRaportareValabilitate dataRaportare) throws DateRegistruValidationException {
        if (gospodarie.getAdresaGospodaries() != null) {
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
                /*validare Localitate - UAT - Judet*/
                validareCoduriSiruta(adresaGospodarie.getAdresa().getNomUat(), adresaGospodarie.getAdresa().getNomJudet(), adresaGospodarie.getAdresa().getNomLocalitate());
                //
                validareAdresaRenns(adresaGospodarie.getAdresa(), "date_identificare_gospodarie_PF");
                //
                validareAdresaCUAandReferintaGeoXml(adresaGospodarie.getAdresa(), "date_identificare_gospodarie_PF");


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
    public Class getCapitolClass() {
        return Capitol_0_12.class;
    }


    @Override
    public void salveazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        if (IndicativXml.ADAUGA_SI_INLOCUIESTE.equals(ranDocDTO.getIndicativ())) {
            if (oldGospodarie != null) {
                //cap0_12 nu poate modifica cap0_34
                List<DetinatorPj> lstPj = detinatorPjRepository.findByGospodarieAndFkNomJudet(oldGospodarie.getIdGospodarie(), oldGospodarie.getNomUat().getNomJudet().getId());
                if (lstPj != null && !lstPj.isEmpty()) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.CAP0_XX_UPDATE_CAP0_YY, TipCapitol.CAP0_34.name(), "modificata", TipCapitol.CAP0_12.name());
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
       /*DETINATOR_PF - O gospodarie are un singur DetinatorPf*/
        DetinatorPf detinatorPf = gospodarie.getDetinatorPfs().get(0);
        detinatorPf.setGospodarie(gospodarie);
        detinatorPfRepository.save(detinatorPf);
        /**
         * ADRESA - domiciliuFiscal + adresaGospodarie
         *
         */
        if (gospodarie.getAdresaGospodaries() != null) {
            /*adresele*/
            for (AdresaGospodarie adresaGospodarie : gospodarie.getAdresaGospodaries()) {
                adresaGospodarie.getAdresa().setDataStart(new Date());
                adresaRepository.save(adresaGospodarie.getAdresa());
                adresaGospodarie.setGospodarie(gospodarie);
                adresaGospodarieRepository.save(adresaGospodarie);
            }
            /*geometrie pentru fiecare adresa*/
            for (AdresaGospodarie adresaGospodarie : gospodarie.getAdresaGospodaries()) {
                if (StringUtils.isNotEmpty(adresaGospodarie.getAdresa().getGeometrieGML())) {
                    geometrieService.insertAdresaGIS(adresaGospodarie.getAdresa().getIdAdresa(), adresaGospodarie.getAdresa().getGeometrieGML(), gospodarie.getNomUat().getNomJudet().getId());
                }
            }
        }
    }

    /**
     * @param oldGospodarie gospodaria din baza de date
     * @param ranDocDTO     info trimise prin xml
     */
    private void updateDateRegistru(Gospodarie oldGospodarie, RanDocDTO ranDocDTO) throws DateRegistruValidationException {
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
        /*DETINATOR_PF - este obligatoriu in XML;*/
        DetinatorPf oldDetinatorPf = oldGospodarie.getDetinatorPfs().get(0);
        DetinatorPf newDetinatorPf = newGospodarie.getDetinatorPfs().get(0);
        //update DETINATOR_PF
        oldDetinatorPf.setPersoanaFizica(newDetinatorPf.getPersoanaFizica());
        if (newDetinatorPf.getPersoanaRc() != null) {
            oldDetinatorPf.setPersoanaRc(newDetinatorPf.getPersoanaRc());
        } else {
            oldDetinatorPf.setPersoanaRc(null);
        }
        detinatorPfRepository.save(oldDetinatorPf);
        /*Cap gospodarie cnp_cap1 = cnp_cap012*/
        List<MembruPf> oldMembruPfs = membruPfRepository.findByCodLegaturaRudenieAndGospodarie(LegaturaRudenie.CAP_GOSPODARIE.getCod(), oldGospodarie.getIdGospodarie());
        if (oldMembruPfs != null && !oldMembruPfs.isEmpty()) {
            MembruPf oldMembruPf = oldMembruPfs.get(0);
            if (StringUtils.isNotEmpty(oldMembruPf.getPersoanaFizica().getCnp())
                    && !oldMembruPf.getPersoanaFizica().getCnp().equalsIgnoreCase(oldDetinatorPf.getPersoanaFizica().getCnp() != null ? oldDetinatorPf.getPersoanaFizica().getCnp() : "")) {
                throw new DateRegistruValidationException(DateRegistruValidationCodes.CNP_CAP012_CAP1, oldDetinatorPf.getPersoanaFizica().getCnp() != null ? oldDetinatorPf.getPersoanaFizica().getCnp() : "", oldMembruPf.getPersoanaFizica().getCnp());
            }
            if (StringUtils.isNotEmpty(oldMembruPf.getPersoanaFizica().getNif())
                    && !oldMembruPf.getPersoanaFizica().getNif().equalsIgnoreCase(oldDetinatorPf.getPersoanaFizica().getNif() != null ? oldDetinatorPf.getPersoanaFizica().getNif() : "")) {
                throw new DateRegistruValidationException(DateRegistruValidationCodes.NIF_CAP012_CAP1, oldDetinatorPf.getPersoanaFizica().getNif() != null ? oldDetinatorPf.getPersoanaFizica().getNif() : "", oldMembruPf.getPersoanaFizica().getNif());
            }
        }
        /*ADRESA*/
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
    public void populeazaDateCapitol(GospodarieDTO gospodarieDTO, Integer an, Byte semestru) {
        /*ADRESA - domiciliuFiscal*/
        AdresaGospodarie domiciliuFiscal = adresaGospodarieRepository.findByCodTipAdresaAndGospodarie(TipAdresa.DOMICILIU_FISCAL.getCod(), gospodarieDTO.getGospodarie().getIdGospodarie());
        /*ADRESA - adresaGospodarie*/
        AdresaGospodarie adresaGospodarie = adresaGospodarieRepository.findByCodTipAdresaAndGospodarie(TipAdresa.GOSPODARIE.getCod(), gospodarieDTO.getGospodarie().getIdGospodarie());
        if (domiciliuFiscal != null || adresaGospodarie != null) {
            List<AdresaGospodarie> lstAdresa = new ArrayList<>();
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
        /*DETINATOR_PF*/
        gospodarieDTO.setDetinatorPfs(detinatorPfRepository.findByGospodarieAndFkNomJudet(gospodarieDTO.getGospodarie().getIdGospodarie(), gospodarieDTO.getGospodarie().getNomUat().getNomJudet().getId()));
    }

}
