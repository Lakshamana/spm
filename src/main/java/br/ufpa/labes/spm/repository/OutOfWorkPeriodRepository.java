package br.ufpa.labes.spm.repository;


import br.ufpa.labes.spm.domain.OutOfWorkPeriod;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the OutOfWorkPeriod entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OutOfWorkPeriodRepository extends JpaRepository<OutOfWorkPeriod, Long> {

}
