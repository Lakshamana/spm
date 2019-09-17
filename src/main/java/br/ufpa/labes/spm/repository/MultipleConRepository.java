package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.MultipleCon;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the MultipleCon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MultipleConRepository extends JpaRepository<MultipleCon, Long> {

}
