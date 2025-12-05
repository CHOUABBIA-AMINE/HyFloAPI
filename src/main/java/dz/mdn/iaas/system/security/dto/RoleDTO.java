/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: RoleDTO
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
import java.util.Set;

@Data
@Builder
public class RoleDTO {
    private Long id;
    private String name;
    private String description;
    private Set<PermissionDTO> permissions;
}
