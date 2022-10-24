package ro.uti.ran.core.business.scheduler.ibm.entities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Iterator;

@Component
public class JMSScheduler {

    private static String _TABLE_PREFIX = null;
    private static String _DELETE_SQL = null;
    private DataSource dataSource = null;
    private Object scheduler = null;
    
    private static final String SCHEDULER_CLASS_NAME = "com.ibm.websphere.scheduler.Scheduler";
    private final Log log = LogFactory.getLog(JMSScheduler.class);
    
    @Autowired
    private Environment env;
  
    public JMSTaskInfo createTaskInfo(String taskName) throws Exception {
        Class clazz = Class.forName(JMSTaskInfo.CLASS_NAME);
        Object o = JMSUtil.callMethod(scheduler, SCHEDULER_CLASS_NAME, "createTaskInfo", clazz, clazz.getClass());
        JMSTaskInfo taskInfo = new JMSTaskInfo(o);
        taskInfo.setName(taskName);
        return taskInfo;
    }

    /**
     * Creare tranzactie pentru 2 operatii consecutive cu schedulerul
     *
     * @param taskInfo
     * @return
     */
    public Object create(JMSTaskInfo taskInfo) {

        try {

            log.debug("Adding an IBM task with name '" + taskInfo.getName() + "' in scheduler");

            deleteTaskByName(taskInfo.getName());
            JMSUtil.callMethod(scheduler, SCHEDULER_CLASS_NAME, "create", taskInfo.getTaskInfo(), Class.forName(JMSTaskInfo.INTERFACE_NAME));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void deleteTaskByName(String taskName) throws Exception {
        Iterator tasks = (Iterator) JMSUtil.callMethod(scheduler, SCHEDULER_CLASS_NAME, "findTasksByName", taskName, String.class);
        Object next = null;
        while (tasks.hasNext()) {
            next = tasks.next();
            next = JMSUtil.callMethod(next, JMSTaskInfo.CLASS_NAME, "getTaskId", null, null);
            JMSUtil.callMethod2(scheduler, SCHEDULER_CLASS_NAME, "cancel", next, true, next.getClass(), boolean.class);
        }

        try {
            deleteTaskByNameExternal(taskName);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void deleteTaskByNameExternal(String taskName) throws Exception {

        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(true);

        PreparedStatement preparedStatement = connection.prepareStatement(_DELETE_SQL);
        preparedStatement.setString(1, taskName);
        preparedStatement.executeUpdate();
        connection.close();

    }

    public boolean isActive() {
        log.debug("The IBM Scheduler was detected");
        return scheduler != null;
    }

    @PostConstruct
    private void init() {
        try {
            InitialContext context = new InitialContext();
            scheduler = context.lookup(env.getProperty("sched.jndi"));
            dataSource = (DataSource) context.lookup(env.getProperty("sched.datasource.jndi"));
            
            Field reqField = scheduler.getClass().getDeclaredField("configuration");
            reqField.setAccessible(true);
            Object configuration = reqField.get(scheduler);

            Class partypes[] = new Class[0];
            Object arglist[] = new Object[0];
            Method getContextMethod = configuration.getClass().getMethod("getTablePrefix", partypes);
            getContextMethod.setAccessible(true);
            String tablePrefix = (String) getContextMethod.invoke(configuration, arglist);

            _TABLE_PREFIX = tablePrefix;
            _DELETE_SQL = "DELETE FROM " + _TABLE_PREFIX + "TASK WHERE NAME LIKE ?";
            
        } catch (Exception e) {
            log.debug("Cannot init IBM Scheduler");
        }
    }
}