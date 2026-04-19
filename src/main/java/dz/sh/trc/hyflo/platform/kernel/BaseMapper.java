/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: BaseMapper
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 04-19-2026
 *
 *	@Type		: Interface
 *	@Layer		: Template
 *	@Package	: Platform / kernel
 *
 **/

package dz.sh.trc.hyflo.platform.kernel;

public interface BaseMapper<E, REQ, RES> {

    E toEntity(REQ dto);

    RES toResponse(E entity);

    void updateEntity(REQ dto, E entity);
}