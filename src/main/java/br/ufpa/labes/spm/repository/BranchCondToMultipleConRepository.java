package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.BranchCondToMultipleCon;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the BranchCondToMultipleCon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BranchCondToMultipleConRepository extends JpaRepository<BranchCondToMultipleCon, Long> {

}
