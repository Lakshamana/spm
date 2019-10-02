package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.NotWorkingDayDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link NotWorkingDay} and its DTO {@link NotWorkingDayDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {CalendarMapper.class})
public interface NotWorkingDayMapper extends EntityMapper<NotWorkingDayDTO, NotWorkingDay> {

  @Mapping(source = "calendar.id", target = "calendarId")
  NotWorkingDayDTO toDto(NotWorkingDay notWorkingDay);

  @Mapping(source = "calendarId", target = "calendar")
  NotWorkingDay toEntity(NotWorkingDayDTO notWorkingDayDTO);

  default NotWorkingDay fromId(Long id) {
    if (id == null) {
      return null;
    }
    NotWorkingDay notWorkingDay = new NotWorkingDay();
    notWorkingDay.setId(id);
    return notWorkingDay;
  }
}
