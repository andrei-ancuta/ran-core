package ro.uti.ran.core.security;


import org.apache.xml.security.utils.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ro.uti.ran.core.config.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Stanciu Neculai on 15.Oct.2015.
 */

@ComponentScan(basePackages = "ro.uti.ran.core")
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.DEV)
@ContextConfiguration(
        loader = AnnotationConfigContextLoader.class,
        classes = {
                DevDataSourceConfig.class,
                PortalPersistenceLayerConfig.class,
                RegistruPersistenceLayerConfig.class,
                SecurityConfig.class
        }
)
public class PasswordEncoderTest {
    public static final Logger log = LoggerFactory.getLogger(PasswordEncoderTest.class);

    @Autowired
    private PasswordEncoder encoder;

    private String passwordStr = "1234"; //SHA1=b2a85d6fa6272112db3ac069746079b7bdf7cded
    private String passwordHash;

    @Before
    public void encodePassword() {
        passwordHash = encoder.encode(passwordStr);
        System.out.println("passwordHash:\n" + passwordHash);
    }

    @Test
    public void testPasswordMatch() {

        try {
            String webPage = "http://localhost:8080/rapoarte?download=test.txt";
            String name = "87665";
            String password = "12345678";

            String authString = name + ":" + password;
            System.out.println("auth string: " + authString);
            String authStringEnc = Base64.encode(authString.getBytes());
            //String authStringEnc = new String(authEncBytes);
            System.out.println("Base64 encoded auth string: " + authStringEnc);

            URL url = new URL(webPage);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
            InputStream is = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);

            int numCharsRead;
            char[] charArray = new char[1024];
            StringBuffer sb = new StringBuffer();
            while ((numCharsRead = isr.read(charArray)) > 0) {
                sb.append(charArray, 0, numCharsRead);
            }
            String result = sb.toString();

            System.out.println("*** BEGIN ***");
            System.out.println(result);
            System.out.println("*** END ***");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
