package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ResourcePossibleUseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ResourcePossibleUse} and its DTO {@link ResourcePossibleUseDTO}.
 */
@Mapper(componentModel = "spring", uses = {ResourceMapper.class})
public interface ResourcePossibleUseMapper extends EntityMapper<ResourcePossibleUseDTO, ResourcePossibleUse> {

    @Mapping(source = "theResource.id", target = "theResourceId")
    ResourcePossibleUseDTO toDto(ResourcePossibleUse resourcePossibleUse);

    @Mapping(source = "theResourceId", target = "theResource")
    ResourcePossibleUse toEntity(ResourcePossibleUseDTO resourcePossibleUseDTO);

    default ResourcePossibleUse fromId(Long id) {
        if (id == null) {
            return null;
        }
        ResourcePossibleUse resourcePossibleUse = new ResourcePossibleUse();
        resourcePossibleUse.setId(id);
        return resourcePossibleUse;
    }
}
