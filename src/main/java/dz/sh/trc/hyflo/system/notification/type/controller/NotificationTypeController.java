/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : NotificationTypeController
 *  @CreatedOn  : 02-01-2026
 *  @UpdatedOn  : 02-16-2026
 *
 *  @Type       : Class
 *  @Layer      : Controller
 *  @Package    : System / Notification / Type
 *
 **/

package dz.sh.trc.hyflo.system.notification.type.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.system.notification.type.dto.NotificationTypeDTO;
import dz.sh.trc.hyflo.system.notification.type.service.NotificationTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * REST Controller for notification type configuration and management.
 * Defines notification categories, priorities, icons, and behavior rules.
 */
@Tag(
    name = "Notification Type Configuration",
    description = "API for managing notification type definitions, categories, priorities, and display configurations"
)
@SecurityRequirement(name = "bearer-auth")
@RestController
@RequestMapping("/system/notification/types")
@Slf4j
public class NotificationTypeController extends GenericController<NotificationTypeDTO, Long> {

    private final NotificationTypeService notificationTypeService;

    public NotificationTypeController(NotificationTypeService notificationTypeService) {
        super(notificationTypeService, "NotificationType");
        this.notificationTypeService = notificationTypeService;
    }

    @Operation(
        summary = "Get notification type by ID",
        description = "Retrieve a specific notification type configuration by its unique identifier."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notification type found", content = @Content(schema = @Schema(implementation = NotificationTypeDTO.class))),
        @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "404", description = "Notification type not found", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @Override
    @PreAuthorize("hasAuthority('NOTIFICATION:READ')")
    public ResponseEntity<NotificationTypeDTO> getById(
        @Parameter(description = "Notification type ID", required = true, example = "1") @PathVariable Long id
    ) {
        return super.getById(id);
    }

    @Operation(
        summary = "Get all notification types",
        description = "Retrieve paginated list of all notification type configurations."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notification types retrieved", content = @Content(schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @Override
    @PreAuthorize("hasAuthority('NOTIFICATION:READ')")
    public ResponseEntity<Page<NotificationTypeDTO>> getAll(
        @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
        @Parameter(description = "Sort field", example = "name") @RequestParam(defaultValue = "id") String sortBy,
        @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Operation(
        summary = "Get all active notification types",
        description = "Retrieve complete list of active notification types without pagination. Used for populating dropdowns and selection lists."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Active notification types retrieved", content = @Content(schema = @Schema(implementation = List.class))),
        @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/active")
    @PreAuthorize("hasAuthority('NOTIFICATION:READ')")
    public ResponseEntity<List<NotificationTypeDTO>> getActiveTypes() {
        log.debug("Getting active notification types");
        List<NotificationTypeDTO> types = notificationTypeService.getActiveTypes();
        return success(types);
    }

    @Operation(
        summary = "Create notification type",
        description = "Create a new notification type configuration. Defines category, priority, icon, and display properties."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Notification type created", content = @Content(schema = @Schema(implementation = NotificationTypeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid notification type data", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "409", description = "Notification type code already exists", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @Override
    @PreAuthorize("hasAuthority('NOTIFICATION:MANAGE')")
    public ResponseEntity<NotificationTypeDTO> create(@Valid @RequestBody NotificationTypeDTO dto) {
        return super.create(dto);
    }

    @Operation(
        summary = "Update notification type",
        description = "Update an existing notification type configuration. Changes affect new notifications created with this type."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notification type updated", content = @Content(schema = @Schema(implementation = NotificationTypeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid notification type data", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "404", description = "Notification type not found", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @Override
    @PreAuthorize("hasAuthority('NOTIFICATION:MANAGE')")
    public ResponseEntity<NotificationTypeDTO> update(@PathVariable Long id, @Valid @RequestBody NotificationTypeDTO dto) {
        return super.update(id, dto);
    }

    @Operation(
        summary = "Delete notification type",
        description = "Delete a notification type. WARNING: Cannot delete types that have existing notifications. Consider deactivating instead."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Notification type deleted", content = @Content),
        @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "404", description = "Notification type not found", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "409", description = "Cannot delete type with existing notifications", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @Override
    @PreAuthorize("hasAuthority('NOTIFICATION:MANAGE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }
}
