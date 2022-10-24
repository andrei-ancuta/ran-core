package ro.uti.ran.core.ws.fault;

import ro.uti.ran.core.ws.handlers.RanWsHandlerFilter;

import javax.xml.bind.annotation.XmlElement;


abstract class RanDefaultFaultBean implements Cloneable {

    private String code;
    private String message;

    @XmlElement(required = true)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @XmlElement(required = true)
    public String getMessage() {
        return message;
    }

    public void setMessage(String mess) {

        try {
            // TODO: TBD
            String messageCode = "TBD";
            if (RanWsHandlerFilter.getMessageId() != null) {
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
