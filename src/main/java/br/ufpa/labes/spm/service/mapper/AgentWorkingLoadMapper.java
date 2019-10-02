package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.AgentWorkingLoadDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link AgentWorkingLoad} and its DTO {@link AgentWorkingLoadDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {AgentMapper.class})
public interface AgentWorkingLoadMapper
    extends EntityMapper<AgentWorkingLoadDTO, AgentWorkingLoad> {

  @Mapping(source = "theAgent.id", target = "theAgentId")
  AgentWorkingLoadDTO toDto(AgentWorkingLoad agentWorkingLoad);

  @Mapping(source = "theAgentId", target = "theAgent")
  AgentWorkingLoad toEntity(AgentWorkingLoadDTO agentWorkingLoadDTO);

  default AgentWorkingLoad fromId(Long id) {
    if (id == null) {
      return null;
    }
    AgentWorkingLoad agentWorkingLoad = new AgentWorkingLoad();
    agentWorkingLoad.setId(id);
    return agentWorkingLoad;
  }
}
