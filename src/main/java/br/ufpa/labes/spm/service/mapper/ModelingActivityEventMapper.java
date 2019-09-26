package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ModelingActivityEventDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ModelingActivityEvent} and its DTO {@link ModelingActivityEventDTO}.
 */
@Mapper(componentModel = "spring", uses = {ActivityMapper.class, AgentMapper.class})
public interface ModelingActivityEventMapper extends EntityMapper<ModelingActivityEventDTO, ModelingActivityEvent> {

    @Mapping(source = "theActivity.id", target = "theActivityId")
    @Mapping(source = "theAgent.id", target = "theAgentId")
    ModelingActivityEventDTO toDto(ModelingActivityEvent modelingActivityEvent);

    @Mapping(source = "theActivityId", target = "theActivity")
    @Mapping(source = "theAgentId", target = "theAgent")
    @Mapping(target = "theCatalogEvents", ignore = true)
    @Mapping(target = "removeTheCatalogEvents", ignore = true)
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
