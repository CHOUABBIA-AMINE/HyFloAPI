/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : NotificationTypeRepository
 * @CreatedOn   : 11-27-2026
 * @UpdatedOn   : 11-27-2026
 *
 * @Type        : Interface
 * @Layer       : Repository
 * @Module      : Modules / Coordination / Notification
 * @Package     : dz.sh.hyflo.modules.coordination.notification.infrastructure.persistence.repository
 *
 * @Description : Declares the module-owned persistence repository for NotificationType.
 */

package dz.sh.hyflo.modules.coordination.notification.infrastructure.persistence.repository;

import dz.sh.hyflo.modules.coordination.notification.domain.model.NotificationType;
import org.springframework.data.repository.Repository;

public interface NotificationTypeRepository extends Repository<NotificationType, Long> {
}
