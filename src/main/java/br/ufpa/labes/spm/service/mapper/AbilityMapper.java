package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.AbilityDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ability} and its DTO {@link AbilityDTO}.
 */
@Mapper(componentModel = "spring", uses = {AbilityTypeMapper.class})
public interface AbilityMapper extends EntityMapper<AbilityDTO, Ability> {

    @Mapping(source = "theAbilityType.id", target = "theAbilityTypeId")
    AbilityDTO toDto(Ability ability);

    @Mapping(target = "theReqAgentRequiresAbilities", ignore = true)
    @Mapping(target = "removeTheReqAgentRequiresAbility", ignore = true)
    @Mapping(target = "theAgentHasAbilities", ignore = true)
    @Mapping(target = "removeTheAgentHasAbility", ignore = true)
    @Mapping(source = "theAbilityTypeId", target = "theAbilityType")
    @Mapping(target = "theRoleNeedsAbilities", ignore = true)
    @Mapping(target = "removeTheRoleNeedsAbility", ignore = true)
    Ability toEntity(AbilityDTO abilityDTO);

    default Ability fromId(Long id) {
        if (id == null) {
            return null;
        }
        Ability ability = new Ability();
        ability.setId(id);
        return ability;
    }
}
