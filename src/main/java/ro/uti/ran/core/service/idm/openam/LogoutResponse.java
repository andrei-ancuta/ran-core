package ro.uti.ran.core.service.idm.openam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-14 13:35
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogoutResponse {

    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
