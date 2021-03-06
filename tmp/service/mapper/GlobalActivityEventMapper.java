package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.GlobalActivityEventDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link GlobalActivityEvent} and its DTO {@link GlobalActivityEventDTO}.
 */
@Mapper(componentModel = "spring", uses = {PlainMapper.class})
public interface GlobalActivityEventMapper extends EntityMapper<GlobalActivityEventDTO, GlobalActivityEvent> {

    @Mapping(source = "thePlain.id", target = "thePlainId")
    GlobalActivityEventDTO toDto(GlobalActivityEvent globalActivityEvent);

    @Mapping(source = "thePlainId", target = "thePlain")
    @Mapping(target = "theCatalogEvents", ignore = true)
    @Mapping(target = "removeTheCatalogEvents", ignore = true)
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
