package ro.uti.ran.core.model.messages;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * @author mihai.plavichianu
 *
 */
@Entity
@Table(name = "MSG_RESPONSE")
public class MessageResponseEntity extends MessageEntity {
    @Id
    @Column(name = "ID_MSG")
    private Long idMessage;

    @Column(name = "RESPONSE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date responseDate;

    @Column(name = "FAULT_MSG", length=4000)
    private String faultMsg;

    @Column(name = "FAULT_STACK_TRACE", length=4000)
    private String faultStackTrace;

    @Column(name = "FAULT_CODE", length=255)
    private String faultCode;

    @Override
    public Long getIdMessage() {
        return idMessage;
    }

    @Override
    public void setIdMessage(Long idMessage) {
        this.idMessage = idMessage;
    }

    public Date getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(Date responseDate) {
        this.responseDate = responseDate;
    }

    public String getFaultMsg() {
        return faultMsg;
    }

    public void setFaultMsg(String faultMsg) {
        this.faultMsg = faultMsg;
    }

    public String getFaultCode() {
        return faultCode;
    }

    public void setFaultCode(String faultCode) {
        this.faultCode = faultCode;
    }

	public String getFaultStackTrace() {
		return faultStackTrace;
	}

	public void setFaultStackTrace(String faultStackTrace) {
		this.faultStackTrace = faultStackTrace;
	}
}
