package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CalendarsDTO implements Serializable {

/**
	 *
	 */
	private static final long serialVersionUID = 1L;
private List<CalendarDTO> calendars;

	public CalendarsDTO() {}

	public CalendarsDTO(List<CalendarDTO> calendars) {
		this.calendars = calendars;
	}

	public boolean addCalendar(CalendarDTO calendarDTO) {
		return this.calendars.add(calendarDTO);
	}

	public boolean addCalendarsDTO(CalendarsDTO calendarsDTO) {
		return this.calendars.addAll(calendarsDTO.getCalendars());
	}

	public boolean removeCalendar(CalendarDTO calendarDTO) {
		return this.calendars.remove(calendarDTO);
	}

	public CalendarDTO getCalendarDTO(int index) {
		return this.calendars.get(index);
	}

	public boolean isEmpty() {
		return this.calendars.isEmpty();
	}

	public int size() {
		return this.calendars.size();
	}

	public List<String> getCalendarsNames() {
		List<String> names = new ArrayList<String>();
		for (CalendarDTO calendarDTO : this.calendars) {
			names.add(calendarDTO.getName());
		}

		return names;
	}

	public List<CalendarDTO> getCalendars() {
		return calendars;
	}

}
