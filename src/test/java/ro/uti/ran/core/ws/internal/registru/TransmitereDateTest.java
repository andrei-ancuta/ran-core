package ro.uti.ran.core.ws.internal.registru;
/**
 * Created by miroslav.rusnac on 04/02/2016.
 */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ro.uti.ran.core.ImportConfiguration;
import ro.uti.ran.core.ImportConfigurationAsyncLayerConfig;
import ro.uti.ran.core.config.*;
import ro.uti.ran.core.model.registru.NomTipDetinator;
import ro.uti.ran.core.repository.registru.NomenclatorRepository;
import ro.uti.ran.core.repository.registru.NomenclatorRepositoryImpl;
import ro.uti.ran.core.service.exportNomenclatoare.impl.NomenclatorClassificationServiceImpl;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;
import ro.uti.ran.core.ws.internal.RanAuthorization;
import ro.uti.ran.core.ws.internal.transmitere.TransmitereDate;
import ro.uti.ran.core.ws.model.transmitere.ModalitateTransmitere;
import ro.uti.ran.core.xml.model.types.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.DEV)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {
                RegistruPersistenceLayerConfig.class,
                PortalPersistenceLayerConfig.class,
                DevDataSourceConfig.class,
                ImportConfiguration.class,
                TransmisiiServiceImpl.class,
                NomenclatorRepositoryImpl.class,
                NomenclatorClassificationServiceImpl.class,
                ImportConfigurationAsyncLayerConfig.class

                // ParametruServiceDevImpl.class
        }
)
public class TransmitereDateTest {

    @Autowired
    @Qualifier(value = "transmitereDateService")
    TransmitereDate transmitereDateService;

//    @Autowired
//    @Qualifier(value="transmisiiService")
//    ITransmisiiService transmisiiService;

    @Autowired
    DataSourceConfig dataSourceConfig;

    @Autowired
    NomenclatorRepository nomTest;
    //=====Data import
    private String part1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
            "<DOCUMENT_RAN>\n" +
            "    <HEADER>\n" +
            "        <codXml value=\"";
    private String part2 = "\"/>\n" +
            "        <dataExport>2016-02-04T18:53:35.743+02:00</dataExport>\n" +
            "        <indicativ>ADAUGA_SI_INLOCUIESTE</indicativ>\n" +
            "        <sirutaUAT>168452</sirutaUAT>\n" +
            "    </HEADER>\n" +
            "    <BODY>\n" +
            "        <gospodarie identificator=\"152302569\">\n" +
            "            <capitol_0_12 codCapitol=\"CAP0_12\" denumire=\"date_identificare_gospodarie_PF\">\n" +
            "                <date_identificare_gospodarie_PF>\n" +
            "                    <adresaGospodarie>\n" +
            "                        <apartament></apartament>\n" +
            "                        <bloc></bloc>\n" +
            "                        <numar>-</numar>\n" +
            "                        <scara></scara>\n" +
            "                        <sirutaJudet>387</sirutaJudet>\n" +
            "                        <sirutaLocalitate>168540</sirutaLocalitate>\n" +
            "                        <sirutaUAT>168452</sirutaUAT>\n" +
            "                        <strada>-</strada>\n" +
            "                    </adresaGospodarie>\n" +
            "                    <codExploatatie></codExploatatie>\n" +
            "                    <domiciliuFiscalExtern>\n" +
            "                        <adresaText>Via Santa Sofia no. 44 Milano</adresaText>\n" +
            "                        <codTara>380</codTara>\n" +
            "                    </domiciliuFiscalExtern>\n" +
            "                    <nrUnicIdentificare>676777</nrUnicIdentificare>\n" +
            "                    <pozitieGospodarie>\n" +
            "                        <pozitiaAnterioara>37</pozitiaAnterioara>\n" +
            "                        <pozitieCurenta>36</pozitieCurenta>\n" +
            "                        <volumul>volum gosp</volumul>\n" +
            "                        <rolNominalUnic>3333</rolNominalUnic>\n" +
            "                    </pozitieGospodarie>\n" +
            "                    <tipDetinator>2</tipDetinator>\n" +
            "                    <tipExploatatie>1</tipExploatatie>\n" +
            "                    <gospodar>\n" +
            "                        <nume>Cantemir</nume>\n" +
            "                        <prenume>dimitrie</prenume>\n" +
            "                        <initialaTata>M</initialaTata>\n" +
            "                        <cnp value=\"1650202110251\"/>\n" +
            "                    </gospodar>\n" +
            "                </date_identificare_gospodarie_PF>\n" +
            "            </capitol_0_12>\n" +
            "        </gospodarie>\n" +
            "    </BODY>\n" +
            "</DOCUMENT_RAN>\n";

    @Test
    public void Test1() throws Exception {
//        DataSource source = dataSourceConfig.registruDataSource();
//        Connection conn= source.getConnection();
//        conn.setAutoCommit(false);

        NomTipDetinator nom1 = (NomTipDetinator) nomTest.findOne(NomTipDetinator.class, 2L);
        System.out.println("== " + nom1.getCod() + "===" + nom1.getDenumire() + "====" + nom1.getDataStart() + "===" + nom1.getDataStop() + "===");

        //System.out.println("Autocommit " +source.getConnection().getAutoCommit());
        FiltruTransmisii filtru = new FiltruTransmisii();
        PagingInfo pagingInfo = new PagingInfo(0, 1000);
        SortInfo sortInfo = new SortInfo();

        UUID uuid = buildUUID();
        String dataExport = part1 + uuid.getValue() + part2;

        //logger.org.springframework.transaction=DEBUG;
        transmitereDateService.transmitere(dataExport, ModalitateTransmitere.AUTOMAT, buildRanAuthorization());
        // InformatiiTransmisie status=transmitereDateService.getStatusTransmisie(uuid.getValue(), buildRanAuthorization());
        // assertEquals("Informatia s-a salvat? ",status.getStareTransmisie(), StareTransmisie.SALVATA);

//        filtru.setIndexRegistru(uuid.getValue());
//        TransmisieList listaTransmisii=transmisiiService.getListaIncarcari(filtru,pagingInfo,sortInfo);
//        assertEquals("E strict necesar sa existe o singura intrare ",listaTransmisii.getItems().size(),1);
//
//        //verificam datele inscrise
//        assertEquals("IN == Stocat ",listaTransmisii.getItems().get(0).getContinut(),dataExport);


    }

    private UUID buildUUID() {
        UUID uuid = new UUID();
        uuid.setValue(java.util.UUID.randomUUID().toString());
        return uuid;
    }

    private RanAuthorization buildRanAuthorization() {
        RanAuthorization ranAuthorization = new RanAuthorization();
        ranAuthorization.setContext("UAT");
        ranAuthorization.setIdEntity(2929L);

        return ranAuthorization;
    }

}
