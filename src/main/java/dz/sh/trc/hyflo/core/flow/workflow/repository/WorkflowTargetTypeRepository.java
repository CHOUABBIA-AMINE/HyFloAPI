/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: WorkflowTargetTypeRepository
 * 	@CreatedOn	: 04-19-2026
 *
 * 	@Type		: Interface
 * 	@Layer		: Repository
 * 	@Package	: Flow / Workflow
 *
 **/

package dz.sh.trc.hyflo.core.flow.workflow.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.core.flow.workflow.model.WorkflowTargetType;

@Repository
public interface WorkflowTargetTypeRepository extends JpaRepository<WorkflowTargetType, Long> {

    Optional<WorkflowTargetType> findByCode(String code);
}