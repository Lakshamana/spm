package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.AgendaEventDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link AgendaEvent} and its DTO {@link AgendaEventDTO}.
 */
@Mapper(componentModel = "spring", uses = {CatalogEventMapper.class, TaskMapper.class, NormalMapper.class})
public interface AgendaEventMapper extends EntityMapper<AgendaEventDTO, AgendaEvent> {

    @Mapping(source = "theCatalogEvent.id", target = "theCatalogEventId")
    @Mapping(source = "theTask.id", target = "theTaskId")
    @Mapping(source = "theNormal.id", target = "theNormalId")
    AgendaEventDTO toDto(AgendaEvent agendaEvent);

    @Mapping(source = "theCatalogEventId", target = "theCatalogEvent")
    @Mapping(source = "theTaskId", target = "theTask")
    @Mapping(source = "theNormalId", target = "theNormal")
    AgendaEvent toEntity(AgendaEventDTO agendaEventDTO);

    default AgendaEvent fromId(Long id) {
        if (id == null) {
            return null;
        }
        AgendaEvent agendaEvent = new AgendaEvent();
        agendaEvent.setId(id);
        return agendaEvent;
    }
}
