package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ProcessModelDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link ProcessModel} and its DTO {@link ProcessModelDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {TemplateMapper.class})
public interface ProcessModelMapper extends EntityMapper<ProcessModelDTO, ProcessModel> {

  @Mapping(source = "theOrigin.id", target = "theOriginId")
  ProcessModelDTO toDto(ProcessModel processModel);

  @Mapping(target = "theActivities", ignore = true)
  @Mapping(target = "removeTheActivity", ignore = true)
  @Mapping(target = "theProcess", ignore = true)
  @Mapping(source = "theOriginId", target = "theOrigin")
  @Mapping(target = "theConnections", ignore = true)
  @Mapping(target = "removeTheConnection", ignore = true)
  @Mapping(target = "theProcessModelEvents", ignore = true)
  @Mapping(target = "removeTheProcessModelEvent", ignore = true)
  ProcessModel toEntity(ProcessModelDTO processModelDTO);

  default ProcessModel fromId(Long id) {
    if (id == null) {
      return null;
    }
    ProcessModel processModel = new ProcessModel();
    processModel.setId(id);
    return processModel;
  }
}
