package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ResourceEventDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ResourceEvent} and its DTO {@link ResourceEventDTO}.
 */
@Mapper(componentModel = "spring", uses = {NormalMapper.class, CatalogEventMapper.class, ResourceMapper.class})
public interface ResourceEventMapper extends EntityMapper<ResourceEventDTO, ResourceEvent> {

    @Mapping(source = "theNormal.id", target = "theNormalId")
    @Mapping(source = "theCatalogEvent.id", target = "theCatalogEventId")
    @Mapping(source = "theResource.id", target = "theResourceId")
    ResourceEventDTO toDto(ResourceEvent resourceEvent);

    @Mapping(source = "theNormalId", target = "theNormal")
    @Mapping(source = "theCatalogEventId", target = "theCatalogEvent")
    @Mapping(source = "theResourceId", target = "theResource")
    @Mapping(target = "theRequestorAgents", ignore = true)
    @Mapping(target = "removeTheRequestorAgent", ignore = true)
    ResourceEvent toEntity(ResourceEventDTO resourceEventDTO);

    default ResourceEvent fromId(Long id) {
        if (id == null) {
            return null;
        }
        ResourceEvent resourceEvent = new ResourceEvent();
        resourceEvent.setId(id);
        return resourceEvent;
    }
}
