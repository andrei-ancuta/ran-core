package ro.ancpi.ran.core.ws.fault;

import javax.xml.ws.WebFault;


@WebFault(name = "RanRuntimeException", faultBean = "ro.ancpi.ran.ws.fault.RanRuntimeFaultBean", targetNamespace = "core.ran.ancpi.ro")
public class RanRuntimeException extends ro.uti.ran.core.ws.fault.RanRuntimeException {

	public RanRuntimeException(String message) {
		super(message);
	}

	public RanRuntimeException(Throwable t) {
		super(t);
	}
}
