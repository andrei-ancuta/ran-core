package ro.uti.ran.core.ws.internal.sistem;

/**
 * Created by miroslav.rusnac on 02/02/2016.
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.SerializationUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ro.uti.ran.core.config.*;
import ro.uti.ran.core.model.portal.*;
import ro.uti.ran.core.model.portal.Rol;
import ro.uti.ran.core.service.idm.openam.IdmServiceOpenAM12RestApiImpl;
import ro.uti.ran.core.service.report.ReportUtilizatoriService;
import ro.uti.ran.core.service.sistem.MailServiceImpl;
import ro.uti.ran.core.service.sistem.MessageBundleServiceImpl;
import ro.uti.ran.core.service.sistem.SistemServiceImpl;
import ro.uti.ran.core.service.sistem.TokenGenerator;
import ro.uti.ran.core.service.utilizator.RoluriSearchFilter;
import ro.uti.ran.core.service.utilizator.UatConfigServiceImpl;
import ro.uti.ran.core.service.utilizator.UtilizatorServiceImpl;
import ro.uti.ran.core.service.utilizator.UtilizatoriSearchFilter;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;
import ro.uti.ran.core.ws.WsUtilsService;
import ro.uti.ran.core.ws.internal.utilizator.*;
import ro.uti.ran.core.ws.utils.ExceptionUtil;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.TEST)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {TestDatasourceConfig.class,
                AdminUtilizatorTest.ImportConfiguration.class,
                UtilizatorServiceImpl.class,
                PortalPersistenceLayerConfig.class,
                IdmServiceOpenAM12RestApiImpl.class,
                RestTemplate.class,
                OpenAmConfig.class,
                SistemServiceImpl.class,
                TokenGenerator.class,
                StandardPasswordEncoder.class,
                ExceptionUtil.class,
                UatConfigServiceImpl.class,
                RegistruPersistenceLayerConfig.class,
                MailServiceImpl.class,
                JavaMailSenderImpl.class,
                MessageBundleServiceImpl.class,
                ReportUtilizatoriService.class,
                ObjectMapper.class
        }
)

//@TransactionConfiguration(transactionManager = "portalTransactionManager", defaultRollback = true)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AdminUtilizatorTest {

    @ContextConfiguration
    @ComponentScan({
            "ro.uti.ran.core.ws.internal.utilizator",
            "ro.uti.ran.core.ws.utils",
            "ro.uti.ran.core.service.idm"
    })

    @PropertySource("classpath:application.properties")


    static class ImportConfiguration {

        @Bean
        public AdminUtilizator getInfoUtilizatorService() {
            return new AdminUtilizatorImpl();
        }

        @Bean
        public WsUtilsService getWsUtilsService() {
            return new WsUtilsService();
        }

    }

    @Autowired
    @Qualifier(value = "adminUtilizatorService")
    AdminUtilizator adminService;

//    @Autowired
//    DataSourceConfig dataSourceConfig;
//
//    @Test
//    public void Test10GetListaRoluri() throws Exception {
//        PagingInfo pagingInfo = new PagingInfo(0, 1000);
//        RoluriSearchFilter filter = new RoluriSearchFilter();
//        SortInfo sortInfo = new SortInfo();
//        RoluriListResult listaRoluri = adminService.getListaRoluri(filter, pagingInfo, sortInfo);
//        System.out.println("== Lista roluri ==");
//        List<Rol> listRols = listaRoluri.getItems();
//        for (Rol rol : listRols) {
//            System.out.println("Cod rol: " + rol.getCod() + " Denumire rol: " + rol.getDenumire());
//        }
//
//    }
//
//    @Test
//    public void Test20GetListaUtilizatori() throws Exception {
//        PagingInfo pagingInfo = new PagingInfo(0, 1000);
//        SortInfo sortInfo = new SortInfo();
//        UtilizatoriSearchFilter searchFilter = new UtilizatoriSearchFilter();
//        UtilizatoriListResult listaUsers = adminService.getListaUtilizatori(searchFilter, pagingInfo, sortInfo);
//        System.out.println("== Lista utilizatori ==");
//        List<Utilizator> users = listaUsers.getItems();
//        for (Utilizator detaliiUtilizator : users) {
//            System.out.println("==   ID utilizator ====" + detaliiUtilizator.getId());
//            System.out.println("Nume:" + detaliiUtilizator.getNume());
//            System.out.println("Prenume:" + detaliiUtilizator.getPrenume());
//            System.out.println("CNP:" + detaliiUtilizator.getCnp());
//            System.out.println("Email:" + detaliiUtilizator.getEmail());
//            String isActiv = detaliiUtilizator.getActiv() ? "da" : "nu";
//            System.out.println("Este activ:" + isActiv);
//        }
//    }
//
//    @Test
//    public void Test21GetUtilizator() throws Exception {
//        Utilizator detaliiUtilizator = adminService.getUtilizator(1007L);
//        System.out.println("== Utilizator ID ==");
//        System.out.println("==   ID utilizator ====" + detaliiUtilizator.getId());
//        System.out.println("Nume:" + detaliiUtilizator.getNume());
//        System.out.println("Prenume:" + detaliiUtilizator.getPrenume());
//        System.out.println("CNP:" + detaliiUtilizator.getCnp());
//        System.out.println("Email:" + detaliiUtilizator.getEmail());
//        String isActiv = detaliiUtilizator.getActiv() ? "da" : "nu";
//        System.out.println("Este activ:" + isActiv);
//    }
//
//    @Test
//    public void Test11SteregeRol() throws Exception {
////        PagingInfo pagingInfo = new PagingInfo(0, 1000);
////        RoluriSearchFilter filter = new RoluriSearchFilter();
////        SortInfo sortInfo = new SortInfo();
////        RoluriListResult listaRoluri = adminService.getListaRoluri(filter, pagingInfo, sortInfo);
////        System.out.println("== Lista roluri pana la stergere ==");
////        List<Rol> listRols = listaRoluri.getItems();
//        Test10GetListaRoluri();
////        for (Rol rol : listRols) {
////            System.out.println("Cod rol: " + rol.getCod() + " Denumire rol: " + rol.getDenumire());
////        }
//        adminService.stergeRol(1l);
//        Test10GetListaRoluri();
////        listaRoluri = adminService.getListaRoluri(filter, pagingInfo, sortInfo);
////        System.out.println("== Lista roluri dupa stergere ==");
////        listRols = listaRoluri.getItems();
////        for (Rol rol : listRols) {
////            System.out.println("Cod rol: " + rol.getCod() + " Denumire rol: " + rol.getDenumire());
////        }
//    }
//
//    @Test
//    public void Test12GetRol() throws Exception {
//        Rol rol = adminService.getRol(rolId);
//        System.out.println("==  Rol Id=" + rolId + " detalii ==");
//        System.out.println("Cod: " + rol.getCod());
//        System.out.println("Denumire: " + rol.getDenumire());
//        System.out.println("Descriere: " + rol.getDescriere());
//        System.out.println("Componenta: " + rol.getComponenta());
//        System.out.println("Context: " + rol.getContext());
//        String isActive = rol.getActiv() ? "da" : "nu";
//        System.out.println("Este activ: " + isActive);
//    }
//
//    @Test
//    public void Test13GetRolByCode() throws Exception {
//        Rol rol = adminService.getRolByCode(rolCode);
//        System.out.println("==  Rol " + rolCode + " detalii ==");
//        System.out.println("Id: " + rol.getId());
//        System.out.println("Cod: " + rol.getCod());
//        System.out.println("Denumire: " + rol.getDenumire());
//        System.out.println("Descriere: " + rol.getDescriere());
//        System.out.println("Componenta: " + rol.getComponenta());
//        System.out.println("Context: " + rol.getContext());
//        String isActive = rol.getActiv() ? "da" : "nu";
//        System.out.println("Este activ: " + isActive);
//    }
//
//    @Test
//    public void Test14GetRoluriUtilizator() throws Exception {
//        List<RolUtilizator> roles = adminService.getRoluriUtilizatorByIdUtilizator(1004L);
//
//        System.out.println("== Roluri utilizator Id=1004 ==");
//        for (RolUtilizator rol : roles) {
//            rolCode = rol.getRol().getCod();
//            Test13GetRolByCode();
//            //System.out.println("Cod rol:"+rol.getRol().getCod()+" Denumire rol:"+rol.getRol().getDenumire());
//        }
//    }
//
//    @Test
//    public void Test15SalveazaRol() throws Exception {
//        rolCode = "GOSPODAR";
//        System.out.println("===== anterior modificarii");
//        Test13GetRolByCode();
//        Rol rol = adminService.getRolByCode(rolCode);
//        rol.setDescriere("Test modificare descriere");
//        adminService.salveazaRol(rol);
//        System.out.println("===== modificare rol");
//        Test13GetRolByCode();
//
//
//    }
//
//    @Test
//    @Transactional
//    public void Test16RevocaRoluri() throws Exception {
//        List<Long> revokeRol = new ArrayList<Long>() {{
//            add(1036l);
//        }};
//        adminService.revocaRoluriUtilizator(1004L, revokeRol);
//    }
//
//    @Test
//    @Transactional
//    public void Test17AssignDoubleRol() throws Exception {
//        RolUtilizator rol = new RolUtilizator();
//
//        rol.setRol(adminService.getRolByCode("ADM-GOSP"));
//        List<Long> revokeRol = new ArrayList<Long>() {{
//            add(1029l);
//        }};
//        adminService.asigneazaRolUtilizator(1004L, rol);
//        assertFalse("Dublare Rol", true);
//    }
//
//    @Before
//    public void execBefore() throws Exception{
//        DataSource source = dataSourceConfig.portalDataSource();
//        Connection conn= source.getConnection();
//        conn.setAutoCommit(false);
//        System.out.println("Autocommit " +source.getConnection().getAutoCommit());
//    }

    @Test
    @Transactional
     public void Test00Scenariu() throws Exception {


        PagingInfo pagingInfo = new PagingInfo(0, 5);
        SortInfo sortInfo = new SortInfo();
        RoluriSearchFilter filter = new RoluriSearchFilter();
        UtilizatoriSearchFilter userFilter = new UtilizatoriSearchFilter();
        List<Long> idRolsDeleted = new ArrayList<>();

        Utilizator dublare = (Utilizator) SerializationUtils.clone(adminService.getUtilizator(adminService.getListaUtilizatori(userFilter, pagingInfo, sortInfo).getItems().get(0).getId()));
        dublare.setId(null);
        //incerc dublarea utilizatorului
        System.out.println(dublare.getNumeUtilizator());
        try {
            dublare = adminService.salveazaUtilizator(dublare);
            fail("Dublare utilizator nume " + dublare.getNumeUtilizator());
        } catch (Exception ex) {
            assertNull(dublare.getId());
        }

        Utilizator toSave = new Utilizator();
        toSave.setCnp("1650202110251");
        toSave.setEmail("test@scen.com");
        toSave.setNume("TestNume");
        toSave.setPrenume("TestPrenume");
        toSave.setNumeUtilizator("test@scen.com");
        toSave.setObservatii("Testare creare utilizator");
        //verificare creare utilizator


        toSave = adminService.salveazaUtilizator(toSave);
        System.out.println("==ID == " + toSave.getId());
        assertNotNull("Utilizator creat Id not bull", toSave.getId());
//
//        //TO DO adminService.verificaUtilizator - face commit nu am gasit
//        assertEquals("EXISTA_RAN", adminService.verificaUtilizator("test@scen.com").getRezolutie().name());

        //vrificare coincidenta
        assertEquals("Verificare utilizator creat cu cel stocat", toSave, adminService.getUtilizator(toSave.getId()));

        //dublu cnp gospodar
        //toSave = adminService.salveazaGospodar(toSave);


        RoluriListResult listaRoluri = adminService.getListaRoluri(filter, pagingInfo, sortInfo);
        List<Rol> listRols = listaRoluri.getItems();

        for (Rol rol : listRols) {
            RolUtilizator assignRol = new RolUtilizator();
            assignRol.setRol(rol);
            adminService.asigneazaRolUtilizator(toSave.getId(), assignRol);
        }

        List<RolUtilizator> roluriAsignate = adminService.getRoluriUtilizatorByIdUtilizator(toSave.getId());
        assertEquals("Numarul de roluri asignate coincide cu cel initial", listRols.size(), roluriAsignate.size());

        for (RolUtilizator rolAsignat : roluriAsignate) {
            assertTrue("Verificare corectitudine asignare", listRols.contains(rolAsignat.getRol()));
        }
        //


        //stergem rol
        RolUtilizator rolDeleted = roluriAsignate.get(0);
        idRolsDeleted.add(rolDeleted.getId());
        adminService.revocaRoluriUtilizator(toSave.getId(), idRolsDeleted);
        assertFalse("Nu a fost sters rolul " + rolDeleted.getRol().getCod(), adminService.getRoluriUtilizatorByIdUtilizator(toSave.getId()).contains(rolDeleted));




    }

    private final long rolId = 1L;
    private static String rolCode = "GOSPODAR";
    private long rolUtilizator;

}
