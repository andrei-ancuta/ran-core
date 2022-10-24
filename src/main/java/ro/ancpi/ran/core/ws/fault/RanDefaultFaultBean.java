package ro.ancpi.ran.core.ws.fault;

import ro.uti.ran.core.ws.handlers.RanWsHandlerFilter;


abstract class RanDefaultFaultBean implements Cloneable {

	private String code;
	private String message;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String mess) {
		
		try {
			// TODO: TBD
			String messageCode = "TBD";
			if(RanWsHandlerFilter.getMessageId() != null) {
				messageCode = RanWsHandlerFilter.getMessageId();
			}
			message = "{msgId=" + messageCode + "}";
		} catch (Exception e) {
			e.printStackTrace();
        	message = "";
        }

		message = mess + " " + message;

	}
	 
}
