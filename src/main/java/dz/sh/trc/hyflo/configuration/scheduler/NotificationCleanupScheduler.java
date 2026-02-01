/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : NotificationCleanupScheduler
 *  @CreatedOn  : 02-01-2026
 *  @UpdatedOn  : 02-01-2026
 *
 *  @Type       : Class
 *  @Layer      : Scheduler
 *  @Package    : System / Notification / Scheduler
 *
 **/

package dz.sh.trc.hyflo.configuration.scheduler;

import dz.sh.trc.hyflo.system.notification.core.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduled task to cleanup old read notifications
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
    prefix = "notification.cleanup",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true
)
public class NotificationCleanupScheduler {

    private final NotificationService notificationService;

    @Value("${notification.cleanup.retention-days:30}")
    private int retentionDays;

    /**
     * Run daily at 2 AM to cleanup old read notifications
     */
    @Scheduled(cron = "${notification.cleanup.cron:0 0 2 * * ?}")
    public void cleanupOldNotifications() {
        log.info("Starting scheduled notification cleanup (retention: {} days)", retentionDays);
        
        try {
            notificationService.deleteOldReadNotifications(retentionDays);
            log.info("Successfully completed notification cleanup");
        } catch (Exception e) {
            log.error("Error during notification cleanup", e);
        }
    }
}
