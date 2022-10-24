package ro.uti.ran.core.model.messages;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author mihai.plavichianu
 *
 */
@Entity
@Table(name = "MSG_OPERATION")
public class OperationEntity implements Serializable {

	@Id
	@Column(name = "ID_OPERATION")
    @SequenceGenerator(name = "SEQ_OPERATION", allocationSize = 1, sequenceName = "SEQ_OPERATION")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_OPERATION")
	private Long idOperation;

    // TODO: to be removed !?!?!? replaced by wsdlOperationName & wsdlServiceName
	@Column(name = "OPERATION_TYPE")
	private String operationType;

    @Column(name = "IP_ADDRESS", length = 50)
    private String ipAddress;

    @Column(name = "WSDL_OPERATION_NAME", length = 60)
    private String wsdlOperationName;

    @Column(name = "WSDL_SERVICE_NAME", length = 60)
    private String wsdlServiceName;

    @Column(name = "OPERATION_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date operationTime;
    
    @Column(name = "HOST_NAME", length = 60)
    private String hostName;

    public Long getIdOperation() {
		return idOperation;
	}

	public void setIdOperation(Long operationId) {
		this.idOperation = operationId;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}


    public Date getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(Date createTime) {
		this.operationTime = createTime;
	}

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ip) {
        this.ipAddress = ip;
    }

    public String getWsdlOperationName() {
        return wsdlOperationName;
    }

    public void setWsdlOperationName(String wsdlOperationName) {
        this.wsdlOperationName = wsdlOperationName;
    }

    public String getWsdlServiceName() {
        return wsdlServiceName;
    }

    public void setWsdlServiceName(String wsdlServiceName) {
        this.wsdlServiceName = wsdlServiceName;
    }

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

}
