package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ModelingActivityEventDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ModelingActivityEvent} and its DTO {@link ModelingActivityEventDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {})
public interface ModelingActivityEventMapper
    extends EntityMapper<ModelingActivityEventDTO, ModelingActivityEvent> {

  @Mapping(target = "theEventSuper", ignore = true)
  @Mapping(target = "theCatalogEventToModelingActivities", ignore = true)
  @Mapping(target = "removeTheCatalogEventToModelingActivity", ignore = true)
  ModelingActivityEvent toEntity(ModelingActivityEventDTO modelingActivityEventDTO);

  default ModelingActivityEvent fromId(Long id) {
    if (id == null) {
      return null;
    }
    ModelingActivityEvent modelingActivityEvent = new ModelingActivityEvent();
    modelingActivityEvent.setId(id);
    return modelingActivityEvent;
  }
}
