package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.EventDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Event} and its DTO {@link EventDTO}.
 */
@Mapper(componentModel = "spring", uses = {AgendaEventMapper.class, CatalogEventMapper.class, ConnectionEventMapper.class, GlobalActivityEventMapper.class, ModelingActivityEventMapper.class, ProcessEventMapper.class, ProcessModelEventMapper.class, ResourceEventMapper.class, TaskMapper.class, SpmLogMapper.class, EventTypeMapper.class, ActivityMapper.class, PlainMapper.class, AgentMapper.class})
public interface EventMapper extends EntityMapper<EventDTO, Event> {

    @Mapping(source = "theAgendaEventSub.id", target = "theAgendaEventSubId")
    @Mapping(source = "theCatalogEventSub.id", target = "theCatalogEventSubId")
    @Mapping(source = "theConnectionEventSub.id", target = "theConnectionEventSubId")
    @Mapping(source = "theGlobalActivityEventSub.id", target = "theGlobalActivityEventSubId")
    @Mapping(source = "theModelingActivityEventSub.id", target = "theModelingActivityEventSubId")
    @Mapping(source = "theProcessEventSub.id", target = "theProcessEventSubId")
    @Mapping(source = "theProcessModelEventSub.id", target = "theProcessModelEventSubId")
    @Mapping(source = "theResourceEventSub.id", target = "theResourceEventSubId")
    @Mapping(source = "theCatalogEvents.id", target = "theCatalogEventsId")
    @Mapping(source = "theTask.id", target = "theTaskId")
    @Mapping(source = "theLog.id", target = "theLogId")
    @Mapping(source = "theEventType.id", target = "theEventTypeId")
    @Mapping(source = "theActivity.id", target = "theActivityId")
    @Mapping(source = "thePlain.id", target = "thePlainId")
    @Mapping(source = "theAgent.id", target = "theAgentId")
    EventDTO toDto(Event event);

    @Mapping(source = "theAgendaEventSubId", target = "theAgendaEventSub")
    @Mapping(source = "theCatalogEventSubId", target = "theCatalogEventSub")
    @Mapping(source = "theConnectionEventSubId", target = "theConnectionEventSub")
    @Mapping(source = "theGlobalActivityEventSubId", target = "theGlobalActivityEventSub")
    @Mapping(source = "theModelingActivityEventSubId", target = "theModelingActivityEventSub")
    @Mapping(source = "theProcessEventSubId", target = "theProcessEventSub")
    @Mapping(source = "theProcessModelEventSubId", target = "theProcessModelEventSub")
    @Mapping(source = "theResourceEventSubId", target = "theResourceEventSub")
    @Mapping(source = "theCatalogEventsId", target = "theCatalogEvents")
    @Mapping(source = "theTaskId", target = "theTask")
    @Mapping(source = "theLogId", target = "theLog")
    @Mapping(source = "theEventTypeId", target = "theEventType")
    @Mapping(source = "theActivityId", target = "theActivity")
    @Mapping(source = "thePlainId", target = "thePlain")
    @Mapping(source = "theAgentId", target = "theAgent")
    @Mapping(target = "theRequestorAgents", ignore = true)
    @Mapping(target = "removeTheRequestorAgent", ignore = true)
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
