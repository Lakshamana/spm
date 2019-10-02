package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.CatalogEventDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CatalogEvent} and its DTO {@link CatalogEventDTO}.
 */
@Mapper(componentModel = "spring", uses = {PlainMapper.class})
public interface CatalogEventMapper extends EntityMapper<CatalogEventDTO, CatalogEvent> {

    @Mapping(source = "thePlain.id", target = "thePlainId")
    CatalogEventDTO toDto(CatalogEvent catalogEvent);

    @Mapping(source = "thePlainId", target = "thePlain")
    @Mapping(target = "theResourceEvents", ignore = true)
    @Mapping(target = "removeTheResourceEvent", ignore = true)
    @Mapping(target = "theProcessModelEvents", ignore = true)
    @Mapping(target = "removeTheProcessModelEvent", ignore = true)
    @Mapping(target = "theAgendaEvents", ignore = true)
    @Mapping(target = "removeTheAgendaEvent", ignore = true)
    @Mapping(target = "theConnectionEvents", ignore = true)
    @Mapping(target = "removeTheConnectionEvent", ignore = true)
    @Mapping(target = "theGlobalActivityEvents", ignore = true)
    @Mapping(target = "removeTheGlobalActivityEvent", ignore = true)
    @Mapping(target = "theModelingActivityEvents", ignore = true)
    @Mapping(target = "removeTheModelingActivityEvent", ignore = true)
    @Mapping(target = "theProcessEvents", ignore = true)
    @Mapping(target = "removeTheProcessEvent", ignore = true)
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
