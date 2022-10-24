///To change Junit
package ro.uti.ran.core.ws.sesiune;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ro.uti.ran.core.service.idm.LoginResult;
import ro.uti.ran.core.ws.internal.sesiune.Sesiune;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by miroslav.rusnac on 28/01/2016.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
public class MultSesiuneTest {
    static Service sesWs;
    static Sesiune sesiuneService;
    static String token;
    static int i=0;

    String pemCertificate= "-----BEGIN CERTIFICATE-----\n" +
            "MIIC3jCCAkegAwIBAgIJAPjvtkovi5mhMA0GCSqGSIb3DQEBCwUAMIGHMQswCQYD\n" +
            "VQQGEwJSTzEKMAgGA1UECAwBQjESMBAGA1UEBwwJQnVjaGFyZXN0MQwwCgYDVQQK\n" +
            "DANVVEkxDDAKBgNVBAsMA1VUSTEaMBgGA1UEAwwRcmFuLWFkbWluQGhvc3Qucm8x\n" +
            "IDAeBgkqhkiG9w0BCQEWEXJhbi1hZG1pbkBob3N0LnJvMB4XDTE2MDEwNzE0NDY0\n" +
            "MVoXDTE3MDEwNjE0NDY0MVowgYcxCzAJBgNVBAYTAlJPMQowCAYDVQQIDAFCMRIw\n" +
            "EAYDVQQHDAlCdWNoYXJlc3QxDDAKBgNVBAoMA1VUSTEMMAoGA1UECwwDVVRJMRow\n" +
            "GAYDVQQDDBFyYW4tYWRtaW5AaG9zdC5ybzEgMB4GCSqGSIb3DQEJARYRcmFuLWFk\n" +
            "bWluQGhvc3Qucm8wgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBANILe7YCskD5\n" +
            "JWsFTHRNKT6KLFK7l0q1RzQdLEkCO7tE4a5tXlEFGmw+nf2XaygMJ1rAnGMdDleM\n" +
            "i4tzxWT+4H7TJCHSIpazI1fWurIbpVPpHsAcEUUvcMLFa2iqssPWW5588SJ/BNff\n" +
            "VH3D9l7SwEdGA1/Sz8eaaqyoZ8bzCxFpAgMBAAGjUDBOMB0GA1UdDgQWBBRdY8AK\n" +
            "/BxEiEr0DBaUUnWo87P7qDAfBgNVHSMEGDAWgBRdY8AK/BxEiEr0DBaUUnWo87P7\n" +
            "qDAMBgNVHRMEBTADAQH/MA0GCSqGSIb3DQEBCwUAA4GBAGq8HSIg84j5nmRUIJde\n" +
            "UArgYcBExgA6Kp7pHISOziJvgr5G5qBFsCRTYcVDZklsJtonlnpGQDfOWBabbhrc\n" +
            "H6r2dsrJXDV1b2eCY8dr0MDaWjzBGcJByq//b4ExL97gcWpYF+s1vJ24q5oCJs40\n" +
            "sk9eeUbXLryQuLMSpZO+EyFQ\n" +
            "-----END CERTIFICATE-----";
    @BeforeClass
    public static void setUp() throws Exception {
        sesWs= Service.create(new URL("http://localhost:8080/service/internal/Sesiune?wsdl"),
                new QName("http://internal.ws.core.ran.uti.ro","SesiuneService"));
        sesiuneService= sesWs.getPort(Sesiune.class);
        System.out.println("================= "+i);
    }

    @Test
    public void testMultiple() throws Exception {
        int threadCount = 200;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        List<Future<Void>> futures = new ArrayList<Future<Void>>();

        for (int x = 0; x < threadCount; x++) {
            Callable<Void> callable = new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    LoginResult loginResult = sesiuneService.loginWithCertificate(pemCertificate);
                    System.out.println("== " + loginResult.getToken());
                    return null;
                }
            };
            Future<Void> submit = executorService.submit(callable);
            futures.add(submit);
        }

            List<Exception> exceptions = new ArrayList<Exception>();
            for (Future<Void> future : futures) {
                try {
                    future.get();
                } catch (Exception e) {
                    exceptions.add(e);
                    e.printStackTrace(System.err);
                }
            }

            executorService.shutdown();
        }
    }
