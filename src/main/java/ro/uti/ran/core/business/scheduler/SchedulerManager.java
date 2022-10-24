package ro.uti.ran.core.business.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.stereotype.Component;

import ro.uti.ran.core.business.scheduler.ibm.SchedulerExecutorContext;
import ro.uti.ran.core.business.scheduler.ibm.WASScheduler;
import ro.uti.ran.core.service.parametru.ParametruService;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

@Component
public class SchedulerManager implements TaskScheduler {

    @Autowired
    private WASScheduler wasScheduler;

    @Autowired
    private GenericScheduler genericScheduler;

    @Autowired
    private SchedulerExecutorContext schedulerExecutorContext;
    
    @Autowired
    private ParametruService parametruService; 

    @Override
    public ScheduledFuture<?> schedule(Runnable runnable, Trigger trigger) {
        return getScheduler(runnable).schedule(runnable, trigger);
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable runnable, Date startTime) {
        return getScheduler(runnable).schedule(runnable, startTime);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long period) {
        return scheduleAtFixedRate(runnable, new Date(), period);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, Date startTime, long period) {
        return getScheduler(runnable).scheduleAtFixedRate(runnable, startTime, period);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable runnable, long delay) {
        return scheduleWithFixedDelay(runnable, new Date(), delay);
    }

	@Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable runnable, Date startTime, long delay) {
        return getScheduler(runnable).scheduleWithFixedDelay(runnable, startTime, delay);
    }

    private TaskScheduler getScheduler(Runnable runnable) {
        if (isClusterable(runnable)) {
            return wasScheduler;
        }

        return genericScheduler;
    }

    private boolean isClusterable(Runnable runnable) {
        if (!wasScheduler.isActive()) {
            System.out.println("The IBM Scheduler was not detected for runnable " + runnable);
            return false;
        }

        try {
            boolean clusterable = schedulerExecutorContext.isClusterable(runnable);
            System.out.println("The Task clusterable status is " + clusterable + " for runnable " + runnable);
            return clusterable;
        } catch (Exception e) {
            return false;
        }

    }

}
