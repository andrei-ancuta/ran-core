package ro.uti.ran.core.messages;

import java.util.Map;
import java.util.List;

/**
 * 
 * @author mihai.plavichianu
 *
 */
public class MessageRequestData extends MessageData {
	
    private String wsdlServiceName;
    private String wsdlOperationName;
    private String ipAddress;
    private String hostName;
    private Map<String, List<String>> httpHeaders;

    private String ranOperationType;

    public String getWsdlServiceName() {
        return wsdlServiceName;
    }

    public void setWsdlServiceName(String wsdlServiceName) {
        this.wsdlServiceName = wsdlServiceName;
    }

    public String getWsdlOperationName() {
        return wsdlOperationName;
    }

    public void setWsdlOperationName(String wsdlOperationName) {
        this.wsdlOperationName = wsdlOperationName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getRanOperationType() {
        return ranOperationType;
    }

    public void setRanOperationType(String ranOperationType) {
        this.ranOperationType = ranOperationType;
    }

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	
	public void setHttpHeaders(Map<String, List<String>> httpHeaders) {
		this.httpHeaders = httpHeaders;
	}
	
	public Map<String, List<String>> getHttpHeaders() {
		return httpHeaders;
	}
}
