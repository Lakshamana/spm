package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.ActivityMetric;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/** Spring Data repository for the ActivityMetric entity. */
@SuppressWarnings("unused")
@Repository
public interface ActivityMetricRepository extends JpaRepository<ActivityMetric, Long> {}
