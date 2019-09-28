package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ReqAgentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReqAgent} and its DTO {@link ReqAgentDTO}.
 */
@Mapper(componentModel = "spring", uses = {AgentMapper.class, RoleMapper.class})
public interface ReqAgentMapper extends EntityMapper<ReqAgentDTO, ReqAgent> {

    @Mapping(source = "theAgent.id", target = "theAgentId")
    @Mapping(source = "theRole.id", target = "theRoleId")
    ReqAgentDTO toDto(ReqAgent reqAgent);

    @Mapping(source = "theAgentId", target = "theAgent")
    @Mapping(source = "theRoleId", target = "theRole")
    @Mapping(target = "theReqAgentRequiresAbilities", ignore = true)
    @Mapping(target = "removeTheReqAgentRequiresAbility", ignore = true)
    ReqAgent toEntity(ReqAgentDTO reqAgentDTO);

    default ReqAgent fromId(Long id) {
        if (id == null) {
            return null;
        }
        ReqAgent reqAgent = new ReqAgent();
        reqAgent.setId(id);
        return reqAgent;
    }
}
