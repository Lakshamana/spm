package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.AutomaticActivity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AutomaticActivity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AutomaticActivityRepository extends JpaRepository<AutomaticActivity, Long> {

}
