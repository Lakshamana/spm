package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.AgentAffinityAgentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link AgentAffinityAgent} and its DTO {@link AgentAffinityAgentDTO}.
 */
@Mapper(componentModel = "spring", uses = {AgentMapper.class})
public interface AgentAffinityAgentMapper extends EntityMapper<AgentAffinityAgentDTO, AgentAffinityAgent> {

    @Mapping(source = "fromAffinity.id", target = "fromAffinityId")
    @Mapping(source = "toAffinity.id", target = "toAffinityId")
    AgentAffinityAgentDTO toDto(AgentAffinityAgent agentAffinityAgent);

    @Mapping(source = "fromAffinityId", target = "fromAffinity")
    @Mapping(source = "toAffinityId", target = "toAffinity")
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
