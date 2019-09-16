package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.OrganizationMetric;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/** Spring Data repository for the OrganizationMetric entity. */
@SuppressWarnings("unused")
@Repository
public interface OrganizationMetricRepository extends JpaRepository<OrganizationMetric, Long> {}
