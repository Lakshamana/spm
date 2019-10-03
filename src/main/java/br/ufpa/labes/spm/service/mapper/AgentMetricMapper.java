package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.AgentMetricDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link AgentMetric} and its DTO {@link AgentMetricDTO}.
 */
@Mapper(componentModel = "spring", uses = {AgentMapper.class})
public interface AgentMetricMapper extends EntityMapper<AgentMetricDTO, AgentMetric> {

    @Mapping(source = "agent.id", target = "agentId")
    AgentMetricDTO toDto(AgentMetric agentMetric);

    @Mapping(source = "agentId", target = "agent")
    AgentMetric toEntity(AgentMetricDTO agentMetricDTO);

    default AgentMetric fromId(Long id) {
        if (id == null) {
            return null;
        }
        AgentMetric agentMetric = new AgentMetric();
        agentMetric.setId(id);
        return agentMetric;
    }
}
