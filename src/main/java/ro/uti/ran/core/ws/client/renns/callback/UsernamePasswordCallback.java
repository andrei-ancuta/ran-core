package ro.uti.ran.core.ws.client.renns.callback;

import javax.security.auth.callback.*;
import java.io.IOException;

/**
 * Created by Anastasia cea micuta on 11/20/2016.
 */
public class UsernamePasswordCallback implements CallbackHandler {
    public UsernamePasswordCallback() {

    }

    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof NameCallback) {
                NameCallback nc = (NameCallback) callbacks[i];
                nc.setName("ran_soap");
            } else if (callbacks[i] instanceof PasswordCallback) {
                PasswordCallback pc = (PasswordCallback) callbacks[i];
                pc.setPassword("Qazwsxedc1234!".toCharArray());
            } else {
                throw new UnsupportedCallbackException(callbacks[i],
                        "Unrecognized Callback");
            }
        }
    }
}