package ro.uti.ran.core.common;


import java.io.Serializable;

/**
 * 
 * @author mihai.plavichianu
 *
 */
public class OperationContext implements Serializable {

    /**
     * operation id take from the database  (as designed: idMessageRequest = idMessageResponse = idOperation)
     */
    private Long idOperation;

    /**
     * The main operations supported by the system
     */
    private String ranOperationType;

	public Long getIdOperation() {
		return idOperation;
	}

	public void setIdOperation(Long idOperation) {
		this.idOperation = idOperation;
	}

	public String getRanOperationType() {
		return ranOperationType;
	}

	public void setRanOperationType(String ranOperationType) {
		this.ranOperationType = ranOperationType;
	}

}
