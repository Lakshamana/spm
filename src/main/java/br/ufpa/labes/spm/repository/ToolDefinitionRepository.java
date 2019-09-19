package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.tools.IToolDefinitionDAO;


import br.ufpa.labes.spm.domain.ToolDefinition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the ToolDefinition entity.
 */
@Repository
public interface ToolDefinitionRepository extends IToolDefinitionDAO, JpaRepository<ToolDefinition, Long> {

    @Query(value = "select distinct toolDefinition from ToolDefinition toolDefinition left join fetch toolDefinition.theArtifactTypes",
        countQuery = "select count(distinct toolDefinition) from ToolDefinition toolDefinition")
    Page<ToolDefinition> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct toolDefinition from ToolDefinition toolDefinition left join fetch toolDefinition.theArtifactTypes")
    List<ToolDefinition> findAllWithEagerRelationships();

    @Query("select toolDefinition from ToolDefinition toolDefinition left join fetch toolDefinition.theArtifactTypes where toolDefinition.id =:id")
    Optional<ToolDefinition> findOneWithEagerRelationships(@Param("id") Long id);

}
