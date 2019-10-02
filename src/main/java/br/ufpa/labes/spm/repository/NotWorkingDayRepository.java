package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.NotWorkingDay;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/** Spring Data repository for the NotWorkingDay entity. */
@SuppressWarnings("unused")
@Repository
public interface NotWorkingDayRepository extends JpaRepository<NotWorkingDay, Long> {}
