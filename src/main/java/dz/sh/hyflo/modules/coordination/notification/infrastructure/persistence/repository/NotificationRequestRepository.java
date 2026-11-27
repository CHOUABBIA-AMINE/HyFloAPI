/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : NotificationRequestRepository
 * @CreatedOn   : 11-27-2026
 * @UpdatedOn   : 11-27-2026
 *
 * @Type        : Interface
 * @Layer       : Repository
 * @Module      : Modules / Coordination / Notification
 * @Package     : dz.sh.hyflo.modules.coordination.notification.infrastructure.persistence.repository
 *
 * @Description : Declares the module-owned persistence repository for NotificationRequest.
 */

package dz.sh.hyflo.modules.coordination.notification.infrastructure.persistence.repository;

import dz.sh.hyflo.modules.coordination.notification.domain.model.NotificationRequest;
import org.springframework.data.repository.Repository;

public interface NotificationRequestRepository extends Repository<NotificationRequest, Long> {
}
