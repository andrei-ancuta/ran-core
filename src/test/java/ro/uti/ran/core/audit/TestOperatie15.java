import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ro.uti.ran.core.config.DevDataSourceConfig;
import ro.uti.ran.core.config.PortalPersistenceLayerConfig;
import ro.uti.ran.core.config.Profiles;
import ro.uti.ran.core.config.RegistruPersistenceLayerConfig;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.model.portal.OperatieSesiune;
import ro.uti.ran.core.model.portal.Sesiune;
import ro.uti.ran.core.model.portal.TipOperatie;
import ro.uti.ran.core.model.registru.NomJudet;
import ro.uti.ran.core.model.registru.NomUat;
import ro.uti.ran.core.model.registru.Nomenclator;
import ro.uti.ran.core.model.registru.TipNomenclator;
import ro.uti.ran.core.repository.portal.OperatieSesiuneRepository;
import ro.uti.ran.core.repository.portal.SesiuneRepository;
import ro.uti.ran.core.repository.portal.TipOperatieRepository;
import ro.uti.ran.core.service.nomenclator.NomenclatorSearchFilter;
import ro.uti.ran.core.service.nomenclator.NomenclatorService;
import ro.uti.ran.core.utils.GenericListResult;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {
                DevDataSourceConfig.class,
                PortalPersistenceLayerConfig.class,
                RegistruPersistenceLayerConfig.class,
                TestOperatie15.ImportConfiguration.class
        })
@ActiveProfiles(profiles = {Profiles.DEV})
public class TestOperatie15 {

    @ContextConfiguration
    @ComponentScan({"ro.uti.ran.core.repository", "ro.uti.ran.core.service.nomenclator"})
    static class ImportConfiguration {

    }

    @Autowired
    private TipOperatieRepository tipOperatieRepository;
    @Autowired
    private OperatieSesiuneRepository operatieSesiuneRepo;

    @Autowired
    private SesiuneRepository sesiuneRepo;

    @Test
    public void testAdaugareOperatie() throws RanBusinessException {
        Sesiune sesiuneaCurenta = sesiuneRepo.findByToken("AQIC5wM2LY4SfcxZHD3V-NdA6h2VGn7BjZBwNKfVgo9vEfg.*AAJTSQACMDEAAlNLABQtMzA2NTcxMTgxNDcxMDQzNDAyOQ..*").get(0);

                TipOperatie operatieCurenta = tipOperatieRepository.findByCod("NOM-SAVE");;


            OperatieSesiune operatieSesiune = new OperatieSesiune();
            operatieSesiune.setDataOperatie(new Date());
            operatieSesiune.setDescriere(
                    "Operatie: SALVARE_INREGISTRARE_NOMENCLATOR executata cu parametrii de intrare: [{\"id\":1004,\"baseId\":1004,\"codAlfa2\":\"MD\",\"codAlfa3\":\"MDA\",\"codNumeric\":498,\"dataStart\":1460667600000,\"dataStop\":null,\"denumire\":\"Republica Moldova audit..\",\"lastModifiedDate\":null}] rezultat intors: {\"id\":1004,\"baseId\":1004,\"codAlfa2\":\"MD\",\"codAlfa3\":\"MDA\",\"codNumeric\":498,\"dataStart\":1460667600000,\"dataStop\":null,\"denumire\":\"Republica Moldova audit..\",\"lastModifiedDate\":1462261012000}"
            );
            operatieSesiune.setDescriereComplet(
                    "S-a descarcat arhiva zip corespunzatoare nomenclatorului ro.uti.ran.core.model.registru.NomTara@57814f99."
            );
            operatieSesiune.setSesiune(sesiuneaCurenta);
            operatieSesiune.setTipOperatie(operatieCurenta);
            operatieSesiuneRepo.save(operatieSesiune);


    }
}