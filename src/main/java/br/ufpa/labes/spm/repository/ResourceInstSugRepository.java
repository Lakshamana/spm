package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.ResourceInstSug;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the ResourceInstSug entity.
 */
@Repository
public interface ResourceInstSugRepository extends JpaRepository<ResourceInstSug, Long> {

    @Query(value = "select distinct resourceInstSug from ResourceInstSug resourceInstSug left join fetch resourceInstSug.resourceSuggesteds",
        countQuery = "select count(distinct resourceInstSug) from ResourceInstSug resourceInstSug")
    Page<ResourceInstSug> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct resourceInstSug from ResourceInstSug resourceInstSug left join fetch resourceInstSug.resourceSuggesteds")
    List<ResourceInstSug> findAllWithEagerRelationships();

    @Query("select resourceInstSug from ResourceInstSug resourceInstSug left join fetch resourceInstSug.resourceSuggesteds where resourceInstSug.id =:id")
    Optional<ResourceInstSug> findOneWithEagerRelationships(@Param("id") Long id);

}
