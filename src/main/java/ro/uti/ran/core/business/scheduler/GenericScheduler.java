package ro.uti.ran.core.business.scheduler;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

@Component
public class GenericScheduler extends ThreadPoolTaskScheduler {

    private static final long serialVersionUID = 1L;

    public GenericScheduler() {
        setPoolSize(1);
        initialize();
    }

}
