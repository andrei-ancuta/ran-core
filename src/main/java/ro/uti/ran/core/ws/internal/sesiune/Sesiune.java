package ro.uti.ran.core.ws.internal.sesiune;

import ro.uti.ran.core.service.idm.*;
import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

/**
 * Serviciu de lucru cu IDM (OpenAM)
 * <p/>
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-15 15:40
 */
@WebService
public interface Sesiune {

    /**
     * Autentificare certificat
     *
     * @param certificate
     * @return
     * @throws RanException
     * @throws RanRuntimeException
     */
    @WebMethod
    LoginResult loginWithCertificate(
            @WebParam(name = "certificate") String certificate
    ) throws RanException, RanRuntimeException;

    /**
     * Autentificare
     *
     * @param username
     * @param password
     * @return
     * @throws RanException
     * @throws RanRuntimeException
     */
    @WebMethod
    LoginResult login(
            @WebParam(name = "username") String username,
            @WebParam(name = "password") String password
    ) throws RanException, RanRuntimeException;

    @WebMethod
    List<CookieInfo> buildSessionCookies(
            @WebParam(name = "loginResult") LoginResult loginResult
    ) throws RanException, RanRuntimeException;

    /**
     * @return
     * @throws RanException
     * @throws RanRuntimeException
     */
    @WebMethod
    InfoSesiune getSessionInfo(
            @WebParam(name = "token") String token
    ) throws RanException, RanRuntimeException;


    @WebMethod
    void setSessionAttribute(
            @WebParam(name = "token") String token,
            @WebParam(name = "key") String key,
            @WebParam(name = "value") String value
    ) throws RanException, RanRuntimeException;


    /**
     * @param token
     * @param key
     * @return
     * @throws Exception
     */
    @WebMethod
    String getSessionAttribute(
            @WebParam(name = "token") String token,
            @WebParam(name = "key") String key
    ) throws RanException, RanRuntimeException;

    /**
     * Schimbare parola utilizator autentificat
     *
     * @param token
     * @param currentPassword
     * @param newPassword
     * @throws RanException
     * @throws RanRuntimeException
     */
    @WebMethod
    ChangePasswordResult changeMyPassword(
            @WebParam(name = "token") String token,
            @WebParam(name = "currentPassword") String currentPassword,
            @WebParam(name = "newPassword") String newPassword
    ) throws RanException, RanRuntimeException;


    /**
     * Resetare parola utilizator.
     *
     * @param token - BCrypt hash for username + userId
     * @return
     * @throws RanException
     * @throws RanRuntimeException
     */

    @WebMethod
    ChangePasswordResult resetMyPassword(
            @WebParam(name = "token") String token,
            @WebParam(name="newPassword") String newPassword,
            @WebParam(name = "activateUser") Boolean activateUser
    ) throws RanException, RanRuntimeException;

    @WebMethod
    void sendPasswordRecoveryMail(
            @WebParam(name = "username") String username
    )  throws RanException, RanRuntimeException;

    /**
     * @param token
     * @return
     * @throws RanException
     * @throws RanRuntimeException
     */
    @WebMethod
    LogoutResult logout(
            @WebParam(name = "token") String token
    ) throws RanException, RanRuntimeException;

    @WebMethod
    Boolean isEmailValid(@WebParam(name = "email") String email) throws RanException, RanRuntimeException;
}
