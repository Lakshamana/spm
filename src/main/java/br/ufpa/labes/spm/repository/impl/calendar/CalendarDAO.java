package br.ufpa.labes.spm.repository.impl.calendar;




import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.calendar.ICalendarDAO;
import br.ufpa.labes.spm.domain.Calendar;



public class CalendarDAO extends BaseDAO<Calendar, String> implements ICalendarDAO {



	protected CalendarDAO(Class<Calendar> businessClass) {
		super(businessClass);
	}

	public CalendarDAO() {
		super(Calendar.class);
	}

	@Override
	public String validNameProject(String name, Integer project_Oid) {
		// TODO Auto-generated method stub
		return null;
	}





	}




