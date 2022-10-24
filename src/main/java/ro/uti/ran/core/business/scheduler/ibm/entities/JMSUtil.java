package ro.uti.ran.core.business.scheduler.ibm.entities;

import java.lang.reflect.Method;

public abstract class JMSUtil {

    protected static Method getMethod(String className, String methodName, Class parameterType) throws ClassNotFoundException, SecurityException, NoSuchMethodException {
        Class clazz = Class.forName(className);
        Method method = null;

        if (parameterType != null) {
            method = clazz.getMethod(methodName, parameterType);
        } else {
            method = clazz.getMethod(methodName);
        }

        return method;
    }

    protected static Method getMethod2(String className, String methodName, Class parameterType1, Class parameterType2) throws ClassNotFoundException, SecurityException, NoSuchMethodException {
        Class clazz = Class.forName(className);
        Method method = clazz.getMethod(methodName, parameterType1, parameterType2);
        return method;
    }

    protected static Object callMethod(Object o, String className, String methodName, Object value, Class parameterType) {

        Method method = null;
        try {
            if (parameterType != null) {
                method = getMethod(className, methodName, parameterType);
                return method.invoke(o, new Object[]{value});
            } else {
                method = getMethod(className, methodName, null);
                return method.invoke(o, new Object[]{});
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected static void callMethod2(Object o, String className, String methodName, Object value1, Object value2, Class parameterType1, Class parameterType2) {

        try {
            Method method = getMethod2(className, methodName, parameterType1, parameterType2);
            method.invoke(o, new Object[]{value1, value2});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
