package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.Subroutine;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Subroutine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubroutineRepository extends JpaRepository<Subroutine, Long> {

}
