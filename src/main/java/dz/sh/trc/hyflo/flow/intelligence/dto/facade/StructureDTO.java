/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: StructureDTO
 * 	@CreatedOn	: 02-10-2026
 *
 * 	@Type		: DTO (Data Transfer Object)
 * 	@Layer		: DTO / Facade
 * 	@Package	: Flow / Intelligence
 *
 * 	@Purpose	: Lightweight DTO for organizational Structure information.
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.dto.facade;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Structure/Organization DTO for facade layer data transfer.
 * 
 * Contains organizational structure information (manager).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StructureDTO {
    
    private Long id;
    private String code;
    private String designation;
}