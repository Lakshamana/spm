package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.ProcessMetric;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ProcessMetric entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcessMetricRepository extends JpaRepository<ProcessMetric, Long> {

}
