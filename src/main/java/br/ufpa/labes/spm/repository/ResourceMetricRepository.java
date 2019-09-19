package br.ufpa.labes.spm.repository;


import br.ufpa.labes.spm.domain.ResourceMetric;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ResourceMetric entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResourceMetricRepository extends JpaRepository<ResourceMetric, Long> {

}
