package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.CalendarDayDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CalendarDay} and its DTO {@link CalendarDayDTO}.
 */
@Mapper(componentModel = "spring", uses = {CalendarMapper.class})
public interface CalendarDayMapper extends EntityMapper<CalendarDayDTO, CalendarDay> {

    @Mapping(source = "theCalendar.id", target = "theCalendarId")
    CalendarDayDTO toDto(CalendarDay calendarDay);

    @Mapping(source = "theCalendarId", target = "theCalendar")
    CalendarDay toEntity(CalendarDayDTO calendarDayDTO);

    default CalendarDay fromId(Long id) {
        if (id == null) {
            return null;
        }
        CalendarDay calendarDay = new CalendarDay();
        calendarDay.setId(id);
        return calendarDay;
    }
}
