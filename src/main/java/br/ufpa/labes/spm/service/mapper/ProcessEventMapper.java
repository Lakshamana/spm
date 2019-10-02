package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ProcessEventDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link ProcessEvent} and its DTO {@link ProcessEventDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {CatalogEventMapper.class, ProcessMapper.class})
public interface ProcessEventMapper extends EntityMapper<ProcessEventDTO, ProcessEvent> {

  @Mapping(source = "theCatalogEvent.id", target = "theCatalogEventId")
  @Mapping(source = "theProcess.id", target = "theProcessId")
  ProcessEventDTO toDto(ProcessEvent processEvent);

  @Mapping(source = "theCatalogEventId", target = "theCatalogEvent")
  @Mapping(source = "theProcessId", target = "theProcess")
  ProcessEvent toEntity(ProcessEventDTO processEventDTO);

  default ProcessEvent fromId(Long id) {
    if (id == null) {
      return null;
    }
    ProcessEvent processEvent = new ProcessEvent();
    processEvent.setId(id);
    return processEvent;
  }
}
