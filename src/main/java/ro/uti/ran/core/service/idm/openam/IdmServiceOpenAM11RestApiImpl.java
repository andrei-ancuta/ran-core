package ro.uti.ran.core.service.idm.openam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ro.uti.ran.core.service.idm.SessionInfo;
import ro.uti.ran.core.service.idm.exception.IdmIntegrationException;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-08 16:00
 */
@Component
@Lazy
public class IdmServiceOpenAM11RestApiImpl extends IdmServiceOpenAMRestApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdmServiceOpenAM11RestApiImpl.class);

    private static final String SERVERINFO_DEFAULTS = "{\n" +
            "  \"domains\": [\n" +
            "    \".uti.ro\"\n" +
            "  ],\n" +
            "  \"protectedUserAttributes\": [],\n" +
            "  \"cookieName\": \"iPlanetDirectoryPro\",\n" +
            "  \"secureCookie\": false,\n" +
            "  \"forgotPassword\": \"true\",\n" +
            "  \"selfRegistration\": \"false\",\n" +
            "  \"lang\": \"en\",\n" +
            "  \"successfulUserRegistrationDestination\": \"default\",\n" +
            "  \"socialImplementations\": [],\n" +
            "  \"referralsEnabled\": \"false\",\n" +
            "  \"zeroPageLogin\": {\n" +
            "    \"enabled\": false,\n" +
            "    \"refererWhitelist\": [\n" +
            "      \"\"\n" +
            "    ],\n" +
            "    \"allowedWithoutReferer\": true\n" +
            "  },\n" +
            "  \"FQDN\": \"devran.uti.ro\"\n" +
            "}";


    @Override
    public SessionInfo getSessionInfo(String token) throws IdmIntegrationException {
        try {
            String attributesUidUrl = env.getProperty("openam11.attributes-uid").replace("{:token}", token);
            LOGGER.debug("Preluare informatii server OpenAM, url : {}", attributesUidUrl);
            String response = new RestTemplate().getForObject(attributesUidUrl, String.class);
            
            String uid = null;
            for(String line : (response.indexOf("\r\n") > 0 ? response.split("\r\n") : response.split("\n"))) {
            	if(line.contains("userdetails.attribute.value")) {
            		 uid = line.replace("userdetails.attribute.value=", "");
            		 break;
            	}
            }
           
            String tokenValidUrl = env.getProperty("openam11.token-valid").replace("{:token}", token);
            LOGGER.debug("Preluare informatii server OpenAM, url : {}", tokenValidUrl);
            response = new RestTemplate().getForObject(tokenValidUrl, String.class);
            Boolean isTokenValid = Boolean.valueOf(response.trim().replace("boolean=", ""));

            String realm = env.getProperty("openam.realm");

            SessionInfo sessionInfo = new SessionInfo();
            sessionInfo.setUsername(uid);
            sessionInfo.setToken(token);
            sessionInfo.setValid(isTokenValid);
            sessionInfo.setRealm(realm);

            return sessionInfo;
        } catch (Throwable th) {
            LOGGER.debug("Eroare la preluare informatii server openam11", th);
            throw new IdmIntegrationException("Eroare la preluare informatii server openam11", th);
        }
    }

    @Override
    protected ServerInfo _getServerInfo() throws IdmIntegrationException {
        try {
            String cookieDomainsUrl = env.getProperty("openam11.cookie-domains");
            LOGGER.debug("Preluare informatii server OpenAM, url : {}", cookieDomainsUrl);
            ServerInfo serverInfo = new RestTemplate().getForObject(cookieDomainsUrl, ServerInfo.class);

            String cookieNameUrl = env.getProperty("openam11.cookie-name");
            LOGGER.debug("Preluare informatii server OpenAM, url : {}", cookieNameUrl);
            String cookieName = new RestTemplate().getForObject(cookieNameUrl, String.class);

            serverInfo.setCookieName(cookieName.trim().substring(cookieName.indexOf("=") + 1));

            LOGGER.debug("OpenAM server info:{}", serverInfo);
            return serverInfo;
        } catch (Throwable th) {
            LOGGER.debug("Eroare la preluare informatii server openam11", th);
            return null;
        }
    }
}
