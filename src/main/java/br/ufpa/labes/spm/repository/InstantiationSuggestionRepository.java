package br.ufpa.labes.spm.repository;


import br.ufpa.labes.spm.domain.InstantiationSuggestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the InstantiationSuggestion entity.
 */
@Repository
public interface InstantiationSuggestionRepository extends JpaRepository<InstantiationSuggestion, Long> {

    @Query(value = "select distinct instantiationSuggestion from InstantiationSuggestion instantiationSuggestion left join fetch instantiationSuggestion.sugRsrcs",
        countQuery = "select count(distinct instantiationSuggestion) from InstantiationSuggestion instantiationSuggestion")
    Page<InstantiationSuggestion> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct instantiationSuggestion from InstantiationSuggestion instantiationSuggestion left join fetch instantiationSuggestion.sugRsrcs")
    List<InstantiationSuggestion> findAllWithEagerRelationships();

    @Query("select instantiationSuggestion from InstantiationSuggestion instantiationSuggestion left join fetch instantiationSuggestion.sugRsrcs where instantiationSuggestion.id =:id")
    Optional<InstantiationSuggestion> findOneWithEagerRelationships(@Param("id") Long id);

}
