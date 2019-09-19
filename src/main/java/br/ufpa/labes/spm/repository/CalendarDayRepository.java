package br.ufpa.labes.spm.repository;


import br.ufpa.labes.spm.domain.CalendarDay;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CalendarDay entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CalendarDayRepository extends JpaRepository<CalendarDay, Long> {

}
