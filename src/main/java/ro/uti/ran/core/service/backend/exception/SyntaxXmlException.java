package ro.uti.ran.core.service.backend.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * Aceasta exceptie se arunca la parsarea unui xml
 * Created by Dan on 12-Oct-15.
 */
public class SyntaxXmlException extends Exception {
    /**
     * Descrierea erorii ce va fi utilizat la recipisa de răspuns;
     */
    private List<String> lstErrorDescription;

    public SyntaxXmlException() {
        super();
        lstErrorDescription = new ArrayList<String>();
    }

    public SyntaxXmlException(String message) {
        super(message);
        lstErrorDescription = new ArrayList<String>();
    }

    public SyntaxXmlException(String message, Throwable cause) {
        super(message, cause);
        lstErrorDescription = new ArrayList<String>();
    }

    public SyntaxXmlException(Throwable cause) {
        super(cause);
        lstErrorDescription = new ArrayList<String>();
    }

    public void addErrorDescription(String errorDescription) {
        lstErrorDescription.add(errorDescription);
    }

    public List<String> getLstErrorDescription() {
        return lstErrorDescription;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(300);
        for(String errMsg : lstErrorDescription) {
            sb.append(errMsg).append('\n');
        }
        return sb.toString();
    }
}
