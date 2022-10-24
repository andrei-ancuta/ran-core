package ro.uti.ran.core.business.scheduler.ibm;

import com.ibm.ws.scheduler.cron.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.stereotype.Component;
import ro.uti.ran.core.business.scheduler.ibm.entities.JMSScheduler;
import ro.uti.ran.core.business.scheduler.ibm.entities.JMSTaskInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;


@Component
public class WASScheduler implements TaskScheduler {

    protected static final String CLASS_NAME = "className";
    protected static final String METHOD_NAME = "methodName";

    @Autowired
    private JMSScheduler scheduler;

    @Autowired
    private SchedulerExecutorContext schedulerExecutorContext;

    @Autowired
    private Environment env;

    public WASScheduler() {
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable runnable, Trigger trigger) {
        JMSTaskInfo taskInfo = createTask(runnable);
        taskInfo.setStartTime(new Date());
        taskInfo.setUserCalendar(null, "CRON");

        String cronExpr = trigger.toString().split(":")[1].trim();
        taskInfo.setRepeatInterval(cronExpr);
        try {
            CronExpression cron = new CronExpression(cronExpr);
            Date today = new Date();
            Date nextExecution = cron.calculateClosestTime(today, true);
            taskInfo.setStartTime(nextExecution);
        } catch (ParseException e) {
            System.out.println("Eroare la setare timp start. Taskul va rula o data la deploy:");
            e.printStackTrace();
        }

        scheduler.create(taskInfo);
        return null;
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable runnable, Date startTime) {
        JMSTaskInfo taskInfo = createTask(runnable);
        taskInfo.setStartTime(startTime);
        taskInfo.setNumberOfRepeats(1);
        scheduler.create(taskInfo);
        return null;
    }


    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long period) {
        return scheduleAtFixedRate(runnable, new Date(), period);
    }


    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, Date startTime, long period) {
        JMSTaskInfo taskInfo = createTask(runnable);
        taskInfo.setStartTime(startTime);
        taskInfo.setRepeatInterval(period + "ms");
        scheduler.create(taskInfo);
        return null;
    }


    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable runnable, long delay) {
        return scheduleWithFixedDelay(runnable, new Date(), delay);
    }


    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable runnable, Date startTime, long delay) {
        JMSTaskInfo taskInfo = createTask(runnable);
        taskInfo.setStartTime(startTime);
        //TODO Implementare Topic
        taskInfo.setRepeatInterval(delay + "ms");
        scheduler.create(taskInfo);
        return null;
    }

    private JMSTaskInfo createTask(Runnable runnable) {
        try {
            JMSTaskInfo taskInfo = scheduler.createTaskInfo(getTaskName(runnable));
            taskInfo.setConnectionFactoryJndiName(env.getProperty("sched.connection.fatory.queue"));
            taskInfo.setDestinationJndiName(env.getProperty("sched.connection.queue"));
            taskInfo.setMessageData(prepareMessage(runnable));
            taskInfo.setNumberOfRepeats(-1);
            taskInfo.setUserCalendar(null, "SIMPLE");
            return taskInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private byte[] prepareMessage(Runnable runnable) throws IOException {
        Map<String, String> mapMessageData = new HashMap<String, String>();
        Map.Entry<String, String> target = schedulerExecutorContext.getTarget(runnable);
        mapMessageData.put(CLASS_NAME, target.getKey());
        mapMessageData.put(METHOD_NAME, target.getValue());
        return marshall(mapMessageData);
    }

    protected byte[] marshall(Map<String, String> map) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(map);
        objOut.close();
        return out.toByteArray();
    }

    private String getTaskName(Runnable runnable) {
        Map.Entry<String, String> target = schedulerExecutorContext.getTarget(runnable);
        return target.getKey() + "***" + target.getValue();
    }

    public boolean isActive() {
        return scheduler.isActive();
    }
}