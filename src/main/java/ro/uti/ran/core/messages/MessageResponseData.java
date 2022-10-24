package ro.uti.ran.core.messages;

/**
 * 
 * @author mihai.plavichianu
 *
 */
public class MessageResponseData extends MessageData {
    private FaultData fault;

    public FaultData getFault() {
        return fault;
    }

    public void setFault(FaultData fault) {
        this.fault = fault;
    }
}
