package ro.uti.ran.core.service.idm.openam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
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
public class IdmServiceOpenAM12RestApiImpl extends IdmServiceOpenAMRestApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdmServiceOpenAM12RestApiImpl.class);

    @Override
    public SessionInfo getSessionInfo(String token) throws IdmIntegrationException {
        String url = env.getProperty("openam12.session-info").replace("{:token}", token);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);


        LOGGER.debug("Executing get session info for token {} on url {}", token, url);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        LOGGER.debug("Result session info {}", response);

        String responseBody = response.getBody();

        if (RestUtils.isError(response.getStatusCode())) {
            ErrorResource errorResource = null;
            try {
                errorResource = objectMapper.readValue(responseBody, ErrorResource.class);
            } catch (Throwable th) {
                throw new IdmIntegrationException("Eroare la parsare response", th);
            }

            LOGGER.error("Eroare la schimbare parola {}", errorResource);

            throw new IdmIntegrationException("Eroare preluare info sesiune : " + errorResource);

        } else {
            SessionInfoResource sessionInfoResource = null;
            try {
                sessionInfoResource = objectMapper.readValue(responseBody, SessionInfoResource.class);
            } catch (Throwable th) {
                throw new IdmIntegrationException("Eroare la parsare response session info", th);
            }

            LOGGER.debug("Sessin info response {}", sessionInfoResource);


            //
            // Rezultat
            //
            SessionInfo result = new SessionInfo();

            result.setToken(token);
            result.setUsername(sessionInfoResource.getUid());
            result.setRealm(sessionInfoResource.getRealm());
            result.setValid(sessionInfoResource.isValid());

            return result;
        }
    }

    @Override
    protected ServerInfo _getServerInfo() throws IdmIntegrationException {
        try {
            String openAmInfoUrl = env.getProperty("openam12.server-info");
            LOGGER.debug("Preluare informatii server OpenAM, url : {}", openAmInfoUrl);
            ServerInfo serverInfo = new RestTemplate().getForObject(openAmInfoUrl, ServerInfo.class);

            LOGGER.debug("OpenAM server info:{}", serverInfo);
            return serverInfo;
        } catch (Throwable th) {
            LOGGER.debug("Eroare la preluare informatii server openam12", th);
            return null;
        }
    }


}
