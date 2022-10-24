package ro.uti.ran.core.service.idm.openam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-08 16:17
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResource {

    private String tokenId;

    private String reason;

    private String message;

    private String successUrl;

    private String errorMessage;

    private String failureUrl;

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getFailureUrl() {
        return failureUrl;
    }

    public void setFailureUrl(String failureUrl) {
        this.failureUrl = failureUrl;
    }
}
