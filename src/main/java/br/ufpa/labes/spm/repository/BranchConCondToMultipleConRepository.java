package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.connections.IBranchConCondToMultipleConDAO;


import br.ufpa.labes.spm.domain.BranchConCondToMultipleCon;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the BranchConCondToMultipleCon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BranchConCondToMultipleConRepository extends IBranchConCondToMultipleConDAO, JpaRepository<BranchConCondToMultipleCon, Long> {

}
