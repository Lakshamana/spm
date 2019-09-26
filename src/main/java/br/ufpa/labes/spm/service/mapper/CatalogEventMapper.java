package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.CatalogEventDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CatalogEvent} and its DTO {@link CatalogEventDTO}.
 */
@Mapper(componentModel = "spring", uses = {ResourceEventMapper.class, ProcessModelEventMapper.class, AgendaEventMapper.class, ConnectionEventMapper.class, GlobalActivityEventMapper.class, ModelingActivityEventMapper.class, ProcessEventMapper.class, PlainMapper.class})
public interface CatalogEventMapper extends EntityMapper<CatalogEventDTO, CatalogEvent> {

    @Mapping(source = "theResourceEvent.id", target = "theResourceEventId")
    @Mapping(source = "theProcessModelEvent.id", target = "theProcessModelEventId")
    @Mapping(source = "theAgendaEvent.id", target = "theAgendaEventId")
    @Mapping(source = "theCatalogEvent.id", target = "theCatalogEventId")
    @Mapping(source = "theConnectionEvent.id", target = "theConnectionEventId")
    @Mapping(source = "theGlobalActivityEvent.id", target = "theGlobalActivityEventId")
    @Mapping(source = "theModelingActivityEvent.id", target = "theModelingActivityEventId")
    @Mapping(source = "theProcessEvent.id", target = "theProcessEventId")
    @Mapping(source = "thePlain.id", target = "thePlainId")
    CatalogEventDTO toDto(CatalogEvent catalogEvent);

    @Mapping(source = "theResourceEventId", target = "theResourceEvent")
    @Mapping(source = "theProcessModelEventId", target = "theProcessModelEvent")
    @Mapping(source = "theAgendaEventId", target = "theAgendaEvent")
    @Mapping(source = "theCatalogEventId", target = "theCatalogEvent")
    @Mapping(source = "theConnectionEventId", target = "theConnectionEvent")
    @Mapping(source = "theGlobalActivityEventId", target = "theGlobalActivityEvent")
    @Mapping(source = "theModelingActivityEventId", target = "theModelingActivityEvent")
    @Mapping(source = "theProcessEventId", target = "theProcessEvent")
    @Mapping(source = "thePlainId", target = "thePlain")
    @Mapping(target = "theCatalogEvents", ignore = true)
    @Mapping(target = "removeTheCatalogEvents", ignore = true)
    @Mapping(target = "theAgendaEvents", ignore = true)
    @Mapping(target = "removeTheAgendaEvent", ignore = true)
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
