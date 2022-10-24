package ro.uti.ran.core.datasourcerouter;

import java.sql.Connection;

/**
 * <p/>
 * User: <a href="mailto:bogdan.ardeleanu@uti.ro"> Bogdan Ardeleanu</a>
 * Date: Jan 6, 2011
 * Time: 12:42:20 PM
 * <p/>
 * UTI Systems, TIC
 */
public class DBConnectionHolder {
    private static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>();

    public static void setConnection(Connection connection) {
        if (connection == null) System.out.println("connection cannot be null");
        connectionHolder.set(connection);
    }

    public static Connection getConnection() {
        return connectionHolder.get();
    }

    public static void clearConnection() {
        connectionHolder.remove();
    }
}
