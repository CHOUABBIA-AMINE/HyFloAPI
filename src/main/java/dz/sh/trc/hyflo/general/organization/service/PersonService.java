/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: PersonService
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: General / Organization
 *
 **/

package dz.sh.trc.hyflo.general.organization.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.general.organization.dto.PersonDTO;
import dz.sh.trc.hyflo.general.organization.model.Person;
import dz.sh.trc.hyflo.general.organization.repository.PersonRepository;
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
    	return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(Person entity, PersonDTO dto) {
        dto.updateEntity(entity);
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
