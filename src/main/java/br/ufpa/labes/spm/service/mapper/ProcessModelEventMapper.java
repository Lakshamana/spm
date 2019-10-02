package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ProcessModelEventDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link ProcessModelEvent} and its DTO {@link ProcessModelEventDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {CatalogEventMapper.class, ProcessModelMapper.class})
public interface ProcessModelEventMapper
    extends EntityMapper<ProcessModelEventDTO, ProcessModelEvent> {

  @Mapping(source = "theCatalogEvent.id", target = "theCatalogEventId")
  @Mapping(source = "theProcessModel.id", target = "theProcessModelId")
  ProcessModelEventDTO toDto(ProcessModelEvent processModelEvent);

  @Mapping(source = "theCatalogEventId", target = "theCatalogEvent")
  @Mapping(source = "theProcessModelId", target = "theProcessModel")
  ProcessModelEvent toEntity(ProcessModelEventDTO processModelEventDTO);

  default ProcessModelEvent fromId(Long id) {
    if (id == null) {
      return null;
    }
    ProcessModelEvent processModelEvent = new ProcessModelEvent();
    processModelEvent.setId(id);
    return processModelEvent;
  }
}
