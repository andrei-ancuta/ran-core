package ro.uti.ran.core.common;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class HeadersContext implements Serializable {

    private Map<String, List<String>> headers;

	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, List<String>> headers) {
		this.headers = headers;
	}
}
