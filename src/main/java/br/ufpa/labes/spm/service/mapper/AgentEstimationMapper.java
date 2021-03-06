package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.AgentEstimationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link AgentEstimation} and its DTO {@link AgentEstimationDTO}.
 */
@Mapper(componentModel = "spring", uses = {AgentMapper.class})
public interface AgentEstimationMapper extends EntityMapper<AgentEstimationDTO, AgentEstimation> {

    @Mapping(source = "agent.id", target = "agentId")
    AgentEstimationDTO toDto(AgentEstimation agentEstimation);

    @Mapping(source = "agentId", target = "agent")
    AgentEstimation toEntity(AgentEstimationDTO agentEstimationDTO);

    default AgentEstimation fromId(Long id) {
        if (id == null) {
            return null;
        }
        AgentEstimation agentEstimation = new AgentEstimation();
        agentEstimation.setId(id);
        return agentEstimation;
    }
}
