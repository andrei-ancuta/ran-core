package ro.uti.ran.core.messages;

import java.io.Serializable;

/**
 * 
 * @author mihai.plavichianu
 *
 */
public class MessageData implements Serializable {
    private Object soapMessage;

    public String getSoapMessage() {
    	
    	if(soapMessage == null) {
    		return null;
    	}
    	
        return soapMessage.toString();
    }

    public void setSoapMessage(Object soapMessage) {
        this.soapMessage = soapMessage;
    }
}
