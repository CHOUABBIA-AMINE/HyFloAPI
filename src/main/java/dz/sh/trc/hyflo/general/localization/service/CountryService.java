/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: CountryService
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: General / Localization
 *
 **/

package dz.sh.trc.hyflo.general.localization.service;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.general.localization.dto.CountryDTO;
import dz.sh.trc.hyflo.general.localization.model.Country;
import dz.sh.trc.hyflo.general.localization.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CountryService extends GenericService<Country, CountryDTO, Long> {

    private final CountryRepository countryRepository;

    @Override
    protected JpaRepository<Country, Long> getRepository() {
        return countryRepository;
    }

    @Override
    protected String getEntityName() {
        return "Country";
    }

    @Override
    protected CountryDTO toDTO(Country entity) {
        return CountryDTO.fromEntity(entity);
    }

    @Override
    protected Country toEntity(CountryDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(Country entity, CountryDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public CountryDTO create(CountryDTO dto) {
        log.info("Creating country: code={}, designationFr={}", dto.getCode(), dto.getDesignationFr());
        return super.create(dto);
    }

    @Override
    @Transactional
    public CountryDTO update(Long id, CountryDTO dto) {
        log.info("Updating country with ID: {}", id);
        return super.update(id, dto);
    }

    public List<CountryDTO> getAll() {
        log.debug("Getting all countries without pagination");
        return countryRepository.findAll().stream()
                .map(CountryDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<CountryDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for countries with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}
