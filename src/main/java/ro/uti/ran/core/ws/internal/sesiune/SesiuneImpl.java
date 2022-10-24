package ro.uti.ran.core.ws.internal.sesiune;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ro.uti.ran.core.model.portal.Context;
import ro.uti.ran.core.model.portal.Parametru;
import ro.uti.ran.core.model.portal.Utilizator;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.repository.portal.ContextRepository;
import ro.uti.ran.core.repository.portal.SesiuneRepository;
import ro.uti.ran.core.repository.portal.UtilizatorRepository;
import ro.uti.ran.core.service.idm.*;
import ro.uti.ran.core.service.parametru.ParametruService;
import ro.uti.ran.core.service.sistem.MailService;
import ro.uti.ran.core.service.sistem.MessageBundleService;
import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.ws.utils.AuditInfo;
import ro.uti.ran.core.ws.utils.AuditInfoThreadLocal;
import ro.uti.ran.core.ws.utils.ExceptionUtil;

import javax.jws.WebService;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-15 16:40
 */
@WebService(
        serviceName = "SesiuneService",
        endpointInterface = "ro.uti.ran.core.ws.internal.sesiune.Sesiune",
        targetNamespace = "http://internal.ws.core.ran.uti.ro",
        portName = "SesiuneServicePort")
@Service("sesiuneService")
public class SesiuneImpl implements Sesiune {

    public static final String NOTIFICARE_PAROLA_UITATA_SUBJECT_KEY = "notificareCerereUitatParola.subject";
    public static final String NOTIFICARE_PAROLA_UITATA_CONTENT_KEY = "notificareCerereUitatParola.content";

    private static final Logger LOGGER = LoggerFactory.getLogger(SesiuneImpl.class);

    @Autowired
    @Qualifier(value = "idmService")
    IdmService idmService;
    @Autowired
    SesiuneRepository sesiuneRepository;
    @Autowired
    UtilizatorRepository utilizatorRepository;
    @Autowired
    ContextRepository contextRepository;
    @Autowired
    private ExceptionUtil exceptionUtil;
    @Autowired
    private MailService mailService;
    @Autowired
    private MessageBundleService messageBundleService;
    @Autowired
    private ParametruService parametruService;
    @Autowired
    private EmailUrlTokenGeneratorService emailUrlTokenGeneratorService;

    @Override
    public LoginResult loginWithCertificate(String certificate) throws RanException,
            RanRuntimeException {
        try {
            return idmService.login(certificate);
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public LoginResult login(String username, String password) throws RanException, RanRuntimeException {
        try {
            return idmService.login(username, password);
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public List<CookieInfo> buildSessionCookies(LoginResult loginResult) throws RanException, RanRuntimeException {
        try {
            return idmService.buildSessionCookies(loginResult);
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public InfoSesiune getSessionInfo(String token) throws RanException, RanRuntimeException {
        try {

            SessionInfo sessionInfo = idmService.getSessionInfo(token);

            InfoSesiune infoSesiune = new InfoSesiune();
            infoSesiune.setNumeUtilizator(sessionInfo.getUsername());
            infoSesiune.setValida(sessionInfo.isValid());

            return infoSesiune;
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public void setSessionAttribute(String token, String key, String value) throws RanException, RanRuntimeException {
        try {
            idmService.setSessionAttribute(token, key, value);

            //
            // Creare record sesiune la setare atribut X-CONTEXT
            // Daca utilizatorul contine roluri pentru mai multe contexte, se solicita selectia contextului
            // Pentru un singur context se apeleaza tothand aceasta functie de setare context curent in sesiune
            //
            if (key.equals("X-CONTEXT")) {

                AuditInfo auditInfo = AuditInfoThreadLocal.get();
                if (auditInfo == null) {
                    throw new IllegalStateException("Informatii de audit lipsa ");
                }

                InfoSesiune infoSesiune = getSessionInfo(token);
                if (!infoSesiune.isValida()) {
                    throw new IllegalStateException("Sesiune invalida, " + token);
                }

                Utilizator utilizator = utilizatorRepository.findByNumeUtilizatorIgnoreCase(infoSesiune.getNumeUtilizator());
                if (utilizator == null) {
                    throw new IllegalStateException("Nu exista utilizator in baza de date, " + infoSesiune.getNumeUtilizator());
                }

                Context context = contextRepository.findByCod(value);
                if (context == null) {
                    throw new IllegalStateException("Nu exista context in baza de date avand cod, " + value);
                }

                ro.uti.ran.core.model.portal.Sesiune sesiune = new ro.uti.ran.core.model.portal.Sesiune();
                sesiune.setUtilizator(utilizator);
                sesiune.setUidSesiuneHttp(token);
                sesiune.setAdresaIp(auditInfo.getRemoteIp());
                sesiune.setDataStart(new Date());
                sesiune.setContext(context);

                sesiuneRepository.save(sesiune);
            }

        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public String getSessionAttribute(String token, String key) throws RanException, RanRuntimeException {
        try {
            return idmService.getSessionAttribute(token, key);
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public ChangePasswordResult changeMyPassword(String token, String currentPassword, String newPassword) throws RanException, RanRuntimeException {
        try {
            return idmService.changeMyPassword(token, currentPassword, newPassword);
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public ChangePasswordResult resetMyPassword(String token, String newPassword, Boolean activateUser) throws RanException, RanRuntimeException {
        try {
            LOGGER.debug("Changing password for user {} to {}", token, newPassword);
            Utilizator utilizator = null;
            utilizator = extractUserIdFromToken(token);
            String username = null;
 
            if (utilizator != null) {
                username = utilizator.getNumeUtilizator();
                if (utilizator.getActiv().equals(Boolean.TRUE) && activateUser) {
                    ChangePasswordResult changePasswordResultError = new ChangePasswordResult();
                    changePasswordResultError.setSuccess(Boolean.FALSE);
                    changePasswordResultError.setMessage("Utilizatorul este deja activat!");
                    return changePasswordResultError;
                }
                
                if (activateUser) {
                    utilizator.setActiv(true);
                    utilizatorRepository.save(utilizator);
                }
                
            } else {
                ChangePasswordResult changePasswordResultError = new ChangePasswordResult();
                changePasswordResultError.setSuccess(Boolean.FALSE);
                changePasswordResultError.setMessage("Id utilizator furnizat este incorect!");
                return changePasswordResultError;
            }
        
            return idmService.resetMyPassword(username, newPassword);

        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    private Utilizator extractUserIdFromToken(String token) {
        try {
            Utilizator utilizator = utilizatorRepository.findByTokenUtilizator(token);
           
            if (utilizator != null) {
                return utilizator;
            } else {
                return null;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void sendPasswordRecoveryMail(String username) throws RanException, RanRuntimeException {
        try {
            Parametru parametru = parametruService.getParametru(RanConstants.URL_LOGIN_PARAM_KEY);
            Parametru urlBaseParam = parametruService.getParametru(RanConstants.URL_BASE_PARAM_KEY);
            Utilizator utilizator = utilizatorRepository.findByNumeUtilizatorIgnoreCase(username);
            if (utilizator != null) {
                String userToken = emailUrlTokenGeneratorService.generateTokenAndSaveToUser(utilizator);
                StringBuilder sb = new StringBuilder(30);
                sb.append(urlBaseParam.getValoare())
                        .append(parametru.getValoare())
                        .append(MessageFormat.format(RanConstants.FORGOT_PASSWORD_REDIRECT_QUERY, userToken));
                String redirectUrl = sb.toString();
                mailService.sendEmail(Arrays.asList(username),
                        messageBundleService.getMessage(NOTIFICARE_PAROLA_UITATA_SUBJECT_KEY),
                        messageBundleService.getMessage(NOTIFICARE_PAROLA_UITATA_CONTENT_KEY, new String[]{redirectUrl}));
            }
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }


    @Override
    public LogoutResult logout(String token) throws RanException, RanRuntimeException {
        try {
            LogoutResult logoutResult = idmService.logout(token);
            List<ro.uti.ran.core.model.portal.Sesiune> sesiuneList = sesiuneRepository.findByToken(token);
            ro.uti.ran.core.model.portal.Sesiune sesiune = null;
            if (sesiuneList != null && sesiuneList.size() > 1) {
                sesiune = sesiuneList.get(0);
            }
            if (sesiune != null) {
                sesiune.setDataStop(new Date());
                sesiuneRepository.save(sesiune);
            }

            return logoutResult;
        } catch (Exception e) {
            throw exceptionUtil.buildException(new RanRuntimeException(e));
        }
    }

    @Override
    public Boolean isEmailValid(String email) throws RanException, RanRuntimeException {
        Utilizator utilizator = utilizatorRepository.findByNumeUtilizatorIgnoreCase(email);
        if (utilizator == null) {
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

}
