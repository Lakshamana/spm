package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.BranchConCond;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the BranchConCond entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BranchConCondRepository extends JpaRepository<BranchConCond, Long> {

}
