package ro.ancpi.ran.core.ws;

import ro.uti.ran.core.common.HeadersContext;
import ro.uti.ran.core.ws.WsUtils;
import ro.uti.ran.core.ws.external.RanAuthentication;
import ro.ancpi.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.ws.internal.RanAuthorization;

import java.util.List;

/**
 * Created by Sache on 8/17/2016.
 */
public class WsUtilsExternal extends WsUtils {

    public static RanAuthentication checkRanAuthentication(RanAuthentication ranAuthentication, HeadersContext headersContext) throws RanRuntimeException {
        if (null == ranAuthentication || (null == ranAuthentication.getUsername() && null == ranAuthentication.getPassword())) {
            if (null != headersContext && null != headersContext.getHeaders()) {
                List<String> headers = headersContext.getHeaders().get(AUTHORIZATION_HEADER_NAME);
                if (null != headers) {
                    String authHeader = !headers.isEmpty() ?
                            headers.get(0) : null;

                    if (null != authHeader) {
                        return getRanAuthenticationFromString(authHeader);
                    }
                }
            }
        } else {
            if (null == ranAuthentication.getUsername() || ranAuthentication.getUsername().isEmpty()) {
                throw new RanRuntimeException("Invalid soap header element: " + RAN_AUTHENTICATION_HEADER_NAME + ".username");
            }

            if (null == ranAuthentication.getPassword() || ranAuthentication.getPassword().isEmpty()) {
                throw new RanRuntimeException("Invalid soap header element: " + RAN_AUTHENTICATION_HEADER_NAME + ".password");
            }
            return ranAuthentication;
        }
        throw new RanRuntimeException(RAN_AUTHENTICATION_HEADER_NAME + " soap header is required but not found on the request");
    }

    private static RanAuthentication getRanAuthenticationFromString(String authHeader) throws RanRuntimeException {
        authHeader = authHeader.replaceFirst(BASIC_AUTHENTICATION_REGEX, EMPTY_STRING);
        authHeader = new String(DECODER.decode(authHeader));
        String[] creds = authHeader.contains(USERNAME_PASSWORD_SEPARATOR) ? authHeader.split(USERNAME_PASSWORD_SEPARATOR) : null;
        String username = null != creds ? creds[0] : null;
        String password = null != creds ? creds[1] : null;

        if (null == username || username.isEmpty()) {
            throw new RanRuntimeException("Invalid soap header element: " + AUTHORIZATION_HEADER_NAME + ".username");
        }
        if (null == password || password.isEmpty()) {
            throw new RanRuntimeException("Invalid soap header element: " + AUTHORIZATION_HEADER_NAME + ".password");
        }
        RanAuthentication temp = new RanAuthentication();
        temp.setPassword(password);
        temp.setUsername(username);
        return temp;
    }
}
