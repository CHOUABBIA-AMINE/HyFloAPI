/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : GroupMembershipRepository
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Interface
 * @Layer       : Repository
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository
 *
 * @Description : Declares the module-owned persistence repository for GroupMembership.
 */

package dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository;

import dz.sh.hyflo.modules.foundation.identity.domain.model.GroupMembership;
import org.springframework.data.repository.Repository;

public interface GroupMembershipRepository extends Repository<GroupMembership, Long> {
    GroupMembership save(GroupMembership membership);

    @org.springframework.data.jpa.repository.Query("select m from GroupMembership m where m.group.id = :groupId and m.id > :afterId order by m.id")
    java.util.List<GroupMembership> findHistory(@org.springframework.data.repository.query.Param("groupId") long groupId,
            @org.springframework.data.repository.query.Param("afterId") long afterId, org.springframework.data.domain.Pageable page);

    @org.springframework.data.jpa.repository.Query("select count(m) from GroupMembership m where m.group.id = :groupId and m.user.id = :userId "
            + "and (m.validTo is null or m.validTo > :now) and (m.revokedAt is null or m.revokedAt > :now)")
    long countNonEnded(@org.springframework.data.repository.query.Param("groupId") long groupId,
            @org.springframework.data.repository.query.Param("userId") long userId,
            @org.springframework.data.repository.query.Param("now") java.time.Instant now);

    @org.springframework.data.jpa.repository.Query("select m.user.id from GroupMembership m where m.group.id = :groupId and m.id = :id")
    java.util.Optional<Long> findMemberId(@org.springframework.data.repository.query.Param("groupId") long groupId,
            @org.springframework.data.repository.query.Param("id") long id);

    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @org.springframework.data.jpa.repository.Query("select m from GroupMembership m where m.group.id = :groupId and m.id = :id")
    java.util.Optional<GroupMembership> findSelectedForUpdate(@org.springframework.data.repository.query.Param("groupId") long groupId,
            @org.springframework.data.repository.query.Param("id") long id);
}
