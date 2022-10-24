package ro.uti.ran.core.ftp;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ro.uti.ran.core.config.*;
import ro.uti.ran.core.model.portal.Sistem;
import ro.uti.ran.core.repository.portal.SistemRepository;
import ro.uti.ran.core.service.security.RanUserDetailsService;

/**
 * Created by Stanciu Neculai on 26.Oct.2015.
 */

@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration(
        loader = AnnotationConfigContextLoader.class,
        classes = {
                FtpServerConfig.class,
                DevDataSourceConfig.class,
                PortalPersistenceLayerConfig.class,
                RegistruPersistenceLayerConfig.class,
                SecurityConfig.class,
                FtpRootDirPathTest.ImportConfiguration.class
        })
@ActiveProfiles(profiles = {Profiles.DEV})
public class FtpRootDirPathTest {

    @ContextConfiguration
    @ComponentScan({"ro.uti.ran.core.ftp","ro.uti.ran.core.service.security","ro.uti.ran.core.service.parametru"})
    static class ImportConfiguration {

    }

    @Autowired
    @Qualifier("templatePathFtpRootDir")
    private FtpServerRootDirStrategy serverRootDir;

    @Autowired
    private SistemRepository sistemRepository;

    @Autowired
    private RanUserDetailsService ranUserDetailsService;

    private static final String UAT_HUNEDOARA = "86810";
    @Test
    public void testAuthPath(){
        UserDetails userDetails = ranUserDetailsService.loadUserByUsername(UAT_HUNEDOARA);
        Sistem sistemUser = ranUserDetailsService.loadSistemUserByUsername(UAT_HUNEDOARA);
        Assert.assertNotNull(sistemUser);

        serverRootDir.setSistemUser(sistemUser);
        serverRootDir.setUserDetailsAuthorities(userDetails.getAuthorities());
        try {
            System.out.println(serverRootDir.getRootDirPath());
        } catch (FtpPathNotAuthorizedException e) {
            Assert.fail();
        }
    }
    @Test
    public void testPath(){
        UserDetails userDetails = ranUserDetailsService.loadUserByUsername(UAT_HUNEDOARA);
        Sistem sistemUser = ranUserDetailsService.loadSistemUserByUsername(UAT_HUNEDOARA);
        Assert.assertNotNull(sistemUser);
        serverRootDir.setSistemUser(sistemUser);
        serverRootDir.setUserDetailsAuthorities(userDetails.getAuthorities());
        try {
            String rootPath = serverRootDir.getRootDirPath();
            System.out.println(rootPath);
        } catch (FtpPathNotAuthorizedException e) {
            System.out.println(e.getMessage());
            Assert.fail();
        }
    }

}
