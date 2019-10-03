package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.AbilityTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link AbilityType} and its DTO {@link AbilityTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AbilityTypeMapper extends EntityMapper<AbilityTypeDTO, AbilityType> {


    @Mapping(target = "theAbilities", ignore = true)
    @Mapping(target = "removeTheAbility", ignore = true)
    AbilityType toEntity(AbilityTypeDTO abilityTypeDTO);

    default AbilityType fromId(Long id) {
        if (id == null) {
            return null;
        }
        AbilityType abilityType = new AbilityType();
        abilityType.setId(id);
        return abilityType;
    }
}
