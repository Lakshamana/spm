package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.AgentInstSugDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link AgentInstSug} and its DTO {@link AgentInstSugDTO}.
 */
@Mapper(componentModel = "spring", uses = {RoleMapper.class, AgentMapper.class})
public interface AgentInstSugMapper extends EntityMapper<AgentInstSugDTO, AgentInstSug> {

    @Mapping(source = "theRole.id", target = "theRoleId")
    @Mapping(source = "chosenAgent.id", target = "chosenAgentId")
    AgentInstSugDTO toDto(AgentInstSug agentInstSug);

    @Mapping(source = "theRoleId", target = "theRole")
    @Mapping(source = "chosenAgentId", target = "chosenAgent")
    @Mapping(target = "agentSuggesteds", ignore = true)
    @Mapping(target = "removeAgentSuggested", ignore = true)
    AgentInstSug toEntity(AgentInstSugDTO agentInstSugDTO);

    default AgentInstSug fromId(Long id) {
        if (id == null) {
            return null;
        }
        AgentInstSug agentInstSug = new AgentInstSug();
        agentInstSug.setId(id);
        return agentInstSug;
    }
}
