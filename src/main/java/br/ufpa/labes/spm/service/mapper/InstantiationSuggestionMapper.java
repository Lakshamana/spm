package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.InstantiationSuggestionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link InstantiationSuggestion} and its DTO {@link
 * InstantiationSuggestionDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
      PeopleInstSugMapper.class,
      ActivityInstantiatedMapper.class,
      ResourceMapper.class,
      TypeMapper.class
    })
public interface InstantiationSuggestionMapper
    extends EntityMapper<InstantiationSuggestionDTO, InstantiationSuggestion> {

  @Mapping(source = "thePeopleInstSugSub.id", target = "thePeopleInstSugSubId")
  @Mapping(source = "theActivityInstantiated.id", target = "theActivityInstantiatedId")
  @Mapping(source = "chosenResource.id", target = "chosenResourceId")
  @Mapping(source = "requiredResourceType.id", target = "requiredResourceTypeId")
  InstantiationSuggestionDTO toDto(InstantiationSuggestion instantiationSuggestion);

  @Mapping(source = "thePeopleInstSugSubId", target = "thePeopleInstSugSub")
  @Mapping(source = "theActivityInstantiatedId", target = "theActivityInstantiated")
  @Mapping(source = "chosenResourceId", target = "chosenResource")
  @Mapping(source = "requiredResourceTypeId", target = "requiredResourceType")
  @Mapping(target = "removeSugRsrc", ignore = true)
  InstantiationSuggestion toEntity(InstantiationSuggestionDTO instantiationSuggestionDTO);

  default InstantiationSuggestion fromId(Long id) {
    if (id == null) {
      return null;
    }
    InstantiationSuggestion instantiationSuggestion = new InstantiationSuggestion();
    instantiationSuggestion.setId(id);
    return instantiationSuggestion;
  }
}
