package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ResourceTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ResourceType} and its DTO {@link ResourceTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ResourceTypeMapper extends EntityMapper<ResourceTypeDTO, ResourceType> {


    @Mapping(target = "theRequiredResources", ignore = true)
    @Mapping(target = "removeTheRequiredResource", ignore = true)
    @Mapping(target = "theResources", ignore = true)
    @Mapping(target = "removeTheResource", ignore = true)
    ResourceType toEntity(ResourceTypeDTO resourceTypeDTO);

    default ResourceType fromId(Long id) {
        if (id == null) {
            return null;
        }
        ResourceType resourceType = new ResourceType();
        resourceType.setId(id);
        return resourceType;
    }
}
