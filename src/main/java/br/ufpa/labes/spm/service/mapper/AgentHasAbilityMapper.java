package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.AgentHasAbilityDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link AgentHasAbility} and its DTO {@link AgentHasAbilityDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {AgentMapper.class, AbilityMapper.class})
public interface AgentHasAbilityMapper extends EntityMapper<AgentHasAbilityDTO, AgentHasAbility> {

  @Mapping(source = "theAgent.id", target = "theAgentId")
  @Mapping(source = "theAbility.id", target = "theAbilityId")
  AgentHasAbilityDTO toDto(AgentHasAbility agentHasAbility);

  @Mapping(source = "theAgentId", target = "theAgent")
  @Mapping(source = "theAbilityId", target = "theAbility")
  AgentHasAbility toEntity(AgentHasAbilityDTO agentHasAbilityDTO);

  default AgentHasAbility fromId(Long id) {
    if (id == null) {
      return null;
    }
    AgentHasAbility agentHasAbility = new AgentHasAbility();
    agentHasAbility.setId(id);
    return agentHasAbility;
  }
}
