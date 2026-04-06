/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : AsyncConfig
 *  @CreatedOn  : 02-01-2026
 *  @UpdatedOn  : 02-01-2026
 *
 *  @Type       : Class
 *  @Layer      : Configuration
 *  @Package    : Configuration
 *
 **/

package dz.sh.trc.hyflo.configuration.async;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import dz.sh.trc.hyflo.platform.security.async.AsyncProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * Async configuration for event processing
 */
@Slf4j
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

	@Autowired
	private final AsyncProperties props;
	
	@Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(props.corePoolSize());
        executor.setMaxPoolSize(props.maxPoolSize());
        executor.setQueueCapacity(props.queueCapacity());
        executor.setThreadNamePrefix(props.threadNamePrefix());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(props.awaitTerminationSeconds());
        executor.initialize();
        return executor;
    }
}
