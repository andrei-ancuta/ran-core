package ro.uti.ran.core.business.scheduler.ibm.entities;

import java.util.Date;

public class JMSTaskInfo {

    protected static final String CLASS_NAME = "com.ibm.websphere.scheduler.MessageTaskInfo";
    protected static final String INTERFACE_NAME = "com.ibm.websphere.scheduler.TaskInfo";

    private Object taskInfo;


    public JMSTaskInfo(Object taskInfo) {
        this.taskInfo = taskInfo;
    }

    protected Object getTaskInfo() {
        return taskInfo;
    }

    public void setConnectionFactoryJndiName(String connectionFactoryJndiName) {
        JMSUtil.callMethod(taskInfo, CLASS_NAME, "setConnectionFactoryJndiName", connectionFactoryJndiName, String.class);
    }

    public void setDestinationJndiName(String destinationJndiName) {
        JMSUtil.callMethod(taskInfo, CLASS_NAME, "setDestinationJndiName", destinationJndiName, String.class);
    }

    public void setMessageData(byte[] messageData) {
        JMSUtil.callMethod(taskInfo, CLASS_NAME, "setMessageData", messageData, byte[].class);
    }

    public void setNumberOfRepeats(int numberOfRepeats) {
        JMSUtil.callMethod(taskInfo, CLASS_NAME, "setNumberOfRepeats", numberOfRepeats, int.class);
    }

    public void setUserCalendar(String homeJNDIName, String specifier) {
        JMSUtil.callMethod2(taskInfo, CLASS_NAME, "setUserCalendar", homeJNDIName, specifier, String.class, String.class);
    }

    public void setStartTime(Date date) {
        JMSUtil.callMethod(taskInfo, CLASS_NAME, "setStartTime", date, Date.class);
    }

    public void setRepeatInterval(String repeatInterval) {
        JMSUtil.callMethod(taskInfo, CLASS_NAME, "setRepeatInterval", repeatInterval, String.class);
    }

    public String getName() {
        return (String) JMSUtil.callMethod(taskInfo, CLASS_NAME, "getName", null, null);
    }

    public void setName(String name) {
        JMSUtil.callMethod(taskInfo, CLASS_NAME, "setName", name, String.class);
    }


}
