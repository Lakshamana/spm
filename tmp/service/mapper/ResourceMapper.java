package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ResourceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Resource} and its DTO {@link ResourceDTO}.
 */
@Mapper(componentModel = "spring", uses = {ResourceTypeMapper.class})
public interface ResourceMapper extends EntityMapper<ResourceDTO, Resource> {

    @Mapping(source = "belongsTo.id", target = "belongsToId")
    @Mapping(source = "requires.id", target = "requiresId")
    @Mapping(source = "theResourceType.id", target = "theResourceTypeId")
    ResourceDTO toDto(Resource resource);

    @Mapping(source = "belongsToId", target = "belongsTo")
    @Mapping(source = "requiresId", target = "requires")
    @Mapping(source = "theResourceTypeId", target = "theResourceType")
    @Mapping(target = "theResourceEvents", ignore = true)
    @Mapping(target = "removeTheResourceEvent", ignore = true)
    @Mapping(target = "theRequiredResources", ignore = true)
    @Mapping(target = "removeTheRequiredResource", ignore = true)
    @Mapping(target = "theResourceChosenSuggestions", ignore = true)
    @Mapping(target = "removeTheResourceChosenSuggestions", ignore = true)
    @Mapping(target = "theResourcePossibleUses", ignore = true)
    @Mapping(target = "removeTheResourcePossibleUse", ignore = true)
    @Mapping(target = "theResourceMetrics", ignore = true)
    @Mapping(target = "removeTheResourceMetric", ignore = true)
    @Mapping(target = "theResourceEstimations", ignore = true)
    @Mapping(target = "removeTheResourceEstimation", ignore = true)
    @Mapping(target = "possesses", ignore = true)
    @Mapping(target = "removePossess", ignore = true)
    @Mapping(target = "isRequireds", ignore = true)
    @Mapping(target = "removeIsRequired", ignore = true)
    @Mapping(target = "theResourceSuggestions", ignore = true)
    @Mapping(target = "removeTheResourceSuggestions", ignore = true)
    Resource toEntity(ResourceDTO resourceDTO);

    default Resource fromId(Long id) {
        if (id == null) {
            return null;
        }
        Resource resource = new Resource();
        resource.setId(id);
        return resource;
    }
}
