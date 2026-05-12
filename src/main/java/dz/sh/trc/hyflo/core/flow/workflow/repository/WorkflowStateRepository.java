/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: WorkflowStateRepository
 * 	@CreatedOn	: 04-19-2026
 *
 * 	@Type		: Interface
 * 	@Layer		: Repository
 * 	@Package	: Flow / Workflow
 *
 **/

package dz.sh.trc.hyflo.core.flow.workflow.repository;

import dz.sh.trc.hyflo.core.flow.workflow.model.WorkflowState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkflowStateRepository extends JpaRepository<WorkflowState, Long> {

    Optional<WorkflowState> findByCode(String code);

    boolean existsByCode(String code);
}