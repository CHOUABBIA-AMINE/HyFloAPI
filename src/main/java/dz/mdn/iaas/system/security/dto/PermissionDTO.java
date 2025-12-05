/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: PermissionDTO
 *	@CreatedOn	: 11-18-2025
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: System / Security
 *
 **/

package dz.mdn.iaas.system.security.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PermissionDTO {
    private Long id;
    private String name;
    private String description;
}
