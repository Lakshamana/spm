package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.CalendarDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Calendar} and its DTO {@link CalendarDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CalendarMapper extends EntityMapper<CalendarDTO, Calendar> {


    @Mapping(target = "notWorkingDays", ignore = true)
    @Mapping(target = "removeNotWorkingDays", ignore = true)
    @Mapping(target = "projects", ignore = true)
    @Mapping(target = "removeProject", ignore = true)
    Calendar toEntity(CalendarDTO calendarDTO);

    default Calendar fromId(Long id) {
        if (id == null) {
            return null;
        }
        Calendar calendar = new Calendar();
        calendar.setId(id);
        return calendar;
    }
}
