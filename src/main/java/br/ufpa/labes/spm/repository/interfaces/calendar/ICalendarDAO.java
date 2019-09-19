package br.ufpa.labes.spm.repository.interfaces.calendar;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Calendar;

public interface ICalendarDAO extends IBaseDAO<Calendar, Long> {

  String validNameProject(String name, Integer project_Oid);
}
