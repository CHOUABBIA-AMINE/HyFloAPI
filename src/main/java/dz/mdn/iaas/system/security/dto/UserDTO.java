/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: UserDTO
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
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String password;
    private boolean enabled;
    private Set<RoleDTO> roles;
    private Set<GroupDTO> groups;
}
