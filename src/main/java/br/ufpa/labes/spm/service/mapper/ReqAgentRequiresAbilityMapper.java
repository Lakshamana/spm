package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ReqAgentRequiresAbilityDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReqAgentRequiresAbility} and its DTO {@link ReqAgentRequiresAbilityDTO}.
 */
@Mapper(componentModel = "spring", uses = {ReqAgentMapper.class, AbilityMapper.class})
public interface ReqAgentRequiresAbilityMapper extends EntityMapper<ReqAgentRequiresAbilityDTO, ReqAgentRequiresAbility> {

    @Mapping(source = "theReqAgent.id", target = "theReqAgentId")
    @Mapping(source = "theAbility.id", target = "theAbilityId")
    ReqAgentRequiresAbilityDTO toDto(ReqAgentRequiresAbility reqAgentRequiresAbility);

    @Mapping(source = "theReqAgentId", target = "theReqAgent")
    @Mapping(source = "theAbilityId", target = "theAbility")
    ReqAgentRequiresAbility toEntity(ReqAgentRequiresAbilityDTO reqAgentRequiresAbilityDTO);

    default ReqAgentRequiresAbility fromId(Long id) {
        if (id == null) {
            return null;
        }
        ReqAgentRequiresAbility reqAgentRequiresAbility = new ReqAgentRequiresAbility();
        reqAgentRequiresAbility.setId(id);
        return reqAgentRequiresAbility;
    }
}
