package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.AgendaEventDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link AgendaEvent} and its DTO {@link AgendaEventDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {})
public interface AgendaEventMapper extends EntityMapper<AgendaEventDTO, AgendaEvent> {

  @Mapping(target = "theCatalogEventToAgenda", ignore = true)
  @Mapping(target = "removeTheCatalogEventToAgenda", ignore = true)
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
