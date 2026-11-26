/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : GroupMembershipService
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-26-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.application.service
 *
 * @Description : Declares the module-owned application service for GroupMembership.
 */

package dz.sh.hyflo.modules.foundation.identity.application.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import dz.sh.hyflo.modules.foundation.identity.domain.model.GroupMembership;
import dz.sh.hyflo.modules.foundation.identity.domain.model.UserType;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.*;
import dz.sh.hyflo.modules.foundation.identity.application.service.SessionAuthenticationService.CurrentIdentity;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GroupMembershipService {
    private final GroupMembershipRepository memberships;
    private final GroupRepository groups;
    private final UserRepository users;
    private final PlatformRoleGrantRepository platformGrants;
    private final GroupRoleGrantRepository groupGrants;
    private final PlatformAuthorizationService authorization;

    public GroupMembershipService(GroupMembershipRepository memberships, GroupRepository groups,
            UserRepository users, PlatformRoleGrantRepository platformGrants, GroupRoleGrantRepository groupGrants,
            PlatformAuthorizationService authorization) {
        this.memberships = memberships;
        this.groups = groups;
        this.users = users;
        this.platformGrants = platformGrants;
        this.groupGrants = groupGrants;
        this.authorization = authorization;
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public List<Entry> list(CurrentIdentity principal, long groupId, long afterId) {
        authorization.requireRead(principal, PlatformAuthorizationService.MEMBERSHIP_READ);
        if (afterId < 0) throw new IllegalArgumentException("Invalid cursor");
        groups.findById(groupId).orElseThrow(GroupService::denied);
        Instant now = Instant.now();
        return memberships.findHistory(groupId, afterId, PageRequest.of(0, 100)).stream().map(m -> entry(m, now)).toList();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Entry assign(CurrentIdentity principal, long groupId, long userId) {
        String permission = PlatformAuthorizationService.MEMBERSHIP_ASSIGN;
        var actor = authorization.requireCatalogWrite(principal, permission);
        if (actor.getId() == userId) throw GroupService.denied();
        var target = users.findForUpdate(userId).filter(u -> !"hyflo-bootstrap".equals(u.getUsername()))
                .orElseThrow(GroupService::denied);
        if (platformGrants.existsByUserId(userId)) throw GroupService.denied();
        if (!SessionAuthenticationService.eligible(target) || !target.getUserType().hasCode(UserType.HUMAN_CODE))
            throw new GroupService.GroupRejected();
        var group = groups.findForUpdate(groupId).orElseThrow(GroupService::denied);
        actor = authorization.requireCatalogWrite(principal, permission);
        Instant now = Instant.now().truncatedTo(ChronoUnit.MICROS);
        if (!group.isEnabled() || memberships.countNonEnded(groupId, userId, now) > 0
                || groupGrants.countNonEndedForGroup(groupId, now) > 0)
            throw new GroupService.GroupRejected();
        actor = authorization.requireCatalogWrite(principal, permission);
        return entry(memberships.save(new GroupMembership(target, group, now, null, actor)), now);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void revoke(CurrentIdentity principal, long groupId, long membershipId) {
        String permission = PlatformAuthorizationService.MEMBERSHIP_REVOKE;
        authorization.requireCatalogWrite(principal, permission);
        long userId = memberships.findMemberId(groupId, membershipId).orElseThrow(GroupService::denied);
        users.findForUpdate(userId).orElseThrow(GroupService::denied);
        groups.findForUpdate(groupId).orElseThrow(GroupService::denied);
        var membership = memberships.findSelectedForUpdate(groupId, membershipId).orElseThrow(GroupService::denied);
        var actor = authorization.requireCatalogWrite(principal, permission);
        Instant now = Instant.now().truncatedTo(ChronoUnit.MICROS);
        if (membership.getRevokedAt() != null || (membership.getValidTo() != null && !membership.getValidTo().isAfter(now)))
            throw new GroupService.GroupRejected();
        membership.revoke(now, actor);
    }

    private static Entry entry(GroupMembership membership, Instant now) {
        return new Entry(membership.getId(), membership.getGroup().getId(), membership.getUser().getId(),
                membership.getValidFrom(), membership.getValidTo(), membership.getRevokedAt(),
                membership.getGrantedBy().getId(), membership.getRevokedBy() == null ? null : membership.getRevokedBy().getId(),
                membership.getGroup().isEnabled() && membership.isActiveAt(now));
    }

    public record Entry(long id, long groupId, long userId, Instant validFrom, Instant validTo, Instant revokedAt,
            long grantedBy, Long revokedBy, boolean active) {}
}
