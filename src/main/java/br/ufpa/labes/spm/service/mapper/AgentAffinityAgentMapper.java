package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.AgentAffinityAgentDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link AgentAffinityAgent} and its DTO {@link AgentAffinityAgentDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {AgentMapper.class})
public interface AgentAffinityAgentMapper
    extends EntityMapper<AgentAffinityAgentDTO, AgentAffinityAgent> {

  @Mapping(source = "toAffinity.id", target = "toAffinityId")
  @Mapping(source = "fromAffinity.id", target = "fromAffinityId")
  AgentAffinityAgentDTO toDto(AgentAffinityAgent agentAffinityAgent);

  @Mapping(source = "toAffinityId", target = "toAffinity")
  @Mapping(source = "fromAffinityId", target = "fromAffinity")
  AgentAffinityAgent toEntity(AgentAffinityAgentDTO agentAffinityAgentDTO);

  default AgentAffinityAgent fromId(Long id) {
    if (id == null) {
      return null;
    }
    AgentAffinityAgent agentAffinityAgent = new AgentAffinityAgent();
    agentAffinityAgent.setId(id);
    return agentAffinityAgent;
  }
}
