package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.EventDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Event} and its DTO {@link EventDTO}.
 */
@Mapper(componentModel = "spring", uses = {CatalogEventMapper.class, SpmLogMapper.class, EventTypeMapper.class})
public interface EventMapper extends EntityMapper<EventDTO, Event> {

    @Mapping(source = "theCatalogEvents.id", target = "theCatalogEventsId")
    @Mapping(source = "theLog.id", target = "theLogId")
    @Mapping(source = "theEventType.id", target = "theEventTypeId")
    EventDTO toDto(Event event);

    @Mapping(source = "theCatalogEventsId", target = "theCatalogEvents")
    @Mapping(source = "theLogId", target = "theLog")
    @Mapping(source = "theEventTypeId", target = "theEventType")
    Event toEntity(EventDTO eventDTO);

    default Event fromId(Long id) {
        if (id == null) {
            return null;
        }
        Event event = new Event();
        event.setId(id);
        return event;
    }
}
