package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.BranchANDCon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/** Spring Data repository for the BranchANDCon entity. */
@Repository
public interface BranchANDConRepository extends JpaRepository<BranchANDCon, Long> {

  @Query(
      value =
          "select distinct branchANDCon from BranchANDCon branchANDCon left join fetch branchANDCon.toMultipleCons",
      countQuery = "select count(distinct branchANDCon) from BranchANDCon branchANDCon")
  Page<BranchANDCon> findAllWithEagerRelationships(Pageable pageable);

  @Query(
      "select distinct branchANDCon from BranchANDCon branchANDCon left join fetch branchANDCon.toMultipleCons")
  List<BranchANDCon> findAllWithEagerRelationships();

  @Query(
      "select branchANDCon from BranchANDCon branchANDCon left join fetch branchANDCon.toMultipleCons where branchANDCon.id =:id")
  Optional<BranchANDCon> findOneWithEagerRelationships(@Param("id") Long id);
}
