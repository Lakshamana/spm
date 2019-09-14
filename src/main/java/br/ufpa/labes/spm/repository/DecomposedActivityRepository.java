package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.DecomposedActivity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DecomposedActivity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DecomposedActivityRepository extends JpaRepository<DecomposedActivity, Long> {

}
