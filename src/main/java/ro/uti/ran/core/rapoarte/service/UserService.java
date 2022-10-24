package ro.uti.ran.core.rapoarte.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ro.uti.ran.core.exception.WsAuthenticationException;
import ro.uti.ran.core.model.portal.Sistem;

import ro.uti.ran.core.service.security.RanUserDetailsService;
import ro.uti.ran.core.ws.external.RanAuthentication;
import ro.uti.ran.core.ws.internal.RanAuthorization;

import java.util.*;

import static ro.uti.ran.core.exception.codes.WsAutenticationCodes.*;
import static ro.uti.ran.core.exception.codes.WsAutenticationCodes.USER_NOT_FOUND;

/**
 * Created by miroslav.rusnac on 17/03/2016.
 */
@Service
@PropertySource("classpath:rapoarte.properties")
public class UserService  {


    @Autowired
    private RanUserDetailsService ranUserDetailsService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private Environment env;



    public RanAuthorization  checkUser(final String username,final String password) throws WsAuthenticationException {
        RanAuthentication ranAuthentication = new RanAuthentication();
        ranAuthentication.setPassword(password);
        ranAuthentication.setUsername(username);

        try {
            Sistem userDetails = ranUserDetailsService.loadSistemUserByUsername(ranAuthentication.getUsername());
            if (null == userDetails) {
                throw new WsAuthenticationException(USER_NOT_FOUND, ranAuthentication.getUsername());
            }

            if (!userDetails.getActiv()) {
                throw new WsAuthenticationException(USER_INACTIVE, ranAuthentication.getUsername());
            }

            if (encoder.matches(ranAuthentication.getPassword(), userDetails.getLicenta())) {
                RanAuthorization ranAuthorization = new RanAuthorization();
                if (null != userDetails.getUat()) {
                    ranAuthorization.setContext("UAT");
                    ranAuthorization.setIdEntity(userDetails.getUat().getId());
                } else if (null != userDetails.getJudet()) {
                    ranAuthorization.setContext("UAT");
                    ranAuthorization.setIdEntity(userDetails.getJudet().getId());
                } else if (null != userDetails.getInstitutie()) {
                    ranAuthorization.setContext(userDetails.getInstitutie().getCod());
                    ranAuthorization.setIdEntity(userDetails.getInstitutie().getId());
                } else {
                    throw new WsAuthenticationException(CONT_INSTITUTIE_INEXISTENT, ranAuthentication.getUsername());
                }

                return ranAuthorization;

            } else {
                throw new WsAuthenticationException(COD_LICENTA_INCORECT, ranAuthentication.getPassword());
            }

        } catch (UsernameNotFoundException e) {
            throw new WsAuthenticationException(USER_NOT_FOUND, ranAuthentication.getUsername());
        }

    }

    public String getProperty(String propertyName){
        return env.getProperty(propertyName);
    }

    public boolean checkPermission(String context){
        boolean denided = true;
        String permitedContext = env.getProperty("reports.permissions");
        List<String> list = new ArrayList<String>(Arrays.asList(permitedContext.split(",")));
        Set<String> set = new HashSet<String>(list);
        if (set.contains(context))
        {
            denided=false;
        }
        return denided;
    }

}