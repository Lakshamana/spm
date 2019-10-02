package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ResourceInstSugDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link ResourceInstSug} and its DTO {@link ResourceInstSugDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {ResourceMapper.class, ResourceTypeMapper.class})
public interface ResourceInstSugMapper extends EntityMapper<ResourceInstSugDTO, ResourceInstSug> {

  @Mapping(source = "resourceChosen.id", target = "resourceChosenId")
  @Mapping(source = "resourceTypeRequired.id", target = "resourceTypeRequiredId")
  ResourceInstSugDTO toDto(ResourceInstSug resourceInstSug);

  @Mapping(source = "resourceChosenId", target = "resourceChosen")
  @Mapping(source = "resourceTypeRequiredId", target = "resourceTypeRequired")
  @Mapping(target = "removeResourceSuggested", ignore = true)
  ResourceInstSug toEntity(ResourceInstSugDTO resourceInstSugDTO);

  default ResourceInstSug fromId(Long id) {
    if (id == null) {
      return null;
    }
    ResourceInstSug resourceInstSug = new ResourceInstSug();
    resourceInstSug.setId(id);
    return resourceInstSug;
  }
}
