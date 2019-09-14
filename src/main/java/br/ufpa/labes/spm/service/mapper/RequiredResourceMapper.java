package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.RequiredResourceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link RequiredResource} and its DTO {@link RequiredResourceDTO}.
 */
@Mapper(componentModel = "spring", uses = {NormalMapper.class, ResourceTypeMapper.class, ResourceMapper.class})
public interface RequiredResourceMapper extends EntityMapper<RequiredResourceDTO, RequiredResource> {

    @Mapping(source = "theNormal.id", target = "theNormalId")
    @Mapping(source = "theResourceType.id", target = "theResourceTypeId")
    @Mapping(source = "theResource.id", target = "theResourceId")
    RequiredResourceDTO toDto(RequiredResource requiredResource);

    @Mapping(source = "theNormalId", target = "theNormal")
    @Mapping(source = "theResourceTypeId", target = "theResourceType")
    @Mapping(source = "theResourceId", target = "theResource")
    RequiredResource toEntity(RequiredResourceDTO requiredResourceDTO);

    default RequiredResource fromId(Long id) {
        if (id == null) {
            return null;
        }
        RequiredResource requiredResource = new RequiredResource();
        requiredResource.setId(id);
        return requiredResource;
    }
}
