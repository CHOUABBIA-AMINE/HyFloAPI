/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : GroupAdministrationController
 * @CreatedOn   : 11-26-2026
 * @UpdatedOn   : 11-26-2026
 *
 * @Type        : Class
 * @Layer       : Controller
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.api.controller
 *
 * @Description : GroupAdministrationController for group and membership administration.
 */

package dz.sh.hyflo.modules.foundation.identity.api.controller;

import java.sql.SQLException;
import java.util.UUID;
import java.util.function.Supplier;
import dz.sh.hyflo.modules.foundation.identity.api.request.*;
import dz.sh.hyflo.modules.foundation.identity.api.response.GroupResponse;
import dz.sh.hyflo.modules.foundation.identity.api.response.GroupMembershipResponse;
import dz.sh.hyflo.modules.foundation.identity.application.service.GroupService;
import dz.sh.hyflo.modules.foundation.identity.application.service.GroupMembershipService;
import dz.sh.hyflo.modules.foundation.identity.application.service.SessionAuthenticationService.CurrentIdentity;
import dz.sh.hyflo.platform.exception.AuthenticationProblems;
import jakarta.validation.Valid;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/platform/groups")
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class GroupAdministrationController {
    private final GroupService groups;
    private final GroupMembershipService memberships;

    public GroupAdministrationController(GroupService groups, GroupMembershipService memberships) {
        this.groups = groups;
        this.memberships = memberships;
    }

    @GetMapping
    public ResponseEntity<?> list(@AuthenticationPrincipal CurrentIdentity principal,
            @RequestParam(name = "afterId", defaultValue = "0") String afterId) {
        return execute(HttpStatus.OK, () -> groups.list(principal, identifier(afterId)).stream()
                .map(GroupAdministrationController::response).toList(), false);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<?> read(@AuthenticationPrincipal CurrentIdentity principal, @PathVariable("groupId") String groupId) {
        return execute(HttpStatus.OK, () -> response(groups.read(principal, positive(groupId))), false);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@AuthenticationPrincipal CurrentIdentity principal, @Valid @RequestBody CreateGroupRequest request) {
        return execute(HttpStatus.CREATED, () -> response(groups.create(principal, request.code(), request.displayName(), request.reason())), true);
    }

    @PostMapping(path = "/{groupId}/rename", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> rename(@AuthenticationPrincipal CurrentIdentity principal, @PathVariable("groupId") String groupId,
            @Valid @RequestBody RenameGroupRequest request) {
        return execute(HttpStatus.OK, () -> response(groups.rename(principal, positive(groupId), request.displayName(), request.reason())), false);
    }

    @PostMapping(path = "/{groupId}/retire", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> retire(@AuthenticationPrincipal CurrentIdentity principal, @PathVariable("groupId") String groupId,
            @Valid @RequestBody UserLifecycleRequest request) {
        return execute(HttpStatus.OK, () -> response(groups.retire(principal, positive(groupId), request.reason())), false);
    }

    @GetMapping("/{groupId}/memberships")
    public ResponseEntity<?> listMemberships(@AuthenticationPrincipal CurrentIdentity principal, @PathVariable("groupId") String groupId,
            @RequestParam(name = "afterId", defaultValue = "0") String afterId) {
        return execute(HttpStatus.OK, () -> memberships.list(principal, positive(groupId), identifier(afterId)).stream()
                .map(GroupAdministrationController::membershipResponse).toList(), false);
    }

    @PostMapping(path = "/{groupId}/memberships", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> assign(@AuthenticationPrincipal CurrentIdentity principal, @PathVariable("groupId") String groupId,
            @Valid @RequestBody AddGroupMemberRequest request) {
        return execute(HttpStatus.CREATED, () -> membershipResponse(memberships.assign(principal, positive(groupId), request.userId())), false);
    }

    @PostMapping("/{groupId}/memberships/{membershipId}/revoke")
    public ResponseEntity<?> revoke(@AuthenticationPrincipal CurrentIdentity principal, @PathVariable("groupId") String groupId,
            @PathVariable("membershipId") String membershipId) {
        return execute(HttpStatus.NO_CONTENT, () -> {
            memberships.revoke(principal, positive(groupId), positive(membershipId));
            return null;
        }, false);
    }

    private static GroupResponse response(GroupService.Entry group) {
        return new GroupResponse(group.id(), group.code(), group.displayName(), group.enabled());
    }

    private static GroupMembershipResponse membershipResponse(GroupMembershipService.Entry member) {
        return new GroupMembershipResponse(member.id(), member.groupId(), member.userId(), member.validFrom(), member.validTo(),
                member.revokedAt(), member.grantedBy(), member.revokedBy(), member.active());
    }

    private static long identifier(String value) {
        if (value == null || !value.matches("[0-9]{1,19}")) throw new IllegalArgumentException("Invalid identifier");
        return Long.parseLong(value);
    }

    private static long positive(String value) {
        long id = identifier(value);
        if (id <= 0) throw new IllegalArgumentException("Invalid identifier");
        return id;
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<?> malformed() { return problem(HttpStatus.BAD_REQUEST); }

    private static ResponseEntity<?> execute(HttpStatus status, Supplier<?> command, boolean create) {
        try {
            return ResponseEntity.status(status).header("Cache-Control", "no-store").body(command.get());
        } catch (AuthenticationException e) {
            return problem(HttpStatus.UNAUTHORIZED);
        } catch (AccessDeniedException e) {
            return problem(HttpStatus.FORBIDDEN);
        } catch (IllegalArgumentException e) {
            return problem(HttpStatus.BAD_REQUEST);
        } catch (GroupService.GroupRejected e) {
            return problem(HttpStatus.CONFLICT);
        } catch (RuntimeException e) {
            return problem(create && duplicate(e) ? HttpStatus.CONFLICT : HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    private static boolean duplicate(Throwable failure) {
        for (Throwable cause = failure; cause != null && cause != cause.getCause(); cause = cause.getCause()) {
            if (cause instanceof SQLException sql && sql.getErrorCode() == 1062
                    && sql.getMessage() != null && sql.getMessage().contains("T_10_01_03_UK_01")) return true;
        }
        return false;
    }

    private static ResponseEntity<?> problem(HttpStatus status) {
        String correlation = UUID.randomUUID().toString();
        ProblemDetail body = status == HttpStatus.CONFLICT
                ? ProblemDetail.forStatusAndDetail(status, "Group operation rejected")
                : AuthenticationProblems.problem(status, correlation);
        if (status == HttpStatus.CONFLICT) {
            body.setProperty("code", "GROUP_OPERATION_REJECTED");
            body.setProperty("correlationId", correlation);
        }
        var response = ResponseEntity.status(status).header("Cache-Control", "no-store")
                .contentType(MediaType.APPLICATION_PROBLEM_JSON);
        if (status == HttpStatus.UNAUTHORIZED) response.header("WWW-Authenticate", "Bearer");
        return response.body(body);
    }
}
