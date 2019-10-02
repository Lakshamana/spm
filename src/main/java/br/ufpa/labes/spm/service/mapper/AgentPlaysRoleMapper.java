package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.AgentPlaysRoleDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link AgentPlaysRole} and its DTO {@link AgentPlaysRoleDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {AgentMapper.class, RoleMapper.class})
public interface AgentPlaysRoleMapper extends EntityMapper<AgentPlaysRoleDTO, AgentPlaysRole> {

  @Mapping(source = "theAgent.id", target = "theAgentId")
  @Mapping(source = "theRole.id", target = "theRoleId")
  AgentPlaysRoleDTO toDto(AgentPlaysRole agentPlaysRole);

  @Mapping(source = "theAgentId", target = "theAgent")
  @Mapping(source = "theRoleId", target = "theRole")
  AgentPlaysRole toEntity(AgentPlaysRoleDTO agentPlaysRoleDTO);

  default AgentPlaysRole fromId(Long id) {
    if (id == null) {
      return null;
    }
    AgentPlaysRole agentPlaysRole = new AgentPlaysRole();
    agentPlaysRole.setId(id);
    return agentPlaysRole;
  }
}
