package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.ArtifactCon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/** Spring Data repository for the ArtifactCon entity. */
@Repository
public interface ArtifactConRepository extends JpaRepository<ArtifactCon, Long> {

  @Query(
      value =
          "select distinct artifactCon from ArtifactCon artifactCon left join fetch artifactCon.toMultipleCons",
      countQuery = "select count(distinct artifactCon) from ArtifactCon artifactCon")
  Page<ArtifactCon> findAllWithEagerRelationships(Pageable pageable);

  @Query(
      "select distinct artifactCon from ArtifactCon artifactCon left join fetch artifactCon.toMultipleCons")
  List<ArtifactCon> findAllWithEagerRelationships();

  @Query(
      "select artifactCon from ArtifactCon artifactCon left join fetch artifactCon.toMultipleCons where artifactCon.id =:id")
  Optional<ArtifactCon> findOneWithEagerRelationships(@Param("id") Long id);
}
