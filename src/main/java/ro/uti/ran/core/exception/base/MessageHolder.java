package ro.uti.ran.core.exception.base;

import java.io.Serializable;

public class MessageHolder implements Serializable {

    private final String message;
    private final String hint;
    private final int secondaryCode;
    private String resourceKey;

    public MessageHolder(int secondaryCode, String message, String hint) {
        this.message = message;
        this.hint = hint;
        this.secondaryCode = secondaryCode;
    }

    public MessageHolder(int secondaryCode, String message, String hint, String resourceKey) {
        this(secondaryCode, message, hint);
        this.resourceKey = resourceKey;
    }

    String format(Object... args) {
        return String.format(message, args);
    }

    public String getMessage() {
        return message;
    }

    public String getHint() {
        return hint;
    }

    public int getSecondaryCode() {
        return secondaryCode;
    }

    public String getResourceKey() {
        return resourceKey;
    }
}
