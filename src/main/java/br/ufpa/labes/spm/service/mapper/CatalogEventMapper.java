package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.CatalogEventDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link CatalogEvent} and its DTO {@link CatalogEventDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {
      AgendaEventMapper.class,
      ConnectionEventMapper.class,
      GlobalActivityEventMapper.class,
      ModelingActivityEventMapper.class,
      ProcessEventMapper.class,
      ProcessModelEventMapper.class,
      ResourceEventMapper.class,
      PlainMapper.class
    })
public interface CatalogEventMapper extends EntityMapper<CatalogEventDTO, CatalogEvent> {

  @Mapping(source = "theAgendaEvent.id", target = "theAgendaEventId")
  @Mapping(source = "theCatalogEvent.id", target = "theCatalogEventId")
  @Mapping(source = "theConnectionEvent.id", target = "theConnectionEventId")
  @Mapping(source = "theGlobalActivityEvent.id", target = "theGlobalActivityEventId")
  @Mapping(source = "theModelingActivityEvent.id", target = "theModelingActivityEventId")
  @Mapping(source = "theProcessEvent.id", target = "theProcessEventId")
  @Mapping(source = "theProcessModelEvent.id", target = "theProcessModelEventId")
  @Mapping(source = "theResourceEvent.id", target = "theResourceEventId")
  @Mapping(source = "thePlain.id", target = "thePlainId")
  CatalogEventDTO toDto(CatalogEvent catalogEvent);

  @Mapping(source = "theAgendaEventId", target = "theAgendaEvent")
  @Mapping(source = "theCatalogEventId", target = "theCatalogEvent")
  @Mapping(source = "theConnectionEventId", target = "theConnectionEvent")
  @Mapping(source = "theGlobalActivityEventId", target = "theGlobalActivityEvent")
  @Mapping(source = "theModelingActivityEventId", target = "theModelingActivityEvent")
  @Mapping(source = "theProcessEventId", target = "theProcessEvent")
  @Mapping(source = "theProcessModelEventId", target = "theProcessModelEvent")
  @Mapping(source = "theResourceEventId", target = "theResourceEvent")
  @Mapping(source = "thePlainId", target = "thePlain")
  @Mapping(target = "theEventSuper", ignore = true)
  @Mapping(target = "theAgendaEvents", ignore = true)
  @Mapping(target = "removeTheAgendaEvent", ignore = true)
  @Mapping(target = "theCatalogEventToCatalogs", ignore = true)
  @Mapping(target = "removeTheCatalogEventToCatalog", ignore = true)
  CatalogEvent toEntity(CatalogEventDTO catalogEventDTO);

  default CatalogEvent fromId(Long id) {
    if (id == null) {
      return null;
    }
    CatalogEvent catalogEvent = new CatalogEvent();
    catalogEvent.setId(id);
    return catalogEvent;
  }
}
