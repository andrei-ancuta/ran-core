package ro.uti.ran.core.service.idm;

import ro.uti.ran.core.service.idm.exception.IdmIntegrationException;
import ro.uti.ran.core.service.idm.openam.ServerInfo;

import java.util.List;

/**
 * Serviciu IDM
 * <p/>
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-06 19:54
 */
public interface IdmService {

    /**
     * Autentificare utilizator dupa username si parola
     *
     * @param username
     * @param password
     * @return
     */
    LoginResult login(String username, String password) throws IdmIntegrationException;


    /**
     * Autentificare utilizator dupa certificat
     *
     * @param pemCertificate
     * @return
     */
    LoginResult login(String pemCertificate) throws IdmIntegrationException;

    /**
     * @param loginResult
     * @return
     */
    List<CookieInfo> buildSessionCookies(LoginResult loginResult) throws IdmIntegrationException;


    /**
     * Date sesiune
     *
     * @param token
     * @return
     * @throws IdmIntegrationException
     */
    SessionInfo getSessionInfo(String token) throws IdmIntegrationException;


    ServerInfo getServerInfo() throws IdmIntegrationException;

    /**
     * Creare identitate
     *
     * @param identity
     * @throws Exception
     */
    CreateIdentityResult createIdentity(Identity identity) throws IdmIntegrationException;


    Identity getIdentity(String username) throws IdmIntegrationException;

    /**
     * Setare parametru sesiune
     *
     * @param token
     * @param key
     * @param value
     * @throws Exception
     */
    void setSessionAttribute(String token, String key, String value) throws IdmIntegrationException;


    /**
     * @param token
     * @param key
     * @return
     * @throws Exception
     */
    String getSessionAttribute(String token, String key) throws IdmIntegrationException;


    /**
     * Schimbare parola utilizator
     *
     * @param username
     * @param newPassword
     * @return
     */
    ChangePasswordResult changeUserPassword(String username, String newPassword) throws IdmIntegrationException;


    /**
     * Resetare parola utilizator
     *
     * @param username
     * @param newPassword
     * @return
     */
    ChangePasswordResult resetUserPassword(String username, String newPassword) throws IdmIntegrationException;
    
    /**
     * Resetare parola utilizator de catre utlizator
     *
     * @param username
     * @param newPassword
     * @return
     */
    ChangePasswordResult resetMyPassword(String username, String newPassword) throws IdmIntegrationException;

    /**
     * Schimbare parola utilizator curent
     *
     * @param token
     * @param currentPassword
     * @param newPassword
     * @return
     * @throws IdmIntegrationException
     */
    ChangePasswordResult changeMyPassword(String token, String currentPassword, String newPassword) throws IdmIntegrationException;


    /**
     * @param token
     */
    LogoutResult logout(String token) throws IdmIntegrationException;


    /**
     * Adaugare user la grupuri
     * @param username
     */
	void addUserToGroups(String username);
	
}