/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : NotificationTypeService
 *  @CreatedOn  : 02-01-2026
 *  @UpdatedOn  : 02-01-2026
 *
 *  @Type       : Class
 *  @Layer      : Service
 *  @Package    : System / Notification / Type
 *
 **/

package dz.sh.trc.hyflo.system.notification.type.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.system.notification.type.dto.NotificationTypeDTO;
import dz.sh.trc.hyflo.system.notification.type.model.NotificationType;
import dz.sh.trc.hyflo.system.notification.type.repository.NotificationTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for NotificationType management
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class NotificationTypeService extends GenericService<NotificationType, NotificationTypeDTO, Long> {

    private final NotificationTypeRepository notificationTypeRepository;

    @Override
    protected JpaRepository<NotificationType, Long> getRepository() {
        return notificationTypeRepository;
    }

    @Override
    protected NotificationTypeDTO toDTO(NotificationType entity) {
        return NotificationTypeDTO.fromEntity(entity);
    }

    @Override
    protected NotificationType toEntity(NotificationTypeDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(NotificationType entity, NotificationTypeDTO dto) {
        dto.updateEntity(entity);
    }

    @Cacheable(value = "notificationTypes", key = "'active'")
    @Transactional(readOnly = true)
    public List<NotificationTypeDTO> getAllActive() {
        log.debug("Fetching all active notification types");
        return notificationTypeRepository.findByActiveTrue().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "notificationTypes", key = "#code")
    @Transactional(readOnly = true)
    public NotificationTypeDTO getByCode(String code) {
        log.debug("Fetching notification type by code: {}", code);
        NotificationType type = notificationTypeRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "NotificationType not found with code: " + code
                ));
        return toDTO(type);
    }

    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        log.debug("Checking if notification type exists with code: {}", code);
        return notificationTypeRepository.existsByCode(code);
    }

    @Override
    @CacheEvict(value = "notificationTypes", allEntries = true)
    @Transactional
    public NotificationTypeDTO create(NotificationTypeDTO dto) {
        log.info("Creating new NotificationType with code: {}", dto.getCode());
        if (dto.getCode() != null && notificationTypeRepository.existsByCode(dto.getCode())) {
            throw new IllegalArgumentException(
                    "NotificationType with code '" + dto.getCode() + "' already exists"
            );
        }
        return super.create(dto);
    }

    @Override
    @CacheEvict(value = "notificationTypes", allEntries = true)
    @Transactional
    public NotificationTypeDTO update(Long id, NotificationTypeDTO dto) {
        log.info("Updating NotificationType with ID: {}", id);
        if (dto.getCode() != null) {
            notificationTypeRepository.findByCode(dto.getCode())
                    .ifPresent(existing -> {
                        if (!existing.getId().equals(id)) {
                            throw new IllegalArgumentException(
                                    "NotificationType with code '" + dto.getCode() + "' already exists"
                            );
                        }
                    });
        }
        return super.update(id, dto);
    }

    @Override
    @CacheEvict(value = "notificationTypes", allEntries = true)
    @Transactional
    public void delete(Long id) {
        log.info("Deleting NotificationType with ID: {}", id);
        super.delete(id);
    }

    @Transactional(readOnly = true)
    public Page<NotificationTypeDTO> globalSearch(String query, Pageable pageable) {
        log.debug("Performing global search with query: {}", query);
        if (query == null || query.trim().isEmpty()) {
            return getAll(pageable);
        }
        String searchTerm = "%" + query + "%";
        Page<NotificationType> results = notificationTypeRepository.globalSearch(searchTerm, pageable);
        return results.map(this::toDTO);
    }

    @Override
    protected String getEntityName() {
        return "NotificationType";
    }
}
