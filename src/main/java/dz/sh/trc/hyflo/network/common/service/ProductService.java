/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ProductService
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: Network / Common
 *
 **/

package dz.sh.trc.hyflo.network.common.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.exception.BusinessValidationException;
import dz.sh.trc.hyflo.network.common.dto.ProductDTO;
import dz.sh.trc.hyflo.network.common.model.Product;
import dz.sh.trc.hyflo.network.common.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProductService extends GenericService<Product, ProductDTO, Long> {

    private final ProductRepository productRepository;

    @Override
    protected JpaRepository<Product, Long> getRepository() {
        return productRepository;
    }

    @Override
    protected String getEntityName() {
        return "Product";
    }

    @Override
    protected ProductDTO toDTO(Product entity) {
        return ProductDTO.fromEntity(entity);
    }

    @Override
    protected Product toEntity(ProductDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(Product entity, ProductDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public ProductDTO create(ProductDTO dto) {
        log.info("Creating product: code={}", dto.getCode());
        
        if (productRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException("Product with code '" + dto.getCode() + "' already exists");
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        log.info("Updating product with ID: {}", id);
        
        if (productRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new BusinessValidationException("Product with code '" + dto.getCode() + "' already exists");
        }
        
        return super.update(id, dto);
    }

    public List<ProductDTO> getAll() {
        log.debug("Getting all products without pagination");
        return productRepository.findAll().stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<ProductDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for products with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> productRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }
}
