package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.GlobalActivityEventDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link GlobalActivityEvent} and its DTO {@link GlobalActivityEventDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {})
public interface GlobalActivityEventMapper
    extends EntityMapper<GlobalActivityEventDTO, GlobalActivityEvent> {

  @Mapping(target = "theEventSuper", ignore = true)
  @Mapping(target = "theCatalogEventToGlobalActivities", ignore = true)
  @Mapping(target = "removeTheCatalogEventToGlobalActivity", ignore = true)
  GlobalActivityEvent toEntity(GlobalActivityEventDTO globalActivityEventDTO);

  default GlobalActivityEvent fromId(Long id) {
    if (id == null) {
      return null;
    }
    GlobalActivityEvent globalActivityEvent = new GlobalActivityEvent();
    globalActivityEvent.setId(id);
    return globalActivityEvent;
  }
}
