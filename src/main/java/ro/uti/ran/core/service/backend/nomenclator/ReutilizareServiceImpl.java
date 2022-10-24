package ro.uti.ran.core.service.backend.nomenclator;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.datasourcerouter.ContextHolder;
import ro.uti.ran.core.dto.ParametriiInterogarePF;
import ro.uti.ran.core.dto.ParametriiInterogarePJ;
import ro.uti.ran.core.model.registru.PersoanaFizica;
import ro.uti.ran.core.model.registru.PersoanaRc;
import ro.uti.ran.core.repository.introducere.PersoanaPFRepository;
import ro.uti.ran.core.repository.introducere.PersoanaPJRepository;
import ro.uti.ran.core.repository.registru.PersoanaFizicaRepository;
import ro.uti.ran.core.repository.registru.PersoanaRcRepository;

import java.util.Date;
import java.util.List;

import static ro.uti.ran.core.datasourcerouter.EnvironmentType.RAL;
import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.NomFormaOrganizareRc;

/**
 * Created by Dan on 19-Jan-16.
 */
@Service
@Transactional(value = "registruTransactionManager", propagation = Propagation.REQUIRES_NEW)
public class ReutilizareServiceImpl implements ReutilizareService {

    @Autowired
    protected NomenclatorService nomSrv;
    @Autowired
    private PersoanaFizicaRepository persoanaFizicaRepository;
    @Autowired
    private PersoanaRcRepository persoanaRcRepository;

    @Autowired
    private PersoanaPFRepository persoanaPFRepository;
    @Autowired
    private PersoanaPJRepository persoanaPJRepository;


    @Override
    public synchronized Long reutilizarePersoanaFizica(PersoanaFizica persoanaFizica, Long idNomUat) {
        if (RAL.equals(ContextHolder.getEnvironmentType())) {
            ParametriiInterogarePF parametriiInterogare =
                    new ParametriiInterogarePF.ParametriiInterogarePFBuilder(
                            persoanaFizica.getNume().toUpperCase(),
                            persoanaFizica.getPrenume().toUpperCase(),
                            persoanaFizica.getInitialaTata().toUpperCase(),
                            idNomUat)
                            .cnp(persoanaFizica.getCnp())
                            .nif(persoanaFizica.getNif())
                            .build();
            return persoanaPFRepository.findPersoanaFizicaFromIntroducere(parametriiInterogare);
        } else {
            List<PersoanaFizica> db = null;
            if (StringUtils.isNotEmpty(persoanaFizica.getCnp())) {
                db = persoanaFizicaRepository.findByNumeAndPrenumeAndInitialaTataAndCnp(
                        persoanaFizica.getNume().toUpperCase(),
                        persoanaFizica.getPrenume().toUpperCase(),
                        persoanaFizica.getInitialaTata().toUpperCase(),
                        persoanaFizica.getCnp().toUpperCase());
            } else if (StringUtils.isNotEmpty(persoanaFizica.getNif())) {
                db = persoanaFizicaRepository.findByNumeAndPrenumeAndInitialaTataAndNif(
                        persoanaFizica.getNume().toUpperCase(),
                        persoanaFizica.getPrenume().toUpperCase(),
                        persoanaFizica.getInitialaTata().toUpperCase(),
                        persoanaFizica.getNif().toUpperCase());
            }
            if (db != null && !db.isEmpty()) {
                return db.get(0).getIdPersoanaFizica();
            } else {
                persoanaFizica.setDataStart(new Date());
                persoanaFizica = persoanaFizicaRepository.save(persoanaFizica);
                return persoanaFizica.getIdPersoanaFizica();
            }
        }
    }

    @Override
    public synchronized Long reutilizarePersoanaRc(PersoanaRc persoanaRc, Long idNomFormaOrganizareRc, Long idNomUat) {
        if (RAL.equals(ContextHolder.getEnvironmentType())) {
            ParametriiInterogarePJ parametriiInterogare =
                    new ParametriiInterogarePJ.ParametriiInterogarePJBuilder(
                            persoanaRc.getCui().toUpperCase(),
                            persoanaRc.getDenumire().toUpperCase(),
                            idNomFormaOrganizareRc,
                            idNomUat)
                            .cif(persoanaRc.getCif())
                            .build();
            return persoanaPJRepository.findPersoanaRcFromIntroducere(parametriiInterogare);
        } else {
            List<PersoanaRc> db = null;
            ro.uti.ran.core.model.registru.NomFormaOrganizareRc nomFormaOrganizareRc = nomSrv.getNomenlatorForId(NomFormaOrganizareRc, idNomFormaOrganizareRc);
            persoanaRc.setNomFormaOrganizareRc(nomFormaOrganizareRc);
            if (persoanaRc.getCif() != null) {
                db = persoanaRcRepository.findByCuiAndCifAndDenumireAndNomFormaOrganizareRcId(persoanaRc.getCui().toUpperCase(), persoanaRc.getCif().toUpperCase(), persoanaRc.getDenumire().toUpperCase(), idNomFormaOrganizareRc);
            } else {
                db = persoanaRcRepository.findByCuiAndDenumireAndNomFormaOrganizareRcId(persoanaRc.getCui().toUpperCase(), persoanaRc.getDenumire().toUpperCase(), idNomFormaOrganizareRc);
            }
            if (db != null && !db.isEmpty()) {
                return db.get(0).getIdPersoanaRc();
            } else {
                persoanaRc.setDataStart(new Date());
                persoanaRcRepository.save(persoanaRc);
                return persoanaRc.getIdPersoanaRc();
            }
        }
    }
}
