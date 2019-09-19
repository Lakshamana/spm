package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.WorkGroupMetric;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the WorkGroupMetric entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkGroupMetricRepository extends JpaRepository<WorkGroupMetric, Long> {

}
