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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Slf4j
@Configuration
@EnableAsync
@RequiredArgsConstructor
@EnableConfigurationProperties(AsyncProperties.class)   // ← THIS is the fix
public class AsyncConfig implements AsyncConfigurer {

    private final AsyncProperties props;   // ← now properly injected

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
        log.info("Async executor initialized — pool: {}/{}, queue: {}",
            props.corePoolSize(), props.maxPoolSize(), props.queueCapacity());
        return executor;
    }
}