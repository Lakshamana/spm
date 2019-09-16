package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.EventTypeDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link EventType} and its DTO {@link EventTypeDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {})
public interface EventTypeMapper extends EntityMapper<EventTypeDTO, EventType> {

  @Mapping(target = "theTypeSuper", ignore = true)
  @Mapping(target = "theEvents", ignore = true)
  @Mapping(target = "removeTheEvent", ignore = true)
  EventType toEntity(EventTypeDTO eventTypeDTO);

  default EventType fromId(Long id) {
    if (id == null) {
      return null;
    }
    EventType eventType = new EventType();
    eventType.setId(id);
    return eventType;
  }
}
