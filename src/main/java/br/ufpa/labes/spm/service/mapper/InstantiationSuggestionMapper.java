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
    uses = {ActivityInstantiatedMapper.class})
public interface InstantiationSuggestionMapper
    extends EntityMapper<InstantiationSuggestionDTO, InstantiationSuggestion> {

  @Mapping(source = "theActivityInstantiated.id", target = "theActivityInstantiatedId")
  InstantiationSuggestionDTO toDto(InstantiationSuggestion instantiationSuggestion);

  @Mapping(source = "theActivityInstantiatedId", target = "theActivityInstantiated")
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
