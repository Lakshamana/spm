package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.BranchCondToActivity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the BranchCondToActivity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BranchCondToActivityRepository extends JpaRepository<BranchCondToActivity, Long> {

}
