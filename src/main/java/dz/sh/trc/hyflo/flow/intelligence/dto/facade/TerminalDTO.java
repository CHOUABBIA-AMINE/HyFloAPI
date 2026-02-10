/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: TerminalDTO
 * 	@CreatedOn	: 02-10-2026
 *
 * 	@Type		: DTO (Data Transfer Object)
 * 	@Layer		: DTO / Facade
 * 	@Package	: Flow / Intelligence
 *
 * 	@Purpose	: Lightweight DTO for Terminal basic information.
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.dto.facade;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Terminal DTO for facade layer data transfer.
 * 
 * Contains basic terminal information (departure/arrival points).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TerminalDTO {
    
    private Long id;
    private String code;
    private String name;
    
    /**
     * Terminal type designation (e.g., "Station de pompage", "Terminal maritime")
     */
    private String typeName;
}