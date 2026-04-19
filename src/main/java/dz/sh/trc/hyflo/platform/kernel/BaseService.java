/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: BaseService
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 04-19-2026
 *
 *	@Type		: interface
 *	@Layer		: Template
 *	@Package	: Configuration / Template
 *
 **/

package dz.sh.trc.hyflo.platform.kernel;

import java.util.List;

public interface BaseService<REQ, RES> {

    RES create(REQ request);

    RES update(Long id, REQ request);

    RES getById(Long id);

    List<RES> getAll();

    void delete(Long id);
}