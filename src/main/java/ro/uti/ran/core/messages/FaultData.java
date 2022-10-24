package ro.uti.ran.core.messages;

import java.io.Serializable;

/**
 * 
 * @author mihai.plavichianu
 *
 */
public class FaultData implements Serializable {
	
    private String faultMessage;
    private String faultCode;
    private Throwable faultStackTrace;

    public String getFaultMessage() {
        return faultMessage;
    }

    public void setFaultMessage(String faultMessage) {
        this.faultMessage = faultMessage;
    }

    public String getFaultCode() {
        return faultCode;
    }

    public void setFaultCode(String faultCode) {
        this.faultCode = faultCode;
    }

	public Throwable getFaultStackTrace() {
		return faultStackTrace;
	}

	public void setFaultStackTrace(Throwable faultStackTrace) {
		this.faultStackTrace = faultStackTrace;
	}
}
