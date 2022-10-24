package ro.uti.ran.core.service.backend.capitol;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.codes.DateRegistruValidationCodes;
import ro.uti.ran.core.model.registru.Adresa;
import ro.uti.ran.core.model.registru.Cladire;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.utils.IndicativXml;
import ro.uti.ran.core.service.backend.GeometrieService;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;
import ro.uti.ran.core.service.backend.utils.DataRaportareValabilitate;
import ro.uti.ran.core.xml.model.capitol.Capitol_11;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.*;


/**
 * Clădiri existente la începutul anului pe raza localității
 * Created by Dan on 06-Nov-15.
 */
@Service("capitol_11Service")
@Transactional(value = "registruTransactionManager", rollbackFor = RanBusinessException.class)
public class Capitol_11ServiceImpl extends AbstractCapitolFara0XXService {

    @Autowired
    private GeometrieService geometrieService;

    @Override
    public void valideazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        if (IndicativXml.ADAUGA_SI_INLOCUIESTE.equals(ranDocDTO.getIndicativ())) {
            valideazaDateRegistruLaEditare(ranDocDTO);
        } else {
            validareExistentaGospodarieNomUatHeader(ranDocDTO);
        }
    }


    /**
     * - an + fk_gospodarie + identificator
     *
     * @param ranDocDTO datele ce trebuiesc validate (din mesajul XML)
     * @throws DateRegistruValidationException semnaleaza date invalide
     */
    public void valideazaDateRegistruLaEditare(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
       /*gospodaria trebuie sa existe in baza*/
        Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        if (oldGospodarie == null) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.GOSPODARIE_NOT_FOUND, ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        }
        Gospodarie gospodarie = ranDocDTO.getGospodarie();
        if (gospodarie.getCladires() != null) {
            List<String> xmlIdentificators = new ArrayList<>();
            DataRaportareValabilitate dataRaportare = new DataRaportareValabilitate(ranDocDTO.getAnRaportare());
            for (Cladire cladire : gospodarie.getCladires()) {

                String terenGml = cladire.getGeometrieGML();
                if (terenGml != null) {
                    geometrieService.validateGeometrie(terenGml);
                    geometrieService.validateGeometriePoligonUatLimit(ranDocDTO.getSirutaUAT(), terenGml);
                }
                String identificator = cladire.getIdentificator();
                /*unicitate*/
                if (xmlIdentificators.contains(identificator)) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.SECTIUNE_CAPITOL_DUPLICATA, "cladire", "IDENTIFICATOR", identificator);
                } else {
                    xmlIdentificators.add(identificator);
                }
                /*FK_NOM_TIP_CLADIRE*/
                if (cladire.getNomTipCladire() != null) {
                    ro.uti.ran.core.model.registru.NomTipCladire nomTipCladire = nomSrv.getNomenclatorForStringParam(NomTipCladire, cladire.getNomTipCladire().getCod(), dataRaportare);
                    if (nomTipCladire == null) {
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, NomTipCladire.getLabel(), NomTipCladire.getCodeColumn(), cladire.getNomTipCladire().getCod(), dataRaportare);
                    }
                    cladire.setNomTipCladire(nomTipCladire);
                }
                /*FK_NOM_DESTINATIE_CLADIRE*/
                if (cladire.getNomDestinatieCladire() != null) {
                    ro.uti.ran.core.model.registru.NomDestinatieCladire nomDestinatieCladire = nomSrv.getNomenclatorForStringParam(NomDestinatieCladire, cladire.getNomDestinatieCladire().getCod(), dataRaportare);
                    if (nomDestinatieCladire == null) {
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, NomDestinatieCladire.getLabel(), NomDestinatieCladire.getCodeColumn(), cladire.getNomDestinatieCladire().getCod(), dataRaportare);
                    }
                    cladire.setNomDestinatieCladire(nomDestinatieCladire);
                }
                /*FK_ADRESA*/
                if (cladire.getAdresa() != null) {
                    Adresa adresa = cladire.getAdresa();
                    /*CLADIRE ADRESA  NOM_TARA*/
                    ro.uti.ran.core.model.registru.NomTara nomTara = nomSrv.getNomenclatorForStringParam(NomTara, adresa.getNomTara().getCodNumeric(), dataRaportare);
                    if (nomTara == null) {
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, NomTara.getLabel(), NomTara.getCodeColumn(), adresa.getNomTara().getCodNumeric(), ranDocDTO.getDataExport());
                    }
                    adresa.setNomTara(nomTara);
                    /*CLADIRE ADRESA  NOM_LOCALITATE*/
                    ro.uti.ran.core.model.registru.NomLocalitate nomLocalitate = nomSrv.getNomenclatorForStringParam(NomLocalitate, adresa.getNomLocalitate().getCodSiruta(), dataRaportare);
                    if (nomLocalitate == null) {
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, NomLocalitate.getLabel(), NomLocalitate.getCodeColumn(), adresa.getNomLocalitate().getCodSiruta(), dataRaportare);
                    }
                    adresa.setNomLocalitate(nomLocalitate);
                    /*CLADIRE ADRESA NOM_JUDET*/
                    if (adresa.getNomJudet() != null) {
                        ro.uti.ran.core.model.registru.NomJudet nomJudet = nomSrv.getNomenclatorForStringParam(NomJudet, adresa.getNomJudet().getCodSiruta(), dataRaportare);
                        if (nomJudet == null) {
                            throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, NomJudet.getLabel(), NomJudet.getCodeColumn(), adresa.getNomJudet().getCodSiruta(), dataRaportare);
                        }
                        adresa.setNomJudet(nomJudet);
                        cladire.setFkNomJudet(nomJudet.getId());
                    }
                    /*CLADIRE ADRESA  NOM_UAT*/
                    if (adresa.getNomUat() != null) {
                        ro.uti.ran.core.model.registru.NomUat nomUatAdresa = nomSrv.getNomenclatorForStringParam(NomUat, adresa.getNomUat().getCodSiruta(), dataRaportare);
                        if (nomUatAdresa == null) {
                            throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, NomUat.getLabel(), NomUat.getCodeColumn(), adresa.getNomUat().getCodSiruta(), dataRaportare);
                        }
                        adresa.setNomUat(nomUatAdresa);
                    }
                    validareCoduriSiruta(adresa.getNomUat(), adresa.getNomJudet(), adresa.getNomLocalitate());
                    //
                    validareAdresaRenns(adresa, "cladire");
                    //
                    validareAdresaCUAandReferintaGeoXml(adresa, "cladire");
                    //
                    if (StringUtils.isNotEmpty(adresa.getGeometrieGML())) {
                        //geometria va fi validata sa fie o geometrie corecta si de tip 'punct'
                        geometrieService.validateGeometrie(adresa.getGeometrieGML());
                        //adresa gospodariei/cladirilor sunt exclusiv din UAT-ul care transmite
                        geometrieService.validateGeometriePunctUatLimit(ranDocDTO.getSirutaUAT(), adresa.getGeometrieGML());
                    }
                }
                /*fkNomJudet*/
                cladire.setFkNomJudet(oldGospodarie.getNomUat().getNomJudet().getId());
            }
        }
    }


    /**
     * @param ranDocDTO info trimse prin xml
     */
    public void addUpdateDateRegistru(RanDocDTO ranDocDTO) {
        Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        List<Cladire> oldCladiresDinAn = cladireRepository.findByAnAndGospodarieAndFkNomJudet(ranDocDTO.getAnRaportare(), oldGospodarie.getIdGospodarie(), oldGospodarie.getNomUat().getNomJudet().getId());
        if (ranDocDTO.getGospodarie().getCladires() != null) {
            /*stergere*/
            if (oldCladiresDinAn != null) {
                for (Cladire oldCladire : oldCladiresDinAn) {
                    cladireRepository.delete(oldCladire);
                }
            }
            for (Cladire xmlCladire : ranDocDTO.getGospodarie().getCladires()) {
                /*adaugare*/
                xmlCladire.getAdresa().setDataStart(new Date());
                adresaRepository.save(xmlCladire.getAdresa());
                //
                xmlCladire.setGospodarie(oldGospodarie);
                cladireRepository.save(xmlCladire);
            }
            /*Geometria*/
            for (Cladire xmlCladire : ranDocDTO.getGospodarie().getCladires()) {
                if (StringUtils.isNotEmpty(xmlCladire.getGeometrieGML())) {
                    geometrieService.insertCladireGIS(xmlCladire.getIdCladire(), xmlCladire.getGeometrieGML(), oldGospodarie.getNomUat().getNomJudet().getId());
                }
            }
        }
    }

    /**
     * Identific info din XML cu cele din baza dupa anumite chei simple sau compuse (cnp, an, semestru, cod nomenclator, codRand, etc).
     * Info din XML care apare si in DB - delete
     *
     * @param ranDocDTO info trimse prin xml
     */
    public void anuleazaDateRegistru(RanDocDTO ranDocDTO) {
        Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        List<Cladire> oldCladiresDinAn = cladireRepository.findByAnAndGospodarieAndFkNomJudet(ranDocDTO.getAnRaportare(), oldGospodarie.getIdGospodarie(), oldGospodarie.getNomUat().getNomJudet().getId());
        /*anulare intreg capitol*/
        if (oldCladiresDinAn != null) {
            for (Cladire oldCladire : oldCladiresDinAn) {
                cladireRepository.delete(oldCladire);
            }
        }
    }

    @Override
    public void populeazaDateCapitol(GospodarieDTO gospodarieDTO, Integer an, Byte semestru) {
        /*CLADIRE - din an*/
        gospodarieDTO.setCladires(cladireRepository.findByAnAndGospodarieAndFkNomJudet(an, gospodarieDTO.getGospodarie().getIdGospodarie(), gospodarieDTO.getGospodarie().getNomUat().getNomJudet().getId()));
        /*Geometrie*/
        if (gospodarieDTO.getCladires() != null) {
            for (Cladire cladire : gospodarieDTO.getCladires()) {
                if (cladire.getGeometrieCladire() != null) {
                    String geometrieGML = geometrieService.getCladireGIS(cladire.getGeometrieCladire().getIdGeometrieCladire());
                    if (StringUtils.isNotEmpty(geometrieGML)) {
                        cladire.setGeometrieGML(geometrieGML);
                    }
                }
            }
        }
    }

    @Override
    public Class getCapitolClass() {
        return Capitol_11.class;
    }
}