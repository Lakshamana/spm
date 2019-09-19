package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.RoleDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Role} and its DTO {@link RoleDTO}.
 */
@Mapper(componentModel = "spring", uses = {RoleTypeMapper.class})
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {

    @Mapping(source = "subordinate.id", target = "subordinateId")
    @Mapping(source = "theRoleType.id", target = "theRoleTypeId")
    RoleDTO toDto(Role role);

    @Mapping(target = "theReqAgents", ignore = true)
    @Mapping(target = "removeTheReqAgent", ignore = true)
    @Mapping(source = "subordinateId", target = "subordinate")
    @Mapping(source = "theRoleTypeId", target = "theRoleType")
    @Mapping(target = "theAgentPlaysRoles", ignore = true)
    @Mapping(target = "removeTheAgentPlaysRole", ignore = true)
    @Mapping(target = "theRoles", ignore = true)
    @Mapping(target = "removeTheRole", ignore = true)
    @Mapping(target = "theRoleNeedsAbilities", ignore = true)
    @Mapping(target = "removeTheRoleNeedsAbility", ignore = true)
    @Mapping(target = "theAgentInstSugs", ignore = true)
    @Mapping(target = "removeTheAgentInstSug", ignore = true)
    Role toEntity(RoleDTO roleDTO);

    default Role fromId(Long id) {
        if (id == null) {
            return null;
        }
        Role role = new Role();
        role.setId(id);
        return role;
    }
}
