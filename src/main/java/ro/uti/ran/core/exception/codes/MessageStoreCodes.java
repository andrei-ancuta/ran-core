package ro.uti.ran.core.exception.codes;


import ro.uti.ran.core.exception.base.MessageHolder;
import ro.uti.ran.core.exception.base.MessageProvider;


public enum MessageStoreCodes implements MessageProvider {

    WRITE_MESSAGE_TO_DISK(1, "Message '%s' could not be saved to disk"),
    SAVE_MESSAGE(2, "Message '%s' could not be saved");

    private MessageHolder provider;

    private MessageStoreCodes(int secondaryCode, String message, String hint) {
        provider = new MessageHolder(secondaryCode, message, hint);
    }

    private MessageStoreCodes(int secondaryCode, String message) {
        this(secondaryCode, message, null);
    }

    public MessageHolder getMessageHolder() {
        return provider;
    }
}
