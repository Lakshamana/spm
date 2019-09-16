package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.PeopleInstSugDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link PeopleInstSug} and its DTO {@link PeopleInstSugDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {AgentInstSugMapper.class, WorkGroupInstSugMapper.class})
public interface PeopleInstSugMapper extends EntityMapper<PeopleInstSugDTO, PeopleInstSug> {

  @Mapping(source = "theAgentInstSug.id", target = "theAgentInstSugId")
  @Mapping(source = "theWorkGroupInstSug.id", target = "theWorkGroupInstSugId")
  PeopleInstSugDTO toDto(PeopleInstSug peopleInstSug);

  @Mapping(source = "theAgentInstSugId", target = "theAgentInstSug")
  @Mapping(source = "theWorkGroupInstSugId", target = "theWorkGroupInstSug")
  @Mapping(target = "theInstSugSuper", ignore = true)
  @Mapping(target = "theAgentInstSugToAgents", ignore = true)
  @Mapping(target = "removeTheAgentInstSugToAgent", ignore = true)
  PeopleInstSug toEntity(PeopleInstSugDTO peopleInstSugDTO);

  default PeopleInstSug fromId(Long id) {
    if (id == null) {
      return null;
    }
    PeopleInstSug peopleInstSug = new PeopleInstSug();
    peopleInstSug.setId(id);
    return peopleInstSug;
  }
}
