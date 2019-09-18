package br.ufpa.labes.spm.service.impl;


import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.qrconsult.spm.converter.core.Converter;
import org.qrconsult.spm.converter.core.ConverterImpl;
import org.qrconsult.spm.converter.exception.ImplementationException;
import br.ufpa.labes.spm.repository.interfaces.calendar.ICalendarDAO;
import org.qrconsult.spm.dtos.calendar.CalendarDTO;
import org.qrconsult.spm.dtos.calendar.CalendarsDTO;
import org.qrconsult.spm.dtos.formProject.ProjectDTO;
import br.ufpa.labes.spm.domain.Calendar;
import br.ufpa.labes.spm.domain.Project;
import br.ufpa.labes.spm.service.interfaces.CalendarServices;

@Stateless
public class CalendarServicesImpl implements CalendarServices  {

	ICalendarDAO calendarDAO;
	private Query query;
	Converter converter = new ConverterImpl();
	private static final String CALENDAR_CLASSNAME = Calendar.class.getName();
	private Calendar calendar;
	private Project project;

	@Override
	public void saveCalendar(CalendarDTO calendarDTO, ProjectDTO projetoOid) {
	System.out.print("caiu no save");
		project = this.convertProjectDTOToPtoject(projetoOid);
		calendar =  new  Calendar();
		try {

			calendar = this.convertCalendarDTOToCalendar(calendarDTO);
			calendar.setProject(project);
			calendarDAO.save(calendar);
			calendar = null;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private Calendar convertCalendarDTOToCalendar(CalendarDTO calendarDTO) {
		try {

			Calendar calendar = new Calendar();
			calendar = (Calendar) converter.getEntity(calendarDTO, calendar);

			return calendar;

		} catch (ImplementationException e) {
			e.printStackTrace();
		}

		return null;
	}

	private Project convertProjectDTOToPtoject(ProjectDTO projectDTO) {
		try {

			Project project = new Project();
			project = (Project) converter.getEntity(projectDTO, project);

			return project;

		} catch (ImplementationException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void updateCalendar(CalendarDTO calendar, Integer projetoOid) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<CalendarDTO> searchAllProjectCalendar() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean deleteAgent(CalendarDTO calendar) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CalendarDTO searchCalendarProjectById(Integer calendarOid,
			Integer projectOid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CalendarDTO changeCalendarToPtoject(CalendarDTO calendar,
			Integer projectOid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String validNameProject(String name, Integer project_Oid) {

		  Query query = calendarDAO.getPersistenceContext().createQuery("SELECT calendar.name FROM " + Calendar.class.getName() + " AS calendar "
	                + "WHERE  calendar.project.oid like :project_Oid");



	        query.setParameter("project_Oid", project_Oid);

	        try{
	        return query.getSingleResult().toString();


	        }catch(Exception e){
	        e.printStackTrace();
	            return null;
	        }
	}

	@Override
	public CalendarsDTO getCalendarsForProject() {

		String hql_project = "SELECT ca FROM " + CALENDAR_CLASSNAME + " as ca)";

		query = calendarDAO.getPersistenceContext().createQuery(hql_project);

		List<Calendar> calendarList = query.getResultList();

		System.out.println("tamanho da lista"+calendarList.size());
		CalendarsDTO calendars = this.convertCalendarsToCalendarsDTO(calendarList);

		return calendars;
	}

	@Override
	public CalendarDTO getCalendarForProject(Integer project_oid) {
		CalendarDTO calendar = new CalendarDTO();
		String hql = "SELECT calendar FROM " + Calendar.class.getName() + " calendar where calendar.project.oid = '" + project_oid + "'";

		query = calendarDAO.getPersistenceContext().createQuery(hql);
		if(query.getSingleResult()==null){
			return null;

		}else{
			Calendar calendario = (Calendar) query.getSingleResult();
		    calendar = this.convertCalendarToCalendarDTO(calendario);
		}

		return calendar;
	}

	private CalendarsDTO convertCalendarsToCalendarsDTO(List<Calendar> calendars) {
		CalendarsDTO calendarsDTO = new CalendarsDTO(new ArrayList<CalendarDTO>());
		for (Calendar calendar : calendars) {
			CalendarDTO calendarDTO = this.convertCalendarToCalendarDTO(calendar);
			calendarsDTO.addCalendar(calendarDTO);
		}

		return calendarsDTO;
	}

	private CalendarDTO convertCalendarToCalendarDTO(Calendar calendar) {
		try {
			CalendarDTO calendarDTO = new CalendarDTO();
			calendarDTO = (CalendarDTO) converter.getDTO(calendar, CalendarDTO.class);

			return calendarDTO;
		} catch (ImplementationException e) {
			e.printStackTrace();
		}

		return null;
	}

}

