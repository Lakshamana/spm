package br.ufpa.labes.spm.repository.interfaces.calendar;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Calendar;

@Local
public interface ICalendarDAO extends IBaseDAO<Calendar, String>{

	String  validNameProject(String name, Integer project_Oid);




}
