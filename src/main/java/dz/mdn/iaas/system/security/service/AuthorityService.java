/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: AuthorityService
 *	@CreatedOn	: 11-18-2025
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: System / Security
 *
 **/

package dz.mdn.iaas.system.security.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.mdn.iaas.system.security.dto.AuthorityDTO;
import dz.mdn.iaas.system.security.model.Authority;
import dz.mdn.iaas.system.security.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorityService {

    private final AuthorityRepository authorityRepository;

    public List<AuthorityDTO> findAll() {
        return authorityRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public AuthorityDTO findById(Long id) {
        return authorityRepository.findById(id)
            .map(this::convertToDTO)
            .orElseThrow(() -> new RuntimeException("Authority not found"));
    }

    @Transactional
    public AuthorityDTO create(AuthorityDTO dto) {
        Authority authority = new Authority();
        authority.setName(dto.getName());
        authority.setDescription(dto.getDescription());
        return convertToDTO(authorityRepository.save(authority));
    }

    @Transactional
    public AuthorityDTO update(Long id, AuthorityDTO dto) {
        Authority authority = authorityRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Authority not found"));
        authority.setName(dto.getName());
        authority.setDescription(dto.getDescription());
        return convertToDTO(authorityRepository.save(authority));
    }

    @Transactional
    public void delete(Long id) {
        authorityRepository.deleteById(id);
    }

    private AuthorityDTO convertToDTO(Authority authority) {
        return AuthorityDTO.builder()
            .id(authority.getId())
            .name(authority.getName())
            .description(authority.getDescription())
            .build();
    }
}
