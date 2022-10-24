package ro.uti.ran.core.service.idm.openam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iplanet.sso.SSOException;
import com.iplanet.sso.SSOToken;
import com.iplanet.sso.SSOTokenManager;
import com.sun.identity.idm.*;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import ro.uti.ran.core.audit.Audit;
import ro.uti.ran.core.audit.AuditOpType;
//import ro.uti.ran.core.model.portal.Parametru;
import ro.uti.ran.core.model.portal.Utilizator;
//import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.repository.portal.RolUtilizatorRepository;
import ro.uti.ran.core.repository.portal.UtilizatorRepository;
import ro.uti.ran.core.service.idm.*;
import ro.uti.ran.core.service.idm.exception.AuthenticationException;
import ro.uti.ran.core.service.idm.exception.ChangePasswordException;
import ro.uti.ran.core.service.idm.exception.CreateIdentityException;
import ro.uti.ran.core.service.idm.exception.IdmIntegrationException;
import ro.uti.ran.core.service.sistem.MailService;
import ro.uti.ran.core.service.sistem.MessageBundleService;
import ro.uti.ran.core.utils.CnpValidator;
import ro.uti.ran.core.ws.internal.parametru.ParametruService;

//import java.text.MessageFormat;
import java.util.*;

import static ro.uti.ran.core.audit.AuditOpType.SCHIMBARE_PAROLA;
import static ro.uti.ran.core.audit.AuditOpType.SCHIMBARE_PAROLA_DE_CATRE_UTILIZATOR;
import static ro.uti.ran.core.audit.AuditOpType.RESETEAZA_PAROLA_DE_CATRE_UTILIZATOR;
import static ro.uti.ran.core.audit.AuditOpType.RESETEAZA_PAROLA;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-08 16:00
 */
public abstract class IdmServiceOpenAMRestApi implements IdmService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdmServiceOpenAMRestApi.class);
	
    @Autowired
    protected Environment env;

    @Autowired
    protected RestTemplate restTemplate;

    @Autowired
    protected ObjectMapper objectMapper;

    private ServerInfo serverInfo;

    @Autowired
    protected SSOTokenManager ssoTokenManager;

    @Autowired
    protected MailService mailService;

    @Autowired
    protected MessageBundleService messageBundleService;

    @Autowired
    protected UtilizatorRepository utilizatorRepository;

    @Autowired
    protected RolUtilizatorRepository rolUtilizatorRepository;

    @Autowired
    private ParametruService parametruService;
    
    @Autowired
    private IdmServiceOpenAMRestGroupManagementApi idmServiceOpenAMRestGroupManagementApi;

    @Override
    public LoginResult login(String pemCertificate) throws IdmIntegrationException {
        pemCertificate = pemCertificate.replaceAll("\n", "");
        Map<String, String> _headers = new HashMap<String, String>();
        _headers.put("X-Client-Cert", pemCertificate);
        return _login(_headers);
    }

    @Audit(opType = AuditOpType.LOGIN_USERNAME_PASSWORD)
    @Override
    public LoginResult login(String username, String password) throws IdmIntegrationException {

        Map<String, String> _headers = new HashMap<String, String>();
        _headers.put("X-OpenAM-Username", username);
        _headers.put("X-OpenAM-Password", password);
        LoginResult loginResult = _login(_headers);
        if (loginResult.isSuccess() && !env.getProperty("openam.admin.user").equals(username)) {
            updateUserInfo(username);
        }
        return loginResult;
    }

    /**
     * https://backstage.forgerock.com/#!/docs/openam/12.0.0/dev-guide/chap-rest#rest-api-auth
     *
     * @return
     * @throws Exception
     */
    protected LoginResult _login(Map<String, String> _headers) throws IdmIntegrationException {

        String url = env.getProperty("openam.login-url");
        String realm = env.getProperty("openam.realm");

        if (realm != null && realm.trim().length() > 0) {
            url += "?realm=" + realm;
        }

        LOGGER.debug("Executing login operation with credentials on url {}", url);

        HttpHeaders headers = new HttpHeaders();
        for (String header : _headers.keySet()) {
            LOGGER.debug(header + ": " + _headers.get(header));
            headers.add(header, _headers.get(header));
        }

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        ResponseEntity<String> response = null;
        String responseBody = null;
        try {
            response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            responseBody = response.getBody();
        } catch (HttpClientErrorException e) {
            responseBody = e.getResponseBodyAsString();
        } catch (Throwable th) {
            throw new AuthenticationException("Eroare la autentificare " + th.getMessage(), th);
        }

        LOGGER.debug("Rezultat login {}", response);

        if (response == null || RestUtils.isError(response.getStatusCode())) {
            ErrorResource errorResource = null;
            try {
                errorResource = objectMapper.readValue(responseBody, ErrorResource.class);
            } catch (Throwable th) {
                throw new IdmIntegrationException("Eroare la parsare raspuns", th);
            }

            LOGGER.error("Eroare autentificare {}", errorResource);

            //throw new AuthenticationException("Eroare autentificare, motiv: " + errorResource.getReason() + ", mesaj: " + errorResource.getMessage());

            LoginResult result = new LoginResult();
            result.setSuccess(false);
            result.setToken(null);
            result.setReason(errorResource.getReason());
            result.setMessage(errorResource.getMessage());

            return result;

        } else {

            //
            // Open AM Login Response
            //
            LoginResource loginResource = null;
            try {
                loginResource = objectMapper.readValue(responseBody, LoginResource.class);
            } catch (Throwable th) {
                throw new IdmIntegrationException("Eroare la parsare raspuns", th);
            }

            LOGGER.debug("Rezultat autentificare {}", loginResource);


            //
            // Construct LoginResult
            //
            LoginResult result = new LoginResult();
            if (StringUtils.isNotEmpty(loginResource.getTokenId())) {
                result.setSuccess(true);
                result.setToken(loginResource.getTokenId());
                result.setMessage(loginResource.getMessage());
            } else {
                result.setSuccess(false);
                result.setToken(null);
                result.setReason(loginResource.getReason());
                result.setMessage(
                        StringUtils.isNotEmpty(loginResource.getMessage()) ? loginResource.getMessage() : loginResource.getErrorMessage()
                );
            }
            return result;
        }
    }

    protected void updateUserInfo(String username) {
        if (username != null && !username.isEmpty()) {
            Identity identity = getIdentity(username);
            Utilizator utilizator = utilizatorRepository.findByNumeUtilizatorIgnoreCase(username);
            if (identity == null || utilizator == null) {
                return;
            }
            // Daca se modifica nume + prenume in OpenAM acestea trebuie actualizate si in portal la urmatoarea autentificare a utilizatorului.
            String prenumeOpenAm = null;
            String prenumeBaza = null;
            if (identity.getFirstName() != null) {
                prenumeOpenAm = identity.getFirstName().trim();
            }
            if (utilizator.getPrenume() != null) {
                prenumeBaza = utilizator.getPrenume().trim();
            }
            if (prenumeOpenAm != null && !prenumeOpenAm.equals(prenumeBaza)) {
                utilizator.setPrenume(prenumeOpenAm);
            }
            String numeOpenAm = null;
            String numeBaza = null;
            if (identity.getLastName() != null) {
                numeOpenAm = identity.getLastName().trim();
            }
            if (utilizator.getNume() != null) {
                numeBaza = utilizator.getNume().trim();
            }
            if (prenumeOpenAm != null && !numeOpenAm.equals(numeBaza)) {
                utilizator.setNume(numeOpenAm);
            }

            utilizatorRepository.save(utilizator);
        }
    }

    @Override
    public List<CookieInfo> buildSessionCookies(LoginResult loginResult) throws IdmIntegrationException {

        String cookieName = getServerInfo().getCookieName();

        CookieInfo cookie = new CookieInfo(cookieName, loginResult.getToken());
        cookie.setDomain(getServerInfo().getDomains().get(0));
        cookie.setPath("/");

        return Arrays.asList(cookie);
    }


    @Override
    public CreateIdentityResult createIdentity(Identity identity) throws IdmIntegrationException {

        LoginResult loginResult = login(
                env.getProperty("openam.admin.user"),
                env.getProperty("openam.admin.password")
        );

        if (!loginResult.isSuccess()) {
            throw new IllegalStateException("Parametri configurare open am incorecti, nu s-a reusit autentificare cu admin ");
        }

        try {

            String url = env.getProperty("openam.create-identity");

            HttpHeaders headers = new HttpHeaders();
            headers.add(getServerInfo().getCookieName(), loginResult.getToken());
            headers.setContentType(MediaType.APPLICATION_JSON);

            String data = "{" +
                    "\"username\": \"" + identity.getUsername() + "\"," +
                    "\"userpassword\": \"" + identity.getUserpassword() + "\"," +
                    "\"mail\": \"" + identity.getMail() + "\"," +
                    "\"cn\": \"" + identity.getFirstName() + "\"," +
                    "\"sn\": \"" + identity.getLastName() + "\"" +
                    "}";


            HttpEntity<String> entity = new HttpEntity<String>(data, headers);


            LOGGER.debug("Executing create identity {} on url {}", identity, url);

            ResponseEntity<String> response = null;
            String responseBody = null;
            try {
                response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                responseBody = response.getBody();
            } catch (HttpClientErrorException e) {
                responseBody = e.getResponseBodyAsString();
            } catch (Throwable th) {
                throw new AuthenticationException("Eroare la apel REST end point pentru autentificare, " + th.getMessage(), th);
            }

            LOGGER.debug("Rezultat creare identity {}", response);

            if (response == null || RestUtils.isError(response.getStatusCode())) {
                ErrorResource errorResource = null;
                try {
                    errorResource = objectMapper.readValue(responseBody, ErrorResource.class);
                } catch (Throwable th) {
                    throw new IdmIntegrationException("Eroare la parsare response", th);
                }

                LOGGER.error("Eroare la creare identity {}", errorResource);

                //throw new CreateIdentityException("Eroare la creare identity : " + errorResource);

                CreateIdentityResult result = new CreateIdentityResult();
                result.setSuccess(false);
                result.setMessage(errorResource.getMessage());
                return result;

            } else {
                try {
                    CreateIdentityResponse createIdentityResponse = null;
                    try {
                        createIdentityResponse = objectMapper.readValue(responseBody, CreateIdentityResponse.class);
                    } catch (Throwable th) {
                        throw new IdmIntegrationException("Eroare la parsare response operatie autentificare", th);
                    }

                    LOGGER.debug("Create response {}", createIdentityResponse);

                    idmServiceOpenAMRestGroupManagementApi.addUserToParametruGroups(identity.getUsername(), headers, true);
                    
                    //
                    // Rezultat
                    //
                    CreateIdentityResult result = new CreateIdentityResult();
                    result.setSuccess(true);
                    result.setMessage("Success");
                    return result;
                }catch (Exception e){
                    CreateIdentityResult result = new CreateIdentityResult();
                    result.setSuccess(false);
                    result.setMessage(e.getMessage());
                    return result;
                }
            }

        } finally {
            logout(loginResult.getToken());
        }
    }
    
    @Override
    public void addUserToGroups(String username) {
    	 LoginResult loginResult = login(
                 env.getProperty("openam.admin.user"),
                 env.getProperty("openam.admin.password")
         );

         if (!loginResult.isSuccess()) {
             throw new IllegalStateException("Parametri configurare open am incorecti, nu s-a reusit autentificare cu admin ");
         }
         
         HttpHeaders headers = new HttpHeaders();
         headers.add(getServerInfo().getCookieName(), loginResult.getToken());
         headers.setContentType(MediaType.APPLICATION_JSON);
         
         try {
        	 idmServiceOpenAMRestGroupManagementApi.addUserToParametruGroups(username, headers, false); 
         } finally {
        	 logout(loginResult.getToken());
         }
    }

    @Override
    public Identity getIdentity(String username) throws IdmIntegrationException {
        String realm = env.getProperty("openam.realm");

        LoginResult loginResult = login(
                env.getProperty("openam.admin.user"),
                env.getProperty("openam.admin.password")
        );

        if (!loginResult.isSuccess()) {
            throw new IllegalStateException("Parametri configurare open am incorecti, nu s-a reusit autentificare cu admin ");
        }

        try {

            SSOToken token = ssoTokenManager.createSSOToken(loginResult.getToken());

            AMIdentityRepository idRepo = new AMIdentityRepository(token, realm);
            IdSearchResults results = idRepo.searchIdentities(IdType.USER, username, new IdSearchControl());
            Set identities = results.getSearchResults();

            if (identities.size() == 0) {
                return null;
            }


            AMIdentity identity = (AMIdentity) identities.iterator().next();

            System.out.println("identity = " + identity);


            Identity result = new Identity();
            result.setUsername(identity.getName());
            result.setMail(getIdentityAttribute(identity, env.getProperty("ldap.attribute.mail")));
            result.setFirstName(getIdentityAttribute(identity, env.getProperty("ldap.attribute.prenume")));
            result.setLastName(getIdentityAttribute(identity, env.getProperty("ldap.attribute.nume")));
            result.setActive(env.getProperty("ldap.attribute.stare.value.activ").equals(getIdentityAttribute(identity, env.getProperty("ldap.attribute.stare"))));
            String cnpOrNif = getIdentityAttribute(identity,env.getProperty("ldap.attribute.cnpOrNif"));
            if(cnpOrNif == null){
                return result;
            }
            if(CnpValidator.isValid(cnpOrNif)){
            	result.setCnp(cnpOrNif);
            } else {
            	result.setNif(cnpOrNif);
            }
            return result;

        } catch (Throwable th) {
            throw new IdmIntegrationException("Eroare la operatia getIdenty", th);
        } finally {
            logout(loginResult.getToken());
        }
    }

    protected String getIdentityAttribute(AMIdentity identity, String attributeName) throws IdRepoException, SSOException {
        Set set = identity.getAttribute(attributeName);
        if (set != null && set.size() > 0) {
            return set.iterator().next().toString();
        }
        return null;
    }

    @Override
    public void setSessionAttribute(String tokenId, String key, String value) throws IdmIntegrationException {
        try {

            SSOToken token = ssoTokenManager.createSSOToken(tokenId);

            token.setProperty(key, value);

        } catch (Throwable th) {
            throw new IdmIntegrationException("Eroare la adaugare parametru in sesiune OpenAM ", th);
        }
    }

    @Override
    public String getSessionAttribute(String tokenId, String key) throws IdmIntegrationException {
        try {

            SSOToken token = ssoTokenManager.createSSOToken(tokenId);

            return token.getProperty(key);

        } catch (Throwable th) {
            throw new IdmIntegrationException("Eroare la preluare parametru sesiune OpenAM ", th);
        }
    }
    
    @Override
    @Audit(opType = RESETEAZA_PAROLA_DE_CATRE_UTILIZATOR)
    public ChangePasswordResult resetMyPassword(String username, String newPassword) throws IdmIntegrationException {
    	return changeUserPassword(username, newPassword);
    }
    
    @Override
    @Audit(opType = RESETEAZA_PAROLA)
    public ChangePasswordResult resetUserPassword(String username, String newPassword) throws IdmIntegrationException {
    	return changeUserPassword(username, newPassword);
    }

    @Override
    @Audit(opType = SCHIMBARE_PAROLA)
    public ChangePasswordResult changeUserPassword(String username, String newPassword) throws IdmIntegrationException {
        LoginResult loginResult = login(
                env.getProperty("openam.admin.user"),
                env.getProperty("openam.admin.password")
        );

        if (!loginResult.isSuccess()) {
            throw new IllegalStateException("Nu s-a reusit autentificare la open am cu utilizator admin");
        }

        try {
            // todo: shimbare parola
            //return _changePasswordRestAPI(username, loginResult.getToken(), "todo", newPassword);
            return _changePasswordClientApi(username, loginResult.getToken(), null, newPassword);
//            return null;
        } finally {
            logout(loginResult.getToken());
        }
    }

    @Audit(opType = SCHIMBARE_PAROLA_DE_CATRE_UTILIZATOR)
    @Override
    public ChangePasswordResult changeMyPassword(String token, String currentPassword, String newPassword) throws IdmIntegrationException {
        SessionInfo sessionInfo = getSessionInfo(token);
        if (!sessionInfo.isValid()) {
            throw new ChangePasswordException("Invalid token or session expired");
        }

        return _changePasswordClientApi(sessionInfo.getUsername(), token, currentPassword, newPassword);
    }

    protected ChangePasswordResult _changePasswordClientApi(String username, String token, String oldPassword, String newPassword) {
        try {
            String userStatusLdapAttr = env.getProperty("ldap.attribute.stare");
            String userStatusActiveValue = env.getProperty("ldap.attribute.stare.value.activ");
            SSOToken ssoToken = SSOTokenManager.getInstance().createSSOToken(token);
            AMIdentityRepository idRepo = new AMIdentityRepository(ssoToken, "/");
            IdSearchResults results = idRepo.searchIdentities(IdType.USER, username, new IdSearchControl());
            Set identities = results.getSearchResults();
            if (!identities.isEmpty()) {
                AMIdentity identity = (AMIdentity) identities.iterator().next();
                Map attrs = new HashMap();
                Set values = new HashSet(1);
                Set userStatusActiveValueSet = new HashSet(1);
                values.add(newPassword);
                attrs.put("userPassword", values);

                userStatusActiveValueSet.add(userStatusActiveValue);
                if(userStatusLdapAttr != null && userStatusActiveValue != null) {
                    attrs.put(userStatusLdapAttr,userStatusActiveValueSet);
                }
                identity.setAttributes(attrs);
                
                if(oldPassword != null) {
                	identity.changePassword(oldPassword, newPassword);
                } else {
                	 identity.store();
                }
                
            }
            ChangePasswordResult rez = new ChangePasswordResult();
            rez.setSuccess(Boolean.TRUE);
            return rez;
        } catch (Exception e) {
        	e.printStackTrace();
            ChangePasswordResult rez = new ChangePasswordResult();
            rez.setSuccess(Boolean.FALSE);
            rez.setMessage(e.getMessage());
            return rez;
        }
    }

    protected ChangePasswordResult _changePasswordRestAPI(String username, String token, String oldPassword, String newPassword) {
        String url = env.getProperty("openam.change-password").replace("{:username}", username);

        HttpHeaders headers = new HttpHeaders();
        headers.add(getServerInfo().getCookieName(), token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String data = "{" +
                "\"currentpassword\": \"" + oldPassword + "\"," +
                "\"userpassword\": \"" + newPassword + "\"" +
                "}";


        HttpEntity<String> entity = new HttpEntity<String>(data, headers);


        LOGGER.debug("Executing change password for {} on url {}, oldpassword:{}, new password:{}", username, url, oldPassword, newPassword);

        ResponseEntity<String> response = null;
        String responseBody = null;
        try {
            response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            responseBody = response.getBody();
        } catch (HttpClientErrorException e) {
            responseBody = e.getResponseBodyAsString();
        } catch (Throwable th) {
            throw new AuthenticationException("Eroare la autentificare " + th.getMessage(), th);
        }

        LOGGER.debug("Rezultat schimbare parola {}", response);

        if (response == null || RestUtils.isError(response.getStatusCode())) {
            ErrorResource errorResource = null;
            try {
                errorResource = objectMapper.readValue(responseBody, ErrorResource.class);
            } catch (Throwable th) {
                throw new IdmIntegrationException("Eroare la parsare response", th);
            }

            LOGGER.error("Eroare la schimbare parola {}", errorResource);

            //throw new ChangePasswordException("Eroare schimbare parola : " + errorResource);

            ChangePasswordResult result = new ChangePasswordResult();
            result.setSuccess(false);
            result.setMessage(errorResource.getMessage());

            return result;

        } else {
            ChangePasswordResource changePasswordResource = null;
            try {
                changePasswordResource = objectMapper.readValue(responseBody, ChangePasswordResource.class);
            } catch (Throwable th) {
                throw new IdmIntegrationException("Eroare la parsare response la schimbare parola", th);
            }

            LOGGER.debug("Change password response {}", changePasswordResource);


            //
            // Rezultat
            //
            ChangePasswordResult result = new ChangePasswordResult();

            result.setSuccess(true);
            result.setMessage("Success");

            return result;
        }
    }

    protected ChangePasswordResult _changePasswordJavaAPI(String username, String oldPassword, String newPassword) {
        String realm = env.getProperty("openam.realm");

        LoginResult loginResult = login(
                env.getProperty("openam.admin.user"),
                env.getProperty("openam.admin.password")
        );

        if (!loginResult.isSuccess()) {
            throw new IllegalStateException("Parametri configurare open am incorecti, nu s-a reusit autentificare cu admin ");
        }

        try {

            SSOToken token = ssoTokenManager.createSSOToken(loginResult.getToken());

            AMIdentityRepository idRepo = new AMIdentityRepository(token, realm);
            IdSearchResults results = idRepo.searchIdentities(IdType.USER, username, new IdSearchControl());
            Set identities = results.getSearchResults();
            AMIdentity identity = (AMIdentity) identities.iterator().next();

            identity.changePassword(oldPassword, newPassword);


            ChangePasswordResult result = new ChangePasswordResult();
            result.setSuccess(true);
            result.setMessage("Success");

            return result;

        } catch (Throwable th) {
            throw new IdmIntegrationException("Eroare la operatia de schimbare parola", th);
        } finally {
            logout(loginResult.getToken());
        }
    }

    @Audit(opType = AuditOpType.LOGOUT)
    @Override
    public LogoutResult logout(String token) throws IdmIntegrationException {

        if (StringUtils.isEmpty(token)) {
            throw new IllegalStateException("Parametri token nedefinit");
        }

        String url = env.getProperty("openam.logout-url");

        HttpHeaders headers = new HttpHeaders();
        headers.add(getServerInfo().getCookieName(), token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        LOGGER.debug("Executing logout for token {} on url {}", token, url);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        LOGGER.debug("Rezultat logout {}", response);

        String responseBody = response.getBody();

        if (RestUtils.isError(response.getStatusCode())) {
            ErrorResource errorResource = null;
            try {
                errorResource = objectMapper.readValue(responseBody, ErrorResource.class);
            } catch (Throwable th) {
                throw new IdmIntegrationException("Eroare la parsare response", th);
            }

            LOGGER.error("Eroare la creare identity {}", errorResource);

            throw new CreateIdentityException("Eroare la creare identity : " + errorResource);

        } else {
            final LogoutResponse logoutResponse;
            try {
                logoutResponse = objectMapper.readValue(responseBody, LogoutResponse.class);
            } catch (Throwable th) {
                throw new IdmIntegrationException("Eroare la parsare response", th);
            }

            LOGGER.debug("Logout response {}", logoutResponse);


            //
            // Rezultat
            //
            LogoutResult result = new LogoutResult() {

                private static final long serialVersionUID = 1L;

                {
                    setSuccess(true);
                    setMessage(logoutResponse.getResult());
                }
            };

            return result;
        }
    }

    @Override
    public synchronized ServerInfo getServerInfo() {
        if (null == serverInfo) {
            serverInfo = _getServerInfo();
        }
        return serverInfo;
    }

    protected abstract ServerInfo _getServerInfo() throws IdmIntegrationException;
  

}
