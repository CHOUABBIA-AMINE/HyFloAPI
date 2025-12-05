/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: GroupDTO
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
public class GroupDTO {
    private Long id;
    private String name;
    private String description;
    private Set<RoleDTO> roles;
}
