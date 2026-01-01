/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: PersonService
 *	@CreatedOn	: 10-14-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Service
 *	@Layer		: Common / Administration
 *	@Package	: Common / Administration / Service
 *
 **/

package dz.mdn.iaas.common.administration.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.mdn.iaas.common.administration.dto.PersonDTO;
import dz.mdn.iaas.common.administration.model.Country;
import dz.mdn.iaas.common.administration.model.Person;
import dz.mdn.iaas.common.administration.repository.PersonRepository;
import dz.mdn.iaas.configuration.template.GenericService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Person Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PersonService extends GenericService<Person, PersonDTO, Long> {

    private final PersonRepository personRepository;
    private final CountryService countryService;

    @Override
    protected JpaRepository<Person, Long> getRepository() {
        return personRepository;
    }

    @Override
    protected String getEntityName() {
        return "Person";
    }

    @Override
    protected PersonDTO toDTO(Person entity) {
        return PersonDTO.fromEntity(entity);
    }

    @Override
    protected Person toEntity(PersonDTO dto) {
        Person entity = dto.toEntity();
        
        // Set relationships
        if (dto.getCountryId() != null) {
            Country country = countryService.getEntityById(dto.getCountryId());
            entity.setCountry(country);
        }
        
        return entity;
    }

    @Override
    protected void updateEntityFromDTO(Person entity, PersonDTO dto) {
        dto.updateEntity(entity);
        
        // Update relationships
        if (dto.getCountryId() != null) {
            Country country = countryService.getEntityById(dto.getCountryId());
            entity.setCountry(country);
        }
    }

    @Override
    @Transactional
    public PersonDTO create(PersonDTO dto) {
        log.info("Creating person: lastNameLt={}, firstNameLt={}", 
                dto.getLastNameLt(), dto.getFirstNameLt());
        return super.create(dto);
    }

    @Override
    @Transactional
    public PersonDTO update(Long id, PersonDTO dto) {
        log.info("Updating person with ID: {}", id);
        return super.update(id, dto);
    }

    public List<PersonDTO> getAll() {
        log.debug("Getting all persons without pagination");
        return personRepository.findAll().stream()
                .map(PersonDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<PersonDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for persons with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}
