package ro.uti.ran.core.model.utils;

/**
 * Created by Stanciu Neculai on 05.Nov.2015.
 */
public enum JMSContextEnum {
    DEFAULT("jndi.jms.connection.factory","jndi.jms.queue"),
    TRANSMISIE("jndi.jms.connection.factory.transmisie","jndi.jms.queue.transmisie");

    private String connectionFactoryProperty;
    private String queueProperty;

    JMSContextEnum(String connectionFactoryProperty, String queueProperty) {
        this.connectionFactoryProperty = connectionFactoryProperty;
        this.queueProperty = queueProperty;
    }

    public String getConnectionFactoryProperty() {
        return connectionFactoryProperty;
    }

    public String getQueueProperty() {
        return queueProperty;
    }
}
