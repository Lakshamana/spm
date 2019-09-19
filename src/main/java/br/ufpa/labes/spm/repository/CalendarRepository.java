package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.calendar.ICalendarDAO;


import br.ufpa.labes.spm.domain.Calendar;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Calendar entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CalendarRepository extends ICalendarDAO, JpaRepository<Calendar, Long> {

}
