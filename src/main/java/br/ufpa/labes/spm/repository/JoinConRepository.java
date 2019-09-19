package br.ufpa.labes.spm.repository;


import br.ufpa.labes.spm.domain.JoinCon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the JoinCon entity.
 */
@Repository
public interface JoinConRepository extends JpaRepository<JoinCon, Long> {

    @Query(value = "select distinct joinCon from JoinCon joinCon left join fetch joinCon.fromMultipleCons",
        countQuery = "select count(distinct joinCon) from JoinCon joinCon")
    Page<JoinCon> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct joinCon from JoinCon joinCon left join fetch joinCon.fromMultipleCons")
    List<JoinCon> findAllWithEagerRelationships();

    @Query("select joinCon from JoinCon joinCon left join fetch joinCon.fromMultipleCons where joinCon.id =:id")
    Optional<JoinCon> findOneWithEagerRelationships(@Param("id") Long id);

}
