package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.WorkGroupInstSug;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the WorkGroupInstSug entity.
 */
@Repository
public interface WorkGroupInstSugRepository extends JpaRepository<WorkGroupInstSug, Long> {

    @Query(value = "select distinct workGroupInstSug from WorkGroupInstSug workGroupInstSug left join fetch workGroupInstSug.sugWorkGroups",
        countQuery = "select count(distinct workGroupInstSug) from WorkGroupInstSug workGroupInstSug")
    Page<WorkGroupInstSug> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct workGroupInstSug from WorkGroupInstSug workGroupInstSug left join fetch workGroupInstSug.sugWorkGroups")
    List<WorkGroupInstSug> findAllWithEagerRelationships();

    @Query("select workGroupInstSug from WorkGroupInstSug workGroupInstSug left join fetch workGroupInstSug.sugWorkGroups where workGroupInstSug.id =:id")
    Optional<WorkGroupInstSug> findOneWithEagerRelationships(@Param("id") Long id);

}
