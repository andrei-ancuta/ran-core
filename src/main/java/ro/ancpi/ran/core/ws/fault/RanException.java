package ro.ancpi.ran.core.ws.fault;

import ro.uti.ran.core.exception.base.RanBusinessException;

import javax.xml.ws.WebFault;

@WebFault(name = "RanException", faultBean = "ro.ancpi.ran.ws.fault.RanFaultBean", targetNamespace = "core.ran.ancpi.ro")
public class RanException extends Exception {

    private RanFaultBean faultInfo;

    public RanException(RanBusinessException exception) {
        super("RanException", exception);
        faultInfo = new RanFaultBean(exception);

    }

    public RanFaultBean getFaultInfo() {
        return faultInfo;
    }

    public void setFaultInfo(RanFaultBean faultInfo) {
        this.faultInfo = faultInfo;
    }
}
