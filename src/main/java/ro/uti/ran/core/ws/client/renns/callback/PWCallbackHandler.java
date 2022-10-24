package ro.uti.ran.core.ws.client.renns.callback;


import org.apache.wss4j.common.ext.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;

/**
 * Created by Anastasia cea micuta on 11/20/2016.
 */
public class PWCallbackHandler implements CallbackHandler {

    @Override
    public void handle(Callback[] callbacks) throws IOException,
            UnsupportedCallbackException {
        WSPasswordCallback c = (WSPasswordCallback) callbacks[0];
        // in a real world client these would probably come from SecurityContextHolder
        // or some session object
        c.setIdentifier("ran_soap");
        c.setPassword("Qazwsxedc1234!");
    }

}