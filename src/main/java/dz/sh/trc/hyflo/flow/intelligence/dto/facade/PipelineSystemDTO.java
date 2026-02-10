/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: PipelineSystemDTO
 * 	@CreatedOn	: 02-10-2026
 *
 * 	@Type		: DTO (Data Transfer Object)
 * 	@Layer		: DTO / Facade
 * 	@Package	: Flow / Intelligence
 *
 * 	@Purpose	: Lightweight DTO for PipelineSystem classification.
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.dto.facade;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Pipeline System DTO for facade layer data transfer.
 * 
 * Contains pipeline system classification information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PipelineSystemDTO {
    
    private Long id;
    private String code;
    private String designation;
}