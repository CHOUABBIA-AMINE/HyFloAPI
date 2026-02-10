/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: AlloyDTO
 * 	@CreatedOn	: 02-10-2026
 *
 * 	@Type		: DTO (Data Transfer Object)
 * 	@Layer		: DTO / Facade
 * 	@Package	: Flow / Intelligence
 *
 * 	@Purpose	: Lightweight DTO for Alloy/Material information.
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.dto.facade;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Alloy/Material DTO for facade layer data transfer.
 * 
 * Contains construction material information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlloyDTO {
    
    private Long id;
    private String code;
    private String designation;
}