package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/** Spring Data repository for the Resource entity. */
@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

  @Query(
      value = "select distinct resource from Resource resource left join fetch resource.requires",
      countQuery = "select count(distinct resource) from Resource resource")
  Page<Resource> findAllWithEagerRelationships(Pageable pageable);

  @Query("select distinct resource from Resource resource left join fetch resource.requires")
  List<Resource> findAllWithEagerRelationships();

  @Query(
      "select resource from Resource resource left join fetch resource.requires where resource.id =:id")
  Optional<Resource> findOneWithEagerRelationships(@Param("id") Long id);
}
