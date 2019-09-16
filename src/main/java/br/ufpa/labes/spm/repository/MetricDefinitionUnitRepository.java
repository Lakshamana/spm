package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.MetricDefinitionUnit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/** Spring Data repository for the MetricDefinitionUnit entity. */
@SuppressWarnings("unused")
@Repository
public interface MetricDefinitionUnitRepository extends JpaRepository<MetricDefinitionUnit, Long> {}
