package ro.uti.ran.core.service.nomenclator;

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
import ro.uti.ran.core.model.registru.*;
import ro.uti.ran.core.utils.GenericListResult;
import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;

import java.util.Date;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-12-09 17:21
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {
                DevDataSourceConfig.class,
                PortalPersistenceLayerConfig.class,
                RegistruPersistenceLayerConfig.class,
                NomenclatorUtilityImplTests.ImportConfiguration.class
        })
@ActiveProfiles(profiles = {Profiles.DEV})
public class NomenclatorUtilityImplTests {

    @ContextConfiguration
    @ComponentScan({"ro.uti.ran.core.repository", "ro.uti.ran.core.service.nomenclator"})
    static class ImportConfiguration {

    }

    @Autowired
    NomenclatorService nomenclatorService;

    @Test
    public void testAdaugareNomenclator() throws RanBusinessException, RanRuntimeException, RanException {

        NomUat uat = new NomUat();
        uat.setDenumire("TEST");
        uat.setCodSiruta(1);
        uat.setDataStart(new Date());

//
        NomJudet judet = new NomJudet();
        judet.setId(1L);

        uat.setNomJudet(judet);


        NomUat saved = (NomUat) nomenclatorService.saveNomenclator(uat);

        Assert.assertNotNull(saved);
        Assert.assertNotNull(saved.getId());
    }

    @Test
    public void testAdaugareIdZero() throws RanBusinessException, RanRuntimeException, RanException {

        NomJudet judet = new NomJudet();
        judet.setId(0L);
        judet.setCodSiruta(1);
        judet.setDenumire("1");
        judet.setCodAlfa("1");
        judet.setDataStart(new Date());

        NomUat saved = (NomUat) nomenclatorService.saveNomenclator(judet);

        Assert.assertNotNull(saved);
        Assert.assertNotNull(saved.getId());
    }

    @Test
    public void testUpdateNomenclator() throws RanBusinessException, RanRuntimeException, RanException {
        GenericListResult<Nomenclator> uats = nomenclatorService.getListaNomenclator(new NomenclatorSearchFilter() {
                                           {
                                               setType(TipNomenclator.NomUat);
                                               setDenumire("TEST");
                                           }
                                       },
                null, null);

        NomUat uat = (NomUat) uats.getItems().get(0);

        Integer newCod = 22;
        uat.setCodSiruta(newCod);

        NomUat saved = (NomUat) nomenclatorService.saveNomenclator(uat);

        Assert.assertNotNull(saved);
        Assert.assertNotNull(saved.getId());
        Assert.assertEquals(saved.getId(), uat.getId());
        Assert.assertEquals(saved.getCodSiruta(), newCod);

    }

    @Test
    public void testGetNomCapitoleActive(){
        NomenclatorSearchFilter filter = new NomenclatorSearchFilter();
        filter.setType(TipNomenclator.NomCapitol);

        GenericListResult result = nomenclatorService.getListaNomenclator(filter, null, null);
        System.out.println("result = " + result);
    }

    @Test
    public void testGetNomAnimalProdActive(){
        NomenclatorSearchFilter filter = new NomenclatorSearchFilter();
        filter.setType(TipNomenclator.CapAnimalProd);
        filter.setShowActiveRecords(true);
        filter.setShowHistoryRecords(false);

        GenericListResult result = nomenclatorService.getListaNomenclator(filter, null, null);
        System.out.println("result = " + result);
    }

    @Test
    public void testDeleteNomenclatorSistem() throws RanBusinessException, RanRuntimeException, RanException {
        nomenclatorService.deleteNomenclator(TipNomenclator.NomIndicativXml, 1L);
    }

    @Test
    public void testDeleteNomenclatorCapRA() throws RanBusinessException, RanRuntimeException, RanException {
        nomenclatorService.deleteNomenclator(TipNomenclator.NomIndicativXml, 1L);
    }
}
