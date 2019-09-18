package br.ufpa.labes.spm.service.interfaces;

import java.util.List;


import org.qrconsult.spm.dtos.calendar.CalendarDTO;
import org.qrconsult.spm.dtos.calendar.CalendarsDTO;
import org.qrconsult.spm.dtos.formProject.ProjectDTO;

@Remote
public interface CalendarServices {


	public void updateCalendar(CalendarDTO calendar,Integer projetoOid);

	public List<CalendarDTO> searchAllProjectCalendar();

   public Boolean deleteAgent(CalendarDTO calendar);

   public CalendarDTO searchCalendarProjectById(Integer calendarOid, Integer projectOid);

   public CalendarDTO changeCalendarToPtoject(CalendarDTO calendar, Integer projectOid);

void saveCalendar(CalendarDTO calendarDTO, ProjectDTO projetoOid);

public String validNameProject(String name, Integer project_Oid);

public CalendarsDTO getCalendarsForProject();

CalendarDTO getCalendarForProject(Integer project_oid);

}

