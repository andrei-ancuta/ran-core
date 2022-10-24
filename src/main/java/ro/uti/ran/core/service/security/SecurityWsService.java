package ro.uti.ran.core.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ro.uti.ran.core.exception.WsAuthenticationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.model.portal.Sistem;
import ro.uti.ran.core.ws.external.RanAuthentication;
import ro.uti.ran.core.ws.internal.RanAuthorization;

import static ro.uti.ran.core.exception.codes.WsAutenticationCodes.*;

/**
 * Created by Anastasia cea micuta on 11/29/2015.
 */
@Component
public class SecurityWsService {
    @Autowired
    private RanUserDetailsService ranUserDetailsService;

    @Autowired
    private PasswordEncoder encoder;

    public RanAuthorization authenticate(RanAuthentication ranAuthentication) throws RanBusinessException {
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
                    ranAuthorization.setUATLocal(Boolean.TRUE);
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
}
