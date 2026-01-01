/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ProviderDTO
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Business / Provider
 *
 **/

package dz.sh.trc.hyflo.business.provider.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.business.provider.model.Provider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Provider Data Transfer Object
 * Extends GenericDTO for automatic entity conversion
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProviderDTO extends GenericDTO<Provider> {

    @NotBlank(message = "Latin designation is required")
    private String designationLt;
    
    private String designationAr;
    
    private String acronymLt;
    
    private String acronymAr;
    
    private String address;
    
    private double capital;
    
    private String comercialRegistryNumber;
    
    private Date comercialRegistryDate;
    
    @NotBlank(message = "Tax identity number is required")
    private String taxeIdentityNumber;
    
    private String statIdentityNumber;
    
    private String bank;
    
    private String bankAccount;
    
    private String swiftNumber;
    
    private String phoneNumbers;
    
    private String faxNumbers;
    
    private String mail;
    
    private String website;
    
    private Long logoId;
    
    @NotNull(message = "Economic nature is required")
    private Long economicNatureId;
    
    @NotNull(message = "Country is required")
    private Long countryId;
    
    private Long stateId;

    @Override
    public Provider toEntity() {
        Provider entity = new Provider();
        entity.setId(this.getId());
        entity.setDesignationLt(this.designationLt);
        entity.setDesignationAr(this.designationAr);
        entity.setAcronymLt(this.acronymLt);
        entity.setAcronymAr(this.acronymAr);
        entity.setAddress(this.address);
        entity.setCapital(this.capital);
        entity.setComercialRegistryNumber(this.comercialRegistryNumber);
        entity.setComercialRegistryDate(this.comercialRegistryDate);
        entity.setTaxeIdentityNumber(this.taxeIdentityNumber);
        entity.setStatIdentityNumber(this.statIdentityNumber);
        entity.setBank(this.bank);
        entity.setBankAccount(this.bankAccount);
        entity.setSwiftNumber(this.swiftNumber);
        entity.setPhoneNumbers(this.phoneNumbers);
        entity.setFaxNumbers(this.faxNumbers);
        entity.setMail(this.mail);
        entity.setWebsite(this.website);
        return entity;
    }

    @Override
    public void updateEntity(Provider entity) {
        if (this.designationLt != null) entity.setDesignationLt(this.designationLt);
        if (this.designationAr != null) entity.setDesignationAr(this.designationAr);
        if (this.acronymLt != null) entity.setAcronymLt(this.acronymLt);
        if (this.acronymAr != null) entity.setAcronymAr(this.acronymAr);
        if (this.address != null) entity.setAddress(this.address);
        entity.setCapital(this.capital);
        if (this.comercialRegistryNumber != null) entity.setComercialRegistryNumber(this.comercialRegistryNumber);
        if (this.comercialRegistryDate != null) entity.setComercialRegistryDate(this.comercialRegistryDate);
        if (this.taxeIdentityNumber != null) entity.setTaxeIdentityNumber(this.taxeIdentityNumber);
        if (this.statIdentityNumber != null) entity.setStatIdentityNumber(this.statIdentityNumber);
        if (this.bank != null) entity.setBank(this.bank);
        if (this.bankAccount != null) entity.setBankAccount(this.bankAccount);
        if (this.swiftNumber != null) entity.setSwiftNumber(this.swiftNumber);
        if (this.phoneNumbers != null) entity.setPhoneNumbers(this.phoneNumbers);
        if (this.faxNumbers != null) entity.setFaxNumbers(this.faxNumbers);
        if (this.mail != null) entity.setMail(this.mail);
        if (this.website != null) entity.setWebsite(this.website);
    }

    public static ProviderDTO fromEntity(Provider entity) {
        if (entity == null) return null;
        
        return ProviderDTO.builder()
                .id(entity.getId())
                .designationLt(entity.getDesignationLt())
                .designationAr(entity.getDesignationAr())
                .acronymLt(entity.getAcronymLt())
                .acronymAr(entity.getAcronymAr())
                .address(entity.getAddress())
                .capital(entity.getCapital())
                .comercialRegistryNumber(entity.getComercialRegistryNumber())
                .comercialRegistryDate(entity.getComercialRegistryDate())
                .taxeIdentityNumber(entity.getTaxeIdentityNumber())
                .statIdentityNumber(entity.getStatIdentityNumber())
                .bank(entity.getBank())
                .bankAccount(entity.getBankAccount())
                .swiftNumber(entity.getSwiftNumber())
                .phoneNumbers(entity.getPhoneNumbers())
                .faxNumbers(entity.getFaxNumbers())
                .mail(entity.getMail())
                .website(entity.getWebsite())
                //.logoId(entity.getLogo() != null ? entity.getLogo().getId() : null)
                .economicNatureId(entity.getEconomicNature() != null ? entity.getEconomicNature().getId() : null)
                .countryId(entity.getCountry() != null ? entity.getCountry().getId() : null)
                .stateId(entity.getState() != null ? entity.getState().getId() : null)
                .build();
    }
}
