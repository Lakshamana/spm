package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.AgentInstSuggestionToAgentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link AgentInstSuggestionToAgent} and its DTO {@link
 * AgentInstSuggestionToAgentDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {AgentInstSugMapper.class, AgentMapper.class})
public interface AgentInstSuggestionToAgentMapper
    extends EntityMapper<AgentInstSuggestionToAgentDTO, AgentInstSuggestionToAgent> {

  @Mapping(source = "theInstAgSugg.id", target = "theInstAgSuggId")
  @Mapping(source = "theAgent.id", target = "theAgentId")
  AgentInstSuggestionToAgentDTO toDto(AgentInstSuggestionToAgent agentInstSuggestionToAgent);

  @Mapping(source = "theInstAgSuggId", target = "theInstAgSugg")
  @Mapping(source = "theAgentId", target = "theAgent")
  AgentInstSuggestionToAgent toEntity(AgentInstSuggestionToAgentDTO agentInstSuggestionToAgentDTO);

  default AgentInstSuggestionToAgent fromId(Long id) {
    if (id == null) {
      return null;
    }
    AgentInstSuggestionToAgent agentInstSuggestionToAgent = new AgentInstSuggestionToAgent();
    agentInstSuggestionToAgent.setId(id);
    return agentInstSuggestionToAgent;
  }
}
