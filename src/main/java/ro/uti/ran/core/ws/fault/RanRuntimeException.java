package ro.uti.ran.core.ws.fault;

import org.apache.commons.lang.exception.ExceptionUtils;

import javax.xml.ws.WebFault;


@WebFault(name = "RanRuntimeException", faultBean = "ro.uti.ran.ws.fault.RanRuntimeFaultBean", targetNamespace = "core.ran.uti.ro")
public class RanRuntimeException extends Exception {

	private RanRuntimeFaultBean faultInfo;

	public RanRuntimeException(String message) {
		super("RanRuntimeException");
		init(message);
		System.err.println("RanRuntimeException (code=" + faultInfo.getCode() + ", message=" + faultInfo.getMessage());
	}

	private void init(String message) {
		faultInfo = new RanRuntimeFaultBean();
		faultInfo.setMessage(message);
		faultInfo.setCode("INTERNAL-ERROR");
	}

	public RanRuntimeException(Throwable t) {
		super("RanRuntimeException", t);
		if (t instanceof RanRuntimeException) {
			faultInfo = ((RanRuntimeException) t).getFaultInfo().clone();
		} else {
			t = null != ExceptionUtils.getRootCause(t) ? ExceptionUtils.getRootCause(t) : t;
			init(null != t.getMessage() ? t.getMessage() : (null != t.getCause() ? t.getCause().getMessage() : t.toString()));
		}
		System.err.println("RanRuntimeException (code=" + faultInfo.getCode() + ", message=" + faultInfo.getMessage());
	}

	public RanRuntimeFaultBean getFaultInfo() {
		return faultInfo;
	}

	public void setFaultInfo(RanRuntimeFaultBean faultInfo) {
		this.faultInfo = faultInfo;
	}
}
