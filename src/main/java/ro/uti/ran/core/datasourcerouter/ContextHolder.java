package ro.uti.ran.core.datasourcerouter;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: Mar 4, 2010
 * Time: 1:26:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContextHolder {
    private static final ThreadLocal<EnvironmentType> contextHolder = new ThreadLocal<EnvironmentType>();

    public static void setEnvironmentType(EnvironmentType environmentType) {
        if (environmentType == null) System.out.println("EnvironmentType cannot be null");
        contextHolder.set(environmentType);
    }

    public static EnvironmentType getEnvironmentType() {
        return contextHolder.get();
    }

    public static void clearEnvironmentType() {
        contextHolder.remove();
    }
}
