# IAAS Exception Handling - Implementation Guide

**Author:** CHOUABBIA Amine  
**Date:** 2025-12-20  
**Version:** 1.0

## Table of Contents

1. [Overview](#overview)
2. [Exception Classes](#exception-classes)
3. [Global Exception Handler](#global-exception-handler)
4. [Usage Examples](#usage-examples)
5. [Best Practices](#best-practices)
6. [Testing Guidelines](#testing-guidelines)

---

## Overview

This package provides comprehensive exception handling for the IAAS application with:

- **9 custom exception classes** for specific error scenarios
- **Enhanced GlobalExceptionHandler** handling 15+ exception types
- **Consistent error response format** across all endpoints
- **Proper HTTP status codes** for each error scenario
- **Detailed logging** at appropriate levels

---

## Exception Classes

### 1. Database Exceptions

#### DatabaseOperationException
**Use:** When database operations fail  
**Status:** 500 INTERNAL_SERVER_ERROR  
**Example:**
```java
throw new DatabaseOperationException("save", "Connection timeout");
```

#### DataIntegrityViolationException
**Use:** When constraints are violated (unique, foreign key, not null)  
**Status:** 409 CONFLICT  
**Example:**
```java
throw new DataIntegrityViolationException("shortName", "Already exists");
```

#### OptimisticLockException
**Use:** When concurrent modification conflicts occur  
**Status:** 409 CONFLICT  
**Example:**
```java
throw new OptimisticLockException("Partner", partnerId);
```

### 2. External Service Exceptions

#### ExternalServiceException
**Use:** When external API/service calls fail  
**Status:** 502 BAD_GATEWAY  
**Example:**
```java
throw new ExternalServiceException("PaymentAPI", "Connection refused");
```

### 3. Security Exceptions

#### AuthenticationException
**Use:** When authentication fails  
**Status:** 401 UNAUTHORIZED  
**Examples:**
```java
throw AuthenticationException.invalidCredentials();
throw AuthenticationException.tokenExpired();
throw AuthenticationException.tokenInvalid();
```

#### AuthorizationException
**Use:** When user lacks required permissions  
**Status:** 403 FORBIDDEN  
**Examples:**
```java
throw new AuthorizationException("Partner", "delete");
throw AuthorizationException.insufficientPermissions();
```

### 4. Request/Validation Exceptions

#### RequestTimeoutException
**Use:** When operations timeout  
**Status:** 408 REQUEST_TIMEOUT  
**Example:**
```java
throw new RequestTimeoutException("data export", 30000);
```

#### InvalidInputException
**Use:** When input is semantically invalid  
**Status:** 400 BAD_REQUEST  
**Example:**
```java
throw new InvalidInputException("endDate", "Cannot be before startDate");
```

#### RateLimitExceededException
**Use:** When API rate limits are exceeded  
**Status:** 429 TOO_MANY_REQUESTS  
**Example:**
```java
throw new RateLimitExceededException(60); // retry after 60 seconds
```

### 5. Existing Exceptions

- **ResourceNotFoundException** - Entity not found (404)
- **BusinessValidationException** - Business rule violations (400)
- **ConsultationException** - Consultation operations (varies)
- **FileOperationException** - File operations (varies)

---

## Global Exception Handler

The `GlobalExceptionHandler` provides centralized exception handling for:

### Handled Exception Types

✅ Custom IAAS Exceptions  
✅ Validation Errors (@Valid, ConstraintViolation)  
✅ Database Exceptions (DataIntegrity, OptimisticLock, DataAccess)  
✅ Security Exceptions (Authentication, Authorization)  
✅ HTTP/Request Exceptions (MethodNotSupported, MediaType, MalformedJSON)  
✅ File Upload Exceptions  
✅ Timeout Exceptions  
✅ Type Mismatch & Missing Parameters  
✅ 404 Not Found  
✅ Generic fallback handler  

### Error Response Format

All exceptions return a consistent JSON structure:

```json
{
  "errorCode": "RESOURCE_NOT_FOUND",
  "message": "Partner not found with ID: 123",
  "path": "/api/partners/123",
  "timestamp": "2025-12-20T16:45:30.123",
  "details": {
    "context": ["additional", "data"]
  }
}
```

---

## Usage Examples

### Service Layer

#### Example 1: Business Validation

```java
@Service
@RequiredArgsConstructor
public class PartnerService extends GenericService<Partner, PartnerDTO, Long> {

    private final PartnerRepository partnerRepository;
    
    @Override
    @Transactional
    public PartnerDTO create(PartnerDTO dto) {
        // Business validation
        if (partnerRepository.existsByShortName(dto.getShortName())) {
            throw new BusinessValidationException(
                "Partner with short name '" + dto.getShortName() + "' already exists"
            );
        }
        
        // Parent class handles database exceptions
        return super.create(dto);
    }
}
```

#### Example 2: External Service Integration

```java
@Service
public class PaymentService {
    
    public PaymentResponse processPayment(PaymentRequest request) {
        try {
            return externalPaymentAPI.charge(request);
        } catch (IOException ex) {
            throw new ExternalServiceException(
                "PaymentAPI", 
                "Failed to process payment: " + ex.getMessage(),
                ex
            );
        } catch (TimeoutException ex) {
            throw new RequestTimeoutException("payment processing", 5000);
        }
    }
}
```

#### Example 3: Input Validation

```java
@Service
public class ReportService {
    
    public Report generateReport(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new InvalidInputException(
                "endDate", 
                "End date cannot be before start date"
            );
        }
        
        // Generate report
    }
}
```

### Controller Layer

**No try-catch needed!** GlobalExceptionHandler handles everything:

```java
@RestController
@RequestMapping("/api/partners")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;
    
    @GetMapping("/{id}")
    public ResponseEntity<PartnerDTO> getById(@PathVariable Long id) {
        // No try-catch needed - exceptions handled globally
        return ResponseEntity.ok(partnerService.getById(id));
    }
    
    @PostMapping
    public ResponseEntity<PartnerDTO> create(@Valid @RequestBody PartnerDTO dto) {
        // Validation errors handled automatically
        // Database errors handled automatically
        PartnerDTO created = partnerService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        // ResourceNotFoundException handled automatically
        // DataIntegrityViolation handled automatically
        partnerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```

---

## Best Practices

### When to Use Each Exception

#### ResourceNotFoundException
✓ Entity not found by ID  
✓ Required relationship not found  
✗ Don't use for business logic failures  

#### BusinessValidationException
✓ Business rule violations  
✓ Invalid state transitions  
✓ Custom validation failures  
✗ Don't use for data format issues  

#### DataIntegrityViolationException
✓ Unique constraint violations  
✓ Foreign key violations  
✓ Not null violations  
✗ Don't throw manually - auto-translated from database  

#### InvalidInputException
✓ Semantic validation (e.g., endDate < startDate)  
✓ Invalid combinations  
✓ Format issues not caught by @Valid  
✗ Don't use for missing required fields (use @Valid)  

### Logging Best Practices

**ERROR level:**
- Database errors
- External service failures
- Unexpected exceptions
- Data corruption

**WARN level:**
- Business validation failures
- Optimistic lock failures
- Authentication failures
- Expected but exceptional conditions

**INFO level:**
- Successful operations
- State changes
- Important business events

**DEBUG level:**
- Query execution
- Method entry/exit
- Detailed processing steps

### Controller Best Practices

❌ **DON'T do this:**
```java
@GetMapping("/{id}")
public ResponseEntity<?> getById(@PathVariable Long id) {
    try {
        PartnerDTO partner = partnerService.getById(id);
        return ResponseEntity.ok(partner);
    } catch (ResourceNotFoundException ex) {
        return ResponseEntity.notFound().build();
    } catch (Exception ex) {
        return ResponseEntity.status(500).build();
    }
}
```

✅ **DO this instead:**
```java
@GetMapping("/{id}")
public ResponseEntity<PartnerDTO> getById(@PathVariable Long id) {
    // Let GlobalExceptionHandler handle all exceptions
    return ResponseEntity.ok(partnerService.getById(id));
}
```

### Service Best Practices

✅ **Throw specific exceptions:**
```java
if (partnerRepository.existsByShortName(shortName)) {
    throw new BusinessValidationException(
        "Partner with short name '" + shortName + "' already exists"
    );
}
```

✅ **Let database exceptions propagate:**
```java
// GenericService already handles database exceptions
return super.create(dto);
```

✅ **Wrap external service failures:**
```java
try {
    return externalService.call();
} catch (IOException ex) {
    throw new ExternalServiceException("ServiceName", ex.getMessage(), ex);
}
```

---

## Testing Guidelines

### Unit Tests

#### Test Exception Throwing

```java
@Test
void create_withDuplicateShortName_shouldThrowBusinessValidationException() {
    // Given
    PartnerDTO dto = new PartnerDTO();
    dto.setShortName("SONATRACH");
    
    when(partnerRepository.existsByShortName("SONATRACH")).thenReturn(true);
    
    // When & Then
    assertThrows(BusinessValidationException.class, () -> {
        partnerService.create(dto);
    });
    
    verify(partnerRepository, never()).save(any());
}
```

#### Test Exception Message

```java
@Test
void getById_withNonExistentId_shouldThrowResourceNotFoundException() {
    // Given
    Long nonExistentId = 999L;
    when(partnerRepository.findById(nonExistentId)).thenReturn(Optional.empty());
    
    // When & Then
    ResourceNotFoundException exception = assertThrows(
        ResourceNotFoundException.class,
        () -> partnerService.getById(nonExistentId)
    );
    
    assertThat(exception.getMessage()).contains("Partner not found with ID: 999");
}
```

### Integration Tests

#### Test Global Exception Handler

```java
@SpringBootTest
@AutoConfigureMockMvc
class PartnerControllerExceptionTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void getById_withNonExistentId_shouldReturn404() throws Exception {
        mockMvc.perform(get("/api/partners/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.errorCode").value("RESOURCE_NOT_FOUND"))
            .andExpect(jsonPath("$.message").value(containsString("not found")))
            .andExpect(jsonPath("$.timestamp").exists());
    }
    
    @Test
    void create_withInvalidData_shouldReturn400() throws Exception {
        mockMvc.perform(post("/api/partners")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
            .andExpect(jsonPath("$.details.fieldErrors").exists());
    }
}
```

---

## Summary

### Key Benefits

✅ **Consistent error responses** across all endpoints  
✅ **User-friendly error messages** for better UX  
✅ **Developer-friendly debugging** with proper logging  
✅ **No boilerplate** try-catch in controllers  
✅ **Easy to test** with clear exception contracts  
✅ **Proper HTTP status codes** for each scenario  
✅ **Backward compatible** with existing code  

### Quick Reference

| Exception | HTTP Status | Use Case |
|-----------|-------------|----------|
| ResourceNotFoundException | 404 | Entity not found |
| BusinessValidationException | 400 | Business rule violation |
| DataIntegrityViolationException | 409 | Constraint violation |
| OptimisticLockException | 409 | Concurrent modification |
| InvalidInputException | 400 | Semantic validation error |
| AuthenticationException | 401 | Authentication failure |
| AuthorizationException | 403 | Permission denied |
| ExternalServiceException | 502 | External service failure |
| RequestTimeoutException | 408 | Operation timeout |
| RateLimitExceededException | 429 | Rate limit exceeded |
| DatabaseOperationException | 500 | Database error |

---

## Questions or Issues?

For questions about implementation:
- Technical Lead: CHOUABBIA Amine
- Documentation: This guide
- Code examples: See [Usage Examples](#usage-examples)

**Remember:** Consistent exception handling improves user experience, debugging, API reliability, code maintainability, and testing!
