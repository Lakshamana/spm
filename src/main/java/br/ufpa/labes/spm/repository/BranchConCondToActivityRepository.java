package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.connections.IBranchConCondToActivityDAO;


import br.ufpa.labes.spm.domain.BranchConCondToActivity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the BranchConCondToActivity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BranchConCondToActivityRepository extends IBranchConCondToActivityDAO, JpaRepository<BranchConCondToActivity, Long> {

}
