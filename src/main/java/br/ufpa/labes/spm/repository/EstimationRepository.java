package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.Estimation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/** Spring Data repository for the Estimation entity. */
@SuppressWarnings("unused")
@Repository
public interface EstimationRepository extends JpaRepository<Estimation, Long> {}
