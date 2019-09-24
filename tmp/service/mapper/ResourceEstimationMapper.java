package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ResourceEstimationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ResourceEstimation} and its DTO {@link ResourceEstimationDTO}.
 */
@Mapper(componentModel = "spring", uses = {ResourceMapper.class})
public interface ResourceEstimationMapper extends EntityMapper<ResourceEstimationDTO, ResourceEstimation> {

    @Mapping(source = "theResource.id", target = "theResourceId")
    ResourceEstimationDTO toDto(ResourceEstimation resourceEstimation);

    @Mapping(source = "theResourceId", target = "theResource")
    ResourceEstimation toEntity(ResourceEstimationDTO resourceEstimationDTO);

    default ResourceEstimation fromId(Long id) {
        if (id == null) {
            return null;
        }
        ResourceEstimation resourceEstimation = new ResourceEstimation();
        resourceEstimation.setId(id);
        return resourceEstimation;
    }
}
