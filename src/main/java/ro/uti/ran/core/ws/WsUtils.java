package ro.uti.ran.core.ws;

import org.apache.commons.codec.binary.Base64;
import ro.uti.ran.core.datasourcerouter.ContextHolder;
import ro.uti.ran.core.ws.external.RanAuthentication;
import ro.uti.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.ws.internal.RanAuthorization;

import static ro.uti.ran.core.datasourcerouter.EnvironmentType.RAL;

/**
 * Created by Anastasia cea micuta on 11/23/2015.
 */
public class WsUtils {
    protected static final String RAN_AUTHORIZATION_HEADER_NAME = "RanAuthorization";
    protected static final String RAN_AUTHENTICATION_HEADER_NAME = "RanAuthentication";
    protected static String AUTHORIZATION_HEADER_NAME = "Authorization";
    protected static String BASIC_AUTHENTICATION_REGEX = "Basic\\s";
    protected static String USERNAME_PASSWORD_SEPARATOR = ":";
    protected static String EMPTY_STRING = "";
    protected static Base64 DECODER = new Base64();

    public static void checkRanAuthorization(RanAuthorization ranAuthorization) throws RanRuntimeException {
        if (null == ranAuthorization) {
            throw new RanRuntimeException(RAN_AUTHORIZATION_HEADER_NAME + " soap header is required but not found on the request");
        }

        if (null == ranAuthorization.getIdEntity() || ranAuthorization.getIdEntity() <= 0) {
            throw new RanRuntimeException("Invalid soap header element: " + RAN_AUTHORIZATION_HEADER_NAME + ".idEntity");
        }

        if (null == ranAuthorization.getContext() || ranAuthorization.getContext().isEmpty()) {
            throw new RanRuntimeException("Invalid soap header element: " + RAN_AUTHORIZATION_HEADER_NAME + ".context");
        }
    }

    public static void switchOnRAL(RanAuthorization ranAuthorization) {
        ContextHolder.clearEnvironmentType();
        if (null != ranAuthorization.getLocal() && ranAuthorization.getLocal()) {
            ContextHolder.setEnvironmentType(RAL);
        }
    }

    public static void switchOffRAL(RanAuthorization ranAuthorization) {
        ContextHolder.clearEnvironmentType();
    }

    public static void checkRanAuthentication(RanAuthentication ranAuthentication) throws RanRuntimeException {
        if (null == ranAuthentication) {
            throw new RanRuntimeException(RAN_AUTHENTICATION_HEADER_NAME + " soap header is required but not found on the request");
        }

        if (null == ranAuthentication.getUsername() || ranAuthentication.getUsername().isEmpty()) {
            throw new RanRuntimeException("Invalid soap header element: " + RAN_AUTHENTICATION_HEADER_NAME + ".username");
        }

        if (null == ranAuthentication.getPassword() || ranAuthentication.getPassword().isEmpty()) {
            throw new RanRuntimeException("Invalid soap header element: " + RAN_AUTHENTICATION_HEADER_NAME + ".password");
        }
    }

    public static void checkRanAuthorizationUAT(RanAuthorization ranAuthorization) throws RanRuntimeException {
        checkRanAuthorization(ranAuthorization);

        if (!"UAT".equals(ranAuthorization.getContext())) {
            throw new ro.ancpi.ran.core.ws.fault.RanRuntimeException("Invalid soap header element value: " + RAN_AUTHORIZATION_HEADER_NAME + ".context. Expected value: UAT, but found: " + ranAuthorization.getContext());
        }
    }

}
